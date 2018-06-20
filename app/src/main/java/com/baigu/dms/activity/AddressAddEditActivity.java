package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.ConfirmDialog;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.Address;
import com.baigu.dms.domain.model.City;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.presenter.AddressPresenter;
import com.baigu.dms.presenter.impl.AddressPresenterImpl;
import com.micky.logger.Logger;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class AddressAddEditActivity extends BaseActivity implements AddressPresenter.AddressView, View.OnClickListener {
    private static final int REQUEST_CODE_CITY_SELECTOR = 10011;

    private AddressPresenter mAddressPresenter;
    private Address mAddress;
    private List<City> mCityList;

    private TextView mTvCitySelect;
    private EditText mEtTakeUser;
    private EditText mEtTakePhone;
    private EditText mEtTakeDetailAddress;
    private CheckBox mCbDefault;
    private ConfirmDialog mConfirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_add_edit);
        initToolBar();
        mAddress = getIntent().getParcelableExtra("address");
        int titleRes = mAddress != null ? R.string.edit_address : R.string.add_adress;
        setTitle(titleRes);
        initView();
        mAddressPresenter = new AddressPresenterImpl(this, this);

        initData();
    }

    private void initView() {
        mTvCitySelect = findView(R.id.tv_city_select);
        mEtTakeUser = findView(R.id.et_take_user);
        mEtTakePhone = findView(R.id.et_take_phone);
        mEtTakeDetailAddress = findView(R.id.et_take_detail_address);
        mCbDefault = findView(R.id.cb_default);
        findViewById(R.id.ll_city_select).setOnClickListener(this);
        findViewById(R.id.rl_save).setOnClickListener(this);
    }

    private void initData() {
        if (mAddress != null) {
            mEtTakeUser.setText(mAddress.getName());
            mEtTakePhone.setText(mAddress.getPhone());
            mTvCitySelect.setText(mAddress.getFullRegionName());
            mEtTakeDetailAddress.setText(mAddress.getAddress());
            mCbDefault.setChecked(mAddress.isDefault());
            mAddressPresenter.loadCityList(mAddress.getRegionid())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<RxOptional<List<City>>>() {
                        @Override
                        public void accept(RxOptional<List<City>> optionalObservable) throws Exception {
                            if (optionalObservable != null) {
                                mCityList = optionalObservable.get();
                                if (mCityList != null && mCityList.size() > 0) {
                                    StringBuilder sbCity = new StringBuilder();
                                    for (City city : mCityList) {
                                        sbCity.append(city.getName());
                                    }
                                    mTvCitySelect.setText(sbCity.toString());
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            Logger.e(throwable, throwable.getMessage());
                        }
                    });
        }
    }

    @Override
    public void onLoadAddress(PageResult<Address> addressPageResult) {

    }

    @Override
    public void onSaveOrUpdateAddress(Address address, boolean b) {
        if (!b) {
            ViewUtils.showToastError(R.string.failed_save_address);
            return;
        }
        ViewUtils.showToastSuccess(R.string.success_save_address);
        Intent intent = getIntent();
        intent.putExtra("data", address);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onDeleteAddress(Address address, boolean b) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CITY_SELECTOR && resultCode == RESULT_OK) {
            if (data != null) {
                mCityList = data.getParcelableArrayListExtra("cityList");
                if (mCityList != null && mCityList.size() > 0) {
                    StringBuilder sbCity = new StringBuilder();
                    for (City city : mCityList) {
                        sbCity.append(city.getName());
                    }
                    mTvCitySelect.setText(sbCity.toString());
                }
            }
        }
    }

    private void save() {
        Address address = new Address();
        address.setShipTo(mEtTakeUser.getText().toString().trim());
        address.setId(mAddress == null ? "" : mAddress.getId());
        address.setPhone(mEtTakePhone.getText().toString().trim());
        address.setAddress(mEtTakeDetailAddress.getText().toString().trim());
        address.setUserid(UserCache.getInstance().getUser().getIds());
        address.setDefault(mCbDefault.isChecked());
        if (mAddress != null && mAddress.getAreaId() != null){
            address.setRegionid(mAddress.getAreaId());
        }
        if (mCityList != null && mCityList.size() > 0) {
            address.setRegionid(mCityList.get(mCityList.size() - 1).getId());
        }
        if (TextUtils.isEmpty(address.getShipTo())) {
            ViewUtils.showToastError(R.string.input_tip_take_user);
            return;
        }
        if (TextUtils.isEmpty(address.getPhone()) || address.getPhone().length() != 11 || !address.getPhone().startsWith("1")) {
            ViewUtils.showToastError(R.string.input_tip_take_phone);
            return;
        }
        if (TextUtils.isEmpty(address.getRegionid())) {
            ViewUtils.showToastError(R.string.input_tip_take_city);
            return;
        }
        if (TextUtils.isEmpty(address.getAddress())) {
            ViewUtils.showToastError(R.string.input_tip_take_detail_address);
            return;
        }
        mAddressPresenter.saveOrUpdateAddress(address);
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastClick()) return;
        switch (v.getId()) {
            case R.id.ll_city_select:
                startActivityForResult(new Intent(this, CitySelectorActivity.class), REQUEST_CODE_CITY_SELECTOR);
                break;
            case R.id.rl_save:
                save();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAddressPresenter != null) {
            mAddressPresenter.onDestroy();
        }
        if (mConfirmDialog != null) {
            mConfirmDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    @Override
    protected void onBackClick(View v) {
        onBack();
    }

    private void onBack() {
        mTvCitySelect = findView(R.id.tv_city_select);
        mEtTakeUser = findView(R.id.et_take_user);
        mEtTakePhone = findView(R.id.et_take_phone);
        mEtTakeDetailAddress = findView(R.id.et_take_detail_address);
        mCbDefault = findView(R.id.cb_default);
        if (!TextUtils.isEmpty(mTvCitySelect.getText())
                || !TextUtils.isEmpty(mEtTakeUser.getText())
                || !TextUtils.isEmpty(mEtTakePhone.getText())
                || !TextUtils.isEmpty(mEtTakeDetailAddress.getText())) {
            if (mConfirmDialog == null) {
                mConfirmDialog = new ConfirmDialog(this, R.string.cancel_confirm);
            }
            mConfirmDialog.setOnConfirmDialogListener(new ConfirmDialog.OnConfirmDialogListener() {
                @Override
                public void onOKClick(View v) {
                    finish();
                }
            });
            mConfirmDialog.show();
        } else {
            finish();
        }
    }
}
