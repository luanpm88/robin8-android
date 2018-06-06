package com.robin8.rb.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.andview.refreshview.XRefreshView;
import com.robin8.rb.R;
import com.robin8.rb.adapter.BrandBillAdapter;
import com.robin8.rb.adapter.ViewPagerAdapter;
import com.robin8.rb.base.BaseFragment;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.model.BrandBillCreditModel;
import com.robin8.rb.model.BrandBillModel;
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
import com.robin8.rb.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static com.robin8.rb.R.id.xrefreshview;

/**
 品牌主账单
 A simple {@link Fragment} subclass. */
public class BrandBillFragment extends BaseFragment implements View.OnClickListener {
    private static final String ARG_PARAM = "TYPE";
    private ViewPager vp;
    private String[] name = {"资金流水", "积分流水"};
    private String type;

    private XRefreshView mXRefreshView;
    private RecyclerView mRecyclerView;
    private LinearLayout mErrorViewLL;
    private WProgressDialog mWProgressDialog;
    private RefreshHeaderView mRefreshHeaderView;
    private RefreshFooterView mRefreshFooterView;
    private final static int INIT_DATA = 0;
    public final static int DRAG_REFRESH = 1;
    private final static int LOAD_MORE = 2;
    private int mCurrentState = INIT_DATA;
    private int mTotalPages;
    private int mCurrentPage = 1;
    protected LinearLayout llList;
    private BrandBillAdapter myAdapter;
    private List<BrandBillModel.BrandBill> mDataList;
    private List<BrandBillCreditModel.CreditsBean> mDataCreditList;
    private String url;
    private int typeNum;

    public BrandBillFragment() {
    }

    @SuppressLint("ValidFragment")
    public BrandBillFragment(ViewPager vp) {
        this.vp = vp;
    }

    public static BrandBillFragment newInstance(int param1, ViewPager vp) {
        BrandBillFragment fragment = new BrandBillFragment(vp);
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int anInt = getArguments().getInt(ARG_PARAM);
            typeNum = anInt;
            type = name[anInt];
        }
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_bsrand_bill, null);
        mXRefreshView = (XRefreshView) view.findViewById(xrefreshview);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mErrorViewLL = (LinearLayout) view.findViewById(R.id.ll_error);
        llList = ((LinearLayout) view.findViewById(R.id.ll_list));
        mErrorViewLL.setOnClickListener(this);
        mDataList = new ArrayList<>();
        mDataCreditList = new ArrayList<>();
        initRecyclerView();
        getData();
        return view;
    }

    private void getData() {
        if ((mTotalPages != 0 && mCurrentPage > mTotalPages) && mCurrentState == LOAD_MORE) {
            mXRefreshView.stopLoadMore(true);
            return;
        }
        if (mCurrentState != LOAD_MORE) {
            mCurrentPage = 1;
        }
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(mActivity);
        }
        mWProgressDialog.show();
        BasePresenter mBasePresenter = new BasePresenter();
        RequestParams mRequestParams = new RequestParams();
        mRequestParams.put("page", mCurrentPage);
        if (typeNum == 0) {
            url = HelpTools.getUrl(CommonConfig.KOL_BRAND_BILL_URL);
        } else {
            url = HelpTools.getUrl(CommonConfig.KOL_BRAND_BILL_CREADIT_URL);
        }
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, url, mRequestParams, new RequestCallback() {

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
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                if (mXRefreshView != null) {
                    mXRefreshView.stopRefresh();
                    mXRefreshView.stopLoadMore();
                }
                if (mRefreshHeaderView != null && mCurrentState == INIT_DATA) {
                    mRefreshHeaderView.setRefreshTime(System.currentTimeMillis());
                }
                if (typeNum == 0) {
                    LogUtil.LogShitou("品牌主账单资金流水", response);
                } else {
                    LogUtil.LogShitou("品牌主账单积分流水", response);
                }
                if (typeNum == 0) {
                    BrandBillModel model = GsonTools.jsonToBean(response, BrandBillModel.class);
                    if (model != null) {
                        if (model.getError() == 0) {
                            mTotalPages = model.getTotal_pages();
                            if (model.getTransactions() != null && model.getTransactions().size() != 0) {
                                mErrorViewLL.setVisibility(View.GONE);
                                if (mCurrentState != LOAD_MORE) {
                                    mDataList.clear();
                                }
                                mCurrentPage++;
                                mDataList.addAll(model.getTransactions());
                                myAdapter.setDataList(mDataList);
                                myAdapter.notifyDataSetChanged();
                            } else {
                                if (mCurrentState != LOAD_MORE) {
                                    mDataList.clear();
                                }
                                myAdapter.notifyDataSetChanged();
                                if (mDataList.size() == 0) {
                                    mErrorViewLL.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                } else if (typeNum == 1) {
                    BrandBillCreditModel modelCredit = GsonTools.jsonToBean(response, BrandBillCreditModel.class);
                    if (modelCredit != null) {
                        if (modelCredit.getError() == 0) {
                            mTotalPages = modelCredit.getTotal_pages();
                            if (modelCredit.getCredits() != null && modelCredit.getCredits().size() != 0) {
                                mErrorViewLL.setVisibility(View.GONE);
                                if (mCurrentState != LOAD_MORE) {
                                    mDataList.clear();
                                }
                                mCurrentPage++;
                                mDataCreditList.addAll(modelCredit.getCredits());
                                myAdapter.setCreditDataList(mDataCreditList);
                                myAdapter.notifyDataSetChanged();
                            } else {
                                if (mCurrentState != LOAD_MORE) {
                                    mDataCreditList.clear();
                                }
                                myAdapter.notifyDataSetChanged();
                                if (mDataCreditList.size() == 0) {
                                    mErrorViewLL.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                }

            }
        });

    }

    private void initRecyclerView() {
        mXRefreshView.setPullLoadEnable(true);
        mXRefreshView.setSlienceLoadMore();
        mXRefreshView.setAutoLoadMore(true);

        mRefreshHeaderView = new RefreshHeaderView(mActivity);
        mXRefreshView.setCustomHeaderView(mRefreshHeaderView);
        mXRefreshView.post(new Runnable() {

            @Override
            public void run() {
                mRefreshHeaderView.findViewById(R.id.ll_setting).getLayoutParams().width = DensityUtils.getScreenWidth(mActivity.getApplicationContext());
            }
        });

        mRefreshFooterView = new RefreshFooterView(mActivity);
        mXRefreshView.setCustomFooterView(mRefreshFooterView);

        mXRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                mCurrentState = DRAG_REFRESH;
                getData();
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                mCurrentState = LOAD_MORE;
                getData();
            }
        });
        myAdapter = new BrandBillAdapter(mDataList,mDataCreditList, typeNum);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(myAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    public void setData(ViewPagerAdapter.SelectItem data, String url, String pageName) {

    }

    @Override
    public void onClick(View view) {

    }
}
