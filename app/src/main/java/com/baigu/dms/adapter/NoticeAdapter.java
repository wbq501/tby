package com.baigu.dms.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.DateUtils;
import com.baigu.dms.domain.model.Notice;
import com.baigu.lrecyclerview.recyclerview.LRecyclerViewAdapter;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/21 18:41
 */
public class NoticeAdapter extends BaseRVAdapter<Notice> {

    private Activity mActivity;

    public NoticeAdapter(Activity activity) {
        mActivity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice_selector, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Notice notice = mDataList.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.tvTitle.setText(notice.getBtitle());
        itemViewHolder.tv_time.setText(DateUtils.StingSimpleDateFormat(notice.getCreateTime()));
    }

    public class ItemViewHolder extends LRecyclerViewAdapter.ViewHolder {
        TextView tvTitle,tv_time;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tv_time = itemView.findViewById(R.id.tv_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(NoticeAdapter.this, getAdapterPosition()-1);
                    }
                }
            });
        }
    }
}
