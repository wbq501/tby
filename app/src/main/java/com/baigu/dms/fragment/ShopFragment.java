package com.baigu.dms.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.activity.AddOrderActivity;
import com.baigu.dms.activity.GoodsDetailActivity;
import com.baigu.dms.activity.GoodsSearchActivity;
import com.baigu.dms.adapter.BaseRVAdapter;
import com.baigu.dms.adapter.GoodsAdapter;
import com.baigu.dms.adapter.GoodsCategoryAdater;
import com.baigu.dms.common.utils.ImageUtil;
import com.baigu.dms.common.utils.ItemDecoration;
import com.baigu.dms.common.utils.SPUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.utils.rxbus.EventType;
import com.baigu.dms.common.utils.rxbus.RxBus;
import com.baigu.dms.common.utils.rxbus.RxBusEvent;
import com.baigu.dms.common.utils.rxbus.Subscribe;
import com.baigu.dms.common.utils.rxbus.ThreadMode;
import com.baigu.dms.common.view.OnRVItemClickListener;
import com.baigu.dms.common.view.PaddingLeftItemDecoration;
import com.baigu.dms.common.view.ShopCardWindow;
import com.baigu.dms.common.view.filtermenu.ExpandFilterMenuView;
import com.baigu.dms.domain.cache.ShopCart;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.model.GoodsCategory;
import com.baigu.dms.domain.model.ShopAdverPictrue;
import com.baigu.dms.domain.model.Sku;
import com.baigu.dms.domain.netservice.common.model.GoodsResult;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.GoodsListPresenter;
import com.baigu.dms.presenter.impl.GoodsListPresenterImpl;
import com.baigu.lrecyclerview.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/24 9:54
 */
