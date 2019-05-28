package com.robin8.rb.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.andview.refreshview.XRefreshView;
import com.robin8.rb.R;
import com.robin8.rb.ui.adapter.BrandBillAdapter;
import com.robin8.rb.ui.adapter.IncomeDetailAdapter;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.ui.model.BrandBillModel;
import com.robin8.rb.ui.fragment.BrandBillFragment;
import com.robin8.rb.ui.module.social.adapter.TabsFragmentAdapter;
import com.robin8.rb.ui.module.social.view.EasySlidingTabs;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 广告主--我的账单页面
 */
public class BrandBillActivity extends BaseActivity implements IncomeDetailAdapter.OnBottomListener {

    private final static int INIT_DATA = 0;
    private final static int DRAG_REFRESH = 1;
    private final static int LOAD_MORE = 2;
    private int mCurrentState = INIT_DATA;
    private WProgressDialog mWProgressDialog;
    private RecyclerView mRecyclerView;
    private BrandBillAdapter mBrandBillAdapter;
    private List<BrandBillModel.BrandBill> mBrandBillList = new ArrayList<BrandBillModel.BrandBill>();
    private XRefreshView mXRefreshView;
    private LinearLayoutManager mLinearLayoutManager;
    private BasePresenter mBasePresenter;
    private RequestParams mRequestParams;
    private String url;
    private String title;
    private boolean mHasMore = true;
    private boolean mIsBottomB;
    private View mNoDataLL;
    public EasySlidingTabs myTabs;
    private ViewPager mViewPager;
    private List<Fragment> mFragmentList;
    private String[] titles;
    private TabsFragmentAdapter tabAdapter;
    private BrandBillFragment moneyStream;
    private BrandBillFragment pointStream;
    @Override
    public void setTitleView() {
        initData();
        mTVCenter.setText(title);
    }

    private void initData() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_brand_bill, mLLContent, true);
        myTabs = ((EasySlidingTabs) view.findViewById(R.id.my_indicator));
        myTabs.setUnderlineHeight(1);
        mViewPager = ((ViewPager) view.findViewById(R.id.my_vp));
        mFragmentList = new ArrayList<>();
        titles =new String[]{getString(R.string.robin422), getString(R.string.robin423)};
        moneyStream = BrandBillFragment.newInstance(0, mViewPager);
        pointStream = BrandBillFragment.newInstance(1, mViewPager);
        mFragmentList.add(moneyStream);
        mFragmentList.add(pointStream);
        tabAdapter = new TabsFragmentAdapter(getSupportFragmentManager(), titles, mFragmentList);
        mViewPager.setAdapter(tabAdapter);
        myTabs.setViewPager(mViewPager);
    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.MY_INCOME;
        super.onResume();
    }


        @Override
        protected void executeOnclickLeftView () {
            finish();
        }

        @Override
        protected void executeOnclickRightView () {
        }

        @Override
        public void isOnBottom ( boolean isBottom){
            this.mIsBottomB = isBottom;
        }


}
