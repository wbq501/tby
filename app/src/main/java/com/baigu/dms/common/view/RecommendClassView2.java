package com.baigu.dms.common.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.baigu.dms.R;
import com.baigu.dms.domain.model.RecommendClass;

import java.util.List;

public class RecommendClassView2 extends LinearLayout{

    private List<RecommendClass> classes;


    public RecommendClassView2(Context context) {
        super(context);
        initView(context);
    }


    public RecommendClassView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RecommendClassView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_recommend_class, this, true);


    }

    public void setData(List<RecommendClass> classes) {
        this.classes = classes;
    }

    public List<RecommendClass> getClasses() {
        return classes;
    }
}
