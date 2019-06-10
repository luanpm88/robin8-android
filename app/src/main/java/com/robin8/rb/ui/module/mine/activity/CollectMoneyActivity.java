package com.robin8.rb.ui.module.mine.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
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
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.module.mine.model.InviteFridensModel;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.util.share.RobinShareDialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 收徒
 */
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
    private static final String IMAGE_URL = CommonConfig.APP_ICON;
    private static final String TITLE_URL = CommonConfig.SERVICE + "/invite?inviter_id=";
    private RobinShareDialog shareDialog;
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
        tvInviteCode.setText(getString(R.string.robin267,inviteFridensModel.getInvite_code()));
        if (TextUtils.isEmpty(inviteFridensModel.getDesc())){
            llGgRule.setVisibility(View.GONE);
        }else {
            llGgRule.setVisibility(View.VISIBLE);
            tvGgRule.setText(inviteFridensModel.getDesc());
        }
        if (TextUtils.isEmpty(inviteFridensModel.getInvite_desc())){
            tvDetail.setText(Html.fromHtml(getString(R.string.robin349)));
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
                    CustomToast.showShort(CollectMoneyActivity.this, R.string.robin314);
                }
                break;

        }
    }

    private void showInviteDialog() {
        int id = BaseApplication.getInstance().getLoginBean().getKol().getId();
        shareDialog = new RobinShareDialog(this);
        String str = getResources().getConfiguration().locale.toString();
        String locale = str.split("_")[0];
        shareDialog.shareFacebook(TITLE_URL+ String.valueOf(id) + "&locale=" + locale,getString(R.string.share_invite_friends_title),getString(R.string.share_invite_friends_text),"");
        shareDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shareDialog.onActivityResult(requestCode,resultCode,data);
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
