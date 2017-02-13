package com.robin8.rb.task;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.fragment.ContentFragment;
import com.robin8.rb.model.BaseBean;
import com.robin8.rb.model.LaunchRewordModel;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.view.widget.CustomDialogManager;

import com.robin8.rb.R;

/**
 * @author Figo
 * @Description 活动管理任务
 * @date 2016/7/28 11:05
 */
public class CampaignTask {

    private static Activity mActivity;
    private  ContentFragment mContentFragment;
    private  BasePresenter mBasePresenter;
    private WProgressDialog mWProgressDialog;

    public CampaignTask(Activity activity) {
        this.mActivity = activity;
        mBasePresenter = new BasePresenter();
    }

    public CampaignTask(Activity activity, ContentFragment contentFragment) {
        this.mActivity = activity;
        this.mContentFragment =  contentFragment;
        mBasePresenter = new BasePresenter();
    }

    /**
     * 撤销活动
     */
    public void cancelCampaign(LaunchRewordModel.Campaign campaign, final int resultCode, boolean showPopB) {

        if(showPopB){
            showPopWindow(campaign, resultCode);
            return;
        }

        if(mWProgressDialog == null){
            mWProgressDialog = WProgressDialog.createDialog(mActivity);
        }
        mWProgressDialog.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("id", campaign.getId());
        mBasePresenter.getDataFromServer(true, HttpRequest.PUT, HelpTools.getUrl(CommonConfig.KOL_CAMPAIGNS_REVOKE_URL), requestParams, new RequestCallback() {
            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
                Log.e("xxfigo", "撤销活动 " + response);
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);
                if (baseBean == null) {
                    CustomToast.showShort(mActivity, mActivity.getString(R.string.please_data_wrong));
                    return;
                }
                if (baseBean.getError() == 0) {
                    if(resultCode == SPConstants.MY_CAMPAIGN_ACTIVITY && mContentFragment!=null){
                        mContentFragment.setCurrentState(ContentFragment.DRAG_REFRESH);
                        mContentFragment.getDataFromNet(ContentFragment.DRAG_REFRESH);
                    }else {
                        mActivity.setResult(resultCode);
                        mActivity.finish();
                    }
                } else {
                    CustomToast.showShort(mActivity, baseBean.getError());
                }
            }
        });
    }

    /**
     * 提示框
     * @param campaign
     * @param resultCode
     */
    private void showPopWindow(final LaunchRewordModel.Campaign campaign, final int resultCode) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_bottom, null);
        TextView confirmTV = (TextView)view.findViewById(R.id.tv_confirm);
        TextView cancelTV = (TextView)view.findViewById(R.id.tv_cancel);
        confirmTV.setText(mActivity.getResources().getString(R.string.revoke_campaign));
        final CustomDialogManager cdm = new CustomDialogManager(mActivity, view);
        confirmTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cdm.dismiss();
                cancelCampaign(campaign, resultCode, false);
            }
        });

        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cdm.dismiss();
            }
        });

        cdm.dg.setCanceledOnTouchOutside(true);
        cdm.dg.getWindow().setGravity(Gravity.BOTTOM);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_animations);
        cdm.showDialog();
    }

    public void onDestroy() {
        if(mBasePresenter != null){
            mBasePresenter =null;
        }
        if(mWProgressDialog != null){
            mWProgressDialog = null;
        }
    }
}
