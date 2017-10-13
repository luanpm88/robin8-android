package com.robin8.rb.module.social.fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.adapter.ViewPagerAdapter;
import com.robin8.rb.base.BaseFragment;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.module.social.bean.InfluenceScoreBean;
import com.robin8.rb.module.social.view.CustomViewPager;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.myprogress.RoundCornerProgressBar;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.view.widget.CircleImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */

public class SocialCardFragment extends BaseFragment implements View.OnClickListener {
    private static final String ARG_PARAM = "TYPE";
    private static final String ARG_PARAM_TWO = "DATA";
    private String[] name = {"微博", "微信"};
    private String type;
    private RelativeLayout layoutShowOne;
    private RelativeLayout layoutShowTwo;
    private RelativeLayout layoutShowThree;
    private ImageView imgShowOne;
    private ImageView imgShowTwo;
    private ImageView imgShowThree;
    private TextView tvCanSee;
    private LinearLayout llOne;
    private LinearLayout llTwo;
    private LinearLayout llThree;
    private LinearLayout llShow;
    private LinearLayout llHide;
    private LinearLayout llWechatHide;
    private TextView tvUserName;
    private TextView tvUserDec;
    private CustomViewPager vp;
    //    private LinearLayoutForListView myListView;
    //private List<SocialDataBean> mDataList;
    private Map<String, Integer> map;
    private Map<String, String> mapCe;
    private TextView tvDataNameOne;
    private TextView tvDataNameTwo;
    private TextView tvDataNameThree;
    private String result;
    private CircleImageView imgUserPhoto;
    private TextView tvRepeatNum;
    private TextView tvCommentNum;
    private TextView tvClickNum;
    private HashIndustry hashIndustry;

    private List<InfluenceScoreBean.IndustriesBean> mDataList;
    private String[] socialTagName;
    private String[] socialTagEnglish;
    private int[] socialTagIcon;
    private TextView tvNumOne;
    private TextView tvNumTwo;
    private TextView tvNumThree;
    private TextView tvRepeatItemOne;
    private TextView tvRepeatItemTwo;
    private TextView tvRepeatItemThree;
    private TextView tvCommentItemOne;
    private TextView tvCommentItemTwo;
    private TextView tvCommentItemThree;
    private TextView tvClickItemOne;
    private TextView tvClickItemTwo;
    private TextView tvClickItemThree;
    private LinearLayout llNoData;
    private LinearLayout llNoDataTwo;
    private LinearLayout llNoDataThree;
    private RoundCornerProgressBar proOne;
    private RoundCornerProgressBar proTwo;
    private RoundCornerProgressBar proThree;
    public static final String myResult = "";
    private LinearLayout llNoBind;
    private LinearLayout llScoreLow;

    public SocialCardFragment() {
    }

    @SuppressLint("ValidFragment")
    public SocialCardFragment(CustomViewPager vp) {
        this.vp = vp;
    }

