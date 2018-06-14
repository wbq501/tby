package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.MoneyValueFilter;
import com.baigu.dms.common.utils.StringUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.GlideCircleTransform;
import com.baigu.dms.common.view.PayPasswordDialog;
import com.baigu.dms.domain.db.RepositoryFactory;
import com.baigu.dms.domain.db.repository.BankTypeRepository;
import com.baigu.dms.domain.model.Bank;
import com.baigu.dms.domain.model.BankType;
import com.baigu.dms.domain.model.Money;
import com.baigu.dms.presenter.ApplywithdrawPresenter;
import com.baigu.dms.presenter.PayPresenter;
import com.baigu.dms.presenter.impl.ApplywithdrawPresenterImpl;
import com.baigu.dms.presenter.impl.PayPresenterImpl;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * @Description 提现申请
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class WithdrawActivity extends BaseActivity implements ApplywithdrawPresenter.Applywithdraw, View.OnClickListener{
    public static final int REQUEST_CODE = 1000;

    private double mAllPrice;//钱包金额
    private TextView tv_mymoney;

    private LinearLayout ll_choose_bank;
    private View layout_bank;
    private TextView tv_bank_name;
    private TextView tv_bank_number;
    private ImageView iv_bank_icon;
    private TextView tv_ok;
    private EditText et_withdraw_money;
    Bank bank;

    private ApplywithdrawPresenter applywithdrawPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        initToolBar();
        setTitle(R.string.withdraw_request);
        applywithdrawPresenter = new ApplywithdrawPresenterImpl(this,this);
        initView();
    }

    private void initView() {
        mAllPrice = getIntent().getDoubleExtra("money",0);
        tv_mymoney = findView(R.id.tv_mymoney);
        tv_mymoney.setText(mAllPrice+"元");

        ll_choose_bank = findView(R.id.ll_choose_bank);
        ll_choose_bank.setOnClickListener(this);

        layout_bank = findView(R.id.layout_bank);
        tv_bank_name = findView(R.id.tv_bank_name);
        iv_bank_icon = findView(R.id.iv_bank_icon);
        tv_bank_number = findView(R.id.tv_bank_number);

        et_withdraw_money = findView(R.id.et_withdraw_money);
        et_withdraw_money.setFilters(new InputFilter[]{new MoneyValueFilter()});
        et_withdraw_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputmoney = et_withdraw_money.getText().toString().trim();
                if (!TextUtils.isEmpty(inputmoney)){
                    if (Double.valueOf(inputmoney) > mAllPrice){
                        et_withdraw_money.setText(mAllPrice+"");
                        et_withdraw_money.setSelection(et_withdraw_money.getText().toString().trim().length());
                    }
                }
            }
        });
        tv_ok = findView(R.id.tv_ok);
        tv_ok.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ViewUtils.isFastClick()) return super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id) {
            case R.id.action_history:
                startActivity(new Intent(this, WithdrawRecordActivity.class));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_choose_bank:
                Intent intent = new Intent(this, BankCardActivity.class);
                intent.putExtra("isChooseBank",true);
                startActivityForResult(intent,REQUEST_CODE);
                break;
            case R.id.tv_ok:
                final String money = et_withdraw_money.getText().toString().trim();
                if (TextUtils.isEmpty(money)){
                    ViewUtils.showToastError(R.string.input_getmoney);
                    return;
                }
                if (bank == null){
                    ViewUtils.showToastError(R.string.choose_bank);
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
                        applywithdrawPresenter.getMyMoney(pwd,money,bank.getId(),true);
                        dialog.dismiss();
                    }
                });
                break;
        }
    }

    @Override
    public void OngetMyMoney(String result) {
        ViewUtils.showToastSuccess(result);
        String withdraw_success = getString(R.string.withdraw_success);
        if (result.equals(withdraw_success)){
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE:
                if (resultCode == RESULT_OK){
                    layout_bank.setVisibility(View.VISIBLE);
                    Bundle bundle = data.getBundleExtra("bundle");
                    bank = bundle.getParcelable("banks");
                    String number =bank.getBankAccount().substring(bank.getBankAccount().length()-4,bank.getBankAccount().length());
                    tv_bank_number.setText(number);
                    tv_bank_name.setText(bank.getBankName());
                    BankTypeRepository bankRepository = RepositoryFactory.getInstance().getBankRepository();
                    List<BankType> bankTypes = bankRepository.queryAllBank();
                    for (BankType bankType : bankTypes){
                        if (bankType.getValue().equals(bank.getBankCode()) && bankType.getName().equals(bank.getBankName())){
                            Glide.with(this).load(bankType.getRemarks()).centerCrop().transform(new GlideCircleTransform(this)).into(iv_bank_icon);
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (applywithdrawPresenter != null){
            applywithdrawPresenter.onDestroy();
        }
    }
}
