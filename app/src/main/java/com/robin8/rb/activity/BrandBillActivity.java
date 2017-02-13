package com.robin8.rb.activity;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.andview.refreshview.XRefreshView;
import com.robin8.rb.R;
import com.robin8.rb.adapter.BrandBillAdapter;
import com.robin8.rb.adapter.IncomeDetailAdapter;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.model.BrandBillModel;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.RefreshFooterView;
import com.robin8.rb.ui.widget.RefreshHeaderView;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.util.GsonTools;

import java.util.ArrayList;
import java.util.List;

/**
 * 广告主--我的账单页面
 */
public class BrandBillActivity extends BaseActivity implements IncomeDetailAdapter.OnBottomListener {

    private final static int INIT_DATA = 0;
    private final static int DRAG_REFRESH = 1;
    private final static int LOAD_MORE = 2;
    private int mCurrentState = INIT_DATA;
    private WProgressDialog mWProgressDialog;
    private RecyclerView mRecyclerView;
    private BrandBillAdapter mBrandBillAdapter;
    private List<BrandBillModel.BrandBill> mBrandBillList = new ArrayList<BrandBillModel.BrandBill>();
    private XRefreshView mXRefreshView;
    private LinearLayoutManager mLinearLayoutManager;
    private BasePresenter mBasePresenter;
    private RequestParams mRequestParams;
    private int mCurrentPage = 1;
    private RefreshHeaderView mRefreshHeaderView;
    private RefreshFooterView mRefreshFooterView;
    private String url;
    private String title;
    private boolean mHasMore = true;
    private boolean mIsBottomB;
    private View mNoDataLL;

    @Override
    public void setTitleView() {
        initData();
        mTVCenter.setText(title);
    }

    private void initData() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_income_detail, mLLContent, true);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_income_detail);
        mRecyclerView.setHasFixedSize(true);
        mXRefreshView = (XRefreshView) findViewById(R.id.xrefreshview);
        mNoDataLL = findViewById(R.id.ll_no_data);
        initXRefreshView();
        initRecyclerView();
        getDataFromNet();
    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.MY_INCOME;
        super.onResume();
    }

    private void initXRefreshView() {

        mXRefreshView.setPullLoadEnable(true);
        mXRefreshView.setSlienceLoadMore();
        mXRefreshView.setAutoLoadMore(true);

        mRefreshHeaderView = new RefreshHeaderView(this);
        mXRefreshView.setCustomHeaderView(mRefreshHeaderView);
        mXRefreshView.post(new Runnable() {
            @Override
            public void run() {
                mRefreshHeaderView.findViewById(R.id.ll_setting).getLayoutParams().width = DensityUtils.getScreenWidth(BrandBillActivity.this);
            }
        });

        mXRefreshView.setPullLoadEnable(true);
        mXRefreshView.setSlienceLoadMore();
        mXRefreshView.setAutoLoadMore(true);

        mRefreshFooterView = new RefreshFooterView(this);
        mXRefreshView.setCustomFooterView(mRefreshFooterView);

        mXRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                mCurrentState = DRAG_REFRESH;
                getDataFromNet();
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                mCurrentState = LOAD_MORE;
                getDataFromNet();
            }
        });
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int lastPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                mIsBottomB = mBrandBillAdapter.getItemCount() - 1 == lastPosition;
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }
        });
    }

    private void initRecyclerView() {
        mBrandBillAdapter = new BrandBillAdapter(mBrandBillList);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mBrandBillAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 加载网络数据
     */
    private void getDataFromNet() {

        if (mCurrentState == LOAD_MORE && !mHasMore) {
            mXRefreshView.stopLoadMore(true);
            return;
        }

        if (mCurrentState == INIT_DATA) {
            mWProgressDialog = WProgressDialog.createDialog(this);
            mWProgressDialog.show();
        }

        if (mCurrentState != LOAD_MORE) {
            mCurrentPage = 1;
        }

        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }

        if (mRequestParams == null) {
            mRequestParams = new RequestParams();
        }

        mRequestParams.put("page", mCurrentPage);
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, url, mRequestParams, new RequestCallback() {
            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                if (mXRefreshView != null) {
                    mXRefreshView.stopRefresh();
                    mXRefreshView.stopLoadMore();
                }
            }

            @Override
            public void onResponse(String response) {
                if (mXRefreshView != null) {
                    mXRefreshView.stopRefresh();
                    mXRefreshView.stopLoadMore();
                }
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                parseJson(response);
            }
        });

    }

    private void parseJson(String json) {

        BrandBillModel brandBillModel = GsonTools.jsonToBean(json, BrandBillModel.class);
        if (brandBillModel != null && brandBillModel.getError() == 0) {
            List<BrandBillModel.BrandBill> tempList = brandBillModel.getTransactions();
            if (tempList != null && tempList.size() > 0) {
                if (mRefreshHeaderView != null && mCurrentState == INIT_DATA) {
                    mRefreshHeaderView.setRefreshTime(System.currentTimeMillis());
                }

                if (mCurrentState != LOAD_MORE) {
                    mBrandBillList.clear();
                }
                mCurrentPage++;
                mBrandBillList.addAll(tempList);
                mBrandBillAdapter.setDataList(mBrandBillList);
                mBrandBillAdapter.notifyDataSetChanged();
            }
        }
        if (mBrandBillList.size() == 0) {
            mNoDataLL.setVisibility(View.VISIBLE);
            mXRefreshView.setVisibility(View.GONE);
        }
    }


        @Override
        protected void executeOnclickLeftView () {
            finish();
        }

        @Override
        protected void executeOnclickRightView () {
        }

        @Override
        public void isOnBottom ( boolean isBottom){
            this.mIsBottomB = isBottom;
        }
    }
