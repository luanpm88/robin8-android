package com.robin8.rb.ui.module.create.protocol;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.R;
import com.robin8.rb.ui.activity.web.WebViewActivity;
import com.robin8.rb.base.BaseRecyclerViewPresenter;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.ui.indiana.protocol.IProtocol;
import com.robin8.rb.ui.module.create.adapter.ProductListAdapter;
import com.robin8.rb.ui.module.create.model.ProductListModel;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.util.GsonTools;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Figo on 2016/6/30.
 */
public class ProductListProtocol implements IProtocol {

    private static final String TYPE_ANNOUNCEMENT = "announcement";
    private static final String TYPE_COMMON = "common";
    private String mCategoryName;
    private int from;
    private Activity mActivity;
    private List<Object> mDataList = new ArrayList<>();
    private ProductListAdapter mAdapter;
    private int mCurrentPage = 1;
    private RequestParams mRequestParams;
    private int mTotalPages;
    private XRefreshView mXRefreshView;
    private View mLLNoData;

    public ProductListProtocol(Activity activity, String categoryName, int from) {
        this.mActivity = activity;
        this.mCategoryName = categoryName;
        this.from = from;
    }

    @Override
    public RequestParams getRequestParams() {

        if (mTotalPages != 0 && mCurrentPage > mTotalPages) {
            return null;
        }

        mRequestParams = new RequestParams();
        mRequestParams.put("page", mCurrentPage);
        if (!TextUtils.isEmpty(mCategoryName)) {
            mRequestParams.put("category_name", mCategoryName);
        }
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
        mAdapter = new ProductListAdapter(mDataList, mActivity.getApplicationContext());
        mAdapter.setOnRecyclerViewListener(new ProductListAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                if (from != SPConstants.ARTICLE_SHARE) {
                    skipToDetail(position);
                }
            }
        });
        return mAdapter;
    }

    private void skipToDetail(int position) {
        if (position < 0 || mDataList == null || mDataList.size() == 0 || position >= mDataList.size()) {
            return;
        }
        Object obj = mDataList.get(position);
        if (obj != null && obj instanceof ProductListModel.CpsMaterialsBean) {
            ProductListModel.CpsMaterialsBean bean = (ProductListModel.CpsMaterialsBean) obj;
            Intent intent = new Intent(mActivity, WebViewActivity.class);
            intent.putExtra("title", mActivity.getString(R.string.robin451));
            intent.putExtra("category", bean.getCategory());
            intent.putExtra("url", bean.getMaterial_url());
            intent.putExtra("bean", bean);
            intent.putExtra("from", from);
            mActivity.startActivity(intent);
        }
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
        if (mDataList == null || mDataList.size() == 0) {
            return;
        }
    }

    @Override
    public void parseJson(String json, int currentState) {

        ProductListModel productListModel = GsonTools.jsonToBean(json, ProductListModel.class);
        if (productListModel != null && productListModel.getError() == 0) {
            mTotalPages = productListModel.getTotal_pages();
            List<ProductListModel.CpsMaterialsBean> tempList = productListModel.getCps_materials();
            if (currentState != BaseRecyclerViewPresenter.LOAD_MORE) {
                mDataList.clear();
            }
            if (tempList != null && tempList.size() > 0) {
                mCurrentPage++;
                mDataList.addAll(tempList);
            }

            mAdapter.setDataList(mDataList);
            mAdapter.notifyDataSetChanged();

            if (currentState != BaseRecyclerViewPresenter.LOAD_MORE  && mDataList.size() == 0) {
                mXRefreshView.setVisibility(View.GONE);
                if (mLLNoData != null) {
                    mLLNoData.setVisibility(View.VISIBLE);
                }
            } else {
                mXRefreshView.setVisibility(View.VISIBLE);
                if (mLLNoData != null) {
                    mLLNoData.setVisibility(View.GONE);
                }
            }
        }
    }

    public void setView(View llNoData, XRefreshView mXRefreshView) {
        this.mXRefreshView = mXRefreshView;
        this.mLLNoData = llNoData;
    }
}
