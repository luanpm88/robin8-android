package com.robin8.rb.module.reword.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.http.xutil.DefaultHttpCallBack;
import com.robin8.rb.http.xutil.IHttpCallBack;
import com.robin8.rb.model.BaseBean;
import com.robin8.rb.model.CampaignInviteBean;
import com.robin8.rb.model.CampaignListBean;
import com.robin8.rb.module.first.model.KolDetailModel;
import com.robin8.rb.module.first.model.SocialAccountsBean;
import com.robin8.rb.module.mine.model.MineShowModel;
import com.robin8.rb.module.mine.presenter.BindSocialPresenter;
import com.robin8.rb.module.reword.activity.DetailContentActivity;
import com.robin8.rb.module.reword.activity.ScreenImgActivity;
import com.robin8.rb.module.reword.activity.SignUpRecruitActivity;
import com.robin8.rb.module.reword.chose_photo.SerializableMap;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.SwipeBackDialog;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.DateUtil;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.view.widget.CircleImageView;
import com.robin8.rb.view.widget.CustomDialog;
import com.robin8.rb.view.widget.CustomDialogManager;
import com.tendcloud.appcpa.TalkingDataAppCpa;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import static com.robin8.rb.base.BaseApplication.isDoubleClick;

/**
 活动详情辅助页面 */
public class DetailContentHelper {

    private static final String CAMPAIGN_TYPE_CPI = "simple_cpi";//cpi
    private static final String CAMPAIGN_TYPE_CPT = "cpt";//cpt
    private static final String CAMPAIGN_TYPE_INVITE = "invite";//邀请
    public static final String CAMPAIGN_TYPE_RECRUIT = "recruit";//招募
    private static final String CAMPAIGN_TYPE_CLICK = "click";//点击
    private static final String CAMPAIGN_TYPE_POST = "post";//转发
    private static final String CAMPAIGN_TYPE_CPA = "cpa";//效果

    public static final String RUNNING = "running";// 待接受
    public static final String SETTLED = "settled";//参与完成已结算
    public static final String REJECTED = "rejected";//参与失败
    public static final String MISSED = "missed";//已错失
    public static final String STATE_PENDING = "pending";
    public static final String STATE_RUNNING = "running";
    public static final String STATE_APPROVED = "approved";
    public static final String STATE_FINISHED = "finished";
    public static final String STATE_SETTLED = "settled";
    public static final String STATE_REJECTED = "rejected";
    public static final String STATE_MISSED = "missed";
    public static final String STATE_PASSED = "passed";
    public static final String STATE_APPLYING = "applying";
    public static final String STATE_REAGY_BEGIN = "countdown";

    public static final int DISALLOW_CLICK = - 1;
    public static final int CLICK_REJECT_CAMPAIGN = 1;//拒绝此活动
    public static final int CLICK_UPLOAD_SCREENSHOT = 2;//上传截图
    public static final int CLICK_RECEIVE_CAMPAIGN = 3;//接受此活动
    public static final int CLICK_CHECK_SCREENSHOT = 4;//查看截图
    public static final int CLICK_UPLOAD_AGAIN = 5;//重新上传
    public static final int CLICK_CHECK_REJECT_REASON = 6;//审核拒绝-查看原因
    public static final int CLICK_SHARE = 7;// 分享
    public static final int CLICK_SIGN_UP = 8;// 招募活动报名
    public static final int CLICK_SHARE_NO_PAY = 9;// 无偿转发
    public static final int CLICK_UPLOAD_AND_CHECK = 10;// 截图审核中弹框增加查看截图
    //  public static final int CLICK_SHARE_NO_IMG = 11;// 截图审核中弹框增加查看截图
    public static final int IMAGE_REQUEST_CODE = 101;
    public static final int IMAGE_REQUEST_LOOK_CODE = 102;
    public static final int IMAGE_REQUEST_MORE_IMG_CODE = 114;
    public static final int IMAGE_REQUEST_PUT_RESULT = 115;
    private StringBuffer sb = new StringBuffer("");
    private View line;
    private TextView tvRight;
    private TextView tvLeft;
    private int mCurrentLeftState = DISALLOW_CLICK;
    private int mCurrentRightState = DISALLOW_CLICK;
    private BasePresenter mBasePresenter;
    private WProgressDialog mWProgressDialog;
    private String mCampaignInviteEntityId;
    private CampaignListBean.CampaignInviteEntity mCampaignInviteEntity;
    private CampaignListBean mEntity;
    private RequestParams mRequestParams;
    private final String BACKSLASH = "/";
    private String TAG = "null";
    public static String SUCCESS = "null";
    private String wechatEn = "wechat";
    private String weiboEn = "weibo";

