/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package com.robin8.rb.ui.module.first.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.ui.widget.OtherGridView;
import com.robin8.rb.util.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * ImagePagerAdapter
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-23
 */
public class TagItemPagerAdapter extends PagerAdapter {

    private final Context mContext;
    private ItemOnclickListener mListener;
    private String[] totalStr;
    private String[] totalStrEn;
    private HashMap<String, String> mHashMap = new HashMap<>();

    public interface ItemOnclickListener {
        void onClick(String tagNameCN, String tagNameEN);
    }

    public void setOnclickListener(ItemOnclickListener listener) {
        this.mListener = listener;
    }

    private ArrayList<ItemListBean> mData = new ArrayList<>();
    private LayoutInflater inflater;
    private int size;

    private int[] id1 = {R.mipmap.icon_item_internet, R.mipmap.icon_item_beauty, R.mipmap.icon_item_babies,
            R.mipmap.entertainment, R.mipmap.icon_item_travel, R.mipmap.icon_item_education,
            R.mipmap.icon_item_fashion, R.mipmap.icon_item_games};
    private int[] id2 = {R.mipmap.icon_item_realestate, R.mipmap.icon_item_finance, R.mipmap.icon_item_digital,
            R.mipmap.icon_item_appliances, R.mipmap.icon_item_health, R.mipmap.icon_item_books,
            R.mipmap.icon_item_sports, R.mipmap.icon_item_airline};
    private int[] id3 = {R.mipmap.icon_item_furniture, R.mipmap.icon_item_auto,
            R.mipmap.icon_item_hotel, R.mipmap.icon_item_camera, R.mipmap.icon_item_food,
            R.mipmap.icon_item_fitness, R.mipmap.icon_item_music, R.mipmap.icon_item_overall};

    public TagItemPagerAdapter(Context context) {
        mContext = context;
        initData();
        this.size = mData == null ? 0 : mData.size();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE/2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int p = position % size;
        View inflate = inflater.inflate(R.layout.first_tag_item, null);
        OtherGridView otherGridView = (OtherGridView) inflate.findViewById(R.id.other_grid_view);
        TextView tv = (TextView) inflate.findViewById(R.id.tv);
        tv.setText(String.valueOf(p + 1) + "/" + size);
        initGridView(otherGridView, mData.get(p).str, mData.get(p).id);
        container.addView(inflate);
        return inflate;
    }

    private void initGridView(OtherGridView otherGridView, final String[] stringArr, final int[] resIdArray) {
        if (otherGridView != null) {
            otherGridView.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return stringArr == null ? 0 : stringArr.length;
                }

                @Override
                public Object getItem(int position) {
                    return null;
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    ViewHolder holder;
                    if (convertView == null) {
                        holder = new ViewHolder();
                        convertView = inflater.inflate(R.layout.item_first_tag, null);
                        holder.tvItem = (TextView) convertView.findViewById(R.id.tv_item);
                        convertView.setTag(holder);
                    } else {
                        holder = (ViewHolder) convertView.getTag();
                    }

                    holder.tvItem.setText(stringArr[position]);
                    holder.tvItem.setTextColor(UIUtils.getColor(R.color.sub_black_custom));
                    Drawable drawable = holder.tvItem.getContext().getResources().getDrawable(resIdArray[position]);
                    holder.tvItem.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mListener != null) {
                                mListener.onClick(stringArr[position],mHashMap.get(stringArr[position]));
                            }
                        }
                    });
                    return convertView;
                }
            });
        }
    }

    private void initData() {

        totalStr = mContext.getResources().getStringArray(R.array.first_page_tag_item_total);
        totalStrEn = mContext.getResources().getStringArray(R.array.first_page_tag_item_total_en);
        String[] str1 = mContext.getResources().getStringArray(R.array.first_page_tag_item1);
        String[] str2 = mContext.getResources().getStringArray(R.array.first_page_tag_item2);
        String[] str3 = mContext.getResources().getStringArray(R.array.first_page_tag_item3);
        mData.clear();
        mData.add(new ItemListBean(str1, id1));
        mData.add(new ItemListBean(str2, id2));
        mData.add(new ItemListBean(str3, id3));

        mHashMap.clear();
        for (int i = 0; i < totalStr.length; i++) {
            mHashMap.put(totalStr[i],totalStrEn[i]);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);// 移除Item
    }

    class ViewHolder {
        public TextView tvItem;
    }

    class ItemListBean {
        public ItemListBean(String[] str, int[] id) {
            this.str = str;
            this.id = id;
        }

        public String[] str;
        public int[] id;
    }
}
