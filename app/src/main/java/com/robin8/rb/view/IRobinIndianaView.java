package com.robin8.rb.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;

/**
 * Created by Figo on 2016/6/24.
 */
public interface IRobinIndianaView {
    void setTitleView(String text);
    RecyclerView getRecyclerView();
    XRefreshView getXRefreshView();
    View getLLNoData();
    TextView getRightTv();
    TextView getBottomTv();
    void setPageName(String name);
}
