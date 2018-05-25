package com.baigu.dms.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.model.BrandQuestion;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.presenter.BrandQuestionPresenter;
import com.baigu.dms.presenter.impl.BrandQuestionPresenterImpl;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * @Description 品牌问答详情
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class BrandQuestionDetailActivity extends BaseActivity implements BrandQuestionPresenter.BrandQuestionView {

    private TextView tvQuestionTitle;
    private TextView tvQuestionContent;
    private TextView tvAnswer;
    private TextView mTvAnswerTip;

    private BrandQuestionPresenter mBrandQuestionPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_question_detail);
        initToolBar();
        setTitle(R.string.brand_question_detail);
        initView();
        final String id = getIntent().getStringExtra("questionId");
        if (TextUtils.isEmpty(id)) {
            ViewUtils.showToastError(R.string.failed_load_brand_question_failed);
            finish();
            return;
        }
        mBrandQuestionPresenter = new BrandQuestionPresenterImpl(this, this);
        Flowable.timer(300L, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                      mBrandQuestionPresenter.getBrandQuestionDetail(id);
                    }
                });
    }

    private void initView() {
        this.tvQuestionTitle = (TextView) findViewById(R.id.tv_question_title);
        this.tvQuestionContent = (TextView) findViewById(R.id.tv_question_content);
        this.tvAnswer = (TextView) findViewById(R.id.tv_answer);
        this.mTvAnswerTip = (TextView) findViewById(R.id.tv_answer_tip);
    }

    @Override
    public void onGetBrandQuestionDetail(BrandQuestion question) {
        if (question == null) {
            ViewUtils.showToastError(R.string.failed_load_brand_question_failed);
            finish();
            return;
        }
        tvQuestionTitle.setText(question.getBrandTitle());
        tvQuestionContent.setText(getString(R.string.question, question.getBrandTitle()));
        tvAnswer.setText("答："+question.getBrandBrief());
        mTvAnswerTip.setVisibility(View.GONE);
    }

    @Override
    public void onGetBrandQuestion(PageResult<BrandQuestion> pageResult) {

    }
}
