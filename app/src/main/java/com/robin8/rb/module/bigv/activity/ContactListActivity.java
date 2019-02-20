package com.robin8.rb.module.bigv.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.LoadingPage;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.module.bigv.adapter.ContactAdapter;
import com.robin8.rb.module.bigv.model.ContactListModel;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.protocol.ContactProtocol;
import com.robin8.rb.task.LoginTask;
import com.robin8.rb.ui.widget.RefreshListView;
import com.robin8.rb.util.CustomToast;

import java.util.List;

public class ContactListActivity extends BaseActivity {
    private LinearLayout layoutList;
    private static final int INIT_DATA = 0;
    private static final int REFRESH = 1;
    private static final int LOAD_MORE = 2;
    private RefreshListView mRefreshListView;
    private LoadingPage mLoadingPage;
    private List<ContactListModel> listModels;
    private ContactAdapter mAdapter;
    private int mCurrentFilter = 0;
    private int mCurrentPage = 1;
    private int mCurrentState = INIT_DATA;
    private ContactProtocol mContactProtocol;

    private boolean isLoad = false;

    @Override
    public void setTitleView() {
        mLLTitleBar.setVisibility(View.GONE);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_contact_list, mLLContent, true);
        layoutList = ((LinearLayout) view.findViewById(R.id.layout_list));
        initData();
    }

    private void initData() {
        mRefreshListView = new RefreshListView(this);
        mRefreshListView.setHeaderDividersEnabled(false);
        mRefreshListView.setFooterDividersEnabled(false);
        mRefreshListView.setFadingEdgeLength(10);
        mRefreshListView.setDividerHeight(10);
        mAdapter = new ContactAdapter(this, listModels);
        mRefreshListView.setAdapter(mAdapter);
        mRefreshListView.setOnRefreshListener(new MyOnRefreshListener());

        mLoadingPage = new LoadingPage(this) {

            @Override
            public void onLoad() {
                initLoginData(INIT_DATA);
            }

            @Override
            public View onCreateSuccessedView() {
                return mRefreshListView;
            }
        };

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutList.addView(mLoadingPage, layoutParams);

    }

    /**
     判断登陆
     @param status 当前加载状态
     */
    private void initLoginData(final int status) {

        mLoadingPage.showSafePage(LoadingPage.STATE_LOADING);
        if (! BaseApplication.getInstance().hasLogined()) {
            LoginTask loginTask = LoginTask.newInstance(this);
            loginTask.start(new RequestCallback() {

                @Override
                public void onError(Exception e) {
                    if (listModels == null || listModels.size() == 0) {
                        mLoadingPage.showSafePage(LoadingPage.STATE_LOAD_ERROR);
                    } else {
                        CustomToast.showShort(ContactListActivity.this, "网络加载失败");
                    }
                }

                @Override
                public void onResponse(String response) {
                    loadData(status);
                }
            }, CommonConfig.TOURIST_PHONE, CommonConfig.TOURIST_CODE, null, null);
        } else {
            loadData(INIT_DATA);
        }

    }

    /**
     数据加载
     @param state
     */
    private void loadData(int state) {
        mCurrentState = state;
        if (mContactProtocol == null) {
            mContactProtocol = new ContactProtocol();
        }

        switch (state) {
            case INIT_DATA:
            case REFRESH:
                mCurrentPage = 1;
                if (mRefreshListView != null) {
                    mRefreshListView.setIsLoadMore(false);
                }
                break;
            case LOAD_MORE:
                if (isLoad) {
                    mRefreshListView.setLoadMoreFinished(RefreshListView.LOAD_MORE_NO_DATA);
                    return;
                }
                break;
        }
    }

    public class MyOnRefreshListener implements RefreshListView.OnRefreshListener {

        @Override
        public void onRefreshing() {
            //  loadData(REFRESH);
        }

        @Override
        public void onLoadingMore() {
            // loadData(LOAD_MORE);
        }
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }
}
