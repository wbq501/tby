package com.baigu.dms.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;

import com.baigu.dms.R;
import com.baigu.dms.adapter.MessageAdapter;
import com.baigu.dms.common.utils.Constants;
import com.baigu.dms.common.utils.IMHelper;
import com.baigu.dms.common.utils.RSAEncryptor;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.utils.rxbus.RxBus;
import com.baigu.dms.common.utils.rxbus.Subscribe;
import com.baigu.dms.common.utils.rxbus.ThreadMode;
import com.baigu.dms.common.view.ChatListView;
import com.baigu.dms.common.view.LoadingDialog;
import com.baigu.dms.common.view.emotionskeyboard.XhsEmoticonsKeyBoard;
import com.baigu.dms.common.view.emotionskeyboard.data.EmoticonEntity;
import com.baigu.dms.common.view.emotionskeyboard.emoji.EmojiBean;
import com.baigu.dms.common.view.emotionskeyboard.hhboard.HHBoardUtils;
import com.baigu.dms.common.view.emotionskeyboard.hhboard.HHFuncView;
import com.baigu.dms.common.view.emotionskeyboard.interfaces.EmoticonClickListener;
import com.baigu.dms.common.view.emotionskeyboard.utils.EmoticonsKeyboardUtils;
import com.baigu.dms.common.view.emotionskeyboard.widget.EmoticonsEditText;
import com.baigu.dms.common.view.emotionskeyboard.widget.FuncLayout;
import com.baigu.dms.common.view.imagepicker.ImagePickerActivity;
import com.baigu.dms.common.view.imagepicker.TakePictureUtils;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.common.utils.rxbus.EventType;
import com.baigu.dms.common.utils.rxbus.RxBusEvent;
import com.baigu.dms.presenter.ChatPresenter;
import com.baigu.dms.presenter.impl.ChatPresenterImpl;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.chat.Message;
import com.hyphenate.helpdesk.callback.Callback;
import com.micky.logger.Logger;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/13 22:19
 */
