package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import com.baigu.dms.R;
import com.baigu.dms.adapter.WithdrawAdapter;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.WalletView;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.Money;
import com.baigu.dms.domain.model.WithdrawHistory;
import com.baigu.dms.presenter.WalletPresenter;
import com.baigu.dms.presenter.WithdrawHistoryPresenter;
import com.baigu.dms.presenter.impl.WalletPresenterImpl;
import com.baigu.dms.presenter.impl.WithdrewHistoryPresenterImpl;
import com.baigu.lrecyclerview.interfaces.OnLoadMoreListener;
import com.baigu.lrecyclerview.interfaces.OnRefreshListener;
import com.baigu.lrecyclerview.recyclerview.LRecyclerView;
import com.baigu.lrecyclerview.recyclerview.LRecyclerViewAdapter;
import com.baigu.lrecyclerview.recyclerview.ProgressStyle;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/7/29 12:31
 */
public class WalletActivity extends BaseActivity implements OnRefreshListener,OnLoadMoreListener, WalletPresenter.WalletView,WithdrawHistoryPresenter.WithdrawHistoryView {
    private WalletPresenter mWalletPresenter;

    private LRecyclerView mRecyclerView;
    private WalletView mWalletView;
    
    WithdrawHistoryPresenter withdrawHistoryPresenter;
    private int pageNum;
    WithdrawAdapter withdrawAdapter;
    boolean isHistory = false;
    String idcardstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        initToolBar();
        setTitle(R.string.wallet);
        mWalletPresenter = new WalletPresenterImpl(this, this);
        withdrawHistoryPresenter = new WithdrewHistoryPresenterImpl(this,this);
        initView();
    }

    private void initView() {
        mRecyclerView = findView(R.id.iRecyclerView);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.LineScalePulseOut);
        mRecyclerView.setArrowImageView(R.mipmap.refresh_down_arrow);
        mRecyclerView.setHeaderViewColor(R.color.white, R.color.white, R.color.colorPrimary);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallBeat);
        mRecyclerView.setFooterViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.main_bg);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setOnRefreshListener(this);
        idcardstatus = UserCache.getInstance().getUser().getIdcardstatus();
        mWalletView = new WalletView(this,idcardstatus);
        withdrawAdapter = new WithdrawAdapter(this, withdrawHistoryPresenter);
        LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(withdrawAdapter);
        adapter.addHeaderView(mWalletView);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadMoreEnabled(true);
        mRecyclerView.setOnLoadMoreListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecyclerView.forceToRefresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wallet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ViewUtils.isFastClick()) {
            return super.onOptionsItemSelected(item);
        }
        int id = item.getItemId();
        if (id == R.id.action_gesture_lock) {
            startActivity(new Intent(WalletActivity.this, WalletIntroActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh(){
        mWalletPresenter.getMyMoney();
        pageNum = 0;
        mRecyclerView.refreshComplete(10);
        loadData(pageNum,true);
    }

    @Override
    public void onGetMyMoney(Money result) {
        mRecyclerView.refreshComplete(10);
        if (result != null) {
            mWalletView.setMoney(result);
            idcardstatus = UserCache.getInstance().getUser().getIdcardstatus();
            mWalletView.setIdcardstatus(idcardstatus);
        } else {
            ViewUtils.showToastError(R.string.failed_load_wallet_info);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWalletView.cancleDialog();
    }

    @Override
    public void onLoadMore() {
        loadData(pageNum,true);
    }

    private void loadData(int pageNum,boolean showDialog) {
        String menberId = UserCache.getInstance().getUser().getIds();
        withdrawHistoryPresenter.getHistory(menberId,pageNum,showDialog,isHistory);
    }

    @Override
    public void getHistory(List<WithdrawHistory> result) {
        if (result == null && pageNum == 0){
            return;
        }
        if (pageNum == 0){

        }else {
            mRecyclerView.setNoMore(result.size() == 0 && withdrawAdapter.getItemCount() > 0);
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
