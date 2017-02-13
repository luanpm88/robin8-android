package com.robin8.rb.module.create.fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.R;
import com.robin8.rb.adapter.ViewPagerAdapter;
import com.robin8.rb.base.BaseFragment;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.indiana.protocol.IProtocol;
import com.robin8.rb.module.create.protocol.MyCreateFragmentProtocol;
import com.robin8.rb.module.mine.protocol.MyPaticipateFragmentProtocol;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.RefreshFooterView;
import com.robin8.rb.ui.widget.RefreshHeaderView;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.DensityUtils;

public class CreateContentFragment extends BaseFragment {

    private final static int INIT_DATA = 0;
    public final static int DRAG_REFRESH = 1;
    private final static int LOAD_MORE = 2;
    private int mCurrentState = INIT_DATA;

    private ViewPagerAdapter.SelectItem mData;
    private XRefreshView mXRefreshView;
    private RecyclerView mRecyclerView;
    private RefreshHeaderView mRefreshHeaderView;
    private RefreshFooterView mRefreshFooterView;
    private LinearLayoutManager mLinearLayoutManager;
    private WProgressDialog mWProgressDialog;
    private int mCurrentPage;
    private BasePresenter mBasePresenter;
    private RequestParams mRequestParams;
    private BaseRecyclerAdapter mAdapter;
    private View mLLNodata;
    IProtocol mProtocol;
    private String mUrl;
    private String mPageName;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_income_detail);
        mLLNodata = view.findViewById(R.id.ll_nodata);
        mRecyclerView.setHasFixedSize(true);
        mXRefreshView = (XRefreshView) view.findViewById(R.id.xrefreshview);
        loadData();
        initXRefreshView();
        initRecyclerView();
        return view;
    }

    @Override
    public void initData() {

    }

    @Override
    public String getName() {
        return mData.name;
    }

    @Override
    public void setData(ViewPagerAdapter.SelectItem data, String url,String pageName) {
        this.mData = data;
        this.mUrl = url;
        this.mPageName = pageName;
    }

    private void loadData() {
        switch (mPageName){
            case StatisticsAgency.MY_TASK:
                mProtocol = new MyPaticipateFragmentProtocol(mActivity,mData,mLLNodata);
                break;
            case StatisticsAgency.MY_CREATE:
                mProtocol = new MyCreateFragmentProtocol(mActivity,mData,mLLNodata);
                break;
        }
        getDataFromNet(mCurrentState);
    }

    private void initXRefreshView() {

        mXRefreshView.setPullLoadEnable(true);
        mXRefreshView.setSlienceLoadMore();
        mXRefreshView.setAutoLoadMore(true);

        mRefreshHeaderView = new RefreshHeaderView(this.getActivity());
        mXRefreshView.setCustomHeaderView(mRefreshHeaderView);
        mXRefreshView.post(new Runnable() {
            @Override
            public void run() {
                mRefreshHeaderView.findViewById(R.id.ll_setting).getLayoutParams().width = DensityUtils.getScreenWidth(mActivity.getApplicationContext());
            }
        });

        mRefreshFooterView = new RefreshFooterView(this.getActivity());
        mXRefreshView.setCustomFooterView(mRefreshFooterView);

        mXRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                mCurrentState = DRAG_REFRESH;
                getDataFromNet(DRAG_REFRESH);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                mCurrentState = LOAD_MORE;
                getDataFromNet(LOAD_MORE);
            }
        });
    }

    private void initRecyclerView() {
        mAdapter = mProtocol.getAdapter();
        mLinearLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 加载网络数据
     *
     * @param currentState
     */
    public void getDataFromNet(int currentState) {
        prepareData(currentState);
        mRequestParams = mProtocol.getRequestParams();
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, mUrl, mRequestParams, new RequestCallback() {
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
                if (mRefreshHeaderView != null && mCurrentState == INIT_DATA) {
                    mRefreshHeaderView.setRefreshTime(System.currentTimeMillis());
                }
                mProtocol.parseJson(response,mCurrentState);
            }
        });
    }

    private void prepareData(int currentState) {
        if (mData == null) {
            return;
        }

        if (currentState == INIT_DATA) {
            mWProgressDialog = WProgressDialog.createDialog(mActivity);
            mWProgressDialog.show();
        }

        if (currentState != LOAD_MORE) {
            mCurrentPage = 1;
            mProtocol.setCurrentPage(1);
        }

        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }

        mRequestParams = new RequestParams();
    }

}
