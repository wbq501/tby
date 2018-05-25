package com.baigu.dms.common.view.imagepicker;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.baigu.dms.R;


/**
 * @Project iSphere
 * @Packate com.hy.imp.main.view.menuitem
 *
 * @Description
 *
 * @Author Micky Liu
 * @Email liuhongwei@isphere.top
 * @Date 2016-08-02 17:21
 * @Company 北京华云合创科技有限公司成都分公司
 * @Copyright Copyright(C) 2016-2018
 * @Version 1.0.0
 */
public class ImagePreviewActionProvider extends ActionProvider implements View.OnClickListener {

    private CheckBox mCheckBox;

    private PreviewActionClickListener mPreviewActionClickListener;

    public ImagePreviewActionProvider(Context context) {
        super(context);
    }

    public void setPreviewActionClickListener(PreviewActionClickListener listener) {
        mPreviewActionClickListener = listener;
    }

    @Override
    public View onCreateActionView() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_image_preview_action, null, false);
        view.setLayoutParams(layoutParams);
        mCheckBox = (CheckBox) view.findViewById(R.id.checkbox);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (mPreviewActionClickListener != null) {
            boolean ableAdd = mPreviewActionClickListener.onPreviewActionSelected(mCheckBox.isChecked());
            if (!ableAdd && !mCheckBox.isChecked()) {
                return;
            }
        }
        mCheckBox.setChecked(!mCheckBox.isChecked());
    }

    public void setPreviewActionSelect(boolean b) {
        mCheckBox.setChecked(b);
    }

    public interface PreviewActionClickListener {
        boolean onPreviewActionSelected(boolean b);
    }
}