public class ChatActivity extends BaseActivity implements ChatPresenter.ChatView, FuncLayout.OnFuncKeyBoardListener,
        ChatListView.ChatListViewListener, HHFuncView.FuncViewClickListener {

    private XhsEmoticonsKeyBoard mEkBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ChatListView mLvMessage;
    private LoadingDialog mLoaddingDialog;
    private String mTo;
    private ChatPresenter mChatPresenter;

    private TakePictureUtils mTakePictureUtils;

    private MessageAdapter mMessageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initToolBar();

        String title = getString(R.string.app_name) + getString(R.string.official_custmer);
        setTitle(title);

        RxBus.getDefault().register(this);

        initView();
        UserCache.getInstance().setChatting(true);

        if (mTakePictureUtils == null) {
            mTakePictureUtils = new TakePictureUtils(this);
        }
        mChatPresenter = new ChatPresenterImpl(this, this);
        mTo = getString(R.string.hx_customer);
        initData();
        mChatPresenter.loadMessageList(mTo, null);
    }

    private void initView() {
        mEkBar = findView(R.id.ek_bar);
        initEmoticonsKeyBoardBar();
        initPullRefresh();

        mLvMessage = findView(R.id.lv_message);
        mLvMessage.setChatListViewListener(this);
        mLvMessage.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_FLING:
                        break;
                    case SCROLL_STATE_IDLE:
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        mEkBar.reset();
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
            }
        });
        mMessageAdapter = new MessageAdapter(this);
        mLvMessage.setAdapter(mMessageAdapter);
    }

    private void initPullRefresh() {
        mSwipeRefreshLayout = findView(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String startMsgId = mMessageAdapter == null || mMessageAdapter.getCount() <= 0 ? null : mMessageAdapter.getItem(0).getMsgId();
                mChatPresenter.loadMessageList(mTo, startMsgId);
            }
        });
    }


    private void initEmoticonsKeyBoardBar() {
        HHBoardUtils.initEmoticonsEditText(mEkBar.getEtChat());
        mEkBar.setAdapter(HHBoardUtils.getCommonAdapter(this, emoticonClickListener));
        mEkBar.addOnFuncKeyBoardListener(this);

        HHFuncView hhFuncView = new HHFuncView(this);
        hhFuncView.setFuncViewClickListener(this);
        mEkBar.addFuncView(hhFuncView);

        mEkBar.getEtChat().setOnSizeChangedListener(new EmoticonsEditText.OnSizeChangedListener() {
            @Override
            public void onSizeChanged(int w, int h, int oldw, int oldh) {
                scrollToBottom();
            }
        });

        mEkBar.getBtnSend().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("test", mEkBar.getEtChat().getText().toString());
                onSendText(mEkBar.getEtChat().getText().toString());
                mEkBar.getEtChat().setText("");
            }
        });

        mEkBar.getBtnVoice().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    EmoticonClickListener emoticonClickListener = new EmoticonClickListener() {
        @Override
        public void onEmoticonClick(Object o, int actionType, boolean isDelBtn) {

            if (isDelBtn) {
                HHBoardUtils.delClick(mEkBar.getEtChat());
            } else {
                if (o == null) {
                    return;
                }
                if (actionType == HHBoardUtils.EMOTICON_CLICK_BIGIMAGE) {
                    if (o instanceof EmoticonEntity) {
                        onSendImage(((EmoticonEntity) o).getIconUri(), false);
                    }
                } else {
                    String content = null;
                    if (o instanceof EmojiBean) {
                        content = ((EmojiBean) o).emoji;
                    } else if (o instanceof EmoticonEntity) {
                        content = ((EmoticonEntity) o).getContent();
                    }

                    if (TextUtils.isEmpty(content)) {
                        return;
                    }
                    int index = mEkBar.getEtChat().getSelectionStart();
                    Editable editable = mEkBar.getEtChat().getText();
                    editable.insert(index, content);
                }
            }
        }
    };

    private void initData() {
        IMHelper.getInstance().addMessageListener();
        if (!ChatClient.getInstance().isLoggedInBefore()) {
            User user = UserCache.getInstance().getUser();
            if (mLoaddingDialog == null) {
                mLoaddingDialog = new LoadingDialog(this);
            }
            mLoaddingDialog.show();
//            ChatClient.getInstance().register(user.getImuser(), user.getImpwd(), new Callback() {
//                @Override
//                public void onSuccess() {
//                    Log.e("onSuccess", "onSuccess: " );
//                }
//
//                @Override
//                public void onError(int i, String s) {
//                    Log.e("onSuccess", "onSuccess: " );
//                }
//
//                @Override
//                public void onProgress(int i, String s) {
//                    Log.e("onSuccess", "onSuccess: " );
//                }
//            });
            try {
//                RSAEncryptor encryptor = new RSAEncryptor();
//                encryptor.loadPrivateKey(user.getPrivateKey());
//                String passwd = encryptor.decryptWithBase64(user.getImpwd());
                ChatClient.getInstance().login(user.getImuser(),user.getImpwd() , new Callback() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mLoaddingDialog.hide();
                                IMHelper.getInstance().addMessageListener();
                                markAllConversationsAsRead();
                            }
                        });
                    }

                    @Override
                    public void onError(int i, String s) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mLoaddingDialog.hide();

                                ViewUtils.showToastInfo(R.string.connect_customer_failed);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            } catch (Exception e) {
                mLoaddingDialog.hide();
                IMHelper.getInstance().removeMessageListener();
                ViewUtils.showToastInfo(R.string.connect_customer_failed);
                finish();
                Logger.e(e, e.getMessage());
            }
        } else {
            markAllConversationsAsRead();
        }
    }

    private void scrollToBottom() {
        mLvMessage.post(new Runnable() {
            @Override
            public void run() {
                mLvMessage.requestLayout();
                mLvMessage.setSelection(mLvMessage.getBottom());
            }
        });

    }

    private void onSendText(String text) {
        mChatPresenter.sendTextMessage(mTo, text);
    }

    private void onSendImage(String imagePath, boolean sendOriginalImage) {
        mChatPresenter.sendImageMessage(mTo, imagePath, sendOriginalImage);
    }

    @Override
    public void OnFuncPop(int height) {
        scrollToBottom();
    }

    @Override
    public void OnFuncClose() {

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (EmoticonsKeyboardUtils.isFullScreen(this)) {
            boolean isConsum = mEkBar.dispatchKeyEventInFullScreen(event);
            return isConsum ? isConsum : super.dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onChatListViewTouchEvent(MotionEvent event) {
        mEkBar.reset();
        mEkBar.getEtChat().requestFocus();
    }

    @Override
    public void onFuncViewClick(int funcView) {
        switch (funcView) {
            case HHFuncView.ALBUM_FUNC_VIEW:
                showImagePicker();
                break;
            case HHFuncView.CAMERA_FUNC_VIEW:
                mTakePictureUtils.takeFromCamera();
                break;
            case HHFuncView.LEAVE_MESSAGE_VIEW:
//                startActivity(new Intent(this, LeaveMessageActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        mTakePictureUtils.onActivityResult(requestCode, resultCode, data, new TakePictureUtils.PictureListener() {
            @Override
            public void onPictureTake(String path) {
                if (!TextUtils.isEmpty(path)) {
                    onSendImage(path, false);
                }
            }
        });
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ImagePickerActivity.REQUEST_IMAGE_PICKER:
                    if (data != null) {
                        List<String> pathList = data.getStringArrayListExtra(ImagePickerActivity
                                .FLAG_SELECTED_PATH_LIST);
                        boolean originImage = data.getBooleanExtra(ImagePickerActivity.FLAG_ORIGIN_IMAGE, false);
                        for (String path : pathList) {
                            onSendImage(path, originImage);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void showImagePicker() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        ImagePickerActivity.ImagePickerOptions options = new ImagePickerActivity.ImagePickerOptions();
        options.setEnableMultiSelect(true);
        options.setShowCamera(true);
        options.setMaxSelect(Constants.IMAGE_PICKER_MAX_SELECT);
        options.setEnableOriginImage(true);
        intent.putExtra(ImagePickerActivity.FLAG_IMAGE_PICKER_OPTIONS, options);
        startActivityForResult(intent, ImagePickerActivity.REQUEST_IMAGE_PICKER);
    }

    @Override
    public void onLoadMessageList(final List<Message> list) {
        mMessageAdapter.appendDataToFront(list);
        mMessageAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
        if (mMessageAdapter.getCount() != 0) {
            mSwipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (list.size() != 0) {
                        mLvMessage.setSelection(list.size() - 1);
                    }
                }
            }, 10);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        markAllConversationsAsRead();
    }

    @Override
    protected void onDestroy() {
        RxBus.getDefault().unregister(this);
        if (mLoaddingDialog != null) {
            mLoaddingDialog.dismiss();
        }
        UserCache.getInstance().setChatting(false);
        super.onDestroy();
    }

    private void markAllConversationsAsRead() {
        Constants.sExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                //所有未读消息数清零
                ChatClient.getInstance().chatManager().markAllConversationsAsRead();
                RxBus.getDefault().post(EventType.TYPE_UPDATE_MESSAGE_NUM);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBusEvent(RxBusEvent event) {
        switch (event.what) {
            case EventType.TYPE_NEW_MESSAGE:
                Message message = (Message) event.object;
                mMessageAdapter.appendData(message);
                mMessageAdapter.notifyDataSetChanged();
                scrollToBottom();
                break;
            case EventType.TYPE_NEW_MESSAGES:
                List<Message> list = (List<Message>) event.object;
                mMessageAdapter.appendListData(list);
                mMessageAdapter.notifyDataSetChanged();
                scrollToBottom();
                break;
            case EventType.TYPE_ADD_ORDER:
                mMessageAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

}
