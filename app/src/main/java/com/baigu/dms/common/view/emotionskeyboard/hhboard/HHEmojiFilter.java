package com.baigu.dms.common.view.emotionskeyboard.hhboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.emotionskeyboard.emoji.EmojiDisplayListener;
import com.baigu.dms.common.view.emotionskeyboard.interfaces.EmoticonFilter;
import com.baigu.dms.common.view.emotionskeyboard.utils.EmoticonsKeyboardUtils;
import com.baigu.dms.common.view.emotionskeyboard.widget.EmoticonSpan;
import com.baigu.dms.common.view.textstyleplus.StyleBuilder;
import com.baigu.dms.common.view.textstyleplus.TextStyleItem;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HHEmojiFilter extends EmoticonFilter {

    public static final int WRAP_DRAWABLE = -1;
    private int emoticonSize = -1;
    public static final Pattern HH_RANGE = Pattern.compile("\\[.*?\\]");

    public static Matcher getMatcher(CharSequence matchStr) {
        return HH_RANGE.matcher(matchStr);
    }

    @Override
    public void filter(EditText editText, CharSequence text, int start, int lengthBefore, int lengthAfter) {
        emoticonSize = emoticonSize == -1 ? EmoticonsKeyboardUtils.getFontHeight(editText) : emoticonSize;
        clearSpan(editText.getText(), start, text.toString().length());
        Matcher m = getMatcher(text.toString().substring(start, text.toString().length()));
        if (m != null) {
            while (m.find()) {
                String key = m.group();
                int icon = HHEmoticons.sEmoticonHashMap.get(key);
                if (icon > 0) {
                    emoticonDisplay(editText.getContext(), editText.getText(), icon, emoticonSize, start + m.start(), start + m.end());
                }
            }
        }
    }

    /**
     * 替换emoji字符串
     * @param context
     * @param spannable
     * @param text
     * @param fontSize
     * @param emojiDisplayListener
     * @return
     */
    public static Spannable spannableFilter(Context context, Spannable spannable, CharSequence text, int fontSize, EmojiDisplayListener emojiDisplayListener) {
        Matcher m = getMatcher(text);
        if (m != null) {
            while (m.find()) {
                String key = m.group();
                int icon = 0;
                if (HHEmoticons.sEmoticonHashMap.containsKey(key)) {
                    icon = HHEmoticons.sEmoticonHashMap.get(key);
                }
                if (emojiDisplayListener == null) {
                    if (icon > 0) {
                        emoticonDisplay(context, spannable, icon, fontSize, m.start(), m.end());
                    }
                } else {
                    emojiDisplayListener.onEmojiDisplay(context, spannable, "" + icon, fontSize, m.start(), m.end());
                }
            }
        }
        return spannable;
    }

    /**
     * 用emoji做分割，遍历添加字符串
     * @param context
     * @param spannable
     * @param text
     * @param fontSize
     * @return
     */
    public static Spannable spannableFilterWithUrl(Context context, SpannableStringBuilder spannable, String text, int fontSize) {
        Matcher m = getMatcher(text);
        String tempStr = text;
        int start = 0;
        int end = 0;
        if (m != null) {
            while (m.find()) {
                String key = m.group();
                int icon = 0;
                if (HHEmoticons.sEmoticonHashMap.containsKey(key)) {
                    icon = HHEmoticons.sEmoticonHashMap.get(key);
                }
                if (icon > 0) {
                    start = m.start();
                    end = m.end();
                    String buffer = tempStr.substring(0, start);// 两个emoji之间的字符串
                    if (!TextUtils.isEmpty(buffer)) {
                        spannable.append(parseMyUrlString(context, buffer));
                    }

                    addEmotionDisplay(context, spannable, icon, fontSize, key);
                    tempStr = tempStr.substring(end, tempStr.length()); //截取字符串
                    m.reset(tempStr);// 更新查找的字符串
                }
            }
            if (!TextUtils.isEmpty(tempStr)) {
                spannable.append(parseMyUrlString(context, tempStr)); //追加字符串
            }
        }
        return spannable;
    }

    private static SpannableString parseMyUrlString(Context context, String inputStr) { // 方法名
        Pattern mPattern1 = Pattern.compile("http://"); //正则规则 符合http://的
        Pattern mPattern2 = Pattern.compile("https://"); //正则规则 符合https://的
        Pattern mPattern3 = Pattern.compile("www."); //正则规则 符合www.
        Matcher mMatcher1 = mPattern1.matcher(inputStr); //正则匹配
        Matcher mMatcher2 = mPattern2.matcher(inputStr); //正则匹配
        Matcher mMatcher3 = mPattern3.matcher(inputStr); //正则匹配
        String tempStr = inputStr; //赋值inputStr 到 tempStr
        Matcher mMatcher = null; //声明局部变量
        if (mMatcher1.find()) { //正则规则匹配
            mMatcher = mMatcher1; //使用mMatcher1规则
        } else if (mMatcher2.find()) { //正则规则匹配
            mMatcher = mMatcher2;//使用mMatcher2规则
        } else if (mMatcher3.find()) { //正则规则匹配
            mMatcher = mMatcher3; //使用mMatcher3规则
        } else {
            return new SpannableString(inputStr); //没有匹配直接返回 原字符块
        }
        mMatcher.reset(inputStr); //重置
        SpannableStringBuilder ssb = new SpannableStringBuilder(); //初始化字符块创建者
        while (mMatcher.find()) { //正则规则匹配
            int start = mMatcher.start(); //开始位置
            int end = tempStr.indexOf(" ", start) + 1; //结束位置
            int breakEnd = tempStr.indexOf("\n", start) + 1; //跳出结束符
            if ((breakEnd < end && breakEnd != 0) || (end == 0 && breakEnd != 0)) {
                // 换行符 也算一个HTTP连接
                end = breakEnd;
            }
            ssb.append(tempStr.substring(0, start)); //追加 截取字符串 0 到开始位置
            if (end == 0) { //结束位置=0
                end = tempStr.length(); //取字符串长度
                tempStr = tempStr.substring(start, end); //截取开始位置 到字符串长度结束
                String regExp = "[\u4E00-\u9FA5]|[\uFF00-\uFFEF]|[\u2E80-\u2EFF]|[\u3000-\u303F]|[\u31C0-\u31EF]"; // 标准CJK文字
                // ,全角ASCII、全角中英文标点、半宽片假名、半宽平假名、半宽韩文字母,CJK部首,CJK标点符号,CJK笔划
                Pattern p = Pattern.compile(regExp); //正则规则
                Matcher m = p.matcher(tempStr); //正则匹配
                String urlName = null; //声明urlName
                if (m.find()) { // 正则规则查找
                    end = m.start(); //结束位置赋值
                    urlName = tempStr.substring(0, end); //截取字符串 0 到结束
                }
                if (urlName == null) { //空指针判读
                    urlName = tempStr; //赋值
                }
                ssb.append(setUrl(context, urlName)); //追加字符串
                if (end > tempStr.length()) { //如果结束长度 大于 字符串长度
                    tempStr = tempStr.substring(tempStr.length()); //按字符串长度截取
                } else {
                    tempStr = tempStr.substring(end); //按结束长度截取
                }
                mMatcher.reset(tempStr);
                // 此处 应该为最后的URL
            } else { //else
                String url = tempStr.substring(start, end); //截取 开始位置和结束位置
                ssb.append(setUrl(context, url)); //追加
                tempStr = tempStr.substring(end); //截取结束位置 赋值给tempStr
                mMatcher.reset(tempStr); //重置
            }
        }
        ssb.append(tempStr);
        return new SpannableString(ssb); //返回
    }

    private static SpannableString setUrl(final Context context, String input) { //方法名
        int endPos = -1; //-1
        int startPos = -1; //-1
        startPos = getPos(input);
        SpannableString sp = new SpannableString(input);
        if (startPos != -1) { //判断!=-1
            String tmpStr = input.substring(startPos, input.length()); //按位置截取
            endPos = tmpStr.indexOf(" "); //找空白 下标
            if (endPos == -1) { //判断!=-1
                endPos = input.length();
            } else {
                endPos = startPos + endPos;
            }
            final String webUrl = input.substring(startPos, endPos);
            sp.setSpan(new Clickable(new View.OnClickListener() {

                @Override //重写
                public void onClick(View v) {
                    try { //try
                        Intent yt = new Intent();//意图new
                        yt.setAction("android.intent.action.VIEW");//设置意图
                        Uri content_url = null; //默认创建变量
                        if (webUrl.indexOf("http://") >= 0 || webUrl.indexOf("https://") >= 0) { //寻找http和https
                            content_url = Uri.parse(webUrl);  //转码
                        } else {
                            content_url = Uri.parse("http://" + webUrl); //转码
                        }
                        yt.setData(content_url); //设置DATA
                        context.startActivity(yt); //启动activity
                    } catch (Exception e) { //异常
                        e.printStackTrace(); //异常输出
                    }
                }
            }), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else { //else

        }
        return sp; //返回
    }

    public static void addEmotionDisplay(Context context, SpannableStringBuilder spannable, int emoticon, int fontSize, String facename) {
        Drawable drawable = getDrawable(context, emoticon);
        if (drawable != null) {
            int itemHeight;
            int itemWidth;
            if (fontSize == WRAP_DRAWABLE) {
                itemHeight = drawable.getIntrinsicHeight();
                itemWidth = drawable.getIntrinsicWidth();
            } else {
                itemHeight = fontSize;
                itemWidth = fontSize;
            }
            drawable.setBounds(0, 0, itemHeight, itemWidth);
            ImageSpan imageSpan = new EmoticonSpan(drawable);
            SpannableString spanStr = new SpannableString(facename);
            spanStr.setSpan(imageSpan, 0, facename.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.append(spanStr);
        }
    }

    /**
     * 用emoji做分割，遍历添加字符串
     * @param context
     * @param text
     * @param fontSize
     * @return
     */
    public static StyleBuilder styleBuilderFilterWithUrl(Context context, String text, int fontSize) {
        StyleBuilder styleBuilder = new StyleBuilder();
        Matcher m = getMatcher(text);
        String tempStr = text;
        int start = 0;
        int end = 0;
        if (m != null) {
            while (m.find()) {
                String key = m.group();
                int icon = 0;
                if (HHEmoticons.sEmoticonHashMap.containsKey(key)) {
                    icon = HHEmoticons.sEmoticonHashMap.get(key);
                }
                if (icon > 0) {
                    start = m.start();
                    end = m.end();
                    String buffer = tempStr.substring(0, start);// 两个emoji之间的字符串
                    if (!TextUtils.isEmpty(buffer)) {
                        parseMyUrlString(context, buffer, styleBuilder);
                    }

                    addEmotionStyleBuilderDisplay(context, styleBuilder, icon, fontSize, key);
                    tempStr = tempStr.substring(end, tempStr.length()); //截取字符串
                    m.reset(tempStr);// 更新查找的字符串
                }
            }
            if (!TextUtils.isEmpty(tempStr)) {
                parseMyUrlString(context, tempStr, styleBuilder); //追加字符串
            }
        }
        return styleBuilder;
    }

    private static void parseMyUrlString(Context context, String inputStr, StyleBuilder styleBuilder) { // 方法名
        if (styleBuilder == null) return;
        Pattern mPattern1 = Pattern.compile("http://"); //正则规则 符合http://的
        Pattern mPattern2 = Pattern.compile("https://"); //正则规则 符合https://的
        Pattern mPattern3 = Pattern.compile("www."); //正则规则 符合www.
        Matcher mMatcher1 = mPattern1.matcher(inputStr); //正则匹配
        Matcher mMatcher2 = mPattern2.matcher(inputStr); //正则匹配
        Matcher mMatcher3 = mPattern3.matcher(inputStr); //正则匹配
        String tempStr = inputStr; //赋值inputStr 到 tempStr
        Matcher mMatcher = null; //声明局部变量
        if (mMatcher1.find()) { //正则规则匹配
            mMatcher = mMatcher1; //使用mMatcher1规则
        } else if (mMatcher2.find()) { //正则规则匹配
            mMatcher = mMatcher2;//使用mMatcher2规则
        } else if (mMatcher3.find()) { //正则规则匹配
            mMatcher = mMatcher3; //使用mMatcher3规则
        } else {
            styleBuilder.addStyleItem(new TextStyleItem(inputStr));
            return; //没有匹配直接返回 原字符块
        }
        mMatcher.reset(inputStr); //重置
        while (mMatcher.find()) { //正则规则匹配
            int start = mMatcher.start(); //开始位置
            int end = tempStr.indexOf(" ", start) + 1; //结束位置
            int breakEnd = tempStr.indexOf("\n", start) + 1; //跳出结束符
            if ((breakEnd < end && breakEnd != 0) || (end == 0 && breakEnd != 0)) {
                // 换行符 也算一个HTTP连接
                end = breakEnd;
            }
            styleBuilder.text(tempStr.substring(0, start)); //追加 截取字符串 0 到开始位置
            if (end == 0) { //结束位置=0
                end = tempStr.length(); //取字符串长度
                tempStr = tempStr.substring(start, end); //截取开始位置 到字符串长度结束
                String regExp = "[\u4E00-\u9FA5]|[\uFF00-\uFFEF]|[\u2E80-\u2EFF]|[\u3000-\u303F]|[\u31C0-\u31EF]"; // 标准CJK文字
                // ,全角ASCII、全角中英文标点、半宽片假名、半宽平假名、半宽韩文字母,CJK部首,CJK标点符号,CJK笔划
                Pattern p = Pattern.compile(regExp); //正则规则
                Matcher m = p.matcher(tempStr); //正则匹配
                String urlName = null; //声明urlName
                if (m.find()) { // 正则规则查找
                    end = m.start(); //结束位置赋值
                    urlName = tempStr.substring(0, end); //截取字符串 0 到结束
                }
                if (urlName == null) { //空指针判读
                    urlName = tempStr; //赋值
                }
                setUrl(context, urlName, styleBuilder); //追加字符串
                if (end > tempStr.length()) { //如果结束长度 大于 字符串长度
                    tempStr = tempStr.substring(tempStr.length()); //按字符串长度截取
                } else {
                    tempStr = tempStr.substring(end); //按结束长度截取
                }
                mMatcher.reset(tempStr);
                // 此处 应该为最后的URL
            } else { //else
                String url = tempStr.substring(start, end); //截取 开始位置和结束位置
                setUrl(context, url, styleBuilder); //追加
                if (url.endsWith("\n")) {
                    tempStr = tempStr.substring(end);
                } else {
                    tempStr = tempStr.substring(end - 1); //截取结束位置 赋值给tempStr
                }
                mMatcher.reset(tempStr); //重置
            }
        }
        styleBuilder.text(tempStr);
    }

    private static void setUrl(final Context context, String input, StyleBuilder styleBuilder) { //方法名
        int endPos = -1; //-1
        int startPos = -1; //-1
        startPos = getPos(input);
        if (startPos != -1) { //判断!=-1
            String tmpStr = input.substring(startPos, input.length()); //按位置截取
            endPos = tmpStr.indexOf(" "); //找空白 下标
            if (endPos == -1) { //判断!=-1
                endPos = input.length();
            } else {
                endPos = startPos + endPos;
            }
            styleBuilder.text(input.substring(0, startPos));
            final String webUrl = input.substring(startPos, endPos);
            TextStyleItem webUrlItem = new TextStyleItem(webUrl).setUnderLined(true);
            webUrlItem.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            webUrlItem.setClickListener(new OnUrlClickListener(context, webUrl));
            styleBuilder.addStyleItem(webUrlItem);
        } else { //else
            styleBuilder.text(input);
        }
    }

    static class OnUrlClickListener implements TextStyleItem.OnClickListener {

        private Context context;
        private String webUrl;

        public OnUrlClickListener(Context context, String webUrl) {
            this.context = context;
            this.webUrl = webUrl;
        }

        @Override
        public void onClick(String clickedText) {
            try { //try
                Intent yt = new Intent();//意图new
                yt.setAction("android.intent.action.VIEW");//设置意图
                Uri content_url = null; //默认创建变量
                if (webUrl.indexOf("http://") >= 0 || webUrl.indexOf("https://") >= 0) { //寻找http和https
                    content_url = Uri.parse(webUrl);  //转码
                } else {
                    content_url = Uri.parse("http://" + webUrl); //转码
                }
                yt.setData(content_url); //设置DATA
                context.startActivity(yt); //启动activity
            } catch (Exception e) { //异常
                e.printStackTrace(); //异常输出
            }
        }
    }

    private static int getPos(String input) {
        if (input.indexOf("http://") >= 0) { //包含http://
            return input.indexOf("http://"); //返回 http://位置
        } else if (input.indexOf("https://") >= 0) { //包含https://位置
            return input.indexOf("https://"); //返回https位置
            // }else if(input.indexOf("ftp://")>=0){
            // return input.indexOf("ftp://");
        } else if (input.indexOf("www.") >= 0) { //是否包含www位置
            return input.indexOf("www."); //返回www位置
        } else {
            return -1; //未找到返回-1
        }
    }

    static class Clickable extends ClickableSpan implements View.OnClickListener { //字符块点击事件
        private final View.OnClickListener mListener;

        public Clickable(View.OnClickListener l) {
            mListener = l;
        }

        @Override //重写
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }

    private void clearSpan(Spannable spannable, int start, int end) {
        if (start == end) {
            return;
        }
        EmoticonSpan[] oldSpans = spannable.getSpans(start, end, EmoticonSpan.class);
        for (int i = 0; i < oldSpans.length; i++) {
            spannable.removeSpan(oldSpans[i]);
        }
    }

    public static void emoticonDisplay(Context context, Spannable spannable, int emoticon, int fontSize, int start, int end) {
        Drawable drawable = getDrawable(context, emoticon);
        if (drawable != null) {
            int itemHeight;
            int itemWidth;
            if (fontSize == WRAP_DRAWABLE) {
                itemHeight = drawable.getIntrinsicHeight();
                itemWidth = drawable.getIntrinsicWidth();
            } else {
                itemHeight = fontSize + ViewUtils.dip2px(4);
                itemWidth = fontSize + ViewUtils.dip2px(4);
            }
            drawable.setBounds(0, 0, itemHeight, itemWidth);
            EmoticonSpan imageSpan = new EmoticonSpan(drawable);
            spannable.setSpan(imageSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
    }

    public static void addEmotionStyleBuilderDisplay(Context context, StyleBuilder styleBuilder, int emoticon, int fontSize, String facename) {
        Drawable drawable = getDrawable(context, emoticon);
        if (drawable != null) {
            int itemHeight;
            int itemWidth;
            if (fontSize == WRAP_DRAWABLE) {
                itemHeight = drawable.getIntrinsicHeight();
                itemWidth = drawable.getIntrinsicWidth();
            } else {
                itemHeight = fontSize;
                itemWidth = fontSize;
            }
            drawable.setBounds(0, 0, itemHeight, itemWidth);
            styleBuilder.addStyleItem(new TextStyleItem(facename).setIconDrawable(drawable));
        }
    }
}
