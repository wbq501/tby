package com.baigu.dms.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.LoadingDialog;
import com.hedgehog.ratingbar.RatingBar;
import com.hyphenate.chat.Message;
import com.hyphenate.helpdesk.callback.Callback;
import com.hyphenate.helpdesk.model.AgentInfo;
import com.hyphenate.helpdesk.model.MessageHelper;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class EvaluationActivity extends BaseActivity implements View.OnClickListener {

    private RatingBar mRattingBar;
    private EditText mEtMessage;
    private LoadingDialog mLoadingDialog;
    private TextView mTvNickName;

    private Message mMessage;
    private float score = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        initToolBar();
        setTitle(R.string.evalutaion_title);
        mMessage = getIntent().getParcelableExtra("message");
        if (mMessage == null) {
            finish();
            return;
        }
        initView();
    }

    private void initView() {
        mTvNickName = findView(R.id.tv_nickname);

        String title = getString(R.string.app_name) + getString(R.string.official_custmer);
        mTvNickName.setText(title);

        mEtMessage = findView(R.id.et_message);
        mRattingBar = findView(R.id.ratingbar);
        mRattingBar.setStar(score);
        mRattingBar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float ratingCount) {
                score = ratingCount;
            }
        });
        findViewById(R.id.tv_submit).setOnClickListener(this);

//        AgentInfo agentInfo = MessageHelper.getAgentInfo(mMessage);
//        mTvNickName.setText(mMessage.from());
//        if (agentInfo != null){
//            if (!TextUtils.isEmpty(agentInfo.getNickname())) {
//                mTvNickName.setText(agentInfo.getNickname());
//            }
//        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            ViewUtils.hideKeyboard(this);
            return super.dispatchTouchEvent(ev);
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                submit();
                break;
            default:
                break;
        }
    }

    private void submit() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.show();
        MessageHelper.sendEvalMessage(mMessage.messageId(), String.valueOf((int) score), mEtMessage.getText().toString(), new Callback() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                            mLoadingDialog.dismiss();
                        }
                        ViewUtils.showToastSuccess(R.string.success_evaluation);
                        setResult(RESULT_OK);
                        finish();
                    }
                });
            }

            @Override
            public void onError(int code, String error) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                            mLoadingDialog.dismiss();
                        }
                        ViewUtils.showToastSuccess(R.string.failed_evaluation);
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }
}