    public static SocialCardFragment newInstance(int param1, CustomViewPager vp) {
        SocialCardFragment fragment = new SocialCardFragment(vp);
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int anInt = getArguments().getInt(ARG_PARAM);
            type = name[anInt];
        }
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_social_card, null);
        tvUserName = ((TextView) view.findViewById(R.id.tv_user_name));
        tvUserDec = ((TextView) view.findViewById(R.id.tv_user_dec));
        imgUserPhoto = ((CircleImageView) view.findViewById(R.id.img_user_photo));
        tvRepeatNum = ((TextView) view.findViewById(R.id.tv_repeat_num));
        tvCommentNum = ((TextView) view.findViewById(R.id.tv_comment_num));
        tvClickNum = ((TextView) view.findViewById(R.id.tv_click_num));

        tvDataNameOne = ((TextView) view.findViewById(R.id.tv_data_type_one));
        tvDataNameTwo = ((TextView) view.findViewById(R.id.tv_data_type_two));
        tvDataNameThree = ((TextView) view.findViewById(R.id.tv_data_type_three));

        tvNumOne = ((TextView) view.findViewById(R.id.tv_num_one));
        tvNumTwo = ((TextView) view.findViewById(R.id.tv_num_two));
        tvNumThree = ((TextView) view.findViewById(R.id.tv_num_three));

        tvRepeatItemOne = ((TextView) view.findViewById(R.id.tv_repeat_num_item_one));
        tvRepeatItemTwo = ((TextView) view.findViewById(R.id.tv_repeat_num_item_two));
        tvRepeatItemThree = ((TextView) view.findViewById(R.id.tv_repeat_num_item_three));

        tvCommentItemOne = ((TextView) view.findViewById(R.id.tv_comment_num_item_one));
        tvCommentItemTwo = ((TextView) view.findViewById(R.id.tv_comment_num_item_two));
        tvCommentItemThree = ((TextView) view.findViewById(R.id.tv_comment_num_item_three));

        tvClickItemOne = ((TextView) view.findViewById(R.id.tv_click_num_item_one));
        tvClickItemTwo = ((TextView) view.findViewById(R.id.tv_click_num_item_two));
        tvClickItemThree = ((TextView) view.findViewById(R.id.tv_click_num_item_three));

        proOne = ((RoundCornerProgressBar) view.findViewById(R.id.pro_one));
        proTwo = ((RoundCornerProgressBar) view.findViewById(R.id.pro_two));
        proThree = ((RoundCornerProgressBar) view.findViewById(R.id.pro_three));

        imgShowOne = (ImageView) view.findViewById(R.id.img_show_one);
        imgShowTwo = (ImageView) view.findViewById(R.id.img_show_two);
        imgShowThree = (ImageView) view.findViewById(R.id.img_show_three);

        layoutShowOne = ((RelativeLayout) view.findViewById(R.id.show_layout_one));
        layoutShowTwo = ((RelativeLayout) view.findViewById(R.id.show_layout_two));
        layoutShowThree = ((RelativeLayout) view.findViewById(R.id.show_layout_three));

        llOne = ((LinearLayout) view.findViewById(R.id.ll_item_one));
        llTwo = ((LinearLayout) view.findViewById(R.id.ll_item_two));
        llThree = ((LinearLayout) view.findViewById(R.id.ll_item_three));

        llShow = ((LinearLayout) view.findViewById(R.id.ll_can_see));
        llHide = ((LinearLayout) view.findViewById(R.id.ll_no_see));//设置权限不能查看的界面
        llWechatHide = ((LinearLayout) view.findViewById(R.id.ll_wechat_no_see));//微信敬请期待
        tvCanSee = ((TextView) view.findViewById(R.id.tv_can_see));//按钮对他人可见
        llNoData = ((LinearLayout) view.findViewById(R.id.ll_data));
        llNoDataTwo = ((LinearLayout) view.findViewById(R.id.ll_data_two));
        llNoDataThree = ((LinearLayout) view.findViewById(R.id.ll_data_three));

        llScoreLow = ((LinearLayout) view.findViewById(R.id.ll_score_low));//影响力没数据

        llNoBind = ((LinearLayout) view.findViewById(R.id.ll_no_bind));
        ((TextView) view.findViewById(R.id.tv_to_bind)).setOnClickListener(this);

        hashIndustry = new HashIndustry();
        mDataList = new ArrayList<>();

        tvCanSee.setOnClickListener(this);
        llOne.setOnClickListener(this);
        llTwo.setOnClickListener(this);
        llThree.setOnClickListener(this);
        //
        if (type.equals(mActivity.getString(R.string.weibo))) {
            llWechatHide.setVisibility(View.GONE);
            llShow.setVisibility(View.VISIBLE);
            llHide.setVisibility(View.GONE);
            tvCanSee.setVisibility(View.VISIBLE);
//            if (!TextUtils.isEmpty(HelpTools.getCommonXml(HelpTools.PagerData))){
//                myData(HelpTools.getCommonXml(HelpTools.PagerData),0);
//            }
            vp.setObjectForPosition(view, 0);
        } else {
            tvCanSee.setVisibility(View.GONE);
            llWechatHide.setVisibility(View.VISIBLE);
            tvUserName.setText("我是Robin8");
            tvUserDec.setText("您的个人数据管理平台");
            vp.setObjectForPosition(view, 1);
        }
        //vp.resetHeight(0);
      //  myData(result, 2);

        return view;
    }
    public void myData(String result, int should) {
        // super.initData();
       // LogUtil.LogShitou("卡看这个should", "===>" + should);
        if (should == 0) {
            tvCanSee.setVisibility(View.VISIBLE);
        } else{
            tvCanSee.setVisibility(View.GONE);
        }
            map = new HashMap<>();
            mapCe = new HashMap<>();
            socialTagName = mActivity.getResources().getStringArray(R.array.social_tag_name);
            socialTagEnglish = mActivity.getResources().getStringArray(R.array.social_tag_english);
            socialTagIcon = new int[]{R.mipmap.icon_other,R.mipmap.icon_phone,R.mipmap.icon_dress, R.mipmap.icon_baby,
                    R.mipmap.icon_disport, R.mipmap.icon_tour, R.mipmap.icon_education,
                    R.mipmap.icon_fashion, R.mipmap.icon_game, R.mipmap.icon_house,
                    R.mipmap.icon_wealh, R.mipmap.icon_camera, R.mipmap.icon_appliance,
                    R.mipmap.icon_health, R.mipmap.icon_book, R.mipmap.icon_sports,
                    R.mipmap.icon_air, R.mipmap.icon_home, R.mipmap.icon_car, R.mipmap.icon_hotel, R.mipmap.icon_video,
                    R.mipmap.icon_food, R.mipmap.icon_body_bulid, R.mipmap.icon_music,
                    R.mipmap.icon_consume,R.mipmap.icon_internet};
            for (int i = 0; i < socialTagName.length; i++) {
                mapCe.put(socialTagEnglish[i], socialTagName[i]);
                map.put(socialTagName[i], socialTagIcon[i]);
            }
            //

            if (! TextUtils.isEmpty(result)) {
                InfluenceScoreBean influenceScoreBean = GsonTools.jsonToBean(result, InfluenceScoreBean.class);
                if (influenceScoreBean != null) {
                    if (should == 1) {
                        if (influenceScoreBean.isInfluence_score_visibility()) {
                            llShow.setVisibility(View.VISIBLE);
                            llHide.setVisibility(View.GONE);
                        } else {
                            llShow.setVisibility(View.GONE);
                            llHide.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (influenceScoreBean.isInfluence_score_visibility()) {
                            tvCanSee.setText("对他人可见");
                        } else {
                            tvCanSee.setText("对他人隐藏");
                        }
                    }
                    tvUserName.setText(influenceScoreBean.getName());
                    tvUserDec.setText(influenceScoreBean.getDescription());
                    if (! TextUtils.isEmpty(influenceScoreBean.getAvatar_url())) {
                        BitmapUtil.loadImage(mActivity.getApplicationContext(), influenceScoreBean.getAvatar_url(), imgUserPhoto);
                    } else {
                        imgUserPhoto.setImageResource(R.mipmap.logo_circle);
                    }
                    tvRepeatNum.setText(String.valueOf(influenceScoreBean.getAvg_posts()));
                    tvCommentNum.setText(String.valueOf(influenceScoreBean.getAvg_comments()));
                    tvClickNum.setText(String.valueOf(influenceScoreBean.getAvg_likes()));
                    mDataList = influenceScoreBean.getIndustries();
                    if (influenceScoreBean.getIndustries().size() != 0) {
                        layoutShowOne.setVisibility(View.VISIBLE);
                        llNoData.setVisibility(View.VISIBLE);
                        llScoreLow.setVisibility(View.GONE);
                        if (mDataList.size() >= 2) {
                            Collections.sort(mDataList, hashIndustry);
                            for (int i = 0; i < mDataList.size(); i++) {
                                if (mDataList.size() == 2) {
                                    llNoDataThree.setVisibility(View.GONE);
                                    setIcon(mDataList.get(0).getIndustry_name(), tvDataNameOne);
                                    setIcon(mDataList.get(1).getIndustry_name(), tvDataNameTwo);
                                    tvNumOne.setText(StringUtil.deleteZero(mDataList.get(0).getIndustry_score())+"%");
                                    tvNumTwo.setText(StringUtil.deleteZero(mDataList.get(1).getIndustry_score())+"%");
                                    tvRepeatItemOne.setText(String.valueOf(mDataList.get(0).getAvg_posts()));
                                    tvRepeatItemTwo.setText(String.valueOf(mDataList.get(1).getAvg_posts()));
                                    tvCommentItemOne.setText(String.valueOf(mDataList.get(0).getAvg_comments()));
                                    tvCommentItemTwo.setText(String.valueOf(mDataList.get(1).getAvg_comments()));
                                    tvClickItemOne.setText(String.valueOf(mDataList.get(0).getAvg_likes()));
                                    tvClickItemTwo.setText(String.valueOf(mDataList.get(1).getAvg_likes()));
                                    proOne.setProgress((float) (mDataList.get(0).getIndustry_score()));
                                    proTwo.setProgress((float) (mDataList.get(1).getIndustry_score()));
//                                    proOne.setBegin(true);
//                                    proTwo.setBegin(true);

                                } else {
                                    setIcon(mDataList.get(0).getIndustry_name(), tvDataNameOne);
                                    setIcon(mDataList.get(1).getIndustry_name(), tvDataNameTwo);
                                    setIcon(mDataList.get(2).getIndustry_name(), tvDataNameThree);
                                    tvNumOne.setText(StringUtil.deleteZero(mDataList.get(0).getIndustry_score())+"%");
                                    tvNumTwo.setText(StringUtil.deleteZero(mDataList.get(1).getIndustry_score())+"%");
                                    tvNumThree.setText(StringUtil.deleteZero(mDataList.get(2).getIndustry_score())+"%");
                                    tvRepeatItemOne.setText(String.valueOf(mDataList.get(0).getAvg_posts()));
                                    tvRepeatItemTwo.setText(String.valueOf(mDataList.get(1).getAvg_posts()));
                                    tvRepeatItemThree.setText(String.valueOf(mDataList.get(2).getAvg_posts()));
                                    tvCommentItemOne.setText(String.valueOf(mDataList.get(0).getAvg_comments()));
                                    tvCommentItemTwo.setText(String.valueOf(mDataList.get(1).getAvg_comments()));
                                    tvCommentItemThree.setText(String.valueOf(mDataList.get(2).getAvg_comments()));
                                    tvClickItemOne.setText(String.valueOf(mDataList.get(0).getAvg_likes()));
                                    tvClickItemTwo.setText(String.valueOf(mDataList.get(1).getAvg_likes()));
                                    tvClickItemThree.setText(String.valueOf(mDataList.get(2).getAvg_likes()));
                                    proOne.setProgress((float) (mDataList.get(0).getIndustry_score()));
                                    proTwo.setProgress((float) (mDataList.get(1).getIndustry_score()));
                                    proThree.setProgress((float) (mDataList.get(2).getIndustry_score()));
//                                    proOne.setBegin(true);
//                                    proTwo.setBegin(true);
//                                    proThree.setBegin(true);
                                }
                            }
                        } else if (mDataList.size() == 1) {
                            llNoDataThree.setVisibility(View.GONE);
                            llNoDataTwo.setVisibility(View.GONE);
                            setIcon(mDataList.get(0).getIndustry_name(), tvDataNameOne);
                            tvNumOne.setText(StringUtil.deleteZero(mDataList.get(0).getIndustry_score())+"%");
                            tvRepeatItemOne.setText(String.valueOf(mDataList.get(0).getAvg_posts()));
                            tvCommentItemOne.setText(String.valueOf(mDataList.get(0).getAvg_comments()));
                            tvClickItemOne.setText(String.valueOf(mDataList.get(0).getAvg_likes()));
                            proOne.setProgress((float) (mDataList.get(0).getIndustry_score()));
                           // proOne.setBegin(true);
                        }
                    }else {
                        llNoData.setVisibility(View.GONE);
                        llScoreLow.setVisibility(View.VISIBLE);
                    }
                } else {
                    //数据未取得
                    llNoData.setVisibility(View.GONE);
                }

            } else {
                llShow.setVisibility(View.GONE);
            }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_item_one:
                upOrDown(imgShowOne, layoutShowOne);
                break;
            case R.id.ll_item_two:
                upOrDown(imgShowTwo, layoutShowTwo);
                break;
            case R.id.ll_item_three:
                upOrDown(imgShowThree, layoutShowThree);
                break;
            case R.id.tv_can_see:
                if (tvCanSee.getText().toString().equals("对他人可见")) {
                    hideOrShow(1);
                } else {
                    hideOrShow(0);
                }
                break;
//            case R.id.tv_to_bind:
//
//                break;
        }
    }


    private void hideOrShow(final int i) {
        //0可见，1隐藏
        BasePresenter mBasePresenter = new BasePresenter();
        RequestParams requestParams = new RequestParams();
        if (i == 0) {
            requestParams.put("action", "on");
          //  LogUtil.LogShitou("=====》open","on");
        } else {
            requestParams.put("action", "off");
          //  LogUtil.LogShitou("=====》close","off");
        }

        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.INFLUENCE_SHOW), requestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
              //  LogUtil.LogShitou("显示或者隐藏", "===>" + response);
                if (i == 0) {
                    tvCanSee.setText("对他人可见");
                } else {
                    tvCanSee.setText("对他人隐藏");
                }
            }
        });
    }

    private void setIcon(String type, TextView tv) {
       // LogUtil.LogShitou("这里？？","==="+type);
        String textTagName = mapCe.get(type);
        Drawable drawable = mActivity.getResources().getDrawable(map.get(textTagName));
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv.setCompoundDrawables(drawable, null, null, null);
        tv.setText(textTagName);
//        if (map.keySet().iterator().hasNext()) {
//
//        }
    }

    private void upOrDown(ImageView img, RelativeLayout lyOne) {
        if (lyOne.getVisibility() == View.VISIBLE) {
            lyOne.setVisibility(View.GONE);
            lyOne.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.pop_out));
            img.setImageResource(R.mipmap.icon_data_down);
        } else {
            lyOne.setVisibility(View.VISIBLE);
            lyOne.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.pop_in));
            img.setImageResource(R.mipmap.icon_up);
        }
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
    public void setData(ViewPagerAdapter.SelectItem data, String url, String pageName) {

    }
}
