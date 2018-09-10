package com.robin8.rb.module.social;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.model.BaseBean;
import com.robin8.rb.module.mine.presenter.BindSocialPresenter;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;

import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;

public class SocialBindActivity extends BaseActivity {

    private TextView tvAgreement;

    @Override
    public void setTitleView() {
        // mTVCenter.setText("社交影响力结果页");
        mLLTitleBar.setVisibility(View.GONE);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_social_bind, mLLContent);
      //  HelpTools.insertCommonXml(HelpTools.FirstIn,"First");
        tvAgreement = ((TextView) view.findViewById(R.id.tv_agreement));
        TextView tvJump = (TextView) view.findViewById(R.id.tv_jump);
        ImageButton imgWeibo = (ImageButton) view.findViewById(R.id.ib_weibo);
        ImageButton imgWeiXin = (ImageButton) view.findViewById(R.id.ib_weixin);
        tvJump.setOnClickListener(this);
        imgWeibo.setOnClickListener(this);
        imgWeiXin.setOnClickListener(this);
        tvAgreement.setText(Html.fromHtml("点击登录即同意<font color=#2dcad0>" + "用户服务协议" + "</font>"));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_jump:
                Intent intent = new Intent(SocialBindActivity.this, MeasureInfluenceActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.ib_weibo:
                bindSocial(SocialBindActivity.this,getString(R.string.weibo));
                break;
            case R.id.ib_weixin:
                bindSocial(SocialBindActivity.this,getString(R.string.weixin));
                break;
        }
    }
    private BasePresenter mBasePresenter;
    private WProgressDialog mWProgressDialog;
    private RequestParams mRequestParams;
    private void bindSocial(final Activity activity, final String providerName) {

        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(activity);
        }
        mWProgressDialog.show();
        BindSocialPresenter presenter = new BindSocialPresenter(activity.getApplicationContext(), null, providerName,0);
        presenter.setOnBindListener(new BindSocialPresenter.OnBindListener() {

            @Override
            public void onResponse(String name) {
                if (null != name) {
                    CustomToast.showShort(activity, "已成功绑定" + providerName);
                    bindPostData(activity, providerName, name);
                } else {
                    CustomToast.showLong(activity, "绑定失败，请重试");
                }
            }
        });
        if (activity.getString(R.string.weibo).equals(providerName)) {
            presenter.authorize(new SinaWeibo());
        }else if (activity.getString(R.string.weixin).equals(providerName)){
            presenter.authorize(new Wechat());
        }


    }

    private void bindPostData(final Activity activity, final String name, String userName) {

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
        //LogUtil.LogShitou("绑定微信的报价之类的", name + "//" + userName);

        mBasePresenter.getDataFromServer(true, HttpRequest.POST, (HelpTools.getUrl(CommonConfig.UPDATE_SOCIAL_URL_OLD)), params, new RequestCallback() {

            @Override
            public void onError(Exception e) {

                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
              //  LogUtil.LogShitou("提交微博绑定", "OK" + response);
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);
                if (bean == null) {
                    CustomToast.showShort(activity, activity.getString(R.string.please_data_wrong));
                    return;
                }
                if (bean.getError() == 0) {
                    Intent intent = new Intent(SocialBindActivity.this, MeasureInfluenceActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    CustomToast.showShort(activity, "绑定失败");
                }
            }
        });
    }


    @Override
    protected void executeOnclickLeftView() {

    }

    @Override
    protected void executeOnclickRightView() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(SocialBindActivity.this, MeasureInfluenceActivity.class);
            intent.putExtra("people", "women");
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        return false;
    }

}
