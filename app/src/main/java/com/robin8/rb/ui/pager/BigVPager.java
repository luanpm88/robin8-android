package com.robin8.rb.ui.pager;

import android.support.v4.app.FragmentActivity;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.robin8.rb.R;
import com.robin8.rb.ui.adapter.BigvListAdapter;
import com.robin8.rb.ui.widget.autoviewpager.AutoScrollViewPager;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.BasePager;
import com.robin8.rb.base.LoadingPage;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.ui.model.BigvListModel;
import com.robin8.rb.ui.model.NotifyMsgEntity;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.protocol.BigvProtocol;
import com.robin8.rb.task.LoginTask;
import com.robin8.rb.ui.widget.RefreshListView;
import com.robin8.rb.ui.widget.RewordFilterDialog;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 大V接活动页面
 Created by zc on 2019/1/7. */

public class BigVPager extends BasePager implements Observer {
    private static final int INIT_DATA = 0;
    private static final int REFRESH = 1;
    private static final int LOAD_MORE = 2;

//    private static final String STATE_PENDING = "pending";
//    private static final String STATE_APPLYING = "applying";
//    private static final String STATE_PASSED = "passed";
//    private static final String STATE_REJECTED = "rejected";

    private String[] filterStringArray = {"all", "passed", "finished", "ended"};
    private String[] filterString = {"全部","进行中","已完成","已结束"};
    private View mPager;
    private RefreshListView mRefreshListView;
    private BigvProtocol mBigvProtocol;
    private int mCurrentFilter = 0;
    private int mCurrentPage = 1;
    private int mCurrentState = INIT_DATA;

    private BigvListAdapter mAdapter;
    private List<BigvListModel.ListBean> listModels = new ArrayList<>();

    private LoadingPage mLoadingPage;
    private RewordFilterDialog mRewordFilterDialog;

    private RadioButton rb;
    private LinearLayout mTopPoints;

    private AutoScrollViewPager mVpAuto;
    //private int mTotalPages=100;
    private boolean isLoad=false;
    private static final String STATE_ALL = "all";//全部
    private static final String STATE_PASSED = "passed";//进行中
    private static final String STATE_ENDED = "ended";//已结束
    private static final String STATE_FINISHED = "finished";//已完成


    public BigVPager(FragmentActivity activity) {
        super(activity);
        this.mActivity = activity;
        NotifyManager.getNotifyManager().addObserver(this);
    }

    @Override
    public void initData() {
        if (mPager == null) {
            addView();
            initLogin(INIT_DATA);
            initTitleBar();
            initViewSettings();
        }
    }


