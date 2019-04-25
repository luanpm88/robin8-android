package com.robin8.rb.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.ui.model.CampaignListBean;
import com.robin8.rb.ui.module.reword.activity.DetailContentActivity;
import com.robin8.rb.ui.module.reword.helper.DetailContentHelper;
import com.robin8.rb.ui.widget.LineSpaceTextView;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.DateUtil;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.util.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 Created by Figo on 2016/6/30. */
public class RewordAdapter extends BaseAdapter {

    public final static String JOIN_SIGN = "approved/rejected/finished/settled";
    public final static String EXECUTED_SIGN = "executed/settled";
    public final static int DIS_JOIN = 0;//未参加
    public final static int JOINED = 1;//已参加
    public final static int EXECUTING = 2;//进行中
    public final static int EXECUTED = 3;//已结束
    public final static int READYBEGIN = 4;//倒计时

    private static final String CAMPAIGN_TYPE_INVITE = "invite";//邀请
    private static final String CAMPAIGN_TYPE_RECRUIT = "recruit";//招募
    private static final String CAMPAIGN_TYPE_CLICK = "click";//点击
    private static final String CAMPAIGN_TYPE_POST = "post";//转发
    private static final String CAMPAIGN_TYPE_CPA = "cpa";//效果
    private static final String CAMPAIGN_TYPE_CPI = "simple_cpi";
    private static final String CAMPAIGN_TYPE_CPT = "cpt";

    private final List<CampaignListBean.CampaignInviteEntity> mList;
    private final LayoutInflater mLayoutInflater;
    private final Context mActivity;
    private HashMap<TextView, Couterdown> timeMap = new HashMap<>();
    private HashMap<Integer, Integer> mapStart = new HashMap<>();
    private SparseArray<CountDownTimer> countDownCounters;

    public RewordAdapter(Activity activity, List<CampaignListBean.CampaignInviteEntity> list) {

        mList = list;
        mActivity = activity;
        mLayoutInflater = LayoutInflater.from(activity);
        this.countDownCounters = new SparseArray<>();

    }

    @Override
    public int getCount() {

        return mList.size();
    }

