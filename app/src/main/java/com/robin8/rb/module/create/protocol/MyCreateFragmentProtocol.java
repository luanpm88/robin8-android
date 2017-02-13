package com.robin8.rb.module.create.protocol;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.activity.WebViewActivity;
import com.robin8.rb.adapter.ViewPagerAdapter;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.indiana.protocol.IProtocol;
import com.robin8.rb.module.create.adapter.MyCreateAdapter;
import com.robin8.rb.module.create.model.CpsArticlesBean;
import com.robin8.rb.module.create.model.MyCreateModel;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Figo on 2016/6/30.
 */
public class MyCreateFragmentProtocol implements IProtocol {
    private final static int INIT_DATA = 0;
    public final static int DRAG_REFRESH = 1;
    private final static int LOAD_MORE = 2;
    private  View mLLNodata;
    private ViewPagerAdapter.SelectItem mData;
    private Activity mActivity;
    private List<CpsArticlesBean> mDataList = new ArrayList<>();
    private MyCreateAdapter mAdapter;
    private RequestParams mRequestParams;
    private String mTitle;
    private int mCurrentPage=1;

    public MyCreateFragmentProtocol(Activity activity, ViewPagerAdapter.SelectItem data, View mLLNodata) {
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
        mAdapter = new MyCreateAdapter(mDataList);
        mAdapter.setData(mData);
        mAdapter.setOnRecyclerViewListener(new MyCreateAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                skipToKolDetail(mDataList.get(position));
            }
        });
        return mAdapter;
    }

    private void skipToKolDetail(CpsArticlesBean bean) {
        Intent intent = new Intent(mActivity, WebViewActivity.class);
        intent.putExtra("title", "详情");
        intent.putExtra("url", bean.getShow_url());
        intent.putExtra("id", bean.getId());
        if(MyCreateAdapter.TYPE_SHARES.equals(mData.campaignType) || MyCreateAdapter.TYPE_PASSED.equals(mData.campaignType)){
            intent.putExtra("from", SPConstants.MYCREATE_LIST1);
        }else {
            intent.putExtra("from", SPConstants.MYCREATE_LIST2);
        }
        intent.putExtra("img_url", bean.getCover());
        intent.putExtra("share_title", bean.getTitle());
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

        MyCreateModel bean = GsonTools.jsonToBean(json, MyCreateModel.class);
        if(bean == null){
            return;
        }
        if (bean.getError() == 0) {
            List<CpsArticlesBean> tempList = bean.getCps_articles();
            if (tempList != null && tempList.size() > 0) {
                if (currentState != LOAD_MORE) {
                    mDataList.clear();
                }
                mCurrentPage++;
                mDataList.addAll(tempList);
                mAdapter.setDataList(mDataList);
                mAdapter.notifyDataSetChanged();
            }
        }else {
            CustomToast.showShort(mActivity,bean.getDetail());
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

}
