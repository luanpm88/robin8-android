package com.robin8.rb.ui.indiana.fragment;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.andview.refreshview.XRefreshView;
import com.robin8.rb.R;
import com.robin8.rb.ui.adapter.ViewPagerAdapter;
import com.robin8.rb.base.BaseFragment;
import com.robin8.rb.base.BaseRecyclerViewActivity;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.ui.indiana.adapter.IndianaMineAdapter;
import com.robin8.rb.ui.indiana.model.IndianaDetailBean;
import com.robin8.rb.ui.indiana.model.IndianaMineModel;
import com.robin8.rb.ui.model.NotifyMsgEntity;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.RefreshFooterView;
import com.robin8.rb.ui.widget.RefreshHeaderView;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class IndianaContentFragment extends BaseFragment implements Observer{

    private final static int INIT_DATA = 0;
    public final static int DRAG_REFRESH = 1;
    private final static int LOAD_MORE = 2;
    private int mCurrentState = INIT_DATA;

    private ViewPagerAdapter.SelectItem mData;
    private XRefreshView mXRefreshView;
    private RecyclerView mRecyclerView;
    private RefreshHeaderView mRefreshHeaderView;
    private RefreshFooterView mRefreshFooterView;
    private List<IndianaDetailBean.IndianaActivity> mCampaignList = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private WProgressDialog mWProgressDialog;
    private int mCurrentPage;
    private BasePresenter mBasePresenter;
    private RequestParams mRequestParams;
    private IndianaMineAdapter mIndianaMineAdapter;
    private View mLLNodata;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_income_detail);
        mLLNodata = view.findViewById(R.id.ll_nodata);
        mRecyclerView.setHasFixedSize(true);
        mXRefreshView = (XRefreshView) view.findViewById(R.id.xrefreshview);

        NotifyManager.getNotifyManager().addObserver(this);

        initXRefreshView();
        initRecyclerView();
        getDataFromNet(mCurrentState);
        return view;
    }

    @Override
    public void initData() {
    }

    @Override
    public String getName() {
        return mData.name;
    }

    @Override
    public void setData(ViewPagerAdapter.SelectItem data, String mUrl , String name) {
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
                getDataFromNet(DRAG_REFRESH);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                getDataFromNet(LOAD_MORE);
            }
        });
    }

    private void skipToNextPage(IndianaDetailBean.IndianaActivity indiana) {
        Intent intent = new Intent(this.getActivity(), BaseRecyclerViewActivity.class);
        intent.putExtra("code", indiana.getCode());
        intent.putExtra("destination", SPConstants.INDIANA_DETAIL);
        intent.putExtra("url", HelpTools.getUrl(CommonConfig.LOTTERY_ACTIVITIES_URL + "/" + indiana.getCode()));
        intent.putExtra("title", mActivity.getString(R.string.detail_indiana));
        startActivityForResult(intent, 0);
    }

    private void initRecyclerView() {
        mIndianaMineAdapter = new IndianaMineAdapter(mCampaignList);
        mIndianaMineAdapter.setOnRecyclerViewListener(new IndianaMineAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                skipToNextPage(mCampaignList.get(position));
            }
        });
        mLinearLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mIndianaMineAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    /**
     * 加载网络数据
     *
     * @param currentState
     */
    public void getDataFromNet(int currentState) {
        prepareData(currentState);
        mRequestParams.put("page", mCurrentPage);
        mRequestParams.put("status", mData.campaignType);
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.LOTTERY_ACTIVITIES_MINE_URL), mRequestParams, new RequestCallback() {
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
        IndianaMineModel indianaMineModel = GsonTools.jsonToBean(json, IndianaMineModel.class);
        if(indianaMineModel == null){
            CustomToast.showShort(mActivity, getString(R.string.please_data_wrong));
            return;
        }
        if (indianaMineModel.getError() == 0) {
            List<IndianaDetailBean.IndianaActivity> tempList = indianaMineModel.getActivities();
            if (tempList != null && tempList.size() > 0) {
                if (mRefreshHeaderView != null && mCurrentState == INIT_DATA) {
                    mRefreshHeaderView.setRefreshTime(System.currentTimeMillis());
                }

                if (mCurrentState != LOAD_MORE) {
                    mCampaignList.clear();
                }
                mCurrentPage++;
                mCampaignList.addAll(tempList);
                mIndianaMineAdapter.setDataList(mCampaignList);
                mIndianaMineAdapter.notifyDataSetChanged();
            }
        }else {
            CustomToast.showShort(mActivity,getString(R.string.please_data_wrong));
            return;
        }

        if (mCampaignList == null || mCampaignList.size() == 0) {
            mXRefreshView.setVisibility(View.INVISIBLE);
            mLLNodata.setVisibility(View.VISIBLE);
        } else {
            mXRefreshView.setVisibility(View.VISIBLE);
            mLLNodata.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof NotifyMsgEntity) {
            NotifyMsgEntity msgEntity = (NotifyMsgEntity) data;
            if (msgEntity.getCode() == NotifyManager.TYPE_INDIANA_PAY_SUCCESSFUL) {
                getDataFromNet(DRAG_REFRESH);
            }
        }
    }

    @Override
    public void onDestroy() {
        NotifyManager.getNotifyManager().deleteObserver(this);
        super.onDestroy();
    }
}
