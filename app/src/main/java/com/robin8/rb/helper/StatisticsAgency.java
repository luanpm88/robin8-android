package com.robin8.rb.helper;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.robin8.rb.util.LogUtil;
import com.tendcloud.appcpa.TalkingDataAppCpa;
import com.tendcloud.tenddata.TCAgent;

/**
 * Created by IBM on 2016/8/11.
 */
public class StatisticsAgency {

    // 首页
    public static final String KOL_LIST = "kol-list";//KOL首页
    public static final String KOL_APPLY_INFO = "kol-apply-info";//KOL申请填写个人资料
    public static final String KOL_APPLY_SOCIAL = "kol-apply-social";//KOL申请填写社交账号
    public static final String KOL_APPLY_SUBMIT = "kol-apply-submit";//KOL申请提交审核
    public static final String KOL_DETAIL = "kol-detail";//KOL详情
    public static final String KOL_LIST_CATEGORY = "kol-list-category";//KOL搜索列表(点击Tag)
    public static final String KOL_SEARCH = "kol-search";//KOL搜索
    public static final String KOL_LIST_FOLLOWERS = "kol-list-followers";//KOL关注列表

    //通知
  //  public static final String NOTIFICATION_LIST = "notification-list";//活动首页
    public static final String INFLUENCE_LIST = "My_influence";//影响力
    public static final String PK_INFLUENCE = "Pk_influence";//影响力pk
    public static final String OTHER_INFLUENCE = "Other_people_influence";//他人影响力
    public static final String GO_TEST_INFLUENCE = "Go_test_influence";//影响力


    // 活动
    public static final String CAMPAIGN_LIST = "campaign-list";//活动首页
    public static final String CAMPAIGN_DETAIL = "campaign-detail";//悬赏活动详情
    public static final String CAMPAIGN_INVITE = "campaign-invite";//邀请活动详情
    public static final String CAMPAIGN_DETAIL_KOLS_LIST = "campaign-detail-kols-list";//悬赏活动KOL参与列表
    public static final String CAMPAIGN_RECRUIT = "campaign-recruit";//招募活动详情
    public static final String CITY_LIST = "city-list";//城市列表
    public static final String SIGN_UP_RECRUIT = "sign_up_recruit";//招募活动报名

    //创作
    public static final String CREATE_LIST = "create_list";
    public static final String PRODUCT_LIST = "product_list";
    public static final String MY_CREATE = "my_create";
    public static final String START_CREATE = "create";
    public static final String ARTICLE_SEARCH = "article_search";

    // 品牌主发起活动
    public static final String ADVERTISER_ADD_PAY = "advertiser-add-pay";//品牌主发起活动支付
    public static final String ADVERTISER_ADD_PREVIEW = "advertiser-add-preview";//品牌主发起活动预览
    public static final String ADVERTISER_ADD = "advertiser-add";//品牌主发起活动编辑
    public static final String ADVERTISER_MONEY_LIST = "advertiser-money-list";//品牌主余额
    public static final String ADVERTISER_MY_DETAIL_KOLS = "advertiser-my-detail-kols";//品牌主活动KOL参与列表
    public static final String ADVERTISER_MY_DETAIL = "advertiser-my-detail";//品牌主活动详情
    public static final String ADVERTISER_MY_LIST = "advertiser-my-list";//品牌主活动列表
    public static final String ADVERTISER_MY = "advertiser-my";//品牌主主页

    public static final String ADVERTISER_ADD_PAY_DETAIL = "advertiser-add-pay-detail";//品牌主发起活动支付详情
    public static final String ADVERTISER_ADD_PAY_ERROR = "advertiser-add-pay-error";//品牌主发起活动支付失败
    public static final String ADVERTISER_ADD_PAY_SUCCESSED = "advertiser-add-pay-successed";//品牌主发起活动支付成功
    public static final String ADVERTISER_RECHARGE_PAY_ERROR = "advertiser-recharge-pay-error";//品牌主充值失败
    public static final String ADVERTISER_RECHARGE_PAY_SUCCESSED = "advertiser-recharge-pay-successed";//品牌主充值成功
    public static final String ADVERTISER_RECHARGE = "advertiser-recharge";//品牌主充值

