package com.baigu.dms.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.activity.AddressAddEditActivity;
import com.baigu.dms.activity.AddressListActivity;
import com.baigu.dms.common.view.ConfirmDialog;
import com.baigu.dms.domain.model.Address;
import com.baigu.dms.presenter.AddressPresenter;

public class AddressAdapter extends BaseRVAdapter<Address> {
    public Activity mActivity;
    private AddressPresenter mAddressPresenter;
    private boolean mSelectAble;
    private ConfirmDialog mConfirmDialog;

    public AddressAdapter(Activity activity, AddressPresenter presenter) {
        mActivity = activity;
        mAddressPresenter = presenter;
    }

    public void setSelectAble(boolean b) {
        mSelectAble = b;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        return new AddressAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Address address = mDataList.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        Context context = itemViewHolder.itemView.getContext();
        itemViewHolder.tvTakeUser.setText(context.getString(R.string.take_user_label, address.getName()));
        itemViewHolder.tvTakePhone.setText(context.getString(R.string.take_phone_label, address.getPhone()));
        itemViewHolder.tvTakeAddress.setText(context.getString(R.string.take_address_label, address.getFullRegionName() + address.getAddress()));
        itemViewHolder.ivArrow.setVisibility(mSelectAble ? View.VISIBLE : View.GONE);
        itemViewHolder.cbDefault.setChecked(address.isDefault());
        itemViewHolder.llEdit.setOnClickListener(new OnAddressManagerClickListener(address));
        itemViewHolder.llDelete.setOnClickListener(new OnAddressManagerClickListener(address));
        itemViewHolder.llCb.setOnClickListener(new OnCheckBoxClickListener(address));
        if (mSelectAble) {
            itemViewHolder.llContent.setOnClickListener(new OnContentClickListener(address));
        } else {
            itemViewHolder.llContent.setOnClickListener(null);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvTakeUser;
        TextView tvTakePhone;
        TextView tvTakeAddress;
        ImageView ivArrow;
        CheckBox cbDefault;
        LinearLayout llCb;
        LinearLayout llEdit;
        LinearLayout llDelete;
        LinearLayout llContent;


        public ItemViewHolder(View itemView) {
            super(itemView);
            tvTakeUser = (TextView) itemView.findViewById(R.id.tv_take_user);
            tvTakePhone = (TextView) itemView.findViewById(R.id.tv_take_phone);
            tvTakeAddress = (TextView) itemView.findViewById(R.id.tv_take_address);
            ivArrow = (ImageView) itemView.findViewById(R.id.iv_arrow);
            llCb = (LinearLayout) itemView.findViewById(R.id.ll_cb);
            cbDefault = (CheckBox) itemView.findViewById(R.id.cb_default);
            llEdit = (LinearLayout) itemView.findViewById(R.id.ll_edit);
            llDelete = (LinearLayout) itemView.findViewById(R.id.ll_delete);
            llContent = (LinearLayout) itemView.findViewById(R.id.ll_content);
        }

    }

    class OnContentClickListener implements View.OnClickListener {
        private Address address;

        public OnContentClickListener(Address address) {
            this.address = address;
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(AddressAdapter.this, mDataList.indexOf(address));
                }
            }
        }
    }


    class OnCheckBoxClickListener implements View.OnClickListener {

        private Address address;

        public OnCheckBoxClickListener(Address address) {
            this.address = address;
        }

        @Override
        public void onClick(View v) {
            address.setDefault(!address.isDefault());
//            address.setShipTo(address.getName());
//            address.setRegionid(address.getAreaId());
            mAddressPresenter.saveOrUpdateAddress(address);
        }
    }

    class OnAddressManagerClickListener implements View.OnClickListener {

        private Address address;

        public OnAddressManagerClickListener(Address address) {
            this.address = address;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_edit:
                    if (mActivity != null) {
                        Intent intent = new Intent(mActivity, AddressAddEditActivity.class);
                        intent.putExtra("address", address);
                        mActivity.startActivityForResult(intent, AddressListActivity.REQUEST_CODE_ADD_UPDATE);
                    }
                    break;
                case R.id.ll_delete:
                    if (mConfirmDialog == null) {
                        mConfirmDialog = new ConfirmDialog(mActivity, "是否删除该地址");
                    }
                    mConfirmDialog.setOnConfirmDialogListener(new ConfirmDialog.OnConfirmDialogListener() {
                        @Override
                        public void onOKClick(View v) {
                            if (mAddressPresenter != null) {
                                mAddressPresenter.deleteAddress(address);
                            }
                            mConfirmDialog.dismiss();
                        }
                    });
                    mConfirmDialog.show();
                    break;
                default:
                    break;
            }
        }
    }
}
