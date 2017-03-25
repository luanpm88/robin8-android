package com.robin8.rb.pager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.activity.LoginActivity;
import com.robin8.rb.activity.WalletActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.BasePager;
import com.robin8.rb.base.BaseRecyclerViewActivity;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.model.LoginBean;
import com.robin8.rb.module.create.activity.FragmentsActivity;
import com.robin8.rb.module.first.model.FirstPagerModel;
import com.robin8.rb.module.first.prenster.FirstPagerPresenter;
import com.robin8.rb.module.first.view.IFirstPagerView;
import com.robin8.rb.module.mine.activity.BeKolFirstActivity;
import com.robin8.rb.module.mine.activity.InviteFriendsActivity;
import com.robin8.rb.module.mine.activity.UserSignActivity;
import com.robin8.rb.util.HelpTools;

/**
 * @author Figo
 */
public class FirstPager extends BasePager implements View.OnClickListener, IFirstPagerView{

    private static final String STATE_PENDING = "pending";  //pending,applying,passed,rejected
    private static final String STATE_APPLYING = "applying";    //pending,applying,passed,rejected
    private static final String STATE_PASSED = "passed";    //pending,applying,passed,rejected
    private static final String STATE_REJECTED = "rejected";    //pending,applying,passed,rejected
    private ViewPager mViewPager;
    private View mNotKolView;
    private View mIsKolView;
    private TextView mTotalIncomeTv;
    private FirstPagerPresenter mFirstPagerPresenter;
    private TextView mCompletedInviteTv;
    private TextView mCompletedInviteIncomeTv;
    private ImageView mCheckSignInIv;
    private TextView mOngoingCampaignsTv;
    private TextView mCompletedCampaignsTv;
    private TextView mCompletedCampaignsIncomeTv;
    private TextView mOngoingProductsTv;
    private TextView mCompletedProductsTv;
    private TextView mCompletedProductsIncomeTv;
    private TextView mOngoingInviteTv;
    private ImageView mCheckShareCampaignsIv;
    private ImageView mCheckShareProductsIv;

    public FirstPager(FragmentActivity activity, ViewPager viewPager) {
        super(activity);
        this.mActivity = activity;
        this.mViewPager = viewPager;
    }

    @Override
    public View initView() {
        initNotKolView();
        initIsKolView();
        FrameLayout frameLayout = new FrameLayout(mActivity);
        frameLayout.addView(mNotKolView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        frameLayout.addView(mIsKolView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        changeVisibleView();
        return frameLayout;
    }

    @Override
    public void initData() {
        if (mFirstPagerPresenter == null) {
            mFirstPagerPresenter = new FirstPagerPresenter(this);
        }
        mFirstPagerPresenter.loadData();
    }

    private void initNotKolView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity.getApplicationContext());
        mNotKolView = layoutInflater.inflate(R.layout.pager_home_not_kol, mLLContent, true);
        TextView beKOLBtn = (TextView) mNotKolView.findViewById(R.id.tv_be_kol);
        beKOLBtn.setOnClickListener(this);
    }

