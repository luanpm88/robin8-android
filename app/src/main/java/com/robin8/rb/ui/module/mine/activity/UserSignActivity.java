package com.robin8.rb.ui.module.mine.activity;

import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.ui.activity.MainActivity;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.ui.model.BaseBean;
import com.robin8.rb.ui.model.LoginBean;
import com.robin8.rb.ui.module.mine.adapter.NoScrollGridAdapter;
import com.robin8.rb.ui.module.mine.model.SignDayHistory;
import com.robin8.rb.ui.module.mine.model.SignDaysModel;
import com.robin8.rb.ui.module.mine.model.SimpleModel;
import com.robin8.rb.ui.module.mine.presenter.UserSignPresenter;
import com.robin8.rb.ui.module.mine.view.IUserSignView;
import com.robin8.rb.ui.module.social.view.LinearLayoutForListView;
import com.robin8.rb.ui.module.social.view.NoScrollGridView;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.ui.widget.CircleImageView;
import com.robin8.rb.ui.dialog.CustomDialogManager;

import java.util.ArrayList;
import java.util.List;


/**
 签到页面 */
public class UserSignActivity extends BaseActivity implements IUserSignView {

    private UserSignPresenter mUserSignPresenter;
    private TextView mHasSignedDaysTv;
    private CircleImageView mCircleImageView;
    private TextView mUserNameTv;
    private TextView mEarnAccumulatedTv;
    private TextView mEarnTodayTv;
    private static final int MaxWidth = 80;
    private static final int MaxSpeed = 200;
    private NoScrollGridView mGridView;
    private List<SignDaysModel> listGrid;
    private float[] rewards = {0.10f, 0.20f, 0.25f, 0.30f, 0.35f, 0.40f, 0.50f};
    private String[] weeks = {"1天", "2天", "3天", "4天", "5天", "6天", "7天"};
    private LinearLayoutForListView myList;
    private List<SimpleModel> mTaskList;
    private TextView tvSign;
    private WProgressDialog mWProgressDialog;
    private NoScrollGridAdapter noScrollGridAdapter;
    private MyTaskAdapter myTaskAdapter;
    private List<String> tasksTitles;
    private LinearLayout llTasks;
    private TextView tvDoTasks;
    private TextView tvTaskOne;
    private TextView tvTaskTwo;
    private TextView tvTaskThree;
    private SignDayHistory bean;
    public static final int NEW_USER_TASKS_OVER = 122;

    @Override
    public void setTitleView() {
        mTVCenter.setText(R.string.sign);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_user_sign, mLLContent, true);

        mCircleImageView = (CircleImageView) view.findViewById(R.id.civ_image);
        mUserNameTv = (TextView) view.findViewById(R.id.tv_user_name);
        mHasSignedDaysTv = (TextView) view.findViewById(R.id.tv_has_signed_days);
        mBottomTv = (TextView) view.findViewById(R.id.tv_bottom);
        //累计收益
        mEarnAccumulatedTv = (TextView) view.findViewById(R.id.tv_earn_accumulated);
        //今日收益
        mEarnTodayTv = (TextView) view.findViewById(R.id.tv_earn_today);
        mGridView = ((NoScrollGridView) view.findViewById(R.id.sign_girdview));
        //签到
        tvSign = ((TextView) view.findViewById(R.id.tv_sign));
        tvSign.setOnClickListener(this);
        LinearLayout ll_show = (LinearLayout) view.findViewById(R.id.ll_sign_show);
        ll_show.setOnClickListener(this);
        //新手任务
        llTasks = ((LinearLayout) view.findViewById(R.id.ll_new_user_tasks));
        tvDoTasks = ((TextView) view.findViewById(R.id.tv_do_tasks));
        tvDoTasks.setOnClickListener(this);
        tvTaskOne = ((TextView) view.findViewById(R.id.tv_new_tasks_one));
        tvTaskTwo = ((TextView) view.findViewById(R.id.tv_new_tasks_two));
        tvTaskThree = ((TextView) view.findViewById(R.id.tv_new_tasks_three));
        //每日任务
        myList = ((LinearLayoutForListView) findViewById(R.id.lv_list));
        mTaskList = new ArrayList<>();
        listGrid = new ArrayList<>();
        tasksTitles = new ArrayList<>();
        //mUserSignPresenter = new UserSignPresenter(this, this);
        //mUserSignPresenter.init();
        updateView();
        getHistory(0);

