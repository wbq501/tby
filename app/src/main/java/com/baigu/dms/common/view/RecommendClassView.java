package com.baigu.dms.common.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.baigu.dms.R;
import com.baigu.dms.activity.RecommendActivity;
import com.baigu.dms.adapter.RecommendClassAdapter;
import com.baigu.dms.common.utils.OnItemClickListener;
import com.baigu.dms.domain.model.RecommendClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public class RecommendClassView extends LinearLayout implements OnItemClickListener {

    private RecyclerView rv_recommend;
    private RecommendClassAdapter adapter;
    private List<RecommendClass> classes;


    public RecommendClassView(Context context) {
        super(context);
        initView(context);
    }


    public RecommendClassView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RecommendClassView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_recommend_class, this, true);
        rv_recommend = view.findViewById(R.id.rl_class);
        rv_recommend.setLayoutManager(new GridLayoutManager(context, 4));
        adapter = new RecommendClassAdapter(context);
        rv_recommend.addItemDecoration(new SpaceItemDecoration(20));
        adapter.setItemClickListener(this);
        rv_recommend.setAdapter(adapter);

    }

    public void setData(List<RecommendClass> classes) {
        this.classes = classes;
        adapter.setData(classes);
        adapter.notifyDataSetChanged();
    }

    public List<RecommendClass> getClasses() {
        return classes;
    }

    @Override
    public void OnItemClick(View view, int position) {
        if (classes != null && classes.size() > 0) {
            Intent intent = new Intent(getContext(), RecommendActivity.class);
            intent.putExtra("categoryId", classes.get(position).getId());
            intent.putExtra("title", classes.get(position).getName());
            getContext().startActivity(intent);
        }
    }
}
