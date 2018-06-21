package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.adapter.OrderDetailGoodsAdapter;
import com.baigu.dms.common.utils.DateUtils;
import com.baigu.dms.common.utils.OrderUtils;
import com.baigu.dms.common.utils.StringUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.utils.rxbus.EventType;
import com.baigu.dms.common.utils.rxbus.RxBus;
import com.baigu.dms.common.view.ConfirmDialog;
import com.baigu.dms.domain.model.Order;
import com.baigu.dms.domain.model.OrderDetail;
import com.baigu.dms.domain.netservice.common.model.OrderDetailResult;
import com.baigu.dms.presenter.OrderDetailPresenter;
import com.baigu.dms.presenter.impl.OrderDetailPresenterImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * @Description 订单详情
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/8 16:56
 */
public class OrderDetailActivity extends BaseActivity implements OrderDetailPresenter.OrderDetailView, View.OnClickListener {

    private ImageView mIvStatus;
    private TextView mTvStatus;
    private TextView mTvTakeUser;
    private TextView mTvTakePhone;
    private TextView mTvTakeAddress;
    private TextView mTvTotalPrice;
    private TextView mTvCouponPrice;
    private TextView mTvGoodsTotalPrice;
    private TextView mTvExpressPrice;
    private TextView mTvOrderNo;
    private TextView mTvOrderCreateTime;
    private TextView mTvExpressName;
    private TextView mTvRemark;
    private TextView mTvReason;
    private TextView mTvCancelOrder;
    private TextView mTvRefundOrder;
    private TextView mTvPayNow;
    private TextView mTvLogisticsQuery;
    private View mLlRemark;
    private View mllReason;
    private View mLlBottom;


    private OrderDetailGoodsAdapter mOrderDetailGoodsAdapter;
    private OrderDetailPresenter mOrderDetailPresenter;
    private ConfirmDialog mConfirmDialog;
    private OrderDetailResult mOrderDetailResult;
    private ConfirmDialog mRefundConfirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initToolBar();
        setTitle(R.string.order_detail);

