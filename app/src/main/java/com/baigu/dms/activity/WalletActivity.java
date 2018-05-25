package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.baigu.dms.R;
import com.baigu.dms.adapter.WalletAdapter;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.WalletView;
import com.baigu.dms.domain.model.Money;
import com.baigu.dms.presenter.WalletPresenter;
import com.baigu.dms.presenter.impl.WalletPresenterImpl;
import com.baigu.lrecyclerview.interfaces.OnRefreshListener;
import com.baigu.lrecyclerview.recyclerview.LRecyclerView;
import com.baigu.lrecyclerview.recyclerview.LRecyclerViewAdapter;
import com.baigu.lrecyclerview.recyclerview.ProgressStyle;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/7/29 12:31
 */
public class WalletActivity extends BaseActivity implements OnRefreshListener, WalletPresenter.WalletView {
    private WalletPresenter mWalletPresenter;

    private LRecyclerView mRecyclerView;
    private WalletView mWalletView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        initToolBar();
        setTitle(R.string.wallet);
        mWalletPresenter = new WalletPresenterImpl(this, this);
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
        mWalletView = new WalletView(this);
        LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(new WalletAdapter());
        adapter.addHeaderView(mWalletView);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadMoreEnabled(false);
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
    }

    @Override
    public void onGetMyMoney(Money result) {
        mRecyclerView.refreshComplete(10);
        if (result != null) {
            mWalletView.setMoney(result);
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
}
