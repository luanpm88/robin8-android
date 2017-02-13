package com.robin8.rb.pager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.andview.refreshview.XRefreshView;
import com.robin8.rb.R;
import com.robin8.rb.activity.LoginActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.BasePager;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.module.first.activity.LaunchRewordFirstActivity;
import com.robin8.rb.module.first.prenster.SearchResultPresenter;
import com.robin8.rb.module.first.view.IFirstPageView;
import com.robin8.rb.ui.widget.RefreshFooterView;
import com.robin8.rb.ui.widget.RefreshHeaderView;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.HelpTools;

/**
 * @author Figo
 */
public class FirstPager extends BasePager implements IFirstPageView, View.OnClickListener {

    private View mPager;
    private WProgressDialog mWProgressDialog;
    private SearchResultPresenter mSearchResultPresenter;
    private XRefreshView mXRefreshView;
    private RecyclerView mRecyclerView;
    private RefreshHeaderView mRefreshHeaderView;
    private RefreshFooterView mRefreshFooterView;

    public FirstPager(FragmentActivity activity) {
        super(activity);
        this.mActivity = activity;
    }

    @Override
    public void initData() {
        if (mPager == null) {
            mLLContent.setBackgroundResource(R.color.white_custom);
            LayoutInflater layoutInflater = LayoutInflater.from(mActivity.getApplicationContext());
            mPager = layoutInflater.inflate(R.layout.pager_first, mLLContent, true);
            mXRefreshView = (XRefreshView) mPager.findViewById(R.id.xrefreshview);
            mRecyclerView = (RecyclerView) mPager.findViewById(R.id.recyclerview);
            mRefreshHeaderView = new RefreshHeaderView(mActivity);
            mRefreshFooterView = new RefreshFooterView(mActivity);
            mWProgressDialog = WProgressDialog.createDialog(mActivity);

            initTitleBar();
            mSearchResultPresenter = new SearchResultPresenter
                    (this, null, null, true,
                            HelpTools.getUrl(CommonConfig.FIRST_KOL_LIST_URL));
            mSearchResultPresenter.init();
        }
    }

    @Override
    public void initTitleBar() {
        mTitleBarText.setText(mActivity.getText(R.string.text_first));
        mTitleBarText.setVisibility(View.VISIBLE);
        firstLeft.setVisibility(View.INVISIBLE);
        firstRight.setVisibility(View.VISIBLE);
        mRewordLaunchIv.setVisibility(View.VISIBLE);
        mRewordLaunchIv.setOnClickListener(this);
        firstRight.setOnClickListener(this);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public XRefreshView getXRefreshView() {
        return mXRefreshView;
    }

    @Override
    public RefreshHeaderView getRefreshHeaderView() {
        return mRefreshHeaderView;
    }

    @Override
    public RefreshFooterView getRefreshFooterView() {
        return mRefreshFooterView;
    }

    @Override
    public WProgressDialog getWProgressDialog() {
        return mWProgressDialog;
    }

    @Override
    public View getLLNoData() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reword_launch_iv:
                skipToLaunch();
                break;
            case R.id.first_right:
                mSearchResultPresenter.skipToSearch();
                break;
        }
    }

    private void skipToLaunch() {
        if (BaseApplication.getInstance().hasLogined()) {
            Intent intent = new Intent(mActivity, LaunchRewordFirstActivity.class);
            mActivity.startActivity(intent);
        } else {
            Intent intent = new Intent(mActivity, LoginActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("from", SPConstants.LAUNCHREWORDACTIVIRY);
            intent.putExtras(bundle);
            mActivity.startActivityForResult(intent, SPConstants.MAIN_TO_LOGIN);
        }
    }
}
