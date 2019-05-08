package com.robin8.rb.ui.pager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.andview.refreshview.XRefreshView;
import com.robin8.rb.R;
import com.robin8.rb.ui.activity.LoginActivity;
import com.robin8.rb.ui.adapter.MainFindListAdapter;
import com.robin8.rb.ui.adapter.MainFindTypeAdapter;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.BasePager;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.ui.model.BaseBean;
import com.robin8.rb.ui.module.find.SetResultCallBack;
import com.robin8.rb.ui.module.find.activity.FindItemDetailActivity;
import com.robin8.rb.ui.module.find.model.FindArticleListModel;
import com.robin8.rb.ui.module.find.view.TopMiddlePopup;
import com.robin8.rb.ui.module.social.view.HorizontalListView;
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
import com.robin8.rb.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import static com.robin8.rb.R.id.xrefreshview;
import static com.robin8.rb.ui.module.find.activity.FindItemDetailActivity.FINDDETAIL;

/**
 创作
 @author zc 2018-03-30 */
public class FindPager extends BasePager implements View.OnClickListener {

    private View mPager;
    private WProgressDialog mWProgressDialog;
    private XRefreshView mXRefreshView;
    private RecyclerView mRecyclerView;
    private LinearLayout mErrorViewLL;
    private LinearLayout llTitleType;
    private View viewTypeBg;
    private List<FindArticleListModel.ListBean> mDataList;
    private MainFindListAdapter myAdapter;
    private RefreshHeaderView mRefreshHeaderView;
    private RefreshFooterView mRefreshFooterView;
    private int mCurrentPage = 1;
    private static final String IMAGE_URL = CommonConfig.APP_ICON;
    private static final String TITLE_URL = CommonConfig.SERVICE + "/invite?inviter_id=";

    private final static int INIT_DATA = 0;
    private final static int DRAG_REFRESH = 1;
    private final static int LOAD_MORE = 2;
    private int mCurrentState = INIT_DATA;
    private int mTotalPages;
    private HorizontalListView mListViewType;
    private List<List<String>> mDataListType;
    private MainFindTypeAdapter mainFindTypeAdapter;
    private String TITLE_TYPE;
    private boolean isLike = false;
    private boolean isCollect = false;
    private TopMiddlePopup topMiddlePopup;
    private static int screenW, screenH;

    public FindPager(FragmentActivity activity) {
        super(activity);
        this.mActivity = activity;
    }

    @Override
    public void initData() {
        if (mPager == null) {
            mLLContent.setBackgroundResource(R.color.white_custom);
            LayoutInflater layoutInflater = LayoutInflater.from(mActivity.getApplicationContext());
            mPager = layoutInflater.inflate(R.layout.pager_find, mLLContent, true);
            mXRefreshView = (XRefreshView) mPager.findViewById(xrefreshview);
            mRecyclerView = (RecyclerView) mPager.findViewById(R.id.recyclerview);
            mWProgressDialog = WProgressDialog.createDialog(mActivity);
            mErrorViewLL = (LinearLayout) mPager.findViewById(R.id.ll_error);
            llTitleType = ((LinearLayout) mPager.findViewById(R.id.ll_title_type));
            viewTypeBg = ((View) mPager.findViewById(R.id.view_bg_type));
            viewTypeBg.setOnClickListener(this);
            mListViewType = ((HorizontalListView) mPager.findViewById(R.id.list_type));
            getScreenPixels();
            initTitleBar();
            mErrorViewLL.setOnClickListener(this);
            mDataListType = new ArrayList<>();
            mDataList = new ArrayList<>();
            mainFindTypeAdapter = new MainFindTypeAdapter(mActivity, mDataListType);
            setPop();
            initRecyclerView();
            getArticle(true, null);
            mainFindTypeAdapter.setOnRecyclerViewListener(new MainFindTypeAdapter.OnItemViewListener() {

                @Override
                public void onItemClick(int position) {
                    mainFindTypeAdapter.setSelect(position);
                    TITLE_TYPE = mDataListType.get(position).get(0);
                    mainFindTypeAdapter.notifyDataSetChanged();
                    getArticle(false, TITLE_TYPE);
                    mTitleBarText.setText(mDataListType.get(position).get(1));
                    topMiddlePopup.dismiss();
                    // typeLayout(false);
                }
            });
            registCast();
        }
    }


