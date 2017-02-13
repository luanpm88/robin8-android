package com.robin8.rb.module.first.prenster;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.andview.refreshview.XRefreshView;
import com.robin8.rb.R;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.module.first.adapter.FirstPageListAdapter;
import com.robin8.rb.module.first.helper.ViewPagerHelper;
import com.robin8.rb.module.first.model.BigVsBean;
import com.robin8.rb.module.first.model.FirstListModel;
import com.robin8.rb.module.first.view.IFirstPageView;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.PresenterI;
import com.robin8.rb.ui.widget.RefreshFooterView;
import com.robin8.rb.ui.widget.RefreshHeaderView;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.util.GsonTools;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Figo
 */
public class SearchResultPresenter implements PresenterI {

    private final static int INIT_DATA = 0;
    private final static int DRAG_REFRESH = 1;
    public final static int LOAD_MORE = 2;
    private static final String ORDER_BY_CREATED = "order_by_created";
    private static final String ORDER_BY_HOT = "order_by_hot";
    private int mCurrentState = INIT_DATA;
    private XRefreshView mXRefreshView;
    private RefreshHeaderView mRefreshHeaderView;
    private RefreshFooterView mRefreshFooterView;
    private RecyclerView mRecyclerView;
    private IFirstPageView mIFirstPageView;
    private Context mContext;
    private String mTagName;
    private WProgressDialog mWProgressDialog;
    private RequestParams mRequestParams;
    private int mCurrentPage = 1;
    private String mKolName;
    private List<Object> mBigVsList = new ArrayList<>();
    private List<Object> mBigVsNewList = new ArrayList<>();
    private int mTotalPages;
    private boolean mWithKolAnnouncementB = true;
    private LinearLayoutManager mLinearLayoutManager;
    private FirstPageListAdapter mFirstPageListAdapter;
    private boolean mNeedHeader;
    private List<Object> mDataList = new ArrayList<>();
    private String mUrl;
    private View mLLNoData;
    private String mCurrentOrder = ORDER_BY_CREATED;

    public SearchResultPresenter(IFirstPageView view, String tagName, String kolName, boolean withHeaderB, String url) {
        mIFirstPageView = view;
        mTagName = tagName;
        mKolName = kolName;
        mNeedHeader = withHeaderB;
        mUrl = url;

        mXRefreshView = mIFirstPageView.getXRefreshView();
        mRecyclerView = mIFirstPageView.getRecyclerView();
        mRefreshHeaderView = mIFirstPageView.getRefreshHeaderView();
        mRefreshFooterView = mIFirstPageView.getRefreshFooterView();
        mLLNoData = mIFirstPageView.getLLNoData();
        mContext = mRecyclerView.getContext();
        initXRefreshView();
        initRecyclerView();
    }

    public void searchKol(String kolName) {
        mKolName = kolName;
        mCurrentState = INIT_DATA;
        getDataFromNet();
    }

