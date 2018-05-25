package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.cache.ShopCart;

/**
 * @Description 支付成功
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class PaySuccessActivity extends BaseActivity {


    private String mOrderNum;
    private String mPayedMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        initToolBar();
        setTitle(R.string.pay_success);
        mOrderNum = getIntent().getStringExtra("orderNum");
        mPayedMoney = getIntent().getStringExtra("payedMoney");
        initView();
    }

    private void initView() {
        TextView tvPaySuccessTip = findView(R.id.tv_pay_success_tip1);
        tvPaySuccessTip.setText(getString(R.string.pay_success_tip1, mPayedMoney));
        TextView tvOrderNo = findView(R.id.tv_order_no);
        tvOrderNo.setText(mOrderNum);
        final TextView tvCopy = findView(R.id.tv_copy);
        tvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtils.copy(PaySuccessActivity.this, mOrderNum);
                ViewUtils.showToastSuccess(R.string.success_copy);
            }
        });
        findViewById(R.id.rl_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
    }

    @Override
    protected void onBackClick(View v) {
        super.onBackClick(v);
        back();
    }

    private void back() {
        Intent intent = new Intent(PaySuccessActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