    private void getArticle(final boolean first, String type) {
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
        if (mRefreshHeaderView != null && mCurrentState == INIT_DATA) {
            mRefreshHeaderView.setRefreshTime(System.currentTimeMillis());
        }
        BasePresenter mBasePresenter = new BasePresenter();
        RequestParams params = new RequestParams();
        params.put("page", mCurrentPage);
        if (first == false) {
            params.put("type", type);
        }
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.FIND_ARTICLE), params, new RequestCallback() {

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
                if (first) {
                    LogUtil.LogShitou("find文章+", response);
                } else {
                    LogUtil.LogShitou("find文章类型+", response);
                }
                FindArticleListModel model = GsonTools.jsonToBean(response, FindArticleListModel.class);
                if (model != null) {
                    if (model.getError() == 0) {
                        mTotalPages = model.getTotal_pages();
                        if (model.getLabels().size() != 0) {
                            if (mDataListType != null) {
                                mDataListType.clear();
                            }
                            mDataListType.addAll(model.getLabels());
                            TITLE_TYPE = model.getLabels().get(0).get(0);
                            mainFindTypeAdapter.setSelect(0);
                            mListViewType.setAdapter(mainFindTypeAdapter);
                        }
                        if (model.getList() != null && model.getList().size() != 0) {
                            mErrorViewLL.setVisibility(View.GONE);
                            if (mCurrentState != LOAD_MORE) {
                                mDataList.clear();
                            }
                            mCurrentPage++;
                            mDataList.addAll(model.getList());
                            myAdapter.notifyDataSetChanged();
                        } else {
                            mErrorViewLL.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    mErrorViewLL.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private FindArticleListModel.ListBean listBean;

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
                if (TextUtils.isEmpty(TITLE_TYPE)) {
                    getArticle(true, null);
                } else {
                    getArticle(false, TITLE_TYPE);
                }
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                mCurrentState = LOAD_MORE;
                if (TextUtils.isEmpty(TITLE_TYPE)) {
                    getArticle(true, null);
                } else {
                    getArticle(false, TITLE_TYPE);
                }
            }
        });
        myAdapter = new MainFindListAdapter(mActivity, mDataList);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(myAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myAdapter.setClickListener(new MainFindListAdapter.RecyclerListener() {

            @Override
            public void OnSimpleClick(View v, int position, SetResultCallBack setResultCallBack) {
                if (! BaseApplication.getInstance().hasLogined()) {
                    login();
                } else {
                    if (mDataList.get(position) != null) {
                        switch (v.getId()) {
                            case R.id.tv_collect:
                                if (isCollect == false) {
                                    if (mDataList.get(position).isIs_collected() == false) {
                                        setParams(0, true, mDataList.get(position), setResultCallBack);
                                    } else {
                                        setParams(0, false, mDataList.get(position), setResultCallBack);
                                    }
                                } else {
                                    setParams(0, false, mDataList.get(position), setResultCallBack);
                                }
                                break;
                            case R.id.ll_like:
                                if (isLike == false) {
                                    if (mDataList.get(position).isIs_liked() == false) {
                                        setParams(1, true, mDataList.get(position), setResultCallBack);
                                    } else {
                                        setParams(1, false, mDataList.get(position), setResultCallBack);
                                    }
                                } else {
                                    setParams(1, false, mDataList.get(position), setResultCallBack);
                                }
                                break;
                            default:
                                setParams(2, true, mDataList.get(position), setResultCallBack);
                                break;
                        }
                    }

                }
            }

            @Override
            public void OnItemClick(View v, int position) {
                listBean = mDataList.get(position);
                Intent intent = new Intent(mActivity, FindItemDetailActivity.class);
                if (listBean != null) {
                    intent.putExtra(FINDDETAIL, listBean);
                    LogUtil.LogShitou("穿过去的position","====>"+position);
                    intent.putExtra(FindItemDetailActivity.FINDDETAPOSITION, position);
                    intent.putExtra(FindItemDetailActivity.FINDWHERE, "1");
                    mActivity.startActivity(intent);
                    mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                } else {
                    CustomToast.showShort(mActivity, R.string.robin388);
                }
            }
        });
    }

    /**
     设置
     @param whith 收藏／喜欢／分享
     @param is 是否收藏
     @param listBean listBean
     @param setResultCallBack
     */
    private void setParams(final int whith, final boolean is, final FindArticleListModel.ListBean listBean, final SetResultCallBack setResultCallBack) {
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(mActivity);
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
        mRequestParams.put("post_id", listBean.getPost_id());
        mRequestParams.put("tag",listBean.getTag());
        mRequestParams.put("title",listBean.getTitle());
        if (is) {
            mRequestParams.put("_action", "add");//add | cancel
        } else {
            mRequestParams.put("_action", "cancel");//add | cancel
        }
        mRequestParams.put("tag", listBean.getTag());
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
                LogUtil.LogShitou("find文章设置+", response);
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
    public void initTitleBar() {
        mTitleBarText.setText(R.string.robin377);
        mTitleBarText.setVisibility(View.VISIBLE);
        firstLeft.setVisibility(View.INVISIBLE);
        mImgTypeMenu.setVisibility(View.VISIBLE);
        firstRight.setVisibility(View.GONE);
        mRewordLaunchIv.setVisibility(View.GONE);
        firstRight.setImageResource(R.mipmap.icon_ark_cps);
        mRewordLaunchIv.setImageResource(R.mipmap.icon_ark_write);
        mRewordLaunchIv.setOnClickListener(this);
        firstRight.setOnClickListener(this);
        mImgTypeMenu.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_error:
                getArticle(true, null);
                break;
            case R.id.img_type_menu:
                if (TITLE_TYPE != null && mDataListType != null) {
                    for (int i = 0; i < mDataListType.size(); i++) {
                        if (mTitleBarText.getText().toString().equals(mDataListType.get(i).get(1))) {
                            mainFindTypeAdapter.setSelect(i);
                            mainFindTypeAdapter.notifyDataSetChanged();
                        }
                    }
                }
                if (isUp == false) {
                    topMiddlePopup.show(llTitle);
                    typePop(true);
                } else {
                    typePop(false);
                    topMiddlePopup.dismiss();
                }
                // if (llTitleType.getVisibility() == View.VISIBLE) {
                //     typeLayout(false);
                //     } else {
                //     typeLayout(true);
                //     }
                break;
            case R.id.view_bg_type:
                typeLayout(false);
                break;
        }
    }

    private void setPop() {
        topMiddlePopup = new TopMiddlePopup(mActivity, screenW, screenH, mainFindTypeAdapter, mDataListType);
        topMiddlePopup.setOnRecyclerViewListener(new TopMiddlePopup.OnItemViewListener() {

            @Override
            public void onItemClick(boolean is) {
                if (is) {
                    typePop(false);
                }
            }
        });

    }

    /**
     获取屏幕的宽和高
     */
    public void getScreenPixels() {
        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenW = metrics.widthPixels;
        screenH = metrics.heightPixels;
    }

    private boolean isUp = false;

    private void typePop(boolean is) {
        if (is) {
            isUp = true;
            mImgTypeMenu.setImageResource(R.mipmap.icon_up);
        } else {
            isUp = false;
            mImgTypeMenu.setImageResource(R.mipmap.icon_data_down);
        }
    }

    private void typeLayout(boolean isShow) {
        if (isShow) {
            viewTypeBg.setVisibility(View.VISIBLE);
            viewTypeBg.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.pop_in));
            llTitleType.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.pop_in));
            llTitleType.setVisibility(View.VISIBLE);
            mImgTypeMenu.setImageResource(R.mipmap.icon_up);
        } else {
            viewTypeBg.setVisibility(View.GONE);
            llTitleType.setVisibility(View.GONE);
            mImgTypeMenu.setImageResource(R.mipmap.icon_data_down);
        }
    }

    private void login() {
        Intent intent = new Intent(mActivity, LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("from", SPConstants.MAIN_FIND);
        intent.putExtras(bundle);
        mActivity.startActivityForResult(intent, SPConstants.MAIN_FIND);
    }


    public static String action = "com.bean.refresh";
    private RefreshCast myReiceiver;

    public class RefreshCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != intent.getAction()) {
                if (action.equals(intent.getAction())) {
                    Bundle data = intent.getBundleExtra("datas");
                    int position = data.getInt("position");
                    listBean = (FindArticleListModel.ListBean) data.getSerializable("data");
                   try {
                       if (mDataList.size()==0){
                           listBean.setIs_collected(listBean.isIs_collected());
                           listBean.setIs_liked(listBean.isIs_liked());
                           myAdapter.notifyDataSetChanged();
                       }else {
                           mDataList.set(position, listBean);
                           myAdapter.notifyDataSetChanged();
                       }
                   }catch (Exception e){
                       e.printStackTrace();
                       listBean.setIs_collected(listBean.isIs_collected());
                       listBean.setIs_liked(listBean.isIs_liked());
                       myAdapter.notifyDataSetChanged();
                   }
                }
            }
        }
    }

    private void registCast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(action);
        myReiceiver = new RefreshCast();
        mActivity.registerReceiver(myReiceiver, filter);
    }

    private void unRegistCast() {
        mActivity.unregisterReceiver(myReiceiver);
    }


}
