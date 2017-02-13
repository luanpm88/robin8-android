package com.robin8.rb.view;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Figo on 2016/6/24.
 */
public interface IBindSocialView {

    LinearLayout getLinearLayout();

    ListView getBindListView();

    void setBindSocialTV(int visible);
    TextView getBindSocialTV();
}
