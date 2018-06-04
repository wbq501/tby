package com.baigu.dms.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;

import com.baigu.dms.R;
import com.baigu.dms.adapter.OrderAdapter;
import com.baigu.dms.adapter.WithdrawAdapter;
import com.baigu.dms.common.utils.EmptyViewUtil;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.model.WithdrawHistory;
import com.baigu.dms.presenter.WithdrawHistoryPresenter;
import com.baigu.dms.presenter.impl.WithdrewHistoryPresenterImpl;
import com.baigu.lrecyclerview.interfaces.OnItemClickListener;
import com.baigu.lrecyclerview.interfaces.OnLoadMoreListener;
import com.baigu.lrecyclerview.interfaces.OnRefreshListener;
import com.baigu.lrecyclerview.recyclerview.LRecyclerView;
import com.baigu.lrecyclerview.recyclerview.LRecyclerViewAdapter;
import com.baigu.lrecyclerview.recyclerview.ProgressStyle;

import java.util.List;

/**
 * @Description 提现记录
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class WithdrawRecordActivity extends BaseActivity implements OnLoadMoreListener,OnRefreshListener,OnItemClickListener,WithdrawHistoryPresenter.WithdrawHistoryView {

    LRecyclerView rv_history;
    WithdrawAdapter withdrawAdapter;
    WithdrawHistoryPresenter withdrawHistoryPresenter;

    private int pageNum;
    boolean isHistory = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_record);
        initToolBar();
        isHistory = getIntent().getBooleanExtra("isHistory",true);
        if (isHistory){
            setTitle(R.string.withdraw_record);
        }else {
            setTitle(R.string.transaction_record);
        }
        withdrawHistoryPresenter = new WithdrewHistoryPresenterImpl(this,this);
        initView();
        loadData(pageNum,true);
    }

    private void initView() {
        rv_history = findView(R.id.rv_history);
        rv_history.setLoadingMoreProgressStyle(ProgressStyle.BallBeat);
        rv_history.setHeaderViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.main_bg);
        rv_history.setFooterViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.main_bg);
        rv_history.setLayoutManager(new LinearLayoutManager(this));
        withdrawAdapter = new WithdrawAdapter(this, withdrawHistoryPresenter);
        LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(withdrawAdapter);
        adapter.setOnItemClickListener(this);
        rv_history.setAdapter(adapter);
        rv_history.setPullRefreshEnabled(false);
        rv_history.setOnRefreshListener(this);
        rv_history.setLoadMoreEnabled(true);
        rv_history.setOnLoadMoreListener(this);

        EmptyViewUtil.initData(this,R.string.withdrew_history_no);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onLoadMore() {
//        EmptyViewUtil.hide(WithdrawRecordActivity.this);
        loadData(pageNum,true);
    }

    @Override
    public void onRefresh() {
        pageNum = 0;
        loadData(pageNum,true);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    private void loadData(int pageNum,boolean showDialog) {
        String menberId = UserCache.getInstance().getUser().getIds();
        withdrawHistoryPresenter.getHistory(menberId,pageNum,showDialog,isHistory);
    }

    @Override
    public void getHistory(List<WithdrawHistory> result) {
        if (result == null && pageNum == 0){
            EmptyViewUtil.show(this);
            return;
        }
        if (pageNum == 0){
            EmptyViewUtil.hide(this);
        }else {
            rv_history.setNoMore(result.size() == 0 && withdrawAdapter.getItemCount() > 0);
        }
        if (pageNum == 0){
            withdrawAdapter.clearData();
            withdrawAdapter.notifyDataSetChanged();
        }
        withdrawAdapter.appendDataList(result);
        withdrawAdapter.notifyDataSetChanged();
        pageNum++;
    }
}
