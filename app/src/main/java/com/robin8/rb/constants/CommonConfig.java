package com.robin8.rb.constants;

import android.os.Environment;

import com.robin8.rb.base.BaseApplication;

import java.io.File;

/**
 * 主要的配置文件
 */
public class CommonConfig {

    public static final String HELP_URL = "http://robin8.net/kol_publish_campaign_help";
    public static final String SITE_URL = "http://www.robin8.net";

    static {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            path = Environment.getExternalStorageDirectory()
                    + File.separator + "robin8" + File.separator;
        else
            path = BaseApplication.getContext().getCacheDir()
                    + File.separator;
    }

    public static final String TOURIST_PHONE = "13000000000";//游客手机号
    public static final String TOURIST_CODE = "123456";//游客验证码

    //缓存路径
    public static String path;

    //  public static final String SERVICE="http://restful.mictech.com.cn/";
    //	public static String SERVICE="http://192.168.0.18:8080/watch-http/";

    // public static String SERVICE = "http://test.robin8.net/";//robin8-测试服务器地址
    public static String SERVICE = "http://139.196.44.225/";//beansmile-测试服务器地址
    // public static String SERVICE = "http://robin8.net/";//正式服务器地址
    public static final String UpdateUrl = "api/v2/upgrades/check";
    public static final String BIND_IDENTITY_URL = "api/v2/influences/bind_identity";
    public static final String START_URL = "api/v2/influences/start";
    public static final String UNBIND_IDENTITY_URL = "api/v2/influences/unbind_identity";
    public static final String KOLS_IDENTITY_BIND_URL ="api/v1/kols/identity_bind";
    public static final String CAMPAIGN_INVITES_URL = "api/v1/campaign_invites";
    public static final String SIGN_IN_URL = "api/v2/kols/sign_in";
    public static final String GET_CODE_URL = "api/v1/phones/get_code";
    public static final String FEED_BACK_URL =  "api/v1/feedbacks/create";

    public static final String CAMPAIGNS_DETAIL_URL = "api/v1/campaigns/";
    public static final String CAMPAIGNS_APPLY_URL = "api/v1/campaigns/apply";

    public static final String LAUNCH_CAMPAIGNS_URL = "api/v1_4/kol_campaigns";
    public static final String MODIFY_LAUNCH_CAMPAIGNS_URL = "api/v1_4/kol_campaigns/update";
    public static final String KOLS_ACCOUNT_URL = "api/v1/kols/account";
    public static final String HELP_INFO_URL = "api/v2/system/account_notice";
    public static final String TRANSACTIONS_URL = "api/v1_3/transactions";
    public static final String KOLS_ALIPAY_URL = "api/v1_3/kols/alipay";//  是否绑定
    public static final String WITHDRAW_APPLY_URL = "api/v1_3/withdraws/apply";//  提现申请
    public static final String BIND_ALIPAY_URL = "api/v1_3/kols/bind_alipay";// 绑定支付宝账号
    public static final String PAY_BY_VOUCHER_URL = "api/v1_4/kol_campaigns/pay_by_voucher";// 是否 使用任务奖金抵扣
    public static final String CAMPAIGN_CHECK_SUCCESS_BY_ALIPAY_URL = "api/v1_4/kol_campaigns/show";// 付宝支付后 向服务器确认 是否 支付成功:
    public static final String CAMPAIGN_DETAIL_URL = "api/v1_4/kol_campaigns/detail";// 活动详情
    public static final String BRAND_AMMOUNT_PAY_URL = "api/v1_4/kol_campaigns/pay";// 使用 brand 余额 支付
    public static final String BRAND_RECHARGE_URL = "api/v1_4/kol_brand/recharge";// 充值

    public static final String KOL_BRAND_AMOUNT_URL = "api/v1_4/kol_brand";// 广告主余额
    public static final String KOL_BRAND_BILL_URL = "api/v1_4/kol_brand/bill";

    public static final String KOL_CAMPAIGNS_REVOKE_URL = "api/v1_4/kol_campaigns/revoke";//撤销活动

    public static final String LOTTERY_ACTIVITIES_URL = "api/v1_3/lottery_activities";//夺宝活动  活动列表
    public static final String LOTTERY_ACTIVITIES_MINE_URL = "api/v1_3/my/lottery_activities";//我的夺宝活动  活动列表
    public static final String LOTTERY_ORDERS_URL = "api/v1_3/lottery_orders";
    public static final String DELIVERY_ADDRESS_URL = " api/v1_3/my/address";
    public static final String UPDATE_PROFILE_URL = "api/v1_3/kols/update_profile";

    public static final String CHECK_IN_HISTORY_URL = "api/v1_3/tasks/check_in_history";//签到历史
    public static final String CHECK_IN_URL = "api/v1_3/tasks/check_in";//签到

    public static final String FIRST_KOL_LIST_URL = "api/v1_6/big_v";//首页列表数据
    public static final String MY_CARE_URL = "api/v1_6/my/friends";

    public static final String MY_SHOW_URL = "api/v1_6/my/show";
    public static final String BIG_V_APPLY_FIRST_URL = "api/v1_6/big_v_applies/update_profile";
    public static final String UPDATE_SOCIAL_URL = "api/v1_6/big_v_applies/update_social";
    public static final String SUBMIT_APPLY_URL = "api/v1_6/big_v_applies/submit_apply";
    public static final String CAMPAIGNS_MATERIALS_URL = "api/v1_6/campaigns/materials";
    public static final String CAMPAIGN_INVITES16_URL = "api/v1_6/campaign_invites/";
    public static final String MY_SHOW13_URL = "api/v1_3/my/show";
    public static final String MESSAGES_URL = "api/v1/messages";
    public static final String READ_ALL_MESSAGES_URL ="api/v2/messages/read_all";
    public static final String HELP_CENTER_URL = "api/v1_6/system/account_notice";
    public static final String SWEEP_LOGIN_URL = "api/v1_5/scan_qr_code_and_login";

    public static final String INVITE_FRIENDS_URL =  "api/v1_3/tasks/invite_info";
    public static final String OAUTH_LOGIN_URL = "api/v2/kols/oauth_login";
    public static final String BIND_MOBILE_URL = "api/v1/kols/bind_mobile";
    public static final String JOINED_KOLS_URL = "api/v1_4/kol_campaigns/joined_kols";

    public static final String CPS_ARTICLES_URL = "api/v1_7/cps_articles";//文章创作首页列表
    public static final String PRODUCT_LIST_URL = "api/v1_7/cps_materials";//商品列表
    public static final String ARTICLES_LIST_URL = "api/v2/articles";//文章列表
    public static final String ARTICLES_SEARCH_URL = "api/v2/articles/search";//文章搜索
    public static final String MY_CREATE_URL = "api/v1_7/cps_articles/my_articles";//我的创作列表
    public static final String SHARE_ARTICLE_URL =  "api/v1_7/cps_articles/share_article";
    public static final String IMAGES_UPLOAD_URL =  "api/v1_7/images/upload";
    public static final String RELEASE_URL =  "api/v1_7/cps_articles/create";

    public static final String EXPECT_EFFECT_LIST_URL = "api/v1_8/campaign_analysis/expect_effect_list";
    public static final String CONTENT_ANALYSIS_URL = "api/v1_8/campaign_analysis/content_analysis";
    public static final String INVITEE_ANALYSIS_URL = "api/v1_8/campaign_analysis/invitee_analysis";
    public static final String CATEGORIES_URL =  "api/v1_7/cps_materials/categories";

    public static final String CAMPAIGN_EVALUATIONS = "api/v1_8/campaign_evaluations/evaluate";
}
