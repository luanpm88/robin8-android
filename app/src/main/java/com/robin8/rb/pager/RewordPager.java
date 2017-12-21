package com.robin8.rb.pager;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.robin8.rb.R;
import com.robin8.rb.activity.ChangeHttpActivity;
import com.robin8.rb.activity.LoginActivity;
import com.robin8.rb.adapter.ImagePagerAdapter;
import com.robin8.rb.adapter.RewordAdapter;
import com.robin8.rb.autoviewpager.AutoScrollViewPager;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.BasePager;
import com.robin8.rb.base.LoadingPage;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.model.BannerBean;
import com.robin8.rb.model.CampaignListBean;
import com.robin8.rb.model.LoginBean;
import com.robin8.rb.model.NotifyMsgEntity;
import com.robin8.rb.module.first.activity.LaunchRewordFirstActivity;
import com.robin8.rb.module.reword.banner.CardPagerAdapter;
import com.robin8.rb.module.reword.banner.ShadowTransformer;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.protocol.RewordProtocol;
import com.robin8.rb.task.LoginTask;
import com.robin8.rb.ui.widget.RefreshListView;
import com.robin8.rb.ui.widget.RewordFilterDialog;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * 活动页面
 *
 * @author Figo
 */
public class RewordPager extends BasePager implements Observer {

    private static final int INIT_DATA = 0;
    private static final int REFRESH = 1;
    private static final int LOAD_MORE = 2;

    private static final String STATE_PENDING = "pending";
    private static final String STATE_APPLYING = "applying";
    private static final String STATE_PASSED = "passed";
    private static final String STATE_REJECTED = "rejected";

    private String[] filterStringArray = {"all", "running", "approved", "verifying", "completed", "missed"};
    private String[] filterString = {"全部", "新任务", "进行中", "待审核", "已完成", "已错失"};
    private List<BannerBean> mBannerList = new ArrayList<BannerBean>();
    private List<CampaignListBean.CampaignInviteEntity> mInviteEntityList = new ArrayList<CampaignListBean.CampaignInviteEntity>();
    private View mPager;
    private RefreshListView mRefreshListView;
    private RewordProtocol mRewordProtocol;
    private int mCurrentFilter = 0;
    private int mCurrentPage = 1;
    private int mCurrentState = INIT_DATA;
    private RewordAdapter mRewordAdapter;
    private LoadingPage mLoadingPage;
    private RewordFilterDialog mRewordFilterDialog;

    private RadioButton rb;
    private LinearLayout mTopPoints;

   // private com.robin8.rb.module.reword.banner.ViewPager mVpAuto;
   private AutoScrollViewPager mVpAuto;
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private int mTotalPages;

    public RewordPager(FragmentActivity activity) {
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
                    if (mInviteEntityList == null || mInviteEntityList.size() == 0) {
                        mLoadingPage.showSafePage(LoadingPage.STATE_LOAD_ERROR);
                    } else {
                        CustomToast.showShort(mActivity.getApplicationContext(), "网络加载失败");
                    }
                }

