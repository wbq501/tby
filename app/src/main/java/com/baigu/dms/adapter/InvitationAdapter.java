package com.baigu.dms.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.presenter.InvitationPresenter;

public class InvitationAdapter extends BaseRVAdapter<User> {
    public Activity mActivity;
    private InvitationPresenter mInvitationPresenter;
    private boolean mUnVerified;

    public InvitationAdapter(Activity activity, InvitationPresenter presenter) {
        mActivity = activity;
        mInvitationPresenter = presenter;
    }

    public void setUnVerified(boolean b) {
        mUnVerified = b;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invitation, parent, false);
        return new InvitationAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        User user = mDataList.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        Context context = itemViewHolder.itemView.getContext();
        itemViewHolder.tvName.setText(context.getString(R.string.name_label, TextUtils.isEmpty(user.getRealName()) ? user.getNick() : user.getRealName()));
        itemViewHolder.tvPhone.setText(context.getString(R.string.phone_label, user.getPhone()));
        if (mUnVerified) {
            itemViewHolder.lineVerify.setVisibility(View.VISIBLE);
            itemViewHolder.llVerify.setVisibility(View.VISIBLE);
            itemViewHolder.tvVerify.setOnClickListener(new OnVerifyClickListener(user));
        } else {
            itemViewHolder.lineVerify.setVisibility(View.GONE);
            itemViewHolder.llVerify.setVisibility(View.GONE);
            itemViewHolder.llVerify.setOnClickListener(null);
        }
    }

    class OnVerifyClickListener implements View.OnClickListener {
        private User user;

        public OnVerifyClickListener(User user) {
            this.user = user;
        }

        @Override
        public void onClick(View view) {
            mInvitationPresenter.verify(user.getId());
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvPhone;
        TextView tvVerify;
        View lineVerify;
        View llVerify;


        public ItemViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_realname);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_phone);
            tvVerify = (TextView) itemView.findViewById(R.id.tv_verify);
            lineVerify = itemView.findViewById(R.id.line_verify);
            llVerify = itemView.findViewById(R.id.ll_verify);
        }
    }
}
