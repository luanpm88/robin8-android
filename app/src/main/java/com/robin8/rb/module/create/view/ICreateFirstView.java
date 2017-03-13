package com.robin8.rb.module.create.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.andview.refreshview.XRefreshView;
import com.robin8.rb.ui.widget.RefreshFooterView;
import com.robin8.rb.ui.widget.RefreshHeaderView;
import com.robin8.rb.ui.widget.WProgressDialog;

public interface ICreateFirstView {

    RecyclerView getRecyclerView();
    XRefreshView getXRefreshView();
    RefreshHeaderView getRefreshHeaderView();
    RefreshFooterView getRefreshFooterView();
    LinearLayout getErrorView();
    WProgressDialog getWProgressDialog();
    View getLLNoData();
}
