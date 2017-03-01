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
import com.robin8.rb.activity.MyCampaignActivity;
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
    private ImageView mMessageIv;
    private TextView mTotalIncomeTv;
    private FirstPagerPresenter mFirstPagerPresenter;
    private TextView mSignInTv;
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
        Button beKOLBtn = (Button) mNotKolView.findViewById(R.id.btn_be_kol);
        beKOLBtn.setOnClickListener(this);
    }

    private void initIsKolView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity.getApplicationContext());
        mIsKolView = layoutInflater.inflate(R.layout.pager_home_is_kol,mLLContent, true);
        mIsKolView.findViewById(R.id.ll_message).setOnClickListener(this);
        mIsKolView.findViewById(R.id.ll_indiana).setOnClickListener(this);
        mIsKolView.findViewById(R.id.ll_total_income).setOnClickListener(this);
        mIsKolView.findViewById(R.id.ll_sign_in).setOnClickListener(this);
        mIsKolView.findViewById(R.id.ll_ongoing_campaigns).setOnClickListener(this);
        mIsKolView.findViewById(R.id.ll_completed_campaigns).setOnClickListener(this);
        mIsKolView.findViewById(R.id.ll_share_campaigns).setOnClickListener(this);
        mIsKolView.findViewById(R.id.ll_ongoing_product_share).setOnClickListener(this);
        mIsKolView.findViewById(R.id.ll_completed_product_share).setOnClickListener(this);
        mIsKolView.findViewById(R.id.ll_share_product).setOnClickListener(this);
        mIsKolView.findViewById(R.id.ll_ongoing_invite).setOnClickListener(this);
        mIsKolView.findViewById(R.id.ll_completed_invite).setOnClickListener(this);
        mIsKolView.findViewById(R.id.ll_share_invite).setOnClickListener(this);

        mMessageIv = (ImageView) mIsKolView.findViewById(R.id.iv_message);
        mTotalIncomeTv = (TextView) mIsKolView.findViewById(R.id.tv_total_income);
        mSignInTv = (TextView) mIsKolView.findViewById(R.id.tv_sign_in);

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
        if (!baseApplication.hasLogined() || loginBean.getKol().getRole_apply_status() != STATE_PASSED) {
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
            case R.id.btn_be_kol:
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
            case R.id.ll_message:
                {
                    Intent intent = new Intent();
                    intent.setClass(mActivity, BaseRecyclerViewActivity.class);
                    intent.putExtra("destination", SPConstants.MESSAGE_ACTIVITY);
                    intent.putExtra("url", HelpTools.getUrl(CommonConfig.MESSAGES_URL));
                    intent.putExtra("title", mActivity.getString(R.string.message));
                    startActivity(intent);
                }
                break;
            case R.id.ll_indiana:
            {
                Intent intent = new Intent(mActivity, BaseRecyclerViewActivity.class);
                intent.putExtra("destination", SPConstants.INDIANA_ROBIN);
                intent.putExtra("url", HelpTools.getUrl(CommonConfig.LOTTERY_ACTIVITIES_URL));
                intent.putExtra("title", mActivity.getString(R.string.robin_indiana));
                startActivity(intent);
            }
            break;
            case R.id.ll_total_income:
                startActivity(WalletActivity.class);
                break;
            case R.id.ll_sign_in:
                startActivity(UserSignActivity.class);
                break;
            case R.id.ll_ongoing_campaigns:
                startActivity(MyCampaignActivity.class);
                break;
            case R.id.ll_completed_campaigns:
                startActivity(MyCampaignActivity.class);
                break;
            case R.id.ll_share_campaigns:
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(1);
                }
                break;
            case R.id.ll_ongoing_product_share:
                startProductShareActivity();
                break;
            case R.id.ll_completed_product_share:
                startProductShareActivity();
                break;
            case R.id.ll_share_product:
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(2);
                }
                break;
            case R.id.ll_ongoing_invite:
                startActivity(InviteFriendsActivity.class);
                break;
            case R.id.ll_completed_invite:
                startActivity(InviteFriendsActivity.class);
                break;
            case R.id.ll_share_invite:
                startActivity(InviteFriendsActivity.class);
                break;
        }
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
        mTotalIncomeTv.setText(totalIncome);
    }

    @Override
    public void showUnreadMessage(int unReadMessages) {
        if (unReadMessages > 0) {
            mMessageIv.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.icon_first_pager_unread_message));
        } else {
            mMessageIv.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.icon_first_pager_message));
        }
    }

    @Override
    public void setSignInData(String continuousCheckInCount, boolean hadCheckedInToday) {
        mSignInTv.setText("×" + continuousCheckInCount);
        if (hadCheckedInToday) {
            mCheckSignInIv.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.icon_checked));
        } else {
            mCheckSignInIv.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.icon_unchecked));
        }
    }

    @Override
    public void setCampaignData(FirstPagerModel.Campaigns campaignData) {
        mOngoingCampaignsTv.setText(campaignData.getRunningCount());
        mCompletedCampaignsTv.setText(campaignData.getCompletedCount());
        mCompletedCampaignsIncomeTv.setText("¥" + campaignData.getIncome());
        if (campaignData.isHadSharedToday()) {
            mCheckShareCampaignsIv.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.icon_checked));
        } else {
            mCheckShareCampaignsIv.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.icon_unchecked));
        }
    }

    @Override
    public void setProductData(FirstPagerModel.Product productData) {
        mOngoingProductsTv.setText(productData.getRunningCount());
        mCompletedProductsTv.setText(productData.getSoldCount());
        mCompletedProductsIncomeTv.setText("¥" + productData.getIncome());
        if (productData.isHadSharedToday()) {
            mCheckShareProductsIv.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.icon_checked));
        } else {
            mCheckShareProductsIv.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.icon_unchecked));
        }
    }

    @Override
    public void setInviteData(FirstPagerModel.Invite inviteData) {
        mOngoingInviteTv.setText(inviteData.getRunningCount());
        mCompletedInviteTv.setText(inviteData.getCompletedCount());
        mCompletedInviteIncomeTv.setText("¥" + inviteData.getIncome());
    }


}
