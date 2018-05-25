package com.baigu.dms.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.DateUtils;
import com.baigu.dms.common.utils.StringUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.utils.rxbus.EventType;
import com.baigu.dms.common.utils.rxbus.RxBus;
import com.baigu.dms.domain.model.Order;
import com.baigu.dms.domain.netservice.common.model.OrderDetailResult;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.presenter.OrderDetailPresenter;
import com.baigu.dms.presenter.OrderPresenter;
import com.baigu.dms.presenter.impl.OrderDetailPresenterImpl;
import com.baigu.dms.presenter.impl.OrderPresenterImpl;

import java.text.SimpleDateFormat;

public class RefundResonActivity extends BaseActivity implements OrderPresenter.OrderView, View.OnClickListener {
    private EditText reason;
    private Button submit;
    private OrderPresenter mOrderPresenter;
    private String id;
    private String time;
    private int type;
    public static final int REQUEST_CODE = 0;
    public static final int RESULT_CODE = 1;
    public static final int REQUEST_DETAIL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_reson);
        initToolBar();
        setTitle("退款原因");
        initView();
    }

    private void initView() {
        reason = findView(R.id.et_refund_reason);
        submit = findView(R.id.bt_confirm);
        id = getIntent().getStringExtra("orderId");
        time = getIntent().getStringExtra("orderCreateTime");
        type = getIntent().getIntExtra("type", 0);
        mOrderPresenter = new OrderPresenterImpl(this,this);
        submit.setOnClickListener(this);
    }


    @Override
    public void onLoadOrderList(PageResult<Order> orderPageResult) {

    }

    @Override
    public void onCancelOrder(boolean result) {

    }

    @Override
    public void onRefundOrder(boolean result) {
        if (result) {
            ViewUtils.showToastSuccess(R.string.success_refund_order);
            RxBus.getDefault().post(EventType.TYPE_REFUND_ORDER);
            if (type == REQUEST_DETAIL) {
                setResult(RESULT_CODE);
            }
            finish();

        } else {
            ViewUtils.showToastError(R.string.failed_refund_order);
        }

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_confirm:
               String mReason= reason.getText().toString();
               if(TextUtils.isEmpty(mReason)){
                   ViewUtils.showToastError(getString(R.string.refund_reason));
                   return;
               }
                if (mOrderPresenter != null) {
                    mOrderPresenter.refundOrder(id, StringUtils.getOrderDate(DateUtils.longToStr(Long.valueOf(time), new SimpleDateFormat("yyyyMM"))),mReason);
                }
                break;
        }
    }
}
