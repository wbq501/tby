package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.Bank;
import com.baigu.dms.domain.model.BankType;
import com.baigu.dms.presenter.BankListPresenter;
import com.baigu.dms.presenter.BankPresenter;
import com.baigu.dms.presenter.impl.BankPresenterImpl;

import java.util.List;


/**
 * @Description 添加银行卡
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class AddBankCardActivity extends BaseActivity implements View.OnClickListener, BankPresenter.AddBankView {

    private LinearLayout selecte_bank;
    private RelativeLayout save_bank;
    private TextView bankName;
    private TextView userName;
    private TextView bankNumber;
    private EditText edbankNumber;
    private BankPresenter bankPresenter;
    private String banCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_card);
        initToolBar();
        setTitle(R.string.add_bank_card);
        initView();
    }

    private void initView() {
        selecte_bank = (LinearLayout) findViewById(R.id.rl_bank_select);
        save_bank = (RelativeLayout) findViewById(R.id.rl_save);
        bankName = (TextView) findViewById(R.id.tv_bank_name);
        userName = findViewById(R.id.tv_bank_add_name);
        bankNumber = findViewById(R.id.tv_bank_number);
        edbankNumber = findView(R.id.et_take_detail_address);
        bankPresenter = new BankPresenterImpl(this, this);

        userName.setText(UserCache.getInstance().getUser().getRealname());
        selecte_bank.setOnClickListener(this);
        save_bank.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_bank_select:
                startActivityForResult(new Intent(this, BankTypeSelectorActivity.class), RESULT_CANCELED);
                break;
            case R.id.rl_save:
                Bank bank = new Bank();
//                bank.setName(bankName.getText().toString());
                bank.setBankAccount(edbankNumber.getText().toString());
                bank.setBankCode(banCode);
                bank.setBankOpenName(userName.getText().toString());
                bankPresenter.addBank(bank);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {
            BankType bankType = data.getParcelableExtra("bankType");
            banCode = bankType.getValue();
            bankName.setText(bankType.getName());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void addBank(Boolean result) {
            if(result){
                ViewUtils.showToastInfo(R.string.bank_success);
                finish();
            }else{
                ViewUtils.showToastInfo(R.string.bank_false);
            }
    }
}