        mTaskList.add(new SimpleModel(R.mipmap.icon_share_campain, getString(R.string.robin291,0), getString(R.string.robin286), getString(R.string.robin284)));
        mTaskList.add(new SimpleModel(R.mipmap.icon_article_social, getString(R.string.robin290,0), getString(R.string.robin287), getString(R.string.robin285)));
        mTaskList.add(new SimpleModel(R.mipmap.icon_invite_friends, getString(R.string.robin289,0), getString(R.string.robin288), getString(R.string.robin284)));
        myTaskAdapter = new MyTaskAdapter();
        myList.setAdapter(myTaskAdapter);
        weeks = getResources().getStringArray(R.array.sign_days);
        for (int i = 0; i < rewards.length; i++) {
            SignDaysModel signDaysModel = new SignDaysModel();
            signDaysModel.setRewards(rewards[i]);
            signDaysModel.setWeek(weeks[i]);
            listGrid.add(i, signDaysModel);
        }
        noScrollGridAdapter = new NoScrollGridAdapter(UserSignActivity.this, listGrid);
        mGridView.setAdapter(noScrollGridAdapter);
    }

    private void updateView() {
        LoginBean bean = BaseApplication.getInstance().getLoginBean();
        //昵称头像
        if (bean != null && bean.getKol() != null) {
            mUserNameTv.setText(bean.getKol().getName());
            BitmapUtil.loadImage(this, bean.getKol().getAvatar_url(), mCircleImageView);
            if (! TextUtils.isEmpty(HelpTools.getCommonXml(HelpTools.ISNEWUSER))) {
                if (HelpTools.getCommonXml(HelpTools.ISNEWUSER).equals("is")) {
                    llTasks.setVisibility(View.VISIBLE);
                } else {
                    llTasks.setVisibility(View.GONE);
                }
            } else {
                llTasks.setVisibility(View.GONE);
            }

        }
    }

    private void getHistory(final int count) {
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(UserSignActivity.this);
        }
        mWProgressDialog.show();
        BasePresenter mBasePresenter = new BasePresenter();
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.CHECK_IN_HISTORY_URL), null, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }


            @Override
            public void onResponse(String response) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                LogUtil.LogShitou("签到历史" + HelpTools.getUrl(CommonConfig.CHECK_IN_HISTORY_URL), response);
                bean = GsonTools.jsonToBean(response, SignDayHistory.class);
                if (bean != null) {
                    if (bean.getError() == 0) {
                        if (bean.is_show_newbie() == true) {
                            llTasks.setVisibility(View.GONE);
                        }
                        mEarnAccumulatedTv.setText(String.valueOf(bean.getTotal_check_in_amount()));
                        mEarnTodayTv.setText(String.valueOf(bean.getToday_already_amount()));
                        if (bean.isToday_had_check_in()) {
                            tvSign.setClickable(false);
                            tvSign.setText(R.string.robin292);
                            //                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            //                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            //                                    tvSign.setBackground(getDrawable(R.drawable.shape_solid_gray));
                            //                                }
                            //                            }
                            tvSign.setBackgroundResource(R.drawable.shape_solid_gray);
                            mHasSignedDaysTv.setText(getString(R.string.robin293,bean.getContinuous_checkin_count(),String.valueOf(bean.getTomorrow_can_amount())));
                        } else {
                            tvSign.setClickable(true);
                            tvSign.setText(R.string.robin207);
                            //                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            //                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            //                                    tvSign.setBackground(getDrawable(R.drawable.shape_solid_blue));
                            //                                }else {
                            //                                }
                            //                            }
                            tvSign.setBackgroundResource(R.drawable.shape_solid_blue);
                            mHasSignedDaysTv.setText(getString(R.string.robin294,bean.getContinuous_checkin_count(),String.valueOf(bean.getToday_can_amount())));
                        }
                        tasksTitles.add(getString(R.string.robin291,bean.getCampaign_invites_count()));
                        tasksTitles.add(getString(R.string.robin290,bean.getRed_money_count()));
                        tasksTitles.add(getString(R.string.robin289,bean.getInvite_friends()));
                        myTaskAdapter.setTitle(tasksTitles);
                        myTaskAdapter.notifyDataSetChanged();
                        noScrollGridAdapter.setmSignList(bean.getCheck_in_7());
                        noScrollGridAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }


    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.MY_SIGN;
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_sign:
                sign();
                break;
            case R.id.ll_sign_show:
                shareCtaAndCti();
                break;
            case R.id.tv_do_tasks:
                Intent intent = new Intent(UserSignActivity.this, NewUserTaskActivity.class);
                startActivityForResult(intent, NEW_USER_TASKS_OVER);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == NEW_USER_TASKS_OVER) {
                CustomToast.showShort(UserSignActivity.this, "新手任务完成，奖励已发放");
                llTasks.setVisibility(View.GONE);
            }
        }
    }

    /**
     签到
     */
    public void sign() {
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(UserSignActivity.this);
        }
        mWProgressDialog.show();
        BasePresenter mBasePresenter = new BasePresenter();
        mBasePresenter.getDataFromServer(true, HttpRequest.PUT, HelpTools.getUrl(CommonConfig.CHECK_IN_URL), null, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
                //  LogUtil.LogShitou("签到" + HelpTools.getUrl(CommonConfig.CHECK_IN_URL), response);
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);
                if (bean == null) {
                    CustomToast.showShort(UserSignActivity.this, getString(R.string.please_data_wrong));
                    return;
                }
                if (bean.getError() == 0) {
                    getHistory(1);
                    showSuccessDialog();
                } else {
                    if (! TextUtils.isEmpty(bean.getDetail())) {
                        CustomToast.showShort(UserSignActivity.this, bean.getDetail());
                    }
                }
            }
        });
    }

    private void shareCtaAndCti() {
        View view = LayoutInflater.from(UserSignActivity.this).inflate(R.layout.dialog_promit_cpa, null);
        TextView confirmTV = (TextView) view.findViewById(R.id.tv_confirm);
        TextView infoTv = (TextView) view.findViewById(R.id.tv_info);
        TextView infoSecondTv = (TextView) view.findViewById(R.id.tv_info_second);
        TextView topTv = (TextView) view.findViewById(R.id.tv_top);
        infoSecondTv.setVisibility(View.VISIBLE);
        topTv.setText(R.string.sign_rule);
        confirmTV.setText(R.string.known);
        // infoTv.setText(Html.fromHtml("1.连续签到,获得奖励累加,\n第一天签到获得<font color=#2bdbe2>" + "0.10" + "</font>元,\n连续签到两天获得<font color=#2bdbe2>" + "0.20" + "</font>元,\n连续签到三天获得<font color=#2bdbe2>" + "0.25" + "</font>元,\n连续签到四天获得<font color=#2bdbe2>" + "0.30" + "</font>元,\n连续签到五天获得<font color=#2bdbe2>" + "0.35" + "</font>元,\n连续签到六天获得<font color=#2bdbe2>" + "0.40" + "</font>元,\n连续签到七天获得<font color=#2bdbe2>" + "0.50" + "</font>元。")+"\n2.七天一个周期,第八天重新开始计算。(若中途某一天停止签到,仍会重新计算)");
        infoTv.setText(Html.fromHtml("1.连续签到,获得奖励累加,\n第一天签到获得<font color=#2bdbe2>" + "0.10" + "</font>元,\n连续签到两天获得<font color=#2bdbe2>" + "0.20" + "</font>元,\n连续签到三天获得<font color=#2bdbe2>" + "0.25" + "</font>元,\n连续签到四天获得<font color=#2bdbe2>" + "0.30" + "</font>元,\n连续签到五天获得<font color=#2bdbe2>" + "0.35" + "</font>元,\n连续签到六天获得<font color=#2bdbe2>" + "0.40" + "</font>元,\n连续签到七天获得<font color=#2bdbe2>" + "0.50" + "</font>元。"));
        infoSecondTv.setText("2.七天一个周期,第八天重新开始计算。(若中途某一天停止签到,仍会重新计算)");
        infoTv.setGravity(Gravity.LEFT);
        infoSecondTv.setGravity(Gravity.LEFT);
        final CustomDialogManager cdm = new CustomDialogManager(UserSignActivity.this, view);
        confirmTV.setOnClickListener(new View.OnClickListener() {

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

    private void showSuccessDialog() {
        String s = "";
        View view = LayoutInflater.from(UserSignActivity.this).inflate(R.layout.dialog_sign_success, null);
        TextView tvInfo = (TextView) view.findViewById(R.id.tv_info);
        TextView tvKnow = (TextView) view.findViewById(R.id.tv_know);
        final CustomDialogManager cdm = new CustomDialogManager(UserSignActivity.this, view);
        if (bean != null) {
            s = "签到成功，" + StringUtil.addZeroForNum(String.valueOf(bean.getToday_can_amount()), 4) + "元奖励已放入您的钱包！\n您已连续签到" + (bean.getContinuous_checkin_count() + 1) + "天，不要间断哦～";

        } else {
            s = "签到成功";
        }
        tvInfo.setText(s);
        if (bean != null) {
            int length = String.valueOf(bean.getContinuous_checkin_count()).trim().length();
            StringUtil.setTextViewSpan(tvInfo, 33, 5, 9, getResources().getColor(R.color.blue_custom));
            StringUtil.setTextViewSpan(tvInfo, 33, 27, 27 + length, getResources().getColor(R.color.blue_custom));
        }
        tvKnow.setOnClickListener(new View.OnClickListener() {

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

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
    }

    @Override
    public void setCircleImageView(String url) {
        BitmapUtil.loadImage(this, url, mCircleImageView);
    }

    @Override
    public void setHasSignedDaysTv(String text) {
        mHasSignedDaysTv.setText(getString(R.string.you_have_sign) + text + getString(R.string.day));
    }

    @Override
    public void setUserNameTv(String text) {
        if (TextUtils.isEmpty(text)) {
            mUserNameTv.setVisibility(View.GONE);
        } else {
            mUserNameTv.setText(text);
        }
    }

    class MyTaskAdapter extends BaseAdapter {
        private List<String> title;

        @Override
        public int getCount() {

            return mTaskList.size();
        }

        @Override
        public SimpleModel getItem(int position) {

            return mTaskList.get(position);
        }


        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(UserSignActivity.this).inflate(R.layout.prove_social_score_item, null);
                convertView.setTag(new UserSignActivity.Holder(convertView));
            }
            final UserSignActivity.Holder holder = (UserSignActivity.Holder) convertView.getTag();

            SimpleModel item = getItem(position);
            holder.imgIcon.setImageResource(item.icon);
            if (getTitle() != null && getTitle().size() != 0) {
                holder.tvTitle.setText(getTitle().get(position));
            } else {
                holder.tvTitle.setText(item.title);
            }
            holder.tvContent.setText(item.detail);
            holder.btnAction.setText(item.btn);
            if (position == 0) {
                holder.btnAction.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //活动分享
                        Intent intent = new Intent(UserSignActivity.this, MainActivity.class);
                        intent.putExtra("register_main", "zhu");
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (position==1){
                holder.btnAction.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(UserSignActivity.this, MainActivity.class);
                        intent.putExtra("register_main", "read");
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                });
            }else {
                holder.btnAction.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //邀请好友
                        startActivity(new Intent(UserSignActivity.this, CollectMoneyActivity.class));
                        finish();
                    }
                });
            }
            return convertView;
        }


        public List<String> getTitle() {
            return title;
        }

        public void setTitle(List<String> title) {
            this.title = title;
        }
    }

    class Holder {


        private final ImageView imgIcon;
        private final TextView tvTitle;
        private final TextView tvContent;
        private final TextView btnAction;

        Holder(View view) {
            imgIcon = ((ImageView) view.findViewById(R.id.img_icon));
            tvTitle = ((TextView) view.findViewById(R.id.tv_title));
            tvContent = ((TextView) view.findViewById(R.id.tv_content));
            btnAction = ((TextView) view.findViewById(R.id.btn_action));

        }
    }

    @Override
    public TextView mEarnAccumulatedTv() {
        return mEarnAccumulatedTv;
    }

    @Override
    public TextView getEarnTodayTv() {
        return mEarnTodayTv;
    }

    @Override
    public NoScrollGridView getSignDayGv() {
        return mGridView;
    }

    @Override
    public LinearLayoutForListView getTasksLv() {
        return myList;
    }

    @Override
    public TextView signTv() {
        return tvSign;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tvSign.setBackgroundResource(0);
        System.gc();
    }
}
