package com.baigu.dms.common.view.imagepicker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.Constants;
import com.baigu.dms.common.utils.FileUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.imagepicker.adapter.ImageFolderAdapter;
import com.baigu.dms.common.view.imagepicker.model.SDFolder;
import com.baigu.dms.domain.file.FileRepository;

import java.util.List;


public class ImageFolderChooserActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private static final int SELECT_FILE_REQUEST_CODE = 1001;

    private ListView mLvFolder;
    private ProgressBar mProgressBar;
    private ImageFolderAdapter mFolderAdapter;
    private int mOperationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_folder_chooser);
        initToolBar();

        mOperationType = getIntent().getIntExtra("type", OperationType.TYPE_UNKNOWN);
        int titleResId = mOperationType == OperationType.FileOperationType.TYPE_IMAGE ? R.string.select_img_folder : R.string.select_video;
        setTitle(titleResId);

        initView();

        loadData();
    }

    private void initView() {

        mLvFolder = findView(R.id.lv_folder);
        mLvFolder.setOnItemClickListener(this);

        mLvFolder.setVisibility(View.GONE);
        View view = new View(this);
        view.setBackgroundColor(getResources().getColor(R.color.line));
        ListView.LayoutParams params = new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewUtils.dip2px(0.5F));
        view.setLayoutParams(params);
        mLvFolder.addFooterView(view);

        mFolderAdapter = new ImageFolderAdapter(this);
        mLvFolder.setAdapter(mFolderAdapter);

        mProgressBar = findView(R.id.progress_bar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_FILE_REQUEST_CODE:
                    String path = data.getStringExtra("path");
                    Intent intent = getIntent();
                    intent.putExtra("path", path);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SDFolder imageFolder = mFolderAdapter.getItem(position);
        Intent intent = new Intent(this, ImageChooserActivity.class);
        intent.putExtra("folder", imageFolder);
        startActivityForResult(intent, SELECT_FILE_REQUEST_CODE);
    }

    private void loadData() {
        if (!FileUtils.isExistExternalStore()) return;
        if (mOperationType == OperationType.TYPE_UNKNOWN) return;
        mProgressBar.setVisibility(View.VISIBLE);
        Constants.sExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                List<SDFolder> folderList = null;
                switch (mOperationType) {
                    case OperationType.FileOperationType.TYPE_IMAGE:
                        folderList = FileRepository.getAllImageFolder();
                        break;
                    case OperationType.FileOperationType.TYPE_VIDEO:
                        folderList = FileRepository.getAllVideoFolder();
                        break;
                    default:
                        break;
                }
                refreshData(folderList);
            }
        });
    }

    private void refreshData(final List<SDFolder> folderList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.GONE);
                mFolderAdapter.setData(folderList);
                mFolderAdapter.notifyDataSetChanged();

                int visibility = mFolderAdapter.getCount() > 0 ? View.VISIBLE : View.GONE;
                mLvFolder.setVisibility(visibility);
            }
        });
    }
}
