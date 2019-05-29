package com.robin8.rb.base.constants;

import android.os.Environment;

import com.robin8.rb.base.BaseApplication;

import java.io.File;

/**
 主要的配置文件 */
public class CommonConfig {

    public static final String HELP_URL = "http://robin8.net/kol_publish_campaign_help";
    public static final String SITE_URL = "http://www.robin8.net";
    public static final String RONG_CLOUD_KEY = "c9kqb3rdkii8j";
    public static final String RONG_CLOUD_SECRET = "3Qgy92wONUMc";
    public static final String RONG_CLOUD_ID = "KEFU151140489031686";
    public static final String APP_ICON = "http://img.robin8.net/robin8_icon.png";
    public static final String APP_IMG_URL = "http://img.robin8.net/";

    static {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            path = Environment.getExternalStorageDirectory() + File.separator + "robin8" + File.separator;
        else
            path = BaseApplication.getContext().getCacheDir() + File.separator;
    }

    public static final String TOURIST_PHONE = "13000000000";//游客手机号
    public static final String TOURIST_CODE = "123456";//游客验证码

    //缓存路径
    public static String path;
;
    //18321878526
//     public static String SERVICE = "https://qa.robin8.net/";//robin8-测试服务器地址/test server path
//     public static String SERVICE = "http://api.robin8.net/";//正式服务器地址／the formal server path
     public static String SERVICE = "http://13.229.232.233:4000/";//正式服务器地址／the formal server path
//    public static String SERVICE = "https://app.robin8.com/";//global robin8-测试服务器地址/test server path

    public static final String WEBPATH = "file:///android_asset/blockchain_pems/";//本地web路径
    public static final String UpdateUrl = "api/v2/upgrades/check";
    public static final String BIND_IDENTITY_URL = "api/v2/influences/bind_identity";
    public static final String START_URL = "api/v2/influences/start";
    public static final String UNBIND_IDENTITY_URL = "api/v2/influences/unbind_identity";
    public static final String KOLS_IDENTITY_BIND_URL_OLD = "api/v1/kols/identity_bind";
    public static final String KOLS_IDENTITY_BIND_URL = "api/v1/kols/identity_bind_v2";//新接口
    public static final String CAMPAIGN_INVITES_URL = "api/v1/campaign_invites";
    public static final String CAMPAIGN_INVITES_URL_V3 = "api/v3_0/campaign_invites";
    public static final String SIGN_IN_URL = "api/v2/kols/sign_in";
    public static final String GET_CODE_URL = "api/v1/phones/get_code";
    public static final String FEED_BACK_URL = "api/v1/feedbacks/create";
    public static final String FIRST_PAGER_URL = "api/v2_0/kols/overview";

    public static final String CAMPAIGNS_DETAIL_URL = "api/v1/campaigns/";
    public static final String CAMPAIGNS_APPLY_URL = "api/v1/campaigns/apply";

    public static final String LAUNCH_CAMPAIGNS_URL = "api/v1_4/kol_campaigns";
    public static final String MODIFY_LAUNCH_CAMPAIGNS_URL = "api/v1_4/kol_campaigns/update";
    public static final String KOLS_ACCOUNT_URL = "api/v1/kols/account";
    public static final String UPDATE_IS_READ_URL = "api/v1/withdraws/update_is_read";//异常状态提示信息已读/无参
    public static final String HELP_INFO_URL = "api/v2/system/account_notice";
    public static final String TRANSACTIONS_URL = "api/v1_3/transactions";
    public static final String KOLS_ALIPAY_URL = "api/v1_3/kols/alipay";//  是否绑定
    public static final String WITHDRAW_APPLY_URL = "api/v1_3/withdraws/apply";//  提现申请
    public static final String BIND_ALIPAY_URL = "api/v1_3/kols/bind_alipay";// 绑定支付宝账号
    public static final String PAY_BY_VOUCHER_URL = "api/v1_4/kol_campaigns/pay_by_voucher";// 是否 使用任务奖金抵扣
    public static final String PAY_BY_CREDIT_URL = "api/v1_4/kol_campaigns/pay_by_credit";// 是否 使用积分抵扣
    public static final String CAMPAIGN_CHECK_SUCCESS_BY_ALIPAY_URL = "api/v1_4/kol_campaigns/show";// 付宝支付后 向服务器确认 是否 支付成功:
    public static final String CAMPAIGN_DETAIL_URL = "api/v1_4/kol_campaigns/detail";// 活动详情
    public static final String BRAND_AMMOUNT_PAY_URL = "api/v1_4/kol_campaigns/pay";// 使用 brand 余额 支付
    public static final String BRAND_RECHARGE_URL = "api/v1_4/kol_brand/recharge";// 充值
    public static final String BRAND_PROMOTIONS_URL = "api/v2_0/promotions";// 充值积分
    public static final String BRAND_MSG_URL = "api/v1_4/kol_brand/update_profile";// 商户资料

