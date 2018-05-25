package com.baigu.dms.activity;

import android.os.Bundle;
import android.view.View;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;
import com.google.gson.GsonBuilder;
import com.hyphenate.helpdesk.callback.ValueCallBack;
import com.hyphenate.helpdesk.domain.NewTicketBody;
import com.hyphenate.helpdesk.manager.TicketManager;
import com.hyphenate.helpdesk.util.Log;

/**
 * @Description 留言
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class LeaveMessageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_message);
        initToolBar();
        setTitle(R.string.leave_message);
        initView();
    }

    private void initView() {
        submit();
    }

    private void submit() {
        NewTicketBody ticketBody = new NewTicketBody();
        ticketBody.setContent("dddd");
        ticketBody.setSubject("ddd");
        NewTicketBody.CreatorBean creatorBean = new NewTicketBody.CreatorBean();
        creatorBean.setEmail("AAA");
        creatorBean.setName("BBB");
        creatorBean.setPhone("18502840555");
        ticketBody.setCreator(creatorBean);

        TicketManager.getInstance().createLeaveMsg(new GsonBuilder().create().toJson(ticketBody).toString(), getString(R.string.hx_project_id), getString(R.string.hx_customer), new ValueCallBack<String>() {

            @Override
            public void onSuccess(final String value) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ViewUtils.showToastSuccess("success");
                        finish();
                    }
                });
            }

            @Override
            public void onError(int code, final String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ViewUtils.showToastSuccess("failed");
                        finish();
                    }
                });
            }

        });
    }
}
