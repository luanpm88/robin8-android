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
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.module.create.activity.EditCreateActivity;
import com.robin8.rb.module.create.activity.FragmentsActivity;
import com.robin8.rb.module.create.prenster.CreateFirstPresenter;
import com.robin8.rb.module.create.view.ICreateFirstView;
import com.robin8.rb.ui.widget.RefreshFooterView;
import com.robin8.rb.ui.widget.RefreshHeaderView;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.HelpTools;

/**
 * @author Figo
 */
public class CreatePager extends BasePager implements ICreateFirstView, View.OnClickListener {

    private View mPager;
    private WProgressDialog mWProgressDialog;
    private CreateFirstPresenter mCreateFirstPresenter;
    private XRefreshView mXRefreshView;
    private RecyclerView mRecyclerView;
    private RefreshHeaderView mRefreshHeaderView;
    private RefreshFooterView mRefreshFooterView;

    public CreatePager(FragmentActivity activity) {
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
            mCreateFirstPresenter = new CreateFirstPresenter(this, HelpTools.getUrl(CommonConfig.CPS_ARTICLES_URL), mActivity);
            mCreateFirstPresenter.init();
        }
    }

    @Override
    public void initTitleBar() {
        mTitleBarText.setText(R.string.text_create);
        mTitleBarText.setVisibility(View.VISIBLE);
        firstLeft.setVisibility(View.INVISIBLE);
        firstRight.setVisibility(View.VISIBLE);
        mRewordLaunchIv.setVisibility(View.VISIBLE);
        firstRight.setImageResource(R.mipmap.icon_ark_cps);
        mRewordLaunchIv.setImageResource(R.mipmap.icon_ark_write);
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
                skipToCreate();
                break;
            case R.id.first_right:
                skipToMyCreate();
                break;
        }
    }

    private void skipToMyCreate() {
        if (BaseApplication.getInstance().hasLogined()) {
            String nameArr[] = {"我的分享", "我的创作", "待审核", "审核拒绝"};//待审核、审核通过、审核拒绝, 我的分享
            String campaignTypeArr[] = {"shares", "passed", "pending", "rejected"};//'pending' , 'passed','rejected', 'shares'
            Intent intent = new Intent(mActivity, FragmentsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArray("name", nameArr);
            bundle.putStringArray("type", campaignTypeArr);
            bundle.putString("page_name", StatisticsAgency.MY_CREATE);
            bundle.putString("title_name", mActivity.getString(R.string.my_create));
            bundle.putString("url", HelpTools.getUrl(CommonConfig.MY_CREATE_URL));
            intent.putExtras(bundle);
            mActivity.startActivity(intent);
        } else {
            login();
        }
    }

    private void skipToCreate() {
        if (BaseApplication.getInstance().hasLogined()) {
            Intent intent = new Intent(mActivity, EditCreateActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("from", SPConstants.LAUNCHREWORDACTIVIRY);
            intent.putExtras(bundle);
            mActivity.startActivityForResult(intent, SPConstants.MAIN_TO_LOGIN);
        } else {
            login();
        }
    }

    private void login() {
        Intent intent = new Intent(mActivity, LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("from", SPConstants.MAINACTIVITY);
        intent.putExtras(bundle);
        mActivity.startActivityForResult(intent, SPConstants.MAIN_TO_LOGIN);
    }
}
