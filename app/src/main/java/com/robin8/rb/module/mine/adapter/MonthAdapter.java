package com.robin8.rb.module.mine.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.module.mine.presenter.UserSignPresenter;

import java.util.ArrayList;
import java.util.List;


public class MonthAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<UserSignPresenter.SignDay> monthList = new ArrayList<UserSignPresenter.SignDay>();

    public MonthAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return monthList == null ? 0 : monthList.size();
    }

    @Override
    public UserSignPresenter.SignDay getItem(int position) {
        if (monthList != null && monthList.size() != 0) {
            return monthList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({ "ResourceAsColor", "ViewHolder" })
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.month_item, null);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (monthList.size() != 0) {
            if (monthList.get(position) != null) {
                UserSignPresenter.SignDay info = monthList.get(position);
                holder.tvDate.setText(info.day);

                if(info.isSign){
                    holder.tvDate.setBackgroundResource(R.drawable.shape_ring_sub_red);
                }else {
                    holder.tvDate.setBackgroundResource(android.R.color.transparent);
                }

            } else {
                holder.tvDate.setText("");
            }
        }

        return convertView;
    }

    public void setMonthList(List<UserSignPresenter.SignDay> mList) {
        monthList.clear();
        monthList.addAll(mList);
        notifyDataSetChanged();
    }

    public class ViewHolder {
        TextView tvDate;
    }

}