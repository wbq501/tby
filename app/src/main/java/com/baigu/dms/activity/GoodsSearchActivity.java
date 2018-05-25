package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.adapter.BaseRVAdapter;
import com.baigu.dms.adapter.GoodsAdapter;
import com.baigu.dms.adapter.GoodsSearchAdapter;
import com.baigu.dms.common.utils.EmptyViewUtil;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.OnRVItemClickListener;
import com.baigu.dms.common.view.PaddingLeftItemDecoration;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.model.GoodsCategory;
import com.baigu.dms.domain.model.ShopAdverPictrue;
import com.baigu.dms.domain.netservice.common.model.GoodsResult;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.GoodsListPresenter;
import com.baigu.dms.presenter.impl.GoodsListPresenterImpl;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/30 12:33
 */
public class GoodsSearchActivity extends BaseActivity implements GoodsListPresenter.GoodsListView, TextWatcher, View.OnClickListener, TextView.OnEditorActionListener, OnRVItemClickListener {

    private RecyclerView mRvGoods;
    private EditText mEtGoodsKey;
    private ImageView mIvClear;
    private GoodsSearchAdapter mGoodsAdapter;

    private GoodsListPresenter mGoodsListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_search);
        initView();
        mGoodsListPresenter = new GoodsListPresenterImpl(this, this);
    }

    private void initView() {
        findViewById(R.id.ll_back).setOnClickListener(this);
        mEtGoodsKey = findView(R.id.et_goods_key);
        mEtGoodsKey.addTextChangedListener(this);
        mEtGoodsKey.setOnEditorActionListener(this);
        mIvClear = findView(R.id.iv_clear);
        mIvClear.setOnClickListener(this);
        mRvGoods = findView(R.id.rv_goods);
        mRvGoods.setLayoutManager(new LinearLayoutManager(this));
        mRvGoods.addItemDecoration(new PaddingLeftItemDecoration(this, 90, true));
        mGoodsAdapter = new GoodsSearchAdapter(this);
        mGoodsAdapter.setOnItemClickListener(this);
        mRvGoods.setAdapter(mGoodsAdapter);
        EmptyViewUtil.initData(this, R.string.search_goods_list_empty);
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        mIvClear.setVisibility(editable.toString().length() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE){
            search();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_clear:
                mEtGoodsKey.setText("");
                break;
            case  R.id.ll_back: {
                ViewUtils.hideKeyboard(this);
                finish();
            }
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ViewUtils.isFastClick()) {
            return super.onOptionsItemSelected(item);
        }
        int id = item.getItemId();
        if (id == R.id.action_search) {
            search();
        }
        return super.onOptionsItemSelected(item);
    }



    private void search() {
        String key = mEtGoodsKey.getText().toString().trim();
        if (TextUtils.isEmpty(key)) {
            ViewUtils.showToastError(R.string.input_tip_product_keyword);
            return;
        }
        String searchKey = mEtGoodsKey.getText().toString().trim();
        mGoodsAdapter.clearData();
        mGoodsAdapter.notifyDataSetChanged();

        EmptyViewUtil.hide(this);

        mGoodsListPresenter.searchGoods(searchKey);
        ViewUtils.hideKeyboard(this);
    }

    @Override
    public void onLoadGoodsCategory(List<GoodsCategory> list) {

    }

    @Override
    public void onLoadGoodsPageList(PageResult<Goods> goodsPageResult) {

    }

    @Override
    public void onLoadGoodsList(GoodsResult goodsList) {

    }

    @Override
    public void onSearchGoods(List<Goods> list) {
        mGoodsAdapter.setData(list);
        mGoodsAdapter.notifyDataSetChanged();
        if (mGoodsAdapter.getItemCount() <= 0) {
            EmptyViewUtil.show(this);
        }
    }

    @Override
    public void onLoadPictrue(BaseResponse<List<ShopAdverPictrue>> response) {

    }

    @Override
    public void onItemClick(BaseRVAdapter adapter, int position) {
        Goods goods = mGoodsAdapter.getItem(position);
        Intent intent = new Intent(this, GoodsDetailActivity.class);
        intent.putExtra("goodsId", goods.getIds());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (mGoodsListPresenter != null) {
            mGoodsListPresenter.onDestroy();
        }
        super.onDestroy();
    }
}