    public void init() {
        getDataNewFromNet();
        getDataFromNet();
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
                getDataNewFromNet();
                getDataFromNet();
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                mCurrentState = LOAD_MORE;
                getDataFromNet();
            }
        });
    }

    private void initRecyclerView() {
        mFirstPageListAdapter = new FirstPageListAdapter(mDataList, mNeedHeader);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mFirstPageListAdapter);
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

    /**
     * 加载zuixin数据
     */
    private void getDataNewFromNet() {

        if (!mNeedHeader) {
            return;
        }

        RequestParams requestParams = new RequestParams();
        requestParams.put("order", ORDER_BY_CREATED);
        getDataFromServer(true, HttpRequest.GET, mUrl, requestParams, new RequestCallback() {
            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onResponse(String response) {
                FirstListModel firstListModel = GsonTools.jsonToBean(response, FirstListModel.class);
                if (firstListModel != null && firstListModel.getError() == 0) {
                    List<BigVsBean> tempList = firstListModel.getBig_vs();
                    if (tempList != null && tempList.size() > 0) {
                        mBigVsNewList.addAll(tempList);
                        mFirstPageListAdapter.setNewList(mBigVsNewList);
                        mFirstPageListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
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

        if (mNeedHeader) {
            mCurrentOrder = ORDER_BY_HOT;
        } else {
            mCurrentOrder = ORDER_BY_CREATED;
        }

        if (mCurrentState == LOAD_MORE || !mNeedHeader) {
            mWithKolAnnouncementB = false;
        }

        if (mCurrentState == INIT_DATA) {
            if (mWProgressDialog == null) {
                mWProgressDialog = mIFirstPageView.getWProgressDialog();
            }
            if (!mWProgressDialog.isShowing()) {
                mWProgressDialog.show();
            }
        }
        if (mRequestParams == null) {
            mRequestParams = new RequestParams();
        }

        if (mWithKolAnnouncementB) {
            mRequestParams.put("with_kol_announcement", "Y");
        } else {
            mRequestParams.put("with_kol_announcement", "N");
        }
        mRequestParams.put("page", mCurrentPage);
        mRequestParams.put("tag_name", mTagName);
        mRequestParams.put("name", mKolName);
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
            }

            @Override
            public void onResponse(String response) {
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
        FirstListModel firstListModel = GsonTools.jsonToBean(response, FirstListModel.class);
        if (firstListModel != null && firstListModel.getError() == 0) {
            if (mCurrentState != LOAD_MORE) {
                mBigVsList.clear();
                if (mNeedHeader) {
                    mBigVsList.add(firstListModel.getKol_announcements());
                }
            }
            mTotalPages = firstListModel.getTotal_pages();
            List<BigVsBean> tempList = firstListModel.getBig_vs();
            if (tempList != null && tempList.size() > 0) {
                mBigVsList.addAll(tempList);
                calculate(mBigVsList, mNeedHeader);
                mFirstPageListAdapter.setDataList(mDataList);
                mFirstPageListAdapter.notifyDataSetChanged();
                mCurrentPage++;
                return;
            }
            calculate(mBigVsList, mNeedHeader);
        }

        if (mCurrentState != LOAD_MORE && !mNeedHeader && mDataList.size() == 0) {
            mXRefreshView.setVisibility(View.GONE);
            if (mLLNoData != null) {
                mLLNoData.setVisibility(View.VISIBLE);
            }
        } else {
            mXRefreshView.setVisibility(View.VISIBLE);
            if (mLLNoData != null) {
                mLLNoData.setVisibility(View.GONE);
            }
        }
    }


    private void calculate(List<Object> list, boolean needHeader) {
        mDataList.clear();
        if (list == null || list.size() <= 0) {
            return;
        }
        mDataList = new ArrayList<>();
        if (needHeader) {
            mDataList.add(list.get(0));
        }
        for (int i = 0; i < list.size(); i++) {
            int fir = 0;
            int sec = 0;
            if (needHeader) {
                fir = 2 * i + 1;
                sec = 2 * i + 2;
            } else {
                fir = 2 * i;
                sec = 2 * i + 1;
            }
            List<Object> temp = new ArrayList<>();
            if (fir >= list.size()) {
                return;
            }
            temp.add(list.get(fir));
            if (sec < list.size()) {
                temp.add(list.get(sec));
            }
            mDataList.add(temp);
        }
    }

    public void skipToSearch() {
        ViewPagerHelper mViewPagerHelper = new ViewPagerHelper(mContext);
        mViewPagerHelper.skipToSearch(null, null, SPConstants.FIRST_PAGE_SEARCH);
    }

    public void clearSearchResult() {
        if (mLLNoData != null) {
            mLLNoData.setVisibility(View.GONE);
        }
        mDataList.clear();
        mBigVsList.clear();
        mFirstPageListAdapter.notifyDataSetChanged();
    }
}
