package com.baigu.dms.common.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.activity.MyInfoActivity;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.Advert;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.netservice.URLFactory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/28 22:18
 */
public class MyInfoHeaderView extends FrameLayout {

    private ImageView mIvHead;
    private TextView mTvNickname;
    private TextView mTvPhone;

    public MyInfoHeaderView(@NonNull Context context) {
        super(context);
        initView();
    }

    public MyInfoHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyInfoHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_my_info_header, this);
        View view = findViewById(R.id.layout_main);
        view.getLayoutParams().width = ViewUtils.getScreenInfo(getContext()).widthPixels;
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), MyInfoActivity.class));
            }
        });
        view.findViewById(R.id.fl_recommend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.showToastInfo("新人推荐");
            }
        });
        mIvHead = (ImageView) view.findViewById(R.id.iv_head);
        mTvNickname = (TextView) view.findViewById(R.id.tv_nickname);
        mTvPhone = (TextView) view.findViewById(R.id.tv_phone);
    }

    public void setData(List<Advert> adList) {
        if (adList == null || adList.size() <= 0) {
            setVisibility(View.GONE);
            return;
        }
    }

    public void refreshData() {
        User user = UserCache.getInstance().getUser();
        Glide.with(getContext()).load(user.getPhoto()).placeholder(R.mipmap.default_head).diskCacheStrategy(DiskCacheStrategy.ALL).transform(new GlideCircleTransform(getContext())).into(mIvHead);

        mTvNickname.setText(user.getNick());
        mTvPhone.setText(user.getCellphone());
    }

    public void onUpdateHead() {
        User user = UserCache.getInstance().getUser();
        Glide.with(getContext()).load(user.getPhoto()).placeholder(R.mipmap.default_head).diskCacheStrategy(DiskCacheStrategy.ALL).transform(new GlideCircleTransform(getContext())).into(mIvHead);
    }

}
