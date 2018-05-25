package com.baigu.dms.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.SPUtils;
import com.baigu.dms.domain.cache.UserCache;
import com.kyleduo.switchbutton.SwitchButton;

/**
 * @Description 勿扰模式
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class FazeModeActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faze_mode);
        initToolBar();
        setTitle(R.string.faze_mode);
        initView();
    }

    private void initView() {
        SwitchButton sbSound = findView(R.id.sb_sound);
        sbSound.setOnCheckedChangeListener(this);
        sbSound.setChecked(SPUtils.getObject(SPUtils.KEY_SOUND, true));

        SwitchButton sbVibrate = findView(R.id.sb_vibrate);
        sbVibrate.setOnCheckedChangeListener(this);
        sbVibrate.setChecked(SPUtils.getObject(SPUtils.KEY_VIBRATE, true));
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.sb_sound:
                UserCache.getInstance().setSound(b);
                break;
            case R.id.sb_vibrate:
                UserCache.getInstance().setVibrate(b);
                break;
            default:
                break;
        }
    }
}
