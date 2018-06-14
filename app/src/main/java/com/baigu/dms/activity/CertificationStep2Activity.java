package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.StringUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.presenter.CertificationPresenter;
import com.baigu.dms.presenter.impl.CertificationPresenterImpl;
import com.github.yoojia.inputs.verifiers.IDCardVerifier;

/**
 * @Description 实名认证
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class CertificationStep2Activity extends BaseActivity implements CertificationPresenter.CertificationView {

    private EditText mEtIDCard;

    private String mIDCardFrontPath;
    private String mIDCardBackPath;
    private CertificationPresenter mCertificationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification_step2);
        initToolBar();
        setTitle(R.string.certification);
        mEtIDCard = findView(R.id.et_idcard);
        mIDCardFrontPath = getIntent().getStringExtra("frontPath");
        mIDCardBackPath = getIntent().getStringExtra("backPath");
        if (TextUtils.isEmpty(mIDCardFrontPath) || TextUtils.isEmpty(mIDCardBackPath)) {
            finish();
            return;
        }
        mCertificationPresenter = new CertificationPresenterImpl(this, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_next, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ViewUtils.isFastClick()) {
            return super.onOptionsItemSelected(item);
        }
        int id = item.getItemId();
        if (id == R.id.action_next) {
            String idCard = mEtIDCard.getText().toString().trim();
            IDCardVerifier idCardVerifier = new IDCardVerifier();
            boolean isIdcard = idCardVerifier.performTestNotEmpty(idCard);
            if (!isIdcard) {
                ViewUtils.showToastError(R.string.input_tip_idcard);
                return super.onOptionsItemSelected(item);
            }
            Intent intent = new Intent(getBaseContext(), UpdatePayPasswdActivity.class);
            intent.putExtra("type", UpdatePayPasswdActivity.TYPE_INITIALIZE);
            intent.putExtra("frontPath",mIDCardFrontPath);
            intent.putExtra("backPath",mIDCardBackPath);
            intent.putExtra("idCard",idCard);
            startActivity(intent);
//            mCertificationPresenter.saveIDCard(idCard, mIDCardFrontPath, mIDCardBackPath);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveIDCard(boolean b) {

        if (!b) {
            ViewUtils.showToastError(R.string.failed_save_idcard);
            return;
        }
        Intent intent = new Intent(this, WalletActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
//        Intent intent = new Intent(this, WalletActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
    }

    @Override
    public void onLoadImage(String imgURL) {

    }

    @Override
    public void onLoadImageBack(String imgURL) {

    }
}
