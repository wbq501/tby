package com.baigu.dms.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.imagepicker.ImagePickerActivity;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.presenter.CertificationPresenter;
import com.baigu.dms.presenter.impl.CertificationPresenterImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 实名认证
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class CertificationStep1Activity extends BaseActivity implements View.OnClickListener, CertificationPresenter.CertificationView {
    public static final int REQUEST_CODE_IDCARD_FRONT = 20011;
    public static final int REQUEST_CODE_IDCARD_BACK = 20012;
    public static final int REQUEST_CODE_IDCARD_DELETE = 20013;


    private LinearLayout mLLIDCardFront;
    private ImageView mIvIDCardFront;
    private LinearLayout mLLIDCardBack;
    private ImageView mIvIDCardBack;

    private String mIDCardFrontPath;
    private String mIDCardBackPath;

    private CertificationPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification_step1);
        initToolBar();
        setTitle(R.string.certification);
        initView();
    }

    private void initView() {
        TextView tvName = findView(R.id.tv_realname);
        User user = UserCache.getInstance().getUser();
        tvName.setText(getString(R.string.upload_your_idcard_tip, user.getNick() == null ? "用户" : user.getNick()));

        presenter = new CertificationPresenterImpl(this, this);

        mLLIDCardFront = findView(R.id.ll_idcard_front);
        mLLIDCardBack = findView(R.id.ll_idcard_back);

        mIvIDCardFront = findView(R.id.iv_idcard_front);
        mIvIDCardBack = findView(R.id.iv_idcard_back);

        mLLIDCardFront.setOnClickListener(this);
        mLLIDCardBack.setOnClickListener(this);
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
            if (TextUtils.isEmpty(mIDCardFrontPath)) {
                ViewUtils.showToastError(R.string.input_tip_upload_idcard_front);
                return super.onOptionsItemSelected(item);
            }
            if (TextUtils.isEmpty(mIDCardBackPath)) {
                ViewUtils.showToastError(R.string.input_tip_upload_idcard_back);
                return super.onOptionsItemSelected(item);
            }
            Intent intent = new Intent(this, CertificationStep2Activity.class);
            intent.putExtra("frontPath", mIDCardFrontPath);
            intent.putExtra("backPath", mIDCardBackPath);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_IDCARD_FRONT:
                    if (data != null) {
                        List<String> pathList = data.getStringArrayListExtra(ImagePickerActivity.FLAG_SELECTED_PATH_LIST);
                        if (pathList != null && pathList.size() > 0) {
                            String path = pathList.get(0);
                            presenter.upLoadImage(path, REQUEST_CODE_IDCARD_FRONT);
                            if (!TextUtils.isEmpty(path)) {
                                Drawable drawable = Drawable.createFromPath(path);
                                mIvIDCardFront.setImageDrawable(drawable);
                            }
                        }
                    }
                    break;
                case REQUEST_CODE_IDCARD_BACK:
                    if (data != null) {
                        List<String> pathList = data.getStringArrayListExtra(ImagePickerActivity.FLAG_SELECTED_PATH_LIST);
                        if (pathList != null && pathList.size() > 0) {
                            String path = pathList.get(0);
                            presenter.upLoadImage(path, REQUEST_CODE_IDCARD_BACK);
                            if (!TextUtils.isEmpty(path)) {

                                Drawable drawable = Drawable.createFromPath(path);
                                mIvIDCardBack.setImageDrawable(drawable);
                            }
                        }
                    }
                    break;
                case REQUEST_CODE_IDCARD_DELETE:
                    if (data != null) {
                        String path = data.getStringExtra("deleteUrl");
                        boolean front = data.getBooleanExtra("isFront", false);
                        if (!TextUtils.isEmpty(path)) {
                            if (front && path.equals(mIDCardFrontPath)) {
                                mIDCardFrontPath = null;
                                mIvIDCardFront.setImageDrawable(null);
                            } else if (path.equals(mIDCardBackPath)) {
                                mIDCardBackPath = null;
                                mIvIDCardBack.setImageDrawable(null);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastClick()) return;
        switch (v.getId()) {
            case R.id.ll_idcard_front:
                if (!TextUtils.isEmpty(mIDCardFrontPath)) {
                    ArrayList<String> list = new ArrayList<>(1);
                    list.add(mIDCardFrontPath);
                    Intent intent = new Intent(this, ImageDeletePreviewActivity.class);
                    intent.putExtra("isFront", true);
                    intent.putExtra("urlList", list);
                    startActivityForResult(intent, REQUEST_CODE_IDCARD_DELETE);
                } else {
                    showImagePicker(REQUEST_CODE_IDCARD_FRONT);
                }
                break;
            case R.id.ll_idcard_back:
                if (!TextUtils.isEmpty(mIDCardBackPath)) {
                    ArrayList<String> list = new ArrayList<>(1);
                    list.add(mIDCardBackPath);
                    Intent intent = new Intent(this, ImageDeletePreviewActivity.class);
                    intent.putExtra("front", false);
                    intent.putExtra("urlList", list);
                    startActivityForResult(intent, REQUEST_CODE_IDCARD_DELETE);
                } else {
                    showImagePicker(REQUEST_CODE_IDCARD_BACK);
                }
                break;
            default:
                break;
        }
    }

    private void showImagePicker(int requestCode) {
        Intent intent = new Intent(CertificationStep1Activity.this, ImagePickerActivity.class);
        ImagePickerActivity.ImagePickerOptions options = new ImagePickerActivity.ImagePickerOptions();
        options.setEnableMultiSelect(false);
        options.setButtonText(getString(R.string.ok));
        options.setShowCamera(true);
        intent.putExtra(ImagePickerActivity.FLAG_IMAGE_PICKER_OPTIONS, options);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onSaveIDCard(boolean b) {

    }

    @Override
    public void onLoadImage(String imgURL) {
        if(TextUtils.isEmpty(imgURL)){
            ViewUtils.showToastError("图片上传失败");
            return;
        }
        mIDCardFrontPath = imgURL;
    }

    @Override
    public void onLoadImageBack(String imgURL) {
        if(TextUtils.isEmpty(imgURL)){
            ViewUtils.showToastError("图片上传失败");
            return;
        }
        mIDCardBackPath = imgURL;

    }
}
