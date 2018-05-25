package com.baigu.dms.common.view.imagepicker;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;


import com.baigu.dms.R;
import com.baigu.dms.adapter.BaseRVAdapter;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.HorizontalItemDecoration;
import com.baigu.dms.common.view.OnRVItemClickListener;
import com.baigu.dms.common.view.imagepicker.adapter.ImagePickerFolderAdapter;
import com.baigu.dms.common.view.imagepicker.model.SDFolder;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/28 22:18
 */
public class ImagePickerFolderView extends PopupWindow implements OnRVItemClickListener {

    private ImagePickerFolderViewListener mImagePickerFolderViewListener;

    private ImagePickerFolderAdapter mFolderAdapter;

    public ImagePickerFolderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public ImagePickerFolderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImagePickerFolderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ImagePickerFolderView(Context context) {
        super(context);
        init(context);
    }

    public void setImagePickerFolderViewListener(ImagePickerFolderViewListener listener) {
        mImagePickerFolderViewListener = listener;
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_image_picker_folder, null);
        setContentView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        int heigth = ViewUtils.getScreenInfo(context).heightPixels - ViewUtils.dip2px(50) - ViewUtils.getStatusBarHeight(context);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(heigth);
        this.setFocusable(true);
//        this.setAnimationStyle(R.style.PopWinBottom);
        ColorDrawable colorDrawable = new ColorDrawable(context.getResources().getColor(R.color.image_picker_folder_bg));
        setBackgroundDrawable(colorDrawable);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_folder);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new HorizontalItemDecoration(context));
        LinearLayout layoutFolder = (LinearLayout) view.findViewById(R.id.layout_folder);
        RelativeLayout.LayoutParams rvLayoutParams = (RelativeLayout.LayoutParams) layoutFolder.getLayoutParams();
        rvLayoutParams.topMargin = ViewUtils.dip2px(95);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new ImagePickerItemDecoration(2));

        mFolderAdapter = new ImagePickerFolderAdapter(context);
        mFolderAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mFolderAdapter);
    }

    public void setData(List<SDFolder> list) {
        mFolderAdapter.setData(list);
        mFolderAdapter.notifyDataSetChanged();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        if (mImagePickerFolderViewListener != null) {
            mImagePickerFolderViewListener.onFolderViewShowed();
        }
    }

    public void setSelectedFolder(SDFolder folder) {
        mFolderAdapter.setSelectedFolder(folder);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mImagePickerFolderViewListener != null) {
            mImagePickerFolderViewListener.onFolderViewDismissed();
        }
    }

    @Override
    public void onItemClick(BaseRVAdapter adapter, int position) {
        SDFolder folder = (SDFolder) adapter.getItem(position);
        if (mImagePickerFolderViewListener != null) {
            mImagePickerFolderViewListener.onFolderSelected(folder);
        }
        dismiss();
    }

    public interface ImagePickerFolderViewListener {
        void onFolderViewShowed();
        void onFolderViewDismissed();
        void onFolderSelected(SDFolder folder);
    }

}
