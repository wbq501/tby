package com.baigu.dms.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.DateUtils;
import com.baigu.dms.common.utils.OnItemClickListener;
import com.baigu.dms.domain.model.Coupon;
import com.baigu.dms.presenter.CouponPresenter;

public class CouponAdapter extends BaseRVAdapter<Coupon.ListBean>{

    private Activity mActivity;
    private CouponPresenter couponPresenter;

    private OnItemClickListener listener;
    private int state = 1;
    private boolean isPop;

    public CouponAdapter(Activity mActivity, CouponPresenter couponPresenter,boolean isPop){
        this.mActivity = mActivity;
        this.couponPresenter = couponPresenter;
        this.isPop = isPop;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.coupon_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        super.onBindViewHolder(viewHolder, position);
        final Coupon.ListBean coupon = getItem(position);
        Coupon.ListBean.CouponUserBean couponUser = coupon.getCouponUser();
        final ViewHolder holder = (ViewHolder) viewHolder;
        int rule = coupon.getCoupon().getRule();
        if (isPop){
            holder.cb_choose.setVisibility(View.GONE);
            holder.cb_choose.setChecked(coupon.getCoupon().isCb_choose());
        }else {
            holder.cb_choose.setVisibility(View.GONE);
            holder.cb_choose.setChecked(coupon.getCoupon().isCb_choose());
        }
        if (state == 1){
            holder.ll_type.setBackgroundResource(R.drawable.couponleft);
            holder.ll_right.setBackgroundResource(R.drawable.couponright);
            holder.tv_money2.setText(rule+"");
            holder.tv_title.setText(rule+"元全场通用券");
            holder.tv_content.setText(coupon.getCoupon().getName());
            holder.tv_time.setText("开始时间："+ DateUtils.StingSimpleDateFormat(couponUser.getCreateDate())+
                    "\n结束时间："+DateUtils.StingSimpleDateFormat(couponUser.getEndDate()));
        }else {
            holder.ll_type.setBackgroundResource(R.drawable.couponleft2);
            holder.ll_right.setBackgroundResource(R.drawable.couponright);
            holder.tv_money2.setText(rule+"");
            holder.tv_title.setText(rule+"元全场通用券");
            holder.tv_title.setTextColor(mActivity.getResources().getColor(R.color.coupon_textcolor));
            holder.tv_content.setText(coupon.getCoupon().getName());
            holder.tv_content.setTextColor(mActivity.getResources().getColor(R.color.coupon_textcolor));
            holder.tv_time.setText("开始时间："+ DateUtils.StingSimpleDateFormat(couponUser.getCreateDate())+
                    "\n结束时间："+DateUtils.StingSimpleDateFormat(couponUser.getEndDate()));
            holder.tv_time.setTextColor(mActivity.getResources().getColor(R.color.coupon_no));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.cb_choose.isChecked()){
                    coupon.getCoupon().setCb_choose(false);
                }else {
                    coupon.getCoupon().setCb_choose(true);
                }
                if(listener != null){
                    listener.OnItemClick(holder.itemView,position);
                }
            }
        });
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout ll_type;
        private LinearLayout ll_right;
        private TextView tv_money1;
        private TextView tv_money2;
        private TextView tv_type;
        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_time;
        private CheckBox cb_choose;

        public ViewHolder(View itemView) {
            super(itemView);
            ll_type = itemView.findViewById(R.id.ll_type);
            ll_right = itemView.findViewById(R.id.ll_right);
            tv_money1 = itemView.findViewById(R.id.tv_money1);
            tv_money2 = itemView.findViewById(R.id.tv_money2);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_time = itemView.findViewById(R.id.tv_time);
            cb_choose = itemView.findViewById(R.id.cb_choose);
        }
    }
}
