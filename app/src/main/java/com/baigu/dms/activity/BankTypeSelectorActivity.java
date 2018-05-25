package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.baigu.dms.R;
import com.baigu.dms.adapter.BankTypeAdapter;
import com.baigu.dms.adapter.BaseRVAdapter;
import com.baigu.dms.common.view.HorizontalItemNoneSEDecoration;
import com.baigu.dms.common.view.OnRVItemClickListener;
import com.baigu.dms.domain.model.BankType;
import com.baigu.dms.presenter.BankTypePresenter;
import com.baigu.dms.presenter.impl.BankTypePresenterImpl;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class BankTypeSelectorActivity extends BaseActivity implements BankTypePresenter.BankTypeView, OnRVItemClickListener {
    private BankTypeAdapter mBankTypeAdapter;

    private BankTypePresenter mBankPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_selector);
        initToolBar();
        setTitle(R.string.bank_type);
        initView();
        mBankPresenter = new BankTypePresenterImpl(this, this);
        mBankPresenter.loadBankList();
    }

    private void initView() {
        RecyclerView rvExpress = findView(R.id.rv_bank);
        rvExpress.setLayoutManager(new LinearLayoutManager(this));
        rvExpress.addItemDecoration(new HorizontalItemNoneSEDecoration(this));
        mBankTypeAdapter = new BankTypeAdapter();
        mBankTypeAdapter.setOnItemClickListener(this);
        rvExpress.setAdapter(mBankTypeAdapter);
    }

    @Override
    public void onLoadBankList(List<BankType> list) {
        mBankTypeAdapter.setData(list);
        mBankTypeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(BaseRVAdapter adapter, int position) {
        BankType bankType = mBankTypeAdapter.getItem(position);
        Intent intent = getIntent();
        intent.putExtra("bankType", bankType);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBankPresenter != null) {
            mBankPresenter.onDestroy();
        }
    }


}
