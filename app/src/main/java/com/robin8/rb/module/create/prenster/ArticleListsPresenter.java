package com.robin8.rb.module.create.prenster;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.R;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.helper.IconFontHelper;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.indiana.protocol.IProtocol;
import com.robin8.rb.model.NotifyMsgEntity;
import com.robin8.rb.module.create.protocol.ArticleListsProtocol;
import com.robin8.rb.module.first.activity.SearchKolActivity;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.presenter.PresenterI;
import com.robin8.rb.ui.widget.RefreshFooterView;
import com.robin8.rb.ui.widget.RefreshHeaderView;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.util.UIUtils;
import com.robin8.rb.view.IRobinIndianaView;

import java.util.Observable;
import java.util.Observer;


/**
 * @author Figo
 */
public class ArticleListsPresenter extends BasePresenter implements PresenterI, Observer {

    public final static int INIT_DATA = 0;
    public final static int DRAG_REFRESH = 1;
    public final static int LOAD_MORE = 2;
    private int mCurrentState = INIT_DATA;
    private WProgressDialog mWProgressDialog;
    private RecyclerView mRecyclerView;
    private BaseRecyclerAdapter mAdapter;
    private XRefreshView mXRefreshView;
    private LinearLayoutManager mLinearLayoutManager;
    private RequestParams mRequestParams;
    private RefreshHeaderView mRefreshHeaderView;
    private RefreshFooterView mRefreshFooterView;
    private String url;
    private String title;

    private final IRobinIndianaView mIUserView;
    private Activity mActivity;
    private IProtocol mIProtocol;
    private int destination;

    public ArticleListsPresenter(Activity activity, IRobinIndianaView userView) {
        mActivity = activity;
        mIUserView = userView;
    }

    public void start() {
        initData();
        initXRefreshView();
        initRecyclerView();
        getDataFromNet();
    }

    private void initData() {
        Intent intent = mActivity.getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        destination = intent.getIntExtra("destination", -1);
        mXRefreshView = mIUserView.getXRefreshView();
        mRecyclerView = mIUserView.getRecyclerView();
        mIUserView.setTitleView(title);
        mIProtocol = new ArticleListsProtocol(mActivity);
        ((ArticleListsProtocol)mIProtocol).setOnItemClickListener(new ArticleListsProtocol.OnItemClickListener() {
            @Override
            public void onItemClick() {
                //刷新
                mCurrentState = DRAG_REFRESH;
                mXRefreshView.startRefresh();
                getDataFromNet();
            }
        });
        mIUserView.getRightTv().setTextSize(22);
        mIUserView.getRightTv().setTextColor(UIUtils.getColor(R.color.sub_black_custom));
        IconFontHelper.setTextIconFont(mIUserView.getRightTv(),R.string.search_sign);
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
            case HttpRequest.PUT:
                HttpRequest.getInstance().put(needHeader, url, params, callback);
                break;
        }
    }

    private void initXRefreshView() {

        mXRefreshView.setPullLoadEnable(true);
        mXRefreshView.setSlienceLoadMore();
        mXRefreshView.setAutoLoadMore(true);

        mRefreshHeaderView = new RefreshHeaderView(mActivity);
        mXRefreshView.setCustomHeaderView(mRefreshHeaderView);
        mXRefreshView.post(new Runnable() {
            @Override
            public void run() {
                mRefreshHeaderView.findViewById(R.id.ll_setting).getLayoutParams().width = DensityUtils.getScreenWidth(mActivity.getApplicationContext());
            }
        });

        mXRefreshView.setPullLoadEnable(true);
        mXRefreshView.setSlienceLoadMore();
        mXRefreshView.setAutoLoadMore(true);

        mRefreshFooterView = new RefreshFooterView(mActivity);
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
    }

    private void initRecyclerView() {
        mAdapter = mIProtocol.getAdapter();
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 加载网络数据
     */
    private void getDataFromNet() {

        if (mCurrentState != LOAD_MORE) {
            mIProtocol.setCurrentPage(1);
        }

        mRequestParams = mIProtocol.getRequestParams();
        if (mRequestParams == null) {
            mXRefreshView.stopLoadMore(true);
            return;
        } else {
            mXRefreshView.setSlienceLoadMore();
            mXRefreshView.setAutoLoadMore(true);
        }

        if (mCurrentState == INIT_DATA) {
            if (mWProgressDialog == null) {
                mWProgressDialog = WProgressDialog.createDialog(mActivity);
            }
            if (!mWProgressDialog.isShowing()) {
                mWProgressDialog.show();
            }
        }

        getDataFromServer(true, HttpRequest.GET, url, mRequestParams, new RequestCallback() {
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
                mIProtocol.parseJson(response, mCurrentState);
            }
        });
    }


    public void finish() {
        if (mWProgressDialog != null) {
            mWProgressDialog.dismiss();
            mWProgressDialog = null;
        }

        NotifyManager.getNotifyManager().deleteObserver(this);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof NotifyMsgEntity) {
            NotifyMsgEntity msgEntity = (NotifyMsgEntity) data;
            if (msgEntity.getCode() == NotifyManager.TYPE_INDIANA_PAY_SUCCESSFUL) {
                mCurrentState = DRAG_REFRESH;
                getDataFromNet();
            }
        }
    }

    /**
     * 文章搜索
     */
    public void OnclickRightView() {
        Intent intent = new Intent(mActivity, SearchKolActivity.class);
        intent.putExtra("from", SPConstants.ARTICLE_SEARCH);
        intent.putExtra("url", CommonConfig.ARTICLES_SEARCH_URL);
        mActivity.startActivity(intent);
    }
}
