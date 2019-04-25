package com.robin8.rb.ui.module.mine.view;

import android.widget.TextView;

import com.robin8.rb.ui.module.social.view.LinearLayoutForListView;
import com.robin8.rb.ui.module.social.view.NoScrollGridView;

/**
 * Created by Figo on 2016/6/24.
 */
public interface IUserSignView {



    void setCircleImageView(String url);
    void setHasSignedDaysTv(String text);

    void setUserNameTv(String text);

    TextView mEarnAccumulatedTv();
    TextView getEarnTodayTv();
    NoScrollGridView getSignDayGv();
    LinearLayoutForListView getTasksLv();
    TextView signTv();
}
