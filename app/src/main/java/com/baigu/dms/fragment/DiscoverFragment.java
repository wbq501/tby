package com.baigu.dms.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.baigu.dms.R;
import com.baigu.dms.activity.BlogActivity;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.X5WebView;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/24 9:54
 */
public class DiscoverFragment extends TabFragment {

    private Toolbar mToolbar;
    private ImageView mIvBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        initView(view);
        mToolbar.setTitle(R.string.discover);
        mIvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BlogActivity.class);
                intent.putExtra("title",getString(R.string.blogweb));
                startActivity(intent);
            }
        });
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        return view;
    }

    private void initView(View view) {
        mToolbar = findView(view, R.id.toolbar);
        mIvBtn=findView(view,R.id.btn_web);
    }

    @Override
    public void onTabChecked(boolean checked) {
        super.onTabChecked(checked);
    }
}
