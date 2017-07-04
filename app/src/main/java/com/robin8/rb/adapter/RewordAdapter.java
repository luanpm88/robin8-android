package com.robin8.rb.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.model.CampaignListBean;
import com.robin8.rb.module.reword.activity.DetailContentActivity;
import com.robin8.rb.module.reword.helper.DetailContentHelper;
import com.robin8.rb.ui.widget.LineSpaceTextView;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.DateUtil;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.StringUtil;

import org.jsoup.helper.DataUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Figo on 2016/6/30.
 */
public class RewordAdapter extends BaseAdapter {

    public final static String JOIN_SIGN = "approved/rejected/finished/settled";
    public final static String EXECUTED_SIGN = "executed/settled";
    public final static int DIS_JOIN = 0;//未参加
    public final static int JOINED = 1;//已参加
    public final static int EXECUTING = 2;//进行中
    public final static int EXECUTED = 3;//已结束

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


    public RewordAdapter(Activity activity, List<CampaignListBean.CampaignInviteEntity> list) {

        mList = list;
        mActivity = activity;
        mLayoutInflater = LayoutInflater.from(activity);

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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.reword_list_item, null, false);
            viewHolder.aliasTextView = (LineSpaceTextView) convertView.findViewById(R.id.tv_alias);
            viewHolder.payinfoLinearLayout = (LinearLayout) convertView.findViewById(R.id.ll_payinfo);
            viewHolder.earnLinearLayout = (LinearLayout) convertView.findViewById(R.id.ll_earn);
            viewHolder.ivImageView = (ImageView) convertView.findViewById(R.id.iv_bg);
            viewHolder.iv_cover = (ImageView) convertView.findViewById(R.id.iv_cover);
            viewHolder.iv_cover2 = (ImageView) convertView.findViewById(R.id.iv_cover2);
            viewHolder.iv_tag = (ImageView) convertView.findViewById(R.id.iv_tag);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            //  viewHolder.tv_buget = (TextView) convertView.findViewById(R.id.tv_buget);
            viewHolder.tv_remainder = (TextView) convertView.findViewById(R.id.tv_remainder);
           // viewHolder.tv_earn_num_thistime = (TextView) convertView.findViewById(R.id.tv_earn_click_numNow);//点击数
            viewHolder.tv_earn_money = (TextView) convertView.findViewById(R.id.tv_earn_money);
           viewHolder.tv_actiontype = (TextView) convertView.findViewById(R.id.tv_actiontype);//类型
        //    viewHolder.tv_earn_click = (TextView) convertView.findViewById(R.id.tv_earn_click_num);
            viewHolder.tv_over = (TextView) convertView.findViewById(R.id.tv_over);
            viewHolder.tv_earn_had = (TextView) convertView.findViewById(R.id.tv_earn_had);
            viewHolder.tv_last = (TextView) convertView.findViewById(R.id.tv_last);//最多可赚
            viewHolder.fl_content = convertView.findViewById(R.id.fl_content);
            //  viewHolder.ll_count_down = ((LinearLayout) convertView.findViewById(R.id.ll_count_down));
            viewHolder.tv_count_down = (TextView) convertView.findViewById(R.id.tv_count_down);
            // viewHolder.tv_earn_click = (TextView) convertView.findViewById(R.id.tv_count_down);
            viewHolder.ll_over_show = (LinearLayout) convertView.findViewById(R.id.ll_over_show);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final CampaignListBean.CampaignInviteEntity item = getItem(position);

        if (item == null) {
            return null;
        }

     //   setClickAndTitleTV(viewHolder, item);
                if (item.getCampaign().getName().length() > 20) {
                    viewHolder.tv_title.setText(item.getCampaign().getName().substring(0, 20) + "...");
                } else {
                    viewHolder.tv_title.setText(item.getCampaign().getName());//标题
                }
                viewHolder.aliasTextView.setText(item.getCampaign().getBrand_name());//商标
        viewHolder.ivImageView.post(new CorrectionRunnable(viewHolder.ivImageView, viewHolder.fl_content));
        BitmapUtil.loadImage(mActivity.getApplicationContext(), item.getCampaign().getImg_url(), viewHolder.ivImageView, R.color.sub_gray_custom);

