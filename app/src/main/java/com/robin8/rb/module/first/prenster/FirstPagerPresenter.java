package com.robin8.rb.module.first.prenster;

import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.module.first.model.FirstPagerModel;
import com.robin8.rb.module.first.view.IFirstPagerView;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.presenter.PresenterI;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;

/**
 * Created by seven on 24/02/2017.
 */

public class FirstPagerPresenter extends BasePresenter implements PresenterI {
    private IFirstPagerView mIFirstPagerView;

    public FirstPagerPresenter(IFirstPagerView iFirstPagerView) {
        this.mIFirstPagerView = iFirstPagerView;
    }

    public void loadData() {

        getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.FIRST_PAGER_URL), null, new RequestCallback() {
            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response) {
//                LogUtil.LogShitou("这是什么",response);
                FirstPagerModel firstPagerModel = GsonTools.jsonToBean(response, FirstPagerModel.class);
                if (firstPagerModel != null) {
                    mIFirstPagerView.setTotalIncome(firstPagerModel.getTotalIncome());
                    mIFirstPagerView.showUnreadMessage(firstPagerModel.getUnReadMessages());
                    mIFirstPagerView.setSignInData(firstPagerModel.getContinuousCheckInCount(), firstPagerModel.isCheckedInToday());

                    if (firstPagerModel.getCampaigns() != null){
                        mIFirstPagerView.setCampaignData(firstPagerModel.getCampaigns());
                    }
                    if (firstPagerModel.getProduct() != null){
                        mIFirstPagerView.setProductData(firstPagerModel.getProduct());
                    }
                    if (firstPagerModel.getInvite() != null){
                        mIFirstPagerView.setInviteData(firstPagerModel.getInvite());
                    }
                }
            }
        });
    }

}
