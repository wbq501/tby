package com.baigu.dms.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private int state;

    public CouponAdapter(Activity mActivity, CouponPresenter couponPresenter,int state){
        this.mActivity = mActivity;
        this.couponPresenter = couponPresenter;
        this.state = state;
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
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        Coupon.ListBean coupon = getItem(position);
        Coupon.ListBean.CouponUserBean couponUser = coupon.getCouponUser();
        ViewHolder holder = (ViewHolder) viewHolder;
        int rule = coupon.getCoupon().getRule();
        if (state == 1){
            holder.ll_type.setBackgroundResource(R.drawable.couponleft);
            holder.ll_right.setBackgroundResource(R.drawable.couponright);
            holder.tv_money2.setText(rule+"");
            holder.tv_title.setText(rule+"元全场通用卷");
            holder.tv_content.setText(coupon.getCoupon().getName());
            holder.tv_time.setText("开始时间："+ DateUtils.StingSimpleDateFormat(couponUser.getCreateDate())+
                    "\n结束时间："+DateUtils.StingSimpleDateFormat(couponUser.getEndDate()));
        }else {
            holder.ll_type.setBackgroundResource(R.drawable.couponleft2);
            holder.ll_right.setBackgroundResource(R.drawable.couponright);
            holder.tv_money2.setText(rule+"");
            holder.tv_title.setText(rule+"元全场通用卷");
            holder.tv_title.setTextColor(mActivity.getResources().getColor(R.color.coupon_textcolor));
            holder.tv_content.setText(coupon.getCoupon().getName());
            holder.tv_content.setTextColor(mActivity.getResources().getColor(R.color.coupon_textcolor));
            holder.tv_time.setText("开始时间："+ DateUtils.StingSimpleDateFormat(couponUser.getCreateDate())+
                    "\n结束时间："+DateUtils.StingSimpleDateFormat(couponUser.getEndDate()));
            holder.tv_time.setTextColor(mActivity.getResources().getColor(R.color.coupon_no));
        }

    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private LinearLayout ll_type;
        private LinearLayout ll_right;
        private TextView tv_money1;
        private TextView tv_money2;
        private TextView tv_type;
        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_time;

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
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(listener != null){
                listener.OnItemClick(view,getLayoutPosition()-1);
            }
        }
    }
}
