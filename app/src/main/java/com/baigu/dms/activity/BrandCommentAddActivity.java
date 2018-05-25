package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.DateUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.utils.rxbus.EventType;
import com.baigu.dms.common.utils.rxbus.RxBus;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.Comment;
import com.baigu.dms.presenter.BrandCommentPresenter;
import com.baigu.dms.presenter.impl.BrandCommentPresenterImpl;

import java.util.Date;

/**
 * @Description 添加评论
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class BrandCommentAddActivity extends BaseActivity implements BrandCommentPresenter.CommentView {

    private String mBrandId;
    private EditText mEtComment;

    private BrandCommentPresenter mBrandCommentPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_comment_add);
        initToolBar();
        setTitle(R.string.add_comment);
        mEtComment = findView(R.id.et_comment);
        mBrandId = getIntent().getStringExtra("brandId");
        if (TextUtils.isEmpty(mBrandId)) {
            finish();
            return;
        }
        mBrandCommentPresenter = new BrandCommentPresenterImpl(this, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ok, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ViewUtils.isFastClick()) {
            return super.onOptionsItemSelected(item);
        }
        int id = item.getItemId();
        if (id == R.id.action_ok) {
            save();
        }
        return super.onOptionsItemSelected(item);
    }

    private void save() {
        if (TextUtils.isEmpty(mEtComment.getText().toString().trim())) {
            ViewUtils.showToastError(R.string.input_tip_comment);
            return;
        }
        mBrandCommentPresenter.addComment(mBrandId, mEtComment.getText().toString().trim());
    }

    @Override
    public void onAddComment(String result) {
        if (TextUtils.isEmpty(result)) {
            ViewUtils.showToastError(R.string.failed_add_comment);
            return;
        }
        RxBus.getDefault().post(EventType.TYPE_COMMENT_ADDED, mBrandId);
        Intent intent = getIntent();
        Comment comment = new Comment();
        comment.setIds(result);
        comment.setPhoto(UserCache.getInstance().getUser().getPhoto());
        comment.setContent(mEtComment.getText().toString().trim());
        comment.setMembername(UserCache.getInstance().getUser().getNick());
        comment.setCreate_time(DateUtils.dateToStr(new Date(), DateUtils.sYMDHMFormat.get()));
        intent.putExtra("comment", comment);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBrandCommentPresenter != null) {
            mBrandCommentPresenter.onDestroy();
        }
    }
}
