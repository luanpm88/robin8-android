package com.robin8.rb.base;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.R;
import com.robin8.rb.activity.LoginActivity;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.helper.IconFontHelper;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.indiana.activity.IndianaMineActivity;
import com.robin8.rb.indiana.protocol.DetailIndianaProtocol;
import com.robin8.rb.indiana.protocol.IProtocol;
import com.robin8.rb.indiana.protocol.RobinIndianaProtocol;
import com.robin8.rb.model.BrandBillModel;
import com.robin8.rb.model.NotifyMsgEntity;
import com.robin8.rb.module.create.protocol.ProductListProtocol;
import com.robin8.rb.module.first.activity.BusinessCooperationActivity;
import com.robin8.rb.module.mine.protocol.MessageProtocol;
import com.robin8.rb.module.mine.protocol.ParticipateCrewListProtocol;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.presenter.PresenterI;
import com.robin8.rb.ui.widget.RefreshFooterView;
import com.robin8.rb.ui.widget.RefreshHeaderView;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.view.IRobinIndianaView;
import com.robin8.rb.view.widget.CustomDialogManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


/**
 * @author Figo
 */
public class BaseRecyclerViewPresenter extends BasePresenter implements PresenterI, Observer {

    public final static int INIT_DATA = 0;
    public final static int DRAG_REFRESH = 1;
    public final static int LOAD_MORE = 2;
    private int mCurrentState = INIT_DATA;
    private WProgressDialog mWProgressDialog;
    private RecyclerView mRecyclerView;
    private BaseRecyclerAdapter mAdapter;
    private List<BrandBillModel.BrandBill> mBrandBillList = new ArrayList<BrandBillModel.BrandBill>();
    private XRefreshView mXRefreshView;
    private LinearLayoutManager mLinearLayoutManager;
    private RequestParams mRequestParams;
    private RefreshHeaderView mRefreshHeaderView;
    private RefreshFooterView mRefreshFooterView;
    private String url;
    private String title;

    private final IRobinIndianaView mIUserView;
    private Activity mActivity;
    private IProtocol mIProtocol;
    private int destination;
    private String hearderUrl;
    private boolean mNeedHeader;
    private String code;
    private RequestParams mHeaderRequestParams;
    private int id;
    private int from;
    private String mCurrentSearchContent;
    private String mCurrentCategoryName;
    private String mCurrentOrder;
    private View llNoData;

    public BaseRecyclerViewPresenter(Activity activity, IRobinIndianaView userView) {
        mActivity = activity;
        mIUserView = userView;
    }

    public void start() {
        initData();
        loadData();
        initXRefreshView();
        initRecyclerView();
        getHeaderData();
    }

    private void initData() {
        Intent intent = mActivity.getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        code = intent.getStringExtra("code");
        from = intent.getIntExtra("from", -1);
        destination = intent.getIntExtra("destination", -1);
        id = intent.getIntExtra("id", 0);
        mXRefreshView = mIUserView.getXRefreshView();
        mRecyclerView = mIUserView.getRecyclerView();
        llNoData = mIUserView.getLLNoData();
        mIUserView.setTitleView(title);

        NotifyManager.getNotifyManager().addObserver(this);
    }

