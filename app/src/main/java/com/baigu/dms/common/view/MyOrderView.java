package com.baigu.dms.common.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.baigu.dms.R;
import com.baigu.dms.activity.OrderListActivity;
import com.baigu.dms.activity.OrderRepealListActivity;
import com.baigu.dms.common.utils.OrderUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.model.Order;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/28 22:18
 */
public class MyOrderView extends FrameLayout implements View.OnClickListener  {

    public MyOrderView(@NonNull Context context) {
        super(context);
        initView();
    }

    public MyOrderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyOrderView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_my_order, this);
        View view = findViewById(R.id.layout_main);
        view.getLayoutParams().width = ViewUtils.getScreenInfo(getContext()).widthPixels;

        view.findViewById(R.id.ll_my_order).setOnClickListener(this);
        view.findViewById(R.id.layout_unpay).setOnClickListener(this);
        view.findViewById(R.id.layout_payed).setOnClickListener(this);
        view.findViewById(R.id.layout_refunded).setOnClickListener(this);
        view.findViewById(R.id.layout_preparing).setOnClickListener(this);
        view.findViewById(R.id.layout_delivered).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int status = Order.Status.ALL;
        switch (v.getId()) {
            case R.id.ll_my_order:
                status = Order.Status.ALL;
                break;
            case R.id.layout_unpay:
                status = Order.Status.UNPAY;
                break;
            case R.id.layout_payed:
                status = Order.Status.UNDELIVER;
                break;
            case R.id.layout_refunded:
                status = Order.Status.REFUNDED;
                break;
            case R.id.layout_preparing://退款
                Intent intent =new Intent(getContext(), OrderRepealListActivity.class);
                intent.putExtra("status", OrderUtils.REFUND_APPLY_FOR);
                getContext().startActivity(intent);
                break;
            case R.id.layout_delivered:
                status = Order.Status.DELIVERED;
                break;
            default:
                break;
        }
        if(v.getId()!=R.id.layout_preparing){
            Intent intent = new Intent(getContext(), OrderListActivity.class);
            intent.putExtra("status", status);
            getContext().startActivity(intent);
        }


    }

}