        initView();
        String orderId = getIntent().getStringExtra("orderId");
        String orderDate = getIntent().getStringExtra("orderDate");
        mOrderDetailPresenter = new OrderDetailPresenterImpl(this, this);
        mOrderDetailPresenter.loadOrderDetail(orderId, orderDate);
    }

    private void initView() {
        mTvStatus = findView(R.id.tv_status);
        mIvStatus = findView(R.id.iv_status);
        mTvTakeUser = findView(R.id.tv_take_user);
        mTvTakePhone = findView(R.id.tv_take_phone);
        mTvTakeAddress = findView(R.id.tv_take_address);
        mTvTotalPrice = findView(R.id.tv_total_price);
        mTvCouponPrice = findView(R.id.tv_coupon_price);
        mTvGoodsTotalPrice = findView(R.id.tv_goods_total_price);
        mTvExpressPrice = findView(R.id.tv_express_price);
        mTvOrderNo = findView(R.id.tv_order_no);
        mTvOrderCreateTime = findView(R.id.tv_order_create_time);
        mTvExpressName = findView(R.id.tv_express_name);
        mTvRemark = findView(R.id.tv_remark);
        mTvReason=findView(R.id.tv_reason);
        mTvCancelOrder = findView(R.id.tv_cancel_order);
        mTvRefundOrder = findView(R.id.tv_refund);
        mTvPayNow = findView(R.id.tv_pay);
        mLlRemark = findView(R.id.ll_remark);
        mllReason=findView(R.id.ll_reason);
        mTvLogisticsQuery = findView(R.id.tv_logistics_query);
        mTvLogisticsQuery.setOnClickListener(this);
        mLlBottom = findViewById(R.id.ll_bottom);
        mTvPayNow.setOnClickListener(this);
        mTvCancelOrder.setOnClickListener(this);
        mTvRefundOrder.setOnClickListener(this);

        final TextView tvCopy = findView(R.id.tv_copy);
        tvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtils.copy(OrderDetailActivity.this, mTvOrderNo.getText().toString().trim());
                ViewUtils.showToastSuccess(R.string.success_copy);
            }
        });

        RecyclerView rvGoods = findView(R.id.rv_goods);
        rvGoods.setHasFixedSize(true);
        rvGoods.setNestedScrollingEnabled(false);
        rvGoods.setLayoutManager(new LinearLayoutManager(this));
        mOrderDetailGoodsAdapter = new OrderDetailGoodsAdapter(this);
        rvGoods.setAdapter(mOrderDetailGoodsAdapter);
    }

    @Override
    public void onClick(View view) {
        if (ViewUtils.isFastClick()) return;
        switch (view.getId()) {
            case R.id.tv_pay:
                Intent intent = new Intent(this, PayActivity.class);
                intent.putExtra("orderId", mOrderDetailResult.getId());
                intent.putExtra("orderNum", mOrderDetailResult.getOrderNo());
                intent.putExtra("orderCreateDate", mOrderDetailResult.getCreateTime());
                Double discountPrice = Double.valueOf(mOrderDetailResult.getDiscountPrice() == null ? "0" : mOrderDetailResult.getDiscountPrice());
                Double totalPrice = Double.valueOf(mOrderDetailResult.getTotalPrice());
                Double goodsPrice = Double.valueOf(mOrderDetailResult.getGoodsPrice());
                intent.putExtra("orderTotalPrice", discountPrice > goodsPrice ? (totalPrice - goodsPrice) : (totalPrice - discountPrice));
                startActivity(intent);
                break;
            case R.id.tv_cancel_order:
                if (mConfirmDialog == null) {
                    mConfirmDialog = new ConfirmDialog(OrderDetailActivity.this, R.string.cancel_order_confirm);
                }
                mConfirmDialog.setOnConfirmDialogListener(new ConfirmDialog.OnConfirmDialogListener() {
                    @Override
                    public void onOKClick(View v) {
                        mConfirmDialog.dismiss();

                        if (mOrderDetailPresenter != null) {
                            mOrderDetailPresenter.cancelOrder(mOrderDetailResult.getId(), StringUtils.getOrderDate(DateUtils.longToStr(Long.valueOf(mOrderDetailResult.getCreateTime()), new SimpleDateFormat("yyyyMM"))));
                        }
                    }
                });
                mConfirmDialog.show();
                break;
            case R.id.tv_refund:
                if (mRefundConfirmDialog == null) {
                    mRefundConfirmDialog = new ConfirmDialog(this, R.string.order_refund_confirm);
                }
                mRefundConfirmDialog.setOnConfirmDialogListener(new ConfirmDialog.OnConfirmDialogListener() {
                    @Override
                    public void onOKClick(View v) {
                        mRefundConfirmDialog.dismiss();
                        Intent intent1 = new Intent(OrderDetailActivity.this, RefundResonActivity.class);
                        intent1.putExtra("orderId", mOrderDetailResult.getId());
                        intent1.putExtra("orderCreateTime", mOrderDetailResult.getCreateTime());
                        intent1.putExtra("type", RefundResonActivity.REQUEST_DETAIL);
                        startActivityForResult(intent1, RefundResonActivity.REQUEST_CODE);
//
                    }
                });
                mRefundConfirmDialog.show();
                break;
            case R.id.tv_logistics_query:
                Intent intent2 = new Intent(OrderDetailActivity.this,KuaiDiDetailActivity.class);
                intent2.putStringArrayListExtra("logisticsNos", (ArrayList<String>) mOrderDetailResult.getLogisticsNos());
                startActivity(intent2);
//                mOrderDetailPresenter.queryLogistics(mOrderDetailResult.getId(), StringUtils.getOrderDate(DateUtils.longToStr(Long.valueOf(mOrderDetailResult.getCreateTime()), new SimpleDateFormat("yyyyMM"))));
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoadOrderDetail(OrderDetailResult orderDetailResult) {
        mOrderDetailResult = orderDetailResult;
        if (mOrderDetailResult != null && mOrderDetailResult.getGoodsList() != null) {
            mOrderDetailGoodsAdapter.setData(mOrderDetailResult.getGoodsList());
            mOrderDetailGoodsAdapter.notifyDataSetChanged();
            char symbol = 165;
            mTvStatus.setText(OrderUtils.getStatusLabel(this, Integer.valueOf(mOrderDetailResult.getStatus())));
            mIvStatus.setBackgroundResource(OrderUtils.getStatusBgResId(this, Integer.valueOf(mOrderDetailResult.getStatus())));
            mTvTakeUser.setText(getString(R.string.take_user_label, mOrderDetailResult.getConsigneeName()));
            mTvTakePhone.setText(getString(R.string.take_phone_label, mOrderDetailResult.getConsigneePhone()));
            mTvTotalPrice.setText(String.valueOf(symbol) + String.valueOf(mOrderDetailResult.getTotalPrice()));
            mTvCouponPrice.setText(String.valueOf(symbol) + orderDetailResult.getDiscountPrice() == null ? "0" : orderDetailResult.getDiscountPrice());
            mTvGoodsTotalPrice.setText(String.valueOf(symbol) + String.valueOf(mOrderDetailResult.getGoodsPrice()));
            mTvExpressPrice.setText(String.valueOf(symbol) + String.valueOf(mOrderDetailResult.getExpressPrice()));
            mTvOrderNo.setText(mOrderDetailResult.getOrderNo());
            mTvOrderCreateTime.setText(DateUtils.longToStr(Long.valueOf(mOrderDetailResult.getCreateTime()), new SimpleDateFormat("yyyy-MM-dd")));
            mTvExpressName.setText(mOrderDetailResult.getExpressName());
            mTvTakeAddress.setText(getString(R.string.take_address_label, mOrderDetailResult.getConsigneeAddress()));
//            mTvTakeAddress.setText(getString(R.string.take_address_label, order.getProvince() + order.getCity() + order.getArea() + order.getReceiptaddress()));
            mTvRemark.setText(mOrderDetailResult.getRemark());
            mLlRemark.setVisibility(TextUtils.isEmpty(mOrderDetailResult.getRemark()) ? View.GONE : View.VISIBLE);
//            mLlRemark.setVisibility(View.VISIBLE);

            int sta = Integer.valueOf(mOrderDetailResult.getStatus());
            if (sta == OrderUtils.UNPAY) {
                mTvCancelOrder.setVisibility(View.VISIBLE);
                mTvPayNow.setVisibility(View.VISIBLE);
                mTvRefundOrder.setVisibility(View.GONE);
            } else if (sta == OrderUtils.UNDELIVER) {
                mTvCancelOrder.setVisibility(View.GONE);
                mTvPayNow.setVisibility(View.GONE);
                mTvRefundOrder.setVisibility(View.VISIBLE);
                mTvLogisticsQuery.setVisibility(View.GONE);
            } else if (sta == OrderUtils.REFUND_APPLY_FOR || sta == OrderUtils.REFUND_APPLY || sta == OrderUtils.REFUNDED) {
                mTvReason.setText(mOrderDetailResult.getRefundReason());
                mllReason.setVisibility(View.VISIBLE);
                mTvCancelOrder.setVisibility(View.GONE);
                mTvRefundOrder.setVisibility(View.GONE);
                mTvPayNow.setVisibility(View.GONE);
            } else {
                mTvCancelOrder.setVisibility(View.GONE);
                mTvPayNow.setVisibility(View.GONE);
                mTvRefundOrder.setVisibility(View.GONE);
            }
            mTvLogisticsQuery.setVisibility(mOrderDetailResult.getLogisticsNos().size() > 0 ? View.VISIBLE : View.GONE);
//            mTvLogisticsQuery.setVisibility(sta == OrderUtils.DELIVER ? View.VISIBLE : View.GONE);
            mLlBottom.setVisibility(mTvCancelOrder.getVisibility() == View.GONE && mTvPayNow.getVisibility() == View.GONE && mTvRefundOrder.getVisibility() == View.GONE ? View.GONE : View.VISIBLE);

        } else {
            ViewUtils.showToastError(R.string.failed_load_order_info);
            finish();
        }
    }

    @Override
    public void onRefundOrder(boolean result) {
        if (result) {
            ViewUtils.showToastSuccess(R.string.success_refund_order);
            RxBus.getDefault().post(EventType.TYPE_REFUND_ORDER);
            finish();
        } else {
            ViewUtils.showToastError(R.string.failed_refund_order);
        }
    }

    @Override
    public void onCancelOrder(boolean result) {
        if (result) {
            ViewUtils.showToastSuccess(R.string.success_cancel_order);
            RxBus.getDefault().post(EventType.TYPE_CANCEL_ORDER);
            finish();
        } else {
            ViewUtils.showToastError(R.string.failed_cancel_order);
        }
    }

    @Override
    public void onQueryLogistics(String result) {
        if (TextUtils.isEmpty(result)) {
            ViewUtils.showToastError(R.string.failed_query_logistics);
            return;
        } else if (StringUtils.isNetUrl(result)) {
            Intent intent = new Intent(this, LogisticsWebActivity.class);
            intent.putExtra("url", result);
            intent.putExtra("title", getString(R.string.logistic_info));
            startActivity(intent);
        } else {
            ViewUtils.showToastError(result);
            return;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RefundResonActivity.REQUEST_CODE && resultCode == RefundResonActivity.RESULT_CODE) {
            finish();
        }
    }
}
