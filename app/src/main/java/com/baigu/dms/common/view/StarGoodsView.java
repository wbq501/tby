package com.baigu.dms.common.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.baigu.dms.R;
import com.baigu.dms.activity.GoodsDetailActivity;
import com.baigu.dms.adapter.BaseRVAdapter;
import com.baigu.dms.adapter.StarGoodsAdapter;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.model.Goods;

import java.util.List;

/**
 * @Description 每日推荐
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/29 0:14
 */
public class StarGoodsView extends FrameLayout {

    private View mLayoutTop;
    private View mLayoutContent;
    private StarGoodsAdapter goodsAdapter;

    public StarGoodsView(@NonNull Context context) {
        super(context);
        initView();
    }

    public StarGoodsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public StarGoodsView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setData(List<Goods> list) {
        if (list == null) {
            mLayoutTop.setVisibility(View.GONE);
            mLayoutContent.setVisibility(View.GONE);
            return;
        }
        mLayoutTop.setVisibility(View.VISIBLE);
        mLayoutContent.setVisibility(View.VISIBLE);
        if (goodsAdapter != null) {
            goodsAdapter.setData(list);
            goodsAdapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_star_goods, this);
        mLayoutTop = findViewById(R.id.layout_top);
        mLayoutContent = findViewById(R.id.layout_content);
        RecyclerView rvStarGoods = (RecyclerView) findViewById(R.id.rv_star_goods);
        rvStarGoods.getLayoutParams().width = ViewUtils.getScreenInfo(getContext()).widthPixels;
        goodsAdapter = new StarGoodsAdapter(getContext());
        goodsAdapter.setOnItemClickListener(new OnRVItemClickListener() {
            @Override
            public void onItemClick(BaseRVAdapter adapter, int position) {
                Goods goods = goodsAdapter.getItem(position);
                Intent intent = new Intent(getContext(), GoodsDetailActivity.class);
                intent.putExtra("goodsId", goods.getIds());
                getContext().startActivity(intent);
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
//        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                switch(goodsAdapter.getItemViewType(position)){
//                    case StarGoodsAdapter.TYPE_2:
//                        return 3;
//                    default:
//                        return 2;
//                }
//            }
//        });
        rvStarGoods.setLayoutManager(layoutManager);
        rvStarGoods.setAdapter(goodsAdapter);
    }
}
