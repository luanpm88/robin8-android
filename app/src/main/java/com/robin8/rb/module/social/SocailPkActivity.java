package com.robin8.rb.module.social;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.activity.MainActivity;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.model.BaseBean;
import com.robin8.rb.model.LoginBean;
import com.robin8.rb.module.social.bean.InfluenceScoreBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.myprogress.CircleView;
import com.robin8.rb.ui.widget.myprogress.RoundCornerProgressBar;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.view.widget.CircleImageView;
import com.robin8.rb.view.widget.CustomDialogManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
 影响力pk */
public class SocailPkActivity extends BaseActivity {

    private ListView mListPk;
    private List<PkListItemBean> myList;
    private ImageView mImgStarLeftOne;
    private ImageView mImgStarRightOne;
    private ImageView mImgStarRightTwo;
    private ImageView mImgStarRightThree;
    private ImageView mImgStarRightfour;
    private ImageView mImgStarRightFive;
    private ImageView mImgStarRightSix;
    private ImageView mImgStarRightSeven;
    private ImageView mImgStarLeftTwo;
    private ImageView mImgStarLeftThree;
    private ImageView mImgStarLeftfour;
    private ImageView mImgStarLeftFive;
    private ImageView mImgStarLeftSix;
    private ImageView mImgStarLeftSeven;
    private ImageView mImgCrown;
    private ImageView mImgCrownRight;
    private TextView mTvNameLeft;
    private TextView mTvNameRight;
    private TextView mTvResultLeft;
    private TextView mTvResultRight;
    private CircleImageView mImgUserPhotoLeft;
    private CircleImageView mImgUserPhotoRight;
    private CircleView mImgPhotoBgLeft;
    private CircleView mImgPhotoBgLeftWhite;
    private CircleView mImgPhotoBgRight;
    private CircleView mImgPhotoBgRightWhite;
    private RoundCornerProgressBar mProLeft;
    private RoundCornerProgressBar mProRight;
    private ImageView mImgPkV;
    private ImageView mImgPkS;
    private boolean begin = false;
    private boolean over = false;
    private MyPkListAdapter myPkListAdapter;
    private RelativeLayout mLeftSuccLayout;
    private RelativeLayout mRightSuccLayout;
    private CustomDialogManager mCustomDialogManager;
   // private static final String IMAGE_URL = "http://7xq4sa.com1.z0.glb.clouddn.com/robin8_icon.png";
    private static final String IMAGE_URL = "http://pp.myapp.com/ma_icon/0/icon_42248507_1505726963/256";
    private static final String TITLE_URL = CommonConfig.SERVICE + "kol/";
    private int success = 0;
    private int iszero = 0;
    private String MyleftScore = "";

    @Override
    public void setTitleView() {
        mLLTitleBar.setBackgroundResource(android.R.color.transparent);
        mTVCenter.setText("影响力对决");
        mShare.setVisibility(View.VISIBLE);
        mShare.setOnClickListener(this);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_socail_pk, mLLContent);
        mListPk = ((ListView) view.findViewById(R.id.list_pk));
        mImgStarLeftOne = ((ImageView) view.findViewById(R.id.img_star_one));
        mImgStarRightOne = ((ImageView) view.findViewById(R.id.img_star_one_right));
        mImgStarLeftTwo = ((ImageView) view.findViewById(R.id.img_star_two));
        mImgStarRightTwo = ((ImageView) view.findViewById(R.id.img_star_two_right));
        mImgStarLeftThree = ((ImageView) view.findViewById(R.id.img_star_three));
        mImgStarRightThree = ((ImageView) view.findViewById(R.id.img_star_three_right));
        mImgStarLeftfour = ((ImageView) view.findViewById(R.id.img_star_four));
        mImgStarRightfour = ((ImageView) view.findViewById(R.id.img_star_four_right));
        mImgStarLeftFive = ((ImageView) view.findViewById(R.id.img_star_five));
        mImgStarRightFive = ((ImageView) view.findViewById(R.id.img_star_five_right));
        mImgStarLeftSix = ((ImageView) view.findViewById(R.id.img_star_six));
        mImgStarRightSix = ((ImageView) view.findViewById(R.id.img_star_six_right));
        mImgStarLeftSeven = ((ImageView) view.findViewById(R.id.img_star_seven));
        mImgStarRightSeven = ((ImageView) view.findViewById(R.id.img_star_seven_right));
        mImgCrown = ((ImageView) view.findViewById(R.id.img_crown));
        mImgCrownRight = ((ImageView) view.findViewById(R.id.img_crown_right));

