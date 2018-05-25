package com.baigu.dms.common.view.emotionskeyboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.baigu.dms.R;
import com.baigu.dms.common.view.emotionskeyboard.adpater.PageSetAdapter;
import com.baigu.dms.common.view.emotionskeyboard.data.PageSetEntity;
import com.baigu.dms.common.view.emotionskeyboard.utils.EmoticonsKeyboardUtils;
import com.baigu.dms.common.view.emotionskeyboard.widget.AutoHeightLayout;
import com.baigu.dms.common.view.emotionskeyboard.widget.EmoticonsEditText;
import com.baigu.dms.common.view.emotionskeyboard.widget.EmoticonsFuncView;
import com.baigu.dms.common.view.emotionskeyboard.widget.EmoticonsIndicatorView;
import com.baigu.dms.common.view.emotionskeyboard.widget.EmoticonsToolBarView;
import com.baigu.dms.common.view.emotionskeyboard.widget.FuncLayout;

import java.util.ArrayList;

public class XhsEmoticonsKeyBoard extends AutoHeightLayout implements View.OnClickListener, EmoticonsFuncView.OnEmoticonsPageViewListener,
        EmoticonsToolBarView.OnToolBarItemClickListener, EmoticonsEditText.OnBackKeyClickListener, FuncLayout.OnFuncChangeListener {

    public static final int FUNC_TYPE_EMOTION = -1;
    public static final int FUNC_TYPE_APPPS = -2;

    protected LayoutInflater mInflater;

    protected ImageView mBtnVoiceOrText;
    protected Button mBtnVoice;
    protected EmoticonsEditText mEtChat;
    protected ImageView mBtnFace;
    protected RelativeLayout mRlInput;
    protected ImageView mBtnMultimedia;
    protected Button mBtnSend;
    protected FuncLayout mLyKvml;

    protected EmoticonsFuncView mEmoticonsFuncView;
    protected EmoticonsIndicatorView mEmoticonsIndicatorView;
    protected EmoticonsToolBarView mEmoticonsToolBarView;

    protected boolean mDispatchKeyEventPreImeLock = false;

    public XhsEmoticonsKeyBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflateKeyboardBar();
        initView();
        initFuncView();
    }

    protected void inflateKeyboardBar() {
        mInflater.inflate(R.layout.view_keyboard_xhs, this);
    }

    protected View inflateFunc() {
        return mInflater.inflate(R.layout.view_func_emoticon, null);
    }

    protected void initView() {
        mBtnVoiceOrText = ((ImageView) findViewById(R.id.btn_voice_or_text));
        mBtnVoice = ((Button) findViewById(R.id.btn_voice));
        mEtChat = ((EmoticonsEditText) findViewById(R.id.et_chat));
        mBtnFace = ((ImageView) findViewById(R.id.btn_face));
        mRlInput = ((RelativeLayout) findViewById(R.id.rl_input));
        mBtnMultimedia = ((ImageView) findViewById(R.id.btn_multimedia));
        mBtnSend = ((Button) findViewById(R.id.btn_send));
        mLyKvml = ((FuncLayout) findViewById(R.id.ly_kvml));

        mBtnVoiceOrText.setOnClickListener(this);
        mBtnFace.setOnClickListener(this);
        mBtnMultimedia.setOnClickListener(this);
        mEtChat.setOnBackKeyClickListener(this);

        mEtChat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mRlInput.setBackgroundResource(R.drawable.im_chat_input_bg_pressed);
                } else {
                    mRlInput.setBackgroundResource(R.drawable.im_chat_input_bg_normal);
                }
            }
        });
    }

    protected void initFuncView() {
        initEmoticonFuncView();
        initEditView();
    }

    protected void initEmoticonFuncView() {
        View keyboardView = inflateFunc();
        mLyKvml.addFuncView(FUNC_TYPE_EMOTION, keyboardView);
        mEmoticonsFuncView = ((EmoticonsFuncView) findViewById(R.id.view_epv));
        mEmoticonsIndicatorView = ((EmoticonsIndicatorView) findViewById(R.id.view_eiv));
        mEmoticonsToolBarView = ((EmoticonsToolBarView) findViewById(R.id.view_etv));
        mEmoticonsFuncView.setOnIndicatorListener(this);
        mEmoticonsToolBarView.setOnToolBarItemClickListener(this);
        mLyKvml.setOnFuncChangeListener(this);
    }

    protected void initEditView() {
        mEtChat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!mEtChat.isFocused()) {
                    mEtChat.setFocusable(true);
                    mEtChat.setFocusableInTouchMode(true);
                }
                return false;
            }
        });

        mEtChat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int index, int before, int count) {
                if (count == 1 && !TextUtils.isEmpty(charSequence)) {
                    char mentionChar = charSequence.toString().charAt(index);
                    if ('@' == mentionChar && mOnAiteListener != null) {
                        mOnAiteListener.onAite();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    mBtnSend.setVisibility(VISIBLE);
                    mBtnMultimedia.setVisibility(GONE);
                    mBtnSend.setBackgroundResource(R.drawable.im_chat_send_bg);
                } else {
                    mBtnMultimedia.setVisibility(VISIBLE);
                    mBtnSend.setVisibility(GONE);
                }
            }
        });
    }
    public interface OnAiteListener{
        void onAite();
    }
    private OnAiteListener mOnAiteListener;
    public void setOnAiteListener(OnAiteListener onAiteListener){
        mOnAiteListener = onAiteListener;
    }
    public void setAdapter(PageSetAdapter pageSetAdapter) {
        if (pageSetAdapter != null) {
            ArrayList<PageSetEntity> pageSetEntities = pageSetAdapter.getPageSetEntityList();
            if (pageSetEntities != null) {
                for (PageSetEntity pageSetEntity : pageSetEntities) {
                    mEmoticonsToolBarView.addToolItemView(pageSetEntity);
                }
            }
        }
        mEmoticonsFuncView.setAdapter(pageSetAdapter);
    }

    public void addFuncView(View view) {
        mLyKvml.addFuncView(FUNC_TYPE_APPPS, view);
    }

    public void reset() {
        EmoticonsKeyboardUtils.closeSoftKeyboard(this);
        mLyKvml.hideAllFuncView();
        mBtnFace.setImageResource(R.mipmap.face);
    }

    protected void showVoice() {
        mRlInput.setVisibility(GONE);
        mBtnVoice.setVisibility(VISIBLE);
        reset();
    }

    protected void checkVoice() {
        if (mBtnVoice.isShown()) {
            mBtnVoiceOrText.setImageResource(R.drawable.im_chat_keyboard);
        } else {
            mBtnVoiceOrText.setImageResource(R.drawable.im_chat_voice);
        }
    }

    protected void showText() {
        mRlInput.setVisibility(VISIBLE);
        mBtnVoice.setVisibility(GONE);
    }

    protected void toggleFuncView(int key) {
        showText();
        mLyKvml.toggleFuncView(key, isSoftKeyboardPop(), mEtChat);
    }

    @Override
    public void onFuncChange(int key) {
        if (FUNC_TYPE_EMOTION == key) {
            mBtnFace.setImageResource(R.mipmap.face_sel);
        } else {
            mBtnFace.setImageResource(R.mipmap.face);
        }
        if (mLyKvml.DEF_KEY == key) {
            mLyKvml.setCurrenFuncKey(key);
        }
        checkVoice();
    }

    protected void setFuncViewHeight(int height) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLyKvml.getLayoutParams();
        params.height = height;
        mLyKvml.setLayoutParams(params);
    }

    @Override
    public void onSoftKeyboardHeightChanged(int height) {
        mLyKvml.updateHeight(height);
    }

    @Override
    public void OnSoftPop(int height) {
        super.OnSoftPop(height);
        mLyKvml.setVisibility(true);
        onFuncChange(mLyKvml.DEF_KEY);
    }

    @Override
    public void OnSoftClose() {
        super.OnSoftClose();
        if (mLyKvml.isOnlyShowSoftKeyboard()) {
            reset();
        } else {
            onFuncChange(mLyKvml.getCurrentFuncKey());
        }
    }

    public void addOnFuncKeyBoardListener(FuncLayout.OnFuncKeyBoardListener l) {
        mLyKvml.addOnKeyBoardListener(l);
    }

    @Override
    public void emoticonSetChanged(PageSetEntity pageSetEntity) {
        mEmoticonsToolBarView.setToolBtnSelect(pageSetEntity.getUuid());
    }

    @Override
    public void playTo(int position, PageSetEntity pageSetEntity) {
        mEmoticonsIndicatorView.playTo(position, pageSetEntity);
    }

    @Override
    public void playBy(int oldPosition, int newPosition, PageSetEntity pageSetEntity) {
        mEmoticonsIndicatorView.playBy(oldPosition, newPosition, pageSetEntity);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_voice_or_text) {

        } else if (i == R.id.btn_face) {
            toggleFuncView(FUNC_TYPE_EMOTION);
//            mEtChat.requestFocus();
//            mEtChat.requestFocusFromTouch();
        } else if (i == R.id.btn_multimedia) {
            toggleFuncView(FUNC_TYPE_APPPS);
        }
    }

    @Override
    public void onToolBarItemClick(PageSetEntity pageSetEntity) {
        mEmoticonsFuncView.setCurrentPageSet(pageSetEntity);
    }

    @Override
    public void onBackKeyClick() {
        if (mLyKvml.isShown()) {
            mDispatchKeyEventPreImeLock = true;
            reset();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (mDispatchKeyEventPreImeLock) {
                    mDispatchKeyEventPreImeLock = false;
                    return true;
                }
                if (mLyKvml.isShown()) {
                    reset();
                    return true;
                } else {
                    return super.dispatchKeyEvent(event);
                }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (EmoticonsKeyboardUtils.isFullScreen((Activity) getContext())) {
            return false;
        }
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        if (EmoticonsKeyboardUtils.isFullScreen((Activity) getContext())) {
            return;
        }
        super.requestChildFocus(child, focused);
    }

    public boolean dispatchKeyEventInFullScreen(KeyEvent event) {
        if(event == null){
            return false;
        }
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (EmoticonsKeyboardUtils.isFullScreen((Activity) getContext()) && mLyKvml.isShown()) {
                    reset();
                    return true;
                }
            default:
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    boolean isFocused;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        isFocused = mEtChat.getShowSoftInputOnFocus();
                    } else {
                        isFocused = mEtChat.isFocused();
                    }
                    if(isFocused){
                        mEtChat.onKeyDown(event.getKeyCode(), event);
                    }
                }
                return false;
        }
    }

    public EmoticonsEditText getEtChat() { return mEtChat; }

    public Button getBtnVoice() { return mBtnVoice; }

    public Button getBtnSend() {
        return mBtnSend;
    }

    public EmoticonsFuncView getEmoticonsFuncView() {
        return mEmoticonsFuncView;
    }

    public EmoticonsIndicatorView getEmoticonsIndicatorView() {
        return mEmoticonsIndicatorView;
    }

    public EmoticonsToolBarView getEmoticonsToolBarView() {
        return mEmoticonsToolBarView;
    }

    public ImageView getBtnFace() {
        return mBtnFace;
    }
}
