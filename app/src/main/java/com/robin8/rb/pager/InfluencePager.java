package com.robin8.rb.pager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.BasePager;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.model.BaseBean;
import com.robin8.rb.model.IndentyBean;
import com.robin8.rb.model.OtherLoginListBean;
import com.robin8.rb.module.first.widget.ScrollViewExtend;
import com.robin8.rb.module.mine.presenter.BindSocialPresenter;
import com.robin8.rb.module.social.BindSocialActivity;
import com.robin8.rb.module.social.SocialInfluenceActivity;
import com.robin8.rb.module.social.adapter.MyHoritalListAdapter;
import com.robin8.rb.module.social.adapter.TabsFragmentAdapter;
import com.robin8.rb.module.social.bean.InfluenceScoreBean;
import com.robin8.rb.module.social.fragment.SocialCardFragment;
import com.robin8.rb.module.social.view.CustomViewPager;
import com.robin8.rb.module.social.view.EasySlidingTabs;
import com.robin8.rb.module.social.view.HorizontalListView;
import com.robin8.rb.module.social.view.ShowDataView;
import com.robin8.rb.module.social.view.StickyNavLayout;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.ui.widget.myprogress.RoundIndicatorView;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.UIUtils;
import com.robin8.rb.view.widget.CustomDialogManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 Created by zc on 2017/7/11. */

public class InfluencePager extends BasePager implements View.OnClickListener {

    private View mPager;
    private RoundIndicatorView viewScoreResult;
    private EasySlidingTabs myTabs;
    private CustomViewPager mViewPager;
    private StickyNavLayout stickyNavLayout;
    private List<Fragment> fragments;
    private String[] titles;
    public TabsFragmentAdapter adapter;
    private HorizontalListView mHlistView;
    private List<InfluenceScoreBean.SimilarKolsBean> mDataList;
    private View line;
    private ScrollViewExtend mScroll;
    private TextView tvSuspend;
    private LinearLayout llToShare;
    private CustomDialogManager mCustomDialogManager;

    private static final String IMAGE_URL = "http://7xq4sa.com1.z0.glb.clouddn.com/robin8_icon.png";
    private static final String TITLE_URL = CommonConfig.SERVICE + "/invite?inviter_id=";
    private TextView tvProveSocial;
    private RelativeLayout llHaveResult;
    private LinearLayout llNoResult;
    private LinearLayout llNoSimilar;
    private LinearLayout llIndustry;
    private String myResult;
    private SocialCardFragment weiBoFg;
    private SocialCardFragment weiChatFg;
    private MyHoritalListAdapter myHoritalListAdapter;
    private ShowDataView showDataView;
    private LinearLayout llManNoLogin;
    private TextView tvMan;
    private String TAG = "";
    private WProgressDialog mWProgressDialog;
    private String[] socialTagName;
    private String[] socialTagEnglish;
    private Map<String, String> mapCe;
    private List<String> need;
    private List<String> real;
    private List<String> resultList;
    private List<Float> valueList;
    private InfluenceScoreBean influenceScoreBean;
    //  private Runnable runnable;


    public InfluencePager(FragmentActivity activity) {
        super(activity);
        this.mActivity = activity;
        rootView = initView();
    }

