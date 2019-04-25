package com.robin8.rb.ui.indiana.protocol;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.R;
import com.robin8.rb.base.BaseRecyclerViewPresenter;
import com.robin8.rb.ui.indiana.activity.IndianaOrderPayActivity;
import com.robin8.rb.ui.indiana.adapter.DetailIndianaAdapter;
import com.robin8.rb.ui.indiana.model.DetailModel;
import com.robin8.rb.ui.indiana.model.IndianaDetailBean;
import com.robin8.rb.ui.indiana.model.OrderBean;
import com.robin8.rb.ui.model.BaseBean;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.ui.dialog.InstantlyIndianaDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Figo on 2016/6/30.
 */
public class DetailIndianaProtocol implements IProtocol {

    private final String code;
    private final TextView mBottomTv;
    private Activity mActivity;
    private List<BaseBean> mOrderList = new ArrayList<BaseBean>();
    private DetailIndianaAdapter mDetailIndianaAdapter;
    private int mCurrentPage = 1;
    private RequestParams mRequestParams;
    private RequestParams mHeaderRequestParams;
    private int mTotalPages;
    private float mKolAmount;
    private IndianaDetailBean.IndianaActivity mIndianaActivity;

    public DetailIndianaProtocol(Activity activity, String code, TextView bottomTv) {
        this.mActivity = activity;
        this.code = code;
        this.mBottomTv = bottomTv;
    }

    @Override
    public RequestParams getRequestParams() {
        LogUtil.LogShitou("夺宝？", "page=" + mCurrentPage + "  mTotalPages=" + mTotalPages);
        if (mTotalPages != 0 && mCurrentPage > mTotalPages) {
            return null;
        }
        if (mRequestParams == null) {
            mRequestParams = new RequestParams();
        }
        mRequestParams.put("page", mCurrentPage);
        mRequestParams.put("code", code);
        return mRequestParams;
    }

    @Override
    public RequestParams getHeaderRequestParams() {
        if (mHeaderRequestParams == null) {
            mHeaderRequestParams = new RequestParams();
        }
        mHeaderRequestParams.put("code", code);
        return mRequestParams;
    }

    @Override
    public BaseRecyclerAdapter getAdapter() {
        mDetailIndianaAdapter = new DetailIndianaAdapter(mOrderList);
        mDetailIndianaAdapter.setOnRecyclerViewListener(new DetailIndianaAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
            }
        });
        return mDetailIndianaAdapter;
    }


    @Override
    public void setCurrentPage(int i) {
        this.mCurrentPage = i;
    }

    @Override
    public void showNumberSeletor() {
        final InstantlyIndianaDialog dialog = new InstantlyIndianaDialog(mActivity, mKolAmount);
        dialog.setInstantlyIndianaListener(new InstantlyIndianaDialog.InstantlyIndianaListener() {
            @Override
            public void skipToPay(int times) {
                dialog.dismiss();
                Intent intent = new Intent(mActivity, IndianaOrderPayActivity.class);
                intent.putExtra("times", times);
                intent.putExtra("code", code);
                intent.putExtra("name", mIndianaActivity.getName());
                intent.putExtra("url", mIndianaActivity.getPoster_url());
                mActivity.startActivity(intent);
            }
        });
        dialog.show();
    }

    @Override
    public void notifyData() {

    }

    @Override
    public void parseJson(String json, int currentState) {
        LogUtil.LogShitou("parseJson", "mCurrentPage1=" + mCurrentPage + "  currentState1=" + currentState + json);
        DetailModel detailModel = GsonTools.jsonToBean(json, DetailModel.class);
        if (detailModel == null) {
            CustomToast.showShort(mActivity, mActivity.getString(R.string.please_data_wrong));
            return;
        }
        if (detailModel.getError() == 0) {
            mTotalPages = detailModel.getTotal_pages();
            List<OrderBean> tempList = detailModel.getOrders();
            if (tempList != null && tempList.size() > 0) {
                if (mCurrentPage<=mTotalPages) {
                    mCurrentPage++;
                    LogUtil.LogShitou("xxfigo", "mCurrentPage=" + mCurrentPage);
                    mOrderList.addAll(tempList);
                    mDetailIndianaAdapter.setDataList(mOrderList);
                    mDetailIndianaAdapter.notifyDataSetChanged();
                }
            }
        } else {
            CustomToast.showShort(mActivity, detailModel.getDetail());
        }
    }

    @Override
    public void parseHeaderJson(String json, int currentState) {
        LogUtil.LogShitou("parseHeaderJson", "json" + json);
        IndianaDetailBean indianaDetailBean = GsonTools.jsonToBean(json, IndianaDetailBean.class);

        if (indianaDetailBean == null) {
            CustomToast.showShort(mActivity, mActivity.getString(R.string.please_data_wrong));
            return;
        }

        if (indianaDetailBean.getError() == 0) {

            mKolAmount = indianaDetailBean.getKol_amount();
            mIndianaActivity = indianaDetailBean.getActivity();
            if (mBottomTv != null && TextUtils.equals(mIndianaActivity.getStatus(), "executing")) {
                mBottomTv.setVisibility(View.VISIBLE);
            } else {
                mBottomTv.setVisibility(View.GONE);
            }

            if (currentState != BaseRecyclerViewPresenter.LOAD_MORE) {
                mOrderList.clear();
                mOrderList.add(indianaDetailBean);
            }
            mDetailIndianaAdapter.setDataList(mOrderList);
            mDetailIndianaAdapter.notifyDataSetChanged();
        } else {
            CustomToast.showShort(mActivity, indianaDetailBean.getDetail());
        }
    }
}
