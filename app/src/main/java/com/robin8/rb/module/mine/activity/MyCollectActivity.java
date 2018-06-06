package com.robin8.rb.module.mine.activity;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.andview.refreshview.XRefreshView;
import com.robin8.rb.R;
import com.robin8.rb.adapter.MainFindListAdapter;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.model.BaseBean;
import com.robin8.rb.module.find.SetResultCallBack;
import com.robin8.rb.module.find.activity.FindItemDetailActivity;
import com.robin8.rb.module.find.model.FindArticleListModel;
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

import static com.robin8.rb.R.id.xrefreshview;

public class MyCollectActivity extends BaseActivity {
    private XRefreshView mXRefreshView;
    private RecyclerView mRecyclerView;
    private LinearLayout mErrorViewLL;
    private WProgressDialog mWProgressDialog;
    private RefreshHeaderView mRefreshHeaderView;
    private RefreshFooterView mRefreshFooterView;
    private List<FindArticleListModel.ListBean> mDataList;
    private MainFindListAdapter myAdapter;
    private final static int INIT_DATA = 0;
    public final static int DRAG_REFRESH = 1;
    private final static int LOAD_MORE = 2;
    private int mCurrentState = INIT_DATA;
    private int mTotalPages;
    private int mCurrentPage = 1;
    private boolean isCollect = false;
    private boolean isLike = false;
    protected LinearLayout llList;

    @Override
    public void setTitleView() {
        mTVCenter.setText(R.string.my_collect);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_my_collect, mLLContent, true);
        mXRefreshView = (XRefreshView) view.findViewById(xrefreshview);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mErrorViewLL = (LinearLayout) view.findViewById(R.id.ll_error);
        llList = ((LinearLayout) view.findViewById(R.id.ll_list));
        mErrorViewLL.setOnClickListener(this);
        mDataList = new ArrayList<>();
        initRecyclerView();
        getData();
        registCast();
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
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();
        BasePresenter mBasePresenter = new BasePresenter();
        RequestParams mRequestParams = new RequestParams();
        mRequestParams.put("page", mCurrentPage);
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.FIND_ARTICLE_LIST), mRequestParams, new RequestCallback() {

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
               // LogUtil.LogShitou("我的收藏文章列表+", response);
                FindArticleListModel model = GsonTools.jsonToBean(response, FindArticleListModel.class);
                if (model != null) {
                    if (model.getError() == 0) {
                        mTotalPages = model.getTotal_pages();
                        if (model.getList() != null && model.getList().size() != 0) {
                            mErrorViewLL.setVisibility(View.GONE);
                            if (mCurrentState != LOAD_MORE) {
                                mDataList.clear();
                            }
                            mCurrentPage++;
                            mDataList.addAll(model.getList());
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
            }
        });

    }

    private FindArticleListModel.ListBean listBean;

    private void initRecyclerView() {
        mXRefreshView.setPullLoadEnable(true);
        mXRefreshView.setSlienceLoadMore();
        mXRefreshView.setAutoLoadMore(true);

        mRefreshHeaderView = new RefreshHeaderView(MyCollectActivity.this);
        mXRefreshView.setCustomHeaderView(mRefreshHeaderView);
        mXRefreshView.post(new Runnable() {

            @Override
            public void run() {
                mRefreshHeaderView.findViewById(R.id.ll_setting).getLayoutParams().width = DensityUtils.getScreenWidth(getApplicationContext());
            }
        });

        mRefreshFooterView = new RefreshFooterView(MyCollectActivity.this);
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
        myAdapter = new MainFindListAdapter(this, mDataList);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(MyCollectActivity.this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(myAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myAdapter.setClickListener(new MainFindListAdapter.RecyclerListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void OnSimpleClick(View v, int position, SetResultCallBack setResultCallBack) {
                switch (v.getId()) {
                    case R.id.tv_collect:
                        if (isCollect == false) {
                            if (mDataList.get(position).isIs_collected() == false) {
                                setParams(0, true, mDataList.get(position).getPost_id(), setResultCallBack);
                            } else {
                                setParams(0, false, mDataList.get(position).getPost_id(), setResultCallBack);
                            }
                        } else {
                            setParams(0, false, mDataList.get(position).getPost_id(), setResultCallBack);
                        }
                        break;
                    case R.id.ll_like:
                        if (isLike == false) {
                            if (mDataList.get(position).isIs_liked() == false) {
                                setParams(1, true, mDataList.get(position).getPost_id(), setResultCallBack);
                            } else {
                                setParams(1, false, mDataList.get(position).getPost_id(), setResultCallBack);
                            }
                        } else {
                            setParams(1, false, mDataList.get(position).getPost_id(), setResultCallBack);
                        }
                        break;
                    default:
                        setParams(2, true, mDataList.get(position).getPost_id(), setResultCallBack);
                        break;
                }
            }

            @Override
            public void OnItemClick(View v, int position) {
                listBean = mDataList.get(position);
                Intent intent = new Intent(MyCollectActivity.this, FindItemDetailActivity.class);
                if (listBean != null) {
                    intent.putExtra(FindItemDetailActivity.FINDDETAIL, listBean);
                    intent.putExtra(FindItemDetailActivity.FINDDETAPOSITION, position);
                    intent.putExtra(FindItemDetailActivity.FINDWHERE, "2");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    CustomToast.showShort(MyCollectActivity.this, "没有数据没有数据");
                }
            }
        });
    }

    private void setParams(final int whith, final boolean is, final String id, final SetResultCallBack setResultCallBack) {
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(MyCollectActivity.this);
        }
        mWProgressDialog.show();
        BasePresenter mBasePresenter = new BasePresenter();
        RequestParams mRequestParams = new RequestParams();
        switch (whith) {
            case 0:
                mRequestParams.put("_type", "collect");
                break;
            case 1:
                mRequestParams.put("_type", "like");
                break;
            case 2:
                mRequestParams.put("_type", "forward");// like|collect
                break;
        }
        mRequestParams.put("post_id", id);
        if (is) {
            mRequestParams.put("_action", "add");//add | cancel
        } else {
            mRequestParams.put("_action", "cancel");//add | cancel
        }
        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.FIND_SET), mRequestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
              //  LogUtil.LogShitou("find文章设置+", response);
                BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);
                if (baseBean.getError() == 0) {
                    if (whith == 0) {
                        if (is == true) {
                            isCollect = true;
                        } else {
                            isCollect = false;
                        }
                        setResultCallBack.onCollect(is);
                    } else if (whith == 1) {
                        if (is == true) {
                            isLike = true;
                        } else {
                            isLike = false;
                        }
                        setResultCallBack.onLike(is);
                    } else {
                        setResultCallBack.onShare(true);
                    }
                }
            }
        });
    }


    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegistCast();
    }

    public static String action = "com.bean.refresh.collect";
    private RefreshCast myReiceiver;

    public class RefreshCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != intent.getAction()) {
                if (action.equals(intent.getAction())) {
                    Bundle data = intent.getBundleExtra("datas");
                    int position = data.getInt("position");
                    listBean = (FindArticleListModel.ListBean) data.getSerializable("data");
                    //listBean =(FindArticleListModel.ListBean) intent.getSerializableExtra("data");
                    mDataList.set(position, listBean);
                    myAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void registCast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(action);
        myReiceiver = new RefreshCast();
        registerReceiver(myReiceiver, filter);
    }

    private void unRegistCast() {
        unregisterReceiver(myReiceiver);
    }
    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.FIND_COLLECT;
        super.onResume();
    }
}
