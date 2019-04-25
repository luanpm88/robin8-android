package com.robin8.rb.ui.activity.uesr_msg;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.ui.model.UserShowBean;
import com.robin8.rb.util.GsonTools;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 开通bigV */
public class UserSelectActivity extends BaseActivity {

    @Bind(R.id.tv_weixin)
    TextView tvWeixin;
    @Bind(R.id.tv_weibo)
    TextView tvWeibo;
    public static final String SELECTPLAT = "UserSelect";
    @Bind(R.id.tv_icon_weibo)
    TextView tvIconWeibo;
    @Bind(R.id.tv_icon_wechat)
    TextView tvIconWechat;
    @Bind(R.id.layout_wechat)
    LinearLayout layoutWechat;
    @Bind(R.id.layout_weibo)
    LinearLayout layoutWeibo;
    @Bind(R.id.img_go_weibo)
    ImageView imgGoWeibo;
    @Bind(R.id.img_go_wechat)
    ImageView imgGoWechat;
    private boolean haveWechat = false;
    private boolean haveWebo = false;
    private String initInfo;
    private String initInfoRegister;
    private String fromRegister;
    private boolean isClose = false;
    private boolean isUpdata = false;

    @Override
    public void setTitleView() {
        mTVCenter.setText("选择平台");
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_user_select, mLLContent, true);
        ButterKnife.bind(this);
        layoutWechat.setOnClickListener(this);
        layoutWeibo.setOnClickListener(this);
        initInfo = getIntent().getStringExtra(SELECTPLAT);
        fromRegister = getIntent().getStringExtra("register");
        if (initInfo != null) {
            initData(initInfo);
        } else {
            tvWeibo.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.mipmap.login_weibo), null, null, null);
            tvWeibo.setTextColor(getResources().getColor(R.color.gray_first));
            tvWeibo.setText("请填写微博报价");
            tvIconWeibo.setVisibility(View.GONE);
            imgGoWechat.setVisibility(View.VISIBLE);

            tvWeixin.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.mipmap.social_weixin_on), null, null, null);
            tvWeixin.setTextColor(getResources().getColor(R.color.gray_first));
            tvWeixin.setText("请填写微信报价");
            tvIconWechat.setVisibility(View.GONE);
            imgGoWeibo.setVisibility(View.VISIBLE);
        }

        if (fromRegister != null) {
            if (fromRegister.equals(UserBaseMsgActivity.BASEMSG)) {
                isClose = true;
                initInfoRegister = getIntent().getStringExtra("register_data");
                if (! TextUtils.isEmpty(initInfoRegister)) {
                    initData(initInfoRegister);
                } else {
                    tvWeibo.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.mipmap.login_weibo), null, null, null);
                    tvWeibo.setTextColor(getResources().getColor(R.color.gray_first));
                    tvWeibo.setText("请填写微博报价");
                    tvIconWeibo.setVisibility(View.GONE);
                    imgGoWechat.setVisibility(View.VISIBLE);

                    tvWeixin.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.mipmap.social_weixin_on), null, null, null);
                    tvWeixin.setTextColor(getResources().getColor(R.color.gray_first));
                    tvWeixin.setText("请填写微信报价");
                    tvIconWechat.setVisibility(View.GONE);
                    imgGoWeibo.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void initData(String initInfo) {
        UserShowBean userShowBean = GsonTools.jsonToBean(initInfo, UserShowBean.class);
        if (userShowBean.getError() == 0) {
            UserShowBean.KolBean.PublicWechatAccountBean public_wechat_account = userShowBean.getKol().getPublic_wechat_account();
            UserShowBean.KolBean.WeiboAccountBean weibo_account = userShowBean.getKol().getWeibo_account();
            if (public_wechat_account != null) {
                haveWechat = true;
                if (public_wechat_account.getStatus() == 0) {
                    //未审核
                    tvWeixin.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.mipmap.social_weixin), null, null, null);
                    tvWeixin.setTextColor(getResources().getColor(R.color.gray_first));
                    tvWeixin.setText(public_wechat_account.getNickname());
                    tvIconWechat.setVisibility(View.VISIBLE);
                    tvIconWechat.setText("审核中");
                    tvIconWechat.setTextColor(getResources().getColor(R.color.blue_2dcad0));
                    imgGoWechat.setVisibility(View.GONE);
                } else if (public_wechat_account.getStatus() == 1) {
                    //审核通过
                    tvWeixin.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.mipmap.social_weixin_on), null, null, null);
                    tvWeixin.setTextColor(getResources().getColor(R.color.black_000000));
                    tvWeixin.setText(public_wechat_account.getNickname());
                    tvIconWechat.setVisibility(View.GONE);
                    imgGoWechat.setVisibility(View.VISIBLE);
                } else {
                    //审核拒绝
                    tvWeixin.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.mipmap.social_weixin), null, null, null);
                    tvWeixin.setTextColor(getResources().getColor(R.color.red_custom));
                    tvWeixin.setText("审核未通过,请联系客服");
                    tvIconWechat.setVisibility(View.GONE);
                    imgGoWechat.setVisibility(View.VISIBLE);
                }
            } else {
                //未开通
                tvWeixin.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.mipmap.social_weixin_on), null, null, null);
                tvWeixin.setTextColor(getResources().getColor(R.color.gray_first));
                tvWeixin.setText("请填写微信报价");
                tvIconWechat.setVisibility(View.GONE);
                imgGoWechat.setVisibility(View.VISIBLE);
            }
            if (weibo_account != null) {
                haveWebo = true;
                if (weibo_account.getStatus() == 0) {
                    //未审核
                    tvWeibo.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.mipmap.social_weibo), null, null, null);
                    tvWeibo.setTextColor(getResources().getColor(R.color.gray_first));
                    tvWeibo.setText(weibo_account.getNickname());
                    tvIconWeibo.setText("审核中");
                    tvIconWeibo.setTextColor(getResources().getColor(R.color.blue_2dcad0));
                    tvIconWeibo.setVisibility(View.VISIBLE);
                    imgGoWeibo.setVisibility(View.GONE);
                } else if (weibo_account.getStatus() == 1) {
                    //审核通过
                    tvWeibo.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.mipmap.social_weibo_on), null, null, null);
                    tvWeibo.setTextColor(getResources().getColor(R.color.black_000000));
                    tvWeibo.setText(weibo_account.getNickname());
                    tvIconWeibo.setVisibility(View.GONE);
                    imgGoWeibo.setVisibility(View.VISIBLE);
                } else {
                    //审核拒绝
                    tvWeibo.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.mipmap.social_weibo), null, null, null);
                    tvWeibo.setTextColor(getResources().getColor(R.color.red_custom));
                    tvWeibo.setText("审核未通过,请联系客服");
                    tvIconWeibo.setVisibility(View.GONE);
                    imgGoWeibo.setVisibility(View.VISIBLE);
                }
            } else {
                tvWeibo.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.mipmap.login_weibo), null, null, null);
                tvWeibo.setTextColor(getResources().getColor(R.color.gray_first));
                tvWeibo.setText("请填写微博报价");
                tvIconWeibo.setVisibility(View.GONE);
                imgGoWeibo.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.layout_wechat:
                Intent intent = new Intent(this, WechatMsgActivity.class);
                if (haveWechat == true) {
                    intent.putExtra(WechatMsgActivity.WECHAT, initInfo);
                }
                if (isClose == true) {
                    if (! TextUtils.isEmpty(initInfoRegister)) {
                        intent.putExtra(WechatMsgActivity.WECHAT, initInfoRegister);
                    }
                    startActivityForResult(intent, SPConstants.CLOSE);
                } else {
                    startActivityForResult(intent, SPConstants.UPDATA_PLAT_DATA);

                }
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                break;
            case R.id.layout_weibo:
                Intent intentWeibo = new Intent(this, WeiBoMsgActivity.class);
                if (haveWebo == true) {
                    intentWeibo.putExtra(WeiBoMsgActivity.WEIBOMSG, initInfo);
                }
                if (isClose == true) {
                    if (! TextUtils.isEmpty(initInfoRegister)) {
                        intentWeibo.putExtra(WeiBoMsgActivity.WEIBOMSG, initInfoRegister);
                    }
                    startActivityForResult(intentWeibo, SPConstants.CLOSE);
                } else {
                    startActivityForResult(intentWeibo, SPConstants.UPDATA_PLAT_DATA);

                }
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPConstants.UPDATA_PLAT_DATA) {
            if (resultCode == RESULT_OK) {
                String updata = data.getStringExtra("updata");
                initInfo = updata;
                initData(updata);
            }
        } else if (requestCode == SPConstants.CLOSE) {
            isUpdata = false;
            if (resultCode == RESULT_OK) {
                String updata = data.getStringExtra("updata");
                initInfo = updata;
                initData(updata);
                if (! TextUtils.isEmpty(updata)) {
                    Intent intent = new Intent(this, UserBaseMsgActivity.class);
                    intent.putExtra("data", updata);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Intent intent = new Intent(this, UserBaseMsgActivity.class);
                    setResult(RESULT_CANCELED, intent);
                    finish();
                }
            } else {
                Intent intent = new Intent(this, UserBaseMsgActivity.class);
                setResult(RESULT_CANCELED, intent);
                finish();
            }

        }
    }

    @Override
    protected void executeOnclickLeftView() {
        if (! TextUtils.isEmpty(initInfo)) {
            if (isUpdata = true) {
                Intent intent = new Intent(UserSelectActivity.this, UserInformationActivity.class);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                finish();
            }
        } else {
            finish();
        }

    }

    @Override
    protected void executeOnclickRightView() {

    }
}
