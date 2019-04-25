/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package com.robin8.rb.ui.module.first.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.robin8.rb.R;
import com.robin8.rb.ui.activity.web.WebViewActivity;
import com.robin8.rb.ui.widget.autoviewpager.RecyclingPagerAdapter;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.ui.module.first.activity.KolDetailContentActivity;
import com.robin8.rb.ui.module.first.model.KolAnnouncementsBean;
import com.robin8.rb.util.BitmapUtil;

import java.util.List;


/**
 * ImagePagerAdapter
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-23
 */
public class ImagePagerAdapter extends RecyclingPagerAdapter {

    private final List<KolAnnouncementsBean> kolAnnouncements;
    private Context context;
    private int size;
    private boolean isInfiniteLoop;

    public ImagePagerAdapter(Context context, List<KolAnnouncementsBean> kolAnnouncements) {
        this.context = context;
        this.kolAnnouncements = kolAnnouncements;
        this.size = kolAnnouncements.size();
        isInfiniteLoop = false;
    }

    @Override
    public int getCount() {
        return isInfiniteLoop ? Integer.MAX_VALUE : kolAnnouncements.size();
    }

    /**
     * get really position
     *
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop ? position % size : position;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup container) {
        ViewHolder holder;
        int p = isInfiniteLoop ? position % size : position;
        if (view == null) {
            holder = new ViewHolder();
            view = holder.imageView = new ImageView(context);
            view.setTag(R.id.ll_load_more, holder);
        } else {
            holder = (ViewHolder) view.getTag(R.id.ll_load_more);
        }
        BitmapUtil.loadImage(context.getApplicationContext(), kolAnnouncements.get(p).getCover_url(), holder.imageView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipToNext(position % size);
            }
        });
        return view;
    }

    public void skipToNext(int position) {

        if(kolAnnouncements==null || kolAnnouncements.size()<=0 || kolAnnouncements.size()<=position){
            return;
        }
        Intent intent = null;
        KolAnnouncementsBean kolAnnouncementsBean = kolAnnouncements.get(position);
        if(kolAnnouncementsBean!=null && kolAnnouncementsBean.getCategory()!=null){
            String category = kolAnnouncementsBean.getCategory();
            switch (category){
                case "link":
                    intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("title",kolAnnouncementsBean.getTitle());
                    intent.putExtra("url", kolAnnouncementsBean.getContent());
                    intent.putExtra("from", SPConstants.FIRST_PAGER);
                    break;
                case "kol":
                    intent = new Intent(context, KolDetailContentActivity.class);
                    intent.putExtra("id", Integer.parseInt(kolAnnouncementsBean.getContent()));
                    break;
            }
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private static class ViewHolder {
        ImageView imageView;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }
}
