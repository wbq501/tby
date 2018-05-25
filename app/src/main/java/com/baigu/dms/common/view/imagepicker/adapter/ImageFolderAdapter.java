package com.baigu.dms.common.view.imagepicker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.adapter.BaseListAdapter;
import com.baigu.dms.common.view.imagepicker.FileChooser;
import com.baigu.dms.common.view.imagepicker.model.SDFolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/28 22:18
 */
public class ImageFolderAdapter extends BaseListAdapter<SDFolder> {

    public ImageFolderAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_image_folder, null);
            holder = new Holder();
            holder.ivFolder = (ImageView) convertView.findViewById(R.id.iv_folder);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_realname);
            holder.tvCount = (TextView) convertView.findViewById(R.id.tv_count);
            holder.ivVideo = (ImageView) convertView.findViewById(R.id.iv_video);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        SDFolder folder = getItem(position);

        holder.tvName.setText(folder.getName());
        holder.tvCount.setText(mContext.getString(R.string.file_count, folder.getCount()));

        Glide.with(mContext).load(folder.getImageUri()).placeholder(R.mipmap.pictures_placeholder).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivFolder);

        int visibility = folder.getFolderType() == FileChooser.FileType.TYPE_VIDEO ? View.VISIBLE : View.GONE;
        holder.ivVideo.setVisibility(visibility);

        return convertView;
    }

    class Holder {
        ImageView ivFolder;
        TextView tvName;
        TextView tvCount;
        ImageView ivVideo;
    }
}
