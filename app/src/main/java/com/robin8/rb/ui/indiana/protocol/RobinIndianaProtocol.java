package com.robin8.rb.ui.indiana.protocol;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.R;
import com.robin8.rb.base.BaseRecyclerViewActivity;
import com.robin8.rb.base.BaseRecyclerViewPresenter;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.ui.indiana.adapter.RobinIndianaAdapter;
import com.robin8.rb.ui.indiana.model.RobinIndianaModel;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Figo on 2016/6/30.
 */
public class RobinIndianaProtocol implements IProtocol {

    private Activity mActivity;
    private List<RobinIndianaModel.IndianaActivity> mIndianaActivityList = new ArrayList<RobinIndianaModel.IndianaActivity>();
    private RobinIndianaAdapter mRobinIndianaAdapter;
    private int mCurrentPage = 1;
    private RequestParams mRequestParams;
    private int mTotalPages;

    public RobinIndianaProtocol(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public RequestParams getRequestParams() {

        if (mTotalPages != 0 && mCurrentPage > mTotalPages) {
            return null;
        }

        if (mRequestParams == null) {
            mRequestParams = new RequestParams();
        }
        mRequestParams.put("page", mCurrentPage);
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
        mRobinIndianaAdapter = new RobinIndianaAdapter(mIndianaActivityList);
        mRobinIndianaAdapter.setOnRecyclerViewListener(new RobinIndianaAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                skipToDetail(position);
            }
        });
        return mRobinIndianaAdapter;
    }

    private void skipToDetail(int position) {
        if (mIndianaActivityList == null || position < 0 || position >= mIndianaActivityList.size()
                || mIndianaActivityList.get(position) == null) {
            return;
        }

        Intent intent = new Intent(mActivity, BaseRecyclerViewActivity.class);
        intent.putExtra("code", mIndianaActivityList.get(position).getCode());
        intent.putExtra("destination", SPConstants.INDIANA_DETAIL);
        intent.putExtra("url", HelpTools.getUrl(CommonConfig.LOTTERY_ACTIVITIES_URL + "/" + mIndianaActivityList.get(position).getCode()));
        intent.putExtra("title", mActivity.getString(R.string.detail_indiana));
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
        Log.e("xxfigo", "mCurrentPage=" + mCurrentPage + "  currentState=" + currentState);
        RobinIndianaModel robinIndianaModel = GsonTools.jsonToBean(json, RobinIndianaModel.class);
        if (robinIndianaModel != null && robinIndianaModel.getError() == 0) {
            mTotalPages = robinIndianaModel.getTotal_pages();
            List<RobinIndianaModel.IndianaActivity> tempList = robinIndianaModel.getActivities();
            if (tempList != null && tempList.size() > 0) {
                if (currentState != BaseRecyclerViewPresenter.LOAD_MORE) {
                    mIndianaActivityList.clear();
                    mIndianaActivityList.add(null);
                }
                mCurrentPage++;
                Log.e("xxfigo", "mCurrentPage=" + mCurrentPage);
                mIndianaActivityList.addAll(tempList);
                mRobinIndianaAdapter.setDataList(mIndianaActivityList);
                mRobinIndianaAdapter.notifyDataSetChanged();
            }
        }
    }
}
