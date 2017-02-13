package com.robin8.rb.module.mine.protocol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.activity.WebViewActivity;
import com.robin8.rb.adapter.ViewPagerAdapter;
import com.robin8.rb.indiana.protocol.IProtocol;
import com.robin8.rb.model.CampaignListBean;
import com.robin8.rb.module.create.model.ArticleListsModel;
import com.robin8.rb.module.mine.adapter.MyParticipateCampaignAdapter;
import com.robin8.rb.module.reword.activity.DetailContentActivity;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.util.GsonTools;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Figo on 2016/6/30.
 */
public class MyPaticipateFragmentProtocol implements IProtocol {
    private final static int INIT_DATA = 0;
    public final static int DRAG_REFRESH = 1;
    private final static int LOAD_MORE = 2;
    private  View mLLNodata;
    private ViewPagerAdapter.SelectItem mData;
    private Activity mActivity;
    private List<CampaignListBean.CampaignInviteEntity> mDataList = new ArrayList<>();
    private MyParticipateCampaignAdapter mAdapter;
    private RequestParams mRequestParams;
    private String mTitle;
    private OnItemClickListener mListener;
    private int mCurrentPage=1;

    public MyPaticipateFragmentProtocol(Activity activity, ViewPagerAdapter.SelectItem data, View mLLNodata) {
        this.mActivity = activity;
        mData = data;
        this.mLLNodata = mLLNodata;
    }

    @Override
    public RequestParams getRequestParams() {
        mRequestParams = new RequestParams();
        mRequestParams.put("page", mCurrentPage);
        mRequestParams.put("status", mData.campaignType);
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
        mAdapter = new MyParticipateCampaignAdapter(mDataList);
        mAdapter.setData(mData);
        mAdapter.setOnRecyclerViewListener(new MyParticipateCampaignAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                skipToNextPage(mDataList.get(position));
            }
        });
        return mAdapter;
    }

    private void skipToNextPage(CampaignListBean.CampaignInviteEntity campaign) {
        Intent intent = new Intent(mActivity, DetailContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", campaign);
        mActivity.startActivity(intent.putExtras(bundle));
    }

    private void skipToDetail(int position) {
        if (position < 0 || mDataList == null || mDataList.size() == 0 || position >= mDataList.size()) {
            return;
        }
        Object obj = mDataList.get(position);
        if (obj != null && obj instanceof ArticleListsModel.ArticlesBean) {
            ArticleListsModel.ArticlesBean bean = (ArticleListsModel.ArticlesBean) obj;
            Intent intent = new Intent(mActivity, WebViewActivity.class);
            intent.putExtra("title", "详情");
            intent.putExtra("url", bean.getArticle_url());
            mActivity.startActivity(intent);
        }
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

        CampaignListBean campaignListBean = GsonTools.jsonToBean(json, CampaignListBean.class);
        if (campaignListBean.getError() == 0) {
            List<CampaignListBean.CampaignInviteEntity> tempList = campaignListBean.getCampaign_invites();
            if (tempList != null && tempList.size() > 0) {
                if (currentState != LOAD_MORE) {
                    mDataList.clear();
                }
                mCurrentPage++;
                mDataList.addAll(tempList);
                mAdapter.setDataList(mDataList);
                mAdapter.notifyDataSetChanged();
            }
        }

        if (mDataList == null || mDataList.size() == 0) {
            mLLNodata.setVisibility(View.VISIBLE);
            mAdapter.setDataList(mDataList);
            mAdapter.notifyDataSetChanged();
        } else {
            mLLNodata.setVisibility(View.INVISIBLE);
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
