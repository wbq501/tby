package com.baigu.dms.common.view.imagepicker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.adapter.BaseRVAdapter;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.OnRVItemClickListener;
import com.baigu.dms.common.view.imagepicker.model.SDFolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/28 22:18
 */
public class ImagePickerFolderAdapter extends BaseRVAdapter<SDFolder> {

    private SDFolder mSelectedFolder;
    private int mImageSize;
    private OnRVItemClickListener mOnItemClickListener;

    public ImagePickerFolderAdapter(Context context) {
        mImageSize = ViewUtils.dip2px(72);
    }

    public void setOnItemClickListener(OnRVItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setSelectedFolder(SDFolder folder) {
        mSelectedFolder = folder;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_picker_folder, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        SDFolder folder = getItem(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        if (folder.getImageUri() != null) {
            Glide.with(itemViewHolder.itemView.getContext()).load(folder.getImageUri()).placeholder(R.mipmap.pictures_placeholder).diskCacheStrategy(DiskCacheStrategy.ALL).into(((ItemViewHolder) holder).iv);
        } else {
            itemViewHolder.iv.setImageDrawable(null);
        }

        itemViewHolder.cbSel.setVisibility(folder.equals(mSelectedFolder) ? View.VISIBLE : View.INVISIBLE);
        itemViewHolder.tvFolderName.setText(folder.getName());
        String numStr = itemViewHolder.itemView.getContext().getString(R.string.image_num, folder.getCount());
        itemViewHolder.tvImageNum.setText(numStr);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv;
        TextView tvFolderName;
        TextView tvImageNum;
        CheckBox cbSel;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.iv = (ImageView) itemView.findViewById(R.id.iv);
            this.tvFolderName = (TextView) itemView.findViewById(R.id.tv_folder_name);
            this.tvImageNum = (TextView) itemView.findViewById(R.id.tv_image_num);
            this.cbSel = (CheckBox) itemView.findViewById(R.id.cb_sel);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (ViewUtils.isFastClick()) return;
            if (mOnItemClickListener != null) {
                int position = getLayoutPosition();
                mSelectedFolder = getItem(position);
                notifyDataSetChanged();
                mOnItemClickListener.onItemClick(ImagePickerFolderAdapter.this, position);
            }
        }
    }
}
