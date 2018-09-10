package com.robin8.rb.module.create.protocol;

import android.app.Activity;
import android.content.Intent;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.activity.web.WebViewActivity;
import com.robin8.rb.base.BaseRecyclerViewPresenter;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.indiana.protocol.IProtocol;
import com.robin8.rb.module.create.adapter.ArticleListsAdapter;
import com.robin8.rb.module.create.model.ArticleListsModel;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.util.GsonTools;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Figo on 2016/6/30.
 */
public class ArticleListsProtocol implements IProtocol {

    private Activity mActivity;
    private List<Object> mDataList = new ArrayList<>();
    private List<Object> mTempList = new ArrayList<>();
    private ArticleListsAdapter mAdapter;
    private RequestParams mRequestParams;
    private String mTitle;
    private OnItemClickListener mListener;

    public ArticleListsProtocol(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public RequestParams getRequestParams() {
        mRequestParams = new RequestParams();
        mRequestParams.put("title", mTitle);
        mRequestParams.put("type", "discovery");
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
        mAdapter = new ArticleListsAdapter(mDataList, mActivity.getApplicationContext());
        mAdapter.setOnRecyclerViewListener(new ArticleListsAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                if (mDataList.get(position) == null) {
                    if (mListener != null) {
                        mListener.onItemClick();
                    }
                    return;
                }
                skipToDetail(position);
            }
        });
        return mAdapter;
    }

    private void skipToDetail(int position) {
        if (position < 0 || mDataList == null || mDataList.size() == 0 || position >= mDataList.size()) {
            return;
        }
        Object obj = mDataList.get(position);
        if (obj != null && obj instanceof ArticleListsModel.ArticlesBean) {
            ArticleListsModel.ArticlesBean bean = (ArticleListsModel.ArticlesBean) obj;
            Intent intent = new Intent(mActivity, WebViewActivity.class);
            intent.putExtra("from", SPConstants.ARTICLE_LIST);
            intent.putExtra("title", "详情");
            intent.putExtra("url", bean.getArticle_url());
            mActivity.startActivity(intent);
        }
    }


    @Override
    public void setCurrentPage(int i) {
    }

    @Override
    public void showNumberSeletor() {
    }

    @Override
    public void notifyData() {
    }

    @Override
    public void parseJson(String json, int currentState) {

        ArticleListsModel articleListsModel = GsonTools.jsonToBean(json, ArticleListsModel.class);
        if (articleListsModel != null && articleListsModel.getError() == 0) {
            List<ArticleListsModel.ArticlesBean> tempList = articleListsModel.getArticles();
            if (tempList != null && tempList.size() > 0) {
                if (currentState != BaseRecyclerViewPresenter.LOAD_MORE) {
                    mTempList.clear();
                    mTempList.addAll(mDataList);
                    mDataList.clear();
                    mDataList.addAll(tempList);
                    if (mTempList.size() > 0) {
                        mDataList.add(null);
                        mDataList.addAll(mTempList);
                    }
                }else {
                    mDataList.addAll(tempList);
                }
                mAdapter.setDataList(mDataList);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public interface OnItemClickListener {
        void onItemClick();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
}
