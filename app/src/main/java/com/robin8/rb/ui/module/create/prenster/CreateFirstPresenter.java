package com.robin8.rb.ui.module.create.prenster;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.andview.refreshview.XRefreshView;
import com.robin8.rb.R;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.ui.module.create.adapter.CreateFirstListAdapter;
import com.robin8.rb.ui.module.create.model.CpsArticlesBean;
import com.robin8.rb.ui.module.create.model.CreateFirstModel;
import com.robin8.rb.ui.module.create.view.ICreateFirstView;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.PresenterI;
import com.robin8.rb.task.LoginTask;
import com.robin8.rb.ui.widget.RefreshFooterView;
import com.robin8.rb.ui.widget.RefreshHeaderView;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Figo
 */
public class CreateFirstPresenter implements PresenterI {

    private final static int INIT_DATA = 0;
    private final static int DRAG_REFRESH = 1;
    public final static int LOAD_MORE = 2;
    private static final String ORDER_BY_CREATED = "order_by_created";
    private static final String ORDER_BY_HOT = "order_by_hot";
    private Activity mActivity;
    private int mCurrentState = INIT_DATA;
    private XRefreshView mXRefreshView;
    private RefreshHeaderView mRefreshHeaderView;
    private RefreshFooterView mRefreshFooterView;
    private RecyclerView mRecyclerView;
    private ICreateFirstView mICreateFirstView;
    private Context mContext;
    private WProgressDialog mWProgressDialog;
    private RequestParams mRequestParams;
    private int mCurrentPage = 1;
    private int mTotalPages;
    private boolean mWithKolAnnouncementB = true;
    private LinearLayoutManager mLinearLayoutManager;
    private CreateFirstListAdapter mCreateFirstListAdapter;
    private List<Object> mDataList = new ArrayList<>();
    private String mUrl;
    private String mCurrentOrder = ORDER_BY_CREATED;
    private final LinearLayout mErrorViewLL;

    public CreateFirstPresenter(ICreateFirstView view, String url, Activity activity) {

        mICreateFirstView = view;
        mUrl = url;
        mActivity = activity;
        mXRefreshView = mICreateFirstView.getXRefreshView();
        mRecyclerView = mICreateFirstView.getRecyclerView();
        mRefreshHeaderView = mICreateFirstView.getRefreshHeaderView();
        mRefreshFooterView = mICreateFirstView.getRefreshFooterView();
        mErrorViewLL = mICreateFirstView.getErrorView();
        mContext = mRecyclerView.getContext();
        initXRefreshView();
        initRecyclerView();
    }

    public void init() {

        loadData();
    }

    private void initXRefreshView() {

        mXRefreshView.setPullLoadEnable(true);
        mXRefreshView.setSlienceLoadMore();
        mXRefreshView.setAutoLoadMore(false);
        mXRefreshView.setCustomHeaderView(mRefreshHeaderView);
        mXRefreshView.setCustomFooterView(mRefreshFooterView);
        mXRefreshView.post(new Runnable() {

            @Override
            public void run() {

                mRefreshHeaderView.findViewById(R.id.ll_setting).getLayoutParams().width = DensityUtils.getScreenWidth(mContext);
            }
        });

        mXRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {

                mCurrentState = DRAG_REFRESH;
                loadData();
            }

            @Override
            public void onLoadMore(boolean isSlience) {

                mCurrentState = LOAD_MORE;
                loadData();
            }
        });
    }

    private void initRecyclerView() {

        mCreateFirstListAdapter = new CreateFirstListAdapter(mDataList, mActivity);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mCreateFirstListAdapter);
    }

    private void loadData() {

        if (TextUtils.isEmpty(HelpTools.getLoginInfo(HelpTools.Token))) {
            initLoginInfo();
        } else {
            getDataFromNet();
        }
    }

    @Override
    public void getDataFromServer(boolean needHeader, int method, String url, RequestParams params, RequestCallback callback) {

        switch (method) {
            case HttpRequest.GET:
                HttpRequest.getInstance().get(needHeader, url, params, callback);
                break;
            case HttpRequest.POST:
                HttpRequest.getInstance().post(needHeader, url, params, callback);
                break;
        }
    }

    private void initLoginInfo() {

        LoginTask loginTask = LoginTask.newInstance(mActivity.getApplicationContext());
        loginTask.start(new RequestCallback() {

            @Override
            public void onError(Exception e) {

                CustomToast.showShort(mActivity.getApplicationContext(), R.string.robin391);
            }

            @Override
            public void onResponse(String response) {
              //  LogUtil.LogShitou("创作","===>"+response);
                getDataFromNet();
            }
        }, CommonConfig.TOURIST_PHONE, CommonConfig.TOURIST_CODE, null,null);

    }


    /**
     * 加载网络数据
     */
    private void getDataFromNet() {

        if (mCurrentState != LOAD_MORE) {
            mCurrentPage = 1;
        } else {
            if (mTotalPages != 0 && mCurrentPage > mTotalPages) {
                mXRefreshView.stopLoadMore(true);
                return;
            }
        }

        if (mCurrentState == LOAD_MORE) {
            mWithKolAnnouncementB = false;
        }

        if (mCurrentState == INIT_DATA) {
            if (mWProgressDialog == null) {
                mWProgressDialog = mICreateFirstView.getWProgressDialog();
            }
            if (! mWProgressDialog.isShowing()) {
                try {
                    mWProgressDialog.show();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        if (mRequestParams == null) {
            mRequestParams = new RequestParams();
        }
        //是否加载更多
        if (mWithKolAnnouncementB) {
            mRequestParams.put("with_kol_announcement", "Y");
        } else {
            mRequestParams.put("with_kol_announcement", "N");
        }
        mRequestParams.put("page", mCurrentPage);
        mRequestParams.put("order", mCurrentOrder);
        getDataFromServer(true, HttpRequest.GET, mUrl, mRequestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {

                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                if (mXRefreshView != null) {
                    mXRefreshView.stopRefresh();
                    mXRefreshView.stopLoadMore(true);
                }
                if (mCurrentState == INIT_DATA && mErrorViewLL != null) {
                    mErrorViewLL.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onResponse(String response) {
                if (mErrorViewLL != null) {
                    mErrorViewLL.setVisibility(View.GONE);
                }
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                if (mXRefreshView != null) {
                    mXRefreshView.stopRefresh();
                    mXRefreshView.stopLoadMore(true);
                }
                if (mRefreshHeaderView != null && mCurrentState == INIT_DATA) {
                    mRefreshHeaderView.setRefreshTime(System.currentTimeMillis());
                }
                parseJson(response);
            }
        });
    }

    private void parseJson(String response) {

        CreateFirstModel createFirstModel = GsonTools.jsonToBean(response, CreateFirstModel.class);
        if (createFirstModel != null && createFirstModel.getError() == 0) {
            if (mCurrentState != LOAD_MORE) {
                mDataList.clear();
                mDataList.add(null);
            }
            mTotalPages = createFirstModel.getTotal_pages();
            List<CpsArticlesBean> tempList = createFirstModel.getCps_articles();
            if (tempList != null && tempList.size() > 0) {
                mDataList.addAll(tempList);
                mCreateFirstListAdapter.setDataList(mDataList);
                mCreateFirstListAdapter.notifyDataSetChanged();
                mCurrentPage++;
                return;
            }
        }
       /* if (mCurrentState != LOAD_MORE && mDataList.size() == 0) {
            mXRefreshView.setVisibility(View.GONE);
        } else {
            mXRefreshView.setVisibility(View.VISIBLE);
        }*/
    }
}
