package com.robin8.rb.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.andview.refreshview.XRefreshView;
import com.robin8.rb.R;
import com.robin8.rb.ui.activity.EvaluateActivity;
import com.robin8.rb.ui.adapter.MyCampaignAdapter;
import com.robin8.rb.ui.adapter.ViewPagerAdapter;
import com.robin8.rb.base.BaseFragment;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.ui.model.LaunchRewordModel;
import com.robin8.rb.ui.model.MyCampaignModel;
import com.robin8.rb.ui.module.first.activity.LaunchRewordSecondActivity;
import com.robin8.rb.ui.module.mine.activity.MyCampaignDetailActivity;
import com.robin8.rb.ui.module.mine.activity.MyLaunchCampaignRejectActivity;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.task.CampaignTask;
import com.robin8.rb.ui.widget.RefreshFooterView;
import com.robin8.rb.ui.widget.RefreshHeaderView;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class ContentFragment extends BaseFragment {

    private final static int INIT_DATA = 0;
    public final static int DRAG_REFRESH = 1;
    private final static int LOAD_MORE = 2;
    private int mCurrentState = INIT_DATA;

    private ViewPagerAdapter.SelectItem mData;
    private XRefreshView mXRefreshView;
    private RecyclerView mRecyclerView;
    private RefreshHeaderView mRefreshHeaderView;
    private RefreshFooterView mRefreshFooterView;
    private List<LaunchRewordModel.Campaign> mCampaignList = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private WProgressDialog mWProgressDialog;
    private int mCurrentPage;
    private BasePresenter mBasePresenter;
    private RequestParams mRequestParams;
    private MyCampaignAdapter mMyCampaignAdapter;
    private View mLLNodata;
    private int mTotalPages;
    public static  final int EVALUATE_ACTIVITY = 0001;
    private int evaluatePosition = 0;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_income_detail);
        mLLNodata = view.findViewById(R.id.ll_nodata);
        mRecyclerView.setHasFixedSize(true);
        mXRefreshView = (XRefreshView) view.findViewById(R.id.xrefreshview);
        initXRefreshView();
        initRecyclerView();
        getDataFromNet(mCurrentState);
        return view;
    }

    public  void setCurrentState(int currentState){
        mCurrentState = currentState;
    }

    @Override
    public void initData() {
    }

    @Override
    public String getName() {
        return mData.name;
    }

    @Override
    public void setData(ViewPagerAdapter.SelectItem data, String mUrl, String name) {
        this.mData = data;
    }

    private void initXRefreshView() {

        mXRefreshView.setPullLoadEnable(true);
        mXRefreshView.setSlienceLoadMore();
        mXRefreshView.setAutoLoadMore(true);

        mRefreshHeaderView = new RefreshHeaderView(this.getActivity());
        mXRefreshView.setCustomHeaderView(mRefreshHeaderView);
        mXRefreshView.post(new Runnable() {
            @Override
            public void run() {
                mRefreshHeaderView.findViewById(R.id.ll_setting).getLayoutParams().width = DensityUtils.getScreenWidth(mActivity.getApplicationContext());
            }
        });

        mRefreshFooterView = new RefreshFooterView(this.getActivity());
        mXRefreshView.setCustomFooterView(mRefreshFooterView);

        mXRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                mCurrentState = DRAG_REFRESH;
                getDataFromNet(DRAG_REFRESH);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                mCurrentState = LOAD_MORE;
                getDataFromNet(LOAD_MORE);
            }
        });
    }

    private void skipToNextPage(LaunchRewordModel.Campaign campaign) {
        Intent intent = null;
        if( "running".equals(mData.campaignType) ||  "completed".equals(mData.campaignType)){
            intent = new Intent(this.getActivity(), MyCampaignDetailActivity.class);
        }else if("rejected".equals(campaign.getStatus())){
            intent = new Intent(this.getActivity(), MyLaunchCampaignRejectActivity.class);
        }else {
            intent = new Intent(this.getActivity(), LaunchRewordSecondActivity.class);
            intent.putExtra("from", SPConstants.MY_LAUNCH_REWORD_ACTIVITY);
        }
        intent.putExtra("id", campaign.getId());
        startActivityForResult(intent, 0);
    }

    private void initRecyclerView() {
        mMyCampaignAdapter = new MyCampaignAdapter(mCampaignList);
        mMyCampaignAdapter.setData(mData);
        mMyCampaignAdapter.setOnRecyclerViewListener(new MyCampaignAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                skipToNextPage(mCampaignList.get(position));
            }

            @Override
            public void onItemCancelClick(int position) {
                cancelCampaign(position);
            }

            @Override
            public boolean onItemLongClick(int position) {
                return false;
            }
        });
        mLinearLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mMyCampaignAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 撤销活动
     *
     * @param position
     */
    private void cancelCampaign(int position) {
        prepareData(DRAG_REFRESH);
        if (mCampaignList == null || position < 0 || position >= mCampaignList.size()) {
            return;
        }
        if (mData.campaignType.equals(MyCampaignAdapter.TYPE_COMPLETED)){
            evaluatePosition = position;
            Intent intent = new Intent(getActivity(),EvaluateActivity.class);
            intent.putExtra(EvaluateActivity.EFFECT_SCORE,mCampaignList.get(position).getEffect_score());
            intent.putExtra(EvaluateActivity.REVIEW_CONTENT,mCampaignList.get(position).getReview_content());
            intent.putExtra(EvaluateActivity.CAMPAIGN_ID,mCampaignList.get(position).getId());
            getActivity().startActivityForResult(intent,EVALUATE_ACTIVITY);

        }else{
            CampaignTask mCampaignTask = new CampaignTask(mActivity, this);
            mCampaignTask.cancelCampaign(mCampaignList.get(position), SPConstants.MY_CAMPAIGN_ACTIVITY, true);
        }

    }

    /**
     * 加载网络数据
     *
     * @param currentState
     */
    public void getDataFromNet(int currentState) {

         if(currentState== LOAD_MORE && mCurrentPage > mTotalPages && mTotalPages!=0){
             mXRefreshView.setLoadComplete(true);
             return;
        }

        prepareData(currentState);
        if(mRequestParams == null){
            mRequestParams = new RequestParams();
        }
        mRequestParams.put("page", mCurrentPage);
        mRequestParams.put("campaign_type", mData.campaignType);
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.LAUNCH_CAMPAIGNS_URL), mRequestParams, new RequestCallback() {
            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                if (mXRefreshView != null) {
                    mXRefreshView.stopRefresh();
                    mXRefreshView.stopLoadMore();
                }
            }

            @Override
            public void onResponse(String response) {
                if (mXRefreshView != null) {
                    mXRefreshView.stopRefresh();
                    mXRefreshView.stopLoadMore();
                }
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                LogUtil.LogShitou("我发布的活动",response);
                parseJson(response);
            }
        });
    }

    private void prepareData(int currentState) {
        if (mData == null) {
            return;
        }

        if (currentState == INIT_DATA) {
            mWProgressDialog = WProgressDialog.createDialog(this.getActivity());
            mWProgressDialog.show();
        }

        if (currentState != LOAD_MORE) {
            mCurrentPage = 1;
        }

        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }

        mRequestParams = new RequestParams();
    }

    private void parseJson(String json) {
        MyCampaignModel myCampaignModel = GsonTools.jsonToBean(json, MyCampaignModel.class);
        if (myCampaignModel!=null && myCampaignModel.getError() == 0) {
            mTotalPages = myCampaignModel.getTotal_pages();
            List<LaunchRewordModel.Campaign> tempList = myCampaignModel.getCampaigns();
            if (mCurrentState != LOAD_MORE) {
                mCampaignList.clear();
            }
            if (tempList != null && tempList.size() > 0) {
                if (mRefreshHeaderView != null && mCurrentState == INIT_DATA) {
                    mRefreshHeaderView.setRefreshTime(System.currentTimeMillis());
                }

                mCurrentPage++;
                mCampaignList.addAll(tempList);
                mMyCampaignAdapter.setDataList(mCampaignList);
                mMyCampaignAdapter.notifyDataSetChanged();
            }
        }

        if (mCampaignList == null || mCampaignList.size() == 0) {
            mLLNodata.setVisibility(View.VISIBLE);
            mMyCampaignAdapter.setDataList(mCampaignList);
            mMyCampaignAdapter.notifyDataSetChanged();
        } else {
            mLLNodata.setVisibility(View.INVISIBLE);
        }
    }



    public void  changeEvaluateData(int requestCode, int resultCode, Intent data){
        if (requestCode == EVALUATE_ACTIVITY && resultCode == Activity.RESULT_OK){
            if(data!= null){
                String reviewContent =  data.getStringExtra(EvaluateActivity.REVIEW_CONTENT);
                int effectScore =  data.getIntExtra(EvaluateActivity.EFFECT_SCORE,0);
                if (mCampaignList == null || evaluatePosition < 0 || evaluatePosition >= mCampaignList.size()) {
                    return;
                }
                LaunchRewordModel.Campaign campaign = mCampaignList.get(evaluatePosition);
                campaign.setEffect_score(effectScore);
                campaign.setReview_content(reviewContent);
                campaign.setEvaluation_status(MyCampaignAdapter.EVALUAT_ED);
                mMyCampaignAdapter.change(mCampaignList,campaign,evaluatePosition);
            }
        }
    }



}