        mImgUserPhotoLeft = ((CircleImageView) view.findViewById(R.id.img_user_left));
        mImgUserPhotoRight = ((CircleImageView) view.findViewById(R.id.img_user_right));
        mImgPhotoBgLeft = ((CircleView) view.findViewById(R.id.img_user_left_bg));
        mImgPhotoBgLeftWhite = ((CircleView) view.findViewById(R.id.img_user_left_bg_white));
        mImgPhotoBgRight = ((CircleView) view.findViewById(R.id.img_user_right_bg));
        mImgPhotoBgRightWhite = ((CircleView) view.findViewById(R.id.img_user_right_bg_white));
        mProLeft = ((RoundCornerProgressBar) view.findViewById(R.id.pro_user_left));
        mProRight = ((RoundCornerProgressBar) view.findViewById(R.id.pro_user_right));
        mImgPkV = ((ImageView) view.findViewById(R.id.img_pk_v));
        mImgPkS = ((ImageView) view.findViewById(R.id.img_pk_s));


        mLeftSuccLayout = ((RelativeLayout) view.findViewById(R.id.user_success_layout_left));
        mRightSuccLayout = ((RelativeLayout) view.findViewById(R.id.user_success_layout_right));
        mTvNameLeft = ((TextView) view.findViewById(R.id.tv_user_name_left));
        mTvNameRight = ((TextView) view.findViewById(R.id.tv_user_name_right));
        mTvResultLeft = ((TextView) view.findViewById(R.id.tv_result_left));
        mTvResultRight = ((TextView) view.findViewById(R.id.tv_result_right));

        TextView tvToCampaign = (TextView) view.findViewById(R.id.tv_to_campaign);
        TextView tvToWechat = (TextView) view.findViewById(R.id.tv_to_wechat);
        tvToCampaign.setOnClickListener(this);
        tvToWechat.setOnClickListener(this);

