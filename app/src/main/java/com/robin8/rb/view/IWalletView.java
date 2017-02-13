package com.robin8.rb.view;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Figo on 2016/6/24.
 */
public interface IWalletView {

    void setTVTotalIncome(CharSequence text);
    void setTVWasItemNumber(CharSequence text);

    void setTVWasConsumeItemNumber(CharSequence text);

    void setTVBeingItemNumber(CharSequence text);
    void setTVBeableItemNumber(CharSequence text);
    void setTVIncomes(CharSequence text);
    void setTVTotal(String text);
    void setLLbottom(boolean clickEnable);

    LineChartView getLineChartView();
}
