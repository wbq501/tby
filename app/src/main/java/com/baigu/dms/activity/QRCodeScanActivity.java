package com.baigu.dms.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.FileUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.utils.rxpermission.PermissionRequest;
import com.baigu.dms.common.view.imagepicker.ImagePickerActivity;
import com.baigu.dms.common.view.qrcode.core.QRCodeView;
import com.baigu.dms.common.view.qrcode.zxing.ZXingView;
import com.baigu.dms.presenter.QRCodePresenter;
import com.baigu.dms.presenter.impl.QRCodePresenterImpl;

import java.util.List;

/**
 * @Description 二维码扫描
 *
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 0:51
 */
public class QRCodeScanActivity extends BaseActivity implements QRCodeView.Delegate, QRCodePresenter.QRCodeView, View.OnClickListener {

    private QRCodeView mQRCodeView;
    private QRCodePresenter mQRCodePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scan);
        initToolBar();
        setTitle(R.string.qrcode_scan);

        ViewUtils.setStatusColor(this, R.color.black);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.dim_background_1));

        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mQRCodeView.setDelegate(this);

        findViewById(R.id.btn_ablum).setOnClickListener(this);
        findViewById(R.id.btn_mycode).setOnClickListener(this);
        ((ToggleButton)findViewById(R.id.tb_light)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mQRCodeView.openFlashlight();
                } else {
                    mQRCodeView.closeFlashlight();
                }
            }
        });
        mQRCodePresenter = new QRCodePresenterImpl(this, this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
        mQRCodeView.showScanRect();
        mQRCodeView.startSpot();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        ViewUtils.setStatusColor(this, R.color.colorPrimaryDark);
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        vibrate();
//        mQRCodeView.startSpot();
        Intent intent = new Intent(this, RegisterStep2Activity.class);
        intent.putExtra("code", result);
        startActivity(intent);
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        ViewUtils.showToastInfo(R.string.camera_open_failed);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ablum:
                String appName = getString(R.string.app_name);
                String tip = getString(R.string.permission_sd_camera, appName, appName);
                PermissionRequest permissionRequest = new PermissionRequest(this, tip, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
                permissionRequest.setPermissionListener(new PermissionRequest.PermissionListener() {
                    @Override
                    public void onGrant() {
                        if (FileUtils.isExistExternalStore()) {
                            showImagePicker();
                        }
                    }
                });
                permissionRequest.requestPermission(true);
                break;
            case R.id.btn_mycode:
                startActivity(new Intent(QRCodeScanActivity.this, MyQRCodeActivity.class));
                break;
            default:
                break;
        }
    }

    private void showImagePicker() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        ImagePickerActivity.ImagePickerOptions options = new ImagePickerActivity.ImagePickerOptions();
        options.setEnableMultiSelect(false);
        options.setShowCamera(false);
        options.setButtonText(getString(R.string.ok));
        intent.putExtra(ImagePickerActivity.FLAG_IMAGE_PICKER_OPTIONS, options);
        startActivityForResult(intent, ImagePickerActivity.REQUEST_IMAGE_PICKER);
    }

    @Override
    public void onDecode(String result) {
        if (!TextUtils.isEmpty(result)) {
            Intent intent = new Intent(this, RegisterStep2Activity.class);
            intent.putExtra("code", result);
            startActivity(intent);
        } else {
            ViewUtils.showToastInfo(R.string.check_qrcode_failed);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ImagePickerActivity.REQUEST_IMAGE_PICKER:
                    if (data != null) {
                        List<String> pathList = data.getStringArrayListExtra(ImagePickerActivity.FLAG_SELECTED_PATH_LIST);
                        if (pathList != null && pathList.size() > 0) {
                            String path = pathList.get(0);
                            if (!TextUtils.isEmpty(path)) {
                                mQRCodePresenter.decode(path);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