        setCampaignTypeView(item, viewHolder, judgeActivityStatues(item));
        setCampaignStateView(item, viewHolder, judgeEnterActivity(item),judgeActivityStatues(item));


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
        return convertView;
    }

    /**
     * 设置活动进行状态
     *
     * @param item
     * @param viewHolder
     * @param enterActivityStatus
     */
    private void setCampaignStateView(CampaignListBean.CampaignInviteEntity item, ViewHolder viewHolder, int enterActivityStatus,int activityStatues) {

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
            if (activityStatues==EXECUTED){
                viewHolder.payinfoLinearLayout.setVisibility(View.GONE);
                viewHolder.iv_cover2.setVisibility(View.VISIBLE);
                viewHolder.earnLinearLayout.setVisibility(View.GONE);
            }else {
                viewHolder.iv_cover2.setVisibility(View.VISIBLE);
                //   viewHolder.payinfoLinearLayout.setVisibility(View.VISIBLE);
                viewHolder.payinfoLinearLayout.setVisibility(View.VISIBLE);
                viewHolder.earnLinearLayout.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置活动种类
     *
     * @param item
     * @param viewHolder
     * @param activityStatues
     */
    private void setCampaignTypeView(CampaignListBean.CampaignInviteEntity item, ViewHolder viewHolder, int activityStatues) {

        String per_budget_type = item.getCampaign().getPer_budget_type();
        if (TextUtils.isEmpty(per_budget_type)) {
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
            viewHolder.ll_over_show.setVisibility(View.GONE);
         //   viewHolder.ll_count_down.setVisibility(View.VISIBLE);
//            int deadLine_time = new Long(DateUtil.getTimeLong(item.getCampaign().getDeadline())).intValue();
//            int now_time = new Long(DateUtil.getTimeLong(DateUtil.getNowTimeMs("yyyyMMddHHmmssSSS"))).intValue();
//            if (deadLine_time - now_time > 0) {
//                viewHolder.tv_count_down.setText(count_down((deadLine_time - now_time) / 1000));
//            }else {
//                viewHolder.ll_count_down.setVisibility(View.GONE);
//            }
            if (item.getInvite_status().equals(DetailContentHelper.CAMPAIGN_TYPE_RECRUIT)){
                viewHolder.tv_count_down.setText( getCountDownTime(item, true,viewHolder.tv_actiontype));

            }else {
                viewHolder.tv_count_down.setText( getCountDownTime(item, false,viewHolder.tv_actiontype));
            }


            if (DetailContentHelper.CAMPAIGN_TYPE_RECRUIT.equals(item.getCampaign().getPer_budget_type())) {
                String recruitEndTime = item.getCampaign().getRecruit_end_time();
                long recruitEndTimeL = DateUtil.getTimeLong(recruitEndTime);
                String status = item.getCampaign().getStatus();
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
            LogUtil.logXXfigo("已结束" + item.getCampaign().getPer_budget_type() + item.getCampaign().getName());
          //  viewHolder.ll_count_down.setVisibility(View.GONE);
            viewHolder.ll_over_show.setVisibility(View.VISIBLE);
            viewHolder.iv_cover.setBackgroundColor(mActivity.getResources().getColor(R.color.cover_transparent_deep));
            viewHolder.tv_actiontype.setTextColor(Color.WHITE);//白色
            viewHolder.tv_over.setVisibility(View.VISIBLE);
            viewHolder.tv_over.setText(R.string.has_been_end);
            viewHolder.payinfoLinearLayout.setVisibility(View.GONE);
        }
        //  viewHolder.tv_buget.setText(StringUtil.deleteZero(String.valueOf(item.getCampaign().getPer_action_budget())));//设置转发金额
    }
    /**
     * 获取与结束时间的时间差
     *
     * @return
     */
    private String getCountDownTime(CampaignListBean.CampaignInviteEntity bean, boolean isRecruit,TextView tv) {
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
            if (day==0){
                sb.append(hour).append("小时");
                sb.append(minute).append("分钟");
            }else {
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
        Log.e("获得天数", "--->" + day);
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

        showTime = day + "天" + hour + "小时" + min + "分";
        return showTime;

    }

    /**
     * 活动类型——招募
     *
     * @param item
     * @param viewHolder
     * @param activityStatues
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
     * 活动类型——邀请
     *
     * @param item
     * @param viewHolder
     * @param activityStatues
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
     * 活动类型——普通
     *
     * @param item
     * @param viewHolder
     * @param activityStatues
     */
    private void setNormalTypeView(CampaignListBean.CampaignInviteEntity item, ViewHolder viewHolder, int activityStatues) {

        if (activityStatues == EXECUTING) {//活动未结束
            viewHolder.tv_last.setText("最多可赚");
            viewHolder.tv_remainder.setText("¥ " + StringUtil.deleteZero(String.valueOf(item.getCampaign().getRemain_budget())));//剩余金额
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
     * 判断是否参与活动
     *
     * @param item
     * @return 0没参与 1参与
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
     * 判断活动是否结束
     *
     * @param item
     * @return 1进行中 2已结束
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
//
//    private void setClickAndTitleTV(ViewHolder viewHolder, CampaignListBean.CampaignInviteEntity item) {
//
//        switch (item.getCampaign().getPer_budget_type()) {
//            case CAMPAIGN_TYPE_CLICK:
//                viewHolder.tv_actiontype.setText("点击");//活动类型
//                viewHolder.tv_earn_click.setText("点击数");
//                break;
//            case CAMPAIGN_TYPE_POST:
//                viewHolder.tv_actiontype.setText("转发");
//                viewHolder.tv_earn_click.setText("转发");
//                break;
//            case CAMPAIGN_TYPE_CPA:
//                viewHolder.tv_actiontype.setText("效果");
//                viewHolder.tv_earn_click.setText("效果");
//                break;
//            case CAMPAIGN_TYPE_RECRUIT:
//                viewHolder.tv_actiontype.setText("招募");
//                viewHolder.tv_earn_click.setText("招募");
//                break;
//            case CAMPAIGN_TYPE_INVITE:
//                viewHolder.tv_actiontype.setText("特邀");
//                viewHolder.tv_earn_click.setText("特邀");
//                break;
//            case CAMPAIGN_TYPE_CPI:
//                viewHolder.tv_actiontype.setText("下载");
//                viewHolder.tv_earn_click.setText("下载");
//                break;
//            case CAMPAIGN_TYPE_CPT:
//                viewHolder.tv_actiontype.setText("任务");
//                viewHolder.tv_earn_click.setText("任务");
//                break;
//            default:
//                viewHolder.tv_actiontype.setText("点击");
//                viewHolder.tv_earn_click.setText("点击");
//                break;
//        }
//    }
//
}