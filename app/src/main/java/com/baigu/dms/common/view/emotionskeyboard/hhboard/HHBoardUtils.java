package com.baigu.dms.common.view.emotionskeyboard.hhboard;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.emotionskeyboard.adpater.EmoticonsAdapter;
import com.baigu.dms.common.view.emotionskeyboard.adpater.PageSetAdapter;
import com.baigu.dms.common.view.emotionskeyboard.data.EmoticonEntity;
import com.baigu.dms.common.view.emotionskeyboard.data.EmoticonPageEntity;
import com.baigu.dms.common.view.emotionskeyboard.data.EmoticonPageSetEntity;
import com.baigu.dms.common.view.emotionskeyboard.emoji.EmojiBean;
import com.baigu.dms.common.view.emotionskeyboard.interfaces.EmoticonClickListener;
import com.baigu.dms.common.view.emotionskeyboard.interfaces.EmoticonDisplayListener;
import com.baigu.dms.common.view.emotionskeyboard.interfaces.PageViewInstantiateListener;
import com.baigu.dms.common.view.emotionskeyboard.utils.EmoticonsKeyboardUtils;
import com.baigu.dms.common.view.emotionskeyboard.utils.imageloader.ImageBase;
import com.baigu.dms.common.view.emotionskeyboard.utils.imageloader.ImageLoader;
import com.baigu.dms.common.view.emotionskeyboard.widget.EmoticonPageView;
import com.baigu.dms.common.view.emotionskeyboard.widget.EmoticonsEditText;
import com.baigu.dms.common.view.textstyleplus.StyleBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HHBoardUtils {

    public static int EMOTICON_CLICK_TEXT = 1;
    public static int EMOTICON_CLICK_BIGIMAGE = 2;

    public static void initEmoticonsEditText(EmoticonsEditText etContent) {
        etContent.addEmoticonFilter(new HHEmojiFilter());
    }

    public static EmoticonClickListener getCommonEmoticonClickListener(final EditText editText) {
        return new EmoticonClickListener() {
            @Override
            public void onEmoticonClick(Object o, int actionType, boolean isDelBtn) {
                if (isDelBtn) {
                    delClick(editText);
                } else {
                    if (o == null) {
                        return;
                    }
                    if (actionType == EMOTICON_CLICK_TEXT) {
                        String content = null;
                        if (o instanceof EmojiBean) {
                            content = ((EmojiBean) o).emoji;
                        } else if (o instanceof EmoticonEntity) {
                            content = ((EmoticonEntity) o).getContent();
                        }

                        if (TextUtils.isEmpty(content)) {
                            return;
                        }
                        int index = editText.getSelectionStart();
                        Editable editable = editText.getText();
                        editable.insert(index, content);
                    }
                }
            }
        };
    }

    public static PageSetAdapter sCommonPageSetAdapter;

    public static PageSetAdapter getCommonAdapter(Context context, EmoticonClickListener emoticonClickListener) {

        if(sCommonPageSetAdapter != null){
            return sCommonPageSetAdapter;
        }

        PageSetAdapter pageSetAdapter = new PageSetAdapter();

        addDefaultPageSetEntity(pageSetAdapter, context, emoticonClickListener);

        return pageSetAdapter;
    }

    public static void addDefaultPageSetEntity(PageSetAdapter pageSetAdapter, Context context, final EmoticonClickListener emoticonClickListener) {
        EmoticonPageSetEntity defaultPageSetEntity
                = new EmoticonPageSetEntity.Builder()
                .setLine(3)
                .setRow(7)
                .setEmoticonList(parseEmotionData(HHEmoticons.sEmoticonHashMap))
                .setIPageViewInstantiateItem(new PageViewInstantiateListener<EmoticonPageEntity>() {
                    @Override
                    public View instantiateItem(ViewGroup container, int position, EmoticonPageEntity pageEntity) {
                        if (pageEntity.getRootView() == null) {
                            EmoticonPageView pageView = new EmoticonPageView(container.getContext());
                            pageView.setNumColumns(pageEntity.getRow());
                            pageEntity.setRootView(pageView);
                            try {
                                EmoticonsAdapter adapter = new EmoticonsAdapter(container.getContext(), pageEntity, emoticonClickListener);
                                adapter.setItemHeightMaxRatio(1.8);
                                adapter.setOnDisPlayListener(getEmoticonDisplayListener(emoticonClickListener));
                                pageView.getEmoticonsGridView().setAdapter(adapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        return pageEntity.getRootView();
                    }
                })
                .setShowDelBtn(EmoticonPageEntity.DelBtnStatus.LAST)
                .setIconUri(ImageBase.Scheme.DRAWABLE.toUri("im_default_emotion"))
                .build();
        pageSetAdapter.add(defaultPageSetEntity);
    }

    public static EmoticonDisplayListener<Object> getEmoticonDisplayListener(final EmoticonClickListener emoticonClickListener){
        return new EmoticonDisplayListener<Object>() {
            @Override
            public void onBindView(int position, ViewGroup parent, EmoticonsAdapter.ViewHolder viewHolder, Object object, final boolean isDelBtn) {
                final EmoticonEntity emoticonEntity = (EmoticonEntity) object;
                if (emoticonEntity == null && !isDelBtn) {
                    return;
                }
                viewHolder.ly_root.setBackgroundResource(R.drawable.im_chat_emoticon);

                if (isDelBtn) {
                    viewHolder.iv_emoticon.setImageResource(R.mipmap.face_del);
                } else {
                    try {
                        ImageLoader.getInstance(viewHolder.iv_emoticon.getContext()).displayImage(emoticonEntity.getIconUri(), viewHolder.iv_emoticon);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (emoticonClickListener != null) {
                            emoticonClickListener.onEmoticonClick(emoticonEntity, EMOTICON_CLICK_TEXT, isDelBtn);
                        }
                    }
                });
            }
        };
    }

    public static void spannableEmoticonFilter(TextView tvContent, String content) {
        if (TextUtils.isEmpty(content)) return;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
        Spannable spannable = HHEmojiFilter.spannableFilter(tvContent.getContext(),
                spannableStringBuilder,
                content,
                EmoticonsKeyboardUtils.getFontHeight(tvContent),
                null);
        tvContent.setText(spannable);
    }


    public static void spannableEmoticonFilterWithUrl(TextView tvContent, String content) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        Spannable spannable = HHEmojiFilter.spannableFilterWithUrl(tvContent.getContext(),
                spannableStringBuilder,
                content,
                EmoticonsKeyboardUtils.getFontHeight(tvContent));
        tvContent.setText(spannable);
    }

    public static void styleBuilderEmoticonFilterWithUrl(TextView tvContent, String content) {
        StyleBuilder styleBuilder = HHEmojiFilter.styleBuilderFilterWithUrl(tvContent.getContext(), content, EmoticonsKeyboardUtils.getFontHeight(tvContent));
        styleBuilder.show(tvContent);
    }

    public static void delClick(EditText editText) {
        int action = KeyEvent.ACTION_DOWN;
        int code = KeyEvent.KEYCODE_DEL;
        KeyEvent event = new KeyEvent(action, code);
        editText.onKeyDown(KeyEvent.KEYCODE_DEL, event);
    }

    public static ArrayList<EmoticonEntity> parseEmotionData(HashMap<String, Integer> data) {
        Iterator iter = data.entrySet().iterator();
        if(!iter.hasNext()){
            return null;
        }
        ArrayList<EmoticonEntity> emojis = new ArrayList<>();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            EmoticonEntity entity = new EmoticonEntity();
            entity.setContent((String) key);
            entity.setIconUri("" + val);
            emojis.add(entity);
        }
        return emojis;
    }
}
