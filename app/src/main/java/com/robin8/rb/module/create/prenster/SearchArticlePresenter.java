package com.robin8.rb.module.create.prenster;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.andview.refreshview.XRefreshView;
import com.robin8.rb.R;
import com.robin8.rb.activity.WebViewActivity;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.module.create.adapter.SearchArticleAdapter;
import com.robin8.rb.module.create.model.ArticleListsModel;
import com.robin8.rb.module.first.helper.ViewPagerHelper;
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
public class SearchArticlePresenter implements PresenterI {

    private final static int INIT_DATA = 0;
    private final static int DRAG_REFRESH = 1;
    public final static int LOAD_MORE = 2;
    private int mCurrentState = INIT_DATA;

    private XRefreshView mXRefreshView;
    private RefreshHeaderView mRefreshHeaderView;
    private RefreshFooterView mRefreshFooterView;
    private RecyclerView mRecyclerView;
    private IFirstPageView mIFirstPageView;
    private Context mContext;
    private String mTitle;
    private WProgressDialog mWProgressDialog;
    private RequestParams mRequestParams;
    private int mCurrentPage = 1;
    private int mTotalPages;
    private LinearLayoutManager mLinearLayoutManager;
    private SearchArticleAdapter mSearchArticleAdapter;
    private List<Object> mDataList = new ArrayList<>();
    private String mUrl;
    private View mLLNoData;

    public SearchArticlePresenter(IFirstPageView view, String url) {
        mIFirstPageView = view;
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

    public void searchKol(String title) {
        mTitle = title;
        mCurrentState = INIT_DATA;
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
        mSearchArticleAdapter = new SearchArticleAdapter(mDataList, mContext);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mSearchArticleAdapter);
        mSearchArticleAdapter.setOnRecyclerViewListener(new SearchArticleAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                skipToDetail(position);
            }
        });
    }

    private void skipToDetail(int position) {
        if (position < 0 || mDataList == null || mDataList.size() == 0 || position >= mDataList.size()) {
            return;
        }
        Object obj = mDataList.get(position);
        if (obj != null && obj instanceof ArticleListsModel.ArticlesBean) {
            ArticleListsModel.ArticlesBean bean = (ArticleListsModel.ArticlesBean) obj;
            Intent intent = new Intent(mContext, WebViewActivity.class);
            intent.putExtra("from", SPConstants.ARTICLE_LIST);
            intent.putExtra("title", "详情");
            intent.putExtra("url", bean.getArticle_url());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    @Override
    public void init() {

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

        mRequestParams.put("page", mCurrentPage);
        mRequestParams.put("title", mTitle);

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
        ArticleListsModel articleListsModel = GsonTools.jsonToBean(response, ArticleListsModel.class);
        if (articleListsModel != null && articleListsModel.getError() == 0) {
            if (mCurrentState != LOAD_MORE) {
                mDataList.clear();
            }
            mTotalPages = articleListsModel.getTotal_pages();
            List<ArticleListsModel.ArticlesBean> tempList = articleListsModel.getArticles();
            if (tempList != null && tempList.size() > 0) {
                mDataList.addAll(tempList);
                mSearchArticleAdapter.setDataList(mDataList);
                mSearchArticleAdapter.notifyDataSetChanged();
                mCurrentPage++;
                return;
            }
        }

        if (mCurrentState != LOAD_MORE && mDataList.size() == 0) {
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

    public void skipToSearch() {
        ViewPagerHelper mViewPagerHelper = new ViewPagerHelper(mContext);
        mViewPagerHelper.skipToSearch(null, null, SPConstants.FIRST_PAGE_SEARCH);
    }

    public void clearSearchResult() {
        if (mLLNoData != null) {
            mLLNoData.setVisibility(View.GONE);
        }
        mDataList.clear();
        mSearchArticleAdapter.notifyDataSetChanged();
    }
}