    private void initLogin(final int status) {

        mLoadingPage.showSafePage(LoadingPage.STATE_LOADING);
        if (!BaseApplication.getInstance().hasLogined()) {
            LoginTask loginTask = LoginTask.newInstance(mActivity.getApplicationContext());
            loginTask.start(new RequestCallback() {

                @Override
                public void onError(Exception e) {
                    if (listModels == null || listModels.size() == 0) {
                        mLoadingPage.showSafePage(LoadingPage.STATE_LOAD_ERROR);
                    } else {
                        CustomToast.showShort(mActivity.getApplicationContext(), "网络加载失败");
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

    public void loadData(final int state) {
        mCurrentState = state;
        if (mBigvProtocol == null) {
            mBigvProtocol = new BigvProtocol();
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

        mBigvProtocol.getBigvTasks(HelpTools.getUrl(CommonConfig.BIGV_LIST_URL), filterStringArray[mCurrentFilter], mCurrentPage, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                switch (state) {
                    case INIT_DATA:
                        mLoadingPage.showSafePage(LoadingPage.STATE_LOAD_ERROR);
                        break;
                    case REFRESH:
                        mRefreshListView.setRefreshFinshed(false);
                        break;
                    case LOAD_MORE:
                        mRefreshListView.setLoadMoreFinished(RefreshListView.LOAD_MORE_FAIL);
                        break;
                }
            }

            @Override
            public void onResponse(String response) {
                LogUtil.LogShitou("获取大v列表数据" + HelpTools.getUrl(CommonConfig.BIGV_LIST_URL), response);
                CacheUtils.putLong(mActivity.getApplicationContext(), SPConstants.TAG_REFRESH_TIME, System.currentTimeMillis());
                BigvListModel bean = GsonTools.jsonToBean(response, BigvListModel.class);

                if (bean != null) {
                    if (! TextUtils.isEmpty(bean.getDetail())) {
                        if (bean.getDetail().contains("401")) {
                            CustomToast.showShort(mActivity, "登陆失效，请重新登陆");
                            return;
                        }
                    }
                }
                if (bean != null && ! TextUtils.isEmpty(bean.getDetail()) && bean.getDetail().contains("401")) {
                    initLogin(INIT_DATA);
                    return;
                }
                if (bean != null && bean.getError() == 0) {
                    if (mCurrentState != LOAD_MORE) {
                        listModels.clear();
                    }

                    List<BigvListModel.ListBean> campiList = bean.getList();
                    switch (state) {
                        case INIT_DATA:
                            mLoadingPage.showSafePage(LoadingPage.STATE_LOAD_SUCCESS);
                            break;
                        case REFRESH:
                            mRefreshListView.setRefreshFinshed(true);
                            break;
                        case LOAD_MORE:
                            if (campiList == null || campiList.size() <= 3) {
                                mRefreshListView.setLoadMoreFinished(RefreshListView.LOAD_MORE_NO_DATA);
                            } else {
                                mRefreshListView.setLoadMoreFinished(RefreshListView.LOAD_MORE_SUCCESS);
                            }
                            break;
                    }

                    if (campiList != null && campiList.size() >= 0) {
                        isLoad=false;
                        mCurrentPage++;
                        listModels.addAll(campiList);
                    }else {
                        isLoad=true;
                    }

                    if (listModels == null || listModels.size() == 0) {
                        mRefreshListView.setLoadMoreFinished(RefreshListView.LOAD_EMPTY);
                    } else if (listModels.size() <= 3) {
                        mRefreshListView.setLoadMoreFinished(RefreshListView.LOAD_MORE_NO_DATA);
                    }
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged(listModels);
                    }
                }

            }
        });
    }


    @Override
    public void initTitleBar() {

        llNormalTitle.setVisibility(View.GONE);
        llCardTitle.setVisibility(View.VISIBLE);
        mBigvFilter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    public void addView() {
        mRefreshListView = new RefreshListView(mActivity.getApplicationContext());
        mRefreshListView.setHeaderDividersEnabled(false);
        mRefreshListView.setFooterDividersEnabled(false);
        mRefreshListView.setFadingEdgeLength(10);
        mRefreshListView.setDividerHeight(15);

        mAdapter = new BigvListAdapter(mActivity, listModels);
        mRefreshListView.setAdapter(mAdapter);
        mRefreshListView.setOnRefreshListener(new MyOnRefreshListener());

        mLoadingPage = new LoadingPage(mActivity.getApplicationContext()) {

            @Override
            public void onLoad() {
                initLogin(INIT_DATA);
            }

            @Override
            public View onCreateSuccessedView() {
                return mRefreshListView;
            }
        };

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mLLContent.addView(mLoadingPage, layoutParams);
    }


    /**
     设置控件点击事件
     */
    private void initViewSettings() {

        mRewordFilterLl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        mRewordFilterDialog = new RewordFilterDialog(mActivity, R.layout.dialog_bigv_filter);
        //设置dialog高度
        mRewordFilterDialog.dg.getWindow().setLayout(DensityUtils.dp2px(300), WindowManager.LayoutParams.WRAP_CONTENT);
        View view = mRewordFilterDialog.getView();
        final RadioGroup rg = (RadioGroup) view.findViewById(R.id.rg);
        ArrayList<RadioButton> radioButtons = new ArrayList<>();
        RadioButton rb0 = (RadioButton) view.findViewById(R.id.rb0);
        RadioButton rb1 = (RadioButton) view.findViewById(R.id.rb1);
        RadioButton rb2 = (RadioButton) view.findViewById(R.id.rb2);
        RadioButton rb3 = (RadioButton) view.findViewById(R.id.rb3);

        radioButtons.add(rb0);
        radioButtons.add(rb1);
        radioButtons.add(rb2);
        radioButtons.add(rb3);

        View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mRewordFilterDialog.dismiss();
            }
        };
        rb0.setOnClickListener(onClickListener);
        rb1.setOnClickListener(onClickListener);
        rb2.setOnClickListener(onClickListener);
        rb3.setOnClickListener(onClickListener);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rb != null) {
                    rb = (RadioButton) rg.findViewById(checkedId);
                    TextPaint tp = rb.getPaint();
                    tp.setFakeBoldText(false);
                }
                rb = (RadioButton) rg.findViewById(checkedId);
                TextPaint tp = rb.getPaint();
                tp.setFakeBoldText(true);
                boolean clickItSelf = false;
                switch (checkedId) {
                    case R.id.rb0:
                        if (mCurrentFilter == 0)
                            clickItSelf = true;
                        mCurrentFilter = 0;
                        break;
                    case R.id.rb1:
                        if (mCurrentFilter == 1)
                            clickItSelf = true;
                        mCurrentFilter = 1;
                        break;
                    case R.id.rb2:
                        if (mCurrentFilter == 2)
                            clickItSelf = true;
                        mCurrentFilter = 2;
                        break;
                    case R.id.rb3:
                        if (mCurrentFilter == 3)
                            clickItSelf = true;
                        mCurrentFilter = 3;
                        break;
                    default:
                        break;
                }
                if (! clickItSelf) {
                    loadData(REFRESH);
                }
            }
        });
        rg.check(radioButtons.get(mCurrentFilter).getId());
        mRewordFilterDialog.showDialog();
    }

    @Override
    public void update(Observable observable, Object data) {

        if (data instanceof NotifyMsgEntity) {
            NotifyMsgEntity msgEntity = (NotifyMsgEntity) data;
            int type = msgEntity.getCode();
            mCurrentPage = 1;
            switch (type) {
                case NotifyManager.TYPE_SHARE_SUCCESS:
                case NotifyManager.TYPE_REFRESH_PROFILE_REWORD_PAGE:
                case NotifyManager.TYPE_LOGIN:
                    loadData(REFRESH);
                    break;
                case NotifyManager.TYPE_LOGIN_OUT:
                    listModels.clear();
                    initLogin(INIT_DATA);
                    break;
            }
        }
    }

    public class MyOnRefreshListener implements RefreshListView.OnRefreshListener {

        @Override
        public void onRefreshing() {
            loadData(REFRESH);
        }

        @Override
        public void onLoadingMore() {
            loadData(LOAD_MORE);
        }
    }

}