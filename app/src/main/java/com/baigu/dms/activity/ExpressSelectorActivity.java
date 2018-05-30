package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.baigu.dms.R;
import com.baigu.dms.adapter.BaseRVAdapter;
import com.baigu.dms.adapter.ExpressAdapter;
import com.baigu.dms.common.view.HorizontalItemDecoration;
import com.baigu.dms.common.view.HorizontalItemNoneSEDecoration;
import com.baigu.dms.common.view.OnRVItemClickListener;
import com.baigu.dms.domain.model.Express;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.model.Sku;
import com.baigu.dms.presenter.ExpressSelectorPresenter;
import com.baigu.dms.presenter.impl.ExpressSelectorPresenterImpl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class ExpressSelectorActivity extends BaseActivity implements ExpressSelectorPresenter.ExpressView, OnRVItemClickListener {
    private ExpressAdapter mExpressAdapter;

    private ExpressSelectorPresenter mExpressSelectorPresenter;


    ArrayList<String> expressA = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_selector);
        initToolBar();
        setTitle(R.string.express);

        expressA = getIntent().getStringArrayListExtra("express");
        initView();
        mExpressSelectorPresenter = new ExpressSelectorPresenterImpl(this, this);
        mExpressSelectorPresenter.loadExpress();
    }

    private void initView() {
        RecyclerView rvExpress = findView(R.id.rv_express);
        rvExpress.setLayoutManager(new LinearLayoutManager(this));
        rvExpress.addItemDecoration(new HorizontalItemNoneSEDecoration(this));
        mExpressAdapter = new ExpressAdapter();
        mExpressAdapter.setOnItemClickListener(this);
        rvExpress.setAdapter(mExpressAdapter);
    }

    @Override
    public void onLoadExpress(List<Express> list) {
        LinkedHashSet<Express> expressList = new LinkedHashSet<>();
        for (int j = 0; j < expressA.size(); j++){
            for (int i = 0; i < list.size(); i++){
                if (expressA.get(j).contains(list.get(i).getValue())){
                    expressList.add(list.get(i));
                }
            }
        }
        if (list != null)
            list.clear();
        list.addAll(expressList);
        mExpressAdapter.setData(list);
        mExpressAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(BaseRVAdapter adapter, int position) {
        Express express = mExpressAdapter.getItem(position);
        Intent intent = getIntent();
        intent.putExtra("express", express);
        intent.putExtra("expressValue",express.getValue());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mExpressSelectorPresenter != null) {
            mExpressSelectorPresenter.onDestroy();
        }
    }
}
