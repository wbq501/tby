package com.baigu.dms.common.view.emotionskeyboard.hhboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;


import com.baigu.dms.R;
import com.baigu.dms.adapter.ChatBottomFuncAdapter;

import java.util.ArrayList;

public class HHFuncView extends RelativeLayout {
    public static final int ALBUM_FUNC_VIEW = 0;
    public static final int CAMERA_FUNC_VIEW = ALBUM_FUNC_VIEW + 1;
    public static final int FILE_FUNC_VIEW = CAMERA_FUNC_VIEW + 1;
    public static final int RED_PACKET_FUNC_VIEW = FILE_FUNC_VIEW + 1;
    public static final int VOICE_FUNC_VIEW = RED_PACKET_FUNC_VIEW + 1;
    public static final int VIDEO_FUNC_VIEW = VOICE_FUNC_VIEW + 1;
    public static final int LEAVE_MESSAGE_VIEW = VIDEO_FUNC_VIEW + 1;

    protected View view;
    private ChatBottomFuncAdapter mChatBottomFuncAdapter;

    public interface FuncViewClickListener {
        void onFuncViewClick(int funcView);
    }

    public HHFuncView(Context context) {
        this(context, null, false);
    }


    public HHFuncView(Context context, AttributeSet attrs, boolean isBurn) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.view_chat_bottom_func, this);
        init();
    }

    protected void init(){
        GridView gv_apps = (GridView) view.findViewById(R.id.gv_funcs);
        ArrayList<FuncItem> mAppBeanList = new ArrayList<FuncItem>();
        mAppBeanList.add(new FuncItem(ALBUM_FUNC_VIEW, R.mipmap.image, "图片"));
        mAppBeanList.add(new FuncItem(CAMERA_FUNC_VIEW, R.mipmap.camera, "拍照"));
        mAppBeanList.add(new FuncItem(LEAVE_MESSAGE_VIEW, R.mipmap.chat_func_place_holder, ""));
        mAppBeanList.add(new FuncItem(VIDEO_FUNC_VIEW, R.mipmap.chat_func_place_holder, ""));
        mAppBeanList.add(new FuncItem(VOICE_FUNC_VIEW, R.mipmap.chat_func_place_holder, ""));
        mAppBeanList.add(new FuncItem(VIDEO_FUNC_VIEW, R.mipmap.chat_func_place_holder, ""));
//        mAppBeanList.add(new FuncItem(LOCATION_FUNC_VIEW, R.mipmap.temp_icon_loaction, "位置"));
        mChatBottomFuncAdapter = new ChatBottomFuncAdapter(getContext(), mAppBeanList);
        gv_apps.setAdapter(mChatBottomFuncAdapter);
    }

    public void setFuncViewClickListener(FuncViewClickListener funcViewClickListener) {
        mChatBottomFuncAdapter.setFuncViewClickListener(funcViewClickListener);
    }

    public static class FuncItem {
        private int id;
        private int icon;
        private String funcName;

        public FuncItem(int id, int icon, String funcName){
            this.id = id;
            this.icon = icon;
            this.funcName = funcName;
        }

        public int getIcon() {
            return icon;
        }

        public String getFuncName() {
            return funcName;
        }

        public int getId() {
            return id;
        }
    }
}