package com.robin8.rb.activity;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.extras.sweep.android.CaptureActivity;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.model.BaseBean;
import com.robin8.rb.module.first.activity.LaunchRewordFirstActivity;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.StringUtil;

/**
 * 品牌主
 */
public class ADHostActivity extends BaseActivity {

    private TextView mBrandAmountTv;
    private View mLayoutRechargeInstantly;
    private View mLayoutMyLaunchCampaign;
    private View mLayoutMyAccountMenu;
    private View mLayoutMySweep;
    private TextView mLaunchTv;
    private float mBrandAmountF;
    private BasePresenter mBasePresenter;
    private WProgressDialog mWProgressDialog;

    @Override
    public void setTitleView() {
        mTVCenter.setText(this.getText(R.string.mine_ad_host));
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_ad_host, mLLContent, true);
        mBrandAmountTv = (TextView) view.findViewById(R.id.tv_brand_amount);
        mLaunchTv = (TextView) view.findViewById(R.id.tv_launch);

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
                Log.e("xxfigo", "response" + response);
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);
                if (baseBean != null && baseBean.getError() == 0) {
                    mBrandAmountF = baseBean.getBrand_amount();
                    mBrandAmountTv.setText(StringUtil.deleteZero(mBrandAmountF));
                }
            }
        });
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
        if(isDoubleClick()){
            return;
        }
        switch (v.getId()) {
            case R.id.layout_recharge_instantly:
                skipToRecharge();
                break;
            case R.id.layout_my_launch_campaign:
                shipToMyCampaign();
                break;
            case R.id.layout_my_account_menu:
                skipToBrandBill();
                break;
            case R.id.tv_launch:
                skipToLaunch();
                break;
            case R.id.iv_back:
                executeOnclickLeftView();
                break;
            case R.id.layout_my_sweep:
                skipToSweep();
                break;
        }
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
        if (requestCode == SPConstants.AD_HOST_ACTIVITY) {
            initData();
        }
    }

    @Override
    public void finish() {
        super.finish();
    }
}
