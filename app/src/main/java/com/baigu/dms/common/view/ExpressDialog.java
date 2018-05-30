package com.baigu.dms.common.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.adapter.ExpressStateAdapter;
import com.baigu.dms.adapter.KuaiDiAdapter;
import com.baigu.dms.common.utils.ImageUtil;
import com.baigu.dms.domain.model.ExpressInfo;
import com.baigu.dms.domain.model.ExpressList;
import com.baigu.dms.domain.model.LogisticsInfo;
import com.baigu.dms.presenter.ExpressGetPresenter;
import com.baigu.dms.presenter.impl.ExpressGetPresenterImpl;
import com.baigu.lrecyclerview.recyclerview.LRecyclerView;
import com.baigu.lrecyclerview.recyclerview.LRecyclerViewAdapter;
import com.baigu.lrecyclerview.recyclerview.ProgressStyle;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ExpressDialog extends Dialog implements ExpressGetPresenter.ExpressGetView{

    ExpressGetPresenter expressGetPresenter;
    private Activity activity;
    String expressList;
    ImageView iv_express_head;
    TextView tv_express_num;
    LinearLayout ll_nomsg;
    TextView tv_load;
    LRecyclerView mRvOrder;

    ExpressStateAdapter expressStateAdapter;

    public ExpressDialog(@NonNull Context context,Activity activity,String expressList) {
        super(context);
        this.activity = activity;
        this.expressList = expressList;
    }

    public ExpressDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.express_dialog);

        expressGetPresenter = new ExpressGetPresenterImpl(activity,this);
        expressGetPresenter.getExpress(expressList);

        initView();
    }

    private void initView() {
        iv_express_head = findViewById(R.id.iv_express_head);
        tv_express_num = findViewById(R.id.tv_express_num);
        ll_nomsg = findViewById(R.id.ll_nomsg);
        tv_load = findViewById(R.id.tv_load);
        mRvOrder = findViewById(R.id.rv_order);

        mRvOrder = findViewById(R.id.rv_order);
        mRvOrder.setLoadingMoreProgressStyle(ProgressStyle.BallBeat);
        mRvOrder.setHeaderViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.main_bg);
        mRvOrder.setFooterViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.main_bg);
        expressStateAdapter = new ExpressStateAdapter(getContext());
        mRvOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(expressStateAdapter);
        mRvOrder.setAdapter(adapter);
        mRvOrder.setPullRefreshEnabled(false);
        mRvOrder.setLoadMoreEnabled(true);
    }


    @Override
    public void loadExpress(LogisticsInfo<ExpressInfo> result) {
        if (result == null || result.getState().equals("0")){
            Glide.with(getContext()).load(R.mipmap.ic_launcher).into(iv_express_head);
            tv_express_num.setText(expressList);
            ll_nomsg.setVisibility(View.VISIBLE);
            tv_load.setText(R.string.empty_data);
            mRvOrder.setVisibility(View.GONE);
        }else {
            ImageUtil.loadImage(getContext(),"https://cdn.kuaidi100.com/images/all/56/"+result.getCom()+".png",iv_express_head);
            tv_express_num.setText(result.getNu());
            ll_nomsg.setVisibility(View.GONE);
            mRvOrder.setVisibility(View.VISIBLE);
            expressStateAdapter.setData(result.getData());
        }
    }

}