    @Override
    public View initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity.getApplicationContext());
        mPager = layoutInflater.inflate(R.layout.layout_social_detail, null);
        //        FrameLayout frameLayout = new FrameLayout(mActivity);
        //        mPager = View.inflate(mActivity, R.layout.layout_social_detail, null);
        //        frameLayout.addView(mPager, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        //====title设置====//
        RelativeLayout allTitleLayout = (RelativeLayout) mPager.findViewById(R.id.rl_title);
        TextView tvTitle = (TextView) mPager.findViewById(R.id.titlebar_text);
        tvTitle.setText(R.string.text_social);
        tvTitle.setVisibility(View.VISIBLE);
        llToShare = ((LinearLayout) mPager.findViewById(R.id.ll_share));
        llToShare.setVisibility(View.VISIBLE);
        llToShare.setOnClickListener(this);
        allTitleLayout.setVisibility(View.VISIBLE);
        //====等待测评结果====／／
        llManNoLogin = ((LinearLayout) mPager.findViewById(R.id.layout_man));//未登录界面展示
        tvMan = ((TextView) mPager.findViewById(R.id.tv_measure_this));
        tvMan.setOnClickListener(this);//检测是否绑定了微博
        llHaveResult = ((RelativeLayout) mPager.findViewById(R.id.layout_have));
        llNoResult = ((LinearLayout) mPager.findViewById(R.id.layout_no_wait));
        ((TextView) mPager.findViewById(R.id.tv_wait_this)).setText("引擎正在计算你几卡车的庞大数据,以最好的形式展示,让你了解自己有多么重要(全程不超过一分钟)");
        llNoSimilar = ((LinearLayout) mPager.findViewById(R.id.ll_similar_kol));
        llIndustry = ((LinearLayout) mPager.findViewById(R.id.ll_industry));
        //====社交详情=====//
        viewScoreResult = ((RoundIndicatorView) mPager.findViewById(R.id.view_score_result));//圆环
        viewScoreResult.setCurrentValues(0);
        viewScoreResult.setTitle("");
        tvProveSocial = ((TextView) mPager.findViewById(R.id.tv_prove_social));//提升影响力
        tvProveSocial.setOnClickListener(this);
        line = ((View) mPager.findViewById(R.id.my_line));
        tvSuspend = ((TextView) mPager.findViewById(R.id.tv_suspend));//提升影响力悬浮按钮
        tvSuspend.setOnClickListener(this);
        //====横向推荐====//
        mHlistView = ((HorizontalListView) mPager.findViewById(R.id.list_recommend));
        mDataList = new ArrayList<>();
        //====关键词====//
        showDataView = ((ShowDataView) mPager.findViewById(R.id.show_my_data));
        //====fragment====//
        myTabs = ((EasySlidingTabs) mPager.findViewById(R.id.my_indicator));
        mScroll = ((ScrollViewExtend) mPager.findViewById(R.id.scroll_extend));
        myTabs.setUnderlineHeight(1);
        mViewPager = ((CustomViewPager) mPager.findViewById(R.id.my_vp));
        mScroll.setOnScrollChangeListener(new ScrollViewExtend.OnScrollChangeListener() {

            @Override
            public void onScrollChange(ScrollViewExtend view, int x, int y, int oldx, int oldy) {
                if (checkIsVisible(mActivity, line)) {
                    tvSuspend.setVisibility(View.GONE);
                } else {
                    tvSuspend.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrollBottomListener() {

            }

            @Override
            public void onScrollTopListener() {

            }
        });
      //  myHoritalListAdapter = new MyHoritalListAdapter(mActivity, mDataList);
        fragments = new LinkedList<>();
        titles = new String[]{"微博", "微信"};
        weiBoFg = SocialCardFragment.newInstance(0, mViewPager);
        weiChatFg = SocialCardFragment.newInstance(1, mViewPager);
        fragments.add(weiBoFg);
        fragments.add(weiChatFg);
        this.adapter = new TabsFragmentAdapter(mActivity.getSupportFragmentManager(), titles, this.fragments);
        mViewPager.setAdapter(this.adapter);
        myTabs.setViewPager(mViewPager);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewPager.resetHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.resetHeight(0);
        //LogUtil.LogShitou("估计是页面没", "没在的原因");
        //===============//
        return mPager;
    }

    @Override
    public void initData() {
        //        if (! isLogined()) {
        //            llManNoLogin.setVisibility(View.VISIBLE);
        //            return;
        //        } else {
        //            llManNoLogin.setVisibility(View.GONE);
        //            loadData();
        //        }
        //        if (TextUtils.isEmpty(HelpTools.getCommonXml(HelpTools.IsBind))){
        //           llManNoLogin.setVisibility(View.VISIBLE);
        //        }else {
        //            llManNoLogin.setVisibility(View.GONE);
        //            loadData();
        //        }
        //        if (TextUtils.isEmpty(CacheUtils.getString(mActivity, SPConstants.INFLUENCE_DATA,null))){
        //            //没有缓存数据
        //
        //        }
        BasePresenter mBasePresenter = new BasePresenter();
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.INFLUENCE_INFO_LIST), null, new RequestCallback() {

            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onResponse(String response) {
             //   LogUtil.LogShitou("查询是否绑定了weibo", CommonConfig.INFLUENCE_INFO_LIST + "///" + response);
                IndentyBean indentyBean = GsonTools.jsonToBean(response, IndentyBean.class);
                if (indentyBean != null) {
                    if (indentyBean.getError() == 0) {
                        if (indentyBean.getIdentities().size() != 0) {
                            for (int i = 0; i < indentyBean.getIdentities().size(); i++) {
                                if ((indentyBean.getIdentities().get(i).getProvider()).equals("weibo")) {
                                    TAG = "weibo";
                                }
                            }
                            if (TAG.equals("weibo")) {
                                llManNoLogin.setVisibility(View.GONE);
                                loadData();
                            } else {
                                llHaveResult.setVisibility(View.INVISIBLE);
                                llManNoLogin.setVisibility(View.VISIBLE);
                                //  bindSocial(mActivity, mActivity.getString(R.string.weibo));
                            }
                        } else {
                            llHaveResult.setVisibility(View.INVISIBLE);
                            llManNoLogin.setVisibility(View.VISIBLE);
                            //  bindSocial(mActivity, mActivity.getString(R.string.weibo));
                        }
                    } else {
                        llHaveResult.setVisibility(View.INVISIBLE);
                        llManNoLogin.setVisibility(View.VISIBLE);
                        //  bindSocial(mActivity, mActivity.getString(R.string.weibo));
                    }
                } else {
                    llHaveResult.setVisibility(View.INVISIBLE);
                    llManNoLogin.setVisibility(View.VISIBLE);
                    // bindSocial(mActivity, mActivity.getString(R.string.weibo));
                }
            }

        });
    }

    private void loadData() {
        BasePresenter mBasePresenter = new BasePresenter();
        RequestParams params = new RequestParams();
        params.put("provider", "weibo");
        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.CALCULATE_INFLUENCE_SCORE), params, new RequestCallback() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
               // LogUtil.LogShitou("影响力第一个接口", "==>" + response);
                BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);
                if (baseBean != null) {
                    if (baseBean.getError() == 0) {
                        loadDataTwo();
                    } else {
                        CustomToast.showShort(mActivity, baseBean.getMessage());
                    }
                } else {
                    // llHaveResult.setVisibility(View.INVISIBLE);
                    llNoResult.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private int count = 0;

    private void loadDataTwo() {
        BasePresenter mBasePresenter = new BasePresenter();
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.INFLUENCE_SCORE), null, new RequestCallback() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
               // LogUtil.LogShitou("影响力第二步接口", "===================>" + response);
                // CacheUtils.putString(mActivity, SPConstants.INFLUENCE_DATA, response);
               // LogUtil.LogShitou("看看走了几次", "====>" + count);
                dealDatas(response);
                //weiBoFg.myData(response,0);
            }

        });
    }

    private boolean again = false;
    private boolean overTime = false;

    private void dealDatas(String response) {
        influenceScoreBean = GsonTools.jsonToBean(response, InfluenceScoreBean.class);
        if (influenceScoreBean != null) {
            if (influenceScoreBean.getError() == 0) {
                if (influenceScoreBean.isCalculated()) {
                    //获取数据
                    HelpTools.insertCommonXml(HelpTools.PagerData, response);
                    if (again == true) {
                        llNoResult.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.anim_alpha_out));
                        llNoResult.setVisibility(View.GONE);
                        llHaveResult.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.anim_alpha_in));
                        llHaveResult.setVisibility(View.VISIBLE);
                        again = false;
                    } else {
                        llNoResult.setVisibility(View.GONE);
                        llHaveResult.setVisibility(View.VISIBLE);
                    }

                    //  llNoResult.setVisibility(View.GONE);
                    weiBoFg.myData(response, 0);
                    postDatas(influenceScoreBean);
                } else {
                    //获得倒计时
                    HelpTools.insertCommonXml(HelpTools.PagerData, "");
                    if (count>=5){
                        overTime=true;
                    }else {
                        overTime=false;
                    }
                    llHaveResult.setVisibility(View.INVISIBLE);
                    llNoResult.setVisibility(View.VISIBLE);
                    //                    runnable = new Runnable() {
                    //
                    //                        @Override
                    //                        public void run() {
                    //                            count++;
                    //                            again = true;
                    //                            loadDataTwo();
                    //                            handler.postDelayed(this,5000);
                    //                        }
                    //                    };
                    if (overTime==false){
                        UIUtils.runInMainThread(new Runnable() {

                            @Override
                            public void run() {
                                new Handler().postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        // new Handler().postDelayed(new Runnable() {
                                        //
                                        //     @Override
                                        //     public void run() {
                                        //
                                        //     }
                                        // }, 3500);
                                        count++;
                                        again = true;
                                        loadDataTwo();
                                    }
                                }, 5000);
                            }
                        });
                    }

                    //   LogUtil.LogShitou("倒计时", "====>");
                }
            } else {
                CustomToast.showShort(mActivity, influenceScoreBean.getMessage());
            }
            if (influenceScoreBean.getMessage() != null) {
                llNoResult.setVisibility(View.VISIBLE);
                llHaveResult.setVisibility(View.INVISIBLE);
                CustomToast.showShort(mActivity, influenceScoreBean.getMessage());
            }
        }
    }

    //  private Handler handler = new Handler();

    //    @Override
    //    public void pasue() {
    //        super.pasue();
    //        if (handler!=null){
    //            handler.removeCallbacks(runnable);
    //        }
    //    }

    private void postDatas(InfluenceScoreBean influenceScoreBean) {
        mapCe = new HashMap<>();
        need = new ArrayList<>();
        real = new ArrayList<>();
        resultList = new ArrayList<>();
        valueList = new ArrayList<>();
        String[] s = new String[8];
        socialTagName = mActivity.getResources().getStringArray(R.array.social_tag_name);
        socialTagEnglish = mActivity.getResources().getStringArray(R.array.social_tag_english);

        for (int i = 0; i < socialTagEnglish.length; i++) {
            mapCe.put(socialTagEnglish[i], socialTagName[i]);
        }

        viewScoreResult.setCurrentValues(Float.valueOf(String.valueOf(influenceScoreBean.getInfluence_score())));
        viewScoreResult.setTitle(influenceScoreBean.getInfluence_level());
        if (influenceScoreBean.getSimilar_kols().size() == 0) {
            llNoSimilar.setVisibility(View.GONE);
        } else {
            llNoSimilar.setVisibility(View.VISIBLE);
            mDataList = influenceScoreBean.getSimilar_kols();
            mHlistView.setAdapter(new MyHoritalListAdapter(mActivity, mDataList));
        }
        int size = influenceScoreBean.getIndustries().size();
        if (size == 0) {
            llIndustry.setVisibility(View.GONE);
        } else {
            llIndustry.setVisibility(View.VISIBLE);
            if (size >= 8) {
                for (int i = 0; i < 8; i++) {
                    valueList.add(Float.valueOf(String.valueOf(influenceScoreBean.getIndustries().get(i).getIndustry_score())));
                    s[i] = mapCe.get(influenceScoreBean.getIndustries().get(i).getIndustry_name());
                }
                showDataView.setStr(s);
                showDataView.setValue(valueList);
            }else {
                for (int i = 0; i < influenceScoreBean.getIndustries().size(); i++) {
                    real.add(influenceScoreBean.getIndustries().get(i).getIndustry_name());
                    valueList.add(Float.valueOf(String.valueOf(influenceScoreBean.getIndustries().get(i).getIndustry_score())));
                }
                need.addAll(Arrays.asList(socialTagEnglish));
                resultList.addAll(real);
                need.removeAll(real);
                if (influenceScoreBean.getIndustries().size() < 8 && influenceScoreBean.getIndustries().size() > 0) {
                    for (int i = 0; i < 8 - influenceScoreBean.getIndustries().size(); i++) {
                        resultList.add(need.get(i));
                        valueList.add(0f);
                    }
                }
                for (int i = 0; i < resultList.size(); i++) {
                    s[i] = mapCe.get(resultList.get(i));
                }
                showDataView.setStr(s);
                showDataView.setValue(valueList);
            }
        }
        // LogUtil.LogShitou("走过的卢阿", "3333333");
    }

    private boolean isLogined() {
        BaseApplication baseApplication = BaseApplication.getInstance();
        return baseApplication.hasLogined();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_suspend:
            case R.id.tv_prove_social:
                //提升影响力
                Intent intent = new Intent(mActivity, SocialInfluenceActivity.class);
                if (influenceScoreBean != null) {
                    intent.putExtra("user_url", influenceScoreBean.getAvatar_url());
                    intent.putExtra("user_score", String.valueOf(influenceScoreBean.getInfluence_score()));
                    intent.putExtra("user_level", influenceScoreBean.getInfluence_level());
                    intent.putExtra("user_percentile", influenceScoreBean.getInfluence_score_percentile());
                    intent.putExtra("user_data", influenceScoreBean.getCalculated_date());
                }
                mActivity.startActivity(intent);
                break;
            case R.id.ll_share:
                showDialog(mActivity);
                break;
            case R.id.img_wechat:
                share(Wechat.NAME);
                break;
            case R.id.img_wechat_room:
                share(WechatMoments.NAME);
                break;
            case R.id.img_weibo:
                share(SinaWeibo.NAME);
                break;
            case R.id.img_qq:
                share(QQ.NAME);
                break;
            case R.id.img_qq_room:
                share(QZone.NAME);
                break;
            case R.id.tv_measure_this:
                bindSocial(mActivity, mActivity.getResources().getString(R.string.weibo));
                break;
        }
    }

    private void bindSocial(final Activity activity, final String providerName) {

        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(activity);
        }
        mWProgressDialog.show();
        BindSocialPresenter presenter = new BindSocialPresenter(activity.getApplicationContext(), null, providerName, 0);
        presenter.setOnBindListener(new BindSocialPresenter.OnBindListener() {

            @Override
            public void onResponse(String name) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                if (null != name) {
                    CustomToast.showShort(activity, "已成功绑定" + providerName);
                    llManNoLogin.setVisibility(View.GONE);
                    bindPostData(activity, providerName, name);
                } else {
                    CustomToast.showLong(activity, "绑定失败，请重试");
                }
            }
        });
        if (activity.getString(R.string.weibo).equals(providerName)) {
            presenter.authorize(new SinaWeibo(activity));
        } else if (activity.getString(R.string.weixin).equals(providerName)) {
            presenter.authorize(new Wechat(activity));
        }
    }

    private void bindPostData(final Activity activity, final String name, String userName) {

        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(activity);
        }
        mWProgressDialog.show();
        BasePresenter mBasePresenter = new BasePresenter();
        RequestParams params = new RequestParams();
        params.put("provider_name", name);
        params.put("price", "1");
        params.put("followers_count", "1");
        params.put("username", userName);
        //LogUtil.LogShitou("绑定微信的报价之类的", name + "//" + userName);

        mBasePresenter.getDataFromServer(true, HttpRequest.POST, (HelpTools.getUrl(CommonConfig.UPDATE_SOCIAL_URL_OLD)), params, new RequestCallback() {

            @Override
            public void onError(Exception e) {

                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
             //   LogUtil.LogShitou("提交微博绑定", "OK" + response);
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);
                if (bean == null) {
                    CustomToast.showShort(activity, activity.getString(R.string.please_data_wrong));
                    return;
                }
                if (bean.getError() == 0) {
                    // HelpTools.insertCommonXml(HelpTools.IsBind,"is");
                    loadData();
                    //  initData();
                } else {
                    CustomToast.showShort(activity, "绑定失败");
                }
            }
        });
    }

    private void showDialog(final Activity activity) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_share, null);
        ImageView weixin = (ImageView) view.findViewById(R.id.img_wechat);
        ImageView wechatRoom = (ImageView) view.findViewById(R.id.img_wechat_room);
        ImageView weibo = (ImageView) view.findViewById(R.id.img_weibo);
        ImageView qq = (ImageView) view.findViewById(R.id.img_qq);
        ImageView qqRoom = (ImageView) view.findViewById(R.id.img_qq_room);
        mCustomDialogManager = new CustomDialogManager(mActivity, view);
        weixin.setOnClickListener(this);
        wechatRoom.setOnClickListener(this);
        weibo.setOnClickListener(this);
        qq.setOnClickListener(this);
        qqRoom.setOnClickListener(this);
        mCustomDialogManager.dg.setCanceledOnTouchOutside(true);
        mCustomDialogManager.dg.getWindow().setGravity(Gravity.CENTER);
        mCustomDialogManager.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        mCustomDialogManager.showDialog();
    }

    /**
     弹出分享面板
     @param platName
     */
    private void share(String platName) {

        if (mCustomDialogManager != null) {
            mCustomDialogManager.dismiss();
        }

        int id = BaseApplication.getInstance().getLoginBean().getKol().getId();
        CustomToast.showShort(mActivity, "正在前往分享...");
        ShareSDK.initSDK(mActivity);
        OnekeyShare oks = new OnekeyShare();
        oks.setCallback(new MySharedListener());
        oks.setPlatform(platName);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        if (SinaWeibo.NAME.equals(platName)) {
            oks.setText(mActivity.getResources().getString(R.string.share_pk_text) + TITLE_URL + String.valueOf(id));
        } else {
            oks.setText(mActivity.getResources().getString(R.string.share_pk_text));
        }
        oks.setTitle(mActivity.getResources().getString(R.string.share_pk_title));

        oks.setTitleUrl(TITLE_URL + String.valueOf(id));
        oks.setImageUrl(IMAGE_URL);
        oks.setUrl(TITLE_URL + String.valueOf(id));
        oks.setSite(mActivity.getResources().getString(R.string.app_name));
        oks.setSiteUrl(CommonConfig.SITE_URL);
        oks.show(mActivity);
    }

    private class MySharedListener implements PlatformActionListener {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            CustomToast.showShort(mActivity, "分享成功");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            CustomToast.showShort(mActivity, "分享失败");
        }

        @Override
        public void onCancel(Platform platform, int i) {
            CustomToast.showShort(mActivity, "分享取消");
        }
    }

    public void goToBindSocial() {

        HttpRequest.getInstance().get(true, HelpTools.getUrl(CommonConfig.START_URL), new RequestCallback() {

            @Override
            public void onError(Exception e) {
                CustomToast.showShort(mActivity, mActivity.getString(R.string.no_net));
            }

            @Override
            public void onResponse(String response) {
                // LogUtil.LogShitou("影响力开始测试--->", "++" + response);
                if (TextUtils.isEmpty(response)) {
                    return;
                }

                OtherLoginListBean bean = GsonTools.jsonToBean(response, OtherLoginListBean.class);
                if (bean.getError() == 0) {
                    Intent intent = new Intent(mActivity, BindSocialActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list", bean.getIdentities());
                    bundle.putString("kol_uuid", bean.getKol_uuid());
                    bundle.putBoolean("uploaded_contacts", bean.isUploaded_contacts());
                    mActivity.startActivity(intent.putExtras(bundle));
                    mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });
    }

    public static Boolean checkIsVisible(Context context, View view) {
        // 如果已经加载了，判断广告view是否显示出来，然后曝光
        int screenWidth = getScreenMetrics(context).x;
        int screenHeight = getScreenMetrics(context).y;
        Rect rect = new Rect(0, 0, screenWidth, screenHeight);
        int[] location = new int[2];
        view.getLocationInWindow(location);
        if (view.getLocalVisibleRect(rect)) {
            return true;
        } else {
            //view已不在屏幕可见区域;
            return false;
        }
    }

    /**
     获取屏幕宽度和高度，单位为px
     @param context
     @return
     */
    public static Point getScreenMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        return new Point(w_screen, h_screen);
    }

}

//
//    BasePresenter mBasePresenter = new BasePresenter();
//                mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.INFLUENCE_INFO_LIST), null, new RequestCallback() {
//
//@Override
//public void onError(Exception e) {
//        }
//
//@Override
//public void onResponse(String response) {
//        IndentyBean indentyBean = GsonTools.jsonToBean(response, IndentyBean.class);
//        if (indentyBean != null) {
//        if (indentyBean.getError() == 0) {
//        if (indentyBean.getIdentities().size() != 0) {
//        for (int i = 0; i < indentyBean.getIdentities().size(); i++) {
//        if ((indentyBean.getIdentities().get(i).getProvider()).equals("weibo")) {
//        TAG = "weibo";
//        }
//        }
//        if (TAG.equals("weibo")) {
//        llManNoLogin.setVisibility(View.GONE);
//        initData();
//        } else {
//        bindSocial(mActivity, mActivity.getString(R.string.weibo));
//        }
//        } else {
//        bindSocial(mActivity, mActivity.getString(R.string.weibo));
//        }
//        } else {
//        bindSocial(mActivity, mActivity.getString(R.string.weibo));
//        }
//        } else {
//        bindSocial(mActivity, mActivity.getString(R.string.weibo));
//        }
//        }
//
//        });

//    private void bindSocial(final Activity activity, final String providerName) {
//
//        if (mWProgressDialog == null) {
//            mWProgressDialog = WProgressDialog.createDialog(activity);
//        }
//        mWProgressDialog.show();
//        BindSocialPresenter presenter = new BindSocialPresenter(activity.getApplicationContext(), null, providerName, 0);
//        presenter.setOnBindListener(new BindSocialPresenter.OnBindListener() {
//
//            @Override
//            public void onResponse(String name) {
//                if (mWProgressDialog != null) {
//                    mWProgressDialog.dismiss();
//                }
//                if (null != name) {
//                    CustomToast.showShort(activity, "已成功绑定" + providerName);
//                    bindPostData(activity, providerName, name);
//                } else {
//
//                    CustomToast.showLong(activity, "绑定失败，请重试");
//                }
//            }
//        });
//        if (activity.getString(R.string.weibo).equals(providerName)) {
//            presenter.authorize(new SinaWeibo(activity));
//        } else if (activity.getString(R.string.weixin).equals(providerName)) {
//            presenter.authorize(new Wechat(activity));
//        }
//
//
//    }
//
//    private void bindPostData(final Activity activity, final String name, String userName) {
//
//        if (mWProgressDialog == null) {
//            mWProgressDialog = WProgressDialog.createDialog(activity);
//        }
//        mWProgressDialog.show();
//        BasePresenter mBasePresenter = new BasePresenter();
//        RequestParams params = new RequestParams();
//        params.put("provider_name", name);
//        params.put("price", "1");
//        params.put("followers_count", "1");
//        params.put("username", userName);
//        //LogUtil.LogShitou("绑定微信的报价之类的", name + "//" + userName);
//
//        mBasePresenter.getDataFromServer(true, HttpRequest.POST, (HelpTools.getUrl(CommonConfig.UPDATE_SOCIAL_URL_OLD)), params, new RequestCallback() {
//
//            @Override
//            public void onError(Exception e) {
//
//                if (mWProgressDialog != null) {
//                    mWProgressDialog.dismiss();
//                }
//            }
//
//            @Override
//            public void onResponse(String response) {
//                LogUtil.LogShitou("提交微博绑定", "OK" + response);
//                if (mWProgressDialog != null) {
//                    mWProgressDialog.dismiss();
//                }
//                BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);
//                if (bean == null) {
//                    CustomToast.showShort(activity, activity.getString(R.string.please_data_wrong));
//                    return;
//                }
//                if (bean.getError() == 0) {
//                    llManNoLogin.setVisibility(View.GONE);
//                    initData();
//                    // CustomToast.showShort(activity, "绑定le" + name);
//                } else {
//                    CustomToast.showShort(activity, "绑定失败");
//                }
//            }
//        });
//    }
//