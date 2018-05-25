package com.baigu.dms.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/24 9:55
 */
public class BaseFragment extends Fragment {

    protected <T extends View> T findView(View view, int id) {
        return (T) view.findViewById(id);
    }
}
