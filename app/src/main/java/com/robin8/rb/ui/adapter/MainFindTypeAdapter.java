package com.robin8.rb.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.robin8.rb.R;

import java.util.List;

/**
 Created by zc on 2018/3/28. */

public class MainFindTypeAdapter extends BaseAdapter {
    private List<List<String>> mDataList;
    private Context context;
    private OnItemViewListener onItemViewListener;
    private int select = -1;

    public MainFindTypeAdapter(Context context, List<List<String>> mList) {
        this.context = context;
        this.mDataList = mList;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_find_type, viewGroup,false);
            convertView.setTag(new Holder(convertView));
        }
        final Holder holder = (Holder) convertView.getTag();
        holder.position = position;
        final List<String> list = mDataList.get(position);
        holder.tvType.setText(list.get(1));
        if (select==position){
            holder.tvType.setChecked(true);
        }else {
            holder.tvType.setChecked(false);
        }
        holder.tvType.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (null != onItemViewListener) {
                    onItemViewListener.onItemClick(position);
                }
            }
        });
        return convertView;
    }

    class Holder {
        public int position;

        public RadioButton tvType;

        Holder(View view) {
            tvType = ((RadioButton) view.findViewById(R.id.btn_find_type));
        }


    }

    public static interface OnItemViewListener {
        void onItemClick(int position);
    }

    public void setOnRecyclerViewListener(OnItemViewListener onItemViewListener) {
        this.onItemViewListener = onItemViewListener;
    }


}