    @Override
    public CampaignListBean.CampaignInviteEntity getItem(int position) {

        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    /**
     清空资源
     */
    public void cancelAllTimers() {
        if (countDownCounters == null) {
            return;
        }
        Log.e("TAG", "size :  " + countDownCounters.size());
        for (int i = 0, length = countDownCounters.size(); i < length; i++) {
            CountDownTimer cdt = countDownCounters.get(countDownCounters.keyAt(i));
            if (cdt != null) {
                cdt.cancel();
            }
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.reword_list_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.aliasTextView = (LineSpaceTextView) convertView.findViewById(R.id.tv_alias);
            viewHolder.payinfoLinearLayout = (LinearLayout) convertView.findViewById(R.id.ll_payinfo);
            viewHolder.earnLinearLayout = (LinearLayout) convertView.findViewById(R.id.ll_earn);
            viewHolder.ivImageView = (ImageView) convertView.findViewById(R.id.iv_bg);
            viewHolder.iv_cover = (ImageView) convertView.findViewById(R.id.iv_cover);
            viewHolder.iv_cover2 = (ImageView) convertView.findViewById(R.id.iv_cover2);
            viewHolder.iv_tag = (ImageView) convertView.findViewById(R.id.iv_tag);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_remainder = (TextView) convertView.findViewById(R.id.tv_remainder);
            viewHolder.tv_earn_money = (TextView) convertView.findViewById(R.id.tv_earn_money);
            viewHolder.tv_actiontype = (TextView) convertView.findViewById(R.id.tv_actiontype);//类型
            viewHolder.tv_over = (TextView) convertView.findViewById(R.id.tv_over);
            viewHolder.tv_earn_had = (TextView) convertView.findViewById(R.id.tv_earn_had);
            viewHolder.tv_last = (TextView) convertView.findViewById(R.id.tv_last);//最多可赚
            viewHolder.fl_content = convertView.findViewById(R.id.fl_content);
            viewHolder.tv_count_down = (TextView) convertView.findViewById(R.id.tv_count_down);
            viewHolder.ll_over_show = (LinearLayout) convertView.findViewById(R.id.ll_over_show);
            viewHolder.ll_show_money = (LinearLayout) convertView.findViewById(R.id.ll_show_money);
            viewHolder.ll_will_begin = (LinearLayout) convertView.findViewById(R.id.ll_will_begin);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final CampaignListBean.CampaignInviteEntity item = getItem(position);
        if (item == null) {
            return null;
        }
        if (item.getCampaign().getName().length() > 20) {
            viewHolder.tv_title.setText(item.getCampaign().getName().substring(0, 20) + "...");
        } else {
            viewHolder.tv_title.setText(item.getCampaign().getName());//标题
        }
        viewHolder.aliasTextView.setText(item.getCampaign().getBrand_name());//商标
        viewHolder.ivImageView.post(new CorrectionRunnable(viewHolder.ivImageView, viewHolder.fl_content));
        BitmapUtil.loadImage(mActivity.getApplicationContext(), item.getCampaign().getImg_url(), viewHolder.ivImageView, R.color.sub_gray_custom);
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (BaseApplication.isDoubleClick()) {
                    return;
                }
                Intent intent = new Intent(mActivity, DetailContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", item);
                mActivity.startActivity(intent.putExtras(bundle));
            }
        });
        String per_budget_type = item.getCampaign().getPer_budget_type();
        String status = item.getCampaign().getStatus();//活动状态
        float remainBudget = item.getCampaign().getRemain_budget();//活动预算
        float per_action_budget = item.getCampaign().getPer_action_budget();//活动单价
        if (TextUtils.isEmpty(per_budget_type) || TextUtils.isEmpty(status)) {
            return null;
        }
        switch (per_budget_type) {
            case CAMPAIGN_TYPE_INVITE:
                setInViteTypeView(item, viewHolder, judgeActivityStatues(item));
                break;
            case CAMPAIGN_TYPE_RECRUIT:
                setRecruitTypeView(item, viewHolder, judgeActivityStatues(item));
                break;
            default:
                setNormalTypeView(item, viewHolder, judgeActivityStatues(item));
                break;
        }
        CountDownTimer countDownTimer = countDownCounters.get(viewHolder.tv_count_down.hashCode());
        //将前一个缓存清除
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        //活动未开始，倒计时
        if (judgeActivityStatues(item) == READYBEGIN) {
            final int start_time = new Long(DateUtil.getTimeLong(item.getCampaign().getStart_time())).intValue();
            final int deal_time = new Long(DateUtil.getTimeLong(item.getCampaign().getDeadline())).intValue();
            final int now_time = new Long(DateUtil.getTimeLong(DateUtil.getNowTimeMs("yyyyMMddHHmmssSSS"))).intValue();
            viewHolder.tv_actiontype.setText("距活动开始");
            viewHolder.tv_actiontype.setTextColor(Color.parseColor("#fab719"));//黄色
            viewHolder.payinfoLinearLayout.setVisibility(View.VISIBLE);
            viewHolder.iv_cover.setBackgroundColor(mActivity.getResources().getColor(R.color.cover_transparent_deep));
            viewHolder.earnLinearLayout.setVisibility(View.VISIBLE);
            viewHolder.ll_show_money.setVisibility(View.GONE);
            viewHolder.ll_over_show.setVisibility(View.GONE);
            viewHolder.tv_over.setVisibility(View.GONE);
            viewHolder.ll_will_begin.setVisibility(View.VISIBLE);
            int timer = start_time - now_time;
            if (timer > 0) {
                countDownTimer = new CountDownTimer(timer, 1000) {

                    public void onTick(long millisUntilFinished) {
                        viewHolder.tv_count_down.setText("距开始" + getCountTimeByLong(millisUntilFinished));
                    }

                    public void onFinish() {
                        viewHolder.earnLinearLayout.setVisibility(View.GONE);
                        viewHolder.ll_show_money.setVisibility(View.VISIBLE);
                        viewHolder.ll_will_begin.setVisibility(View.GONE);
                        setCampaignTypeView(item, viewHolder, EXECUTING);
                        setCampaignStateView(item, viewHolder, judgeEnterActivity(item), EXECUTING);
                    }
                }.start();
                countDownCounters.put(viewHolder.tv_count_down.hashCode(), countDownTimer);
            } else {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                viewHolder.earnLinearLayout.setVisibility(View.GONE);
                viewHolder.ll_show_money.setVisibility(View.VISIBLE);
                viewHolder.ll_will_begin.setVisibility(View.GONE);
                setCampaignTypeView(item, viewHolder, EXECUTING);
                setCampaignStateView(item, viewHolder, judgeEnterActivity(item), EXECUTING);
            }
        } else {
            viewHolder.earnLinearLayout.setVisibility(View.GONE);
            viewHolder.ll_show_money.setVisibility(View.VISIBLE);
            viewHolder.ll_will_begin.setVisibility(View.GONE);
            setCampaignTypeView(item, viewHolder, judgeActivityStatues(item));
            setCampaignStateView(item, viewHolder, judgeEnterActivity(item), judgeActivityStatues(item));
        }

        return convertView;
    }