    public static final String KOL_BRAND_AMOUNT_URL = "api/v1_4/kol_brand";// 广告主余额
    public static final String KOL_BRAND_BILL_URL = "api/v1_4/kol_brand/bill";//广告主资金账单
    public static final String KOL_BRAND_BILL_CREADIT_URL = "api/v2_0/credits";//广告主流水账单

    public static final String KOL_CAMPAIGNS_REVOKE_URL = "api/v1_4/kol_campaigns/revoke";//撤销活动

    public static final String LOTTERY_ACTIVITIES_URL = "api/v1_3/lottery_activities";//夺宝活动  活动列表
    public static final String LOTTERY_ACTIVITIES_MINE_URL = "api/v1_3/my/lottery_activities";//我的夺宝活动  活动列表
    public static final String LOTTERY_ORDERS_URL = "api/v1_3/lottery_orders";
    public static final String DELIVERY_ADDRESS_URL = " api/v1_3/my/address";
    public static final String UPDATE_PROFILE_URL = "api/v1_3/kols/update_profile";

    //    public static final String CHECK_IN_HISTORY_URL = "api/v1_3/tasks/check_in_history";//签到历史2.2.7
    //    public static final String CHECK_IN_URL = "api/v1_3/tasks/check_in";//签到2.2.7
    public static final String CHECK_IN_HISTORY_URL = "api/v1_3/check_tasks/check_in_history";//签到历史2.2.7
    public static final String CHECK_IN_URL = "api/v1_3/check_tasks/check_in";//签到2.2.7
    public static final String NEW_TASKS_UP_URL = "api/v2_0/tasks/finish_newbie";//新手任务提交post

    public static final String FIRST_KOL_LIST_URL = "api/v1_6/big_v";//首页列表数据
    public static final String MY_CARE_URL = "api/v1_6/my/friends";

    public static final String MY_SHOW_URL = "api/v1_6/my/show";//我的界面数据
    public static final String MY_INVITATION_CODE_URL = "api/v2_0/kols/invite_code";//我的界面数据
    public static final String BIG_V_APPLY_FIRST_URL = "api/v1_6/big_v_applies/update_profile";
    public static final String UPDATE_SOCIAL_URL_OLD = "api/v1_6/big_v_applies/update_social";
    // public static final String UPDATE_SOCIAL_URL = "api/v1_6/big_v_applies/update_social_v2";//新接口
    public static final String UPDATE_SOCIAL_URL = "api/v1_6/big_v_applies/update_social";//新接口
    public static final String SUBMIT_APPLY_URL = "api/v1_6/big_v_applies/submit_apply";
    public static final String CAMPAIGNS_MATERIALS_URL = "api/v1_6/campaigns/materials";
    public static final String CAMPAIGN_INVITES16_URL = "api/v1_6/campaign_invites/";
    public static final String MY_SHOW13_URL = "api/v1_3/my/show";
    public static final String MESSAGES_URL = "api/v1/messages";
    public static final String READ_ALL_MESSAGES_URL = "api/v2/messages/read_all";
    public static final String HELP_CENTER_URL = "api/v1_6/system/account_notice";
    public static final String SWEEP_LOGIN_URL = "api/v1_5/scan_qr_code_and_login";

