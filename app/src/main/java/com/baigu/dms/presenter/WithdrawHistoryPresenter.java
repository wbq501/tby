package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.WithdrawHistory;

import java.util.List;

public interface WithdrawHistoryPresenter extends BasePresenter{

    void getHistory(String memberId,int page,boolean showDialog,boolean isWithdrawHistory);

    interface WithdrawHistoryView{
        void getHistory(List<WithdrawHistory> result);
    }
}
