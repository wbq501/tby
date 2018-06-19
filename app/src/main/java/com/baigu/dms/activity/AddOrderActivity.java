package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.adapter.GoodsSelAdapter;
import com.baigu.dms.common.utils.SPUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.utils.rxbus.EventType;
import com.baigu.dms.common.utils.rxbus.RxBus;
import com.baigu.dms.common.utils.rxbus.RxBusEvent;
import com.baigu.dms.common.utils.rxbus.Subscribe;
import com.baigu.dms.common.utils.rxbus.ThreadMode;
import com.baigu.dms.common.view.CouponWindow;
import com.baigu.dms.domain.cache.ShopCart;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.db.RepositoryFactory;
import com.baigu.dms.domain.model.Address;
import com.baigu.dms.domain.model.City;
import com.baigu.dms.domain.model.Coupon;
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

    private TextView tv_select_coupon;

    private double mTotalPrice;

    private GoodsSelAdapter mGoodsSelAdapter;

    private AddOrderPresenter mAddOrderPresenter;
    private ExpressSelectorPresenter  mExpressSelectorPresenter;
    private List<Goods> mGoodsList = null;

    LinkedHashSet<String> expressA = new LinkedHashSet<>();
    LinkedHashSet<String> expressB = new LinkedHashSet<>();

    private String expressValue;
    private String couponUserId = "";
    private int rule;

    private float alpha = 1f;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    changAlpha((float) msg.obj);
                    break;
            }
        }
    };

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
        List<Express> expressList = RepositoryFactory.getInstance().getExpressRepository().queryAllOrdered();

        for(Express express : expressList){
            expressB.add(express.getValue());
        }
        for (Goods goods: goodsList) {
            if (expressA.size() > 0){
                if (expressB.size() > 0){
                    expressB.clear();
                    expressB.addAll(expressA);
                }else {
                    expressB.addAll(expressA);
                }
                expressA.clear();
            }
            if(goods.getSkus().size()>1){
                for (Sku sku:goods.getSkus()) {
                    if(sku.getNumber() > 0){
                        Goods mgoods = new Goods();
                        List<Sku> mskus = new ArrayList<Sku>();
                        mskus.add(sku);
                        mgoods.setIds(goods.getIds());
//                        mgoods.setBuyNum(sku.getNumber());
                        mgoods.setSupercoverpath(goods.getSupercoverpath());
                        mgoods.setCoverpath(goods.getCoverpath());
                        mgoods.setUniformprice(sku.getUniformprice());
                        mgoods.setMarketprice(sku.getMarketprice());
                        mgoods.setGoodsname(goods.getGoodsname());
                        mgoods.setSkus(mskus);
                        mgoods.setCategory(goods.getCategory());
                        String expressGroups = sku.getExpressGroups();
                        String[] express = expressGroups.split(",");
                        for (int i = 0; i < express.length; i++){
                            if (expressB.contains(express[i])){
                                expressA.add(express[i]);
                            }
                        }
                        mGoodsList.add(mgoods);
                    }
                }
            }else{
                Goods mgoods = new Goods();
                List<Sku> mskus = new ArrayList<Sku>();
                mgoods.setIds(goods.getIds());
                mgoods.setSupercoverpath(goods.getSupercoverpath());
                mgoods.setCoverpath(goods.getCoverpath());
                mskus.add(goods.getSkus().get(0));
//                mgoods.setBuyNum(goods.getSkus().get(0).getNumber());
                mgoods.setUniformprice(goods.getSkus().get(0).getUniformprice());
                mgoods.setMarketprice(goods.getSkus().get(0).getMarketprice());
                mgoods.setGoodsname(goods.getGoodsname());
                mgoods.setSkus(mskus);
                mgoods.setCategory(goods.getCategory());
                String expressGroups = goods.getSkus().get(0).getExpressGroups();
                String[] express = expressGroups.split(",");
                for (int i = 0; i < express.length; i++){
                    if (expressB.contains(express[i])){
                        expressA.add(express[i]);
                    }
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

        findView(R.id.ll_coupon).setOnClickListener(this);
        tv_select_coupon = findView(R.id.tv_select_coupon);

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
                        expressValue = data.getStringExtra("expressValue");
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
                boolean chooseAdress = isChooseAdress();
                if (!chooseAdress)
                    return;
                Intent intent1 = new Intent(this, ExpressSelectorActivity.class);
                ArrayList<String> express = new ArrayList<>();
                express.addAll(expressA);
                if (expressA.size() <= 0){
                    ViewUtils.showToastSuccess(R.string.express_no);
                    return;
                }
                intent1.putStringArrayListExtra("express", express);
                startActivityForResult(intent1, REQUEST_CODE_EXPRESS_SELECT);
                break;
            case R.id.tv_submit:
                submitOrder();
                break;
            case R.id.ll_use_default_addr:
                break;
            case R.id.ll_coupon:
                boolean chooseAdress2 = isChooseAdress();
                if (!chooseAdress2)
                    return;
                boolean b = chooseExpress();
                if (!b)
                    return;
                showWindow();
                break;
            default:
                break;
        }
    }

    CouponWindow.CouponInterFace couponInterFace = new CouponWindow.CouponInterFace() {
        @Override
        public void getCoupon(Coupon.ListBean couponAdapterItem) {
            if (couponAdapterItem == null){
                couponUserId = "";
                rule = 0;
                char symbol = 165;
                mTvTotalPrice.setText(String.valueOf(symbol) + String.format("%.2f",mTotalPrice));
                tv_select_coupon.setText(R.string.choose_coupon_type);
            }else {
                couponUserId = couponAdapterItem.getCouponUser().getId();
                rule = couponAdapterItem.getCoupon().getRule();
                char symbol = 165;
                mTvTotalPrice.setText(String.valueOf(symbol) + String.format("%.2f",computeGoodsPrice() - rule < 0 ? mTotalPrice - computeGoodsPrice() : mTotalPrice - rule));
                tv_select_coupon.setText("-￥"+couponAdapterItem.getCoupon().getRule());
            }
        }
    };

    private void showWindow() {
        CouponWindow window = new CouponWindow(AddOrderActivity.this,couponInterFace,computeGoodsPrice());
        window.showAtLocation(findView(R.id.ll_addorder), Gravity.BOTTOM, 0, 0);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (alpha > 0.5f) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;

                    alpha -= 0.01f;
                    msg.obj = alpha;
                    mHandler.sendMessage(msg);
                }
            }

        }).start();


        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        while (alpha < 1f) {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Message msg = mHandler.obtainMessage();
                            msg.what = 1;
                            alpha += 0.01f;
                            msg.obj = alpha;
                            mHandler.sendMessage(msg);
                        }
                    }

                }).start();

            }
        });
    }

    private void changAlpha(float v) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = v; //0.0-1.0
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
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

        //清除快递  红包
        mTvSelectExpress.setText("");
        mTvSelectExpress.setTag(null);
        tv_select_coupon.setText("");

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
//            mAddOrderPresenter.expressCompute(cityId, expressId,mGoodsList);
            mAddOrderPresenter.expressCompute(cityId, expressValue,expressId,mGoodsList);
        }
    }

    @Override
    public void onExpressCompute(List<Double> expressPrice) {
        if (expressPrice == null || mGoodsList.size() <= 0) {
            ViewUtils.showToastError(R.string.failed_compute_price);
            mTvSelectExpress.setText("");
            mTvSelectExpress.setTag(null);
            mTvExpressPrice.setText("");
            char symbol = 165;
            double totalPrice = computeGoodsPrice();
            mTvTotalPrice.setText(String.valueOf(symbol) + totalPrice);
            return;
        }

        if (expressPrice.size() == 1){
            ViewUtils.showToastError(R.string.express_support);
            mTvSelectExpress.setText("");
            mTvSelectExpress.setTag(null);
            mTvExpressPrice.setText("");
            char symbol = 165;
            double totalPrice = computeGoodsPrice();
            mTvTotalPrice.setText(String.valueOf(symbol) + totalPrice);
            return;
        }

        char symbol = 165;
        mTotalPrice = computeGoodsPrice() + expressPrice.get(0) + expressPrice.get(1);
        mTvTotalPrice.setText(String.valueOf(symbol) + String.format("%.2f",mTotalPrice));
        String expressPriceStr = String.valueOf(symbol) + expressPrice.get(0);
        mTvExpressPrice.setText(getString(R.string.express_price, expressPriceStr));
    }

    @Override
    public void onAddOrder(OrderDetailResult result) {

        if (result == null || TextUtils.isEmpty(result.getCreateDate()) || TextUtils.isEmpty(result.getOrderNo()) ) {
            ViewUtils.showToastError(R.string.failed_add_order);
            return;
        }

        SPUtils.clearBuyType();
        ShopCart.clearCart();
        RxBus.getDefault().post(EventType.TYPE_ADD_ORDER);
        Intent intent = new Intent(this, PayActivity.class);
//        intent.putExtra("payMode", result.getPayMode());
        intent.putExtra("orderNum", result.getOrderNo());
        intent.putExtra("orderCreateDate", result.getCreateDate());
        intent.putExtra("orderTotalPrice", new Double(String.format("%.2f",computeGoodsPrice() - rule < 0 ? mTotalPrice - computeGoodsPrice() : mTotalPrice - rule)));
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

    private boolean isChooseAdress(){
        if (TextUtils.isEmpty(mEtTakeUser.getText().toString().trim())) {
            ViewUtils.showToastError(R.string.input_tip_take_user);
            return false;
        }

        String phone = mEtTakePhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || phone.length() != 11 || !phone.startsWith("1")) {
            ViewUtils.showToastError(R.string.input_tip_take_phone);
            return false;
        }

        String Regionid = mTvCitySelect.getTag().toString().trim();
        if (TextUtils.isEmpty(Regionid)) {
            ViewUtils.showToastError(R.string.input_tip_take_city);
            return false;
        }

        String detailadress = mEtTakeDetailAddress.getText().toString().trim();
        if (TextUtils.isEmpty(detailadress)) {
            ViewUtils.showToastError(R.string.input_tip_take_detail_address);
            return false;
        }
        return true;
    }

    private boolean chooseExpress(){
        String expressId = mTvSelectExpress.getText() == null ? "" : mTvSelectExpress.getText().toString();
        if (TextUtils.isEmpty(expressId)) {
            ViewUtils.showToastInfo(R.string.input_tip_select_express);
            return false;
        }
        return true;
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

        boolean chooseAdress = isChooseAdress();
        if (!chooseAdress)
            return;

        boolean b = chooseExpress();
        if (!b)
            return;

        String expressId = mTvSelectExpress.getText() == null ? "" : mTvSelectExpress.getText().toString();

        boolean newAddresss = mAddress == null
                || !mAddress.getName().equals(address.getName())
                || !mAddress.getPhone().equals(address.getPhone())
                || !mAddress.getAddress().equals(address.getAddress())
                || !mAddress.getAreaId().equals(address.getAreaId());
        mAddOrderPresenter.checkGoodsStock(mGoodsList);
        if (newAddresss) {
//            mAddOrderPresenter.addOrder(mGoodsList, address, mCbAddFreqAddr.isChecked(), expressId, mEtRemark.getText().toString().trim());
            mAddOrderPresenter.addOrder(mGoodsList, address, mCbAddFreqAddr.isChecked(), expressValue,expressId, mEtRemark.getText().toString().trim(),couponUserId);
        } else {
//            mAddOrderPresenter.addOrder(mGoodsList, mAddress, mCbAddFreqAddr.isChecked(), expressId, mEtRemark.getText().toString().trim());
            mAddOrderPresenter.addOrder(mGoodsList, mAddress, mCbAddFreqAddr.isChecked(), expressValue,expressId, mEtRemark.getText().toString().trim(),couponUserId);
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
