package com.robin8.rb.module.create.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.module.create.prenster.ArticleListsPresenter;
import com.robin8.rb.view.IRobinIndianaView;


/**
 * 文章列表
 */
public class ArticleListsActivity extends BaseActivity implements IRobinIndianaView {

    private RecyclerView mRecyclerView;
    private XRefreshView mXRefreshView;
    private TextView mBottomTv;
    private ArticleListsPresenter mPresenter;

    @Override
    public void setTitleView() {
    }

    @Override
    public void initView() {
        mTVRight.setVisibility(View.VISIBLE);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_income_detail, mLLContent, true);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_income_detail);
        mRecyclerView.setHasFixedSize(true);
        mXRefreshView = (XRefreshView) findViewById(R.id.xrefreshview);
        mBottomTv = (TextView) findViewById(R.id.tv_bottom);

        mPresenter = new ArticleListsPresenter(this, this);
        mPresenter.start();
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
        mPresenter.OnclickRightView();
    }

    @Override
    public void setTitleView(String text) {
        mTVCenter.setText(text);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public XRefreshView getXRefreshView() {
        return mXRefreshView;
    }

    @Override
    public View getLLNoData() {
        return null;
    }

    @Override
    public TextView getRightTv() {
        return mTVRight;
    }

    @Override
    public TextView getBottomTv() {
        return mBottomTv;
    }

    @Override
    public void setPageName(String name) {
        mPageName = name;
    }

    @Override
    public void finish() {
        mPresenter.finish();
        super.finish();
    }

}
