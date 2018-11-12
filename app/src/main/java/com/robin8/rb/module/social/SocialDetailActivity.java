package com.robin8.rb.module.social;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.module.first.widget.ScrollViewExtend;
import com.robin8.rb.module.social.adapter.TabsFragmentAdapter;
import com.robin8.rb.module.social.bean.InfluenceScoreBean;
import com.robin8.rb.module.social.fragment.SocialCardFragment;
import com.robin8.rb.module.social.view.CustomViewPager;
import com.robin8.rb.module.social.view.EasySlidingTabs;
import com.robin8.rb.module.social.view.ShowDataView;
import com.robin8.rb.module.social.view.StickyNavLayout;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.myprogress.RoundIndicatorView;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.view.widget.CustomDialogManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SocialDetailActivity extends BaseActivity {
    public static final String OTHER_DETAIL_TAG = "SocialDetailActivity";
    private int otherKolId;
    private String url;


    private RoundIndicatorView viewScoreResult;
    private EasySlidingTabs myTabs;
    private CustomViewPager mViewPager;
    private StickyNavLayout stickyNavLayout;
    private List<Fragment> fragments;
    private String[] titles;
    public TabsFragmentAdapter adapter;
    private ScrollViewExtend mScroll;
    private LinearLayout llToShare;
    private CustomDialogManager mCustomDialogManager;

    private static final String IMAGE_URL = CommonConfig.APP_ICON;
    private static final String TITLE_URL = CommonConfig.SERVICE + "/invite?inviter_id=";
    private TextView tvProveSocial;
    private LinearLayout llIndustry;
    private Button btn;
    private RelativeLayout llHaveResult;
    private LinearLayout llNoResult;
    private TextView tvWait;
    private ShowDataView showDataView;

    private SocialCardFragment weiBoFg;
    private SocialCardFragment weiChatFg;
    private InfluenceScoreBean scoreBean;

    @Override
    public void setTitleView() {
        mTVCenter.setText("Ta的影响力");
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_social_detail, mLLContent);
        //  llNoSimilar = ((LinearLayout) view.findViewById(R.id.ll_similar_kol));
        llIndustry = ((LinearLayout) view.findViewById(R.id.ll_industry));
        llHaveResult = ((RelativeLayout) view.findViewById(R.id.layout_have_result));
        llNoResult = ((LinearLayout) view.findViewById(R.id.layout_no_result));
        tvWait = ((TextView) view.findViewById(R.id.tv_wait));
        tvWait.setOnClickListener(this);
        //====社交详情=====//
        viewScoreResult = ((RoundIndicatorView) view.findViewById(R.id.view_score_result));//圆环
        viewScoreResult.setCurrentValues(0);
        viewScoreResult.setTitle("");
        myTabs = ((EasySlidingTabs) view.findViewById(R.id.my_indicator));
        mScroll = ((ScrollViewExtend) view.findViewById(R.id.scroll_extend));
        myTabs.setUnderlineHeight(1);
        mViewPager = ((CustomViewPager) view.findViewById(R.id.my_vp));

        showDataView = ((ShowDataView) view.findViewById(R.id.show_my_data));
        btn = ((Button) view.findViewById(R.id.btn_to_pk));
        btn.setOnClickListener(this);

        //=====
        hashIndustry = new HashIndustry();
        mapCe = new HashMap<>();
        socialTagName = getResources().getStringArray(R.array.social_tag_name);
        socialTagEnglish = getResources().getStringArray(R.array.social_tag_english);
        for (int i = 0; i < socialTagEnglish.length; i++) {
            mapCe.put(socialTagEnglish[i], socialTagName[i]);
        }
        //=====
        fragments = new LinkedList<>();
        titles = new String[]{"微博", "微信"};
        weiBoFg = SocialCardFragment.newInstance(0, mViewPager);
        weiChatFg = SocialCardFragment.newInstance(1, mViewPager);
        fragments.add(weiBoFg);
        fragments.add(weiChatFg);
        this.adapter = new TabsFragmentAdapter(getSupportFragmentManager(), titles, this.fragments);
        mViewPager.setAdapter(adapter);
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
        initData();
        mViewPager.resetHeight(0);

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                showDataView.setStr(s);
                showDataView.setValue(valueList);
            } else if (msg.what == 1) {
                showDataView.setStr(s);
                showDataView.setValue(resultList);
            }

        }
    };

    private void initData() {
        Intent intent = getIntent();
        otherKolId = intent.getIntExtra(OTHER_DETAIL_TAG, 0);
      //  LogUtil.LogShitou("查看他人影响力", "===========>>>>" + otherKolId);
        url = HelpTools.getUrl(CommonConfig.INFLUENCE_OTHER + otherKolId + "/similar_kol_details");
        loadData();
    }

    private boolean again = false;
    private boolean overTime = false;
    private int count = 0;

    private void loadData() {
        BasePresenter mBasePresenter = new BasePresenter();
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, url, null, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                CustomToast.showShort(getApplicationContext(), "网络加载失败");
                isHaveResult(false);
                tvWait.setText("加载失败，请点击重试");
            }

            @Override
            public void onResponse(String response) {
              //  LogUtil.LogShitou("他人的影响力", "==========================>" + response);
                scoreBean = GsonTools.jsonToBean(response, InfluenceScoreBean.class);
                if (scoreBean != null) {
                    if (scoreBean.getError() == 0) {
                        if (scoreBean.isCalculated()) {
                            isHaveResult(true);
                            weiBoFg.myData(response, 1);
                            //获取数据
                            postDatas(scoreBean);
                        } else {
                            //获得倒计时
                            if (count > 3) {
                                overTime = true;
                            } else {
                                overTime = false;
                            }
                            isHaveResult(false);
                            tvWait.setText("这位KOL还未测试影响力哦～");
                            llHaveResult.setVisibility(View.INVISIBLE);
                            llNoResult.setVisibility(View.VISIBLE);
                            if (overTime == false) {
                                runnable = new Runnable() {

                                    @Override
                                    public void run() {
                                        count++;
                                        again = true;
                                        loadData();

                                    }
                                };
                                handlerTwo.postDelayed(runnable, 5000);
                            }

                        }
                    } else if (scoreBean.getError() == 1) {
                        isHaveResult(false);
                        weiBoFg.myData(null, 1);
                        tvWait.setText("引擎出现故障，程序哥哥正在疯狂修复~");
                    } else {
                        weiBoFg.myData(null, 1);
                        isHaveResult(false);
                        tvWait.setText("引擎出现故障，程序哥哥正在疯狂修复~");
                        //  CustomToast.showShort(SocialDetailActivity.this, scoreBean.getMessage());
                    }

                } else {
                    weiBoFg.myData(null, 1);
                    isHaveResult(false);
                    tvWait.setText("引擎出现故障，程序哥哥正在疯狂修复~");
                    // CustomToast.showShort(SocialDetailActivity.this, "请检查网络连接");
                }
            }
        });
    }

    private Handler handlerTwo = new Handler();
    private Runnable runnable;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerTwo.removeCallbacks(runnable);
    }

    private void isHaveResult(boolean is) {
        if (is) {
            //页面有数据
            if (again) {
                llNoResult.setAnimation(AnimationUtils.loadAnimation(SocialDetailActivity.this, R.anim.anim_alpha_out));
                llNoResult.setVisibility(View.GONE);
                llHaveResult.setAnimation(AnimationUtils.loadAnimation(SocialDetailActivity.this, R.anim.anim_alpha_in));
                llHaveResult.setVisibility(View.VISIBLE);
                btn.setAnimation(AnimationUtils.loadAnimation(SocialDetailActivity.this, R.anim.anim_alpha_in));
                btn.setVisibility(View.VISIBLE);
            } else {
                llHaveResult.setVisibility(View.VISIBLE);
                btn.setVisibility(View.VISIBLE);
                llNoResult.setVisibility(View.GONE);
            }

        } else {
            llHaveResult.setVisibility(View.GONE);
            btn.setVisibility(View.GONE);
            llNoResult.setVisibility(View.VISIBLE);
        }
    }

    private String[] socialTagName;
    private String[] socialTagEnglish;
    private Map<String, String> mapCe;
    private List<String> need;
    private List<String> real;
    private List<Float> resultList;
    private List<Float> valueList;
    private List<InfluenceScoreBean.IndustriesBean> corList;
    private HashIndustry hashIndustry;
    private String[] s;

    private void postDatas(InfluenceScoreBean influenceScoreBean) {
        need = new ArrayList<>();
        real = new ArrayList<>();
        resultList = new ArrayList<>();
        valueList = new ArrayList<>();
        s = new String[8];
        viewScoreResult.setCurrentValues(Float.valueOf(String.valueOf(influenceScoreBean.getInfluence_score())));
        viewScoreResult.setTitle(influenceScoreBean.getInfluence_level());
        if (influenceScoreBean.getIndustries().size() == 0) {
            llIndustry.setVisibility(View.GONE);
        } else {
            //            for (int i = 0; i < 8; i++) {
            //                s[i] = socialTagName[i];
            //                valueList.add(7f);
            //            }
            //            showDataView.setStr(s);
            //           showDataView.setValue(valueList);
            need.addAll(Arrays.asList(socialTagEnglish));
            int size = influenceScoreBean.getIndustries().size();
            corList = influenceScoreBean.getIndustries();
            if (size < 8) {
                //兴趣不足8个，从全部的兴趣标签中移除已有的兴趣
                for (int i = 0; i < size; i++) {
                    real.add(influenceScoreBean.getIndustries().get(i).getIndustry_name());
                    valueList.add(Float.valueOf(String.valueOf(influenceScoreBean.getIndustries().get(i).getIndustry_score())));
                }
                need.removeAll(real);
                for (int i = 0; i < 8 - size; i++) {
                    //差几个标签就随机选取几个标签
                    real.add(need.get(i));
                    valueList.add(0f);
                }
                for (int i = 0; i < real.size(); i++) {
                    s[i] = mapCe.get(real.get(i));
                }
                handler.sendEmptyMessage(0);
            } else if (size == 8) {
                for (int i = 0; i < size; i++) {
                    real.add(influenceScoreBean.getIndustries().get(i).getIndustry_name());
                    valueList.add(Float.valueOf(String.valueOf(influenceScoreBean.getIndustries().get(i).getIndustry_score())));
                }
                for (int i = 0; i < real.size(); i++) {
                    s[i] = mapCe.get(real.get(i));
                }
                handler.sendEmptyMessage(0);
            } else {
                //大于8个就先排序
                //   Collections.sort(corList, hashIndustry);
                for (int i = 0; i < 8; i++) {
                    s[i] = mapCe.get(corList.get(i).getIndustry_name());
                    resultList.add(Float.valueOf(String.valueOf(corList.get(i).getIndustry_score())));
                }
                handler.sendEmptyMessage(1);
            }
        }
    }

    private boolean isLogined() {
        BaseApplication baseApplication = BaseApplication.getInstance();
        return baseApplication.hasLogined();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_to_pk:
                Intent intent = new Intent(SocialDetailActivity.this, SocailPkActivity.class);
                intent.putExtra("right_score", String.valueOf(scoreBean.getInfluence_score()));
                intent.putExtra("right_post", String.valueOf(scoreBean.getAvg_posts()));
                intent.putExtra("right_comment", String.valueOf(scoreBean.getAvg_comments()));
                intent.putExtra("right_like", String.valueOf(scoreBean.getAvg_likes()));
                intent.putExtra("right_name", scoreBean.getName());
                intent.putExtra("right_imgurl", scoreBean.getAvatar_url());
                startActivity(intent);
                break;
            case R.id.tv_wait:
                initData();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }


    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }

    //排序
    private class HashIndustry implements Comparator<InfluenceScoreBean.IndustriesBean> {

        @Override
        public int compare(InfluenceScoreBean.IndustriesBean t1, InfluenceScoreBean.IndustriesBean t2) {
            if (t1.getIndustry_score() < t2.getIndustry_score()) {
                return 1;
            } else if (t1.getIndustry_score() > t2.getIndustry_score()) {
                return - 1;
            } else {
                return 0;
            }
        }
    }
    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.OTHER_INFLUENCE;
        super.onResume();
    }
}
