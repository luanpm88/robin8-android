package com.robin8.rb.ui.module.reword.banner;


import android.widget.RelativeLayout;

public interface CardAdapter {

    int MAX_ELEVATION_FACTOR = 8;

    float getBaseElevation();

    RelativeLayout getCardViewAt(int position);

    int getCount();
}
