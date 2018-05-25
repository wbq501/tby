package com.baigu.dms.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.DateUtils;
import com.baigu.dms.common.utils.StringUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.ConfirmDialog;
import com.baigu.dms.common.view.PayDialog;
import com.baigu.dms.common.view.PayPasswordDialog;
import com.baigu.dms.domain.cache.ShopCart;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.Money;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.presenter.PayPresenter;
import com.baigu.dms.presenter.impl.PayPresenterImpl;

import java.text.SimpleDateFormat;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class PayActivity extends BaseActivity implements View.OnClickListener, PayPresenter.PayView {

    private String payMode;
    private String mOrderNum;
    private String mOrderCreateDate;
    private TextView allPay;
    private TextView walletPay;
    private TextView aliPay;
    private TextView tvTotlaPrice;
    private CheckBox checkBox_wallet;
    private CheckBox checkBox_ali;
    private RelativeLayout mRAliPay;
    private RelativeLayout mRWalletPay;
    private double mOrderTotalPrice;
    private double mAllPrice;//钱包金额
    private double walletPrice = 0;
    private double aliPrice;
    private PayPresenter mPayPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        initToolBar();
        setTitle(R.string.order_pay);
        mOrderNum = getIntent().getStringExtra("orderNum");
        mOrderCreateDate = DateUtils.longToStr(Long.valueOf(getIntent().getStringExtra("orderCreateDate")), new SimpleDateFormat("yyyyMM"));
//        mOrderTotalPrice = getIntent().getDoubleExtra("orderTotalPrice", 0);
        mOrderTotalPrice = getIntent().getDoubleExtra("orderTotalPrice", 0.0);
        if (TextUtils.isEmpty(mOrderCreateDate) || TextUtils.isEmpty(mOrderNum) || mOrderTotalPrice <= 0) {
            ViewUtils.showToastError(R.string.invalid_order);
            finish();
            return;
        }
        mPayPresenter = new PayPresenterImpl(this, this);
        mPayPresenter.getMyMoney();
        initView();
    }

    private void initView() {
        tvTotlaPrice = findView(R.id.tv_order_total_price);
        allPay = findView(R.id.tv_pay_all);
        aliPay = findView(R.id.tv_pay_ali);
        walletPay = findView(R.id.tv_pay_wallet);
        checkBox_ali = findView(R.id.cb_ali_pay);
        checkBox_wallet = findView(R.id.cb_wallet_pay);
        mRAliPay = findViewById(R.id.rl_alipay);
        mRWalletPay = findViewById(R.id.rl_walletpay);
        char symbol = 165;
        tvTotlaPrice.setText(String.valueOf(symbol) + mOrderTotalPrice);
        allPay.setText(mOrderTotalPrice + "");
        findViewById(R.id.rl_pay).setOnClickListener(this);
        mRAliPay.setOnClickListener(this);
        mRWalletPay.setOnClickListener(this);
        checkBox_wallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    PayDialog dialog = new PayDialog(PayActivity.this);
                    dialog.setPayPrice(mOrderTotalPrice);
                    dialog.setMaxPrice(mAllPrice);
                    //钱包确认
                    dialog.setDialogDismissListener(new PayDialog.PayDialogDismissListener() {
                        @Override
                        public void getMoneyListner(String money) {
                            if (Double.valueOf(money) > 0) {
                                walletPrice = Double.valueOf(money);
                                walletPay.setText("￥:" + walletPrice);
                                walletPay.setVisibility(View.VISIBLE);
                                checkBox_wallet.setChecked(true);
                            } else {
                                checkBox_wallet.setChecked(false);
                                walletPay.setVisibility(View.INVISIBLE);
                            }

                        }
                    });
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            if (walletPay.getVisibility() == View.VISIBLE) {
                                checkBox_wallet.setChecked(true);
                                aliPrice = mOrderTotalPrice - walletPrice;
                                if (aliPrice > 0) {
                                    tvTotlaPrice.setText(String.valueOf(aliPrice));
                                    aliPay.setText(String.valueOf(aliPrice));
                                    mRAliPay.setClickable(true);
                                    tvTotlaPrice.setVisibility(View.VISIBLE);
                                } else {
                                    tvTotlaPrice.setVisibility(View.INVISIBLE);
                                    aliPay.setVisibility(View.INVISIBLE);
                                    mRAliPay.setClickable(false);
                                    checkBox_ali.setChecked(false);
                                }

                            } else {
                                checkBox_wallet.setChecked(false);
                                tvTotlaPrice.setText(String.valueOf(mOrderTotalPrice));
                                aliPay.setText(String.valueOf(mOrderTotalPrice));
                            }
                        }
                    });
                    dialog.show();

                } else {
                    walletPrice = 0;
                    tvTotlaPrice.setVisibility(View.VISIBLE);
                    tvTotlaPrice.setText(String.valueOf(mOrderTotalPrice));
                    aliPay.setText(String.valueOf(mOrderTotalPrice));
                    mRAliPay.setClickable(true);
                }

            }
        });

