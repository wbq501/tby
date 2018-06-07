package com.baigu.dms.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.baigu.dms.R;
import com.baigu.dms.adapter.BankListAdater;
import com.baigu.dms.common.utils.EmptyViewUtil;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.PaddingLeftItemDecoration;
import com.baigu.dms.domain.model.Bank;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.BankListPresenter;
import com.baigu.dms.presenter.DelMyBankPresenter;
import com.baigu.dms.presenter.impl.BankListPresenterImpl;
import com.baigu.dms.presenter.impl.DelMyBankPresenterimpl;
import com.baigu.lrecyclerview.recyclerview.LRecyclerView;
import com.baigu.lrecyclerview.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 银行卡管理
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class BankCardActivity extends BaseActivity implements View.OnClickListener, BankListPresenter.BankListView,DelMyBankPresenter.DelBankView {

    LRecyclerView recyclerView;
    BankListPresenter bankListPresenter;
    DelMyBankPresenter delMyBankPresenter;
    BankListAdater listAdater;
    ImageView back;
    ImageView add;

    List<Bank> banks;
    private boolean isChooseBank = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card);
//        initToolBar();
//        setTitle(R.string.bank_card_manager);
        initView();
    }

    private void initView() {
        isChooseBank = getIntent().getBooleanExtra("isChooseBank",false);
        banks = new ArrayList<>();
        bankListPresenter = new BankListPresenterImpl(this, this);
        delMyBankPresenter = new DelMyBankPresenterimpl(this,this);
        recyclerView = (LRecyclerView) findViewById(R.id.rv_bank);
        back = (ImageView) findViewById(R.id.iv_bank_back);
        add = (ImageView) findViewById(R.id.iv_bank_add);

        back.setOnClickListener(this);
        add.setOnClickListener(this);

        listAdater = new BankListAdater(BankCardActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(BankCardActivity.this));
        recyclerView.setAdapter(new LRecyclerViewAdapter(listAdater));
        recyclerView.setLoadMoreEnabled(true);
        recyclerView.setPullRefreshEnabled(false);
        listAdater.setOnItemClickListener(new BankListAdater.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                if (isChooseBank){
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("banks",banks.get(postion));
                    intent.putExtra("bundle",bundle);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });

        listAdater.setOnItemLongListener(new BankListAdater.OnItemLongListener() {
            @Override
            public void onItemClick(View view, final int postion) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(BankCardActivity.this);
                dialog.setMessage("确定删除银行卡吗").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        delMyBankPresenter.delBank(banks.get(postion).getId());
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bankListPresenter.loadBankList();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_bank_add:
                if (ViewUtils.isFastClick()) return;
                startActivity(new Intent(BankCardActivity.this, AddBankCardActivity.class));
                break;
            case R.id.iv_bank_back:
                finish();
                break;

        }
    }

    @Override
    public void loadBankList(List<Bank> banks) {
        this.banks = banks;
        if (banks != null) {
            if (banks.size() > 0) {
                listAdater.setData(banks);
                listAdater.notifyDataSetChanged();
            } else {
                EmptyViewUtil.show(this);
            }
        } else {
            ViewUtils.showToastError(R.string.failed_load_data);
        }
    }


    @Override
    public void delBank(String result) {
        if (banks != null){
            banks.clear();
        }
        if (result.equals(BaseResponse.SUCCESS)){
            ViewUtils.showToastSuccess("删除成功");
        }else {
            ViewUtils.showToastError("删除失败");
        }
        bankListPresenter.loadBankList();
    }
}