    public static final String OAUTH_LOGIN_URL = "api/v2/kols/oauth_login";
    public static final String BIND_MOBILE_URL = "api/v1/kols/bind_mobile";
    public static final String JOINED_KOLS_URL = "api/v1_4/kol_campaigns/joined_kols";

    public static final String CPS_ARTICLES_URL = "api/v1_7/cps_articles";//文章创作首页列表
    public static final String PRODUCT_LIST_URL = "api/v1_7/cps_materials";//商品列表
    public static final String ARTICLES_LIST_URL = "api/v2/articles";//文章列表
    public static final String ARTICLES_SEARCH_URL = "api/v2/articles/search";//文章搜索
    public static final String MY_CREATE_URL = "api/v1_7/cps_articles/my_articles";//我的创作列表
    public static final String SHARE_ARTICLE_URL = "api/v1_7/cps_articles/share_article";
    public static final String IMAGES_UPLOAD_URL = "api/v1_7/images/upload";
    public static final String RELEASE_URL = "api/v1_7/cps_articles/create";

    public static final String EXPECT_EFFECT_LIST_URL = "api/v1_8/campaign_analysis/expect_effect_list";
    public static final String CONTENT_ANALYSIS_URL = "api/v1_8/campaign_analysis/content_analysis";
    public static final String INVITEE_ANALYSIS_URL = "api/v1_8/campaign_analysis/invitee_analysis";
    public static final String CATEGORIES_URL = "api/v1_7/cps_materials/categories";

    public static final String CAMPAIGN_EVALUATIONS = "api/v1_8/campaign_evaluations/evaluate";


    public static final String SOCIAL_BIND_COUNT = "api/v1/kols/bind_count";//put  - kol_id -provider
    public static final String SOCIAL_UNBIND_COUNT = "api/v1/kols/unbind_count";//put  - kol_id -provider

    //影响力
    public static final String INFLUENCE_PROFILE = "api/v1/kols/profile";// 获取用户信息Get请求
    public static final String INFLUENCE_RANK = "api/v2/influences/rank";//社交账号排名Get请求 参数：kol_uuid
    public static final String INFLUENCE_RANK_WITH_PAGE = "api/v2/influences/rank_with_page";//社交账号排名Get请求 参数：kol_uuid page
    public static final String INFLUENCE_SHARE = "api/v2/influences/share";//  社交影响力分享 参数：kol_uuid
    public static final String INFLUENCE_DETAIL = "api/v2/influences/item_detail";// 社交影响力解读 参数：kol_uuid
    public static final String INFLUENCE_UPGRADE = "api/v2/influences/upgrade";//  影响力提升 参数：kol_uuid
    public static final String INFLUENCE_INFO_LIST = "api/v1/kols/identities";//  获取第三方账号列表
    public static final String INFLUENCE_UNBIND = "api/v1/kols/identity_unbind";//  解除第三方账号绑定 PUT请求 参数： uid
    public static final String INFLUENCE_START = "api/v2/influences/start";// 社交进入测试影响力 GET请求
    public static final String INFLUENCE_BIND_CONTACT = "api/v2/influences/bind_contacts";//上传通讯录POST请求 参数：contacts（数组）
    public static final String INFLUENCE_FORWARD = "api/v2/article_actions/forward";// 我的分享GET请求 参数：page
    public static final String SOCIAL_UNBIND = "api/v1_6/big_v/unbind_social_account";// 我的分享GET请求 参数：page

    //计算影响力
    public static final String CALCULATE_INFLUENCE_SCORE = "api/v2_0/kols/calculate_influence_score";//post
    public static final String INFLUENCE_SCORE = "api/v2_0/kols/influence_score";//get
    public static final String INFLUENCE_SEND_INVITE = "api/v2/influences/send_invite";//post mobile kol_uuid
    public static final String INFLUENCE_OTHER = "api/v2_0/kols/";
    public static final String INVITE_CONTACTS = "api/v2_0/contacts/kol_contacts";
    public static final String INVITE_SEND_SMS = "api/v2_0/contacts/send_invitation";
    public static final String INFLUENCE_SHOW = "api/v2_0/kols/manage_influence_visibility";//get

    //我的-我的活动
    public static final String MY_CAMPAIGNS = "api/v1/campaign_invites/my_campaigns";//get