                @Override
                public void onResponse(String response) {
                    loadData(status);
                }
            }, CommonConfig.TOURIST_PHONE, CommonConfig.TOURIST_CODE, null,null);
        } else {
            loadData(INIT_DATA);
        }
    }

    public void loadData(final int state) {
        mCurrentState = state;
        if (mRewordProtocol == null) {
            mRewordProtocol = new RewordProtocol();
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
                if (mCurrentPage > mTotalPages) {
                    mRefreshListView.setLoadMoreFinished(RefreshListView.LOAD_MORE_NO_DATA);
                    return;
                }
                break;
        }

        String withAnnouncements = null;
        if (state == INIT_DATA) {
            withAnnouncements = "y";
        } else {
            withAnnouncements = "n";
        }
        mRewordProtocol.getRewordTasks(HelpTools.getUrl(CommonConfig.CAMPAIGN_INVITES_URL), filterStringArray[mCurrentFilter], mCurrentPage, withAnnouncements, new RequestCallback() {
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
              //  LogUtil.LogShitou("获取活动列表数据"+HelpTools.getUrl(CommonConfig.CAMPAIGN_INVITES_URL),response);
                CacheUtils.putLong(mActivity.getApplicationContext(), SPConstants.TAG_REFRESH_TIME, System.currentTimeMillis());
                CampaignListBean bean = GsonTools.jsonToBean(response, CampaignListBean.class);

                if (bean != null && !TextUtils.isEmpty(bean.getDetail()) && bean.getDetail().contains("401")) {
                    initLogin(INIT_DATA);
                    return;
                }
                if (bean != null && bean.getError() == 0) {
                    mTotalPages = bean.getTotal_pages();
                    if (mCurrentState != LOAD_MORE) {
                        mInviteEntityList.clear();
                    }

                    List<CampaignListBean.CampaignInviteEntity> campiList = bean.getCampaign_invites();
                    List<CampaignListBean.AnnouncementEntity> announcementsList = bean.getAnnouncements();
                    switch (state) {
                        case INIT_DATA:
                            mLoadingPage.showSafePage(LoadingPage.STATE_LOAD_SUCCESS);
                            initVpData();
                            setVpAuto();
                            break;
                        case REFRESH:
                            mRefreshListView.setRefreshFinshed(true);
                            initVpData();
                            setVpAuto();
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
                        mCurrentPage++;
                        mInviteEntityList.addAll(campiList);
                    }

                    if (mInviteEntityList == null || mInviteEntityList.size() == 0) {
                        mRefreshListView.setLoadMoreFinished(RefreshListView.LOAD_EMPTY);
                    } else if (mInviteEntityList.size() <= 3) {
                        mRefreshListView.setLoadMoreFinished(RefreshListView.LOAD_MORE_NO_DATA);
                    }
                    if (mRewordAdapter != null) {
                        mRewordAdapter.notifyDataSetChanged(mInviteEntityList);
                    }
                }
            }
        });
    }

    public void initVpData() {
        mBannerList.clear();
        BannerBean bean1 = new BannerBean();
        LoginBean loginBean = BaseApplication.getInstance().getLoginBean();
        if (loginBean != null && loginBean.getKol() != null && !TextUtils.isEmpty(loginBean.getKol().getRole_apply_status())) {
            switch (loginBean.getKol().getRole_apply_status()) {
                case STATE_PENDING:
                    bean1.resId = R.mipmap.pic_kol_banner_apply_kol;
                    break;
                case STATE_APPLYING:
                    bean1.resId = R.mipmap.pic_kol_banner_data_reviewing;
                    break;
                case STATE_REJECTED:
                    bean1.resId = R.mipmap.pic_kol_banner_rejected;
                    break;
                case STATE_PASSED:
                    mBannerList.add(new BannerBean(R.mipmap.pic_task_banner_invite_friend, BannerBean.INVITE_FRIEND));
                    mBannerList.add(new BannerBean(R.mipmap.pic_task_banner_indiana, BannerBean.INDIANA));
                    mBannerList.add(new BannerBean(R.mipmap.pic_task_banner_check_in, BannerBean.CHECK_IN));
                    return;
            }
        } else {
            bean1.resId = R.mipmap.pic_kol_banner_apply_kol;
        }
        bean1.type = BannerBean.BE_KOL;
        BannerBean bean2 = new BannerBean();
        bean2.resId = R.mipmap.pic_kol_banner_launch;
        bean2.type = BannerBean.LAUNCH_CAMPAIGN;
        mBannerList.add(bean1);
        mBannerList.add(bean2);
    }
    /**
     * 设置轮播图
     */
    public void setVpAuto() {
        int size = mBannerList.size();
        if (mVpAuto == null) {
            return;
        }
//        mCardAdapter = new CardPagerAdapter(mActivity,mBannerList);
//        mCardShadowTransformer = new ShadowTransformer(mVpAuto, mCardAdapter);
//        mVpAuto.setAdapter(mCardAdapter);
//        mVpAuto.setPageTransformer(false, mCardShadowTransformer);
//        mVpAuto.setOffscreenPageLimit(3);
//        mCardShadowTransformer.setScale(0.27272727f,true);
        mVpAuto.setAdapter(new ImagePagerAdapter(mActivity, mBannerList).setInfiniteLoop(true));
        mVpAuto.setOnPageChangeListener(new MyOnPageChangeListener(size, mTopPoints));
        mVpAuto.setInterval(5000);
        mVpAuto.startAutoScroll();
        mVpAuto.setCurrentItem(1000 * size);
        setPoints(0, size, mTopPoints);
    }

    private void setPoints(int position, int size, LinearLayout topPoints) {
        topPoints.removeAllViews();// 清空以前的点
        for (int i = 0; i < size; i++) {
            View view = new View(mActivity);
            view.setBackgroundResource(R.drawable.tab_dot_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (BaseApplication.mPixelDensityF * 25), (int) (BaseApplication.mPixelDensityF * 7));
            if (i != 0) {
                params.leftMargin = (int) (BaseApplication.mPixelDensityF * 7);
            }
            view.setEnabled(false);// 把所有的点变成灰色
            view.setLayoutParams(params);
            topPoints.addView(view);
        }
        // 一开始把第一个点置为红色
        topPoints.getChildAt(position).setEnabled(true);
    }


    @Override
    public void initTitleBar() {
        mTitleBarText.setText(mActivity.getText(R.string.text_reword));
        mTitleBarText.setVisibility(View.VISIBLE);
        mRewordFilterLl.setVisibility(View.VISIBLE);
       mRewordLaunchIv.setVisibility(View.GONE);
        mTitleBarText.setOnClickListener(new View.OnClickListener() {
            final static int COUNTS = 8;//点击次数
            final static long DURATION = 3 * 1000;//规定有效时间
            long[] mHits = new long[COUNTS];
            @Override
            public void onClick(View view) {

                        /**
                         * 实现双击方法
                         * src 拷贝的源数组
                         * srcPos 从源数组的那个位置开始拷贝.
                         * dst 目标数组
                         * dstPos 从目标数组的那个位子开始写数据
                         * length 拷贝的元素的个数
                         */
                        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                        //实现左移，然后最后一个位置更新距离开机的时间，如果最后一个时间和最开始时间小于DURATION，即连续5次点击
                        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                        if (mHits[0] >= (SystemClock.uptimeMillis() - DURATION)) {
                            //  String tips = "您已在[" + DURATION + "]ms内连续点击【" + mHits.length + "】次了！！！";
                            //  Toast.makeText(SettingActivity.this, tips, Toast.LENGTH_SHORT).show();
                           mActivity.startActivity(new Intent(mActivity, ChangeHttpActivity.class));
                        }
            }
        });
       // mRewordLaunchIv.setVisibility(View.VISIBLE);
    }

    public void addView() {
        mRefreshListView = new RefreshListView(mActivity.getApplicationContext());
        mRefreshListView.setHeaderDividersEnabled(false);
        mRefreshListView.setFooterDividersEnabled(false);
        mRefreshListView.setFadingEdgeLength(10);
        mRefreshListView.setDividerHeight(10);

        View header = View.inflate(mActivity, R.layout.reword_header, null);
        mVpAuto = (AutoScrollViewPager) header.findViewById(R.id.vp_auto);
        mVpAuto.post(new Runnable() {
            @Override
            public void run() {
                mVpAuto.getLayoutParams().height = DensityUtils.getScreenWidth(mVpAuto.getContext()) * 2 / 5;
            }
        });
        mTopPoints = (LinearLayout) header.findViewById(R.id.ll_vp_points);
        mRefreshListView.addSecondHeader(header);

        mRewordAdapter = new RewordAdapter(mActivity, mInviteEntityList);
        mRefreshListView.setAdapter(mRewordAdapter);
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

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mLLContent.addView(mLoadingPage, layoutParams);
    }


    /**
     * 设置控件点击事件
     */
    private void initViewSettings() {

        mRewordLaunchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        });

        mRewordFilterLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        mRewordFilterDialog = new RewordFilterDialog(mActivity, R.layout.dialog_reword_filter);
        //设置dialog高度
        mRewordFilterDialog.dg.getWindow().setLayout(DensityUtils.dp2px(300), WindowManager.LayoutParams.WRAP_CONTENT);
        View view = mRewordFilterDialog.getView();
        final RadioGroup rg = (RadioGroup) view.findViewById(R.id.rg);
        ArrayList<RadioButton> radioButtons = new ArrayList<>();
        RadioButton rb0 = (RadioButton) view.findViewById(R.id.rb0);
        RadioButton rb1 = (RadioButton) view.findViewById(R.id.rb1);
        RadioButton rb2 = (RadioButton) view.findViewById(R.id.rb2);
        RadioButton rb3 = (RadioButton) view.findViewById(R.id.rb3);
        RadioButton rb4 = (RadioButton) view.findViewById(R.id.rb4);
        RadioButton rb5 = (RadioButton) view.findViewById(R.id.rb5);
        radioButtons.add(rb0);
        radioButtons.add(rb1);
        radioButtons.add(rb2);
        radioButtons.add(rb3);
        radioButtons.add(rb4);
        radioButtons.add(rb5);
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
        rb4.setOnClickListener(onClickListener);
        rb5.setOnClickListener(onClickListener);
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
                    case R.id.rb4:
                        if (mCurrentFilter == 4)
                            clickItSelf = true;
                        mCurrentFilter = 4;
                        break;
                    case R.id.rb5:
                        if (mCurrentFilter == 5)
                            clickItSelf = true;
                        mCurrentFilter = 5;
                        break;
                    default:
                        break;
                }
                if (!clickItSelf) {
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
                    mInviteEntityList.clear();
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

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        private int size;
        private LinearLayout linearLayout;

        MyOnPageChangeListener(int size, LinearLayout linearLayout) {
            this.size = size;
            this.linearLayout = linearLayout;
        }

        @Override
        public void onPageSelected(int position) {
            setPoints(position % size, size, linearLayout);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}
