package com.robin8.rb.module.first.view;

import com.robin8.rb.module.first.model.FirstPagerModel;

/**
 * Created by seven on 24/02/2017.
 */

public interface IFirstPagerView {

    void setTotalIncome(String totalIncome);
    void showUnreadMessage(int unReadMessages);
    void setSignInData(String continuousCheckInCount, boolean hadCheckedInToday);
    void setCampaignData(FirstPagerModel.Campaigns campaignData);
    void setProductData(FirstPagerModel.Product productData);
    void setInviteData(FirstPagerModel.Invite inviteData);

}