    /**
     设置活动进行状态
     @param item
     @param viewHolder
     @param enterActivityStatus
     */
    private void setCampaignStateView(CampaignListBean.CampaignInviteEntity item, ViewHolder viewHolder, int enterActivityStatus, int activityStatues) {
        if (activityStatues != READYBEGIN) {
            if (enterActivityStatus == JOINED) {//是否参与活动
                //参与活动
                viewHolder.iv_cover2.setVisibility(View.GONE);
                viewHolder.payinfoLinearLayout.setVisibility(View.GONE);
                viewHolder.earnLinearLayout.setVisibility(View.VISIBLE);
                //    viewHolder.tv_earn_num_thistime.setText(String.valueOf(item.getCampaign().getPer_action_budget()));//设置每次转发费用/改为点击数
                //   viewHolder.tv_earn_num_thistime.setText(String.valueOf(item.getAvail_click()));//设置每次转发费用/改为点击数
                viewHolder.tv_earn_money.setText(String.valueOf(item.getEarn_money()));//已赚的金额

                switch (item.getStatus()) {
                    case "approved"://开始了 参与了
                        viewHolder.tv_earn_had.setText("即将赚");
                        break;
                    case "rejected"://结束了 没参与
                        viewHolder.tv_earn_had.setText("已赚");
                        viewHolder.tv_earn_money.setText("0");//已赚的金额
                        break;
                    case "finished"://结束了 参与了
                        viewHolder.tv_earn_had.setText("即将赚");
                        break;
                    case "settled"://结束了 参与了 结算了
                        viewHolder.tv_earn_had.setText("已赚");
                        break;
                    default:
                        break;
                }
            } else {
                //没参与
                if (activityStatues == EXECUTED) {
                    viewHolder.payinfoLinearLayout.setVisibility(View.GONE);
                    viewHolder.iv_cover2.setVisibility(View.VISIBLE);
                    viewHolder.earnLinearLayout.setVisibility(View.GONE);
                } else {
                    viewHolder.iv_cover2.setVisibility(View.VISIBLE);
                    //   viewHolder.payinfoLinearLayout.setVisibility(View.VISIBLE);
                    viewHolder.payinfoLinearLayout.setVisibility(View.VISIBLE);
                    viewHolder.earnLinearLayout.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     设置活动种类
     @param item
     @param viewHolder
     @param activityStatues
     */
    private void setCampaignTypeView(CampaignListBean.CampaignInviteEntity item, final ViewHolder viewHolder, int activityStatues) {

        String per_budget_type = item.getCampaign().getPer_budget_type();
        String status = item.getCampaign().getStatus();//活动状态
        float remainBudget = item.getCampaign().getRemain_budget();//活动预算
        float per_action_budget = item.getCampaign().getPer_action_budget();//活动单价
        if (TextUtils.isEmpty(per_budget_type) || TextUtils.isEmpty(status)) {
            return;
        }
        switch (per_budget_type) {
            case CAMPAIGN_TYPE_INVITE:
                setInViteTypeView(item, viewHolder, activityStatues);
                break;
            case CAMPAIGN_TYPE_RECRUIT:
                setRecruitTypeView(item, viewHolder, activityStatues);
                break;
            default:
                setNormalTypeView(item, viewHolder, activityStatues);
                break;
        }
        if (activityStatues == EXECUTING) {//活动未结束
            viewHolder.iv_cover.setBackgroundColor(mActivity.getResources().getColor(R.color.cover_transparent));
            viewHolder.tv_actiontype.setTextColor(Color.parseColor("#fab719"));//黄色
            if (item.getInvite_status().equals(DetailContentHelper.CAMPAIGN_TYPE_RECRUIT)) {
                viewHolder.tv_count_down.setText(getCountDownTime(item, true, viewHolder.tv_actiontype));
            } else {
                viewHolder.tv_count_down.setText(getCountDownTime(item, false, viewHolder.tv_actiontype));
            }

            if (DetailContentHelper.CAMPAIGN_TYPE_RECRUIT.equals(item.getCampaign().getPer_budget_type())) {
                String recruitEndTime = item.getCampaign().getRecruit_end_time();
                long recruitEndTimeL = DateUtil.getTimeLong(recruitEndTime);

                if (! TextUtils.isEmpty(status) && (status.equals("running") || status.equals("executing")) && recruitEndTimeL <= System.currentTimeMillis()) {// "报名已结束"
                    viewHolder.tv_over.setVisibility(View.VISIBLE);
                    viewHolder.tv_over.setText(R.string.sign_end);
                } else {
                    viewHolder.tv_over.setVisibility(View.GONE);
                }
            } else {
                viewHolder.tv_over.setVisibility(View.GONE);
            }
        } else if (activityStatues == EXECUTED) {//已结束
            //  viewHolder.ll_over_show.setVisibility(View.VISIBLE);
            viewHolder.iv_cover.setBackgroundColor(mActivity.getResources().getColor(R.color.cover_transparent_deep));
            viewHolder.tv_actiontype.setTextColor(Color.WHITE);//白色
            viewHolder.tv_over.setVisibility(View.VISIBLE);
            if (judgeEnterActivity(item) == JOINED) {
                if (TextUtils.isEmpty(item.getImg_status())) {
                    viewHolder.tv_over.setText(R.string.ending);
                } else {
                    if (status.equals("executed") || (! item.getImg_status().equals("passed"))) {
                        viewHolder.tv_over.setText(R.string.ending);
                    }
                    if (status.equals("settled") || (status.equals("executed") && item.getImg_status().equals("passed"))) {
                        viewHolder.tv_over.setText(R.string.has_been_end);
                    }
                }
            } else {
                viewHolder.tv_over.setText(R.string.has_been_end);
            }

            viewHolder.payinfoLinearLayout.setVisibility(View.GONE);
        }
        if (judgeEnterActivity(item) != JOINED) {
            //活动预算 < 活动单价 已抢完visible
            if ((remainBudget - per_action_budget) >= 0) {
                viewHolder.ll_over_show.setVisibility(View.GONE);
            } else {
                viewHolder.ll_over_show.setVisibility(View.VISIBLE);
            }
        } else {
            viewHolder.ll_over_show.setVisibility(View.GONE);
        }
        //  viewHolder.tv_buget.setText(StringUtil.deleteZero(String.valueOf(item.getCampaign().getPer_action_budget())));//设置转发金额
    }

    /**
     获取与结束时间的时间差
     @return
     */
    private String getCountDownTime(CampaignListBean.CampaignInviteEntity bean, boolean isRecruit, TextView tv) {
        if (bean == null) {
            return "";
        }
        String info = "";
        if (isRecruit) {
            info = "距报名截止";
        } else {
            info = "距活动结束";
        }
        tv.setText(info);
        StringBuffer sb = new StringBuffer("");
        String deadline = null;
        if (isRecruit) {
            deadline = bean.getCampaign().getRecruit_end_time();
        } else {
            deadline = bean.getCampaign().getDeadline();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            Date deadDate = sdf.parse(deadline);
            Date currentDate = new Date();
            long time1 = deadDate.getTime();
            long time2 = currentDate.getTime();
            long test = Math.abs(time2 - time1);
            long day = test / (1000 * 60 * 60 * 24);
            long hour = test % (1000 * 60 * 60 * 24) / (1000 * 60 * 60);//- day * 24;
            long minute = test % (1000 * 60 * 60 * 24) % (1000 * 60 * 60) / (1000 * 60);
            if (day == 0) {
                sb.append(hour).append("小时");
                sb.append(minute).append("分钟");
            } else {
                sb.append(day).append("天");
                sb.append(hour).append("小时");
                sb.append(minute).append("分钟");
            }


            if (day <= 0 && hour <= 0 && minute <= 0) {
                return "已结束";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String count_down(int sum) {

        String showTime = "";
        int day = sum / 60 / 60 / 24;
        // 先获取个秒数值
        int sec = sum % 60;
        // 如果大于60秒，获取分钟。（秒数）
        int sec_time = sum / 60;
        // 再获取分钟
        int min = sec_time % 60;
        // 如果大于60分钟，获取小时（分钟数）。
        int min_time = sec_time / 60;
        // 获取小时
        int hour = min_time % 24;
        // 剩下的自然是天数
        day = min_time / 24;

        showTime = min + "分" + sec + "秒";
        return showTime;

    }

    /**
     活动类型——招募
     @param item
     @param viewHolder
     @param activityStatues
     */
    private void setRecruitTypeView(CampaignListBean.CampaignInviteEntity item, ViewHolder viewHolder, int activityStatues) {

        if (activityStatues == EXECUTED) {//活动已结束
            // viewHolder.earnLinearLayout.setVisibility(View.GONE);
            viewHolder.ll_over_show.setVisibility(View.VISIBLE);
            viewHolder.payinfoLinearLayout.setVisibility(View.GONE);
        }
        viewHolder.tv_remainder.setText(String.valueOf(item.getCampaign().getMax_action()));//结束时已花费金额
        viewHolder.iv_tag.setVisibility(View.VISIBLE);
        viewHolder.iv_tag.setBackgroundResource(R.mipmap.icon_task_tag_recruit);
        viewHolder.tv_last.setText("招募人数");
    }

    /**
     活动类型——邀请
     @param item
     @param viewHolder
     @param activityStatues
     */
    private void setInViteTypeView(CampaignListBean.CampaignInviteEntity item, ViewHolder viewHolder, int activityStatues) {

        if (activityStatues == EXECUTED) {//活动已结束
            viewHolder.earnLinearLayout.setVisibility(View.GONE);
        }
        viewHolder.tv_remainder.setText(String.valueOf(item.getCampaign().getMax_action()));//剩余金额
        viewHolder.iv_tag.setVisibility(View.VISIBLE);
        viewHolder.iv_tag.setBackgroundResource(R.mipmap.icon_task_tag_invite);
        viewHolder.tv_last.setText("特邀人数");
    }

    /**
     活动类型——普通
     @param item
     @param viewHolder
     @param activityStatues
     */
    private void setNormalTypeView(CampaignListBean.CampaignInviteEntity item, ViewHolder viewHolder, int activityStatues) {

        if (activityStatues == EXECUTING) {//活动未结束
            viewHolder.tv_last.setText("最多可赚");
            viewHolder.tv_remainder.setText("¥ " + StringUtil.deleteZero(String.valueOf(item.getCampaign().getBudget())));//剩余金额
        } else if (activityStatues == EXECUTED) {//活动已结束
            // viewHolder.tv_last.setText("已抢完");
            viewHolder.payinfoLinearLayout.setVisibility(View.GONE);
            viewHolder.ll_over_show.setVisibility(View.VISIBLE);
            // viewHolder.tv_remainder.setText("¥ " + StringUtil.deleteZero(String.valueOf(item.getCampaign().getTake_budget())));//结束时已花费金额
        }

        String per_budget_type = item.getCampaign().getPer_budget_type();
        if (CAMPAIGN_TYPE_CPI.equals(per_budget_type) || CAMPAIGN_TYPE_CPT.equals(per_budget_type)) {
            viewHolder.iv_tag.setVisibility(View.VISIBLE);
            viewHolder.iv_tag.setBackgroundResource(R.mipmap.icon_task_tag_new);
        } else {
            viewHolder.iv_tag.setVisibility(View.GONE);
        }
    }

    /**
     判断是否参与活动
     @param item
     @return 0没参与 1参与
     */
    private int judgeEnterActivity(CampaignListBean.CampaignInviteEntity item) {

        int status = DIS_JOIN; //['pending','running','approved','rejected','finished', 'settled']'待接收','进行中',

        if (item == null) {
            return status;
        }
        if (! TextUtils.isEmpty(item.getStatus()) && JOIN_SIGN.contains(item.getStatus())) {
            status = JOINED;
        } else {
            status = DIS_JOIN;
        }
        return status;
    }

    /**
     判断活动是否结束
     @param item
     @return 1进行中 2已结束
     */
    private int judgeActivityStatues(CampaignListBean.CampaignInviteEntity item) {

        int status = EXECUTING;
        if (item == null || item.getCampaign() == null) {
            return status;
        }

        if (EXECUTED_SIGN.contains(item.getCampaign().getStatus())) {
            status = EXECUTED;
        } else {
            status = EXECUTING;
        }
        if (item.getCampaign().getStatus().equals("countdown")) {
            status = READYBEGIN;
        }
        return status;
    }

    public void notifyDataSetChanged(List<CampaignListBean.CampaignInviteEntity> mInviteEntityList) {

        notifyDataSetChanged();
    }

    static class ViewHolder {

        public LineSpaceTextView aliasTextView;
        public LinearLayout payinfoLinearLayout;
        public LinearLayout earnLinearLayout;
        public ImageView ivImageView;
        public ImageView iv_cover;
        public ImageView iv_cover2;
        public TextView tv_title;
        //public TextView tv_buget;
        public TextView tv_remainder;
        //  public TextView tv_earn_num_thistime;
        public TextView tv_earn_money;
        public TextView tv_actiontype;
        //  public TextView tv_earn_click;
        public TextView tv_over;
        public TextView tv_earn_had;
        public TextView tv_last;
        public ImageView iv_tag;
        public View fl_content;
        //  public LinearLayout ll_count_down;
        public TextView tv_count_down;//倒计时
        public LinearLayout ll_over_show;
        public LinearLayout ll_show_money;
        public LinearLayout ll_will_begin;

    }

    private class CorrectionRunnable implements Runnable {

        private View flContent;
        private ImageView ivImageView;

        public CorrectionRunnable(ImageView ivImageView, View flContent) {

            this.ivImageView = ivImageView;
            this.flContent = flContent;
        }

        @Override
        public void run() {

            ivImageView.getLayoutParams().height = DensityUtils.getScreenWidth(ivImageView.getContext()) * 9 / 16;
            flContent.getLayoutParams().height = DensityUtils.getScreenWidth(ivImageView.getContext()) * 9 / 16;
        }
    }

    // mapStart.put(position, deal_time - now_time);
    //    CountDownTimer tc = timeMap.get(viewHolder.tv_count_down);
    //            if (tc != null) {
    //        tc.cancel();
    //        tc = null;
    //    }
    //    CountDownTimer makeValueID = new Couterdown((mapStart.get(position)) * 1000, 1000) {
    //
    //        @Override
    //        public void onFinish() {
    //            super.onFinish();
    //            viewHolder.earnLinearLayout.setVisibility(View.GONE);
    //            viewHolder.ll_show_money.setVisibility(View.VISIBLE);
    //            viewHolder.ll_will_begin.setVisibility(View.GONE);
    //            setCampaignTypeView(item, viewHolder, judgeActivityStatues(item));
    //            setCampaignStateView(item, viewHolder, judgeEnterActivity(item), judgeActivityStatues(item));
    //        }
    //
    //        @Override
    //        public void onTick(long millisUntilFinished) {
    //            viewHolder.tv_actiontype.setText("距活动开始");
    //            viewHolder.tv_count_down.setText("距开始" + getHours(millisUntilFinished));
    //            viewHolder.iv_cover.setBackgroundColor(mActivity.getResources().getColor(R.color.cover_transparent_deep));
    //            viewHolder.earnLinearLayout.setVisibility(View.VISIBLE);
    //            viewHolder.ll_show_money.setVisibility(View.GONE);
    //            viewHolder.ll_will_begin.setVisibility(View.VISIBLE);
    //        }
    //
    //        @Override
    //        public String getHours(long time) {
    //            return super.getHours(time);
    //        }
    //
    //    }.start();
    //            timeMap.put(viewHolder.tv_count_down, (Couterdown) makeValueID);
    class Couterdown extends CountDownTimer {

        public Couterdown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTick(long arg0) {
        }

        public String getHours(long time) {
            long second = time / 1000;
            long hour = second / 60 / 60;
            long minute = (second - hour * 60 * 60) / 60;
            long sec = (second - hour * 60 * 60) - minute * 60;

            String rHour = "";
            String rMin = "";
            String rSs = "";
            // 时
            if (hour < 10) {
                rHour = "0" + hour;
            } else if (hour == 0) {
                rHour = "0";
            } else {
                rHour = hour + "";
            }
            // 分
            if (minute < 10) {
                rMin = "0" + minute;
            } else if (minute == 0) {
                rMin = "0";
            } else {
                rMin = minute + "";
            }
            // 秒
            if (sec < 10) {
                rSs = "0" + sec;
            } else if (sec == 0) {
                rSs = "0";
            } else {
                rSs = sec + "";
            }
            //            if (TextUtils.isEmpty(rHour)){
            //                return rMin+"分钟"+rSs+"秒";
            //            }else if (TextUtils.isEmpty(rMin)){
            //
            //            }else if (TextUtils.isEmpty(rHour))
            return rHour + "小时" + rMin + "分" + rSs;

        }
    }

    public static String getCountTimeByLong(long finishTime) {
        int totalTime = (int) (finishTime / 1000);//秒
        int hour = 0, minute = 0, second = 0;

        if (3600 <= totalTime) {
            hour = totalTime / 3600;
            totalTime = totalTime - 3600 * hour;
        }
        if (60 <= totalTime) {
            minute = totalTime / 60;
            totalTime = totalTime - 60 * minute;
        }
        if (0 <= totalTime) {
            second = totalTime;
        }
        StringBuilder sb = new StringBuilder();

        if (hour < 10) {
            sb.append("0").append(hour).append("小时");
        } else {
            sb.append(hour).append("小时");
        }
        if (minute < 10) {
            sb.append("0").append(minute).append("分钟");
        } else {
            sb.append(minute).append("分钟");
        }
        if (second < 10) {
            sb.append("0").append(second).append("秒");
        } else {
            sb.append(second).append("秒");
        }
        return sb.toString();

    }

}