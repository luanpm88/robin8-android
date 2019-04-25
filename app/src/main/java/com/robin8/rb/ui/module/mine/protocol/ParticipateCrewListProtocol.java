package com.robin8.rb.ui.module.mine.protocol;

import android.app.Activity;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.base.BaseRecyclerViewPresenter;
import com.robin8.rb.ui.indiana.protocol.IProtocol;
import com.robin8.rb.ui.module.mine.adapter.ParticipateCrewListAdapter;
import com.robin8.rb.ui.module.mine.model.ParticipateCrewListModel;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Figo on 2016/6/30.
 */
public class ParticipateCrewListProtocol implements IProtocol {

    private static final String TYPE_ANNOUNCEMENT = "announcement";
    private static final String TYPE_COMMON = "common";
    private  int id;
    private Activity mActivity;
    private List<ParticipateCrewListModel.CampaignInvitesBean> mDataList = new ArrayList<ParticipateCrewListModel.CampaignInvitesBean>();
    private ParticipateCrewListAdapter mParticipateCrewListAdapter;
    private int mCurrentPage = 1;
    private RequestParams mRequestParams;
    private int mTotalPages;

    public ParticipateCrewListProtocol(Activity activity, int id) {
        this.mActivity = activity;
        this.id = id;
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
        mRequestParams.put("id", id);
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
        mParticipateCrewListAdapter = new ParticipateCrewListAdapter(mDataList);
        return mParticipateCrewListAdapter;
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
        LogUtil.logXXfigo("json"+json);
        ParticipateCrewListModel model = GsonTools.jsonToBean(json, ParticipateCrewListModel.class);
        if (model != null && model.getError() == 0) {
            mTotalPages = model.getTotal_pages();
            List<ParticipateCrewListModel.CampaignInvitesBean> tempList = model.getCampaign_invites();
            if (tempList != null && tempList.size() > 0) {
                if (currentState != BaseRecyclerViewPresenter.LOAD_MORE) {
                    mDataList.clear();
                }
                mCurrentPage++;
                mDataList.addAll(tempList);
                mParticipateCrewListAdapter.setDataList(mDataList);
                mParticipateCrewListAdapter.notifyDataSetChanged();
            }
        }
    }
}
