package com.robin8.rb.pager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

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
import com.robin8.rb.module.mine.activity.BeKolFirstActivity;
import com.robin8.rb.module.mine.activity.InviteFriendsActivity;
import com.robin8.rb.module.mine.activity.UserSignActivity;
import com.robin8.rb.util.HelpTools;

/**
 * @author Figo
 */
public class FirstPager extends BasePager implements View.OnClickListener{

    private static final String STATE_PENDING = "pending";  //pending,applying,passed,rejected
    private static final String STATE_APPLYING = "applying";    //pending,applying,passed,rejected
    private static final String STATE_PASSED = "passed";    //pending,applying,passed,rejected
    private static final String STATE_REJECTED = "rejected";    //pending,applying,passed,rejected
    private ViewPager mViewPager;
    private View mNotKolView;
    private View mIsKolView;

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
        super.initData();
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
        mIsKolView.findViewById(R.id.iv_message).setOnClickListener(this);
        mIsKolView.findViewById(R.id.ll_indiana).setOnClickListener(this);
        mIsKolView.findViewById(R.id.ll_total_money).setOnClickListener(this);
        mIsKolView.findViewById(R.id.ll_sign_in).setOnClickListener(this);
        mIsKolView.findViewById(R.id.ll_ongoing_activities).setOnClickListener(this);
        mIsKolView.findViewById(R.id.ll_completed_activities).setOnClickListener(this);
        mIsKolView.findViewById(R.id.ll_share_activities).setOnClickListener(this);
        mIsKolView.findViewById(R.id.ll_ongoing_product_share).setOnClickListener(this);
        mIsKolView.findViewById(R.id.ll_completed_product_share).setOnClickListener(this);
        mIsKolView.findViewById(R.id.ll_share_product).setOnClickListener(this);
        mIsKolView.findViewById(R.id.ll_ongoing_invite).setOnClickListener(this);
        mIsKolView.findViewById(R.id.ll_completed_invite).setOnClickListener(this);
        mIsKolView.findViewById(R.id.ll_share_invite).setOnClickListener(this);
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
            case R.id.iv_message:
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
            case R.id.ll_total_money:
                startActivity(WalletActivity.class);
                break;
            case R.id.ll_sign_in:
                startActivity(UserSignActivity.class);
                break;
            case R.id.ll_ongoing_activities:
                startActivity(MyCampaignActivity.class);
                break;
            case R.id.ll_completed_activities:
                startActivity(MyCampaignActivity.class);
                break;
            case R.id.ll_share_activities:
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

}
