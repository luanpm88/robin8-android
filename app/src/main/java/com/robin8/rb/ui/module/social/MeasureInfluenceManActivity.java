package com.robin8.rb.ui.module.social;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.ui.activity.MainActivity;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.ui.model.BaseBean;
import com.robin8.rb.ui.model.IndentyBean;
import com.robin8.rb.ui.module.mine.presenter.BindSocialPresenter;
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

public class MeasureInfluenceManActivity extends BaseActivity {
    private String TAG = "";

    @Override
    public void setTitleView() {
        mLLTitleBar.setVisibility(View.GONE);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_measure_influence_man, mLLContent);
        HelpTools.insertCommonXml(HelpTools.ThirdIn,"Third");
        TextView tvMeasure = (TextView) view.findViewById(R.id.tv_measure_this);
        tvMeasure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //检测是否有微博授权，没有跳转到授权页面，有的话跳转到测评结果页面
                BasePresenter mBasePresenter = new BasePresenter();
                mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.INFLUENCE_INFO_LIST), null, new RequestCallback() {

                    @Override
                    public void onError(Exception e) {
                    }

                    @Override
                    public void onResponse(String response) {
                        IndentyBean indentyBean = GsonTools.jsonToBean(response, IndentyBean.class);
                        if (indentyBean != null) {
                            if (indentyBean.getError() == 0) {
                                if (indentyBean.getIdentities().size() != 0) {
                                    for (int i = 0; i < indentyBean.getIdentities().size(); i++) {
                                        if ((indentyBean.getIdentities().get(i).getProvider()).equals("weibo")) {
                                            TAG = "weibo";
                                        }
                                    }
                                    if (TAG.equals("weibo")) {
                                        HelpTools.insertCommonXml(HelpTools.IsBind,"is");
                                        jumpInfluence();
                                    } else {
                                        bindSocial(MeasureInfluenceManActivity.this, getString(R.string.weibo));
                                    }
                                } else {
                                    bindSocial(MeasureInfluenceManActivity.this, getString(R.string.weibo));
                                }
                            } else {
                                bindSocial(MeasureInfluenceManActivity.this, getString(R.string.weibo));
                            }
                        } else {
                            bindSocial(MeasureInfluenceManActivity.this, getString(R.string.weibo));
                        }
                    }

                });
            }
        });

    }

    private WProgressDialog mWProgressDialog;

    private void bindSocial(final Activity activity, final String providerName) {

        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(activity);
        }
        mWProgressDialog.show();
        BindSocialPresenter presenter = new BindSocialPresenter(activity.getApplicationContext(), null, providerName, 0);
        presenter.setOnBindListener(new BindSocialPresenter.OnBindListener() {

            @Override
            public void onResponse(String name) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
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
        } else if (activity.getString(R.string.weixin).equals(providerName)) {
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
               // LogUtil.LogShitou("提交微博绑定", "OK" + response);
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);
                if (bean == null) {
                    CustomToast.showShort(activity, activity.getString(R.string.please_data_wrong));
                    return;
                }
                if (bean.getError() == 0) {
                   // HelpTools.insertCommonXml(HelpTools.IsBind,"is");
                   // CustomToast.showShort(activity, "绑定le" + name);
                    Intent intent = new Intent(MeasureInfluenceManActivity.this, MainActivity.class);
                    intent.putExtra("register_main", "influence");
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    CustomToast.showShort(activity, "绑定失败");
                }
            }
        });
    }

    private void jumpInfluence() {
        Intent intent = new Intent(MeasureInfluenceManActivity.this, MainActivity.class);
        intent.putExtra("register_main", "influence");
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
            Intent intent = new Intent(MeasureInfluenceManActivity.this, MainActivity.class);
            intent.putExtra("register_main", "influence");
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        return false;
    }
    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.GO_TEST_INFLUENCE;
        super.onResume();
    }
}
