package com.robin8.rb.ui.indiana.protocol;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.okhttp.RequestParams;

/**
 * Created by Figo on 2016/6/30.
 */
public interface IProtocol {

    RequestParams getRequestParams();

    RequestParams getHeaderRequestParams();

    void parseHeaderJson(String json, int currentState);

    void parseJson(String json, int currentState);

    BaseRecyclerAdapter getAdapter();

    void setCurrentPage(int i);

    void showNumberSeletor();

    void notifyData();

}
