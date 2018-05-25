package com.baigu.dms.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.text.ClipboardManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.widget.TextView;
import android.widget.Toast;


import com.baigu.dms.BaseApplication;
import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.activity.LoginActivity;
import com.baigu.dms.common.view.toast.Toasty;
import com.baigu.dms.domain.cache.ShopCart;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.netservice.common.token.TokenManager;
import com.hyphenate.chat.ChatClient;
import com.micky.logger.Logger;


public class ViewUtils {

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null && activity.getCurrentFocus().getWindowToken() != null) {
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null && activity.getCurrentFocus().getWindowToken() != null) {
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.SHOW_FORCED);
        }
    }

    /**
     * 复制
     */
    public static void copy(Context context, String text) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context
                .CLIPBOARD_SERVICE);
        cmb.setText(text.trim());
    }

    /**
     * 粘贴
     */
    public static String paste(Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context
                .CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }

    public static DisplayMetrics getScreenInfo(Context context) {
        // 获取屏幕大小
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm;
    }

    /**
     * 获取屏幕分辨率
     *
     * @param context
     * @return
     */
    public static int[] getScreenSize(Context context) {
        DisplayMetrics metrics = getScreenInfo(context);
        return new int[]{metrics.widthPixels, metrics.heightPixels};
    }

    /***
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    // 将px值转换为dip或dp值，保证尺寸大小不变
    public static int px2dip(float pxValue) {
        final float scale = BaseApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    // 将dip或dp值转换为px值，保证尺寸大小不变
    public static int dip2px(float dipValue) {
        final float scale = BaseApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    // 将px值转换为sp值，保证文字大小不变
    public static int px2sp(float pxValue) {
        final float fontScale = BaseApplication.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    // 将sp值转换为px值，保证文字大小不变
    public static int sp2px(float spValue) {
        final float fontScale = BaseApplication.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**防止连续点击*/
    private static long lastClickTime;
    public static boolean isFastClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( timeD > 0 && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 根据图片名称获取图片的资源id的方法
     * @param iconName
     * @return
     */
    public static int getResource(Context context, String iconName) {
        int resId = context.getResources().getIdentifier(iconName, "mipmap", context.getPackageName());
        return resId;
    }

    public static void hideInputMethod(Activity activity) {
        try {
            if(activity.getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {

        }
    }

    public static float getTextViewLength(Paint paint, String text){
        float textLength = paint.measureText(text);
        return textLength;
    }

    public static void exitApp(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(BaseActivity.ACTION_LOGOUT));
    }

    public static void restartApp(Context context) {
        exitApp(context);
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void toAppDetailSetting(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(intent);
    }

    public static void showToastInfo(String text) {
        Toasty.info(BaseApplication.getContext(), text, Toast.LENGTH_LONG).show();
    }

    public static void showToastInfo(int resId) {
        Context context = BaseApplication.getContext();
        Toasty.info(context, context.getString(resId), Toast.LENGTH_LONG).show();
    }

    public static void showToastWarning(String text) {
        Toasty.warning(BaseApplication.getContext(), text, Toast.LENGTH_LONG).show();
    }

    public static void showToastWarning(int resId) {
        Context context = BaseApplication.getContext();
        Toasty.warning(context, context.getString(resId), Toast.LENGTH_LONG).show();
    }

    public static void showToastError(String text) {
        Toasty.error(BaseApplication.getContext(), text, Toast.LENGTH_LONG).show();
    }

    public static void showToastError(int resId) {
        Context context = BaseApplication.getContext();
        Toasty.error(context, context.getString(resId), Toast.LENGTH_LONG).show();
    }

    public static void showToastSuccess(String text) {
        Toasty.success(BaseApplication.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static void showToastSuccess(int resId) {
        Context context = BaseApplication.getContext();
        Toasty.success(context, context.getString(resId), Toast.LENGTH_SHORT).show();
    }
    
    public static void setupWebViewSettings(WebSettings settings) {
        if (settings == null) return;
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(false);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setAppCacheEnabled(false);
        settings.setDomStorageEnabled(false);
        settings.setUseWideViewPort(false);
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false);
        settings.setDisplayZoomControls(false);
    }

   public static void setStatusColor(Activity activity, int color) {
       if (activity == null) {
           return;
       }
       try {
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
               Window window = activity.getWindow();
               window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
               window.setStatusBarColor(activity.getResources().getColor(R.color.black));
               //底部导航栏
               //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
           }
       } catch (Exception e) {
           Logger.e(e, e.getMessage());
       }
   }

   /**关闭APP*/
   public static void closeApp(Context context) {
       if (ChatClient.getInstance().isLoggedInBefore()) {
           IMHelper.getInstance().logout();
       }
       TokenManager.getInstance().setToken("");
       SPUtils.putObject("token","");
       UserCache.getInstance().destroy();
       ShopCart.clearCart();

       Intent intent = new Intent(context, LoginActivity.class);
       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
       context.startActivity(intent);
       Intent logoutIntent = new Intent(BaseActivity.ACTION_LOGOUT);
       LocalBroadcastManager.getInstance(context).sendBroadcast(logoutIntent);
   }

   /**退出登录*/
    public static void logout(Context context) {
        UserCache.getInstance().setUser(null);
        closeApp(context);
    }
}
