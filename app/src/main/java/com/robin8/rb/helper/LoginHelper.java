package com.robin8.rb.helper;

import android.app.Activity;
import android.content.Intent;

import com.robin8.rb.R;
import com.robin8.rb.activity.ADHostActivity;
import com.robin8.rb.activity.MyCampaignActivity;
import com.robin8.rb.activity.WalletActivity;
import com.robin8.rb.base.BaseRecyclerViewActivity;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.model.LoginBean;
import com.robin8.rb.module.first.activity.LaunchRewordFirstActivity;
import com.robin8.rb.module.mine.activity.CollectMoneyActivity;
import com.robin8.rb.module.mine.activity.HelpCenterActivity;
import com.robin8.rb.module.mine.activity.InvitationCodeActivity;
import com.robin8.rb.module.mine.activity.SettingActivity;
import com.robin8.rb.module.mine.activity.UserSignActivity;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;


/**
 * Created by IBM on 2016/7/14.
 */
public class LoginHelper {
    public static void loginSuccess(LoginBean loginBean , int from, Activity mActivity) {
        Intent intent = null;
        CustomToast.showShort(mActivity, "登录成功");
        switch (from) {
            case SPConstants.MAINACTIVITY:
                LogUtil.LogShitou("哈哈哈","zheli");
                mActivity.finish();
                break;
            case SPConstants.DETAILCONTENTACTIVITY:
                mActivity.setResult(SPConstants.REQUEST_DETAILCONTENTACTIVITY);
                mActivity.finish();
                break;
            case SPConstants.LAUNCHREWORDACTIVIRY:
                intent = new Intent(mActivity, LaunchRewordFirstActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mActivity.startActivity(intent);
                mActivity.finish();
                break;
            case SPConstants.WALLETACTIVIRY:
                intent = new Intent(mActivity, WalletActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mActivity.startActivity(intent);
                mActivity.finish();
                break;
            case SPConstants.AD_HOST_ACTIVITY:
                intent = new Intent(mActivity, ADHostActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mActivity.startActivity(intent);
                mActivity.finish();
                break;
            case SPConstants.ROBININDIANA:
                intent = new Intent(mActivity, BaseRecyclerViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("destination", SPConstants.INDIANA_ROBIN);
                intent.putExtra("url", HelpTools.getUrl(CommonConfig.LOTTERY_ACTIVITIES_URL));
                intent.putExtra("title", mActivity.getString(R.string.robin_indiana));
                mActivity.startActivity(intent);
                mActivity.finish();
                break;
            case SPConstants.USER_SIGN_ACTIVITY:
                intent = new Intent(mActivity, UserSignActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mActivity.startActivity(intent);
                mActivity.finish();
                break;
            case SPConstants.MY_CAMPAIGN_ACTIVITY:
                intent = new Intent(mActivity, MyCampaignActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mActivity.startActivity(intent);
                mActivity.finish();
                break;
            case SPConstants.SETTING_ACTIVITY:
                intent = new Intent(mActivity, SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mActivity.startActivity(intent);
                mActivity.finish();
                break;
            case SPConstants.INVITE_FRIENDS_ACTIVITY:
                intent = new Intent(mActivity, CollectMoneyActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mActivity.startActivity(intent);
                mActivity.finish();
                break;
            case SPConstants.INVITATION_CODE:
                intent = new Intent(mActivity, InvitationCodeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mActivity.startActivity(intent);
                mActivity.finish();
                break;
            case SPConstants.HELP_CENTER:
                intent = new Intent(mActivity, HelpCenterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mActivity.startActivity(intent);
                mActivity.finish();
            case SPConstants.RONG_CLOUD:
                mActivity.finish();
            default:
                mActivity.finish();
                break;
        }
    }
}
