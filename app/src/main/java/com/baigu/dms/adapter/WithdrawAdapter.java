package com.baigu.dms.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.DateUtils;
import com.baigu.dms.domain.model.WithdrawHistory;
import com.baigu.dms.presenter.WithdrawHistoryPresenter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class WithdrawAdapter extends BaseRVAdapter<WithdrawHistory>{

    public Activity mActivity;
    private WithdrawHistoryPresenter mOrderPresenter;

    public WithdrawAdapter(Activity activity, WithdrawHistoryPresenter presenter) {
        mActivity = activity;
        mOrderPresenter = presenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.withdraw_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewHolder viewHolder = (ViewHolder) holder;
        WithdrawHistory withdrawHistory = mDataList.get(position);
        viewHolder.tv_type.setText("提现金额");
        viewHolder.tv_time.setText(DateUtils.StingSimpleDateFormat(withdrawHistory.getUpdateDate()));
        int applyStatus = withdrawHistory.getApplyStatus();
        BigDecimal ammout = null;
        if (applyStatus == 0){
            ammout = new BigDecimal(withdrawHistory.getAmount());
        }else {
            ammout = new BigDecimal(withdrawHistory.getApplyAmount());
        }
        BigDecimal divide = ammout.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        viewHolder.tv_money.setText("￥"+ divide.doubleValue());
        String stats="";
        switch (withdrawHistory.getApplyStatus()){
            case 1:
                stats = "审核中";
                break;
            case 2:
                stats = "办理中";
                break;
            case 3:
                stats = "已完成";
                break;
            case 4:
                stats = "已驳回";
                break;
            case 0:
                stats = withdrawHistory.getDescribetion();
                switch (withdrawHistory.getExpRecType()){
                    case 0:
                        viewHolder.tv_type.setText("未知");
                        break;
                    case 1:
                        viewHolder.tv_type.setText("支出");
                        break;
                    case 2:
                        viewHolder.tv_type.setText("收入");
                        break;
                }
                break;
        }
        viewHolder.tv_state.setText(stats);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_type;
        private TextView tv_time;
        private TextView tv_money;
        private TextView tv_state;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_money = itemView.findViewById(R.id.tv_money);
            tv_state = itemView.findViewById(R.id.tv_state);
        }
    }
}