    public DetailContentHelper(View line, TextView tvRight, TextView tvLeft) {

        this.line = line;
        this.tvRight = tvRight;
        this.tvLeft = tvLeft;
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            int time = msg.what;

            setCountTimeView(time, tvLeft);
            time = time - 1;

            if (time < 0) {
                return;
            }

            Message msg1 = handler.obtainMessage();
            msg1.what = time;
            handler.sendMessageDelayed(msg1, 1000);
        }
    };

    public void updateBottomShareView(CampaignListBean.CampaignInviteEntity bean) {

        if (bean == null || bean.getCampaign() == null || TextUtils.isEmpty(bean.getCampaign().getPer_budget_type()) || TextUtils.isEmpty(bean.getStatus())) {
            return;
        }

        mCampaignInviteEntity = bean;
        mCampaignInviteEntityId = String.valueOf(mCampaignInviteEntity.getId());

        switch (bean.getCampaign().getPer_budget_type()) {
            case CAMPAIGN_TYPE_INVITE:
                updateBottomShareViewInvite(bean, tvLeft, tvRight, line);
                break;
            case CAMPAIGN_TYPE_RECRUIT:
                updateBottomShareViewRecruit(bean, tvLeft, tvRight, line);
                break;
            default:
                updateBottomShareViewNormal(bean, tvLeft, tvRight, line);
                break;
        }
    }

    /**
     特邀活动-底部按钮显示逻辑
     @param bean
     @param tvLeft
     @param tvRight
     @param line
     */
    private void updateBottomShareViewInvite(CampaignListBean.CampaignInviteEntity bean, TextView tvLeft, TextView tvRight, View line) {

        String status = bean.getStatus();
        switch (status) {
            case STATE_PENDING:// 整个显示 @"活动未开始"
                setBottomView(false, tvLeft, tvRight, line, R.string.campaign_unstart);
                mCurrentLeftState = DISALLOW_CLICK;
                mCurrentRightState = DISALLOW_CLICK;
                break;
            case STATE_RUNNING://左边显示 @"拒绝此活动"  //  右边显示 @"接受此活动"
                setBottomView(tvLeft, tvRight, line, R.string.reject_campaign, R.string.receive_this_campaign);
                mCurrentLeftState = CLICK_REJECT_CAMPAIGN;
                mCurrentRightState = CLICK_RECEIVE_CAMPAIGN;
                break;
            case STATE_APPROVED:
            case STATE_FINISHED:
                setBottomViewWhenFinished(bean, tvLeft, tvRight, line);
                break;
            case STATE_SETTLED: // 整个显示 @"活动已完成"
                setBottomView(false, tvLeft, tvRight, line, R.string.campaign_compeleted);
                mCurrentLeftState = DISALLOW_CLICK;
                mCurrentRightState = DISALLOW_CLICK;
                break;
            case STATE_REJECTED:// 整个显示 @"活动审核失败"
                setBottomView(false, tvLeft, tvRight, line, R.string.campaign_approve_failed);
                mCurrentLeftState = DISALLOW_CLICK;
                mCurrentRightState = DISALLOW_CLICK;
                break;
            case STATE_MISSED:// 整个显示 @"活动已错失"
                setBottomView(false, tvLeft, tvRight, line, R.string.campaign_missed);
                mCurrentLeftState = DISALLOW_CLICK;
                mCurrentRightState = DISALLOW_CLICK;
                break;
            case STATE_REAGY_BEGIN:
                setBottomView(false, tvLeft, tvRight, line, R.string.campaign_ready_begin);
                mCurrentLeftState = DISALLOW_CLICK;
                mCurrentRightState = DISALLOW_CLICK;
                break;
        }
    }


    /**
     招募活动-底部按钮显示逻辑
     @param bean
     @param tvLeft
     @param tvRight
     @param line
     */
    private void updateBottomShareViewRecruit(CampaignListBean.CampaignInviteEntity bean, TextView tvLeft, TextView tvRight, View line) {

        String status = bean.getStatus();
        switch (status) {
            case STATE_PENDING:
                setBottomView(false, tvLeft, tvRight, line, R.string.campaign_unstart);
                mCurrentLeftState = DISALLOW_CLICK;
                mCurrentRightState = DISALLOW_CLICK;
                break;

            case STATE_RUNNING:
                String recruitEndTime = bean.getCampaign().getRecruit_end_time();
                long recruitEndTimeL = DateUtil.getTimeLong(recruitEndTime);
                if (recruitEndTimeL <= System.currentTimeMillis()) {// 整个显示 @ "报名已结束"
                    setBottomView(false, tvLeft, tvRight, line, R.string.sign_up_end);
                    mCurrentLeftState = DISALLOW_CLICK;
                    mCurrentRightState = DISALLOW_CLICK;
                } else {// 整个显示 @ "立即报名"
                    setBottomView(true, tvLeft, tvRight, line, R.string.sign_up_instantly);
                    mCurrentLeftState = CLICK_SIGN_UP;
                    mCurrentRightState = CLICK_SIGN_UP;
                }
                break;

            case STATE_APPLYING:// 整个显示 @ "活动已报名,正在审核"
                setBottomView(false, tvLeft, tvRight, line, R.string.campaign_sign_up_checking);
                mCurrentLeftState = DISALLOW_CLICK;
                mCurrentRightState = DISALLOW_CLICK;
                break;

            case STATE_APPROVED:
            case STATE_FINISHED:
                setBottomViewRecruitWhenFinished(bean, tvLeft, tvRight, line);
                break;

            case STATE_SETTLED: // 整个显示 @"活动已完成"
                setBottomView(false, tvLeft, tvRight, line, R.string.campaign_compeleted);
                mCurrentLeftState = DISALLOW_CLICK;
                mCurrentRightState = DISALLOW_CLICK;
                break;
            case STATE_REJECTED:// 整个显示 @"报名审核失败"
                setBottomView(false, tvLeft, tvRight, line, R.string.sign_up_approve_failed);
                mCurrentLeftState = DISALLOW_CLICK;
                mCurrentRightState = DISALLOW_CLICK;
                break;
            case STATE_MISSED:// 整个显示 @"活动已错失"
                setBottomView(false, tvLeft, tvRight, line, R.string.campaign_missed);
                mCurrentLeftState = DISALLOW_CLICK;
                mCurrentRightState = DISALLOW_CLICK;
                break;
            case STATE_REAGY_BEGIN:
                setBottomView(false, tvLeft, tvRight, line, R.string.campaign_ready_begin);
                mCurrentLeftState = DISALLOW_CLICK;
                mCurrentRightState = DISALLOW_CLICK;
                break;
        }
    }

    private void setBottomViewRecruitWhenFinished(CampaignListBean.CampaignInviteEntity bean, TextView tvLeft, TextView tvRight, View line) {

        String startTime = bean.getCampaign().getStart_time();
        long startTimeL = DateUtil.getTimeLong(startTime);
        if (startTimeL >= System.currentTimeMillis()) {// 整个显示 @ "活动报名成功"
            if (TextUtils.isEmpty(bean.getCampaign().getPer_action_type())) {
                setBottomView(false, tvLeft, tvRight, line, R.string.campaign_sign_up_success);
                mCurrentLeftState = DISALLOW_CLICK;
                mCurrentRightState = DISALLOW_CLICK;
            } else {
                setBottomViewWhenFinished(bean, tvLeft, tvRight, line);
            }
        } else {
            boolean can_upload_screenshot = bean.isCan_upload_screenshot();
            String screenshot = bean.getScreenshot();
            if (startTimeL >= System.currentTimeMillis() || TextUtils.isEmpty(screenshot) && ! can_upload_screenshot) {// 整个显示 @ "立即分享"
                setBottomView(true, tvLeft, tvRight, line, getTextId(bean));
                mCurrentLeftState = CLICK_SHARE;
                mCurrentRightState = CLICK_SHARE;
            } else {
                setBottomViewNormalWhenFinished(bean, tvLeft, tvRight, line);
            }
        }

    }

    private int getTextId(CampaignListBean.CampaignInviteEntity bean) {
        int textId = R.string.share_for_money;
        //        String per_action_type = bean.getCampaign().getPer_action_type();
        //        String wechat_auth_type = bean.getCampaign().getWechat_auth_type();
        //        int textId = R.string.share_to_wechatmoments;
        //        if (! TextUtils.isEmpty(wechat_auth_type) && wechat_auth_type.equals("self_info")) {
        //            textId = R.string.share_to_wechat_favorites;
        //        } else {
        //            if (TextUtils.isEmpty(per_action_type) || "wechat".equals(per_action_type)) {
        //                textId = R.string.share_to_wechatmoments;
        //            } else if ("weibo".equals(per_action_type)) {
        //                textId = R.string.share_to_weibo;
        //            } else if ("qq".equals(per_action_type)) {
        //                textId = R.string.share_to_qzone;
        //            }
        //        }
        return textId;
    }

    /**
     悬赏活动-底部按钮显示逻辑
     @param bean
     @param tvLeft
     @param tvRight
     @param line
     */
    private void updateBottomShareViewNormal(CampaignListBean.CampaignInviteEntity bean, TextView tvLeft, TextView tvRight, View line) {

        String status = bean.getStatus();
        switch (status) {
            case STATE_PENDING:// 整个显示 @"活动未开始"
                setBottomView(false, tvLeft, tvRight, line, R.string.campaign_unstart);
                mCurrentLeftState = DISALLOW_CLICK;
                mCurrentRightState = DISALLOW_CLICK;
                break;
            case STATE_RUNNING://整个显示 @ "立即分享"
                setBottomView(true, tvLeft, tvRight, line, getTextId(bean));
                mCurrentLeftState = CLICK_SHARE;
                mCurrentRightState = CLICK_SHARE;
                break;
            case STATE_APPROVED:
            case STATE_FINISHED:
                setBottomViewNormalWhenFinished(bean, tvLeft, tvRight, line);
                break;
            case STATE_SETTLED: // 整个显示 @"活动已完成"
                setBottomView(false, tvLeft, tvRight, line, R.string.campaign_compeleted);
                mCurrentLeftState = DISALLOW_CLICK;
                mCurrentRightState = DISALLOW_CLICK;
                break;
            case STATE_REJECTED:// 整个显示 @"活动审核失败"
                setBottomView(false, tvLeft, tvRight, line, R.string.campaign_approve_failed);
                mCurrentLeftState = DISALLOW_CLICK;
                mCurrentRightState = DISALLOW_CLICK;
                break;
            case STATE_MISSED:// 整个显示 @"活动已错失,无偿转发"
                setBottomView(false, tvLeft, tvRight, line, R.string.campaign_missed);
                //    setBottomView(true, tvLeft, tvRight, line, R.string.campaign_missed_forwarding);
                //                mCurrentLeftState = CLICK_SHARE_NO_PAY;
                //                mCurrentRightState = CLICK_SHARE_NO_PAY;
                mCurrentLeftState = DISALLOW_CLICK;
                mCurrentRightState = DISALLOW_CLICK;
                break;
            case STATE_REAGY_BEGIN:
                setBottomView(false, tvLeft, tvRight, line, R.string.campaign_ready_begin);
                mCurrentLeftState = DISALLOW_CLICK;
                mCurrentRightState = DISALLOW_CLICK;
                break;
        }
    }

    private void setBottomViewNormalWhenFinished(CampaignListBean.CampaignInviteEntity bean, TextView tvLeft, TextView tvRight, View line) {

        String screenshot = bean.getScreenshot();
        if (TextUtils.isEmpty(screenshot)) {
            String status = bean.getStatus();
            boolean startUploadScreenshot = bean.isStart_upload_screenshot();
            boolean canUploadScreenshot = bean.isCan_upload_screenshot();
            if (STATE_FINISHED.equals(status) && startUploadScreenshot) {
                if (canUploadScreenshot) {//左边显示 @ "上传截图"   右边显示 @ "再次分享"
                    setBottomView(tvLeft, tvRight, line, R.string.upload_screenshot, R.string.share_again);
                    mCurrentLeftState = CLICK_UPLOAD_SCREENSHOT;
                    mCurrentRightState = CLICK_SHARE_NO_PAY;
                    // mCurrentRightState = CLICK_SHARE_NO_IMG;
                } else {// 左边显示 @ "上传截图时间已过"    右边显示 @ "再次分享"
                    setBottomView(tvLeft, tvRight, line, R.string.upload_screenshot_time_end, R.string.share_again);
                    mCurrentLeftState = DISALLOW_CLICK;
                    mCurrentRightState = CLICK_SHARE_NO_PAY;
                }
            } else {
                List<Integer> uploadIntervalTime = bean.getUpload_interval_time();
                if (uploadIntervalTime == null || uploadIntervalTime.size() == 0) {// 左边显示 @ "未知状态"    右边显示 @ "再次分享"
                    setBottomView(tvLeft, tvRight, line, R.string.unknown_state, R.string.share_again);
                    mCurrentLeftState = DISALLOW_CLICK;
                    mCurrentRightState = CLICK_SHARE_NO_PAY;
                } else {// 左边显示 @ "%@分%@秒 后请上传截图"     右边显示 @ "再次分享"
                    countDownTime(bean);
                    tvLeft.setText(getTimeLast(bean));
                    setBottomView(tvLeft, tvRight, line, - 1, R.string.share_again);
                    mCurrentLeftState = CLICK_UPLOAD_SCREENSHOT;
                    mCurrentRightState = CLICK_SHARE_NO_PAY;
                }
            }
        } else {
            String imgStatus = bean.getImg_status();
            if (imgStatus.equals(STATE_PASSED)) {// 整个显示 @"审核通过-查看截图"  右边显示 @"再次分享"
                setBottomView(tvLeft, tvRight, line, R.string.approved_check_the_screenshot, R.string.share_again);
                mCurrentLeftState = CLICK_CHECK_SCREENSHOT;
                // mCurrentRightState = CLICK_SHARE;
                mCurrentRightState = CLICK_SHARE_NO_PAY;
            } else if (imgStatus.equals(STATE_PENDING)) {// 左边显示 @"审核中-查看截图"   右边显示 @"重新上传"
                //原代码
                //                setBottomView(tvLeft, tvRight, line, R.string.approving_check_the_screenshot, R.string.upload_again);
                //                mCurrentLeftState = CLICK_CHECK_SCREENSHOT;
                //                mCurrentRightState = CLICK_UPLOAD_AGAIN;
                setBottomView(tvLeft, tvRight, line, R.string.upload_again, R.string.share_again);
                mCurrentLeftState = CLICK_UPLOAD_AND_CHECK;
                // mCurrentRightState = CLICK_SHARE;
                mCurrentRightState = CLICK_SHARE_NO_PAY;
            } else if (imgStatus.equals(STATE_REJECTED)) {// 左边显示 @"审核拒绝-查看原因"   右边显示 @"重新上传"
                setBottomView(tvLeft, tvRight, line, R.string.approve_reject_check_reason, R.string.upload_again);
                mCurrentLeftState = CLICK_CHECK_REJECT_REASON;
                mCurrentRightState = CLICK_UPLOAD_AGAIN;
            }
        }
    }

    private void setBottomViewWhenFinished(CampaignListBean.CampaignInviteEntity bean, TextView tvLeft, TextView tvRight, View line) {
        String screenshot = bean.getScreenshot();
        if (TextUtils.isEmpty(screenshot)) {
            boolean can_upload_screenshot = bean.isCan_upload_screenshot();
            if (can_upload_screenshot) { // 整个显示 @"上传截图"
                setBottomLeftView(true, tvLeft, tvRight, line, R.string.upload_screenshot);
                mCurrentLeftState = CLICK_UPLOAD_SCREENSHOT;
                mCurrentRightState = DISALLOW_CLICK;
            } else {// 整个显示 @"活动进行中"
                //                setBottomView(false, tvLeft, tvRight, line, R.string.campaign_is_in_the_way);
                //                mCurrentLeftState = DISALLOW_CLICK;
                //                mCurrentRightState = DISALLOW_CLICK;
                //TODO
                setBottomView(true, tvLeft, tvRight, line, getTextId(bean));
                mCurrentLeftState = CLICK_SHARE;
                mCurrentRightState = CLICK_SHARE;
            }
        } else {
            String imgStatus = bean.getImg_status();
            if (imgStatus.equals(STATE_PASSED)) {// 整个显示 @"审核通过-查看截图"
                setBottomLeftView(true, tvLeft, tvRight, line, R.string.approved_check_the_screenshot);
                mCurrentLeftState = CLICK_CHECK_SCREENSHOT;
                mCurrentRightState = DISALLOW_CLICK;
            } else if (imgStatus.equals(STATE_PENDING)) {// 左边显示 @"审核中-查看截图"   右边显示 @"重新上传"
                //                setBottomView(tvLeft, tvRight, line, R.string.approving_check_the_screenshot, R.string.upload_again);
                //                mCurrentLeftState = CLICK_CHECK_SCREENSHOT;
                //                mCurrentRightState = CLICK_UPLOAD_AGAIN;
                setBottomView(tvLeft, tvRight, line, R.string.upload_again, R.string.share_again);
                mCurrentLeftState = CLICK_UPLOAD_AND_CHECK;
                // mCurrentRightState = CLICK_SHARE;
                mCurrentRightState = CLICK_SHARE_NO_PAY;
            } else if (imgStatus.equals(STATE_REJECTED)) {// 左边显示 @"审核拒绝-查看原因"   右边显示 @"重新上传"
                setBottomView(tvLeft, tvRight, line, R.string.approve_reject_check_reason, R.string.upload_again);
                mCurrentLeftState = CLICK_CHECK_REJECT_REASON;
                mCurrentRightState = CLICK_UPLOAD_AGAIN;
            }
        }
    }

    /**
     显示左边View
     */
    private void setBottomLeftView(boolean leftClickAble, TextView tvLeft, TextView tvRight, View line, int leftText) {

        setBottomView(leftClickAble, false, true, false, tvLeft, tvRight, line, leftText, - 1);
    }

    /**
     显示右边View
     */
    private void setBottomView(boolean rightClickAble, TextView tvLeft, TextView tvRight, View line, int rightText) {

        setBottomView(false, rightClickAble, false, true, tvLeft, tvRight, line, - 1, rightText);
    }

    /**
     显示两个View（皆可点击）
     */
    private void setBottomView(TextView tvLeft, TextView tvRight, View line, int leftText, int rightText) {

        setBottomView(true, true, true, true, tvLeft, tvRight, line, leftText, rightText);
    }

    /**
     显示View
     */
    private void setBottomView(boolean leftClickAble, boolean rightClickAble, boolean leftShow, boolean rightShow, TextView tvLeft, TextView tvRight, View line, int leftText, int rightText) {

        if (leftClickAble) {
            tvLeft.setClickable(true);
            tvLeft.setBackgroundResource(R.color.blue_custom);
        } else {
            tvLeft.setClickable(false);
            tvLeft.setBackgroundResource(R.color.sub_gray_custom);
        }

        if (leftShow) {
            tvLeft.setVisibility(View.VISIBLE);
            if (leftText != - 1) {
                tvLeft.setText(leftText);
            } else {
                tvLeft.setClickable(false);
            }
        } else {
            tvLeft.setVisibility(View.GONE);
        }

        if (rightClickAble) {
            tvRight.setClickable(true);
            tvRight.setBackgroundResource(R.color.blue_custom);
        } else {
            tvRight.setClickable(false);
            tvRight.setBackgroundResource(R.color.sub_gray_custom);
        }

        if (rightShow) {
            tvRight.setVisibility(View.VISIBLE);
            if (rightText != - 1) {
                tvRight.setText(rightText);
            }
        } else {
            tvRight.setVisibility(View.GONE);
        }

        if (rightShow && leftShow) {
            line.setVisibility(View.VISIBLE);
        } else {
            line.setVisibility(View.GONE);
        }
    }

    /**
     设置下页中间视图
     活动参与状态
     活动参与人员状态
     */
    public void setUpCenterView(CampaignInviteBean baseBean, CampaignListBean.CampaignInviteEntity bean, List<CampaignInviteBean.InviteesBean> inviteList, TextView clickView, TextView moneyView, TextView infoView, TextView countView, TextView joinNumberTv, LinearLayout linearLayout, int count, TextView tvPutResult, TextView tvPutEnter) {

        if (bean == null || TextUtils.isEmpty(bean.getInvite_status())) {
            return;
        }

        if (inviteList == null || inviteList.size() == 0) {
            joinNumberTv.setText("已经有0人参加");
        } else {
            if (count == - 1) {
                joinNumberTv.setText("已经有" + inviteList.size() + "人参加");
            } else {
                joinNumberTv.setText("已经有" + count + "人参加");
            }
        }
        initInviteesList(linearLayout, inviteList);
        setClickView(baseBean, bean, clickView, tvPutResult, tvPutEnter);

        switch (bean.getInvite_status()) {
            case RUNNING://活动正在进行，且未参加
                countView.setVisibility(View.VISIBLE);
                tvPutResult.setText("参加此活动可额外获得PUT奖励");
                switch (bean.getCampaign().getPer_budget_type()) {
                    case CAMPAIGN_TYPE_CLICK://
                        infoView.setText(Html.fromHtml("分享后，好友每个点击可帮您获得:<font color=#ecb200>¥ </font>" + StringUtil.deleteZero(bean.getCampaign().getPer_action_budget())));
                        countView.setText(getCountDownTime(bean, false));
                        break;
                    case CAMPAIGN_TYPE_CPA:
                        infoView.setText("分享后好友完成指定任务即获得报酬");
                        countView.setText(getCountDownTime(bean, false));
                        break;
                    case CAMPAIGN_TYPE_POST://
                        infoView.setText(Html.fromHtml("分享后，您可通过转发活动获得:<font color=#ecb200>¥ </font>" + StringUtil.deleteZero(bean.getCampaign().getPer_action_budget())));
                        countView.setText(getCountDownTime(bean, false));
                        break;
                    case CAMPAIGN_TYPE_RECRUIT:
                        infoView.setText("参与招募活动获得奖励");
                        countView.setText(getCountDownTime(bean, true));
                        break;
                    case CAMPAIGN_TYPE_INVITE:
                        infoView.setText("参与特邀活动获得奖励");
                        countView.setText(getCountDownTime(bean, false));
                        break;

                    case CAMPAIGN_TYPE_CPI:
                        infoView.setText("分享后打开链接下载APP即可获得报酬");
                        countView.setText(getCountDownTime(bean, false));
                        break;

                    case CAMPAIGN_TYPE_CPT://
                        infoView.setText(Html.fromHtml("分享后，您可通过完成任务获得:<font color=#ecb200>¥ </font>" + StringUtil.deleteZero(bean.getCampaign().getPer_action_budget())));
                        countView.setText(getCountDownTime(bean, false));
                        break;
                }
                break;

            default:
                countView.setVisibility(View.GONE);
                String perBudgetType = bean.getCampaign().getPer_budget_type();
                String perActionType = bean.getCampaign().getPer_action_type();
                //已参与且审核通过，settled
                if (CAMPAIGN_TYPE_RECRUIT.equals(perBudgetType) && (TextUtils.isEmpty(perActionType) || STATE_APPLYING.equals(bean.getInvite_status()))) {
                    infoView.setText("参与招募活动获得奖励");
                } else if (CAMPAIGN_TYPE_INVITE.equals(perBudgetType)) {
                    infoView.setText("参与特邀活动获得奖励");
                } else {
                    String status = bean.getStatus();
                    String per_budget_type = bean.getCampaign().getPer_budget_type();
                    if (per_budget_type.equals(CAMPAIGN_TYPE_CLICK)) {
                        if (SETTLED.equals(status) || MISSED.equals(status)) {
                            infoView.setText(Html.fromHtml("已获得<font color=#ecb200>" + bean.getAvail_click() + "</font>次点击，已赚<font color=#ecb200>¥ </font>" + StringUtil.deleteZero(String.valueOf(bean.getEarn_money()))));
                        } else if (STATE_APPROVED.equals(status) || STATE_FINISHED.equals(status)) {
                            infoView.setText(Html.fromHtml("已获得<font color=#ecb200>" + bean.getAvail_click() + "</font>次点击，即将赚<font color=#ecb200>¥ </font>" + StringUtil.deleteZero(String.valueOf(bean.getEarn_money()))));
                        } else if (REJECTED.equals(status)) {
                            infoView.setText(Html.fromHtml("已获得<font color=#ecb200>" + bean.getAvail_click() + "</font>次点击，已赚<font color=#ecb200>¥ </font>" + StringUtil.deleteZero(String.valueOf(bean.getEarn_money()))));
                            //                            if (TextUtils.isEmpty(bean.getReject_reason())){
                            //                                infoView.setText(Html.fromHtml("已获得<font color=#ecb200>" + bean.getAvail_click()
                            //                                        + "</font>次点击，已赚<font color=#ecb200>¥</font>"
                            //                                        + StringUtil.deleteZero(String.valueOf(bean.getEarn_money()))));
                            //                            }else {
                            //                                infoView.setText(Html.fromHtml("<font color=#ecb200>" + bean.getAvail_click()
                            //                                        + "</font>"));
                            //                            }
                        } else {
                            infoView.setText(Html.fromHtml("已获得<font color=#ecb200>" + bean.getAvail_click() + "</font>次点击，即将赚<font color=#ecb200>¥ </font>" + StringUtil.deleteZero(String.valueOf(bean.getEarn_money()))));
                        }
                    } else {
                        if (SETTLED.equals(status) || MISSED.equals(status)) {
                            infoView.setText(Html.fromHtml("已完成，已赚<font color=#ecb200>¥ </font>" + StringUtil.deleteZero(String.valueOf(bean.getEarn_money()))));
                        } else if (STATE_APPROVED.equals(status) || STATE_FINISHED.equals(status)) {
                            infoView.setText(Html.fromHtml("活动进行中，即将赚<font color=#ecb200>¥ </font>" + StringUtil.deleteZero(String.valueOf(bean.getEarn_money()))));
                        } else if (REJECTED.equals(status)) {
                            infoView.setText(Html.fromHtml("已完成，已赚<font color=#ecb200>¥ </font>" + StringUtil.deleteZero(String.valueOf(bean.getEarn_money()))));
                            infoView.setText(Html.fromHtml("已完成，已赚<font color=#ecb200>¥ </font>0"));
                            //                            if (TextUtils.isEmpty(bean.getReject_reason())){
                            //                                infoView.setText(Html.fromHtml("已完成，已赚<font color=#ecb200>¥</font>"
                            //                                        + StringUtil.deleteZero(String.valueOf(bean.getEarn_money()))));
                            //                            }else {
                            //                                infoView.setText(Html.fromHtml("<font color=#ecb200>" + bean.getAvail_click()
                            //                                        + "</font>"));
                            //                            }
                        } else {
                            infoView.setText(Html.fromHtml("活动进行中，即将赚<font color=#ecb200>¥ </font>" + StringUtil.deleteZero(String.valueOf(bean.getEarn_money()))));
                        }
                    }

                }
                break;
        }
    }

    /**
     上传截图
     单张
     @param file
     */
    public void uploadTurnImage(final Activity activity, String filename, File file) {

        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(activity);
        }
        mWProgressDialog.show();
        LinkedHashMap<String, Object> requestMap = new LinkedHashMap<>();
        String urlImg = HelpTools.getUrl(CommonConfig.CAMPAIGN_INVITES_URL + "/" + mCampaignInviteEntityId + "/upload_screenshot");
        requestMap.put("[url]", urlImg);
        requestMap.put("[file/image/jpeg]screenshot", file);
        mBasePresenter.postImage(true, HttpRequest.PUT, requestMap, new DefaultHttpCallBack(null) {

            @Override
            public void onComplate(ResponceBean responceBean) {
                LogUtil.LogShitou("活动上传截图", "===>" + responceBean);
                if (mWProgressDialog != null) {
                    try {
                        mWProgressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                CampaignInviteBean baseBean = GsonTools.jsonToBean(responceBean.pair.second, CampaignInviteBean.class);
                if (baseBean == null) {
                    CustomToast.showShort(activity, activity.getString(R.string.please_data_wrong));
                    return;
                }
                if (baseBean.getError() == 0) {
                    CampaignListBean.CampaignInviteEntity entity = baseBean.getCampaign_invite();
                    updateBottomShareView(entity);
                    CustomToast.showShort(activity, "上传截图成功");
                } else {
                    CustomToast.showShort(activity, baseBean.getDetail());
                }

            }

            public void onFailure(IHttpCallBack.ResponceBean responceBean) {

                if (mWProgressDialog != null) {
                    try {
                        mWProgressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                try {
                    BaseBean baseBean = GsonTools.jsonToBean(responceBean.pair.second, BaseBean.class);
                    if (! TextUtils.isEmpty(baseBean.getDetail())) {
                        CustomToast.showShort(BaseApplication.getContext(), baseBean.getDetail());
                    } else {
                        CustomToast.showShort(BaseApplication.getContext(), responceBean.pair.second);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     上传截图
     多张
     @param mapImgs
     */
    public void uploadTurnImages(final Activity activity, SerializableMap mapImgs) {
        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(activity);
        }
        if (mapImgs == null) {
            CustomToast.showShort(activity, activity.getString(R.string.img_empty));
            return;
        }
        mWProgressDialog.show();

        mBasePresenter.getDataFromServer(true, HttpRequest.PUT, (HelpTools.getUrl(CommonConfig.CAMPAIGN_INVITES_URL + "/" + mCampaignInviteEntityId + "/upload_screenshot")), "screenshot", mapImgs.getMap(), new RequestCallback() {

            @Override
            public void onError(Exception e) {
                // LogUtil.LogShitou("走到这里没有","333"+e.getMessage());
                if (mWProgressDialog != null) {
                    try {
                        mWProgressDialog.dismiss();
                    } catch (Exception es) {
                        es.printStackTrace();
                    }

                }
            }

            @Override
            public void onResponse(String response) {
                //  LogUtil.LogShitou("活动上传截图-多图", "===>" + response);
                if (mWProgressDialog != null) {
                    try {
                        mWProgressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                CampaignInviteBean baseBean = GsonTools.jsonToBean(response, CampaignInviteBean.class);
                if (baseBean == null) {
                    CustomToast.showShort(activity, activity.getString(R.string.please_data_wrong));
                    return;
                }
                if (baseBean.getError() == 0) {
                    CampaignListBean.CampaignInviteEntity entity = baseBean.getCampaign_invite();
                    updateBottomShareView(entity);
                    CustomToast.showShort(activity, "上传截图成功");
                } else {
                    CustomToast.showShort(activity, baseBean.getDetail());
                }
            }
        });
    }

    /**
     活动参与人员显示数量管理
     @param linearLayout
     @param inviteList
     */
    private void initInviteesList(LinearLayout linearLayout, List<CampaignInviteBean.InviteesBean> inviteList) {

        Context context = linearLayout.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        linearLayout.removeAllViews();
        if (inviteList != null && inviteList.size() >= 0) {
            int size = inviteList.size();
            if (size > 4) {
                size = 4;
            }
            for (int i = 0; i < size; i++) {

                if (inviteList.get(i) == null) {
                    continue;
                }

                View view = inflater.inflate(R.layout.invitee_item, null);
                CircleImageView civImage = (CircleImageView) view.findViewById(R.id.civ_image);
                BitmapUtil.loadImage(context, inviteList.get(i).getAvatar_url(), civImage);
                linearLayout.addView(view);
            }
        }

        View view = inflater.inflate(R.layout.invitee_item, null);
        CircleImageView civImage = (CircleImageView) view.findViewById(R.id.civ_image);
        civImage.setBackgroundResource(R.mipmap.icon_user_defaul);
        linearLayout.addView(view);
    }

    private void setClickView(CampaignInviteBean baseBean, CampaignListBean.CampaignInviteEntity bean, TextView clickView, TextView tvPutResult, TextView tvPutEnter) {

        String append = "";
        switch (bean.getCampaign().getPer_budget_type()) {
            case CAMPAIGN_TYPE_CLICK:
                append = ("点击");
                break;
            case CAMPAIGN_TYPE_CPA:
                append = ("效果");
                break;
            case CAMPAIGN_TYPE_POST:
                append = ("转发");
                break;
            case CAMPAIGN_TYPE_RECRUIT:
                append = ("招募");
                break;
            case CAMPAIGN_TYPE_INVITE:
                append = ("特邀");
                break;
            case CAMPAIGN_TYPE_CPI:
                append = ("下载");
                break;
            case CAMPAIGN_TYPE_CPT:
                append = ("任务");
                break;
        }
        String status = bean.getStatus();
       // String isOpen = HelpTools.getLoginInfo(HelpTools.ISOPENPUT);
       // String isPutUser = HelpTools.getLoginInfo(HelpTools.WEBADDRESS);
        if (bean.getCampaign().getPer_budget_type().equals(CAMPAIGN_TYPE_RECRUIT)) {
            tvPutResult.setVisibility(View.GONE);
            tvPutEnter.setVisibility(View.GONE);
        } else {
            if (SETTLED.equals(status)) {
                tvPutResult.setVisibility(View.VISIBLE);
                tvPutEnter.setVisibility(View.GONE);
//                if (! TextUtils.isEmpty(isPutUser)) {
//                } else {
//                    tvPutResult.setText("活动已结束，无法获得额外奖励");
//                }
                tvPutResult.setText("已获得" + baseBean.getPut_count() + "个PUT的额外奖励，请去钱包查看");

            } else if (MISSED.equals(status)) {
                tvPutEnter.setVisibility(View.GONE);
                tvPutResult.setVisibility(View.VISIBLE);
                tvPutResult.setText("活动已结束，无法获得额外奖励");
            } else if (REJECTED.equals(status)) {
                tvPutEnter.setVisibility(View.GONE);
                tvPutResult.setVisibility(View.VISIBLE);
//                if (! TextUtils.isEmpty(isPutUser)) {
//                } else {
//                    tvPutResult.setText("活动已结束，无法获得额外奖励");
//                }
                tvPutResult.setText("已获得" + baseBean.getPut_count() + "个PUT的额外奖励，请去钱包查看");

            } else {
                tvPutResult.setVisibility(View.VISIBLE);
                tvPutResult.setText("参加此活动可额外获得PUT奖励");
                tvPutEnter.setVisibility(View.GONE);
            }
        }

        clickView.setText(append);

        // moneyView.setText("根据你的历史表现，本次" + append + "活动单次可赚:￥ " + StringUtil.deleteZero(bean.getCampaign().getPer_action_budget()));
    }

    private void countDownTime(CampaignListBean.CampaignInviteEntity bean) {

        List<Integer> intervalTime = bean.getUpload_interval_time();
        if (intervalTime == null || intervalTime.size() != 4) {
            return;
        }
        int time = intervalTime.get(2) * 60 + intervalTime.get(3);
        Message msg = handler.obtainMessage();
        msg.what = time;
        handler.sendMessageDelayed(msg, 1000);
    }

    private void setCountTimeView(int time, TextView view) {
        if (time == 0) {
            view.setClickable(true);
            view.setText(R.string.upload_screenshot);
            return;
        } else {
            view.setClickable(false);
        }

        int m = time / 60; //分钟
        int s = time % 60; //秒
        if (sb.length() > 0) {
            sb.delete(0, sb.length());
        }
        if (m == 0) {
            sb.append(String.valueOf(s)).append("秒");
        } else {
            sb.append(String.valueOf(m)).append("分钟").append(String.valueOf(s)).append("秒");
        }
        view.setText(sb.append("后请上传截图").toString());
    }

    /**
     设置倒计时显示
     @param bean
     */
    private String getTimeLast(CampaignListBean.CampaignInviteEntity bean) {
        //设置开始时间
        List<Integer> interval_time = bean.getUpload_interval_time();
        StringBuffer sb = new StringBuffer("");
        if (interval_time.get(0) > 0) {
            sb.append(interval_time.get(0) + "天");
        }
        if (interval_time.get(1) > 0) {
            sb.append(interval_time.get(1) + "小时");
        }
        if (interval_time.get(2) > 0) {
            sb.append(interval_time.get(2) + "分");
        }
        sb.append(interval_time.get(3) + "秒");

        if (interval_time.get(2) == 0 && interval_time.get(3) == 0) {
            return BaseApplication.getContext().getString(R.string.upload_screenshot);
        }
        return sb.append("后请上传截图").toString();
    }

    /**
     获取与结束时间的时间差
     @return
     */
    private String getCountDownTime(CampaignListBean.CampaignInviteEntity bean, boolean isRecruit) {

        if (bean == null) {
            return "";
        }
        String info = "";
        if (isRecruit) {
            info = "距报名截止";
        } else {
            info = "距活动结束";
        }

        StringBuffer sb = new StringBuffer(info);
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
            sb.append(day).append("天");
            sb.append(hour).append("小时");
            sb.append(minute).append("分钟");

            if (day <= 0 && hour <= 0 && minute <= 0) {
                return "已结束";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public void showSignUpSuccessDialog(Activity activity, String text) {

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_sign_up_success, null);
        TextView cancelTV = (TextView) view.findViewById(R.id.tv_cancel);
        TextView infoTv = (TextView) view.findViewById(R.id.tv_info);
        infoTv.setText(text);
        final CustomDialogManager cdm = new CustomDialogManager(activity, view);

        cancelTV.setOnClickListener(new View.OnClickListener() {

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

    public void onDestroy() {

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    public void clickBottomRight(Activity activity) {
        if (isDoubleClick()) {
            return;
        }
        switch (mCurrentRightState) {
            case DISALLOW_CLICK:
                return;
            case CLICK_REJECT_CAMPAIGN://特邀——拒绝此活动
                rejectInvite(activity, mCampaignInviteEntity);
                break;
            case CLICK_UPLOAD_AGAIN://重新上传
            case CLICK_UPLOAD_SCREENSHOT://上传截图
                showSelectImageDialog(activity, mCampaignInviteEntity, false);
                break;
            case CLICK_RECEIVE_CAMPAIGN://特邀——接受此活动
                receiveInvite(activity, mCampaignInviteEntity);
                break;
            case CLICK_CHECK_SCREENSHOT://查看截图
                showSnapDialog(activity, mCampaignInviteEntity, "1");
                break;
            case CLICK_CHECK_REJECT_REASON://审核拒绝-查看原因
                showRejectDialog(activity, mCampaignInviteEntity);
                break;
            case CLICK_SHARE://分享
                showShareDialog(activity, mCampaignInviteEntity, 0);
                break;
            case CLICK_SIGN_UP:// 招募——活动报名
                registrationActivities(activity, mCampaignInviteEntity);
                break;
            case CLICK_SHARE_NO_PAY:// 无偿转发
                if (mCampaignInviteEntity.getCampaign().getPer_action_type().equals(wechatEn)) {
                    showShareDialog(activity, mCampaignInviteEntity, 1);
                } else if (mCampaignInviteEntity.getCampaign().getPer_action_type().equals(weiboEn)) {
                    popSharedialog(activity, mCampaignInviteEntity, weiboEn, - 1);
                } else {
                    if (TextUtils.isEmpty(mCampaignInviteEntity.getSub_type())) {
                        showShareDialog(activity, mCampaignInviteEntity, 1);
                    } else {
                        if (mCampaignInviteEntity.getSub_type().equals(wechatEn)) {
                            showShareDialog(activity, mCampaignInviteEntity, 1);
                        } else if (mCampaignInviteEntity.getSub_type().equals(weiboEn)) {
                            popSharedialog(activity, mCampaignInviteEntity, weiboEn, - 1);
                        } else {
                            showShareDialog(activity, mCampaignInviteEntity, 1);
                        }
                    }

                }
                break;
        }
    }

    /**
     @param activity
     @param type 0是正常流程，1是无偿转发
     @param wechatType 0是朋友圈，1是群组
     */
    private void bindWechat(final Activity activity, final int type, final int wechatType) {

        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(activity);
        }
        mWProgressDialog.show();
        String data = CacheUtils.getString(activity, SPConstants.MINE_DATA, null);
        MineShowModel mineShowModel = GsonTools.jsonToBean(data, MineShowModel.class);
        if (mineShowModel != null) {
            int id = mineShowModel.getKol().getId();
            if (mBasePresenter == null) {
                mBasePresenter = new BasePresenter();
            }

            if (mRequestParams == null) {
                mRequestParams = new RequestParams();
            }

            mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.FIRST_KOL_LIST_URL + BACKSLASH + String.valueOf(id) + BACKSLASH + "detail"), mRequestParams, new RequestCallback() {

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
                    LogUtil.LogShitou("活动分享获取信息！！！", response);
                    KolDetailModel kolDetailModel = GsonTools.jsonToBean(response, KolDetailModel.class);
                    if (kolDetailModel != null && kolDetailModel.getError() == 0) {
                        List<SocialAccountsBean> mSocialAccounts = kolDetailModel.getSocial_accounts();
                        if (mSocialAccounts != null) {
                            if (mSocialAccounts.size() != 0) {
                                for (int i = 0; i < mSocialAccounts.size(); i++) {
                                    if (mSocialAccounts.get(i).getProvider_name().equals(activity.getString(R.string.weixin)) || mSocialAccounts.get(i).getProvider().equals(wechatEn)) {
                                        TAG = activity.getString(R.string.weixin);
                                    }
                                }
                                if (TAG.equals("null")) {
                                    //没有绑定微信
                                    CustomToast.showShort(activity, "正在前往微信中");
                                    bind(activity, activity.getString(R.string.weixin), type, wechatType);
                                } else {
                                    // LogUtil.LogShitou("type","====>"+type);
                                    if (type == 0) {
                                        share(activity, mCampaignInviteEntity, wechatEn, wechatType);
                                    } else {
                                        popSharedialog(activity, mCampaignInviteEntity, wechatEn, wechatType);
                                    }
                                }
                            } else {
                                CustomToast.showShort(activity, "正在前往微信中");
                                bind(activity, activity.getString(R.string.weixin), type, wechatType);
                            }
                        } else {
                            CustomToast.showShort(activity, "正在前往微信中");
                            bind(activity, activity.getString(R.string.weixin), type, wechatType);
                        }
                    }
                }
            });
        }
    }

    private void bind(final Activity activity, final String names, final int type, final int wechatType) {
        BindSocialPresenter presenter = new BindSocialPresenter(activity.getApplicationContext(), null, names, 0);
        presenter.setOnBindListener(new BindSocialPresenter.OnBindListener() {

            @Override
            public void onResponse(String name) {
                if (null != name) {
                    CustomToast.showShort(activity, "已成功绑定" + names);
                    bindPostData(activity, names, name, type, wechatType);
                } else {
                    CustomToast.showLong(activity, "绑定失败，请重试");
                }
            }
        });
        if (activity.getString(R.string.weixin).equals(names)){
            presenter.authorize(new Wechat());
        } else if (activity.getString(R.string.weibo).equals(names)) {
            presenter.authorize(new SinaWeibo());
        }

    }

    private void bindPostData(final Activity activity, String name, String userName, final int type, final int wechatType) {

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
        LogUtil.LogShitou("绑定微信", name + "//" + userName);

        mBasePresenter.getDataFromServer(true, HttpRequest.POST, (HelpTools.getUrl(CommonConfig.UPDATE_SOCIAL_URL_OLD)), params, new RequestCallback() {

            @Override
            public void onError(Exception e) {

                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
                   LogUtil.LogShitou("分享页面提交微信绑定", "OK" + response);
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);
                if (bean == null) {
                    CustomToast.showShort(activity, activity.getString(R.string.please_data_wrong));
                    return;
                }
                if (bean.getError() == 0) {
                    if (type == 1) {
                        popSharedialog(activity, mCampaignInviteEntity, wechatEn, wechatType);
                    } else {
                        share(activity, mCampaignInviteEntity, wechatEn, wechatType);
                    }

                } else {
                    CustomToast.showShort(activity, "绑定失败");
                }
            }
        });
    }


    public void clickBottomLeft(Activity activity) {
        if (isDoubleClick()) {
            return;
        }
        switch (mCurrentLeftState) {
            case DISALLOW_CLICK:
                return;
            case CLICK_REJECT_CAMPAIGN://特邀——拒绝此活动
                rejectInvite(activity, mCampaignInviteEntity);
                break;
            case CLICK_UPLOAD_AGAIN://重新上传
            case CLICK_UPLOAD_SCREENSHOT://上传截图
                showSelectImageDialog(activity, mCampaignInviteEntity, false);
                break;
            case CLICK_RECEIVE_CAMPAIGN://特邀——接受此活动
                receiveInvite(activity, mCampaignInviteEntity);
                break;
            case CLICK_CHECK_SCREENSHOT://查看截图
                showSnapDialog(activity, mCampaignInviteEntity, "1");
                break;
            case CLICK_CHECK_REJECT_REASON://审核拒绝-查看原因
                showRejectDialog(activity, mCampaignInviteEntity);
                break;
            case CLICK_SHARE://分享
                showShareDialog(activity, mCampaignInviteEntity, 0);
                break;
            case CLICK_SIGN_UP:// 招募——活动报名
                registrationActivities(activity, mCampaignInviteEntity);
                break;
            case CLICK_SHARE_NO_PAY:// 无偿转发
                if (mCampaignInviteEntity.getCampaign().getPer_action_type().equals(wechatEn)) {
                    showShareDialog(activity, mCampaignInviteEntity, 1);
                } else if (mCampaignInviteEntity.getCampaign().getPer_action_type().equals(weiboEn)) {
                    popSharedialog(activity, mCampaignInviteEntity, weiboEn, - 1);
                } else {
                    if (mCampaignInviteEntity.getSub_type().equals(wechatEn)) {
                        showShareDialog(activity, mCampaignInviteEntity, 1);
                    } else if (mCampaignInviteEntity.getSub_type().equals(weiboEn)) {
                        popSharedialog(activity, mCampaignInviteEntity, weiboEn, - 1);
                    } else {
                        showShareDialog(activity, mCampaignInviteEntity, 1);
                    }
                }
                break;
            case CLICK_UPLOAD_AND_CHECK:
                showSelectImageDialog(activity, mCampaignInviteEntity, true);
                break;

        }
    }


    /**
     免责协议
     @param activity
     @param campaignInviteEntity
     @param shareType 分享平台
     @param wchatType 微信朋友圈／群组
     */
    private void popProtocolDialog(final Activity activity, final CampaignListBean.CampaignInviteEntity campaignInviteEntity, final String shareType, final int wchatType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_text_protocol, null);
        TextView confirmTV = (TextView) view.findViewById(R.id.tv_confirm);
        TextView rightTv = (TextView) view.findViewById(R.id.tv_right);
        final CustomDialogManager cdm = new CustomDialogManager(activity, view);
        confirmTV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // CustomToast.showShort(activity, "同意协议");
                if (shareType.equals(wechatEn)) {
                    bindWechat(activity, 0, wchatType);
                } else {
                    share(activity, campaignInviteEntity, weiboEn, wchatType);
                }
                //  share(activity, campaignInviteEntity);
                cdm.dismiss();
                CacheUtils.putBoolean(activity, SPConstants.AGREE_PROTOCOL, true);
            }
        });
        rightTv.setOnClickListener(new View.OnClickListener() {

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


    /**
     接受活动邀请
     @param campaignInviteEntity
     @param activity
     */
    private void receiveInvite(final Activity activity, CampaignListBean.CampaignInviteEntity campaignInviteEntity) {

        String url = HelpTools.getUrl(CommonConfig.CAMPAIGNS_DETAIL_URL) + campaignInviteEntity.getCampaign().getId() + "/approve";
        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }

        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(activity);
        }
        mWProgressDialog.show();
        mBasePresenter.getDataFromServer(true, HttpRequest.PUT, url, null, new RequestCallback() {

            @Override
            public void onError(Exception e) {

                CustomToast.showShort(activity, "网络连接错误");
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {

                CampaignInviteBean bean = GsonTools.jsonToBean(response, CampaignInviteBean.class);
                if (bean != null && bean.getError() == 0 && bean.getCampaign_invite() != null && bean.getCampaign_invite().getCampaign() != null) {
                    CampaignListBean.CampaignInviteEntity entity = bean.getCampaign_invite();
                    updateBottomShareView(entity);
                    showSignUpSuccessDialog(activity, "活动接受成功");
                    NotifyManager.getNotifyManager().notifyChange(NotifyManager.TYPE_SHARE_SUCCESS);
                    SUCCESS = "success";
                } else if (bean != null) {
                    CustomToast.showShort(activity, bean.getDetail());
                }
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }
        });
    }

    /**
     拒绝特邀活动
     @param activity
     @param campaignInviteEntity
     */
    private void rejectInvite(final Activity activity, CampaignListBean.CampaignInviteEntity campaignInviteEntity) {

        String url = HelpTools.getUrl(CommonConfig.CAMPAIGN_INVITES16_URL) + campaignInviteEntity.getId() + "/reject";
        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }

        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(activity);
        }
        mWProgressDialog.show();
        mBasePresenter.getDataFromServer(true, HttpRequest.POST, url, null, new RequestCallback() {

            @Override
            public void onError(Exception e) {

                CustomToast.showShort(activity, activity.getString(R.string.no_net));
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {

                CampaignInviteBean bean = GsonTools.jsonToBean(response, CampaignInviteBean.class);
                if (bean != null && bean.getError() == 0 && bean.getCampaign_invite() != null && bean.getCampaign_invite().getCampaign() != null) {
                    CampaignListBean.CampaignInviteEntity entity = bean.getCampaign_invite();
                    updateBottomShareView(entity);
                } else if (bean != null) {
                    CustomToast.showShort(activity, bean.getDetail());
                }
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }
        });
    }

    /**
     报名招募活动
     */
    private void registrationActivities(final Activity activity, CampaignListBean.CampaignInviteEntity campaignInviteEntity) {

        if (campaignInviteEntity.getCampaign().is_applying_note_required() || campaignInviteEntity.getCampaign().is_applying_picture_required()) {
            Intent intent = new Intent(activity, SignUpRecruitActivity.class);
            intent.putExtra("campaign_invite_entity", campaignInviteEntity.getCampaign());
            activity.startActivity(intent);
            return;
        }

        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }

        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(activity);
        }
        mWProgressDialog.show();
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", campaignInviteEntity.getCampaign().getId());
        mBasePresenter.getDataFromServer(true, HttpRequest.PUT, HelpTools.getUrl(CommonConfig.CAMPAIGNS_APPLY_URL), requestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {

                CustomToast.showShort(activity, "网络连接错误");
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {

                CampaignInviteBean bean = GsonTools.jsonToBean(response, CampaignInviteBean.class);
                if (bean != null && bean.getError() == 0 && bean.getCampaign_invite() != null && bean.getCampaign_invite().getCampaign() != null) {
                    CampaignListBean.CampaignInviteEntity entity = bean.getCampaign_invite();
                    updateBottomShareView(entity);
                    showSignUpSuccessDialog(activity, activity.getString(R.string.campaign_sign_up_success_wait_for_auditing));
                } else if (bean != null) {
                    CustomToast.showShort(activity, bean.getDetail());
                }
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }
        });
    }

    /**
     截图被拒绝
     @param activity
     @param campaignInviteEntity
     */
    public void showRejectDialog(final Activity activity, final CampaignListBean.CampaignInviteEntity campaignInviteEntity) {

        String rejectReason = campaignInviteEntity.getReject_reason();
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_reject_screenshot, null);
        TextView confirmTV = (TextView) view.findViewById(R.id.tv_confirm);
        TextView infoTv = (TextView) view.findViewById(R.id.tv_info);
        TextView rightTv = (TextView) view.findViewById(R.id.tv_right);
        confirmTV.setText(R.string.screenshot_reference);
        infoTv.setText(rejectReason);
        final CustomDialogManager cdm = new CustomDialogManager(activity, view);
        confirmTV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cdm.dismiss();
                showSnapDialog(activity, campaignInviteEntity, "0");

            }
        });
        rightTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cdm.dismiss();
                showSnapDialog(activity, campaignInviteEntity, "1");

            }
        });
        cdm.dg.setCanceledOnTouchOutside(true);
        cdm.dg.getWindow().setGravity(Gravity.CENTER);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        cdm.showDialog();
    }

    /**
     截图参考
     @param activity
     @param campaignInviteEntity
     @param type 0: 截图参考 ； 1：查看截图 ；2:上传截图
     */
    private void showSnapDialog(Activity activity, CampaignListBean.CampaignInviteEntity campaignInviteEntity, String type) {

        if (campaignInviteEntity.getCpi_example_screenshots() != null || campaignInviteEntity.getCpi_example_screenshots().size() != 0) {
            Intent intent = new Intent(activity, ScreenImgActivity.class);
            intent.putStringArrayListExtra(ScreenImgActivity.EXTRA_NAME_LIST, (ArrayList<String>) campaignInviteEntity.getScreenshot_comment());
            if (type.equals("0")) {
                //标题
                intent.putExtra(ScreenImgActivity.EXTRA_TITLE, activity.getString(R.string.screenshot_reference));
                intent.putExtra(ScreenImgActivity.EXTRA_TYPE, "0");
                //参考图片
                intent.putStringArrayListExtra(ScreenImgActivity.EXTRA_SCREEN_LISTS, (ArrayList<String>) campaignInviteEntity.getCpi_example_screenshots());
                activity.startActivity(intent);
            } else if (type.equals("1")) {
                intent.putExtra(ScreenImgActivity.EXTRA_TITLE, activity.getString(R.string.look_the_screenshot));
                intent.putExtra(ScreenImgActivity.EXTRA_TYPE, "1");
                //已上传图片
                intent.putStringArrayListExtra(ScreenImgActivity.EXTRA_SCREEN_LISTS, (ArrayList<String>) campaignInviteEntity.getScreenshots());
                activity.startActivityForResult(intent, IMAGE_REQUEST_MORE_IMG_CODE);
            } else if (type.equals("2")) {
                intent.putExtra(ScreenImgActivity.EXTRA_TITLE, activity.getString(R.string.upload_screenshot));
                intent.putExtra(ScreenImgActivity.EXTRA_TYPE, "2");
                intent.putStringArrayListExtra(ScreenImgActivity.EXTRA_SCREEN_LISTS, (ArrayList<String>) campaignInviteEntity.getCpi_example_screenshots());
                activity.startActivityForResult(intent, IMAGE_REQUEST_MORE_IMG_CODE);
            }
        } else {
            CustomToast.showShort(activity, "截图示例不存在！");
        }
    }

    /**
     显示上传截图对话框
     @param activity
     @param campaignInviteEntity
     @param isAdd 是否添加查看截图
     */
    private void showSelectImageDialog(final Activity activity, final CampaignListBean.CampaignInviteEntity campaignInviteEntity, boolean isAdd) {

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_post_screenshot, null);
        TextView openGalleryTv = (TextView) view.findViewById(R.id.tv_open_gallery);
        TextView referenceScreenshotTv = (TextView) view.findViewById(R.id.tv_reference_screenshot);
        TextView helpTv = (TextView) view.findViewById(R.id.tv_help);
        TextView lookScreenShot = (TextView) view.findViewById(R.id.tv_look_screenshot);
        if (isAdd) {
            lookScreenShot.setVisibility(View.VISIBLE);
        } else {
            lookScreenShot.setVisibility(View.GONE);
        }
        final CustomDialogManager cdm = new CustomDialogManager(activity, view);
        helpTv.setText(R.string.cancel);
        lookScreenShot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                cdm.dismiss();
                showSnapDialog(activity, mCampaignInviteEntity, "1");
            }
        });
        if (campaignInviteEntity.getImg_status() != null && campaignInviteEntity.getScreenshots() != null && campaignInviteEntity.getScreenshots().size() != 0) {
            openGalleryTv.setText("再次上传");
        } else {
            openGalleryTv.setText("上传截图");
        }
        openGalleryTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //打开相册
                cdm.dismiss();
                if (campaignInviteEntity.getImg_status() != null && campaignInviteEntity.getScreenshots() != null && campaignInviteEntity.getScreenshots().size() != 0) {
                    showSnapDialog(activity, campaignInviteEntity, "1");
                } else {
                    showSnapDialog(activity, campaignInviteEntity, "2");
                }

            }
        });

        referenceScreenshotTv.setVisibility(View.VISIBLE);
        referenceScreenshotTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cdm.dismiss();
                showSnapDialog(activity, campaignInviteEntity, "0");

            }
        });

        helpTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cdm.dismiss();
            }
        });
        cdm.dg.setCanceledOnTouchOutside(true);
        cdm.dg.getWindow().setGravity(Gravity.BOTTOM);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        cdm.showDialog();
    }

    /**
     显示帮助对话框
     @param activity
     */
    private void showHelpDialog(Activity activity) {

        final CustomDialog dialog = new SwipeBackDialog(activity, R.layout.dialog_help);
        View view = dialog.getView();
        ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
        iv_image.setBackgroundResource(R.mipmap.uploadsnap_help);
        View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        };

        //设置dialog高度
        dialog.dg.getWindow().setLayout(DensityUtils.getScreenWidth(activity), DensityUtils.getScreenHeight(activity) - DensityUtils.getStatusHeight(activity));
        dialog.showDialog();
    }

    private CustomDialogManager mCustomDialogManager;
    private List<DetailContentActivity.ShareBean> myShareListCampaign;


    /**
     分享
     @param activity
     @param mCampaignInviteEntity
     @param i
     */
    private void showShareDialog(final Activity activity, final CampaignListBean.CampaignInviteEntity mCampaignInviteEntity, final int i) {

        View view = LayoutInflater.from(activity).inflate(R.layout.campaign_share_bottom_layout, null);
        RecyclerView recyclerViewCampaign = (RecyclerView) view.findViewById(R.id.recycler_view_campaign);
        LinearLayout llTop = (LinearLayout) view.findViewById(R.id.ll_top_share);
        LinearLayout llBittom = ((LinearLayout) view.findViewById(R.id.ll_bottom_share));
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        llTop.setVisibility(View.GONE);
        llBittom.setVisibility(View.VISIBLE);

        //分享活动
        myShareListCampaign = new ArrayList<>();
        if (TextUtils.isEmpty(mCampaignInviteEntity.getCampaign().getPer_action_type())) {
            CustomToast.showShort(activity, "网络繁忙，请稍后再试");
            return;
        } else {
            if (mCampaignInviteEntity.getCampaign().getPer_action_type().equals(wechatEn)) {
                myShareListCampaign.add(new DetailContentActivity.ShareBean(activity.getResources().getString(R.string.wechat), R.mipmap.icon_social_wechatmoments_on));
                myShareListCampaign.add(new DetailContentActivity.ShareBean(activity.getResources().getString(R.string.weixin), R.mipmap.login_weixin));
            } else if (mCampaignInviteEntity.getCampaign().getPer_action_type().equals(weiboEn)) {
                myShareListCampaign.add(new DetailContentActivity.ShareBean(activity.getResources().getString(R.string.weibo), R.mipmap.login_weibo));
            } else {
                myShareListCampaign.add(new DetailContentActivity.ShareBean(activity.getResources().getString(R.string.weibo), R.mipmap.login_weibo));
                myShareListCampaign.add(new DetailContentActivity.ShareBean(activity.getResources().getString(R.string.wechat), R.mipmap.icon_social_wechatmoments_on));
                myShareListCampaign.add(new DetailContentActivity.ShareBean(activity.getResources().getString(R.string.weixin), R.mipmap.login_weixin));

            }
        }
        DetailContentActivity.ShareAdapter shareAdapterCampaing = new DetailContentActivity.ShareAdapter(myShareListCampaign);
        LinearLayoutManager linearLayoutManagerTwo = new LinearLayoutManager(recyclerViewCampaign.getContext());
        linearLayoutManagerTwo.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewCampaign.setLayoutManager(linearLayoutManagerTwo);
        recyclerViewCampaign.setAdapter(shareAdapterCampaing);
        final boolean agreeProtocol = CacheUtils.getBoolean(activity, SPConstants.AGREE_PROTOCOL, false);
        shareAdapterCampaing.setOnRecyclerViewListener(new DetailContentActivity.ShareAdapter.OnRecyclerViewListener() {

            @Override
            public void onItemClick(View v, int position) {
                if (mCampaignInviteEntity.getCampaign().getPer_action_type().equals(wechatEn)) {
                    if (position == 0) {//朋友圈
                        if (agreeProtocol) {
                            bindWechat(activity, i, 0);
                        } else {
                            popProtocolDialog(activity, mCampaignInviteEntity, wechatEn, 0);
                        }
                    } else {//群组
                        if (agreeProtocol) {
                            bindWechat(activity, i, 1);
                        } else {
                            popProtocolDialog(activity, mCampaignInviteEntity, wechatEn, 1);
                        }
                    }
                } else if (mCampaignInviteEntity.getCampaign().getPer_action_type().equals(weiboEn)) {
                    if (agreeProtocol) {
                        share(activity, mCampaignInviteEntity, wechatEn, - 1);
                    } else {
                        popProtocolDialog(activity, mCampaignInviteEntity, weiboEn, - 1);
                    }
                } else {
                    if (agreeProtocol) {
                        if (position == 0) {
                            share(activity, mCampaignInviteEntity, weiboEn, - 1);
                        } else if (position == 1) {
                            bindWechat(activity, 0, 0);
                        } else {
                            bindWechat(activity, 0, 1);
                        }
                    } else {
                        if (position == 0) {
                            popProtocolDialog(activity, mCampaignInviteEntity, weiboEn, - 1);
                        } else if (position == 1) {
                            popProtocolDialog(activity, mCampaignInviteEntity, wechatEn, 0);
                        } else {
                            popProtocolDialog(activity, mCampaignInviteEntity, wechatEn, 1);
                        }
                    }
                }
                mCustomDialogManager.dismiss();
            }
        });


        mCustomDialogManager = new CustomDialogManager(activity, view);
        tvCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mCustomDialogManager.dismiss();
            }
        });
        mCustomDialogManager.dg.setCanceledOnTouchOutside(true);
        mCustomDialogManager.dg.getWindow().setGravity(Gravity.BOTTOM);
        mCustomDialogManager.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        mCustomDialogManager.showDialog();
    }

    private void share(final Activity activity, final CampaignListBean.CampaignInviteEntity campaignInviteEntity, final String shareType, final int wechatType) {
        if (! TextUtils.isEmpty(campaignInviteEntity.getUuid())) {//再次分享
            showShareDialog(activity, campaignInviteEntity, 1);
            // popSharedialog(activity, campaignInviteEntity, shareType, 0);
            return;
        }
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(activity);
        }
        mWProgressDialog.show();
        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }

        mBasePresenter.getDataFromServer(true, HttpRequest.PUT, HelpTools.getUrl(CommonConfig.CAMPAIGNS_DETAIL_URL + "/" + campaignInviteEntity.getCampaign().getId() + "/receive"), null, new RequestCallback() {

            @Override
            public void onError(Exception e) {

                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                CustomToast.showShort(activity, "网络连接错误");
            }

            @Override
            public void onResponse(String response) {
                LogUtil.LogShitou("这里是回调", "==>" + response);
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                CampaignInviteBean bean = GsonTools.jsonToBean(response, CampaignInviteBean.class);
                if (bean.getError() == 0 && bean.getCampaign_invite() != null && bean.getCampaign_invite().getCampaign() != null) {
                    CampaignListBean.CampaignInviteEntity entity = bean.getCampaign_invite();
                    mCampaignInviteEntityId = String.valueOf(entity.getId());
                    shareSuccess(activity, entity, shareType, wechatType);
                } else {
                    CustomToast.showShort(activity, bean.getDetail());
                }
            }
        });
    }


    /**
     弹出分享面板
     */
    private void popSharedialog(Activity activity, CampaignListBean.CampaignInviteEntity campaignInviteEntity, String shareType, int wechatType) {
        String per_action_type = campaignInviteEntity.getCampaign().getPer_action_type();
        String platName = "";
        String wechat_auth_type = campaignInviteEntity.getCampaign().getWechat_auth_type();
        if (! TextUtils.isEmpty(wechat_auth_type) && wechat_auth_type.equals("self_info")) {
            if (wechatType == 0) {
                platName = WechatMoments.NAME;
            } else {
                platName = Wechat.NAME;
            }
        } else {
            if (TextUtils.isEmpty(per_action_type) || "wechat".equals(per_action_type)) {
                if (wechatType == 0) {
                    platName = WechatMoments.NAME;
                } else {
                    platName = Wechat.NAME;
                }
            } else if ("weibo".equals(per_action_type)) {
                platName = SinaWeibo.NAME;
            } else if ("qq".equals(per_action_type)) {
                platName = QZone.NAME;
            } else if (("wechat,weibo").equals(per_action_type)) {
                if (shareType.equals(weiboEn)) {
                    platName = SinaWeibo.NAME;
                } else {
                    if (wechatType == 0) {
                        platName = WechatMoments.NAME;
                    } else {
                        platName = Wechat.NAME;
                    }
                }
            }
        }

        CustomToast.showShort(activity, "正在前往分享...");
        //ShareSDK.initSDK(activity);
        OnekeyShare oks = new OnekeyShare();
        oks.setPlatform(platName);
        MySharedListener mySharedListener = new MySharedListener(activity, campaignInviteEntity);
        oks.setCallback(mySharedListener);
        oks.disableSSOWhenAuthorize();
        oks.setTitle(campaignInviteEntity.getCampaign().getName());
        String shareUrl = null;
        if (campaignInviteEntity.getStatus().equals("missed")) {//如果此时已错过
            shareUrl = campaignInviteEntity.getCampaign().getUrl();
        } else {
            shareUrl = campaignInviteEntity.getShare_url();
        }
        if (SinaWeibo.NAME.equals(platName)) {
            //  oks.setText(("#Robin8#" + "「" + campaignInviteEntity.getCampaign().getBrand_name() + "「" + campaignInviteEntity.getCampaign().getName() + "」」" + shareUrl));
            oks.setText(("#Robin8#" + "「" + "「" + campaignInviteEntity.getCampaign().getName() + "」」" + shareUrl));
        } else {
            oks.setText(campaignInviteEntity.getCampaign().getDescription());
            oks.setTitleUrl(campaignInviteEntity.getShare_url());
            // url仅在微信（包括好友和朋友圈）中使用
            oks.setUrl(shareUrl);
        }
        oks.setImageUrl(campaignInviteEntity.getCampaign().getImg_url());
        oks.setSite(activity.getString(R.string.app_name));
        oks.setSiteUrl(CommonConfig.SITE_URL);
        oks.show(activity);
    }

    /**
     分享成功
     @param activity
     @param mCampaignInviteEntity
     @param plat
     @param wechatType
     */
    private void shareSuccess(final Activity activity, final CampaignListBean.CampaignInviteEntity mCampaignInviteEntity, final String plat, final int wechatType) {
        //分享成功 如果是第一次分享 调用share接口 此时活动状态将变为approved
        if (mCampaignInviteEntity.getStatus().equals("running") || CAMPAIGN_TYPE_RECRUIT.equals(mCampaignInviteEntity.getCampaign().getPer_budget_type())) {
            RequestParams requestParams = new RequestParams();
            if (mBasePresenter == null) {
                mBasePresenter = new BasePresenter();
            }
            if (mCampaignInviteEntity.getCampaign().getPer_action_type().equals(wechatEn)) {
                requestParams.put("sub_type", wechatEn);
            } else if (mCampaignInviteEntity.getCampaign().getPer_action_type().equals(weiboEn)) {
                requestParams.put("sub_type", weiboEn);
            } else {
                requestParams.put("sub_type", plat);
            }

            mBasePresenter.getDataFromServer(true, HttpRequest.PUT, HelpTools.getUrl(CommonConfig.CAMPAIGN_INVITES_URL + "/" + mCampaignInviteEntity.getId() + "/share"), requestParams, new RequestCallback() {

                @Override
                public void onError(Exception e) {

                    CustomToast.showShort(activity, "网络连接错误");
                }

                @Override
                public void onResponse(String response) {
                    LogUtil.LogShitou("转发分享结果", response);
                    CampaignInviteBean campaignInviteEntity = GsonTools.jsonToBean(response, CampaignInviteBean.class);
                    listBean = GsonTools.jsonToBean(response, CampaignListBean.class);
                    if (campaignInviteEntity != null && campaignInviteEntity.getError() == 0) {
                        CampaignListBean.CampaignInviteEntity entity = campaignInviteEntity.getCampaign_invite();
                        // updateBottomShareView(entity);
                        bean = entity;
                        entityId = String.valueOf(entity.getId());
                        // NotifyManager.getNotifyManager().notifyChange(NotifyManager.TYPE_SHARE_SUCCESS);
                        SUCCESS = "success";
                        popSharedialog(activity, entity, plat, wechatType);
                    } else if (campaignInviteEntity != null) {
                        CustomToast.showShort(activity, campaignInviteEntity.getDetail());
                    } else {
                        CustomToast.showShort(activity, "分享失败，请重新分享");
                    }
                }
            });
        }
    }

    private CampaignListBean.CampaignInviteEntity bean;
    private CampaignListBean listBean;
    private String entityId;

    /**
     分享成功后的截图审核规则
     @param activity
     @param entity
     */
    private void showShareSuccessDialog(final Activity activity, final CampaignListBean.CampaignInviteEntity entity) {

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_reject_screenshot, null);
        TextView confirmTV = (TextView) view.findViewById(R.id.tv_confirm);
        TextView infoTv = (TextView) view.findViewById(R.id.tv_info);
        TextView rightTv = (TextView) view.findViewById(R.id.tv_right);
        TextView topTv = (TextView) view.findViewById(R.id.tv_top);
        topTv.setVisibility(View.GONE);
        infoTv.setText("必须点击左下角上传截图才能获得奖励");
        confirmTV.setText(R.string.upload_screenshot);
        rightTv.setText(R.string.known);
        infoTv.setGravity(Gravity.LEFT);
        final CustomDialogManager cdm = new CustomDialogManager(activity, view);
        confirmTV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // showSelectImageDialog(activity, entity, false);
                cdm.dismiss();
                showSnapDialog(activity, entity, "2");
            }
        });
        rightTv.setOnClickListener(new View.OnClickListener() {

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

    /**
     给gg使用的弹窗
     @param activity
     @param str
     */
    private void showGgDialog(final Activity activity, String str) {

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_gg_rule, null);
        TextView infoTv = (TextView) view.findViewById(R.id.tv_rule);
        LinearLayout llBg = (LinearLayout) view.findViewById(R.id.layout_bg);
        infoTv.setText(str);
        final CustomDialogManager cdm = new CustomDialogManager(activity, view);
        llBg.setOnClickListener(new View.OnClickListener() {

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

    /**
     提示30分钟后上传截图
     */
    private void show(final Activity activity) {

        final CustomDialog customDialog = new CustomDialog(activity, R.layout.dialog_uploadsceenshotinfo);
        View view = customDialog.getView();
        final TextView tv_know = (TextView) view.findViewById(R.id.tv_know);
        tv_know.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showHelpInfoDialog(activity);
                customDialog.dismiss();
            }
        });
        customDialog.dg.getWindow().setLayout(DensityUtils.dp2px(270), DensityUtils.dp2px(150));
        customDialog.showDialog();
    }

    /**
     显示上传截图帮助对话框
     */
    private void showHelpInfoDialog(Activity activity) {

        final SwipeBackDialog myDialog = new SwipeBackDialog(activity, R.layout.dialog_upload_screenshot_help);
        View view = myDialog.getView();
        final TextView tv_know = (TextView) view.findViewById(R.id.tv_know);
        tv_know.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                myDialog.dismiss();
            }
        });
        myDialog.dg.getWindow().setLayout(DensityUtils.getScreenWidth(activity.getApplicationContext()), DensityUtils.getScreenHeight(activity.getApplicationContext()) - DensityUtils.getStatusHeight(activity));
        myDialog.showDialog();

    }

    public class MySharedListener implements PlatformActionListener {

        private Activity activity;
        private CampaignListBean.CampaignInviteEntity campaignInviteEntity;

        public MySharedListener(Activity activity, CampaignListBean.CampaignInviteEntity campaignInviteEntity) {

            this.activity = activity;
            this.campaignInviteEntity = campaignInviteEntity;
        }

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

            if (mCampaignInviteEntity.getStatus().equals("running") || CAMPAIGN_TYPE_RECRUIT.equals(mCampaignInviteEntity.getCampaign().getPer_budget_type())) {
                if (bean != null) {
                    updateBottomShareView(bean);
                    NotifyManager.getNotifyManager().notifyChange(NotifyManager.TYPE_SHARE_SUCCESS);
                    SUCCESS = "success";
                    if (((tvLeft.getText().toString().trim().equals(activity.getString(R.string.upload_screenshot))))) {
                        // showShareSuccessDialog(activity, bean);
                        if (listBean != null) {
                            if (! TextUtils.isEmpty(listBean.getAlert())) {
                                showGgDialog(activity, listBean.getAlert());
                            } else {
                                showShareSuccessDialog(activity, bean);
                            }
                        } else {
                            showShareSuccessDialog(activity, bean);
                        }
                    } else {
                        if (! TextUtils.isEmpty(listBean.getAlert())) {
                            showGgDialog(activity, listBean.getAlert());
                        }
                    }
                }
            } else if (mCampaignInviteEntity.getStatus().equals(STATE_APPROVED)) {
                String screenshot = mCampaignInviteEntity.getScreenshot();
                if (TextUtils.isEmpty(screenshot) && ((tvLeft.getText().toString().trim().equals(activity.getString(R.string.upload_screenshot))))) {
                    if (listBean != null) {
                        if (! TextUtils.isEmpty(listBean.getAlert())) {
                            showGgDialog(activity, listBean.getAlert());
                        } else {
                            showShareSuccessDialog(activity, mCampaignInviteEntity);
                        }
                    } else {
                        showShareSuccessDialog(activity, mCampaignInviteEntity);
                    }
                }
            } else {
                if (listBean != null) {
                    if (! TextUtils.isEmpty(listBean.getAlert())) {
                        showGgDialog(activity, listBean.getAlert());
                    }
                }
            }
            // 分享活动成功埋点
            TalkingDataAppCpa.onCustEvent4();
            CustomToast.showShort(activity, "分享成功");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            CustomToast.showShort(activity, "分享失败，请重新分享");

        }

        @Override
        public void onCancel(Platform platform, int i) {
            CustomToast.showShort(activity, "取消分享");
        }
    }
}