    private void loadData() {
        TextView rightTv = mIUserView.getRightTv();
        TextView bottomTv = mIUserView.getBottomTv();
        if(bottomTv!=null){
            bottomTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIProtocol.showNumberSeletor();
                }
            });
        }

        switch (destination) {
            case SPConstants.INDIANA_DETAIL:
                mIProtocol = new DetailIndianaProtocol(mActivity, code, bottomTv);
                hearderUrl = url;
                url = url + "/orders";
                mNeedHeader = true;
                rightTv.setVisibility(View.INVISIBLE);
                mIUserView.setPageName(StatisticsAgency.MY_INDIANA_DETAIL);
                break;
            case SPConstants.INDIANA_ROBIN:
                mIProtocol = new RobinIndianaProtocol(mActivity);
                rightTv.setText(mActivity.getString(R.string.text_mine));
                mIUserView.setPageName(StatisticsAgency.MY_INDIANA);
                break;

            case SPConstants.MESSAGE_ACTIVITY:
                mIProtocol = new MessageProtocol(mActivity);
                rightTv.setTextSize(20);
                IconFontHelper.setTextIconFont(rightTv, R.string.icons_message_open);
                mIUserView.setPageName(StatisticsAgency.MY_MESSAGE);
                break;

            case SPConstants.PARTICIPATE_CREW_LIST:
                mIProtocol = new ParticipateCrewListProtocol(mActivity, id);
                rightTv.setVisibility(View.INVISIBLE);
                mIUserView.setPageName(StatisticsAgency.ADVERTISER_MY_DETAIL_KOLS);
                break;

            case SPConstants.PRODUCT_LIST:
                mIProtocol = new ProductListProtocol(mActivity, null, from);
                ((ProductListProtocol)mIProtocol).setView(llNoData,mXRefreshView);
                mIUserView.setPageName(StatisticsAgency.PRODUCT_LIST);
                break;
        }
    }


    @Override
    public void getDataFromServer(boolean needHeader, int method, String url, RequestParams params, RequestCallback callback) {
        switch (method) {
            case HttpRequest.GET:
                HttpRequest.getInstance().get(needHeader, url, params, callback);
                break;
            case HttpRequest.POST:
                HttpRequest.getInstance().post(needHeader, url, params, callback);
                break;
            case HttpRequest.PUT:
                HttpRequest.getInstance().put(needHeader, url, params, callback);
                break;
        }
    }

    private void initXRefreshView() {

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

        mXRefreshView.setPullLoadEnable(true);
        mXRefreshView.setSlienceLoadMore();
        mXRefreshView.setAutoLoadMore(true);

        mRefreshFooterView = new RefreshFooterView(mActivity);
        mXRefreshView.setCustomFooterView(mRefreshFooterView);

        mXRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                mCurrentState = DRAG_REFRESH;
                getHeaderData();
            }

            @Override
            public void onLoadMore(boolean isSlience) {
                mCurrentState = LOAD_MORE;
                getHeaderData();
            }
        });
    }

    private void initRecyclerView() {
        mAdapter = mIProtocol.getAdapter();
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


    /**
     * 加载网络数据
     *
     */
    private void getDataFromNet() {

        if (mCurrentState != LOAD_MORE) {
            mIProtocol.setCurrentPage(1);
        }

        mRequestParams = mIProtocol.getRequestParams();
        if (mRequestParams == null) {
            mXRefreshView.stopLoadMore(true);
            return;
        } else {
            mXRefreshView.setSlienceLoadMore();
            mXRefreshView.setAutoLoadMore(true);
        }

        if (!TextUtils.isEmpty(mCurrentSearchContent)) {
            mRequestParams.put("goods_name", mCurrentSearchContent);
        }

        if (!TextUtils.isEmpty(mCurrentCategoryName) && !mCurrentCategoryName.equals(mActivity.getString(R.string.all_classify))) {
            mRequestParams.put("category_name", mCurrentCategoryName);
        }

        if (!TextUtils.isEmpty(mCurrentOrder)) {
            mRequestParams.put("order_by", mCurrentOrder);
        }

        if (mCurrentState == INIT_DATA) {
            if (mWProgressDialog == null) {
                mWProgressDialog = WProgressDialog.createDialog(mActivity);
            }
            if (!mWProgressDialog.isShowing()) {
                mWProgressDialog.show();
            }
        }

        getDataFromServer(true, HttpRequest.GET, url, mRequestParams, new RequestCallback() {
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

                if (mRefreshHeaderView != null && mCurrentState == INIT_DATA) {
                    mRefreshHeaderView.setRefreshTime(System.currentTimeMillis());
                }
                mIProtocol.parseJson(response, mCurrentState);
            }
        });
    }

    public void getHeaderData() {

        if (!mNeedHeader || mCurrentState == LOAD_MORE) {
            getDataFromNet();
            return;
        }
        if (mCurrentState == INIT_DATA) {
            if (mWProgressDialog == null) {
                mWProgressDialog = WProgressDialog.createDialog(mActivity);
            }
            if (!mWProgressDialog.isShowing()) {
                mWProgressDialog.show();
            }
        }
        mHeaderRequestParams = mIProtocol.getHeaderRequestParams();
        getDataFromServer(true, HttpRequest.GET, hearderUrl, mHeaderRequestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
                mIProtocol.parseHeaderJson(response, mCurrentState);
                getDataFromNet();
            }
        });
    }

    public void finish() {
        if (mWProgressDialog != null) {
            mWProgressDialog.dismiss();
            mWProgressDialog = null;
        }

        NotifyManager.getNotifyManager().deleteObserver(this);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof NotifyMsgEntity) {
            NotifyMsgEntity msgEntity = (NotifyMsgEntity) data;
            if (msgEntity.getCode() == NotifyManager.TYPE_INDIANA_PAY_SUCCESSFUL) {
                mCurrentState = DRAG_REFRESH;
                getHeaderData();
            }
            if (msgEntity.getCode() == NotifyManager.TYPE_INSERT_PRODUCT) {
                mActivity.finish();
            }
        }
    }

    public void OnclickRightView() {
        Intent intent;
        switch (destination) {
            case SPConstants.INDIANA_ROBIN:
                intent = new Intent(mActivity, IndianaMineActivity.class);
                mActivity.startActivity(intent);
                break;
            case SPConstants.MESSAGE_ACTIVITY:
                showMessageDiaog();
                break;
            case SPConstants.PRODUCT_LIST:
                if (BaseApplication.getInstance().hasLogined()) {
                    intent = new Intent(mActivity, BusinessCooperationActivity.class);
                    intent.putExtra("from", SPConstants.PRODUCT_LIST);
                    mActivity.startActivity(intent);
                } else {
                    intent = new Intent(mActivity, LoginActivity.class);
                    mActivity.startActivity(intent);
                }
                break;
        }
    }

    /**
     * 消息设置已读弹窗
     */
    private void showMessageDiaog() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_confirm, null);
        TextView confirmTV = (TextView) view.findViewById(R.id.tv_confirm);
        TextView cancelTV = (TextView) view.findViewById(R.id.tv_cancel);
        final CustomDialogManager cdm = new CustomDialogManager(mActivity, view);
        confirmTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readAllMessage();
                cdm.dismiss();
            }
        });
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cdm.dismiss();
            }
        });

        cdm.dg.setCanceledOnTouchOutside(true);
        cdm.dg.getWindow().setGravity(Gravity.CENTER);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        cdm.showDialog();
    }

    private void readAllMessage() {
        mIProtocol.notifyData();
        mAdapter.notifyDataSetChanged();
        getDataFromServer(true, HttpRequest.PUT, HelpTools.getUrl(CommonConfig.READ_ALL_MESSAGES_URL), null, new RequestCallback() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
                LogUtil.logXXfigo("READ_ALL_MESSAGES_URL  " + response);
            }
        });
    }

    public void searchProduct(String searchContent, String categoryName, String order) {
        mCurrentState = DRAG_REFRESH;
        mCurrentSearchContent = searchContent;
        mCurrentCategoryName = categoryName;
        mCurrentOrder = order;
        getDataFromNet();
    }
}
