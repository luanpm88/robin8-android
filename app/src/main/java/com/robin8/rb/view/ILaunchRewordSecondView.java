package com.robin8.rb.view;

import android.widget.TextView;

import com.robin8.rb.ui.widget.SwitchView;

/**
 * Created by Figo on 2016/6/24.
 */
public interface ILaunchRewordSecondView {

    void setImageView(String path);

    void setActivityTitleTv(String title);//活动标题

    void setActivityTimeTv(String text);//活动时间

    void setBrandInfoTv(String text);//活动简介

    void setCountWayTv(String text);//点击 | ¥0.5

    void setTotalConsumeTv(String text);//活动总预算

    void setAccountIncomeTv(String text);//¥1232（余额：¥1232）

    void setCountTv(String title);//总计

    TextView getPayInstantlyTv();

    SwitchView getViewSwitch();

    void setViewSwitch(boolean flag);

    TextView getRightTv();
}
