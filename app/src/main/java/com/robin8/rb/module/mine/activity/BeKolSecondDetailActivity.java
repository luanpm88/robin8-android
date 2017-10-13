package com.robin8.rb.module.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.helper.IconFontHelper;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.model.BaseBean;
import com.robin8.rb.module.first.model.SocialAccountsBean;
import com.robin8.rb.module.mine.presenter.BindSocialPresenter;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 成为KOL
 */
public class BeKolSecondDetailActivity extends BaseActivity {

    private static final int TYPE_PERSONAL_SHOW = 0;
    private static final int TYPE_BIND = 1;
    private static final int TYPE_GONGZHONGHAO = 2;
    private static final int TYPE_NORMAL = 3;
    @Bind(R.id.tv_title1)
    TextView tvTitle1;
    @Bind(R.id.et_content1)
    EditText etContent1;
    @Bind(R.id.tv_title2)
    TextView tvTitle2;
    @Bind(R.id.et_content2)
    EditText etContent2;
    @Bind(R.id.tv_title3)
    TextView tvTitle3;
    @Bind(R.id.et_content3)
    EditText etContent3;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_arrow)
    TextView tvArrow;
    @Bind(R.id.rl_bind)
    RelativeLayout rlBind;
    private String name;
    private String userName;
    private boolean mBindedB;
    private int mCurrentPageType;
    private SocialAccountsBean mSocialAccountsBean;

    @Override
    public void setTitleView() {
        initData();
        if (getString(R.string.personal_show).endsWith(name)) {
            mTVCenter.setText(this.getText(R.string.please_write_personal_show));
        } else {
            mTVCenter.setText(this.getText(R.string.please_write) + name);
        }
    }

    @Override
    public void initView() {
        mBottomTv.setVisibility(View.VISIBLE);
        mBottomTv.setText(getString(R.string.submit));
        View view = LayoutInflater.from(this).inflate(R.layout.activity_be_kol_second_detail, mLLContent, true);
        ButterKnife.bind(this);
        mBottomTv.setOnClickListener(this);
        judgeConditions();
    }

    private void judgeConditions() {
        if (getString(R.string.personal_show).endsWith(name)) {
            mCurrentPageType = TYPE_PERSONAL_SHOW;
            updateView(getString(R.string.please_write_personal_show), getString(R.string.please_write_personal_show), getString(R.string.please_write_personal_show),
                    getString(R.string.for_example_address), getString(R.string.for_example_address), getString(R.string.for_example_address), false);
        } else if (getString(R.string.weixin).endsWith(name) || getString(R.string.weibo).endsWith(name) || getString(R.string.qq).endsWith(name)) {
            // 微信、QQ、微博
            mCurrentPageType = TYPE_BIND;
            updateView(getString(R.string.please_write) + name + getString(R.string.friends_number),
                    getString(R.string.please_write) + name + getString(R.string.offer_unit),
                    getString(R.string.please_bind) + name + getString(R.string.account),
                    getString(R.string.for_example) + String.valueOf(1000),
                    getString(R.string.for_example) + String.valueOf(500),
                    getString(R.string.click_open) + name, true);
        } else if (getString(R.string.gongzhonghao).endsWith(name)) {
            mCurrentPageType = TYPE_GONGZHONGHAO;
            updateView(getString(R.string.please_write) + name + getString(R.string.corresponding_wechat),
                    getString(R.string.please_write) + name + getString(R.string.offer_unit),
                    null,
                    getString(R.string.for_example) + "Robin8China",
                    getString(R.string.for_example) + String.valueOf(1000),
                    null, false);
        } else {
            //LogUtil.LogShitou("这是我走的","other"+name);
            mCurrentPageType = TYPE_NORMAL;
            updateView(getString(R.string.please_write) + name + getString(R.string.nickname),
                    getString(R.string.please_write) + name + getString(R.string.offer_unit),
                    null,
                    getString(R.string.for_example) + "Robin8",
                    getString(R.string.for_example) + String.valueOf(1000),
                    null, false);
        }
    }

    private void updateView(String tvTitleStr1, String tvTitleStr2, String tvTitleStr3, String etContentStr1, String etContentStr2, String etContentStr3, boolean isNeedBind) {

        if (mSocialAccountsBean != null) {
            if (mCurrentPageType == TYPE_NORMAL) {
                etContent1.setText(mSocialAccountsBean.getUsername());
            } else if (mCurrentPageType == TYPE_BIND) {
                etContent1.setText(String.valueOf(mSocialAccountsBean.getFollowers_count()));
            } else {
                etContent1.setHint(etContentStr1);
            }
            etContent2.setText(mSocialAccountsBean.getPrice());
        } else {
            etContent1.setHint(etContentStr1);
            etContent2.setHint(etContentStr2);
        }

        tvTitle1.setText(tvTitleStr1);
        tvTitle2.setText(tvTitleStr2);

        if (TextUtils.isEmpty(tvTitleStr3)) {
            tvTitle3.setVisibility(View.GONE);
            etContent3.setVisibility(View.GONE);
        } else {
            tvTitle3.setText(tvTitleStr3);
            etContent3.setHint(etContentStr3);
        }

        if (isNeedBind) {
            rlBind.setVisibility(View.VISIBLE);
            etContent3.setVisibility(View.GONE);
            String username = null;
            if (mSocialAccountsBean != null) {
                username = mSocialAccountsBean.getUsername();
            }
            if (TextUtils.isEmpty(username)) {
                tvName.setText(etContentStr3);
            } else {
                tvName.setText(getString(R.string.has_binded) + name + " " + username);
            }
            IconFontHelper.setTextIconFont(tvArrow, R.string.arrow_right);
            rlBind.setOnClickListener(this);
        }
    }

    private void initData() {
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        Serializable obj = intent.getSerializableExtra("socialAccountsBean");
        if (obj instanceof SocialAccountsBean) {
            mSocialAccountsBean = (SocialAccountsBean) obj;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                submit();
                break;
            case R.id.rl_bind:
                bind();
                break;
        }
    }

    private void bind() {
        BindSocialPresenter presenter = new BindSocialPresenter(this.getApplicationContext(), tvName, name,0);
        presenter.setOnBindListener(new BindSocialPresenter.OnBindListener() {
            @Override
            public void onResponse(String name) {
              //  CustomToast.showLong(BeKolSecondDetailActivity.this,"==bind==>"+name);
                userName = name;
            }
        });
        if (getString(R.string.weixin).equals(name)) {
            presenter.authorize(new Wechat(this));
        } else if (getString(R.string.qq).equals(name)) {
            presenter.authorize(new QQ(this));
        } else if (getString(R.string.weibo).equals(name)) {
            presenter.authorize(new SinaWeibo(this));
        }
    }

    private void submit() {
        Intent intent = getIntent();
        String text = null;
        if (mCurrentPageType == TYPE_PERSONAL_SHOW) {
            StringBuffer sb = new StringBuffer();
            String s1 = etContent1.getText().toString();
            String s2 = etContent2.getText().toString();
            String s3 = etContent3.getText().toString();
            int counts = 0;
            if (!TextUtils.isEmpty(s1)) {
                sb.append(s1).append(",");
                counts++;
            }
            if (!TextUtils.isEmpty(s2)) {
                sb.append(s2).append(",");
                counts++;
            }
            if (!TextUtils.isEmpty(s3)) {
                sb.append(s3).append(",");
                counts++;
            }
            if (counts <= 0) {
                CustomToast.showShort(this, getString(R.string.please_write_personal_show));
                return;
            }
            text = sb.substring(0, sb.length() - 1);
            intent.putExtra("counts", counts);
        } else if (mCurrentPageType == TYPE_BIND) {

            if (TextUtils.isEmpty(etContent1.getText().toString())) {
                CustomToast.showShort(this, getString(R.string.please_write) + name + getString(R.string.friends_number));
                return;
            }
            if (TextUtils.isEmpty(etContent2.getText().toString())) {
                CustomToast.showShort(this, getString(R.string.please_write) + name + getString(R.string.offer_unit));
                return;
            }
            if (TextUtils.isEmpty(tvName.getText().toString()) || !tvName.getText().toString().contains(getString(R.string.has_binded))) {
                CustomToast.showShort(this, getString(R.string.please_bind) + name + getString(R.string.account));
                return;
            }
        } else {
            if (TextUtils.isEmpty(etContent1.getText().toString())) {
                CustomToast.showShort(this, getString(R.string.please_write) + name + getString(R.string.nickname));
                return;
            }
            if (TextUtils.isEmpty(etContent2.getText().toString())) {
                CustomToast.showShort(this, getString(R.string.please_write) + name + getString(R.string.offer_unit));
                return;
            }
        }
        postData(text, intent);
    }

    private void postData(String text, final Intent intent) {
        final String url;
        BasePresenter mBasePresenter = new BasePresenter();
        RequestParams params = new RequestParams();
        if (mCurrentPageType == TYPE_PERSONAL_SHOW) {
            params.put("kol_shows", text);
            url = HelpTools.getUrl(CommonConfig.SUBMIT_APPLY_URL);
        } else {
            params.put("provider_name", name);
            params.put("price", etContent2.getText().toString());

            String username = null;
            if (mCurrentPageType == TYPE_BIND) {
                username = userName;
                params.put("followers_count", etContent1.getText().toString());
                params.put("username", username);
            } else if (mCurrentPageType == TYPE_NORMAL) {
                username = etContent1.getText().toString();
                params.put("username", username);
              //  LogUtil.LogShitou("绑定结果username",username);
            }else if(mCurrentPageType == TYPE_GONGZHONGHAO){
                params.put("uid", etContent1.getText().toString());
            }
            url = HelpTools.getUrl(CommonConfig.UPDATE_SOCIAL_URL);
        }

        mBasePresenter.getDataFromServer(true, HttpRequest.POST, url, params, new RequestCallback() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {

              //  LogUtil.LogShitou("绑定的当前url",url);
               // LogUtil.LogShitou("绑定的详情",response);
                BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);

                if (bean == null) {
                    CustomToast.showShort(BeKolSecondDetailActivity.this, getString(R.string.please_data_wrong));
                    return;
                }

                if (bean.getError() == 0) {
                    if (mCurrentPageType == TYPE_PERSONAL_SHOW) {
                      //  LogUtil.LogShitou("看返回","这是1");
                        setResult(SPConstants.BE_KOL_SECOND_PERSONAL_SHOW, intent);
                    }else {
                      //  LogUtil.LogShitou("看返回","这是2");
                        setResult(SPConstants.BE_KOL_SECOND_ITEM_SOCIAL, intent);
                    }
                   // setResult(SPConstants.BE_KOL_SECOND_PERSONAL_SHOW, intent);
                    NotifyManager.getNotifyManager().notifyChange(NotifyManager.TYPE_REFRESH_PROFILE);
                    finish();
                } else {
                    if (!TextUtils.isEmpty(bean.getDetail())){
                        CustomToast.showShort(BeKolSecondDetailActivity.this, bean.getDetail());
                    }

                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
