package com.baigu.dms.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.StringUtils;
import com.baigu.dms.common.view.GlideCircleTransform;
import com.baigu.dms.domain.model.Comment;
import com.baigu.dms.domain.model.Praise;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

public class CommentPraiseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private boolean mCommentItem = true;
    public Activity mActivity;
    private List<Comment> mCommentDataList = new ArrayList<>();
    private List<Praise> mPraiseDataList = new ArrayList<>();

    public CommentPraiseAdapter(Activity activity) {
        mActivity = activity;
    }

    public void selCommentTab(boolean b) {
        mCommentItem = b;
        notifyDataSetChanged();
    }

    public void setCommentData(List<Comment> list) {
        mCommentDataList.clear();
        if (list != null) {
            mCommentDataList.addAll(list);
        }
    }

    public void addComment(Comment comment) {
        mCommentDataList.add(0, comment);
        notifyDataSetChanged();
    }

    public void setPraiseData(List<Praise> list) {
        mPraiseDataList.clear();
        if (list != null) {
            mPraiseDataList.addAll(list);
        }
    }

    public void appendCommentDataList(List<Comment> list) {
        if (list != null) {
            mCommentDataList.addAll(list);
        }
    }

    public void appendPraiseDataList(List<Praise> list) {
        if (list != null) {
            mPraiseDataList.addAll(list);
        }
    }
    public void clearCommentData() {
        mCommentDataList.clear();
    }

    public void clearPraiseData() {
        mPraiseDataList.clear();
    }

    @Override
    public int getItemCount() {
        return mCommentItem ? mCommentDataList.size() : mPraiseDataList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_praise, parent, false);
        return new CommentPraiseAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mCommentItem) {
            Comment comment = mCommentDataList.get(position);
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            Glide.with(mActivity).load(comment.getPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.default_head).transform(new GlideCircleTransform(mActivity)).into(itemViewHolder.ivHead);
            itemViewHolder.tvName.setText(comment.getMembername());
            itemViewHolder.tvCreateTime.setText(StringUtils.getTimeLabelStr(comment.getCreate_time()));
            itemViewHolder.tvContent.setText(comment.getContent());
            itemViewHolder.tvContent.setVisibility(View.VISIBLE);
        } else {
            Praise praise = mPraiseDataList.get(position);
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            Glide.with(mActivity).load(praise.getPhoto()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.default_head).transform(new GlideCircleTransform(mActivity)).into(itemViewHolder.ivHead);
            itemViewHolder.tvName.setText(praise.getMembername());
            itemViewHolder.tvCreateTime.setText(StringUtils.getTimeLabelStr(praise.getCreate_time()));
            itemViewHolder.tvContent.setText("");
            itemViewHolder.tvContent.setVisibility(View.GONE);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHead;
        TextView tvName;
        TextView tvCreateTime;
        TextView tvContent;


        public ItemViewHolder(View itemView) {
            super(itemView);
            ivHead = (ImageView) itemView.findViewById(R.id.iv_head);
            tvName = (TextView) itemView.findViewById(R.id.tv_realname);
            tvCreateTime = (TextView) itemView.findViewById(R.id.tv_create_time);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }
}
