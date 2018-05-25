package com.baigu.dms.presenter.impl;

import android.app.Activity;

import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.presenter.BlogPresenter;

import java.util.List;

public class BlogPresenterImpl extends BasePresenterImpl implements BlogPresenter{

    private BlogView blogView;

    public BlogPresenterImpl(Activity activity,BlogView blogView) {
        super(activity);
        this.blogView = blogView;
    }

    @Override
    public void loadBlog(String send) {
        addDisposable(new BaseAsyncTask<String, Void, List<Goods>>(){

            @Override
            protected RxOptional<List<Goods>> doInBackground(String... strings) {
                return null;
            }

            @Override
            protected void onPostExecute(List<Goods> goods) {
                super.onPostExecute(goods);
                if (blogView != null){
                    blogView.loadGoodList(goods);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (blogView != null){
                    blogView.loadGoodList(null);
                }
            }
        }.execute(send));
    }
}
