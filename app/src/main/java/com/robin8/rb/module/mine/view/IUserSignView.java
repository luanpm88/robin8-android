package com.robin8.rb.module.mine.view;

import android.widget.TextView;

import com.robin8.rb.ui.widget.OtherGridView;

/**
 * Created by Figo on 2016/6/24.
 */
public interface IUserSignView {

//    mCircleImageView = (CircleImageView) view.findViewById(R.id.civ_image);
//    mHasSignedDaysTv = (TextView) view.findViewById(R.id.tv_has_signed_days);
//    mMonthTv = (TextView) view.findViewById(R.id.tv_month);
//    mMonthGv = (OtherGridView) view.findViewById(R.id.gv_month);
//

    void setCircleImageView(String url);
    void setHasSignedDaysTv(String text);

    void setUserNameTv(String text);

    void setMonthTv(String text);
    OtherGridView getMonthGv();
    TextView getBottomTv();
}