    // 我的
    public static final String MY = "my";//我的主页
    public static final String MY_FAVORITE = "my-favorite";//我的收藏文章
    public static final String MY_TASK = "my-task";//我的活动
    public static final String MY_CONTACT_KOL = "my-contact-kol";//我的特邀KOL
    public static final String MY_CONTACT_RANKING = "my-contact-ranking";//我的搜索引擎合作
    public static final String MY_HELP = "my-help";//我的帮助中心
    public static final String MY_INCOME_BUNDING = "my-income-bunding";//我的钱包绑定
    public static final String MY_INCOME_CASH = "my-income-cash";//我的钱包提现
    public static final String MY_INCOME_LIST = "my-income-list";//我的钱包账单
    public static final String MY_INCOME = "my-income";//我的钱包
    public static final String MY_INDIANA_ADDRESS = "my-indiana-address";//我的一元夺宝收货地址
    public static final String MY_INDIANA_DETAIL_CALCULATION = "my-indiana-detail-calculation";//我的一元夺宝计算规则
    public static final String MY_INDIANA_DETAIL_DES = "my-indiana-detail-des";//我的一元夺宝商品详情
    public static final String MY_INDIANA_DETAIL = "my-indiana-detail";//我的一元夺宝详情页
    public static final String MY_INDIANA_ALL = "my-indiana-all";//我的一元夺宝我的夺宝
    public static final String MY_INDIANA_PAY = "my-indiana-pay";//我的一元夺宝支付页面
    public static final String MY_INDIANA = "my-indiana";//我的一元夺宝列表
    public static final String MY_INVITE = "my-invite";//我的邀请好友
    public static final String MY_SIGN = "my-sign";//我的每日签到
    public static final String MY_SETTING = "my-setting";//我的设置
    public static final String MY_MESSAGE = "my-message";//我的消息
    public static final String MY_INVITATION_CODE = "my-incitation_code";//输入邀请码

    public static final String SINA_SHARE = "sina_share";//新浪分享
    public static final String TD_AD_ID = "TD_AD_ID";
    public static final String TD_AD_CHANNEL_ID = "GooglePlay";
    public static final String TD_ANALYTICS_APP_ID = "TD_ANALYTICS_APP_ID";
    public static final String TD_ANALYTICS_CHANNEL_ID = "TD_ANALYTICS_CHANNEL_ID";

    public static void init(Context context) {
        TCAgent.LOG_ON = true;
        TalkingDataAppCpa.LOG_NO = true;
        TCAgent.setReportUncaughtExceptions(true);

        try {
            ApplicationInfo applicationInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            //talking data 应用统计初始化
            String analyticChannelId = applicationInfo.metaData.getString(TD_ANALYTICS_CHANNEL_ID);
            String analyticAppId = applicationInfo.metaData.getString(TD_ANALYTICS_APP_ID);
            TCAgent.init(context,analyticAppId,analyticChannelId);
            //talking data 广告检测初始化
            String adAppId = applicationInfo.metaData.getString(TD_AD_ID);
            LogUtil.LogShitou("埋点的id","++-->"+adAppId);
            TalkingDataAppCpa.init(context,
                    adAppId,
                    TD_AD_CHANNEL_ID);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void onPageStart(Context context, String pageName) {
        LogUtil.logXXfigo("onPageStart  " + pageName);
        context = context.getApplicationContext();
        TCAgent.onPageStart(context, pageName);
    }

    public static void onPageEnd(Context context, String pageName) {
        LogUtil.logXXfigo("onPageEnd  " + pageName);
        context = context.getApplicationContext();
        TCAgent.onPageEnd(context, pageName);
    }
}
