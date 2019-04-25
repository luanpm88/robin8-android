package com.robin8.rb.ui.module.find.view.snaprecyclerview;

/**
 * OnSnapListener
 *
 * @author takusemba
 * @since 30/07/2017
 **/
public interface OnSnapListener {

    /**
     * Called when RecyclerView is snapped
     *
     * @param position snapped position
     */
    void snapped(int position);
}