        initData();

    }

    private void inAinmation() {
        List<Animator> animators = new ArrayList<>();
        ObjectAnimator translationXAnim = ObjectAnimator.ofFloat(mImgPkV, "translationX", - 150, 0, - 3, 0);
        translationXAnim.setDuration(1200);
        translationXAnim.start();
        animators.add(translationXAnim);

        ObjectAnimator translationYAnim = ObjectAnimator.ofFloat(mImgPkV, "translationY", - 200, 0, - 3, 0);
        translationYAnim.setDuration(1200);
        translationYAnim.start();
        animators.add(translationYAnim);

        ObjectAnimator alpha = ObjectAnimator.ofFloat(mImgPkV, "alpha", 0, 0.7f, 1);
        alpha.setDuration(1200);
        alpha.start();
        animators.add(alpha);
        ObjectAnimator transXAnim = ObjectAnimator.ofFloat(mImgPkS, "translationX", 150, 0, 3, 0);
        transXAnim.setDuration(1200);
        transXAnim.start();
        animators.add(transXAnim);

        ObjectAnimator transYAnim = ObjectAnimator.ofFloat(mImgPkS, "translationY", 200, 0, 3, 0);
        transYAnim.setDuration(1200);
        transYAnim.start();
        animators.add(transYAnim);

        ObjectAnimator alphaS = ObjectAnimator.ofFloat(mImgPkS, "alpha", 0, 0.7f, 1);
        alphaS.setDuration(1200);
        alphaS.start();
        animators.add(alphaS);

        AnimatorSet btnSexAnimatorSet = new AnimatorSet();//
        btnSexAnimatorSet.playTogether(animators);
        btnSexAnimatorSet.setStartDelay(1);
        btnSexAnimatorSet.start();
        btnSexAnimatorSet.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
               // LogUtil.LogShitou("动画结束", "新的开始");
                //showPro(80);
                begin = true;
                mProLeft.setBegin(true);
                if (iszero == 1) {
                    mProLeft.setProgress(0);
                } else {
                    if (TextUtils.isEmpty(MyleftScore)) {
                        mProLeft.setProgress(Float.valueOf(MyleftScore));
                    } else {
                        mProLeft.setProgress(Float.parseFloat(String.valueOf(influenceScoreBean.getInfluence_score())));
                    }
                }
                mProRight.setBegin(true);
                mProRight.setProgress(Float.valueOf(right_score));
                myPkListAdapter.notifyDataSetChanged();
                mProLeft.setOnAnimationEndListener(new RoundCornerProgressBar.OnAnimationEndListener() {

                    @Override
                    public void onAnimationEnd() {
                        mProLeft.setBegin(false);
                        mProRight.setBegin(false);
                        begin = false;
                        over = true;
                        myPkListAdapter.notifyDataSetChanged();
                        String leftScroe;
                        if (iszero == 1) {
                            leftScroe = "0";
                        } else {
                            leftScroe = String.valueOf(influenceScoreBean.getInfluence_score());
                        }
                        if (Float.valueOf(leftScroe) > Float.valueOf(right_score)) {
                            //左边赢了
                            success = 1;
                            mProRight.setProgressColor(getResources().getColor(R.color.gray_acacac));
                            mProRight.setProgressBackgroundColor(getResources().getColor(R.color.white_custom));
                            //  mImgPhotoBgRight.setTitleBackgroundColor(R.color.red_custom);
                            mImgPhotoBgRight.setVisibility(View.GONE);
                            mImgPhotoBgRightWhite.setVisibility(View.VISIBLE);
                           // LogUtil.LogShitou("左边赢", "左边！！！");
                        } else if (leftScroe.equals(right_score)) {
                            success = 3;
                           // LogUtil.LogShitou("平赢", "平边！！！");
                        } else {
                            success = 2;
                            // LogUtil.LogShitou("右边赢", "左边的数据" + Float.valueOf(String.valueOf(influenceScoreBean.getInfluence_score())) + "右边！！！" + Float.valueOf(right_score));
                            mProLeft.setProgressColor(getResources().getColor(R.color.gray_acacac));
                            mProLeft.setProgressBackgroundColor(getResources().getColor(R.color.white_custom));
                            // mImgPhotoBgLeft.setTitleBackgroundColor(R.color.red_custom);
                            mImgPhotoBgLeft.setVisibility(View.GONE);
                            mImgPhotoBgLeftWhite.setVisibility(View.VISIBLE);
                           // LogUtil.LogShitou("右边赢", "右边！！！");
                        }
                        inAinmationSuccess(success);
                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_share:
                showDialog(SocailPkActivity.this);
                break;
            case R.id.tv_to_campaign:
                //去做活动
                Intent intent = new Intent(SocailPkActivity.this, MainActivity.class);
                intent.putExtra("register_main", "zhu");
                startActivity(intent);
                finish();
                break;
            case R.id.tv_to_wechat:
                //和微信朋友pk
                share(Wechat.NAME);
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
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void inAinmationSuccess(int success) {
        if (success == 1) {
            mTvResultLeft.setVisibility(View.VISIBLE);
            mLeftSuccLayout.setVisibility(View.VISIBLE);
            mRightSuccLayout.setVisibility(View.INVISIBLE);
            mTvResultRight.setVisibility(View.VISIBLE);
            mTvResultRight.setBackground(SocailPkActivity.this.getDrawable(R.drawable.shape_gray_rectangular));
            mTvResultLeft.setBackground(SocailPkActivity.this.getDrawable(R.drawable.shape_yellow_rectangular));
            mTvResultLeft.setText("胜");
            mTvResultRight.setText("败");
        } else if (success == 2) {
            mTvResultRight.setVisibility(View.VISIBLE);
            mRightSuccLayout.setVisibility(View.VISIBLE);
            mLeftSuccLayout.setVisibility(View.INVISIBLE);
            mTvResultLeft.setVisibility(View.VISIBLE);
            mTvResultLeft.setBackground(SocailPkActivity.this.getDrawable(R.drawable.shape_gray_rectangular));
            mTvResultRight.setBackground(SocailPkActivity.this.getDrawable(R.drawable.shape_yellow_rectangular));
            mTvResultLeft.setText("败");
            mTvResultRight.setText("胜");
        } else if (success == 3) {
            mTvResultRight.setVisibility(View.VISIBLE);
            mRightSuccLayout.setVisibility(View.VISIBLE);
            mLeftSuccLayout.setVisibility(View.VISIBLE);
            mTvResultLeft.setVisibility(View.VISIBLE);
            mTvResultLeft.setBackground(SocailPkActivity.this.getDrawable(R.drawable.shape_yellow_rectangular));
            mTvResultRight.setBackground(SocailPkActivity.this.getDrawable(R.drawable.shape_yellow_rectangular));
            mTvResultLeft.setText("平");
            mTvResultRight.setText("平");
        }

        List<Animator> animators = new ArrayList<>();
        if (success == 1) {
            creatAnimation(mImgCrown, animators, 800);
            creatAnimation(mTvResultLeft, animators, 800);
            creatAnimation(mTvResultRight, animators, 800);

            creatAnimation(mImgStarLeftOne, animators, 800);
            creatAnimation(mImgStarLeftTwo, animators, 800);
            creatAnimation(mImgStarLeftThree, animators, 800);
            creatAnimation(mImgStarLeftfour, animators, 800);
            creatAnimation(mImgStarLeftFive, animators, 800);
            creatAnimation(mImgStarLeftSeven, animators, 800);
            creatAnimation(mImgStarLeftSix, animators, 800);
        } else if (success == 2) {
            creatAnimation(mImgCrownRight, animators, 800);
            creatAnimation(mTvResultLeft, animators, 800);
            creatAnimation(mTvResultRight, animators, 800);

            creatAnimation(mImgStarRightOne, animators, 800);
            creatAnimation(mImgStarRightTwo, animators, 800);
            creatAnimation(mImgStarRightThree, animators, 800);
            creatAnimation(mImgStarRightfour, animators, 800);
            creatAnimation(mImgStarRightFive, animators, 800);
            creatAnimation(mImgStarRightSeven, animators, 800);
            creatAnimation(mImgStarRightSix, animators, 800);
        } else if (success == 3) {
            creatAnimation(mImgCrown, animators, 800);
            creatAnimation(mImgCrownRight, animators, 800);
            creatAnimation(mTvResultLeft, animators, 800);
            creatAnimation(mTvResultRight, animators, 800);

            creatAnimation(mImgStarRightOne, animators, 800);
            creatAnimation(mImgStarRightTwo, animators, 800);
            creatAnimation(mImgStarRightThree, animators, 800);
            creatAnimation(mImgStarRightfour, animators, 800);
            creatAnimation(mImgStarRightFive, animators, 800);
            creatAnimation(mImgStarRightSeven, animators, 800);
            creatAnimation(mImgStarRightSix, animators, 800);

            creatAnimation(mImgStarLeftOne, animators, 800);
            creatAnimation(mImgStarLeftTwo, animators, 800);
            creatAnimation(mImgStarLeftThree, animators, 800);
            creatAnimation(mImgStarLeftfour, animators, 800);
            creatAnimation(mImgStarLeftFive, animators, 800);
            creatAnimation(mImgStarLeftSeven, animators, 800);
            creatAnimation(mImgStarLeftSix, animators, 800);
        }



        AnimatorSet btnSexAnimatorSet = new AnimatorSet();//
        btnSexAnimatorSet.playTogether(animators);
        btnSexAnimatorSet.setStartDelay(1);
        btnSexAnimatorSet.start();
        btnSexAnimatorSet.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
               // LogUtil.LogShitou("动画结束", "新的开始");
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void creatAnimation(View view, List<Animator> animators, int time) {
        ObjectAnimator alphaA = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        alphaA.setDuration(time);
        alphaA.start();
        animators.add(alphaA);
    }

    private String right_score;
    private String right_post;
    private String right_comment;
    private String right_like;
    private String right_name;
    private String right_imgurl;
    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.PK_INFLUENCE;
        super.onResume();
    }
    private void initData() {
        Intent intent = getIntent();
        right_score = intent.getStringExtra("right_score");
        right_comment = intent.getStringExtra("right_comment");
        right_post = intent.getStringExtra("right_post");
        right_like = intent.getStringExtra("right_like");
        right_name = intent.getStringExtra("right_name");
        right_imgurl = intent.getStringExtra("right_imgurl");
        mTvNameRight.setText(right_name);
        if (TextUtils.isEmpty(right_imgurl)) {
            mImgUserPhotoRight.setImageResource(R.mipmap.logo_circle);
        } else {
            BitmapUtil.loadImage(getApplicationContext(), right_imgurl, mImgUserPhotoRight);
        }
        myList = new ArrayList<>();
        loadData();
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
                //  LogUtil.LogShitou("影响力第一个接口", "==>" + response);
                BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);
                if (baseBean != null) {
                    if (baseBean.getError() == 0) {
                        iszero = 0;
                        if (! TextUtils.isEmpty(HelpTools.getCommonXml(HelpTools.PagerData))) {
                            influenceScoreBean = GsonTools.jsonToBean(HelpTools.getCommonXml(HelpTools.PagerData), InfluenceScoreBean.class);
                            if (influenceScoreBean != null) {
                                if (influenceScoreBean.isCalculated()) {
                                    //获取数据
                                    if (TextUtils.isEmpty(influenceScoreBean.getAvatar_url())) {
                                        mImgUserPhotoLeft.setImageResource(R.mipmap.logo_circle);
                                    } else {
                                        BitmapUtil.loadImage(getApplicationContext(), influenceScoreBean.getAvatar_url(), mImgUserPhotoLeft);
                                    }
                                    MyleftScore = String.valueOf(influenceScoreBean.getInfluence_score());
                                    mTvNameLeft.setText(influenceScoreBean.getName());
                                    inAinmation();
                                    myList.add(new PkListItemBean(StringUtil.deleteZero(influenceScoreBean.getInfluence_score()), "影响力分数", right_score));
                                    myList.add(new PkListItemBean(String.valueOf(influenceScoreBean.getAvg_posts()), "平均转发", right_post));
                                    myList.add(new PkListItemBean(String.valueOf(influenceScoreBean.getAvg_comments()), "平均互动", right_comment));
                                    myList.add(new PkListItemBean(String.valueOf(influenceScoreBean.getAvg_likes()), "平均点赞", right_like));
                                    myPkListAdapter = new MyPkListAdapter();
                                    mListPk.setAdapter(myPkListAdapter);
                                } else {
                                    //获得倒计时
                                    //   LogUtil.LogShitou("倒计时", "====>");
                                    iszero = 1;
                                    mImgUserPhotoLeft.setImageResource(R.mipmap.logo_circle);
                                    LoginBean loginBean = BaseApplication.getInstance().getLoginBean();
                                    if (loginBean != null) {
                                        if (TextUtils.isEmpty(loginBean.getKol().getName())) {
                                            mTvNameLeft.setText("我");
                                        } else {
                                            mTvNameLeft.setText(loginBean.getKol().getName());
                                        }
                                    } else {
                                        mTvNameLeft.setText("我");
                                    }
                                    myList.add(new PkListItemBean(String.valueOf(0), "影响力分数", right_score));
                                    myList.add(new PkListItemBean(String.valueOf(0), "平均转发", right_post));
                                    myList.add(new PkListItemBean(String.valueOf(0), "平均互动", right_comment));
                                    myList.add(new PkListItemBean(String.valueOf(0), "平均点赞", right_like));
                                    myPkListAdapter = new MyPkListAdapter();
                                    mListPk.setAdapter(myPkListAdapter);
                                }
                            } else {
                                iszero = 1;
                                mImgUserPhotoLeft.setImageResource(R.mipmap.logo_circle);
                                LoginBean loginBean = BaseApplication.getInstance().getLoginBean();
                                if (loginBean != null) {
                                    if (TextUtils.isEmpty(loginBean.getKol().getName())) {
                                        mTvNameLeft.setText("我");
                                    } else {
                                        mTvNameLeft.setText(loginBean.getKol().getName());
                                    }
                                } else {
                                    mTvNameLeft.setText("我");
                                }
                                inAinmation();
                                myList.add(new PkListItemBean(String.valueOf(0), "影响力分数", right_score));
                                myList.add(new PkListItemBean(String.valueOf(0), "平均转发", right_post));
                                myList.add(new PkListItemBean(String.valueOf(0), "平均互动", right_comment));
                                myList.add(new PkListItemBean(String.valueOf(0), "平均点赞", right_like));
                                myPkListAdapter = new MyPkListAdapter();
                                mListPk.setAdapter(myPkListAdapter);
                            }
                        } else {
                            loadDataTwo();
                        }
                    } else {
                        HelpTools.insertCommonXml(HelpTools.PagerData, "");
                        iszero = 1;
                        mImgUserPhotoLeft.setImageResource(R.mipmap.logo_circle);
                        LoginBean loginBean = BaseApplication.getInstance().getLoginBean();
                        if (loginBean != null) {
                            if (TextUtils.isEmpty(loginBean.getKol().getName())) {
                                mTvNameLeft.setText("我");
                            } else {
                                mTvNameLeft.setText(loginBean.getKol().getName());
                            }
                        } else {
                            mTvNameLeft.setText("我");
                        }
                        inAinmation();
                        myList.add(new PkListItemBean(String.valueOf(0), "影响力分数", right_score));
                        myList.add(new PkListItemBean(String.valueOf(0), "平均转发", right_post));
                        myList.add(new PkListItemBean(String.valueOf(0), "平均互动", right_comment));
                        myList.add(new PkListItemBean(String.valueOf(0), "平均点赞", right_like));
                        myPkListAdapter = new MyPkListAdapter();
                        mListPk.setAdapter(myPkListAdapter);
                    }
                }

            }
        });
    }

    private InfluenceScoreBean influenceScoreBean;

    private void loadDataTwo() {
        BasePresenter mBasePresenter = new BasePresenter();
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.INFLUENCE_SCORE), null, new RequestCallback() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
             //   LogUtil.LogShitou("影响力第二步接口", "==>" + response);
                //   weiBoFg.myData(response,0);
                influenceScoreBean = GsonTools.jsonToBean(response, InfluenceScoreBean.class);
                if (influenceScoreBean != null) {
                    if (influenceScoreBean.getError() == 0) {
                        if (influenceScoreBean.isCalculated()) {
                            //获取数据
                            if (TextUtils.isEmpty(influenceScoreBean.getAvatar_url())) {
                                mImgUserPhotoLeft.setImageResource(R.mipmap.logo_circle);
                            } else {
                                BitmapUtil.loadImage(getApplicationContext(), influenceScoreBean.getAvatar_url(), mImgUserPhotoLeft);
                            }
                            MyleftScore = String.valueOf(influenceScoreBean.getInfluence_score());
                            mTvNameLeft.setText(influenceScoreBean.getName());
                            inAinmation();
                            myList.add(new PkListItemBean(StringUtil.deleteZero(influenceScoreBean.getInfluence_score()), "影响力分数", right_score));
                            myList.add(new PkListItemBean(String.valueOf(influenceScoreBean.getAvg_posts()), "平均转发", right_post));
                            myList.add(new PkListItemBean(String.valueOf(influenceScoreBean.getAvg_comments()), "平均互动", right_comment));
                            myList.add(new PkListItemBean(String.valueOf(influenceScoreBean.getAvg_likes()), "平均点赞", right_like));
                            myPkListAdapter = new MyPkListAdapter();
                            mListPk.setAdapter(myPkListAdapter);
                        } else {
                            //获得倒计时
                            //   LogUtil.LogShitou("倒计时", "====>");
                            iszero = 1;
                            mImgUserPhotoLeft.setImageResource(R.mipmap.logo_circle);
                            LoginBean loginBean = BaseApplication.getInstance().getLoginBean();
                            if (loginBean != null) {
                                if (TextUtils.isEmpty(loginBean.getKol().getName())) {
                                    mTvNameLeft.setText("我");
                                } else {
                                    mTvNameLeft.setText(loginBean.getKol().getName());
                                }
                            } else {
                                mTvNameLeft.setText("我");
                            }
                            inAinmation();
                            myList.add(new PkListItemBean(String.valueOf(0), "影响力分数", right_score));
                            myList.add(new PkListItemBean(String.valueOf(0), "平均转发", right_post));
                            myList.add(new PkListItemBean(String.valueOf(0), "平均互动", right_comment));
                            myList.add(new PkListItemBean(String.valueOf(0), "平均点赞", right_like));
                            myPkListAdapter = new MyPkListAdapter();
                            mListPk.setAdapter(myPkListAdapter);
                        }
                    } else {
                        CustomToast.showShort(SocailPkActivity.this, influenceScoreBean.getMessage());
                        finish();
                    }
                    if (influenceScoreBean.getMessage() != null) {
                        CustomToast.showShort(SocailPkActivity.this, influenceScoreBean.getMessage());
                        finish();
                    }
                }
            }
        });
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }


    class MyPkListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return myList == null ? 0 : myList.size();
        }

        @Override
        public PkListItemBean getItem(int i) {
            return myList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = LayoutInflater.from(SocailPkActivity.this).inflate(R.layout.pk_list_item, null);
                convertView.setTag(new Holder(convertView));
            }
            final Holder holder = (Holder) convertView.getTag();
            PkListItemBean item = getItem(i);
            holder.mTvName.setText(item.getCenterName());
            holder.mTvLeftScore.setText(item.getLeftScore());
            holder.mTvRightScore.setText(item.getRightScore());
            //  holder.proLeft.setProgress(90);
            //holder.proRight.setProgress(22);


            if (begin == true) {
                holder.proLeft.setBegin(true);
                holder.proRight.setBegin(true);
                if (Float.valueOf(myList.get(i).getLeftScore()) > 100) {
                    holder.proLeft.setProgress(Float.valueOf(myList.get(i).getLeftScore()) / 10);
                } else {
                    holder.proLeft.setProgress(Float.valueOf(myList.get(i).getLeftScore()));
                }
                if (Float.valueOf(myList.get(i).getRightScore()) > 100) {
                    holder.proRight.setProgress(Float.valueOf(myList.get(i).getRightScore()) / 10);
                } else {
                    holder.proRight.setProgress(Float.valueOf(myList.get(i).getRightScore()));
                }


            }
            if (over == true) {
                //                holder.proLeft.setProgress(Float.valueOf(myList.get(i).getLeftScore()));
                //                holder.proRight.setProgress(Float.valueOf(myList.get(i).getRightScore()));
                if (Float.valueOf(myList.get(i).getLeftScore()) > 100) {
                    holder.proLeft.setProgress(Float.valueOf(myList.get(i).getLeftScore()) / 10);
                } else {
                    holder.proLeft.setProgress(Float.valueOf(myList.get(i).getLeftScore()));
                }
                if (Float.valueOf(myList.get(i).getRightScore()) > 100) {
                    holder.proRight.setProgress(Float.valueOf(myList.get(i).getRightScore()) / 10);
                } else {
                    holder.proRight.setProgress(Float.valueOf(myList.get(i).getRightScore()));
                }
                if (success == 1) {
                    holder.proRight.setProgressColor(getResources().getColor(R.color.gray_acacac));
                } else if (success == 2) {
                    holder.proLeft.setProgressColor(getResources().getColor(R.color.gray_acacac));
                }
            }
            holder.proLeft.setOnAnimationEndListener(new RoundCornerProgressBar.OnAnimationEndListener() {

                @Override
                public void onAnimationEnd() {
                    holder.proLeft.setBegin(false);
                    holder.proRight.setBegin(false);
                    if (success == 1) {
                        holder.proRight.setProgressColor(getResources().getColor(R.color.gray_acacac));
                    } else if (success == 2) {
                        holder.proLeft.setProgressColor(getResources().getColor(R.color.gray_acacac));
                    }
                }
            });
            return convertView;
        }

    }

    class Holder {

        private TextView mTvLeftScore;
        private TextView mTvRightScore;
        private TextView mTvName;
        private RoundCornerProgressBar proLeft;
        private RoundCornerProgressBar proRight;

        Holder(View view) {
            mTvLeftScore = ((TextView) view.findViewById(R.id.tv_left_score));
            mTvRightScore = ((TextView) view.findViewById(R.id.tv_right_score));
            mTvName = ((TextView) view.findViewById(R.id.tv_center_name));
            proLeft = ((RoundCornerProgressBar) view.findViewById(R.id.pro_left));
            proRight = ((RoundCornerProgressBar) view.findViewById(R.id.pro_right));

        }
    }

    private void showDialog(final Activity activity) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_share, null);
        ImageView weixin = (ImageView) view.findViewById(R.id.img_wechat);
        ImageView wechatRoom = (ImageView) view.findViewById(R.id.img_wechat_room);
        ImageView weibo = (ImageView) view.findViewById(R.id.img_weibo);
        ImageView qq = (ImageView) view.findViewById(R.id.img_qq);
        ImageView qqRoom = (ImageView) view.findViewById(R.id.img_qq_room);
        mCustomDialogManager = new CustomDialogManager(activity, view);
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
        String url_share = TITLE_URL + String.valueOf(id) + "/kol_pk";
        CustomToast.showShort(SocailPkActivity.this, "正在前往分享...");
        ShareSDK.initSDK(SocailPkActivity.this);
        OnekeyShare oks = new OnekeyShare();
        oks.setCallback(new MySharedListener());
        oks.setPlatform(platName);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        if (SinaWeibo.NAME.equals(platName)) {
            oks.setText(SocailPkActivity.this.getResources().getString(R.string.share_pk) + (TITLE_URL + String.valueOf(id)+"/kol_pk"));
        } else {
            oks.setText(SocailPkActivity.this.getResources().getString(R.string.share_pk));
        }
        oks.setTitle(SocailPkActivity.this.getResources().getString(R.string.share_pk));

        oks.setTitleUrl(url_share);
        oks.setImageUrl(IMAGE_URL);
        oks.setUrl(url_share);
        oks.setSite(SocailPkActivity.this.getResources().getString(R.string.app_name));
        oks.setSiteUrl(CommonConfig.SITE_URL);
        oks.show(SocailPkActivity.this);
    }

    private class MySharedListener implements PlatformActionListener {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            CustomToast.showShort(SocailPkActivity.this, "分享成功");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            CustomToast.showShort(SocailPkActivity.this, "分享失败");
        }

        @Override
        public void onCancel(Platform platform, int i) {
            CustomToast.showShort(SocailPkActivity.this, "分享取消");
        }
    }

    public class PkListItemBean {
        private String leftScore;
        private String rightScore;
        private String centerName;

        public PkListItemBean(String leftScore, String centerName, String rightScore) {
            this.leftScore = leftScore;
            this.centerName = centerName;
            this.rightScore = rightScore;

        }

        public String getLeftScore() {
            return leftScore;
        }

        public void setLeftScore(String leftScore) {
            this.leftScore = leftScore;
        }

        public String getRightScore() {
            return rightScore;
        }

        public void setRightScore(String rightScore) {
            this.rightScore = rightScore;
        }

        public String getCenterName() {
            return centerName;
        }

        public void setCenterName(String centerName) {
            this.centerName = centerName;
        }
    }

}