public class ShopFragment extends TabFragment implements GoodsListPresenter.GoodsListView, View.OnClickListener, GoodsAdapter.OnGoodsAmountChangeListener, OnRVItemClickListener {
    private RecyclerView mRvGoods;
    private RecyclerView mGoodsCategory;
    private GoodsAdapter mGoodsAdapter;
    private GoodsCategoryAdater mGoodsCategoryAdater;
    private GoodsListPresenter mGoodsListPresenter;
    private TextView mTvMoney;
    private TextView mTvSelectedOk;
    private ImageView adver;
    private LinearLayout shopLayout;
    LinearLayoutManager manager = null;
    List<String> categories;
    List<GoodsCategory> mCategory;
    List<Goods> mgoodsList;
    List<Integer> mTitleIntList;
    private String mCurTitle;
    private int mCurrPage = 1;
    private float alpha = 1f;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    changAlpha((float) msg.obj);
                    break;
            }
        }
    };

    private boolean isOnDown = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        mGoodsListPresenter = new GoodsListPresenterImpl((BaseActivity) getActivity(), this);
        initView(view);
        RxBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onTabChecked(boolean checked) {
        super.onTabChecked(checked);
    }

    private void initView(View view) {
        shopLayout = findView(view, R.id.ll_shop);
        mTvMoney = findView(view, R.id.tv_shop_price);
        findView(view, R.id.ll_shop_search).setOnClickListener(this);
        mTvSelectedOk = findView(view, R.id.tv_shop_ok);
        mTvSelectedOk.setOnClickListener(this);
        adver = findView(view, R.id.iv_shop_header);

        mRvGoods = findView(view, R.id.rv_goods);
        mGoodsCategory = findView(view, R.id.rv_class);

        mTvMoney.setText(R.string.shop);
        mGoodsCategoryAdater = new GoodsCategoryAdater(getContext());
        mGoodsCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        mGoodsCategory.setAdapter(mGoodsCategoryAdater);
        manager = new LinearLayoutManager(getContext());
        mRvGoods.setLayoutManager(manager);
        mRvGoods.addItemDecoration(new PaddingLeftItemDecoration(getContext(), 90, false));
        mGoodsAdapter = new GoodsAdapter(getActivity());
        mGoodsAdapter.setOnGoodsAmountChangeListener(this);
        mGoodsAdapter.setOnItemClickListener(this);
        mRvGoods.setAdapter(mGoodsAdapter);
        mRvGoods.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isOnDown){
                    changeGoodsCategory(manager.findLastVisibleItemPosition());
                }else {
                    changeGoodsCategory(manager.findFirstVisibleItemPosition());
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0){
                    isOnDown = true;
                }else {
                    isOnDown = false;
                }
            }
        });

        mGoodsCategoryAdater.setItmListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mGoodsCategoryAdater.setSelet(position);
                mGoodsCategoryAdater.notifyDataSetChanged();
                if (null != mTitleIntList && mTitleIntList.size() > position) {
                    mGoodsAdapter.setSelection(position);
                }


            }
        });
        mGoodsListPresenter.loadImage();
        mGoodsListPresenter.loadGoodsList();
    }

    private void changeGoodsCategory(int firstVisibleItemPosition) {
        String name = mGoodsAdapter.getDataList().get(firstVisibleItemPosition).getCategory().getName();
        int p = categories.indexOf(name);
        mGoodsCategoryAdater.setSelet(p);
        mGoodsCategoryAdater.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCategory != null) {
            changeCategoryNumber();
        }
        if (mGoodsAdapter != null) {
            onShopCartChanged();
            mGoodsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoadGoodsCategory(List<GoodsCategory> list) {
    }

    @Override
    public void onLoadGoodsPageList(PageResult<Goods> goodsPageResult) {
    }

    @Override
    public void onLoadGoodsList(GoodsResult goodsList) {
//        mRvGoods.refreshComplete(10);
        if (goodsList == null) {
            ViewUtils.showToastError(R.string.failed_load_data);
            return;
        }

        categories = new ArrayList<>();
        mCategory = goodsList.categories;
        mTitleIntList = new ArrayList<>();
        mgoodsList = goodsList.products;

        mGoodsCategory.setVisibility(View.VISIBLE);
        mRvGoods.setVisibility(View.VISIBLE);
        adver.setVisibility(View.VISIBLE);
        //转换
        for (int i = 0; i < goodsList.categories.size(); i++) {
            categories.add(goodsList.categories.get(i).getName());
        }

        for (int i = 0; i < goodsList.products.size(); i++) {
            Goods goods = goodsList.products.get(i);
            if (i == 0) {
                mTitleIntList.add(0);
            } else if (goods.getCategory() != null && goodsList.products.get(i - 1).getCategory() !=null && !goods.getCategory().getId().equals(goodsList.products.get(i - 1).getCategory().getId())) {
                mTitleIntList.add(i);
            }
        }
        mGoodsCategoryAdater.setData(mCategory);
        mRvGoods.addItemDecoration(new ItemDecoration(getContext(), mgoodsList, new ItemDecoration.OnDecorationCallback() {
            @Override
            public String onGroupId(int pos) {
                if (mgoodsList.get(pos).getCategory() != null && mgoodsList.get(pos).getCategory().getName() != null)
                    return mgoodsList.get(pos).getCategory().getName();
                return "-1";
            }

            @Override
            public String onGroupFirstStr(int pos) {
                if (mgoodsList.get(pos).getCategory().getName() != null)
                    return mgoodsList.get(pos).getCategory().getName();
                return "";
            }

            @Override
            public void onGroupFirstStr(String title) {
                for (int i = 0; i < categories.size(); i++) {
                    if (!title.equals(mCurTitle) && title.equals(categories.get(i))) {
                        mCurTitle = title;
                        mGoodsCategoryAdater.setSelet(i);
                    }
                }
            }
        }));
        changeCategoryNumber();
        mGoodsAdapter.setData(mgoodsList);
        mGoodsAdapter.setmTitleIntList(mTitleIntList);
        mGoodsAdapter.setmRecyclerView(mRvGoods);
        mGoodsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSearchGoods(List<Goods> list) {

    }

    @Override
    public void onLoadPictrue(BaseResponse<List<ShopAdverPictrue>> response) {
        if (response.getCode() == 1) {
            ImageUtil.loadImage(getActivity(), response.getData().get(0).getAdvertisImg(), adver);
        } else {
            ImageUtil.loadImage(getActivity(), "", adver);
        }
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastClick()) return;
        switch (v.getId()) {
            case R.id.tv_shop_ok:
                if (ShopCart.getGoodsListSelected().size() == 0) {
                    return;
                }

                ShopCardWindow window = new ShopCardWindow(getContext());
                window.showAtLocation(shopLayout, Gravity.BOTTOM, 0, 0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (alpha > 0.5f) {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Message msg = mHandler.obtainMessage();
                            msg.what = 1;

                            alpha -= 0.01f;
                            msg.obj = alpha;
                            mHandler.sendMessage(msg);
                        }
                    }

                }).start();


                window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        changeCategoryNumber();
                        mGoodsAdapter.notifyDataSetChanged();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                while (alpha < 1f) {
                                    try {
                                        Thread.sleep(10);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    Message msg = mHandler.obtainMessage();
                                    msg.what = 1;
                                    alpha += 0.01f;
                                    msg.obj = alpha;
                                    mHandler.sendMessage(msg);
                                }
                            }

                        }).start();

                    }
                });


                break;
            case R.id.ll_shop_search:
                startActivity(new Intent(getActivity(), GoodsSearchActivity.class));
                break;
            default:
                break;
        }
    }

    private void changAlpha(float v) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = v; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    public void onDestroy() {
        RxBus.getDefault().unregister(this);
        if (mGoodsListPresenter != null) {
            mGoodsListPresenter.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onAmountChanged() {
        onShopCartChanged();
        changeCategoryNumber();
    }

    @Override
    public void onAmountChanged(List<Sku> skus, int position) {
        onShopCartChanged();
        mgoodsList.get(position).setSkus(skus);
        changeCategoryNumber();
    }

    private void onShopCartChanged() {
//        int visibility = ShopCart.getGoodsListSelected().size() == 0 ? View.GONE : View.VISIBLE;
//        mTvSelectedOk.setVisibility(visibility);
//        mTvMoney.setVisibility(visibility);
        mTvMoney.setText( ShopCart.getGoodsListSelected().size() == 0 ? "商城": getString(R.string.total_price, String.valueOf(ShopCart.getCount())));
    }

    //计算每个分类商品的数量并更新
    private void changeCategoryNumber() {
        if (ShopCart.getGoodsListSelected().size() > 0) {
            for (GoodsCategory category : mCategory) {
                int numberCount = 0;
                for (Goods goods : ShopCart.getGoodsListSelected()) {
                    if (goods.getCategory().getId().equals(category.getId())) {
                        if (goods.getSkus().size() > 1) {
                            for (Sku sku : goods.getSkus()) {
                                if (sku.getNumber() > 0) {
                                    numberCount += sku.getNumber();
                                }
                            }
                        } else {
                            numberCount += goods.getSkus().get(0).getNumber();
                        }

                    }
                }
                category.setNumber(numberCount);
            }
            Set<String> all = SPUtils.getDataList("buyType");
            Set<String> buyall = new LinkedHashSet<>();//新建已经加入购物车的类型
            for (Goods good : ShopCart.getGoodsListSelected()){
                List<Sku> skus = good.getSkus();
                for (int i = 0; i < skus.size(); i++){
                    if (skus.get(i).getNumber() >  0){
                        String[] split = skus.get(i).getGroup().split(",");
                        for (int j = 0; j < split.length; j++){
                            buyall.add(split[j]);
                        }
                    }
                }
            }
            for (String groupall : all){
                if (!buyall.contains(groupall)){
                    all.remove(groupall);
                }
            }
        } else {
            for (GoodsCategory category : mCategory) {
                category.setNumber(0);
            }
            SPUtils.clearBuyType();
            for (int i = 0; i < mgoodsList.size(); i++){
                List<Sku> skus = mgoodsList.get(i).getSkus();
                for (int j = 0; j < skus.size(); j++){
                    skus.get(j).setNumber(0);
                }
            }
        }

        mGoodsCategoryAdater.notifyDataSetChanged();

    }


    @Override
    public void onItemClick(BaseRVAdapter adapter, int position) {
        Goods goods = mGoodsAdapter.getItem(position);
        Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
        intent.putExtra("goodsId", goods.getIds());
        startActivity(intent);
    }

}
