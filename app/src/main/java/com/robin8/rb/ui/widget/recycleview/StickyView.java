package com.robin8.rb.ui.widget.recycleview;

import android.view.View;

/**
 *
 *  获取吸附View相关的信息
 */

public interface StickyView {

    /**
     * 是否是吸附view
     * @param view
     * @return
     */
    boolean isStickyView(View view);

    /**
     * 得到吸附view的itemType
     * @return
     */
    int getStickViewType();
}
