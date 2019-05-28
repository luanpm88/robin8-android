package com.robin8.rb.ui.module.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.ui.module.mine.adapter.ApprenticeAdapter;
import com.robin8.rb.ui.module.mine.model.CollectMoneyModel;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.RefreshFooterView;
import com.robin8.rb.ui.widget.RefreshHeaderView;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CollectListActivity extends BaseActivity {
    public static final String ALL_COLLECT_MONEY = "all_money";
    public static final String APPRENTICE_MONEY = "apprentice_money";
    public static final String TODAY_APPRENTICE = "today_apprentice";
    public static final String TYPE = "type";
    @Bind(R.id.tv_people_count)
    TextView tvPeopleCount;
    @Bind(R.id.tv_collect_money)
    TextView tvCollectMoney;
    @Bind(R.id.rv_income_detail)
    RecyclerView mRecyclerView;
    @Bind(R.id.xrefreshview)
    XRefreshView xrefreshview;
    @Bind(R.id.tv_wait_this)
    TextView tvWaitThis;
    @Bind(R.id.layout_no_wait)
    LinearLayout layoutNoWait;
     @Bind(R.id.ll_header)
    LinearLayout layoutHeader;

    private List<CollectMoneyModel.ListBean> mDataList;
    private WProgressDialog mWProgressDialog;
    private int mCurrentPage = 1;
    private RefreshHeaderView mRefreshHeaderView;
    private RefreshFooterView mRefreshFooterView;

    private final static int INIT_DATA = 0;
    public final static int DRAG_REFRESH = 1;
    private final static int LOAD_MORE = 2;
    private int mCurrentState = INIT_DATA;
    private ApprenticeAdapter mAdapter;
    private int thisType = 0;
    private String url="";
    private int mTotalPages;
    @Override
    public void setTitleView() {
        mTVCenter.setText(getString(R.string.apprentice_all_collect_money));
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_collect_list, mLLContent, true);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String allMOney = intent.getStringExtra(ALL_COLLECT_MONEY);
        String type = intent.getStringExtra(TYPE);
        if (type.equals(APPRENTICE_MONEY)){
            //收徒总收益
            thisType = 0;
            mTVCenter.setText(getString(R.string.apprentice_all_collect_money));
            layoutHeader.setVisibility(View.VISIBLE);
            if (! TextUtils.isEmpty(allMOney)){
                tvCollectMoney.setText(allMOney);
            }
            url = HelpTools.getUrl(CommonConfig.APPRENTICE_COLLECT_MONEY_URL);
        }else {
            thisType = 1;
            mTVCenter.setText(getString(R.string.today_apprentices));
            layoutHeader.setVisibility(View.GONE);
            url = HelpTools.getUrl(CommonConfig.TODAY_GET_APPRENTICE_URL);
        }
        initXRefreshView();
        initRecyclerView();
        initData(thisType,mCurrentPage);
    }

    private void initData(final int thisType, int page) {
        if ((mTotalPages != 0 && mCurrentPage > mTotalPages) && mCurrentState == LOAD_MORE) {
            xrefreshview.stopLoadMore(true);
            return;
        }
        if (mCurrentState != LOAD_MORE) {
            mCurrentPage = 1;
            page = 1;
        }
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();
        final BasePresenter mBasePresenter = new BasePresenter();
        RequestParams mRequestParams = new RequestParams();
        mRequestParams.put("page", page);
        mBasePresenter.getDataFromServer(true, HttpRequest.GET,url, mRequestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                if (xrefreshview != null) {
                    xrefreshview.stopRefresh();
                    xrefreshview.stopLoadMore();
                }
            }

            @Override
            public void onResponse(String response) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                if (xrefreshview != null) {
                    xrefreshview.stopRefresh();
                    xrefreshview.stopLoadMore();
                }
                if (mRefreshHeaderView != null && mCurrentState == INIT_DATA) {
                    mRefreshHeaderView.setRefreshTime(System.currentTimeMillis());
                }
               // LogUtil.LogShitou("徒弟总收益", url+"===" + response);
                CollectMoneyModel collectMoneyModel = GsonTools.jsonToBean(response, CollectMoneyModel.class);
                if (collectMoneyModel != null && collectMoneyModel.getError() == 0) {
                    mTotalPages = collectMoneyModel.getTotal_pages();
                    if (thisType==0){
                        tvPeopleCount.setText(String.valueOf(collectMoneyModel.getTotal_count()));
                    }
                    if (collectMoneyModel.getList() != null && collectMoneyModel.getList().size() != 0) {
                        layoutNoWait.setVisibility(View.GONE);
                        if (mCurrentState != LOAD_MORE) {
                            mDataList.clear();
                        }
                        mCurrentPage++;
                        mDataList.addAll(collectMoneyModel.getList());
                        mAdapter.setDataList(mDataList,thisType);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mAdapter.setDataList(mDataList,thisType);
                        mAdapter.notifyDataSetChanged();
                        if (mDataList.size()==0){
                            layoutNoWait.setVisibility(View.VISIBLE);
                        }
                        if (thisType==0){
                            tvWaitThis.setText(R.string.robin471);
                        }else {
                            tvWaitThis.setText(R.string.robin472);
                        }
                    }
                }
            }
        });
    }
    private void initXRefreshView() {

        xrefreshview.setPullLoadEnable(true);
        xrefreshview.setSlienceLoadMore();
        xrefreshview.setAutoLoadMore(true);

        mRefreshHeaderView = new RefreshHeaderView(CollectListActivity.this);
        xrefreshview.setCustomHeaderView(mRefreshHeaderView);
        xrefreshview.post(new Runnable() {
            @Override
            public void run() {
                mRefreshHeaderView.findViewById(R.id.ll_setting).getLayoutParams().width = DensityUtils.getScreenWidth(getApplicationContext());
            }
        });

        mRefreshFooterView = new RefreshFooterView(CollectListActivity.this);
        xrefreshview.setCustomFooterView(mRefreshFooterView);

        xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                mCurrentState = DRAG_REFRESH;
                initData(thisType,DRAG_REFRESH);
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                mCurrentState = LOAD_MORE;
                initData(thisType,LOAD_MORE);
            }
        });
    }

    private void initRecyclerView() {
        mDataList = new ArrayList<>();
        mAdapter = new ApprenticeAdapter(thisType,CollectListActivity.this,mDataList);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(CollectListActivity.this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }


}
