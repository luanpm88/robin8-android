package com.robin8.rb.ui.pager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.R;
import com.robin8.rb.ui.activity.LoginActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.BasePager;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.helper.IconFontHelper;
import com.robin8.rb.ui.indiana.protocol.IProtocol;
import com.robin8.rb.ui.module.mine.protocol.MessageProtocol;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.ui.widget.RefreshFooterView;
import com.robin8.rb.ui.widget.RefreshHeaderView;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.ui.dialog.CustomDialogManager;

/**
 * 通知界面
 * Created by seven on 25/03/2017.
 */

public class NotificationPager extends BasePager implements View.OnClickListener {
    public final static int INIT_DATA = 0;
    public final static int DRAG_REFRESH = 1;
    public final static int LOAD_MORE = 2;
    private static final String STATE_PENDING = "pending";  //pending,applying,passed,rejected
    private static final String STATE_APPLYING = "applying";    //pending,applying,passed,rejected
    private static final String STATE_PASSED = "passed";    //pending,applying,passed,rejected
    private static final String STATE_REJECTED = "rejected";    //pending,applying,passed,rejected
    private int mCurrentState = INIT_DATA;
    private View mPager;
    private WProgressDialog mWProgressDialog;
    private XRefreshView mXRefreshView;
    private RecyclerView mRecyclerView;
    private RefreshHeaderView mRefreshHeaderView;
    private RefreshFooterView mRefreshFooterView;
    private LinearLayout mErrorViewLL;
    private TextView mRightTV;
    private IProtocol mIProtocol;
    private BaseRecyclerAdapter mAdapter;
    private View mLoginedView;
    private View mUnLoginedView;
    private TextView mLoginTV;

    public NotificationPager(FragmentActivity activity) {
        super(activity);
        this.mActivity = activity;
    }


    @Override
    public View initView() {
        FrameLayout frameLayout = new FrameLayout(mActivity);
        mPager = (LinearLayout) View.inflate(mActivity, R.layout.pager_notification, null);
        frameLayout.addView(mPager, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mLoginedView = mPager.findViewById(R.id.fl_logined_view);
        mUnLoginedView = mPager.findViewById(R.id.ll_unlogined_view);
        mLoginTV = (TextView) mPager.findViewById(R.id.tv_login);
        mXRefreshView = (XRefreshView) mPager.findViewById(R.id.xrefreshview);
        mRecyclerView = (RecyclerView) mPager.findViewById(R.id.recyclerview);
        mRefreshHeaderView = new RefreshHeaderView(mActivity);
        mRefreshFooterView = new RefreshFooterView(mActivity);
        mErrorViewLL = (LinearLayout) mPager.findViewById(R.id.ll_error);
        mErrorViewLL.setOnClickListener(this);
        mRightTV = (TextView) mPager.findViewById(R.id.tv_right);
        IconFontHelper.setTextIconFont(mRightTV, R.string.icons_message_open);
        mRightTV.setOnClickListener(this);
        mLoginTV.setOnClickListener(this);
        initXRefreshView();
        initRecyclerView();
        return frameLayout;
    }

    private void initXRefreshView() {

        mXRefreshView.setPullLoadEnable(true);
        mXRefreshView.setSlienceLoadMore();
        mXRefreshView.setAutoLoadMore(true);
        mRefreshHeaderView = new RefreshHeaderView(mActivity);
        mXRefreshView.setCustomHeaderView(mRefreshHeaderView);
        mRefreshHeaderView.findViewById(R.id.ll_setting).getLayoutParams().width = DensityUtils.getScreenWidth(mActivity.getApplicationContext());
        mXRefreshView.setPullLoadEnable(true);
        mXRefreshView.setSlienceLoadMore();
        mXRefreshView.setAutoLoadMore(true);

        mRefreshFooterView = new RefreshFooterView(mActivity);
        mXRefreshView.setCustomFooterView(mRefreshFooterView);

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
        mIProtocol = new MessageProtocol(mActivity);

        mAdapter = mIProtocol.getAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadData() {
        if (mCurrentState != LOAD_MORE) {
            mIProtocol.setCurrentPage(1);
        }
        HttpRequest.getInstance().get(true, HelpTools.getUrl(CommonConfig.MESSAGES_URL), mIProtocol.getRequestParams(), new RequestCallback() {
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

    private boolean isLogined() {
        BaseApplication baseApplication = BaseApplication.getInstance();
        return baseApplication.hasLogined();
    }

    /**
     * 判断身份信息,显示不同的view
     */
    public void changeVisibleView() {
        if (isLogined()) {
            mLoginedView.setVisibility(View.VISIBLE);
            mUnLoginedView.setVisibility(View.GONE);
        } else {
            // 未登录
            mLoginedView.setVisibility(View.GONE);
            mUnLoginedView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initData() {
        mErrorViewLL.setVisibility(View.GONE);
        changeVisibleView();
        if (!isLogined()) {
            return;
        }
        mCurrentState = DRAG_REFRESH;
        mXRefreshView.startRefresh();
    }


    /**
     * 消息设置已读弹窗
     */
    private void showMessageDiaog() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_confirm, null);
        TextView confirmTV = (TextView) view.findViewById(R.id.tv_confirm);
        TextView cancelTV = (TextView) view.findViewById(R.id.tv_cancel);
        final CustomDialogManager cdm = new CustomDialogManager(mActivity, view);
        confirmTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readAllMessage();
                cdm.dismiss();
            }
        });
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cdm.dismiss();
            }
        });

        cdm.dg.setCanceledOnTouchOutside(true);
        cdm.dg.getWindow().setGravity(Gravity.CENTER);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        cdm.showDialog();
    }

    private void readAllMessage() {

        HttpRequest.getInstance().put(true,HelpTools.getUrl(CommonConfig.READ_ALL_MESSAGES_URL), null, new RequestCallback() {
            @Override
            public void onError(Exception e) {
                mIProtocol.notifyData();
                mAdapter.notifyDataSetChanged();
               // ((MainActivity)mActivity).hideNotificationRedDot(true);
            }

            @Override
            public void onResponse(String response) {
                mXRefreshView.startRefresh();
                LogUtil.logXXfigo("READ_ALL_MESSAGES_URL  " + response);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                showMessageDiaog();
            break;
            case R.id.ll_error:
                initData();
                break;
            case R.id.tv_login:
                Intent intent = new Intent(mActivity, LoginActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("from", SPConstants.MAINACTIVITY);
                intent.putExtras(bundle);
                mActivity.startActivityForResult(intent, SPConstants.MAIN_TO_LOGIN);
                break;
            default:
                break;
        }

    }
}
