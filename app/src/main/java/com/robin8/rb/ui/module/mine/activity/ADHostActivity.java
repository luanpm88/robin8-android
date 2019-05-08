package com.robin8.rb.ui.module.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.ui.activity.BrandBillActivity;
import com.robin8.rb.ui.activity.MyCampaignActivity;
import com.robin8.rb.ui.activity.RechargeActivity;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.extras.sweep.android.CaptureActivity;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.ui.module.first.activity.LaunchRewordFirstActivity;
import com.robin8.rb.ui.module.mine.model.ADModel;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.ui.widget.CircleImageView;
import com.robin8.rb.ui.dialog.CustomDialogManager;

/**
 * 品牌主
 */
public class ADHostActivity extends BaseActivity {

    private TextView mBrandAmountTv;
    private TextView mBrandPointsTv;
    private View mLayoutRechargeInstantly;
    private View mLayoutMyLaunchCampaign;
    private View mLayoutMyAccountMenu;
    private View mLayoutMySweep;
    private TextView mLaunchTv;
    private TextView mCampanyNameTv;
    private BasePresenter mBasePresenter;
    private WProgressDialog mWProgressDialog;
    public TextView tvApply;
    public TextView mUserTagTv;
    public CircleImageView imgLogo;

    private ADModel baseBean;
    private RelativeLayout llRule;

    @Override
    public void setTitleView() {
        mTVCenter.setText(this.getText(R.string.mine_ad_host));
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_ad_host, mLLContent, true);
        mBrandAmountTv = (TextView) view.findViewById(R.id.tv_brand_amount);
        mBrandPointsTv = (TextView) view.findViewById(R.id.tv_brand_points);
        mCampanyNameTv = (TextView) view.findViewById(R.id.tv_campany_name);
        mUserTagTv = (TextView) view.findViewById(R.id.tv_user_tag);
        imgLogo = ((CircleImageView) view.findViewById(R.id.civ_image));
        mLaunchTv = (TextView) view.findViewById(R.id.tv_launch);
        tvApply = ((TextView) view.findViewById(R.id.tv_apply));
        llRule = ((RelativeLayout) findViewById(R.id.ll_rule));
        llRule.setOnClickListener(this);
        tvApply.setOnClickListener(this);

        mLayoutRechargeInstantly = view.findViewById(R.id.layout_recharge_instantly);
        ImageView ivLRI = (ImageView) mLayoutRechargeInstantly.findViewById(R.id.iv_item);
        TextView tvLRI = (TextView) mLayoutRechargeInstantly.findViewById(R.id.tv_item_title);
        tvLRI.setText(R.string.recharge_instantly);
        ivLRI.setBackgroundResource(R.mipmap.icon_ark_money);

        mLayoutMyLaunchCampaign = view.findViewById(R.id.layout_my_launch_campaign);
        ImageView ivLMC = (ImageView) mLayoutMyLaunchCampaign.findViewById(R.id.iv_item);
        TextView tvLMC = (TextView) mLayoutMyLaunchCampaign.findViewById(R.id.tv_item_title);
        tvLMC.setText(R.string.my_launch_activity);
        ivLMC.setBackgroundResource(R.mipmap.icon_ark_list);

        mLayoutMyAccountMenu = view.findViewById(R.id.layout_my_account_menu);
        ImageView ivLMM = (ImageView) mLayoutMyAccountMenu.findViewById(R.id.iv_item);
        TextView tvLMM = (TextView) mLayoutMyAccountMenu.findViewById(R.id.tv_item_title);
        tvLMM.setText(R.string.my_menu);
        ivLMM.setBackgroundResource(R.mipmap.icon_ark_account);

        mLayoutMySweep = view.findViewById(R.id.layout_my_sweep);
        ImageView ivLMS = (ImageView) mLayoutMySweep.findViewById(R.id.iv_item);
        TextView tvLMS = (TextView) mLayoutMySweep.findViewById(R.id.tv_item_title);
        tvLMS.setText(R.string.sweep_qr_code);
        ivLMS.setBackgroundResource(R.mipmap.icon_ark_add);

