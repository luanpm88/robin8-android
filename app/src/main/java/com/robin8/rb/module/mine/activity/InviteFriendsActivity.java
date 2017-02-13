package com.robin8.rb.module.mine.activity;

import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.module.mine.model.InviteFridensModel;
import com.robin8.rb.module.share.CustomShareHelper;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.view.widget.CustomDialogManager;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by IBM on 2016/8/14.
 */
public class InviteFriendsActivity extends BaseActivity {
    private static final String IMAGE_URL = "http://7xq4sa.com1.z0.glb.clouddn.com/robin8_icon.png";
    private static final String TITLE_URL = CommonConfig.SERVICE+"/invite?inviter_id=";

    @Bind(R.id.tv_reword_info)
    TextView tvRewordInfo;
    @Bind(R.id.tv_invite_number)
    TextView tvInviteNumber;
    @Bind(R.id.tv_reword_money)
    TextView tvRewordMoney;
    private CustomDialogManager mCustomDialogManager;
    private CustomShareHelper mCustomShareHelper;

    @Override
    public void setTitleView() {
        mTVCenter.setText(getString(R.string.invite_friends));
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_invite_friends, mLLContent);
        ButterKnife.bind(this);
        mBottomTv.setVisibility(View.VISIBLE);
        mBottomTv.setOnClickListener(this);
        mBottomTv.setText(R.string.invite_instantly);
        tvRewordInfo.setText(Html.fromHtml("邀请好友下载立得<font color=#ecb200>" + "2元" + "</font>现金奖励"));
        initData();
    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.MY_INVITE;
        super.onResume();
    }

    private void initData() {
        BasePresenter mBasePresenter = new BasePresenter();
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.INVITE_FRIENDS_URL), null, new RequestCallback() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
                LogUtil.logXXfigo("response+" + response);
                InviteFridensModel inviteFridensModel = GsonTools.jsonToBean(response, InviteFridensModel.class);
                if (inviteFridensModel != null && inviteFridensModel.getError() == 0) {
                    updateView(inviteFridensModel);
                }
            }
        });
    }

    private void updateView(InviteFridensModel inviteFridensModel) {
        tvRewordMoney.setText("¥ " +StringUtil.deleteZero(inviteFridensModel.getInvite_amount()));
        tvInviteNumber.setText(String.valueOf(inviteFridensModel.getInvite_count()));
    }


    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_bottom:
                showInviteDialog();
                break;
            case R.id.tv_weixin:
                share(Wechat.NAME);
                break;
            case R.id.tv_wechatmoments:
                share(WechatMoments.NAME);
                break;
            case R.id.tv_weibo:
                share(SinaWeibo.NAME);
//                ShareParams mShareParams  = new ShareParams();
//                mShareParams.setTitle("Title:我是一个兵");
//                mShareParams.setImagePath(IMAGE_URL);
//                mShareParams.setFrom(1);
//                mShareParams.setSubTitle("SubTitle: what a fuck world!");
//                mShareParams.setText("Text: 我参加革命十三年，挨过刀，扛过抢，拼过命，逃过场。。。");
//                mShareParams.setUrl("www.baidu.com");
//                mCustomShareHelper = new CustomShareHelper(this, mShareParams);
//                mCustomShareHelper.shareToSina();
                break;
            case R.id.tv_qq:
                share(QQ.NAME);
                break;
            case R.id.tv_qonze:
                share(QZone.NAME);
                break;
        }
    }

    /**
     * 弹出分享面板
     *
     * @param platName
     */
    private void share(String platName) {

        if (mCustomDialogManager != null) {
            mCustomDialogManager.dismiss();
        }

        int id = BaseApplication.getInstance().getLoginBean().getKol().getId();
        CustomToast.showShort(this, "正在前往分享...");
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        oks.setCallback(new MySharedListener());
        oks.setPlatform(platName);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        if (SinaWeibo.NAME.equals(platName)) {
            oks.setText(getString(R.string.share_invite_friends_text) + TITLE_URL+String.valueOf(id));
        } else {
            oks.setText(getString(R.string.share_invite_friends_text));
        }
        oks.setTitle(getString(R.string.share_invite_friends_title));

        oks.setTitleUrl(TITLE_URL+String.valueOf(id));
        oks.setImageUrl(IMAGE_URL);
        oks.setUrl(TITLE_URL+String.valueOf(id));
        oks.setSite(getString(R.string.app_name));
        oks.setSiteUrl(CommonConfig.SITE_URL);
        oks.show(this);
    }

    private void showInviteDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.invite_friends_dialog, null);
        TextView weixinTV = (TextView) view.findViewById(R.id.tv_weixin);
        TextView wechatmomentsTV = (TextView) view.findViewById(R.id.tv_wechatmoments);
        TextView weiboTV = (TextView) view.findViewById(R.id.tv_weibo);
        TextView qqTV = (TextView) view.findViewById(R.id.tv_qq);
        TextView qonzeTV = (TextView) view.findViewById(R.id.tv_qonze);
        TextView cancelTV = (TextView) view.findViewById(R.id.tv_cancel);
        mCustomDialogManager = new CustomDialogManager(this, view);
        weixinTV.setOnClickListener(this);
        wechatmomentsTV.setOnClickListener(this);
        weiboTV.setOnClickListener(this);
        qqTV.setOnClickListener(this);
        qonzeTV.setOnClickListener(this);
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomDialogManager.dismiss();
            }
        });
        mCustomDialogManager.dg.setCanceledOnTouchOutside(true);
        mCustomDialogManager.dg.getWindow().setGravity(Gravity.BOTTOM);
        mCustomDialogManager.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        mCustomDialogManager.showDialog();
    }

    private class MySharedListener implements PlatformActionListener {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            CustomToast.showShort(InviteFriendsActivity.this, "分享成功");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            CustomToast.showShort(InviteFriendsActivity.this, "分享失败");
        }

        @Override
        public void onCancel(Platform platform, int i) {
            CustomToast.showShort(InviteFriendsActivity.this, "分享取消");
        }
    }
}