    private void initIsKolView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity.getApplicationContext());
        mIsKolView = layoutInflater.inflate(R.layout.pager_home_is_kol,mLLContent, true);
        mIsKolView.findViewById(R.id.rl_indiana).setOnClickListener(this);
        mIsKolView.findViewById(R.id.rl_total_income).setOnClickListener(this);
        mIsKolView.findViewById(R.id.rl_sign_in).setOnClickListener(this);
        mIsKolView.findViewById(R.id.rl_ongoing_campaigns).setOnClickListener(this);
        mIsKolView.findViewById(R.id.rl_completed_campaigns).setOnClickListener(this);
        mIsKolView.findViewById(R.id.rl_share_campaigns).setOnClickListener(this);
        mIsKolView.findViewById(R.id.rl_ongoing_product_share).setOnClickListener(this);
        mIsKolView.findViewById(R.id.rl_completed_product_share).setOnClickListener(this);
        mIsKolView.findViewById(R.id.rl_share_product).setOnClickListener(this);
        mIsKolView.findViewById(R.id.rl_ongoing_invite).setOnClickListener(this);
        mIsKolView.findViewById(R.id.rl_completed_invite).setOnClickListener(this);
        mIsKolView.findViewById(R.id.rl_share_invite).setOnClickListener(this);


        mTotalIncomeTv = (TextView) mIsKolView.findViewById(R.id.tv_total_income);

        mOngoingCampaignsTv = (TextView) mIsKolView.findViewById(R.id.tv_ongoing_campaigns);
        mCompletedCampaignsTv = (TextView) mIsKolView.findViewById(R.id.tv_completed_campaigns);
        mCompletedCampaignsIncomeTv = (TextView) mIsKolView.findViewById(R.id.tv_completed_campaigns_income);

        mOngoingProductsTv = (TextView) mIsKolView.findViewById(R.id.tv_ongoing_products);
        mCompletedProductsTv = (TextView) mIsKolView.findViewById(R.id.tv_completed_products);
        mCompletedProductsIncomeTv = (TextView) mIsKolView.findViewById(R.id.tv_completed_products_income);

        mOngoingInviteTv = (TextView) mIsKolView.findViewById(R.id.tv_ongoing_invite);
        mCompletedInviteTv = (TextView) mIsKolView.findViewById(R.id.tv_completed_invite);
        mCompletedInviteIncomeTv = (TextView) mIsKolView.findViewById(R.id.tv_completed_invite_income);

        mCheckSignInIv = (ImageView) mIsKolView.findViewById(R.id.iv_check_sign_in);
        mCheckShareCampaignsIv = (ImageView) mIsKolView.findViewById(R.id.iv_check_share_campaigns);
        mCheckShareProductsIv = (ImageView) mIsKolView.findViewById(R.id.iv_check_share_products);

    }

    /**
     * 判断身份信息,显示不同的view
     */
    public void changeVisibleView() {
        LoginBean loginBean = BaseApplication.getInstance().getLoginBean();
        BaseApplication baseApplication = BaseApplication.getInstance();
        if (!baseApplication.hasLogined() || !STATE_PASSED.equals(loginBean.getKol().getRole_apply_status())) {
            // 未登录和不是KOL
            mNotKolView.setVisibility(View.VISIBLE);
            mIsKolView.setVisibility(View.GONE);
        } else {
            mNotKolView.setVisibility(View.GONE);
            mIsKolView.setVisibility(View.VISIBLE);
            if (mFirstPagerPresenter != null) {
                mFirstPagerPresenter.loadData();
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (isDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_be_kol:
                // 申请成为KOL
                if (BaseApplication.getInstance().hasLogined()) {
                    startActivity(BeKolFirstActivity.class);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(mActivity, LoginActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("from", SPConstants.MAINACTIVITY);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case 1111111:
                {
                    Intent intent = new Intent();
                    intent.setClass(mActivity, BaseRecyclerViewActivity.class);
                    intent.putExtra("destination", SPConstants.MESSAGE_ACTIVITY);
                    intent.putExtra("url", HelpTools.getUrl(CommonConfig.MESSAGES_URL));
                    intent.putExtra("title", mActivity.getString(R.string.message));
                    startActivity(intent);
                }
                break;
            case R.id.rl_indiana:
            {
                Intent intent = new Intent(mActivity, BaseRecyclerViewActivity.class);
                intent.putExtra("destination", SPConstants.INDIANA_ROBIN);
                intent.putExtra("url", HelpTools.getUrl(CommonConfig.LOTTERY_ACTIVITIES_URL));
                intent.putExtra("title", mActivity.getString(R.string.robin_indiana));
                startActivity(intent);
            }
            break;
            case R.id.rl_total_income:
                startActivity(WalletActivity.class);
                break;
            case R.id.rl_sign_in:
                startActivity(UserSignActivity.class);
                break;
            case R.id.rl_ongoing_campaigns:
                startCampaignsActivity();
                break;
            case R.id.rl_completed_campaigns:
                startCampaignsActivity();
                break;
            case R.id.rl_share_campaigns:
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(1);
                }
                break;
            case R.id.rl_ongoing_product_share:
                startProductShareActivity();
                break;
            case R.id.rl_completed_product_share:
                startProductShareActivity();
                break;
            case R.id.rl_share_product:
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(2);
                }
                break;
            case R.id.rl_ongoing_invite:
                startActivity(InviteFriendsActivity.class);
                break;
            case R.id.rl_completed_invite:
                startActivity(InviteFriendsActivity.class);
                break;
            case R.id.rl_share_invite:
                startActivity(InviteFriendsActivity.class);
                break;
        }
    }

    private void startCampaignsActivity() {
        String nameArr[] = {"进行中", "待上传", "审核中", "已完成"};
        String campaignTypeArr[] = {"approved", "waiting_upload", "verifying", "completed"};
        Intent intent = new Intent(mActivity, FragmentsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArray("name",nameArr);
        bundle.putStringArray("type",campaignTypeArr);
        bundle.putString("page_name", StatisticsAgency.MY_TASK);
        bundle.putString("title_name", mActivity.getString(R.string.my_capaign));
        bundle.putString("url",HelpTools.getUrl(CommonConfig.CAMPAIGN_INVITES_URL));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void startProductShareActivity() {
        Intent intent = new Intent();
        intent.setClass(mActivity, FragmentsActivity.class);
        String nameArr[] = {"我的分享", "我的产品", "待审核", "审核拒绝"};//待审核、审核通过、审核拒绝, 我的分享
        String campaignTypeArr[] = {"shares", "passed", "pending", "rejected"};//'pending' , 'passed','rejected', 'shares'
        Bundle bundle = new Bundle();
        bundle.putStringArray("name", nameArr);
        bundle.putStringArray("type", campaignTypeArr);
        bundle.putString("page_name", StatisticsAgency.MY_CREATE);
        bundle.putString("title_name", mActivity.getString(R.string.my_create));
        bundle.putString("url", HelpTools.getUrl(CommonConfig.MY_CREATE_URL));
        intent.putExtras(bundle);
        intent.setClass(mActivity, FragmentsActivity.class);
        startActivity(intent);
    }

    private void startActivity(Class activityClass) {
        if (mActivity != null) {
            Intent intent = new Intent();
            intent.setClass(mActivity, activityClass);
            mActivity.startActivity(intent);
        }
    }

    private void startActivity(Intent intent) {
        if (mActivity != null && intent != null) {
            mActivity.startActivity(intent);
        }
    }

    @Override
    public void setTotalIncome(String totalIncome) {
        try {
//            if (Float.parseFloat(totalIncome) >= 1000.0f) {
//                mTotalIncomeTv.setTextSize(20);
//            } else {
//                mTotalIncomeTv.setTextSize(30);
//            }
            mTotalIncomeTv.setText(totalIncome);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showUnreadMessage(int unReadMessages) {

    }

    @Override
    public void setSignInData(String continuousCheckInCount, boolean isCheckedInToday) {
        if (isCheckedInToday) {
            mCheckSignInIv.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.pic_checkin));
        } else {
            mCheckSignInIv.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.pic_uncheckin));
        }
    }

    @Override
    public void setCampaignData(FirstPagerModel.Campaigns campaignData) {
        mOngoingCampaignsTv.setText(campaignData.getRunningCount());
        mCompletedCampaignsTv.setText(campaignData.getCompletedCount());
        mCompletedCampaignsIncomeTv.setText("¥" + campaignData.getIncome());
        if (campaignData.isSharedToday()) {
            mCheckShareCampaignsIv.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.pic_shared_campaign));
        } else {
            mCheckShareCampaignsIv.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.pic_unshared_campaign));
        }
    }

    @Override
    public void setProductData(FirstPagerModel.Product productData) {
        mOngoingProductsTv.setText(productData.getRunningCount());
        mCompletedProductsTv.setText(productData.getSoldCount());
        mCompletedProductsIncomeTv.setText("¥" + productData.getIncome());

        if (productData.isSharedToday()) {
            mCheckShareProductsIv.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.pic_shared_product));
        } else {
            mCheckShareProductsIv.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.pic_unshared_product));
        }
    }

    @Override
    public void setInviteData(FirstPagerModel.Invite inviteData) {
        mOngoingInviteTv.setText(inviteData.getRunningCount());
        mCompletedInviteTv.setText(inviteData.getCompletedCount());
        mCompletedInviteIncomeTv.setText("¥" + inviteData.getIncome());
    }


}
