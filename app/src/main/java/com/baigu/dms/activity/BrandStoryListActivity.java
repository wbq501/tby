package com.baigu.dms.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.baigu.dms.R;
import com.baigu.dms.adapter.BaseRVAdapter;
import com.baigu.dms.adapter.BrandStoryAdapter;
import com.baigu.dms.common.utils.EmptyViewUtil;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.OnRVItemClickListener;
import com.baigu.dms.domain.model.BrandStory;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.BrandStoryPresenter;
import com.baigu.dms.presenter.impl.BrandStoryPresenterImpl;
import com.baigu.lrecyclerview.interfaces.OnLoadMoreListener;
import com.baigu.lrecyclerview.interfaces.OnRefreshListener;
import com.baigu.lrecyclerview.recyclerview.LRecyclerView;
import com.baigu.lrecyclerview.recyclerview.LRecyclerViewAdapter;

import java.util.List;

public class BrandStoryListActivity extends BaseActivity implements BrandStoryPresenter.BrandStoryView, OnLoadMoreListener, OnRefreshListener, OnRVItemClickListener {

    private LRecyclerView mRecyclerView;
    private BrandStoryAdapter mBrandStroyAdapter;
    private BrandStoryPresenter mPresenter;
    private int pageNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_story_list);
        initView();
    }

    private void initView() {
        initToolBar();
        setTitle(getString(R.string.brand_story));
        mRecyclerView = findViewById(R.id.rv_brand_list);
        mBrandStroyAdapter = new BrandStoryAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new LRecyclerViewAdapter(mBrandStroyAdapter));
        mRecyclerView.setLoadMoreEnabled(true);
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setOnRefreshListener(this);
        mBrandStroyAdapter.setOnItemClickListener(this);
        mPresenter = new BrandStoryPresenterImpl(this, this);
        EmptyViewUtil.initData(this, R.string.no_message);
        mPresenter.loadList(pageNum + "", true);
    }

    @Override
    public void onLoad(BaseResponse<PageResult<BrandStory>> data) {
        if (data.getCode() == 1) {
            if (pageNum == 0) {
                mBrandStroyAdapter.clearData();
            }
            if (data.getData().list.size() > 0) {
                mBrandStroyAdapter.appendDataList(data.getData().list);
                EmptyViewUtil.hide(this);
            } else {
                if (mBrandStroyAdapter.getDataList().size() > 0) {
                    ViewUtils.showToastInfo(R.string.no_more_messages);
                } else {
                    EmptyViewUtil.show(this);
                }
            }
        } else {
            ViewUtils.showToastError(data.getMessage());
        }
        mRecyclerView.refreshComplete(10);
        mBrandStroyAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadMore() {
        pageNum++;
        mPresenter.loadList(pageNum + "", false);
    }

    @Override
    public void onRefresh() {
        pageNum = 0;
        mPresenter.loadList(pageNum + "", false);
    }

    @Override
    public void onItemClick(BaseRVAdapter adapter, int position) {
        Intent intent = new Intent(this, BrandStoryActivity.class);
        intent.putExtra("brandStory", mBrandStroyAdapter.getItem(position));
        startActivity(intent);
    }
}
