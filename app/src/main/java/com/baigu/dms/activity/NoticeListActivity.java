package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.baigu.dms.R;
import com.baigu.dms.adapter.BaseRVAdapter;
import com.baigu.dms.adapter.NoticeAdapter;
import com.baigu.dms.common.utils.EmptyViewUtil;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.PaddingLeftItemDecoration;
import com.baigu.dms.common.view.OnRVItemClickListener;
import com.baigu.dms.common.view.PaddingLeftRightItemDecoration;
import com.baigu.dms.domain.model.Notice;
import com.baigu.dms.presenter.NoticePresenter;
import com.baigu.dms.presenter.impl.NoticePresenterImpl;
import com.baigu.lrecyclerview.interfaces.OnLoadMoreListener;
import com.baigu.lrecyclerview.interfaces.OnRefreshListener;
import com.baigu.lrecyclerview.recyclerview.LRecyclerView;
import com.baigu.lrecyclerview.recyclerview.LRecyclerViewAdapter;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class NoticeListActivity extends BaseActivity implements NoticePresenter.NoticeView, OnRVItemClickListener, OnRefreshListener, OnLoadMoreListener {

    private LRecyclerView mRvNoticeList;
    private NoticeAdapter mNoticeAdapter;

    private NoticePresenter mNoticePresenter;
    private int pageNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);
        initToolBar();
        setTitle(R.string.notice);

        initView();
        mNoticePresenter = new NoticePresenterImpl(this, this);
        mNoticePresenter.getNoticeList(pageNum + "");
    }

    private void initView() {
        mRvNoticeList = findView(R.id.rv_notice_list);
        mRvNoticeList.setLayoutManager(new LinearLayoutManager(this));
        mNoticeAdapter = new NoticeAdapter(this);
        mNoticeAdapter.setOnItemClickListener(this);
        mRvNoticeList.setAdapter(new LRecyclerViewAdapter(mNoticeAdapter));
        EmptyViewUtil.initData(this, R.string.notice_list_empty);
        mRvNoticeList.setPullRefreshEnabled(true);
        mRvNoticeList.setLoadMoreEnabled(true);
        mRvNoticeList.setOnRefreshListener(this);
        mRvNoticeList.setOnLoadMoreListener(this);
    }

    @Override
    public void onItemClick(BaseRVAdapter adapter, int position) {
        if (ViewUtils.isFastClick()) return;
        Notice notice = mNoticeAdapter.getItem(position);
        Intent intent = new Intent(this, NoticeDetailActivity.class);
        intent.putExtra("notice", notice);
        startActivity(intent);
    }

    @Override
    public void onGetNoticeList(List<Notice> list) {
        if (list != null) {
            if (pageNum == 0) {
                mNoticeAdapter.clearData();
            }
            if (list.size() > 0) {
                mNoticeAdapter.appendDataList(list);
            } else {
                if (mNoticeAdapter.getDataList().size() > 0) {
                    ViewUtils.showToastSuccess(R.string.no_more_messages);
                }
            }

        }
        if (mNoticeAdapter.getItemCount() <= 0) {
            EmptyViewUtil.show(this);
        }
        mRvNoticeList.refreshComplete(10);
        mNoticeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetNotice(Notice notice) {

    }

    @Override
    public void onRefresh() {
        pageNum = 0;
        mNoticePresenter.getNoticeList(pageNum + "");
    }

    @Override
    public void onLoadMore() {
        pageNum++;
        mNoticePresenter.getNoticeList(pageNum + "");
    }
}
