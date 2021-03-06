package com.robin8.rb.ui.module.first.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.andview.refreshview.XRefreshView;
import com.robin8.rb.ui.widget.RefreshFooterView;
import com.robin8.rb.ui.widget.RefreshHeaderView;
import com.robin8.rb.ui.widget.WProgressDialog;

public interface ISearchKolView {

    RecyclerView getRecyclerView();
    XRefreshView getXRefreshView();
    RefreshHeaderView getRefreshHeaderView();
    RefreshFooterView getRefreshFooterView();
    WProgressDialog getWProgressDialog();
    View getLLNoData();

}
