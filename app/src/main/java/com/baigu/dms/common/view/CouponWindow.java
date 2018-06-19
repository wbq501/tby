package com.baigu.dms.common.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.adapter.CouponAdapter;
import com.baigu.dms.common.utils.EmptyViewUtil;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.model.Coupon;
import com.baigu.dms.presenter.CouponPresenter;
import com.baigu.dms.presenter.impl.CouponPresenterimpl;
import com.baigu.lrecyclerview.interfaces.OnItemClickListener;
import com.baigu.lrecyclerview.interfaces.OnLoadMoreListener;
import com.baigu.lrecyclerview.interfaces.OnRefreshListener;
import com.baigu.lrecyclerview.recyclerview.LRecyclerView;
import com.baigu.lrecyclerview.recyclerview.LRecyclerViewAdapter;
import com.baigu.lrecyclerview.recyclerview.ProgressStyle;

import java.util.List;

public class CouponWindow extends PopupWindow implements OnRefreshListener,OnLoadMoreListener,CouponPresenterimpl.CouponView {

    private Activity mActivity;
    private LRecyclerView rv_hb;
    CouponAdapter couponAdapter;
    CouponPresenter couponPresenter;
    FrameLayout ll_empty;
    LinearLayout ll_btn;
    Button btn_sure;
    Button btn_cale;

    private int pageNum = 1;
    private CouponInterFace couponInterFace;
    private double shopPrice;

    public CouponWindow(Activity mActivity, CouponInterFace couponInterFace, double shopPrice){
        this.mActivity = mActivity;
        this.couponInterFace = couponInterFace;
        this.shopPrice = shopPrice;
        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_coupon, null);
        setContentView(view);
        WindowManager mg = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
        setHeight(mg.getDefaultDisplay().getHeight() * 2 / 3);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        ColorDrawable dw = new ColorDrawable();
        setBackgroundDrawable(dw);
        setAnimationStyle(R.style.ShopCardWinow);
        setFocusable(true);

        couponPresenter = new CouponPresenterimpl(mActivity,this);
        initview(view);
        int strId = mActivity.getResources().getIdentifier("coupon_msg", "string", mActivity.getPackageName());
        ll_empty = view.findViewById(R.id.ll_empty);
        TextView tv_empty_tip = view.findViewById(R.id.tv_empty_tip);
        tv_empty_tip.setText(R.string.coupon_msg);
        loadData();
    }

    private void initview(View view) {
        rv_hb = view.findViewById(R.id.rv_hb);
        rv_hb.setLoadingMoreProgressStyle(ProgressStyle.BallBeat);
        rv_hb.setHeaderViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.main_bg);
        rv_hb.setFooterViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.main_bg);
        rv_hb.setLayoutManager(new LinearLayoutManager(mActivity));
        couponAdapter = new CouponAdapter(mActivity,couponPresenter,true);
        LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(couponAdapter);
        rv_hb.setAdapter(adapter);
        rv_hb.setPullRefreshEnabled(true);
        rv_hb.setOnRefreshListener(this);
        rv_hb.setLoadMoreEnabled(true);
        rv_hb.setOnLoadMoreListener(this);
        ll_btn = view.findViewById(R.id.ll_btn);
        btn_sure = view.findViewById(R.id.btn_sure);
        btn_cale = view.findViewById(R.id.btn_cale);

        couponAdapter.setListener(new com.baigu.dms.common.utils.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                if (shopPrice < couponAdapter.getItem(position).getCoupon().getLowestMoney()){
                    ViewUtils.showToastError(R.string.cannot_use);
                    return;
                }
                Coupon.ListBean couponAdapterItem = couponAdapter.getItem(position);
                couponInterFace.getCoupon(couponAdapterItem);
                dismiss();
            }
        });
        btn_cale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couponInterFace.getCoupon(null);
                dismiss();
            }
        });
    }

    @Override
    public void onLoadMore() {
        loadData();
    }

    @Override
    public void onRefresh() {
        pageNum = 1;
        rv_hb.refreshComplete(10);
        loadData();
    }

    private void loadData() {
        couponPresenter.getCouponList(pageNum,1);
    }

    @Override
    public void CouponList(Coupon coupon) {
        if (coupon == null && pageNum == 1){
            ll_empty.setVisibility(View.VISIBLE);
            ll_btn.setVisibility(View.GONE);
            return;
        }
        if (coupon == null){
            ll_empty.setVisibility(View.GONE);
            rv_hb.setNoMore(true);
            return;
        }
        List<Coupon.ListBean> lists = coupon.getList();
        if (lists.get(0).getCoupon() == null){
            ll_empty.setVisibility(View.VISIBLE);
            ll_btn.setVisibility(View.GONE);
            rv_hb.setNoMore(true);
            return;
        }
        if (pageNum == 1){
            if (lists == null || lists.size() == 0){
                ll_empty.setVisibility(View.VISIBLE);
                ll_btn.setVisibility(View.GONE);
            }
            rv_hb.setNoMore(false);
        }else {
            rv_hb.setNoMore(false);
        }
        if (pageNum == 1){
            couponAdapter.clearData();
            couponAdapter.notifyDataSetChanged();
        }
        couponAdapter.appendDataList(lists);
        couponAdapter.notifyDataSetChanged();
        pageNum++;
    }

    public interface CouponInterFace{
        void getCoupon(Coupon.ListBean couponAdapterItem);
    }
}
