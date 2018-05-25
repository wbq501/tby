package com.baigu.dms.common.view.imagepicker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.adapter.BaseListAdapter;
import com.baigu.dms.common.view.imagepicker.FileChooser;
import com.baigu.dms.common.view.imagepicker.model.SDFile;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


public class ImageAdapter extends BaseListAdapter<SDFile> {

    private int mColumnWidth;
    private int mColumnHeight;

    public ImageAdapter(Context context) {
        super(context);
    }

    public void setColumnSize(int width, int height) {
        mColumnWidth = width;
        mColumnHeight = height;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_image, null);
            holder = new Holder();
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            holder.tvSize = (TextView) convertView.findViewById(R.id.tv_size);
            holder.ivVideo = (ImageView) convertView.findViewById(R.id.iv_video);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        SDFile sdFile = getItem(position);
        holder.iv.getLayoutParams().width = mColumnWidth;
        holder.iv.getLayoutParams().height = mColumnHeight;
        Glide.with(mContext).load(sdFile.getThumbUri()).placeholder(R.mipmap.pictures_placeholder).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv);
        holder.tvSize.setText(sdFile.getSize());

        int visibility = sdFile.getFileType() == FileChooser.FileType.TYPE_VIDEO ? View.VISIBLE : View.GONE;
        holder.ivVideo.setVisibility(visibility);
        if (position == 0) {
            holder.tvSize.setVisibility(View.GONE);
        }

        return convertView;
    }

    class Holder {
        ImageView iv;
        ImageView ivVideo;
        TextView tvSize;
    }
}
