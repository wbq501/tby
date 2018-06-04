package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.CertificationResult;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.CertificationResultPresenter;
import com.baigu.dms.presenter.impl.CertificationResultPresenterImpl;

/**
 * @Description 实名认证结果
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class CertificationResultActivity extends BaseActivity implements View.OnClickListener, CertificationResultPresenter.CertificationResultView {

    private Button mBtnCertificationAgain;
    private CertificationResultPresenter mPresenter;
    private TextView tvStatus;
    private ImageView ivStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification_result);
        initToolBar();
        setTitle(R.string.certification);
        initView();
    }

    private void initView() {
        ivStatus = findView(R.id.iv_status);
        tvStatus = findView(R.id.tv_status);
        mBtnCertificationAgain = findViewById(R.id.btn_certification_again);
        mBtnCertificationAgain.setOnClickListener(this);
        User user = UserCache.getInstance().getUser();

        mPresenter = new CertificationResultPresenterImpl(this, this);
        mPresenter.loadCertificationResult(UserCache.getInstance().getUser().getIds());
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastClick()) return;
        switch (v.getId()) {
            case R.id.btn_certification_again:
                startActivity(new Intent(this, CertificationStep1Activity.class));
            default:
                break;
        }
    }

    @Override
    public void loadResult(BaseResponse<CertificationResult> result) {
        if (result.getCode() == 1) {
            switch (result.getData().getIdCardStatus()) {
                case User.IDCardStatus.VERIFY_DOING:
                    ivStatus.setImageResource(R.mipmap.verify_doing);
                    tvStatus.setText(R.string.certification_doing);
                    break;
                case User.IDCardStatus.VERIFY_FAILED:
                    ivStatus.setImageResource(R.mipmap.verify_failed);
                    tvStatus.setText(getString(R.string.certification_failed)+"\n"+"详细原因为:"+result.getData().getFailReason());
                    mBtnCertificationAgain.setVisibility(View.VISIBLE);
                    break;
                case User.IDCardStatus.VERIFY_SUCCESS:
                    ivStatus.setImageResource(R.mipmap.verify_success);
                    tvStatus.setText(R.string.certification_success);
                    break;
                default:
                    finish();
                    break;
            }
        } else {
            ViewUtils.showToastError(result.getMessage());
        }
    }
}
