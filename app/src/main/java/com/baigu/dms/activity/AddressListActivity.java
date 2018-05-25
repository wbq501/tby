package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;

import com.baigu.dms.R;
import com.baigu.dms.adapter.AddressAdapter;
import com.baigu.dms.adapter.BaseRVAdapter;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.utils.rxbus.EventType;
import com.baigu.dms.common.utils.rxbus.RxBus;
import com.baigu.dms.common.view.OnRVItemClickListener;
import com.baigu.dms.domain.model.Address;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.presenter.AddressPresenter;
import com.baigu.dms.presenter.impl.AddressPresenterImpl;
import com.baigu.lrecyclerview.interfaces.OnLoadMoreListener;
import com.baigu.lrecyclerview.recyclerview.LRecyclerView;
import com.baigu.lrecyclerview.recyclerview.LRecyclerViewAdapter;
import com.baigu.lrecyclerview.recyclerview.ProgressStyle;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;


/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class AddressListActivity extends BaseActivity implements AddressPresenter.AddressView, OnLoadMoreListener, OnRVItemClickListener {

    public static final int REQUEST_CODE_ADD_UPDATE = 10012;

    private LRecyclerView mRvAddress;
    private AddressAdapter mAddressAdapter;

    private AddressPresenter mAddressPresenter;

    private int mCurrPage = 1;
    private boolean mAddressSelect = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        initToolBar();
        mAddressSelect = getIntent().getBooleanExtra("select", false);
        int titleResId = mAddressSelect ? R.string.address_select : R.string.address_select;
        setTitle(titleResId);

        mAddressPresenter = new AddressPresenterImpl(this, this);
        initView();
        Flowable.timer(100L, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        loadData();
                    }
                });

    }

    private void initView() {
        mRvAddress = findView(R.id.rv_city);
        mRvAddress.setLoadingMoreProgressStyle(ProgressStyle.BallBeat);
        mRvAddress.setHeaderViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.main_bg);
        mRvAddress.setFooterViewColor(R.color.colorPrimary, R.color.colorPrimary, R.color.main_bg);
        mRvAddress.setLayoutManager(new LinearLayoutManager(this));
        mAddressAdapter = new AddressAdapter(this, mAddressPresenter);
        mAddressAdapter.setOnItemClickListener(this);
        mAddressAdapter.setSelectAble(mAddressSelect);
        mRvAddress.setAdapter(new LRecyclerViewAdapter(mAddressAdapter));
        mRvAddress.setPullRefreshEnabled(false);
        mRvAddress.setLoadMoreEnabled(true);
        mRvAddress.setOnLoadMoreListener(this);

        findViewById(R.id.rl_add_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ViewUtils.isFastClick()) return;
                startActivityForResult(new Intent(AddressListActivity.this, AddressAddEditActivity.class), REQUEST_CODE_ADD_UPDATE);
            }
        });
    }

    private void loadData() {
        mCurrPage = 1;
        mRvAddress.setNoMore(false);
        mAddressPresenter.loadAddress(mCurrPage, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_UPDATE && resultCode == RESULT_OK) {
            mCurrPage = 1;
            mRvAddress.setNoMore(false);
            mAddressPresenter.loadAddress(mCurrPage, true);
        }
    }

    @Override
    public void onLoadMore() {
        mAddressPresenter.loadAddress(mCurrPage, false);
    }

    @Override
    public void onLoadAddress(PageResult<Address> addressPageResult) {
        mRvAddress.setNoMore(addressPageResult != null && addressPageResult.lastPage);
        if (addressPageResult == null) {
            if (mCurrPage == 1) {
                mAddressAdapter.clearData();
                mAddressAdapter.notifyDataSetChanged();
            }
            ViewUtils.showToastError(R.string.failed_load_data);
            return;
        }
        if (addressPageResult.list == null && mCurrPage == 1) {
            mAddressAdapter.clearData();
            mAddressAdapter.notifyDataSetChanged();
        }
        if (addressPageResult.list != null) {
            if (mCurrPage == 1) {
                mAddressAdapter.setData(addressPageResult.list);
            } else {
                mAddressAdapter.appendDataList(addressPageResult.list);
            }
            mAddressAdapter.notifyDataSetChanged();
            mCurrPage++;
        }
    }

    @Override
    public void onSaveOrUpdateAddress(Address address, boolean result) {
        if (!result) {
            ViewUtils.showToastError(R.string.failed_update_address);
            return;
        }
        loadData();

    }

    @Override
    public void onDeleteAddress(Address address, boolean b) {
        if (b) {
            mAddressAdapter.getDataList().remove(address);
            mAddressAdapter.notifyDataSetChanged();
            RxBus.getDefault().post(EventType.TYPE_ADDRESS_DELETE, address.getId());
        } else {
            ViewUtils.showToastError(R.string.failed_update_address);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAddressPresenter != null) {
            mAddressPresenter.onDestroy();
        }
    }

    @Override
    public void onItemClick(BaseRVAdapter adapter, int position) {
        final Address address = mAddressAdapter.getItem(position);
        Intent intent = getIntent();
        intent.putExtra("address", address);
        setResult(RESULT_OK, intent);
        finish();
    }
}