    public static final String RONG_CLOUD_URL = "https://api.cn.ronghub.com/user/getToken.json";//融云
    public static final String UNPAID_URL = "api/v1/campaigns/4440/receive_unpaid";//无偿转发


    public static final String EMAIL_CODE_URL = "api/v2_0/registers/valid_code";//get请求  Parameters:- email (String) (required)
    public static final String EMAIL_LOGIN_URL = "api/v2_0/sessions";//post请求  Parameters:- login (String) (required) password(String)
    public static final String EMAIL_CODE_CHECK_URL = "api/v2_0/registers/valid_email";//post请求  Parameters:- email (String) (required);valid_code (String) (required)
    public static final String EMAIL_REGISTER_URL = "api/v2_0/registers";//post请求   name (String) (required);email (String) (required);password (String) (required)
    public static final String EMAIL_FIXPWD_URL = "api/v2_0/sessions/update_password";//post请求  vtoken (String) (required);email (String) (required);new_password (String) (required)；new_password_confirmation (String) (required)

    //收徒系列
    public static final String INVITE_FRIENDS_URL = "api/v1_3/tasks/invite_info";
    public static final String APPRENTICE_COLLECT_MONEY_URL = "api/v2_0/kols/percentage_on_friend";//收徒总收益；get请求  int page
    public static final String TODAY_GET_APPRENTICE_URL = "api/v2_0/kols/today_friends";//近日已收徒get请求  int page

    //find
    public static final String FIND_ARTICLE_LIST = "api/v2_0/my/article_lists?_action=collects&";//我的收藏
    public static final String FIND_ARTICLE = "api/v2_0/articles";//发现|新鲜事hot
    public static final String FIND_SET = "api/v2_0/articles/set";
    public static final String FIND_READ = "api/v2_0/articles/read";
    public static final String FIND_ITEM_DETAIL = "api/v2_0/articles/recommends";
    public static final String BANNER_CLICK_URL = "api/v2_0/announcements/click";//点击banner调用
    public static final String SPLIT_RED_URL = "api/v2_0/articles/split_red";//拆红包post
    public static final String DOWN = "http://192.168.51.115:3001/aa.txt";//下载文件
    public static final String PUT_WALLET = "pages/blockchain_intro";//钱包

    //login_new
    public static final String UPDATE_BASE_INFO_URL = "api/v2_1/kols/base_info";//更新kol基本资料，post
    public static final String UPDATE_BASE_INFOS_URL = "api/v2_1/base_infos";//kol圈子，get
    public static final String USER_MY_SHOW_URL = "api/v2_1/kols/my_show";//我的个人信息，get
    public static final String UPDATE_WEIBO_INFO_URL = "api/v2_1/kols/applying_weibo_account";//微博信息，post
    public static final String UPDATE_CREATOR_INFO_URL = "api/v2_1/kols/applying_creator";//内容创作者信息，post
    public static final String UPDATE_WECHAT_INFO_URL = "api/v2_1/kols/applying_public_wechat_account";//微信信息，post
    public static final String CHECK_RESULT_URL = "api/v2_1/kols/set_account_is_read";//微信信息，post


    public static final String BIGV_LIST_URL = "api/v3_0/creations";//大V列表，GET
    public static final String BIGV_LIST_DETAIL_URL = "api/v3_0/creations/";//大V详情页，GET
    public static final String BIGV_LIST_DETAIL_TENDER_URL = "/tender";//大V详情报价，post
    public static final String BIGV_LIST_DETAIL_LINK_URL = "/upload_links";//大V详情添加报价，post
    public static final String BIGV_LIST_CAMPAIGN_HISTORY_URL = "api/v3_0/kols/";//大V详情添加报价，post

    public static final String GET_UPLOAD_IMAGE_TOKEN = "api/v3_0/campaign_invites/get_uptoken";//获取七牛的token
    public static final String PUT_WALLET_INFO = "api/v2_0/e_wallets/unpaid";//put钱包信息
    public static final String PUT_WALLET_LIST = "api/v2_0/e_wallets/unpaid_list";//put钱包流水


}
