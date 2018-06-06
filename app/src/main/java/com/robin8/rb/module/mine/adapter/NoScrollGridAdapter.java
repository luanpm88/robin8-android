package com.robin8.rb.module.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.module.mine.model.SignDaysModel;
import com.robin8.rb.util.ScreenUtil;
import com.robin8.rb.util.StringUtil;

import java.util.List;

/**
 Created by zc on 2018/4/23. */

public class NoScrollGridAdapter extends BaseAdapter {
    private final int screenWidth;
    private Context ctx;
    private int s;
    private List<SignDaysModel> list;

    public List<Boolean> getmSignList() {
        return mSignList;
    }

    public void setmSignList(List<Boolean> mSignList) {
        this.mSignList = mSignList;
    }

    private List<Boolean> mSignList;

    public NoScrollGridAdapter(Context ctx, List<SignDaysModel> list) {
        this.ctx = ctx;
        this.list = list;
        screenWidth = ScreenUtil.getScreenWidth(ctx);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // int viewWidth = screenWidth / 7;
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_sign_days, null);
            // AbsListView.LayoutParams params = new AbsListView.LayoutParams(viewWidth, viewWidth);
            // convertView.setLayoutParams(params);
            holder = new ViewHolder();
            holder.tvDays = (TextView) convertView.findViewById(R.id.tv_day);
            holder.tvWeek = (TextView) convertView.findViewById(R.id.tv_week);
            holder.llSeven = (RelativeLayout) convertView.findViewById(R.id.ll_seven);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SignDaysModel daysModel = list.get(position);

        if (getmSignList() != null && getmSignList().size() != 0) {
            if (getmSignList().get(position) == true) {
                if (position==6){
                    holder.tvDays.setVisibility(View.VISIBLE);
                    holder.llSeven.setVisibility(View.GONE);
                }
                holder.tvDays.setBackgroundResource(R.drawable.shape_bg_circle_blue);
                holder.tvDays.setTextColor(ctx.getResources().getColor(R.color.white_custom));
            } else {
                if (position==6){
                    holder.tvDays.setVisibility(View.GONE);
                    holder.llSeven.setVisibility(View.VISIBLE);
                }
                holder.tvDays.setBackgroundResource(R.drawable.shape_circle_white);
                holder.tvDays.setTextColor(ctx.getResources().getColor(R.color.gray_first));
            }
        }
        holder.tvDays.setText(StringUtil.addZeroForNum(String.valueOf(list.get(position).getRewards()), 4));
        holder.tvWeek.setText(String.valueOf(list.get(position).getWeek()));
        return convertView;

    }

    class ViewHolder {
        private TextView tvDays;
        private TextView tvWeek;
        private RelativeLayout llSeven;
    }
}
