package com.baigu.dms.common.view.imagepicker.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;


import com.baigu.dms.BaseApplication;
import com.baigu.dms.R;
import com.baigu.dms.adapter.BaseRVAdapter;
import com.baigu.dms.common.utils.Constants;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.OnRVItemClickListener;
import com.baigu.dms.common.view.imagepicker.ImagePickerActivity;
import com.baigu.dms.common.view.imagepicker.model.SDFile;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/28 22:18
 */
public class ImagePickerAdapter extends BaseRVAdapter<SDFile> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_CAMERA = 2;

    private int mColumnWidth;
    private int mColumnHeight;
    private int mMaxSelect = Constants.IMAGE_PICKER_MAX_SELECT;
    private OnImagePickerListener mOnImagePickerListener;
    private OnRVItemClickListener mOnRVItemClickListener;
    private boolean mEnableMultiSelect = true;

    /**上次已选择的图片数量*/
    private int mPreSelectedCount;

    private List<SDFile> mSelectedList = new ArrayList<SDFile>();

    public void setColumnSize(int width, int height) {
        mColumnWidth = width;
        mColumnHeight = height;
    }

    public void setMaxSelect(int maxSelect) {
        mMaxSelect = maxSelect;
    }

    public void setOnImagePickerListener(OnImagePickerListener listener) {
        mOnImagePickerListener = listener;
    }

    public void setOnRVItemClickListener(OnRVItemClickListener listener) {
        mOnRVItemClickListener = listener;
    }

    public void setPreSelectedCount(int count) {
        mPreSelectedCount = count;
    }

    public void setEnableMultiSelect(boolean b) {
        mEnableMultiSelect = b;
    }

    @Override
    public void setData(List<SDFile> list) {
        super.setData(list);
    }

    public List<SDFile> getSelectedList() {
        return mSelectedList;
    }

    public void setSelectedList(List<SDFile> list) {
        mSelectedList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_ITEM ) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img_picker, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_CAMERA )  {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img_camera, parent, false);
            return new CameraViewHolder(view);
        } else {
            throw new RuntimeException("There is no type that matches the type " + viewType + ", make sure your using types correctly");
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = TYPE_ITEM;
        if (getItem(position).getFileType() == ImagePickerActivity.FLAG_CAMERA) {
            type = TYPE_CAMERA;
        }
        return type;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.getLayoutParams().width = mColumnWidth;
        holder.itemView.getLayoutParams().height = mColumnHeight;

        SDFile sdFile = getItem(position);
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.iv.getLayoutParams().width = mColumnWidth;
            itemViewHolder.iv.getLayoutParams().height = mColumnHeight;
            Glide.with(holder.itemView.getContext()).load(sdFile.getThumbUri()).placeholder(R.mipmap.pictures_placeholder).diskCacheStrategy(DiskCacheStrategy.ALL).into(itemViewHolder.iv);

            itemViewHolder.cbSel.setChecked(mSelectedList.contains(sdFile));
            itemViewHolder.layoutSel.setOnClickListener(new OnLayoutSelClickListener(sdFile, itemViewHolder.cbSel));
        } else {
            CameraViewHolder cameraViewHolder = (CameraViewHolder) holder;
            cameraViewHolder.layoutMain.getLayoutParams().width = mColumnWidth;
            cameraViewHolder.layoutMain.getLayoutParams().height = mColumnHeight;
        }
    }

    class OnLayoutSelClickListener implements View.OnClickListener {
        private SDFile sdFile;
        private CheckBox checkBox;

        public OnLayoutSelClickListener(SDFile sdFile, CheckBox checkBox) {
            this.sdFile = sdFile;
            this.checkBox = checkBox;
        }

        @Override
        public void onClick(View v) {
            if (ViewUtils.isFastClick()) return;
            if (!mSelectedList.contains(sdFile)) {
                int maxSelect = mMaxSelect - mPreSelectedCount;
                if (mSelectedList.size() >= maxSelect) {
                    ViewUtils.showToastInfo(BaseApplication.getContext().getString(R.string.over_max_image_select, maxSelect));
                    return;
                }
                mSelectedList.add(sdFile);
            } else {
                mSelectedList.remove(sdFile);
            }
            checkBox.setChecked(mSelectedList.contains(sdFile));
            if (mOnImagePickerListener != null) {
                mOnImagePickerListener.onSelectedItemChanged(mSelectedList);
            }
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv;
        CheckBox cbSel;
        View layoutSel;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.iv = (ImageView) itemView.findViewById(R.id.iv);
            this.cbSel = (CheckBox) itemView.findViewById(R.id.cb_sel);
            this.layoutSel = itemView.findViewById(R.id.layout_sel);
            this.layoutSel.setVisibility(mEnableMultiSelect ? View.VISIBLE : View.GONE);
            this.itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnImagePickerListener != null) {
                mOnImagePickerListener.onItemClick(ImagePickerAdapter.this, getLayoutPosition());
            }
        }
    }

    public class CameraViewHolder extends RecyclerView.ViewHolder {
        View layoutMain;

        public CameraViewHolder(View itemView) {
            super(itemView);
            this.layoutMain = itemView.findViewById(R.id.layout_main);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnImagePickerListener != null) {
                        mOnImagePickerListener.onCameraClick();
                    }
                }
            });
        }
    }

    public interface OnImagePickerListener extends OnRVItemClickListener {
        void onSelectedItemChanged(List<SDFile> list);
        void onCameraClick();
    }
}
