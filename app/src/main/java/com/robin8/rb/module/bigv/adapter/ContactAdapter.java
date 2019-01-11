package com.robin8.rb.module.bigv.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.model.BigvListModel;

import java.util.List;

/**
 Created by zc on 2019/1/7. */

public class ContactListAdapter extends BaseAdapter {

    private final List<BigvListModel> mList;
    private final LayoutInflater mLayoutInflater;
    private final Context mActivity;


    public ContactListAdapter(Activity activity, List<BigvListModel> list) {

        mList = list;
        mActivity = activity;
        mLayoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {

        return mList.size();
    }

    @Override
    public BigvListModel getItem(int position) {

        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.bigv_list_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_brand_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final BigvListModel item = getItem(position);
        if (item == null) {
            return null;
        }
       // BitmapUtil.loadImage(mActivity.getApplicationContext(), item.getCampaign().getImg_url(), viewHolder.ivImageView, R.color.sub_gray_custom);
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (BaseApplication.isDoubleClick()) {
                    return;
                }
//                Intent intent = new Intent(mActivity, DetailContentActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("bean", item);
//                mActivity.startActivity(intent.putExtras(bundle));
            }
        });
        return convertView;
    }

    public void notifyDataSetChanged(List<BigvListModel> listModelList) {

        notifyDataSetChanged();
    }

    static class ViewHolder {
        public TextView tvName;
    }
}