//        checkBox_ali.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//
//                }
//            }
//        });
    }

    @Override
    public void onPayOrder(boolean result) {
        if (result) {
            Intent intent = new Intent(PayActivity.this, PaySuccessActivity.class);
            intent.putExtra("orderNum", mOrderNum);
            intent.putExtra("payedMoney", String.valueOf(mOrderTotalPrice));
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void loadMoney(Money result) {
        if (result != null) {
            mAllPrice = result.getAmount() - result.getFrozenAmount();
        }
    }

    @Override
    public void onPayOrderByWallet(boolean result) {
        if (result) {
            ShopCart.clearCart();
            Intent intent = new Intent(PayActivity.this, PaySuccessActivity.class);
            intent.putExtra("orderNum", mOrderNum);
            intent.putExtra("payedMoney", String.valueOf(mOrderTotalPrice));
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_pay:
                if (!checkBox_wallet.isChecked() && !checkBox_ali.isChecked()) {
                    ViewUtils.showToastError("请选择支付方式");
                    return;
                }
//                if (checkBox_wallet.isChecked() && checkBox_ali.isChecked()) {//合并支付
//                    payMode = 2 + "";
//                    final PayPasswordDialog dialog = new PayPasswordDialog(this);
//                    dialog.show();
//                    dialog.setOnSubmitClickListener(new PayPasswordDialog.OnSubmitClickListener() {
//                        @Override
//                        public void onClick(String pwd) {
//                            if(TextUtils.isEmpty(pwd)||pwd.length()<6){
//                                ViewUtils.showToastError("请输入正确密码");
//                                return;
//                            }
//                            mPayPresenter.payOrderMerge(mOrderNum, StringUtils.getOrderDate(mOrderCreateDate), String.valueOf(walletPrice), payMode,pwd);
//                            dialog.dismiss();
//
//                        }
//                    });
//
//                    return;
//                }
                if (checkBox_wallet.isChecked() && !checkBox_ali.isChecked()) {//钱包支付
                    if (mOrderTotalPrice - walletPrice > 0) {
                        ViewUtils.showToastError("支付金额不足");
                        return;
                    }
                    final PayPasswordDialog dialog = new PayPasswordDialog(this);
                    dialog.show();
                    dialog.setOnSubmitClickListener(new PayPasswordDialog.OnSubmitClickListener() {
                        @Override
                        public void onClick(String pwd) {
                            if(TextUtils.isEmpty(pwd)||pwd.length()<6){
                                ViewUtils.showToastError("请输入正确密码");
                                return;
                            }
                            mPayPresenter.payOrderByWallet(mOrderNum, StringUtils.getOrderDate(mOrderCreateDate),pwd);
                            dialog.dismiss();
                        }
                    });
//                    mPayPresenter.payOrderByWallet(mOrderNum, StringUtils.getOrderDate(mOrderCreateDate));
                    return;
                }
                if (!checkBox_wallet.isChecked() && checkBox_ali.isChecked()) {//支付宝支付
                    payMode = 1 + "";
                    mPayPresenter.payOrder(mOrderNum, StringUtils.getOrderDate(mOrderCreateDate), payMode);
                    return;
                }


                break;
            case R.id.rl_alipay:
                if (checkBox_ali.isChecked()) {
                    checkBox_ali.setChecked(false);
                    aliPay.setVisibility(View.INVISIBLE);
                    mRWalletPay.setClickable(true);
                } else {
                    checkBox_ali.setChecked(true);
                    aliPay.setVisibility(View.VISIBLE);
                    aliPay.setText("￥" + (mOrderTotalPrice - walletPrice));
                    mRWalletPay.setClickable(false);
                }
                break;
            case R.id.rl_walletpay:
                if(!UserCache.getInstance().getUser().getIdcardstatus().equals(User.IDCardStatus.VERIFY_SUCCESS)){
                    ViewUtils.showToastInfo("您还不是实名认证用户，无法使用钱包支付");
                    return;
                }
                if (checkBox_wallet.isChecked()) {
                    checkBox_wallet.setChecked(false);
                    walletPay.setVisibility(View.INVISIBLE);
                    mRAliPay.setClickable(true);
                } else {
                    checkBox_wallet.setChecked(true);
                    mRAliPay.setClickable(false);
                }
                break;
            default:
                break;
        }
    }
}
