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
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.activity.NoticeDetailActivity;
import com.baigu.dms.activity.NoticeListActivity;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.marqueeview.MarqueeView;
import com.baigu.dms.domain.model.Notice;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/29 0:14
 */
public class NoticeView extends FrameLayout {

    private MarqueeView mMarqueeView;
    private List<Notice> mNoticeList;
    private View mLayoutContainer;

    public NoticeView(@NonNull Context context) {
        super(context);
        initView();
    }

    public NoticeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public NoticeView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setData(List<Notice> list) {
        mNoticeList = list;
        if (mNoticeList == null) {
            mMarqueeView.startWithList(null);
            mLayoutContainer.setVisibility(View.GONE);
            return;
        }
        mLayoutContainer.setVisibility(View.VISIBLE);
        List<CharSequence> marList = new ArrayList<>();
        for (Notice notice : list) {
            marList.add(notice.getBtitle());
        }
        mMarqueeView.startWithList(marList);
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_notice, this);
        mLayoutContainer = findViewById(R.id.layout_container);
        mLayoutContainer.getLayoutParams().width = ViewUtils.getScreenInfo(getContext()).widthPixels;
        mMarqueeView = (MarqueeView) findViewById(R.id.marqueeView);
        mMarqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                if (position < 0 || position >= mNoticeList.size()) {
                    return;
                }
                getContext().startActivity(new Intent(getContext(), NoticeListActivity.class));
//                Intent intent = new Intent(getContext(), NoticeDetailActivity.class);
//                intent.putExtra("notice", mNoticeList.get(position));
//                getContext().startActivity(intent);
            }
        });
//        findViewById(R.id.tv_more).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getContext().startActivity(new Intent(getContext(), NoticeListActivity.class));
//            }
//        });

    }
}
