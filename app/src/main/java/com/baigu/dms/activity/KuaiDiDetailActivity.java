package com.baigu.dms.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.baigu.dms.R;
import com.baigu.dms.adapter.KuaiDiAdapter;
import com.baigu.dms.domain.model.ExpressList;
import com.baigu.dms.presenter.ExpressGetPresenter;
import com.baigu.dms.presenter.OrderPresenter;
import com.baigu.dms.presenter.impl.ExpressGetPresenterImpl;
import com.baigu.lrecyclerview.interfaces.OnItemClickListener;
import com.baigu.lrecyclerview.interfaces.OnLoadMoreListener;
import com.baigu.lrecyclerview.recyclerview.LRecyclerView;
import com.baigu.lrecyclerview.recyclerview.LRecyclerViewAdapter;
import com.baigu.lrecyclerview.recyclerview.ProgressStyle;

import java.util.ArrayList;
import java.util.List;

public class KuaiDiDetailActivity extends BaseActivity implements OnLoadMoreListener, OnItemClickListener {

    private LRecyclerView mRvOrder;
    KuaiDiAdapter kuaiDiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kuaididetail);
        initToolBar();
        setTitle(R.string.express_list);

        initView();
    }

    private void initView() {

        List<String> lists = getIntent().getStringArrayListExtra("logisticsNos");
        mRvOrder = findView(R.id.rv_order);
        mRvOrder.setLoadingMoreProgressStyle(ProgressStyle.BallBeat);
        mRvOrder.setHeaderViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.main_bg);
        mRvOrder.setFooterViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.main_bg);
        kuaiDiAdapter = new KuaiDiAdapter(this,lists);
        mRvOrder.setLayoutManager(new LinearLayoutManager(this));
        LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(kuaiDiAdapter);
        adapter.setOnItemClickListener(this);
        mRvOrder.setAdapter(adapter);
        mRvOrder.setPullRefreshEnabled(false);
        mRvOrder.setLoadMoreEnabled(true);
        mRvOrder.setOnLoadMoreListener(this);
        kuaiDiAdapter.setData(lists);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onLoadMore() {
        kuaiDiAdapter.notifyDataSetChanged();
    }

}
