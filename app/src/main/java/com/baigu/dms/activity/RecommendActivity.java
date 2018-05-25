package com.baigu.dms.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.adapter.RecommendAdapter;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.AdvertView;
import com.baigu.dms.domain.model.Advert;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.presenter.CategoryPresenter;
import com.baigu.dms.presenter.impl.CategoryPresenterImpl;
import com.baigu.lrecyclerview.interfaces.OnRefreshListener;
import com.baigu.lrecyclerview.recyclerview.LRecyclerView;
import com.baigu.lrecyclerview.recyclerview.LRecyclerViewAdapter;
import com.baigu.lrecyclerview.recyclerview.ProgressStyle;

import java.util.ArrayList;
import java.util.List;

public class RecommendActivity extends BaseActivity implements CategoryPresenter.CategoryView, View.OnClickListener, OnRefreshListener {

    private RecyclerView recyclerView;
    private FrameLayout back;

    private RecommendAdapter recommendAdapter;
//    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private CategoryPresenter presenter;
    private String categoryId;
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remmend);
        categoryId = getIntent().getStringExtra("categoryId");
        title = getIntent().getStringExtra("title");
        initToolBar();
        setTitle(title);
        initView();
    }

    private void initView() {

        recyclerView = findViewById(R.id.iRecyclerView);
        presenter = new CategoryPresenterImpl(this, this);
        recommendAdapter = new RecommendAdapter(this);
//        lRecyclerViewAdapter = new LRecyclerViewAdapter(recommendAdapter);
        recyclerView.setAdapter(recommendAdapter);
//        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineScalePulseOut);
//        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallBeat);
//        recyclerView.setHeaderViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.main_bg);
//        recyclerView.setFooterViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.white);
//        recyclerView.setLoadMoreEnabled(false);
//        recyclerView.setPullRefreshEnabled(false);
//        recyclerView.setOnRefreshListener(this);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
//        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                if (position == 1) {
//                    return 2;
//                } else {
//                    return 1;
//                }
//
//            }
//        });
        recyclerView.setLayoutManager(manager);
//        back.setOnClickListener(this);

//        View tool = LayoutInflater.from(this).inflate(R.layout.view_toolbar, null);
//        Toolbar mToolbar = tool.findViewById(R.id.toolbar);
//        if (mToolbar != null) {
//            mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//            mToolbar.setAlpha(1.0F);
//            mToolbar.setTitle(title);
//            mToolbar.getBackground().mutate().setAlpha(255);
//            setSupportActionBar(mToolbar);
//            mToolbar.setNavigationIcon(R.mipmap.im_btn_back);
//            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (ViewUtils.isFastClick()) return;
//                    onBackClick(v);
//                }
//            });
//
//        }
//        lRecyclerViewAdapter.addHeaderView(tool);

//        View view = LayoutInflater.from(this).inflate(R.layout.view_recommend_header, null);
//        TextView mTitle = view.findViewById(R.id.tv_recommend_title);
//        mTitle.setText(title);
//        lRecyclerViewAdapter.addHeaderView(view);
        presenter.loadGoodList(categoryId);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_back:
                finish();
                break;
        }
    }

    @Override
    public void onRefresh() {
        ViewUtils.showToastSuccess("刷新成功");
    }


    @Override
    public void loadGoodList(List<Goods> goodsList) {
        if (goodsList != null && goodsList.size() > 0) {
            recommendAdapter.setData(goodsList);
            recommendAdapter.notifyDataSetChanged();
            Log.i("test",recommendAdapter.getItemCount()+"--");
        }

    }
}
