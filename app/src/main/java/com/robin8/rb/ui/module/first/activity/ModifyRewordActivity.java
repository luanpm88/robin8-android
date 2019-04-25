package com.robin8.rb.ui.module.first.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.extras.wheel.TimePickerView;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.ui.model.LaunchRewordModel;
import com.robin8.rb.ui.model.NotifyMsgEntity;
import com.robin8.rb.ui.module.mine.activity.CampaignChoseTypeActivity;
import com.robin8.rb.ui.module.reword.activity.DetailContentActivity;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.citylist.ContactItemInterface;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.DateUtil;
import com.robin8.rb.util.FileUtils;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.RegExpUtil;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.util.UIUtils;
import com.robin8.rb.ui.dialog.CustomDialogManager;
import com.robin8.rb.ui.dialog.LaunchRewordDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Observable;

import static com.robin8.rb.R.string.wechat;

/**
 发起悬赏活动页面 */
public class ModifyRewordActivity extends BaseActivity implements View.OnClickListener {

    public static final int IDLE = - 1;
    private static final int REJECT = 1;
    private static final String UN_PAY = "unpay";
    private static final int ALL = 0;
    private static final int MALE = 1;
    private static final int FEMALE = 2;

    private String[] agePostArr = {"全部", "(0,20)", "(20,40)", "(40,60)", "(60,100)"};
    private String[] ageArr = {"全部", "0-20", "20-40", "40-60", "60-100"};
    private EditText mETTitle;// 活动标题
    private TextView mTVTitleNum;
    private EditText mETIntroduce;// 活动简介
    private TextView mTVIntroduceNum;
    private View mLLPostImage;
    private ImageView mIVPost;// 图片
    private EditText mETAddress;// 活动链接
    private EditText mETConsume4;// 活动总预算
    private EditText mETConsume5;// 单个点击费用
    private TextView mTVInfoSubType;// 活动平台
    private TextView mTVInfo1;// 活动开始时间
    private TextView mTVInfo2;// 活动结束时间
    private TextView mTVInfo3;// 选择活动类型
    private View mSVContent;
    private View mLLBottom;
    private boolean hasTranslateB;
    private TimePickerView pvTime;
    private ArrayList<String> mDateList = new ArrayList<String>();
    //  private ArrayList<String> mConsumeWayList = new ArrayList<String>();
    private ArrayList<String> mConsumeWayList;
    private LinearLayout mLLWheel;
    private TimePickerView.Type mClickType = TimePickerView.Type.OTHER;
    private View mTVPreview;
    private View mTVSubmit;
    private String mFinalPicturePath;
    private int mCurrenetItem = IDLE;
    private DateBean mStartDateBean;
    private DateBean mEndDateBean;
    private View mView;
    private boolean mImageLoadB;
    private boolean mInfoCompelete;
    private String mCountType;
    private LaunchRewordDialog mLaunchRewordDialog;
    private String[] mArrayTitle;
    private TextView mTvTitle5;
    private Uri mImageUri;
    private int mFrom = 0;
    private LaunchRewordModel.Campaign mModifyCampaign;
    private Bitmap mBitmap;
    private TextView mTVTitle4;
    private TextView mTVInfoAge;
    private TextView mTVInfoSex;
    private TextView mTVInfoClassify;
    private TextView mTVInfoCity;
    List<TagsItem> mGridData = new ArrayList<>();
    private ViewHolder holder;
    private MyGridAdapter mMyGridAdapter;
    private List<ContactItemInterface> contactList;
    private int mAgeType;
   // private String stChoseType;

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.ADVERTISER_ADD;
        super.onResume();
    }

    @Override
    public void setTitleView() {
        mTVCenter.setText(this.getText(R.string.launch_reword));
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_launch_reword, mLLContent, true);
        mSVContent = findViewById(R.id.sv_content);
        mETTitle = (EditText) findViewById(R.id.et_title);
        mTVTitleNum = (TextView) findViewById(R.id.tv_title_num);
        mETIntroduce = (EditText) findViewById(R.id.et_introduce);
        mTVIntroduceNum = (TextView) findViewById(R.id.tv_introduce_num);
        mLLPostImage = findViewById(R.id.ll_post_image);
        mIVPost = (ImageView) findViewById(R.id.iv_post);
        mETAddress = (EditText) findViewById(R.id.et_address);
        mLLBottom = findViewById(R.id.ll_bottom);
        mView = findViewById(R.id.view);
        mLLWheel = (LinearLayout) findViewById(R.id.ll_wheel);

        mTVPreview = findViewById(R.id.tv_preview);
        mTVSubmit = findViewById(R.id.tv_submit);

        mLLPostImage.setOnClickListener(this);
        mTVPreview.setOnClickListener(this);
        mTVSubmit.setOnClickListener(this);
        initItem();
        initAddItem();
        initEditText();
        prepareData();
        updateViewWhenFromReject();
    }

    private void updateViewWhenFromReject() {
        //api/v1_4/kol_campaigns/detail 传过来数据
        Intent intent = getIntent();
        Object obj = intent.getSerializableExtra("campaign");
        mModifyCampaign = null;
        if (obj instanceof LaunchRewordModel.Campaign) {
            mModifyCampaign = (LaunchRewordModel.Campaign) obj;
        }

        if (mModifyCampaign == null) {
            return;
        }
        mETTitle.setText(mModifyCampaign.getName());
        if (TextUtils.isEmpty(mModifyCampaign.getSub_type())){
            mTVInfoSubType.setText("");
        }else {
            if (mModifyCampaign.getSub_type().equals("weibo")){
                mTVInfoSubType.setText(getString(R.string.weibo));
            }else if (mModifyCampaign.getSub_type().equals("wechat,weibo")){
                mTVInfoSubType.setText(getString( R.string.weixin)+getString(R.string.wechat)+","+getString(R.string.weibo));
            }else {
                mTVInfoSubType.setText(getString( R.string.weixin)+getString(R.string.wechat));

            }

        }
        mETIntroduce.setText(mModifyCampaign.getDescription());
        BitmapUtil.loadImage(this.getApplicationContext(), mModifyCampaign.getImg_url(), mIVPost);
        mETAddress.setText(mModifyCampaign.getUrl());
        mTVInfo1.setText(DateUtil.formatTime("yyyy-MM-dd'T'HH:mm:ssZ", mModifyCampaign.getStart_time()));
        mTVInfo2.setText(DateUtil.formatTime("yyyy-MM-dd'T'HH:mm:ssZ", mModifyCampaign.getDeadline()));
        if ("click".equals(mModifyCampaign.getPer_budget_type())) {
            mTVInfo3.setText(getResources().getString(R.string.type_count_by_click));
            mTvTitle5.setText(mArrayTitle[4]);
            mETConsume5.setHint(getResources().getString(R.string.min_2));
        } else if ("post".equals(mModifyCampaign.getPer_budget_type())) {
            mTVInfo3.setText(getResources().getString(R.string.type_count_by_kol));
            mTvTitle5.setText(mArrayTitle[5]);
            mETConsume5.setHint(getResources().getString(R.string.min_20));
        } else if ("simple_cpi".equals(mModifyCampaign.getPer_budget_type())) {
            mTVInfo3.setText(getResources().getString(R.string.type_download_by_kol));
            mTvTitle5.setText(mArrayTitle[6]);
            mETConsume5.setHint(getResources().getString(R.string.min_3));
        } else {
            mTVInfo3.setText(getResources().getString(R.string.type_task_by_kol));
            mTvTitle5.setText(mArrayTitle[7]);
            mETConsume5.setHint(getResources().getString(R.string.min_1));
        }

        if (! UN_PAY.equals(mModifyCampaign.getStatus())) {
            mETConsume4.setEnabled(false);
            mTVTitle4.setTextColor(UIUtils.getColor(R.color.sub_gray_custom));
            mETConsume4.setTextColor(UIUtils.getColor(R.color.sub_gray_custom));
        }

        mETConsume4.setText(StringUtil.deleteZero(mModifyCampaign.getBudget()));
        mETConsume5.setText(StringUtil.deleteZero(mModifyCampaign.getPer_action_budget()));
        mLLBottom.setBackgroundResource(R.color.blue_custom);

        mTVInfoAge.setText(getDisPlay(mModifyCampaign.getAge()));
        if ("0".equals(mModifyCampaign.getGender())) {
            mTVInfoSex.setText("全部");

        } else if ("1".equals(mModifyCampaign.getGender())) {
            mTVInfoSex.setText("男");
        } else if ("2".equals(mModifyCampaign.getGender())) {
            mTVInfoSex.setText("女");
        } else {
            mTVInfoSex.setText(mModifyCampaign.getGender());
        }
        mTVInfoCity.setText(mModifyCampaign.getRegion());
        mTVInfoClassify.setText(mModifyCampaign.getTag_labels());

        mImageLoadB = true;
        mFrom = REJECT;
        mFinalPicturePath = mModifyCampaign.getImg_url();
    }

    private String getDisPlay(String age) {
        for (int i = 0; i < agePostArr.length; i++) {
            if (! TextUtils.isEmpty(age) && age.equals(agePostArr[i])) {
                return ageArr[i];
            }
        }
        return null;
    }

    /**
     设置编辑框
     */
    private void initEditText() {

        EditTextWatcher editTextWatcher = new EditTextWatcher();
        mETAddress.addTextChangedListener(editTextWatcher);
        mETConsume4.addTextChangedListener(editTextWatcher);
        mETConsume5.addTextChangedListener(editTextWatcher);

        mETTitle.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null) {
                    mTVTitleNum.setText(String.valueOf(s.length()) + "/22");
                    setBottomView();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mETIntroduce.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null) {
                    mTVIntroduceNum.setText(String.valueOf(s.length()) + "/140");
                    setBottomView();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mLaunchRewordDialog != null) {
            mLaunchRewordDialog = null;
        }
        if (! TextUtils.isEmpty(mFinalPicturePath) && ! mFinalPicturePath.startsWith("http://")) {
            BitmapUtil.deleteBm(mFinalPicturePath);
        }
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
        NotifyManager.getNotifyManager().deleteObserver(this);
        super.onDestroy();
    }

    /**
     检查信息是否完整
     @param showToast
     @return
     */
    private boolean checkInfoCompelete(boolean showToast) {
        if (TextUtils.isEmpty(mTVInfo3.getText().toString())) {
            if (showToast) {
                CustomToast.showShort(this, "请选择活动类型");
            }
            return false;
        } else {
            if (getString(R.string.type_count_by_click).equals(mTVInfo3.getText().toString())) {
                mCountType = "click";
            } else if (getString(R.string.type_count_by_kol).equals(mTVInfo3.getText().toString())) {
                mCountType = "post";
            } else if (getString(R.string.type_download_by_kol).equals(mTVInfo3.getText().toString())) {
                mCountType = "simple_cpi";
            } else {//任务
                mCountType = "cpt";
            }
        }

        if (TextUtils.isEmpty(mETTitle.getText().toString())) {
            if (showToast) {
                CustomToast.showShort(this, "请填写活动标题");
            }
            return false;
        }

        if (TextUtils.isEmpty(mETIntroduce.getText().toString())) {
            if (showToast) {
                CustomToast.showShort(this, "请填写活动简介");
            }
            return false;
        }

        if (! mImageLoadB) {
            if (showToast) {
                CustomToast.showShort(this, "请上传封面图片");
            }
            return false;
        }

        if (TextUtils.isEmpty(mETAddress.getText().toString())) {
            if (showToast) {
                CustomToast.showShort(this, "请填写活动链接");
            }
            return false;
        } else if (! RegExpUtil.isUrl(mETAddress.getText().toString())) {
            if (showToast) {
                CustomToast.showShort(this, "活动链接格式不正确");
                return false;
            }
        }

        if (TextUtils.isEmpty(mETConsume4.getText().toString())) {
            if (showToast) {
                CustomToast.showShort(this, "请填写活动总预算");
            }
            return false;
        } else {
            //            String totalComsuneStr = mETConsume4.getText().toString();
            //            try {
            //                float totalComsuneF = Float.parseFloat(totalComsuneStr);
            //                if (totalComsuneF < 100) {
            //                    if (showToast) {
            //                        CustomToast.showShort(this, "总预算最低100元");
            //                    }
            //                    return false;
            //                }
            //            } catch (Exception e) {
            //                if (showToast) {
            //                    CustomToast.showShort(this, "总预算金额格式错误");
            //                }
            //                return false;
            //            }
        }

        if (TextUtils.isEmpty(mETConsume5.getText().toString())) {
            if (showToast) {
                CustomToast.showShort(this, "请填写单个费用");
            }
            return false;
        } else {
            String oneComsuneStr = mETConsume5.getText().toString();
            try {
                float oneComsuneF = Float.parseFloat(oneComsuneStr);
                if (oneComsuneF < 0.2) {
                    if (showToast) {
                        CustomToast.showShort(this, "单个点击费用最低0.2元");
                    }
                    return false;
                }
            } catch (Exception e) {
                if (showToast) {
                    CustomToast.showShort(this, "单个点击费用格式错误");
                }
                return false;
            }
        }
        return true;
    }

    private void initAddItem() {
        View layoutAge = findViewById(R.id.layout_age);
        TextView tvTitleAge = (TextView) layoutAge.findViewById(R.id.tv_title);
        mTVInfoAge = (TextView) layoutAge.findViewById(R.id.tv_info);

        View layoutSex = findViewById(R.id.layout_sex);
        TextView tvTitleSex = (TextView) layoutSex.findViewById(R.id.tv_title);
        mTVInfoSex = (TextView) layoutSex.findViewById(R.id.tv_info);

        View layoutClassify = findViewById(R.id.layout_classify);
        TextView tvTitleClassify = (TextView) layoutClassify.findViewById(R.id.tv_title);
        mTVInfoClassify = (TextView) layoutClassify.findViewById(R.id.tv_info);

        View layoutCity = findViewById(R.id.layout_city);
        TextView tvTitleCity = (TextView) layoutCity.findViewById(R.id.tv_title);
        mTVInfoCity = (TextView) layoutCity.findViewById(R.id.tv_info);

        tvTitleAge.setText(R.string.kol_age);
        tvTitleSex.setText(R.string.kol_sex);
        tvTitleClassify.setText(R.string.kol_classify);
        tvTitleCity.setText(R.string.kol_city);

        mTVInfoAge.setText(R.string.all);
        mTVInfoSex.setText(R.string.all);
        mTVInfoClassify.setText(R.string.all);
        mTVInfoCity.setText(R.string.all);

        layoutAge.setOnClickListener(this);
        layoutSex.setOnClickListener(this);
        layoutClassify.setOnClickListener(this);
        layoutCity.setOnClickListener(this);
    }

    private void initItem() {

        mArrayTitle = getResources().getStringArray(R.array.launch_reword_item);

        //选择活动的发布平台
        View layoutSubType = findViewById(R.id.layout_sub_type);
        TextView tvTitleSubType = (TextView) layoutSubType.findViewById(R.id.tv_title);
        mTVInfoSubType = (TextView) layoutSubType.findViewById(R.id.tv_info);
        mTVInfoSubType.setText("");
        layoutSubType.setOnClickListener(this);

        View layoutStartTime = findViewById(R.id.layout_start_time);
        TextView tvTitle1 = (TextView) layoutStartTime.findViewById(R.id.tv_title);
        mTVInfo1 = (TextView) layoutStartTime.findViewById(R.id.tv_info);

        View layoutEndTime = findViewById(R.id.layout_end_time);
        TextView tvTitle2 = (TextView) layoutEndTime.findViewById(R.id.tv_title);
        mTVInfo2 = (TextView) layoutEndTime.findViewById(R.id.tv_info);

        View layoutActivityType = findViewById(R.id.layout_activity_type);
        TextView tvTitle3 = (TextView) layoutActivityType.findViewById(R.id.tv_title);
        mTVInfo3 = (TextView) layoutActivityType.findViewById(R.id.tv_info);

        View layoutActivityConsume = findViewById(R.id.layout_activity_consume);
        mTVTitle4 = (TextView) layoutActivityConsume.findViewById(R.id.tv_title);
        mETConsume4 = (EditText) layoutActivityConsume.findViewById(R.id.et_consume);

        View layoutPerConsume = findViewById(R.id.layout_per_consume);
        mTvTitle5 = (TextView) layoutPerConsume.findViewById(R.id.tv_title);
        mETConsume5 = (EditText) layoutPerConsume.findViewById(R.id.et_consume);

        tvTitleSubType.setText("推广平台选择");
        tvTitle1.setText(mArrayTitle[0]);
        tvTitle2.setText(mArrayTitle[1]);
        tvTitle3.setText(mArrayTitle[2]);
        mTVTitle4.setText(mArrayTitle[3]);
        mTvTitle5.setText(mArrayTitle[4]);

        mTVInfo1.setText(DateUtil.getFormatTime(System.currentTimeMillis() + 2 * 60 * 60 * 1000, SPConstants.YY_MM_DD_HH_MM));
        mTVInfo2.setText(DateUtil.getFormatTime(System.currentTimeMillis() + 26 * 60 * 60 * 1000, SPConstants.YY_MM_DD_HH_MM));
        mTVInfo3.setText(getResources().getString(R.string.type_count_by_click));
        //        mETConsume4.setHint(getResources().getString(R.string.min_100));
        mETConsume5.setHint(getResources().getString(R.string.min_2));

        mTVInfo1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                translateY(TimePickerView.ACTIVITY_START, TimePickerView.Type.DATE_HOUR_MONTH);
            }
        });

        mTVInfo2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                translateY(TimePickerView.ACTIVITY_END, TimePickerView.Type.DATE_HOUR_MONTH);
            }
        });

        mTVInfo3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // translateY(TimePickerView.ACTIVITY_TYPE, TimePickerView.Type.CONSUME_WAY);
                if (TextUtils.isEmpty((mTVInfoSubType.getText().toString()))) {
                    CustomToast.showShort(ModifyRewordActivity.this, "请先选择推广平台");
                } else {
                    if ((mTVInfoSubType.getText().toString()).equals((getString(R.string.weixin) + getString(wechat) + "," + getString(R.string.weibo))) || (mTVInfoSubType.getText().toString()).equals(getString(R.string.weibo))) {
                        //微信微博同时选择／选择微博，活动类型只有三个
                        translateY(TimePickerView.ACTIVITY_TYPE_NO_CLICK, TimePickerView.Type.CONSUME_WAY);
                    } else {
                        translateY(TimePickerView.ACTIVITY_TYPE, TimePickerView.Type.CONSUME_WAY);
                    }
                }
            }
        });

        mETConsume4.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (mCurrenetItem != IDLE) {
                        mCurrenetItem = IDLE;
                        if (pvTime != null) {
                            pvTime.callBack(true);
                        }
                    }
                    onCompelete();
                }
                return false;
            }
        });

        mETConsume5.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (mCurrenetItem != IDLE) {
                        mCurrenetItem = IDLE;
                        if (pvTime != null) {
                            pvTime.callBack(true);
                        }
                    }
                    onCompelete();
                }
                return false;
            }
        });

    }

    /**
     显示wheelView
     */
    private void translateY(int itemId, TimePickerView.Type type) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mLLWheel.getWindowToken(), 0); //强制隐藏键盘

        if (mCurrenetItem != itemId && pvTime != null) {
            pvTime.callBack(false);
        }
        mCurrenetItem = itemId;
        mClickType = type;

        animate();
        initWheel(mClickType);
    }

    private void animate() {
        if (! hasTranslateB) {
            ObjectAnimator animation = ObjectAnimator.ofFloat(mSVContent, "translationY", 0f, - UIUtils.getDimens(R.dimen.launch_reword_translate));
            animation.setDuration(400);
            animation.start();

            mLLWheel.setVisibility(View.VISIBLE);
            ObjectAnimator animation2 = ObjectAnimator.ofFloat(mLLWheel, "translationY", UIUtils.getDimens(R.dimen.launch_reword_translate), 0f);
            animation2.setDuration(400);
            animation2.start();
            mView.setVisibility(View.GONE);
            mLLBottom.setVisibility(View.GONE);
            hasTranslateB = true;
        }
    }

    private void initWheel(TimePickerView.Type type) {

        if (mStartDateBean == null) {
            mStartDateBean = new DateBean();
        }
        if (mEndDateBean == null) {
            mEndDateBean = new DateBean();
        }

        if (type == TimePickerView.Type.OTHER) {
            return;
        }

        if (pvTime == null) {
            pvTime = new TimePickerView(this, mConsumeWayList, mDateList, type, mLLWheel, mCurrenetItem);
        } else {
            switch (mCurrenetItem) {
                case TimePickerView.ACTIVITY_START:
                    pvTime.setSelectItem(mStartDateBean.day, mStartDateBean.hour, mStartDateBean.minute, mCurrenetItem);
                    break;
                case TimePickerView.ACTIVITY_END:
                    pvTime.setSelectItem(mEndDateBean.day, mEndDateBean.hour, mEndDateBean.minute, mCurrenetItem);
                    break;
            }
        }
        pvTime.setType(type);
        pvTime.setCyclic(true, false);
        pvTime.setBelongItem(mCurrenetItem);

        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(String str, int day, int hour, int minute, boolean isMust) {
                String[] backStr = str.split("/");
                switch (mCurrenetItem) {
                    case TimePickerView.ACTIVITY_START:
                        mTVInfo1.setText(backStr[0]);
                        mStartDateBean.day = day;
                        mStartDateBean.hour = hour;
                        mStartDateBean.minute = minute;
                        break;
                    case TimePickerView.ACTIVITY_END:
                        mTVInfo2.setText(backStr[0]);
                        mEndDateBean.day = day;
                        mEndDateBean.hour = hour;
                        mEndDateBean.minute = minute;
                        break;
                    case TimePickerView.ACTIVITY_TYPE:
                    case TimePickerView.ACTIVITY_TYPE_NO_CLICK:
                        mTVInfo3.setText(backStr[1]);
                        if (backStr[1].equals(getString(R.string.type_count_by_click))) {//点击
                            mETConsume5.setHint(getString(R.string.min_2));
                            mTvTitle5.setText(mArrayTitle[4]);
                        } else if (backStr[1].equals(getString(R.string.type_count_by_kol))) {//转发
                            mETConsume5.setHint(getString(R.string.min_20));
                            mTvTitle5.setText(mArrayTitle[5]);
                        } else if (backStr[1].equals(getString(R.string.type_download_by_kol))) {//下载
                            mETConsume5.setHint(getString(R.string.min_3));
                            mTvTitle5.setText(mArrayTitle[6]);
                        } else {//任务
                            mETConsume5.setHint(getString(R.string.min_1));
                            mTvTitle5.setText(mArrayTitle[7]);
                        }
                        break;
                }
                if (isMust) {
                    onCompelete();
                }
            }
        });

    }

    private List<String> tempList;

    public void prepareData() {
        //        String[] consumeWay = getResources().getStringArray(R.array.launch_reword_consume_way);
        //        List<String> tempList = Arrays.asList(consumeWay);
        //根据所选平台更换选择类型的数据
        if (TextUtils.isEmpty((mTVInfoSubType.getText().toString()))) {
            //初始
            tempList = Arrays.asList(getResources().getStringArray(R.array.launch_reword_consume_way));
        } else {
            if ((mTVInfoSubType.getText().toString()).equals((getString(R.string.weixin) + getString(wechat) + "," + getString(R.string.weibo))) || (mTVInfoSubType.getText().toString()).equals(getString(R.string.weibo))) {
                //微信微博同时选择／选择微博，活动类型只有三个
                tempList = Arrays.asList(getResources().getStringArray(R.array.launch_reword_consume_way_no_click));
            } else {
                tempList = Arrays.asList(getResources().getStringArray(R.array.launch_reword_consume_way));
            }
        }
        mConsumeWayList = new ArrayList<String>();

        mConsumeWayList.clear();
        mConsumeWayList.addAll(tempList);

        mDateList.clear();
        try {
            DateUtil.getAllYearDate(mDateList);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (isDoubleClick()) {
            return;
        }
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_post_image:
                skipToLocal();
                break;
            case R.id.tv_preview:
                skipToPreview();
                break;
            case R.id.tv_submit:
                submit();
                break;
            case R.id.layout_age:
                showAgeSelector();
                break;
            case R.id.layout_sex:
                showGenderSelector();
                break;
            case R.id.layout_classify:
                showClassifySelector();
                break;
            case R.id.layout_city:
                skipToCityList();
                break;
            case R.id.layout_sub_type:
                skipToChoseType();
                break;
        }
    }

    private void skipToCityList() {
        Intent intent = new Intent(this, CityListActivity.class);
        intent.putExtra("citylist", (Serializable) contactList);
        startActivityForResult(intent, SPConstants.CITY_LIST);
    }

    /**
     选择发布类型
     */
    private void skipToChoseType() {
        Intent intent = new Intent(this, CampaignChoseTypeActivity.class);
        intent.putExtra("chose", (mTVInfoSubType.getText().toString()));
        startActivityForResult(intent, SPConstants.CHOSE_TYPE);
    }

    private int mTagCount;
    private StringBuffer sb = new StringBuffer();

    private String getTags() {
        sb.delete(0, sb.length());
        mTagCount = 0;
        if (mGridData == null || mGridData.size() == 0) {
            return "";
        }
        for (int i = 0; i < mGridData.size(); i++) {
            TagsItem tagsItem = mGridData.get(i);
            if (tagsItem.isSelected) {
                sb.append(tagsItem.name).append(",");
                mTagCount++;
            }
        }

        if (mTagCount == 0) {
            return null;
        } else if (mTagCount == mGridData.size()) {
            return getString(R.string.all);
        }
        return sb.substring(0, sb.length() - 1);
    }

    private void showClassifySelector() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_classify_selector, null);
        GridView gvTags = (GridView) view.findViewById(R.id.gv_tags);
        TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        initGridData();
        mMyGridAdapter = new MyGridAdapter();
        gvTags.setAdapter(mMyGridAdapter);
        final CustomDialogManager cdm = new CustomDialogManager(this, view);

        tv_confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String tags = getTags();
                if (mTagCount < 2) {
                    CustomToast.showShort(ModifyRewordActivity.this, getString(R.string.count_at_least2));
                    return;
                }
                mTVInfoClassify.setText(tags);
                cdm.dg.dismiss();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                for (int i = 0; i < mGridData.size(); i++) {
                    mGridData.get(i).isSelected = false;
                }
                mMyGridAdapter.notifyDataSetChanged();
            }
        });
        cdm.dg.setCanceledOnTouchOutside(false);
        cdm.dg.getWindow().setGravity(Gravity.CENTER);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        cdm.showDialog();
    }

    private void initGridData() {
        if (mGridData.size() == 0) {
            String[] stringArray = getResources().getStringArray(R.array.first_page_tag_item_total);
            for (int i = 0; i < stringArray.length; i++) {
                TagsItem item = new TagsItem();
                item.name = stringArray[i];
                item.isSelected = true;
                mGridData.add(item);
            }
        }
    }

    /**
     年龄选择弹窗
     */
    private void showAgeSelector() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_age_selector, null);
        TextView tv_all = (TextView) view.findViewById(R.id.tv_all);
        TextView tv_part1 = (TextView) view.findViewById(R.id.tv_part1);
        TextView tv_part2 = (TextView) view.findViewById(R.id.tv_part2);
        TextView tv_part3 = (TextView) view.findViewById(R.id.tv_part3);
        TextView tv_part4 = (TextView) view.findViewById(R.id.tv_part4);
        //        TextView tv_part5 = (TextView) view.findViewById(R.id.tv_part5);
        final CustomDialogManager cdm = new CustomDialogManager(this, view);
        tv_all.setOnClickListener(new AgeSelectorOnClickListener(0, cdm));
        tv_part1.setOnClickListener(new AgeSelectorOnClickListener(1, cdm));
        tv_part2.setOnClickListener(new AgeSelectorOnClickListener(2, cdm));
        tv_part3.setOnClickListener(new AgeSelectorOnClickListener(3, cdm));
        tv_part4.setOnClickListener(new AgeSelectorOnClickListener(4, cdm));
        //        tv_part5.setOnClickListener(new AgeSelectorOnClickListener(5, cdm));
        cdm.dg.setCanceledOnTouchOutside(true);
        cdm.dg.getWindow().setGravity(Gravity.CENTER);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        cdm.showDialog();
    }

    /**
     性别选择弹窗
     */
    private void showGenderSelector() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_gender_selector, null);
        TextView maleTV = (TextView) view.findViewById(R.id.tv_male);
        TextView femaleTV = (TextView) view.findViewById(R.id.tv_female);
        TextView cancelTV = (TextView) view.findViewById(R.id.tv_cancel);
        cancelTV.setText(R.string.all);
        final CustomDialogManager cdm = new CustomDialogManager(this, view);

        maleTV.setOnClickListener(new GenderSelectorOnClickListener(MALE, cdm));
        femaleTV.setOnClickListener(new GenderSelectorOnClickListener(FEMALE, cdm));
        cancelTV.setOnClickListener(new GenderSelectorOnClickListener(ALL, cdm));

        cdm.dg.setCanceledOnTouchOutside(true);
        cdm.dg.getWindow().setGravity(Gravity.CENTER);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_shareboard_animation);
        cdm.showDialog();
    }


    private void skipToLocal() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
        startActivityForResult(i, SPConstants.IMAGE_REQUEST_CODE);
    }

    @Override
    public void update(Observable observable, Object data) {
        super.update(observable, data);

        if (data instanceof NotifyMsgEntity) {
            NotifyMsgEntity msgEntity = (NotifyMsgEntity) data;
            if (NotifyManager.TYPE_PAY_SUCCESSFUL == msgEntity.getCode()) {
                this.finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }
        if (resultCode == SPConstants.RESULT_CLIP) {
            return;
        }

        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case SPConstants.IMAGE_REQUEST_CODE:
                    mImageUri = data.getData();
                    cropImageUri(mImageUri, 1080, 607, SPConstants.RESULT_CROP_CODE);
                    break;
                case SPConstants.RESULT_CROP_CODE:
                    mBitmap = decodeUriAsBitmap(mImageUri);
                    try {
                        mFinalPicturePath = FileUtils.saveBitmapToSD(mBitmap, "temp" + SystemClock.currentThreadTimeMillis());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mIVPost.setImageBitmap(mBitmap);
                    setBottomView();
                    mImageLoadB = true;
                    break;

                case SPConstants.CITY_LIST:
                    String cityListStr = data.getStringExtra("city_list_str");
                    Serializable serializable = data.getSerializableExtra("city_list");
                    if (serializable != null && serializable instanceof List) {
                        contactList = (List<ContactItemInterface>) serializable;
                    }
                    mTVInfoCity.setText(cityListStr);
                    break;
                case SPConstants.CHOSE_TYPE:
                    //选择平台后返回
                   String  stChoseType = data.getStringExtra("chose");
                    if (TextUtils.isEmpty(stChoseType)) {
                      //  stChoseType = "";
                        mTVInfoSubType.setText("");
                    } else {
                        mTVInfoSubType.setText(stChoseType);
                    }
                    if (pvTime != null) {
                        pvTime.clearView();//里面的view==null
                        pvTime = null;
                    }
                    mTVInfo3.setText("");
                    prepareData();
                    break;
            }
        }
    }

    /**
     裁剪图片方法实现
     @param uri
     */
    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 16);
        intent.putExtra("aspectY", 9);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("scale", true);//黑边
        intent.putExtra("scaleUpIfNeeded", true);//黑边
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }

    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     设置底部显示样式
     */
    private void setBottomView() {
        mInfoCompelete = checkInfoCompelete(false);
        if (mInfoCompelete) {
            mLLBottom.setBackgroundResource(R.color.blue_custom);
        } else {
            mLLBottom.setBackgroundResource(R.color.gray_custom);
        }
    }

    /**
     提交
     */
    private void submit() {
      //  LogUtil.LogShitou("这不是空？点击点击", "<====>");

        if (! checkInfoCompelete(true)) {
            return;
        }

        CustomToast.showShort(this, "提交中..");
        LinkedHashMap<String, Object> requestMap = new LinkedHashMap<>();
        requestMap.put("name", mETTitle.getText().toString());
        requestMap.put("description", mETIntroduce.getText().toString());
        requestMap.put("url", mETAddress.getText().toString());
        requestMap.put("start_time", mTVInfo1.getText());
        requestMap.put("deadline", mTVInfo2.getText());
        requestMap.put("per_budget_type", mCountType);
        requestMap.put("budget", mETConsume4.getText().toString());
        requestMap.put("per_action_budget", mETConsume5.getText().toString());

        requestMap.put("age", getPostAge(mTVInfoAge.getText().toString()));
        requestMap.put("region", mTVInfoCity.getText());
        requestMap.put("tags", mTVInfoClassify.getText());
        //发布平台sub_type
        if ((mTVInfoSubType.getText().toString()).equals((getString(R.string.weixin) + getString(wechat) + "," + getString(R.string.weibo)))) {
            String s = "wechat,weibo";
            //   String [] s = new String[]{"wechat","weibo"};
            requestMap.put("sub_type", s);
        } else if ((mTVInfoSubType.getText().toString()).equals(getString(R.string.weibo))) {
            requestMap.put("sub_type", "weibo");
        } else if ((mTVInfoSubType.getText().toString()).equals(getString(R.string.weixin) + getString(wechat))) {
            requestMap.put("sub_type", "wechat");
        } else {
            CustomToast.showShort(this, "请选择推广平台");
            return;
        }
       // LogUtil.LogShitou("这不是空？", "====>" + stChoseType);
        String gender = "全部";
        if (getString(R.string.male).equals(mTVInfoSex.getText())) {
            gender = "1";
        } else if (getString(R.string.female).equals(mTVInfoSex.getText())) {
            gender = "2";
        }
        requestMap.put("gender", gender);

        File file = null;
        String imageName = null;
        if (! TextUtils.isEmpty(mFinalPicturePath) && ! mFinalPicturePath.startsWith("http://")) {
            imageName = mFinalPicturePath.substring(mFinalPicturePath.lastIndexOf("/") + 1);
            file = new File(mFinalPicturePath);
        }

        String url;
        int method;
        if (mFrom == REJECT) {
            requestMap.put("img_url", mModifyCampaign.getImg_url());
            requestMap.put("id", String.valueOf(mModifyCampaign.getId()));
            url = HelpTools.getUrl(CommonConfig.MODIFY_LAUNCH_CAMPAIGNS_URL);
            method = HttpRequest.PUT;
        } else {
            url = HelpTools.getUrl(CommonConfig.LAUNCH_CAMPAIGNS_URL);
            method = HttpRequest.POST;
        }

        BasePresenter mBasePresenter = new BasePresenter();

        mBasePresenter.getDataFromServer(true, method, url, "img", imageName, file, requestMap, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                CustomToast.showShort(ModifyRewordActivity.this, getString(R.string.please_data_wrong));
            }

            @Override
            public void onResponse(String response) {

                LaunchRewordModel launchRewordModel = GsonTools.jsonToBean(response, LaunchRewordModel.class);

                if (launchRewordModel == null) {
                    CustomToast.showShort(ModifyRewordActivity.this, getString(R.string.please_data_wrong));
                    return;
                }

                if (launchRewordModel.getError() == 0) {
                    skipToNextPage(launchRewordModel);
                } else {
                    CustomToast.showShort(ModifyRewordActivity.this, launchRewordModel.getDetail());
                }
            }
        });
    }

    private String getPostAge(String ageS) {
        for (int i = 0; i < ageArr.length; i++) {
            if (! TextUtils.isEmpty(ageS) && ageS.equals(ageArr[i])) {
                return agePostArr[i];
            }
        }
        return null;
    }

    private void skipToNextPage(LaunchRewordModel launchRewordModel) {
        if (mFrom == REJECT && ! UN_PAY.equals(mModifyCampaign.getStatus())) {
            setResult(SPConstants.LAUNCHREWORDACTIVIRY);
            finish();
            return;
        }
        Intent intent = new Intent(this, LaunchRewordSecondActivity.class);
        intent.putExtra("id", launchRewordModel.getCampaign().getId());
        intent.putExtra("launchrewordmodel", launchRewordModel);
        startActivityForResult(addBundle(intent), 0);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void skipToPreview() {
        if (! checkInfoCompelete(true)) {
            return;
        }
        Intent intent = new Intent(this, DetailContentActivity.class);
        startActivity(addBundle(intent));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private Intent addBundle(Intent intent) {
        Bundle bundle = new Bundle();
        bundle.putInt("from", SPConstants.LAUNCHREWORDACTIVIRY);
        bundle.putString("path", mFinalPicturePath);
        bundle.putString("title", mETTitle.getText().toString());
        bundle.putString("start_time", mTVInfo1.getText().toString());
        bundle.putString("end_time", mTVInfo2.getText().toString());
        bundle.putString("introduce", mETIntroduce.getText().toString());
        bundle.putString("count_way", mCountType);
        bundle.putString("every_consume", mETConsume5.getText().toString());
        bundle.putString("address", mETAddress.getText().toString());
        bundle.putString("total_consume", mETConsume4.getText().toString());
        return intent.putExtras(bundle);
    }

    private void onCompelete() {
        if (hasTranslateB) {
            ObjectAnimator animation = ObjectAnimator.ofFloat(mSVContent, "translationY", - UIUtils.getDimens(R.dimen.launch_reword_translate), 0f);
            animation.setDuration(400);
            animation.start();

            mView.setVisibility(View.VISIBLE);
            mLLBottom.setVisibility(View.VISIBLE);

            ObjectAnimator animation2 = ObjectAnimator.ofFloat(mLLWheel, "translationY", 0f, UIUtils.getDimens(R.dimen.launch_reword_translate));
            animation2.setDuration(400);
            animation2.start();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    mLLWheel.setVisibility(View.GONE);
                }
            }, 400);

            hasTranslateB = false;
        }
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
    }


    class DateBean {
        public int day = - 1;
        public int hour = - 1;
        public int minute = - 1;
    }

    class EditTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s != null) {
                setBottomView();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private class GenderSelectorOnClickListener implements View.OnClickListener {
        private int sex;
        private CustomDialogManager cdm;

        public GenderSelectorOnClickListener(int sex, CustomDialogManager cdm) {
            this.sex = sex;
            this.cdm = cdm;
        }

        @Override
        public void onClick(View v) {
            switch (sex) {
                case MALE:
                    mTVInfoSex.setText(R.string.male);
                    break;
                case FEMALE:
                    mTVInfoSex.setText(R.string.female);
                    break;
                case ALL:
                    mTVInfoSex.setText(R.string.all);
                    break;
            }
            cdm.dismiss();
        }
    }

    private class AgeSelectorOnClickListener implements View.OnClickListener {
        private int ageType;
        private CustomDialogManager cdm;

        public AgeSelectorOnClickListener(int ageType, CustomDialogManager cdm) {
            this.ageType = ageType;
            this.cdm = cdm;
        }

        @Override
        public void onClick(View v) {
            mTVInfoAge.setText(ageArr[ageType]);
            mAgeType = ageType;
            cdm.dismiss();
        }
    }

    private class MyGridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mGridData.size();
        }

        @Override
        public TagsItem getItem(int position) {
            return mGridData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TagsItem item = getItem(position);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(ModifyRewordActivity.this).inflate(R.layout.item_detail_be_kol_grid, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvName.setSelected(item.isSelected);
            holder.tvName.setText(item.name);
            holder.tvName.setOnClickListener(new MyOnClickListener(holder.tvName, item));
            return convertView;
        }
    }

    private class TagsItem {
        public String name;
        public boolean isSelected;
    }

    private class ViewHolder {
        public TextView tvName;
    }

    private class MyOnClickListener implements View.OnClickListener {
        private TextView tv;
        private TagsItem item;

        public MyOnClickListener(TextView tv, TagsItem item) {
            this.tv = tv;
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            if (tv.isSelected()) {
                item.isSelected = false;
            } else {
                item.isSelected = true;
            }
            tv.setSelected(item.isSelected);
        }
    }
}
