package com.robin8.rb.ui.module.mine.protocol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.ui.adapter.ViewPagerAdapter;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.ui.indiana.protocol.IProtocol;
import com.robin8.rb.ui.module.bigv.activity.BigvCampaignDetailActivity;
import com.robin8.rb.ui.module.mine.adapter.MyParticipateCampaignAdapter;
import com.robin8.rb.ui.module.mine.model.MyCampaignModel;
import com.robin8.rb.ui.module.reword.activity.DetailContentActivity;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.LogUtil;

import java.util.ArrayList;
import java.util.List;


/**
 我的活动页面 zc
 Created by Figo on 2016/6/30. */
public class MyPaticipateFragmentProtocol implements IProtocol {
    private final static int INIT_DATA = 0;
    public final static int DRAG_REFRESH = 1;
    private final static int LOAD_MORE = 2;
    private View mLLNodata;
    private ViewPagerAdapter.SelectItem mData;
    private Activity mActivity;
    private List<MyCampaignModel.MyCampaignsBean> mDataList = new ArrayList<>();
    private List<MyCampaignModel.ListBean> mBigvDataList = new ArrayList<>();
    private MyParticipateCampaignAdapter mAdapter;
    private RequestParams mRequestParams;
    private String mTitle;
    private OnItemClickListener mListener;
    private int mCurrentPage = 1;
    private int type;

    public MyPaticipateFragmentProtocol(Activity activity, ViewPagerAdapter.SelectItem data, View mLLNodata,int type) {
        this.mActivity = activity;
        mData = data;
        this.mLLNodata = mLLNodata;
        this.type = type;
    }

    @Override
    public RequestParams getRequestParams() {
        mRequestParams = new RequestParams();
        mRequestParams.put("page", mCurrentPage);
        mRequestParams.put("status", mData.campaignType);
        LogUtil.LogShitou("状态",mData.campaignType);
        return mRequestParams;
    }

    @Override
    public RequestParams getHeaderRequestParams() {
        return null;
    }

    @Override
    public void parseHeaderJson(String json, int currentState) {
    }

    @Override
    public BaseRecyclerAdapter getAdapter() {
        if (type==0){
            mAdapter = new MyParticipateCampaignAdapter(mDataList,type);
        }else {
            mAdapter = new MyParticipateCampaignAdapter(mDataList,mBigvDataList,type);
        }
        mAdapter.setData(mData);
        mAdapter.setOnRecyclerViewListener(new MyParticipateCampaignAdapter.OnRecyclerViewListener() {

            @Override
            public void onItemClick(int position) {
                //                if (mDataList.get(position).getInvite_status().equals("rejected")){
                //                    showRejectDialog(mActivity,mDataList.get(position));
                //                }else {
                //                    skipToNextPage(mDataList.get(position));
                //                }
                if (type==0){
                    skipToNextPage(mDataList.get(position));
                }else {
                    skipToBigv(mBigvDataList.get(position));
                }

            }

            @Override
            public void onContentClick(int position) {
                if (type==0){
                    skipToNextPage(mDataList.get(position));
                }else {
                    skipToBigv(mBigvDataList.get(position));
                }
            }

            @Override
            public void onReasonClick(int position) {
                if (type==0){
                    skipToNextPage(mDataList.get(position));
                }else {
                    skipToBigv(mBigvDataList.get(position));
                }
            }
        });
        return mAdapter;
    }

    private void skipToNextPage(MyCampaignModel.MyCampaignsBean campaign) {
        Intent intent = new Intent(mActivity, DetailContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("campaign_id", campaign.getCampaign_id());
        bundle.putInt("from", SPConstants.MY_CAMPAIGN_ACTIVITY);
        mActivity.startActivity(intent.putExtras(bundle));
    }

    private void skipToBigv(MyCampaignModel.ListBean campaign) {
        Intent intent = new Intent(mActivity, BigvCampaignDetailActivity.class);
        intent.putExtra(BigvCampaignDetailActivity.CAMPAIGN_ID, campaign.getCreation_id());
        mActivity.startActivity(intent);
    }

    @Override
    public void setCurrentPage(int i) {
        this.mCurrentPage = i;
    }

    @Override
    public void showNumberSeletor() {
    }

    @Override
    public void notifyData() {
    }

    @Override
    public void parseJson(String json, int currentState) {
       // LogUtil.LogShitou("我的活动", "===>>" + json);
        MyCampaignModel campaignListBean = GsonTools.jsonToBean(json, MyCampaignModel.class);
        if (campaignListBean != null) {
            if (campaignListBean.getError() == 0) {
                if (type==0){
                    List<MyCampaignModel.MyCampaignsBean> tempList = campaignListBean.getMy_campaigns();
                    if (tempList != null && tempList.size() > 0) {
                        if (currentState != LOAD_MORE) {
                            mDataList.clear();
                        }
                        mCurrentPage++;
                        mDataList.addAll(tempList);
                        mAdapter.setDataList(mDataList);
                        mAdapter.notifyDataSetChanged();

                    }
                }else if (type==1){
                    List<MyCampaignModel.ListBean> tempBigvList = campaignListBean.getList();
                    if (tempBigvList != null && tempBigvList.size() > 0) {
                        if (currentState != LOAD_MORE) {
                            mBigvDataList.clear();
                        }
                        mCurrentPage++;
                        mBigvDataList.addAll(tempBigvList);
                        mAdapter.setmBigvDataList(mBigvDataList);
                        mAdapter.notifyDataSetChanged();

                    }
                }

            }

        }

        if (type==0){
            if (mDataList == null || mDataList.size() == 0) {
                mLLNodata.setVisibility(View.VISIBLE);
                mAdapter.setDataList(mDataList);
                mAdapter.notifyDataSetChanged();
            } else {
                mLLNodata.setVisibility(View.INVISIBLE);
            }
        }else {
            if (mBigvDataList == null || mBigvDataList.size() == 0) {
                mLLNodata.setVisibility(View.VISIBLE);
                mAdapter.setmBigvDataList(mBigvDataList);
                mAdapter.notifyDataSetChanged();
            } else {
                mLLNodata.setVisibility(View.INVISIBLE);
            }
        }

    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public interface OnItemClickListener {
        void onItemClick();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
}
