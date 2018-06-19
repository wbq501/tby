package com.baigu.dms.common.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.activity.BankCardActivity;
import com.baigu.dms.activity.CertificationResultActivity;
import com.baigu.dms.activity.CertificationStep1Activity;
import com.baigu.dms.activity.UpdatePayPasswdActivity;
import com.baigu.dms.activity.WalletSecurityActivity;
import com.baigu.dms.activity.WithdrawActivity;
import com.baigu.dms.common.utils.SPUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.Money;
import com.baigu.dms.domain.model.User;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017-9-13 下午 3:13
 */
public class WalletView extends FrameLayout implements View.OnClickListener {
    private TextView mTvMoney;
    private ImageView mIvCertification;
    private CheckBox eyes;
    private LinearLayout mLayoutWallet;
    private Button mBtnNext;
    private View mViewLine;
    private String idcardstatus;
    private AlertDialog dialog;
    private Money money;

    private boolean isMoney;

    public WalletView(@NonNull Context context,String idcardstatus) {
        super(context);
        this.idcardstatus = idcardstatus;
        initView();
    }

    public WalletView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public WalletView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setIdcardstatus(String idcardstatus) {
        this.idcardstatus = idcardstatus;
    }

    public void setMoney(Money money) {
        this.money = money;
        if (isMoney) {
            mTvMoney.setText(String.valueOf(money.getAmount()));
        } else {
            mTvMoney.setText("****");
        }
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_wallet, this);
        int width = (ViewUtils.getScreenInfo(getContext()).widthPixels - 3 * ViewUtils.dip2px(1)) / 4;
        RelativeLayout llBankCardManager = (RelativeLayout) findViewById(R.id.ll_bank_card_manager);
        ViewGroup.LayoutParams params = llBankCardManager.getLayoutParams();
        params.width = width;
        params.height = width;
        llBankCardManager.setLayoutParams(params);
        llBankCardManager.setOnClickListener(this);

        RelativeLayout llCertification = (RelativeLayout) findViewById(R.id.ll_certification);
        params = llCertification.getLayoutParams();
        params.width = width;
        params.height = width;
        llCertification.setLayoutParams(params);
        llCertification.setOnClickListener(this);

        RelativeLayout llWithdrawRequest = (RelativeLayout) findViewById(R.id.ll_withdraw_request);
        params = llWithdrawRequest.getLayoutParams();
        params.width = width;
        params.height = width;
        llWithdrawRequest.setLayoutParams(params);
        llWithdrawRequest.setOnClickListener(this);

        RelativeLayout llSecurity = (RelativeLayout) findViewById(R.id.ll_security);
        params = llSecurity.getLayoutParams();
        params.width = width;
        params.height = width;
        llSecurity.setLayoutParams(params);
        llSecurity.setOnClickListener(this);

        mTvMoney = (TextView) findViewById(R.id.tv_mymoney);
        mIvCertification = (ImageView) findViewById(R.id.iv_certification);
        mBtnNext = findViewById(R.id.btn_next);
        mViewLine = findViewById(R.id.view_line);
        eyes = (CheckBox) findViewById(R.id.cb_eye);
        mLayoutWallet = findViewById(R.id.ll_wallet);
        mBtnNext.setOnClickListener(this);

        isMoney = SPUtils.getObject("isMoney",false);
        eyes.setChecked(!isMoney);

        eyes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isMoney) {
                    mTvMoney.setText("****");
                    SPUtils.putObject("isMoney",false);
                    isMoney = false;
                } else {
                    mTvMoney.setText(String.valueOf(money.getAmount()));
                    SPUtils.putObject("isMoney",true);
                    isMoney = true;
                }
            }
        });

//        idcardstatus = UserCache.getInstance().getUser().getIdcardstatus();
        switch (idcardstatus) {
            case User.IDCardStatus.VERIFY_NONE:
                mLayoutWallet.setVisibility(GONE);
                mViewLine.setVisibility(GONE);
                mBtnNext.setVisibility(VISIBLE);
                break;
            case User.IDCardStatus.VERIFY_SUCCESS:
                mLayoutWallet.setVisibility(VISIBLE);
                mViewLine.setVisibility(VISIBLE);
                mBtnNext.setVisibility(GONE);
                break;
            case User.IDCardStatus.VERIFY_DOING:
                mLayoutWallet.setVisibility(VISIBLE);
                mViewLine.setVisibility(VISIBLE);

                mBtnNext.setVisibility(GONE);
                break;
            case User.IDCardStatus.VERIFY_FAILED:
                mLayoutWallet.setVisibility(VISIBLE);
                mViewLine.setVisibility(VISIBLE);
                mBtnNext.setVisibility(GONE);
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("提示");
        builder.setMessage(R.string.tip_certification_again);
        builder.setPositiveButton("去认证", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getContext().startActivity(new Intent(getContext(), CertificationStep1Activity.class));
            }
        });
        builder.setNegativeButton("不用了", null);

        dialog = builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_bank_card_manager:
                switch (idcardstatus) {
                    case User.IDCardStatus.VERIFY_SUCCESS:
                        getContext().startActivity(new Intent(getContext(), BankCardActivity.class));
                        break;
                    case User.IDCardStatus.VERIFY_DOING:
                        ViewUtils.showToastInfo(getContext().getString(R.string.waite_certification));
                        break;
                    case User.IDCardStatus.VERIFY_FAILED:
                        goRenzhen();
                        break;
                }
                break;
            case R.id.ll_certification:

                if (User.IDCardStatus.VERIFY_SUCCESS.equals(idcardstatus)) {
                    getContext().startActivity(new Intent(getContext(), CertificationResultActivity.class));
                } else if (User.IDCardStatus.VERIFY_DOING.equals(idcardstatus)) {
                    getContext().startActivity(new Intent(getContext(), CertificationResultActivity.class));
                } else if (User.IDCardStatus.VERIFY_FAILED.equals(idcardstatus)) {
                    getContext().startActivity(new Intent(getContext(), CertificationResultActivity.class));
                } else {
                    getContext().startActivity(new Intent(getContext(), CertificationStep1Activity.class));
                }
                break;
            case R.id.ll_withdraw_request:
                switch (idcardstatus) {
                    case User.IDCardStatus.VERIFY_SUCCESS:
                        Intent intent = new Intent(getContext(), WithdrawActivity.class);
                        if (money == null)
                            return;
                        intent.putExtra("money",money.getAmount());
                        getContext().startActivity(intent);
                        break;
                    case User.IDCardStatus.VERIFY_DOING:
                        ViewUtils.showToastInfo(getContext().getString(R.string.waite_certification));
                        break;
                    case User.IDCardStatus.VERIFY_FAILED:
                        goRenzhen();
                        break;
                }
                break;
            case R.id.ll_security:
                switch (idcardstatus) {
                    case User.IDCardStatus.VERIFY_SUCCESS:
                        getContext().startActivity(new Intent(getContext(), WalletSecurityActivity.class));
                        break;
                    case User.IDCardStatus.VERIFY_DOING:
                        ViewUtils.showToastInfo(getContext().getString(R.string.waite_certification));
                        break;
                    case User.IDCardStatus.VERIFY_FAILED:
                        goRenzhen();
                        break;
                }
                break;
            case R.id.btn_next:
                getContext().startActivity(new Intent(getContext(), CertificationStep1Activity.class));
                break;
            default:
                break;
        }
    }

    private void goRenzhen() {
        User user = UserCache.getInstance().getUser();
        String idcardstatus = user.getIdcardstatus();
        if (idcardstatus.equals("1")){

        }else {
            dialog.show();
        }
    }

    public void cancleDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

    }
}