        mLaunchTv.setOnClickListener(this);
        mLayoutRechargeInstantly.setOnClickListener(this);
        mLayoutMyLaunchCampaign.setOnClickListener(this);
        mLayoutMyAccountMenu.setOnClickListener(this);
        mLayoutMySweep.setOnClickListener(this);
        mIVBack.setOnClickListener(this);
        initData();
    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.ADVERTISER_MY;
        initData();
        super.onResume();
    }

    private void initData() {
        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }

        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }

        mWProgressDialog.show();

        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.KOL_BRAND_AMOUNT_URL), null, new RequestCallback() {

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
                baseBean = GsonTools.jsonToBean(response, ADModel.class);
                if (baseBean != null && baseBean.getError() == 0) {
                    mBrandAmountTv.setText(StringUtil.deleteZero(baseBean.getBrand_amountX()));
                    mBrandPointsTv.setText(StringUtil.deleteZero(baseBean.getBrand_credit()));
                    if (!TextUtils.isEmpty(baseBean.getAvatar_url())) {
                        BitmapUtil.loadImage(ADHostActivity.this, baseBean.getAvatar_url(), imgLogo);
                    }
                    if (TextUtils.isEmpty(baseBean.getName())) {
                        mCampanyNameTv.setText(R.string.robin350);
                    } else {
                        mCampanyNameTv.setText(baseBean.getName());
                    }
                    if (TextUtils.isEmpty(baseBean.getCampany_name())) {
                        mUserTagTv.setText(R.string.robin351);
                    } else {
                        mUserTagTv.setText(baseBean.getCampany_name());
                    }
                }
            }
        });
    }

    private boolean isJump() {
        if (TextUtils.isEmpty(baseBean.getCampany_name()) || TextUtils.isEmpty(baseBean.getName())) {
            return false;
        } else {
            return true;
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
        if (isDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.layout_recharge_instantly:
                if (isJump() == false) {
                    showMyDialog();
                } else {
                    skipToRecharge();
                }
                break;
            case R.id.layout_my_launch_campaign:
                //我发布的活动
                if (isJump() == false) {
                    showMyDialog();
                } else {
                    shipToMyCampaign();
                }
                break;
            case R.id.layout_my_account_menu:
                if (isJump() == false) {
                    showMyDialog();
                } else {
                    skipToBrandBill();
                }
                break;
            case R.id.tv_launch:
                if (isJump() == false) {
                    showMyDialog();
                } else {
                    skipToLaunch();
                }
                break;
            case R.id.iv_back:
                executeOnclickLeftView();
                break;
            case R.id.layout_my_sweep:
                if (isJump() == false) {
                    showMyDialog();
                } else {
                    skipToSweep();
                }
                break;
            case R.id.tv_apply:
                Intent intent = new Intent(ADHostActivity.this, ADHostMsgActivity.class);
                intent.putExtra("bean", baseBean);
                startActivityForResult(intent, SPConstants.UPDATA_BRAND_MSG);
                break;
            case R.id.ll_rule:
                showRuleDialog(ADHostActivity.this);
                break;
        }
    }

    private void showRuleDialog(final Activity activity) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_ad_rule, null);
        TextView tvRule = (TextView) view.findViewById(R.id.tv_rule);
        TextView tvData = (TextView) view.findViewById(R.id.tv_data);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout_bg);
        if (baseBean != null) {
            tvRule.setText(getString(R.string.robin260, baseBean.getBrand_credit(), baseBean.getBrand_credit() / 10));
            int length = String.valueOf(baseBean.getBrand_credit()).length();
            int lengthTwo = String.valueOf(baseBean.getBrand_credit() / 10).length();
            StringUtil.setTextViewSpan(tvRule, 0, 5, 5 + length, getResources().getColor(R.color.blue_custom));
            StringUtil.setTextViewSpan(tvRule, 0, 14 + length, 14 + length + lengthTwo, getResources().getColor(R.color.blue_custom));
            if (baseBean.getBrand_credit() == 0) {
                tvData.setVisibility(View.GONE);
            } else {
                tvData.setVisibility(View.VISIBLE);
                tvData.setText(getString(R.string.robin261,baseBean.getBrand_credit_expired_at()));
            }
        } else {
            tvRule.setText(R.string.robin263);
            tvData.setVisibility(View.GONE);
        }

        final CustomDialogManager cdm = new CustomDialogManager(activity, view);
        layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cdm.dismiss();
            }
        });

        cdm.dg.setCanceledOnTouchOutside(true);
        cdm.dg.getWindow().setGravity(Gravity.CENTER);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        cdm.showDialog();
    }

    private void showMyDialog() {
        View view = LayoutInflater.from(ADHostActivity.this).inflate(R.layout.dialog_ad_host, null);
        TextView leftTv = (TextView) view.findViewById(R.id.tv_left);
        TextView infoTv = (TextView) view.findViewById(R.id.tv_info);
        TextView rightTv = (TextView) view.findViewById(R.id.tv_right);
        infoTv.setText(R.string.robin264);
        leftTv.setText(R.string.cancel);
        rightTv.setText(R.string.robin265);
        infoTv.setGravity(Gravity.CENTER);
        final CustomDialogManager cdm = new CustomDialogManager(ADHostActivity.this, view);
        leftTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cdm.dismiss();
            }
        });
        rightTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cdm.dismiss();
                Intent intent = new Intent(ADHostActivity.this, ADHostMsgActivity.class);
                intent.putExtra("bean", baseBean);
                startActivityForResult(intent, SPConstants.UPDATA_BRAND_MSG);
            }
        });
        cdm.dg.setCanceledOnTouchOutside(true);
        cdm.dg.getWindow().setGravity(Gravity.CENTER);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        cdm.showDialog();
    }

    private void skipToSweep() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void shipToMyCampaign() {
        Intent intent = new Intent(this, MyCampaignActivity.class);
        startActivityForResult(intent, SPConstants.AD_HOST_ACTIVITY);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void skipToBrandBill() {
        Intent intent = new Intent(this, BrandBillActivity.class);
        intent.putExtra("url", HelpTools.getUrl(CommonConfig.KOL_BRAND_BILL_URL));
        intent.putExtra("title", getString(R.string.my_menu));
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * 发布悬赏活动
     */
    private void skipToLaunch() {
        Intent intent = new Intent(this, LaunchRewordFirstActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * 立即充值
     */
    private void skipToRecharge() {
        Intent intent = new Intent(ADHostActivity.this, RechargeActivity.class);
        startActivityForResult(intent, SPConstants.AD_HOST_ACTIVITY);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == SPConstants.AD_HOST_ACTIVITY) {
//            initData();
//        } else if (requestCode == SPConstants.UPDATA_BRAND_MSG) {
//            initData();
//        }else if (requestCode == SPConstants.PAY_SUCCESS_ACTIVITY){
//            initData();
//        }
    }

    @Override
    public void finish() {
        super.finish();
    }
}
