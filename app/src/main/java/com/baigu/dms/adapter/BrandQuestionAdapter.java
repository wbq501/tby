package com.baigu.dms.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.domain.model.BrandQuestion;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/29 0:28
 */
public class BrandQuestionAdapter extends BaseRVAdapter<BrandQuestion> {

    public Activity mActivity;

    public BrandQuestionAdapter(Activity activity) {
        mActivity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_brand_question, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BrandQuestion brandQuestion = mDataList.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        Context context = itemViewHolder.itemView.getContext();
        itemViewHolder.tvQuestionTitle.setText(brandQuestion.getBrandTitle());
        itemViewHolder.tvQuestionContent.setText(context.getString(R.string.question, brandQuestion.getBrandTitle()));
//        itemViewHolder.tvQuestionContent.setText(context.getString(R.string.question, brandQuestion.getBrandContent()));
        itemViewHolder.tvAnswer.setText(context.getString(R.string.answer, brandQuestion.getBrandBrief()));
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionTitle;
        TextView tvQuestionContent;
        TextView tvAnswer;
        public ItemViewHolder(View itemView) {
            super(itemView);
            this.tvQuestionTitle = (TextView) itemView.findViewById(R.id.tv_question_title);
            this.tvQuestionContent = (TextView) itemView.findViewById(R.id.tv_question_content);
            this.tvAnswer = (TextView) itemView.findViewById(R.id.tv_answer);
        }

    }


}