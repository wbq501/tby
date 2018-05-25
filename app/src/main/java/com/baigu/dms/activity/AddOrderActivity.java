package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.adapter.GoodsSelAdapter;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.utils.rxbus.EventType;
import com.baigu.dms.common.utils.rxbus.RxBus;
import com.baigu.dms.common.utils.rxbus.RxBusEvent;
import com.baigu.dms.common.utils.rxbus.Subscribe;
import com.baigu.dms.common.utils.rxbus.ThreadMode;
import com.baigu.dms.domain.cache.ShopCart;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.Address;
import com.baigu.dms.domain.model.City;
import com.baigu.dms.domain.model.Express;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.model.Sku;
import com.baigu.dms.domain.netservice.common.model.OrderDetailResult;
import com.baigu.dms.presenter.AddOrderPresenter;
import com.baigu.dms.presenter.ExpressSelectorPresenter;
import com.baigu.dms.presenter.impl.AddOrderPresenterImpl;
import com.baigu.dms.presenter.impl.ExpressSelectorPresenterImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * @Description 添加（确认）订单
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class AddOrderActivity extends BaseActivity implements View.OnClickListener, AddOrderPresenter.OrderAddView, CompoundButton.OnCheckedChangeListener, ExpressSelectorPresenter.ExpressView {

    private static final int REQUEST_CODE_CITY_SELECT = 10021;
    private static final int REQUEST_CODE_ADDRESS_SELECT = 10022;
    private static final int REQUEST_CODE_EXPRESS_SELECT = 10023;

    private LinearLayout mLlContainer;
    private CheckBox mCbUseDefaultAddr;
    private View mViewDefaultAddrLine;
    private TextView mTvCitySelect;
    private EditText mEtTakeUser;
    private EditText mEtTakePhone;
    private EditText mEtTakeDetailAddress;
    private TextView mTvSelectExpress;
    private EditText mEtRemark;
    private CheckBox mCbAddFreqAddr;
    private View mViewAddFreqAddrLine;
    private ListView mLvGoods;
    private TextView mTvExpressPrice;
    private TextView mTvTotalPrice;
    private LinearLayout mLLUseDefaultAddr;
    private View mLLCitySelect;
    private Address mDefaultAddress;
    private Address mAddress;

    private double mTotalPrice;

    private GoodsSelAdapter mGoodsSelAdapter;

    private AddOrderPresenter mAddOrderPresenter;
    private ExpressSelectorPresenter  mExpressSelectorPresenter;
    private List<Goods> mGoodsList = null;

    LinkedHashSet<String> expressA = new LinkedHashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        initToolBar();
        setTitle(R.string.order_confirm);

        changeGoodSList();

        if (mGoodsList == null || mGoodsList.size() <= 0) {
            finish();
            return;
        }
        initView();
        RxBus.getDefault().register(this);

        mAddOrderPresenter = new AddOrderPresenterImpl(this, this);
        mExpressSelectorPresenter = new ExpressSelectorPresenterImpl(this, this);

        Flowable.timer(300L, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                        mAddOrderPresenter.getDefaultAddress(true);
                        mExpressSelectorPresenter.loadExpress();
                    }
                });
    }

    private void changeGoodSList() {
        mGoodsList = new ArrayList<>();
        List<Goods> goodsList = getIntent().getParcelableArrayListExtra("goodsList");
        for (Goods goods: goodsList) {
            if(goods.getSkus().size()>1){
                for (Sku sku:goods.getSkus()) {
                    if(sku.getNumber() > 0){
                        Goods mgoods = new Goods();
                        List<Sku> mskus = new ArrayList<Sku>();
                        mskus.add(sku);
                        mgoods.setIds(goods.getIds());
                        mgoods.setBuyNum(sku.getNumber());
                        mgoods.setUniformprice(sku.getUniformprice());
                        mgoods.setMarketprice(sku.getMarketprice());
                        mgoods.setGoodsname(goods.getGoodsname());
                        mgoods.setSkus(mskus);
                        String expressGroups = sku.getExpressGroups();
                        String[] express = expressGroups.split(",");
                        for (int i = 0; i < express.length; i++){
                            expressA.add(express[i]);
                        }
                        mGoodsList.add(mgoods);
                    }
                }

            }else{
                Goods mgoods = new Goods();
                List<Sku> mskus = new ArrayList<Sku>();
                mgoods.setIds(goods.getIds());
                mskus.add(goods.getSkus().get(0));
                mgoods.setBuyNum(goods.getSkus().get(0).getNumber());
                mgoods.setUniformprice(goods.getSkus().get(0).getUniformprice());
                mgoods.setMarketprice(goods.getSkus().get(0).getMarketprice());
                mgoods.setGoodsname(goods.getGoodsname());
                mgoods.setSkus(mskus);
                String expressGroups = goods.getSkus().get(0).getExpressGroups();
                String[] express = expressGroups.split(",");
                for (int i = 0; i < express.length; i++){
                    expressA.add(express[i]);
                }
                mGoodsList.add(mgoods);
            }

        }
    }

    private void initView() {
        mLlContainer = findView(R.id.ll_container);
        mCbUseDefaultAddr = findView(R.id.cb_default_addr);
        mCbUseDefaultAddr.setOnCheckedChangeListener(this);
        mTvCitySelect = findView(R.id.tv_city_select);
        mEtTakeUser = findView(R.id.et_take_user);
        mEtTakeUser.addTextChangedListener(new UserTextWatcher());
        mEtTakePhone = findView(R.id.et_take_phone);
        mEtTakePhone.addTextChangedListener(new PhoneTextWatcher());
        mEtTakeDetailAddress = findView(R.id.et_take_detail_address);
        mEtTakeDetailAddress.addTextChangedListener(new AddrTextWatcher());
        mTvSelectExpress = findView(R.id.tv_select_express);
        mEtRemark = findView(R.id.et_remark);
        mCbAddFreqAddr = findView(R.id.cb_add_freq_addr);

        mLvGoods = findView(R.id.lv_goods);
        mGoodsSelAdapter = new GoodsSelAdapter(this);
        mGoodsSelAdapter.setData(mGoodsList);
        mLvGoods.setAdapter(mGoodsSelAdapter);

        mTvExpressPrice = findView(R.id.tv_express_price);
        mTvTotalPrice = findView(R.id.tv_total_price);

        mViewAddFreqAddrLine = findViewById(R.id.view_line_add_freq_addr);
        mViewDefaultAddrLine = findViewById(R.id.viewDefaultAddrLine);
        mLLUseDefaultAddr = findView(R.id.ll_use_default_addr);
        mLLUseDefaultAddr.setOnClickListener(this);
        findViewById(R.id.ll_select_addr).setOnClickListener(this);
        mLLCitySelect = findViewById(R.id.ll_city_select);
        mLLCitySelect.setOnClickListener(this);
        findViewById(R.id.ll_express).setOnClickListener(this);
        findViewById(R.id.tv_submit).setOnClickListener(this);

        char symbol = 165;
        double totalPrice = computeGoodsPrice();
        mTvTotalPrice.setText(String.valueOf(symbol) + String.format("%.2f",totalPrice));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CITY_SELECT: //城市选择
                    if (data != null) {
                        City city = data.getParcelableExtra("city");
                        String allCityStr = data.getStringExtra("allCityStr");
                        if (city != null && !TextUtils.isEmpty(allCityStr)) {
                            mTvCitySelect.setText(allCityStr);
                            mTvCitySelect.setTag(city.getId());
                            computeExpress();
                        }
                    }
                    updateFreqAddrVisibility();
                    break;
                case REQUEST_CODE_ADDRESS_SELECT: //地址选择
                    if (data != null) {
                        mAddress = data.getParcelableExtra("address");
                        mAddress.setShipTo(mAddress.getName());
                        mAddress.setRegionid(mAddress.getAreaId());
                        if (mAddress != null) {
                            mCbUseDefaultAddr.setChecked(false);
                            mEtTakeUser.setText(mAddress.getName());
                            mEtTakePhone.setText(mAddress.getPhone());
                            mTvCitySelect.setText(mAddress.getFullRegionName());
                            mTvCitySelect.setTag(mAddress.getAreaId());
                            mEtTakeDetailAddress.setText(mAddress.getAddress() == null ? "":mAddress.getAddress());
                            computeExpress();
                        }
                    }
                    updateFreqAddrVisibility();
                    break;
                case REQUEST_CODE_EXPRESS_SELECT: //快递选择
                    if (data != null) {
                        Express express = data.getParcelableExtra("express");
                        mTvSelectExpress.setText(express == null ? "" : express.getName());
                        mTvSelectExpress.setTag(express.getId());
                        computeExpress();
                    }
                    break;
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int useDefaultVisibility = mDefaultAddress == null ? View.GONE : View.VISIBLE;
        mLLUseDefaultAddr.setVisibility(useDefaultVisibility);
        mViewDefaultAddrLine.setVisibility(useDefaultVisibility);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switchDefaultAddr(b && mDefaultAddress != null);
        computeExpress();
        ViewUtils.hideInputMethod(this);
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastClick()) return;
        switch (v.getId()) {
            case R.id.ll_select_addr:
                Intent intent = new Intent(this, AddressListActivity.class);
                intent.putExtra("select", true);
                startActivityForResult(intent, REQUEST_CODE_ADDRESS_SELECT);
                break;
            case R.id.ll_city_select:
                startActivityForResult(new Intent(this, CitySelectorActivity.class), REQUEST_CODE_CITY_SELECT);
                break;
            case R.id.ll_express:
                Intent intent1 = new Intent(this, ExpressSelectorActivity.class);
                ArrayList<String> express = new ArrayList<>();
                express.addAll(expressA);
                intent1.putStringArrayListExtra("express", express);
                startActivityForResult(intent1, REQUEST_CODE_EXPRESS_SELECT);
                break;
            case R.id.tv_submit:
                submitOrder();
                break;
            case R.id.ll_use_default_addr:
                break;
            default:
                break;
        }
    }

    private void switchDefaultAddr(boolean b) {
        if (b) {
            mEtTakeUser.setText(mDefaultAddress.getShipTo() == null ? mDefaultAddress.getName() : mDefaultAddress.getShipTo());
            mEtTakePhone.setText(mDefaultAddress.getPhone());
            mTvCitySelect.setText(mDefaultAddress.getFullRegionName());
            mTvCitySelect.setTag(mDefaultAddress.getRegionid() == null ? mDefaultAddress.getAreaId() : mDefaultAddress.getRegionid());
            mEtTakeDetailAddress.setText(mDefaultAddress.getAddress());
            mCbAddFreqAddr.setVisibility(View.GONE);
            mViewAddFreqAddrLine.setVisibility(View.GONE);

            mEtTakeUser.setEnabled(false);
            mEtTakePhone.setEnabled(false);
            mTvCitySelect.setEnabled(false);
            mTvCitySelect.setEnabled(false);
            mEtTakeDetailAddress.setEnabled(false);
            mLLCitySelect.setOnClickListener(null);

            int color = getResources().getColor(R.color.color_666666);
            mEtTakeUser.setTextColor(color);
            mEtTakePhone.setTextColor(color);
            mTvCitySelect.setTextColor(color);
            mEtTakeDetailAddress.setTextColor(color);

        } else {
            clearAddress();
        }

    }

    private void clearAddress() {
        mEtTakeUser.setText("");
        mEtTakePhone.setText("");
        mTvCitySelect.setText("");
        mTvCitySelect.setTag(null);
        mEtTakeDetailAddress.setText("");
        mCbAddFreqAddr.setVisibility(View.VISIBLE);
        mViewAddFreqAddrLine.setVisibility(View.VISIBLE);

        mEtTakeUser.setEnabled(true);
        mEtTakePhone.setEnabled(true);
        mTvCitySelect.setEnabled(true);
        mTvCitySelect.setEnabled(true);
        mEtTakeDetailAddress.setEnabled(true);
        mLLCitySelect.setOnClickListener(this);

        int color = getResources().getColor(R.color.main_text);
        mEtTakeUser.setTextColor(color);
        mEtTakePhone.setTextColor(color);
        mTvCitySelect.setTextColor(color);
        mEtTakeDetailAddress.setTextColor(color);
    }

    /**快递计算*/
    private void computeExpress() {
        String cityId = mTvCitySelect.getTag() ==  null ? "" : mTvCitySelect.getTag().toString();
        String expressId = mTvSelectExpress.getText() == null ? "" : mTvSelectExpress.getText().toString();
        if (!TextUtils.isEmpty(cityId) && !TextUtils.isEmpty(expressId) && mGoodsList.size() > 0) {
            mAddOrderPresenter.expressCompute(cityId, expressId,mGoodsList);
        }
    }

    @Override
    public void onExpressCompute(double expressPrice) {
        if (expressPrice <= 0 || mGoodsList.size() <= 0) {
            ViewUtils.showToastError(R.string.failed_compute_price);
            mTvSelectExpress.setText("");
            mTvSelectExpress.setTag(null);
            mTvExpressPrice.setText("");
            char symbol = 165;
            double totalPrice = computeGoodsPrice();
            mTvTotalPrice.setText(String.valueOf(symbol) + totalPrice);
            return;
        }

        char symbol = 165;
        mTotalPrice = computeGoodsPrice() + expressPrice;
        mTvTotalPrice.setText(String.valueOf(symbol) + String.format("%.2f",mTotalPrice));
        String expressPriceStr = String.valueOf(symbol) + expressPrice;
        mTvExpressPrice.setText(getString(R.string.express_price, expressPriceStr));
    }

    @Override
    public void onAddOrder(OrderDetailResult result) {

        if (result == null || TextUtils.isEmpty(result.getCreateDate()) || TextUtils.isEmpty(result.getOrderNo()) ) {
            ViewUtils.showToastError(R.string.failed_add_order);
            return;
        }

        ShopCart.clearCart();
        RxBus.getDefault().post(EventType.TYPE_ADD_ORDER);
        Intent intent = new Intent(this, PayActivity.class);
//        intent.putExtra("payMode", result.getPayMode());
        intent.putExtra("orderNum", result.getOrderNo());
        intent.putExtra("orderCreateDate", result.getCreateDate());
        intent.putExtra("orderTotalPrice", new Double(String.format("%.2f",mTotalPrice)));
        startActivity(intent);
        finish();
    }

    @Override
    public void onCheckGoodsStock(Boolean result) {
        if (!result) {
            finish();
            return;
        }
//        mLlContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGetDefaultAddress(Address address) {
        if (address != null) {
            if (mDefaultAddress != null && mCbUseDefaultAddr.isChecked()) {
                if (!address.getId().equals(mDefaultAddress.getId())) {
                    mCbUseDefaultAddr.setChecked(false);
                } else {
                    mDefaultAddress = address;
                    mCbUseDefaultAddr.setChecked(false);
                    mCbUseDefaultAddr.setChecked(true);
                }
            }
        } else if (mDefaultAddress != null){
            mCbUseDefaultAddr.setChecked(false);
        }
        mDefaultAddress = address;
        int visibility = mDefaultAddress == null ? View.GONE : View.VISIBLE;
        mLLUseDefaultAddr.setVisibility(visibility);
        mViewDefaultAddrLine.setVisibility(visibility);
    }

    private double computeGoodsPrice() {
        double result = 0;
        for (Goods goods : mGoodsList) {
            result += goods.getBuyNum() * goods.getUniformprice();
        }
        return result;
    }

    private void submitOrder() {
        Address address = new Address();
        address.setShipTo(mEtTakeUser.getText().toString().trim());
        address.setName(mEtTakeUser.getText().toString().trim());
        address.setPhone(mEtTakePhone.getText().toString().trim());
        address.setAddress(mEtTakeDetailAddress.getText().toString().trim());
        address.setRegionid(mTvCitySelect.getTag() == null ? "" : mTvCitySelect.getTag().toString());
        address.setAreaId(mTvCitySelect.getTag() == null ? "" : mTvCitySelect.getTag().toString());
        address.setDefault(mCbAddFreqAddr.isChecked());
        address.setUserid(UserCache.getInstance().getUser().getIds());

        if (TextUtils.isEmpty(mEtTakeUser.getText().toString().trim())) {
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
        String expressId = mTvSelectExpress.getText() == null ? "" : mTvSelectExpress.getText().toString();
        if (TextUtils.isEmpty(expressId)) {
            ViewUtils.showToastInfo(R.string.input_tip_select_express);
            return;
        }

        boolean newAddresss = mAddress == null
                || !mAddress.getName().equals(address.getName())
                || !mAddress.getPhone().equals(address.getPhone())
                || !mAddress.getAddress().equals(address.getAddress())
                || !mAddress.getAreaId().equals(address.getAreaId());
        mAddOrderPresenter.checkGoodsStock(mGoodsList);
        if (newAddresss) {
            mAddOrderPresenter.addOrder(mGoodsList, address, mCbAddFreqAddr.isChecked(), expressId, mEtRemark.getText().toString().trim());
        } else {
            mAddOrderPresenter.addOrder(mGoodsList, mAddress, mCbAddFreqAddr.isChecked(), expressId, mEtRemark.getText().toString().trim());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBusEvent(RxBusEvent event) {
        if (event.what == EventType.TYPE_ADDRESS_UPDATE) {
            String addressId = event.object == null ? "" : event.object.toString();
            if (mAddress != null && mAddress.getId() != null && mAddress.getId().equals(addressId)) {
                mAddress = null;
                clearAddress();
            }
            mAddOrderPresenter.getDefaultAddress(false);
            mAddress = null;
            clearAddress();
        } else if (event.what == EventType.TYPE_ADDRESS_DELETE) {
            String addressId = event.object == null ? "" : event.object.toString();
            if (mAddress != null && addressId.equals(mAddress.getId())) {
                mAddress = null;
                clearAddress();
            }
        }
    }

    @Override
    protected void onDestroy() {
        RxBus.getDefault().unregister(this);
        if (mAddOrderPresenter != null) {
            mAddOrderPresenter.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onLoadExpress(List<Express> list) {
        Express defaultExpress = null;
        for (Express express : list) {
            if ("默认快递".equals(express.getName())) {
                defaultExpress = express;
            }
        }
        if (defaultExpress != null) {
            mTvSelectExpress.setText(defaultExpress.getName());
            mTvSelectExpress.setTag(defaultExpress.getId());
            computeExpress();
        }
    }

    private void updateFreqAddrVisibility() {
        int visibility = (mAddress == null || !mAddress.getName().equals(mEtTakeUser.getText().toString().trim())
                || !mAddress.getPhone().equals(mEtTakePhone.getText().toString().trim())
                || !mAddress.getAddress().equals(mEtTakeDetailAddress.getText().toString().trim())
                || !mAddress.getAreaId().equals(mTvCitySelect.getTag() == null ? "" : mTvCitySelect.getTag().toString()) ? View.VISIBLE : View.GONE);
        mCbAddFreqAddr.setVisibility(visibility);
        mViewAddFreqAddrLine.setVisibility(visibility);
    }

    class PhoneTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            updateFreqAddrVisibility();
        }
    }

    class UserTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            updateFreqAddrVisibility();
        }
    }

    class AddrTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            updateFreqAddrVisibility();
        }
    }
}
