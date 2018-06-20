package com.baigu.dms.common.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baigu.dms.activity.ShowImageActivity;

import java.util.ArrayList;
import java.util.List;

public class MJavascriptInterface {
    private Context context;
    private List<String> imageUrls;

    public MJavascriptInterface(Context context,List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @android.webkit.JavascriptInterface
    public void openImage(String img) {
        int position = 0;
        Intent intent = new Intent();
        intent.putStringArrayListExtra("data", (ArrayList<String>) imageUrls);
        for (int i = 0; i < imageUrls.size(); i++){
            if (img.equals(imageUrls.get(i))){
                position = i;
            }
        }
        intent.putExtra("curImageUrl", img);
        intent.putExtra("position",position);
        intent.setClass(context, ShowImageActivity.class);
        context.startActivity(intent);
    }
}
