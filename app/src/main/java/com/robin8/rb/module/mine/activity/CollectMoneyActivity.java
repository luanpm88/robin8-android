package com.robin8.rb.module.mine.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.view.widget.CustomDialogManager;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class CollectMoneyActivity extends BaseActivity {

    @Bind(R.id.tv_all_collect_money)
    TextView tvAllCollectMoney;
    @Bind(R.id.tv_today_apprentices)
    TextView tvTodayApprentices;
    @Bind(R.id.img_down)
    ImageView imgDown;
    @Bind(R.id.ll_rule)
    LinearLayout llRule;
    @Bind(R.id.ll_go_to_all_collect_money)
    RelativeLayout llGoToAllCollectMoney;
    @Bind(R.id.ll_go_to_today_apprentice_list)
    RelativeLayout llGoToTodayApprenticeList;
    @Bind(R.id.btn_action_share)
    TextView btnActionShare;
    @Bind(R.id.btn_action_invite)
    TextView btnActionInvite;
    @Bind(R.id.tv_invite_code)
    TextView tvInviteCode;
    @Bind(R.id.btn_action_code)
    TextView btnActionCode;
    @Bind(R.id.tv_detail)
    TextView tvDetail;
    @Bind(R.id.tv_gg_rule)
    TextView tvGgRule;
    @Bind(R.id.ll_share_money_rule)
    LinearLayout llGgRule;
    private WProgressDialog mWProgressDialog;
    private String invite_amount;
    private static final String IMAGE_URL = "http://7xq4sa.com1.z0.glb.clouddn.com/robin8_icon.png";
    private static final String TITLE_URL = CommonConfig.SERVICE + "/invite?inviter_id=";
    private CustomDialogManager mCustomDialogManager;
    private CustomShareHelper mCustomShareHelper;
    @Override
    public void setTitleView() {
        mTVCenter.setText(getString(R.string.apprentice_collect_money));
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_collect_money, mLLContent, true);
        ButterKnife.bind(this);

        imgDown.setOnClickListener(this);
        llGoToAllCollectMoney.setOnClickListener(this);
        llGoToTodayApprenticeList.setOnClickListener(this);
        btnActionCode.setOnClickListener(this);
        btnActionInvite.setOnClickListener(this);
        btnActionShare.setOnClickListener(this);
        initData();
    }

    private void initData() {
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();
        BasePresenter mBasePresenter = new BasePresenter();
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.INVITE_FRIENDS_URL), null, new RequestCallback() {

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
                LogUtil.LogShitou("收徒好友+", response);
                InviteFridensModel inviteFridensModel = GsonTools.jsonToBean(response, InviteFridensModel.class);
                if (inviteFridensModel != null && inviteFridensModel.getError() == 0) {
                    updateView(inviteFridensModel);
                }
            }
        });
    }
    private void updateView(InviteFridensModel inviteFridensModel) {
        invite_amount = inviteFridensModel.getInvite_amount();
        tvAllCollectMoney.setText(StringUtil.deleteZero(inviteFridensModel.getInvite_amount()));
        tvTodayApprentices.setText(String.valueOf(inviteFridensModel.getInvite_count()));
        tvInviteCode.setText("邀请码收徒("+String.valueOf(inviteFridensModel.getInvite_code())+")");
        if (TextUtils.isEmpty(inviteFridensModel.getDesc())){
            llGgRule.setVisibility(View.GONE);
        }else {
            llGgRule.setVisibility(View.VISIBLE);
            tvGgRule.setText(inviteFridensModel.getDesc());
        }
        if (TextUtils.isEmpty(inviteFridensModel.getInvite_desc())){
            tvDetail.setText(Html.fromHtml("每天前<b><font color=#ecb200><big>" + "10" + "</big></font></b>名徒弟有<b><font color=#ecb200><big>" + "2" + "</big></font></b>元额外奖励"));
        }else {
            tvDetail.setText(inviteFridensModel.getInvite_desc());
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.img_down:
                if (llRule.getVisibility() == View.VISIBLE) {
                    imgDown.setImageResource(R.mipmap.icon_down);
                    llRule.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pop_out));
                    llRule.setVisibility(View.GONE);
                } else {
                    imgDown.setImageResource(R.mipmap.icon_invite_up);
                    llRule.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pop_in));
                    llRule.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.ll_go_to_all_collect_money:
                //总收益
                Intent intent = new Intent(CollectMoneyActivity.this, CollectListActivity.class);
                intent.putExtra(CollectListActivity.ALL_COLLECT_MONEY,invite_amount);
                intent.putExtra(CollectListActivity.TYPE,CollectListActivity.APPRENTICE_MONEY);
                startActivity(intent);
                break;
            case R.id.ll_go_to_today_apprentice_list:
                //今日收徒
                Intent intentT = new Intent(CollectMoneyActivity.this, CollectListActivity.class);
                intentT.putExtra(CollectListActivity.TYPE,CollectListActivity.TODAY_APPRENTICE);
                startActivity(intentT);
                break;
            case R.id.btn_action_share:
                //分享
                showInviteDialog();
                break;
            case R.id.btn_action_invite:
                //去通讯录
                Intent intentI = new Intent(CollectMoneyActivity.this, InviteFriendsActivity.class);
                startActivity(intentI);
                break;
            case R.id.btn_action_code:
                //复制邀请码从6开始
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (TextUtils.isEmpty(tvInviteCode.getText().toString().trim())){
                    CustomToast.showShort(CollectMoneyActivity.this,getString(R.string.no_net));
                }else {
                    String trim = tvInviteCode.getText().toString().trim();
                    String regEx ="[^0-9]";
                    Pattern pattern = Pattern.compile(regEx);
                    Matcher matcher = pattern.matcher(trim);
                    String result = matcher.replaceAll("").trim();
                    cm.setPrimaryClip(ClipData.newPlainText(null,result));
                    CustomToast.showShort(CollectMoneyActivity.this,"邀请码复制成功");
                }
                break;
            case R.id.tv_weixin:
                share(Wechat.NAME);
                break;
            case R.id.tv_wechatmoments:
                share(WechatMoments.NAME);
                break;
            case R.id.tv_weibo:
                share(SinaWeibo.NAME);
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
     弹出分享面板
     @param platName
     */
    private void share(String platName) {

        if (mCustomDialogManager != null) {
            mCustomDialogManager.dismiss();
        }

        int id = BaseApplication.getInstance().getLoginBean().getKol().getId();
        CustomToast.showShort(this, "正在前往分享...");
        //ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        oks.setPlatform(platName);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
       // Platform.ShareParams params = new Platform.ShareParams();
       // params.setShareType(Platform.SHARE_WEBPAGE);
        if (SinaWeibo.NAME.equals(platName)) {
            oks.setText(getString(R.string.share_invite_friends_text) + TITLE_URL + String.valueOf(id));
        } else {
            oks.setText(getString(R.string.share_invite_friends_text));
        }
        oks.setTitle(getString(R.string.share_invite_friends_title));

        oks.setTitleUrl(TITLE_URL + String.valueOf(id));
        oks.setImageUrl(IMAGE_URL);
        oks.setUrl(TITLE_URL + String.valueOf(id));
        oks.setSite(getString(R.string.app_name));
        oks.setSiteUrl(CommonConfig.SITE_URL);
        oks.setCallback(new MySharedListener());
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

            CustomToast.showShort(CollectMoneyActivity.this, "分享成功");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            CustomToast.showShort(CollectMoneyActivity.this, "分享失败");
        }

        @Override
        public void onCancel(Platform platform, int i) {
            CustomToast.showShort(CollectMoneyActivity.this, "分享取消");
        }
    }
    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.MY_APPRENTICE;
        super.onResume();
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }
    //    @Override
    //    protected void onCreate(Bundle savedInstanceState) {
    //        super.onCreate(savedInstanceState);
    //        // TODO: add setContentView(...) invocation
    //        ButterKnife.bind(this);
    //    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
