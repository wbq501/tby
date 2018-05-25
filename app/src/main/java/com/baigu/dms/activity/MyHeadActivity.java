package com.baigu.dms.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.utils.rxbus.EventType;
import com.baigu.dms.common.utils.rxbus.RxBus;
import com.baigu.dms.common.view.GlideCircleTransform;
import com.baigu.dms.common.view.imagepicker.ImagePickerActivity;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.netservice.URLFactory;
import com.baigu.dms.domain.netservice.common.model.MyDataResult;
import com.baigu.dms.presenter.UserPresenter;
import com.baigu.dms.presenter.impl.UserPresenterImpl;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 13:04
 */
public class MyHeadActivity extends BaseActivity implements UserPresenter.UserView {

    public static final int REQUEST_IMAGE_CROP = 1101;
    private ImageView mIvHead;
    private String mToken;
    private UserPresenter mUserPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setBackgroundDrawableResource(R.color.black);
        }
        setContentView(R.layout.activity_my_head);
        initToolBar();
        mToolbar.setBackgroundColor(getResources().getColor(R.color.black));
        setTitle(R.string.head);

        mUserPresenter = new UserPresenterImpl(this, this);
        mIvHead = findView(R.id.iv_head);

        User user = UserCache.getInstance().getUser();
        if (user == null) {
            finish();
            return;
        }
        Glide.with(this).load(user.getPhoto()).placeholder(R.mipmap.default_head).diskCacheStrategy(DiskCacheStrategy.ALL).transform(new GlideCircleTransform(this)).into(mIvHead);
        mUserPresenter.loadToken();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_head, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ViewUtils.isFastClick()) {
            return super.onOptionsItemSelected(item);
        }
        int id = item.getItemId();
        if (id == R.id.action_change_head) {
            showPopWindow(mToolbar);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ImagePickerActivity.REQUEST_IMAGE_PICKER:
                    if (data != null) {
                        List<String> pathList = data.getStringArrayListExtra(ImagePickerActivity.FLAG_SELECTED_PATH_LIST);
                        if (pathList != null && pathList.size() > 0) {
                            goCrop(Uri.parse(pathList.get(0)));
                        }
                    }
                    break;
                case REQUEST_IMAGE_CROP:
                    String path = data.getData().getPath();
                    if (!TextUtils.isEmpty(path)) {
                        mIvHead.setImageURI(Uri.parse(path));
                    }
                    mUserPresenter.saveHead(path, mToken);
                    break;
                default:
                    break;
            }
        }
    }

    public void goCrop(Uri uri) {
        Intent intent = new Intent(MyHeadActivity.this, CropImageActivity.class);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_IMAGE_CROP);
    }

    @Override
    public void onGetMyInfo(User user) {

    }

    @Override
    public void onLoadToken(String token) {
        if (TextUtils.isEmpty(token)) {
            ViewUtils.showToastError(getString(R.string.connect_failuer_toast));
        } else {
            mToken = token;
        }

    }

    @Override
    public void onSaveHead(boolean result) {
        if (!result) {
            ViewUtils.showToastError(R.string.failed_save_head);
            finish();
        } else {
            User user = UserCache.getInstance().getUser();
            if (user == null) {
                finish();
            }
            RxBus.getDefault().post(EventType.TYPE_UPDATE_HEAD_IMAGE);
            Glide.with(this).load(user.getPhoto()).placeholder(R.mipmap.default_head).diskCacheStrategy(DiskCacheStrategy.ALL).transform(new GlideCircleTransform(this)).into(mIvHead);
            finish();
        }
    }

    private void showPopWindow(View v) {
        View contenView = View.inflate(this, R.layout.view_chose_photo, null);
        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        final PopupWindow popWindow = new PopupWindow(contenView, width, height);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new ColorDrawable());
        popWindow.showAsDropDown(v, 3000, ViewUtils.dip2px(-10));
        contenView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyHeadActivity.this, ImagePickerActivity.class);
                ImagePickerActivity.ImagePickerOptions options = new ImagePickerActivity.ImagePickerOptions();
                options.setEnableMultiSelect(false);
                options.setButtonText(getString(R.string.ok));
                options.setShowCamera(true);
                intent.putExtra(ImagePickerActivity.FLAG_IMAGE_PICKER_OPTIONS, options);
                startActivityForResult(intent, ImagePickerActivity.REQUEST_IMAGE_PICKER);
                popWindow.dismiss();
            }

        });
    }
}
