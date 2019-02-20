package com.robin8.rb.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.model.BigvListModel;
import com.robin8.rb.module.bigv.activity.BigvCampaignDetailActivity;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.StringUtil;

import java.util.List;

/**
 Created by zc on 2019/1/7. */

public class BigvListAdapter extends BaseAdapter {

    private final List<BigvListModel.ListBean> mList;
    private final LayoutInflater mLayoutInflater;
    private final Context mActivity;
    private int width;

    public BigvListAdapter(Activity activity, List<BigvListModel.ListBean> list) {
        mActivity = activity;
        mList = list;
        mLayoutInflater = LayoutInflater.from(activity);
        DisplayMetrics displayMetrics = mActivity.getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public BigvListModel.ListBean getItem(int position) {

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
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_author);
            viewHolder.tvDec = (TextView) convertView.findViewById(R.id.tv_brand_dec);
            viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.tv_campaign_status);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_campaign_price);
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.img_brand_pic);
            int screenWidth = width;
            //            ViewGroup.LayoutParams lp = viewHolder.ivIcon.getLayoutParams();
            //            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            //            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //            viewHolder.ivIcon.setLayoutParams(lp);
            //            viewHolder.ivIcon.setMaxWidth(screenWidth);
            //            viewHolder.ivIcon.setMaxHeight(screenWidth/2);
            viewHolder.ivIcon.getLayoutParams().height = screenWidth / 2;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final BigvListModel.ListBean item = getItem(position);
        if (item == null) {
            return null;
        }
        viewHolder.tvName.setText(StringUtil.checkString(item.getName()));
        viewHolder.tvDec.setText(StringUtil.checkString(item.getDescription()));
        viewHolder.tvStatus.setText(StringUtil.checkString(item.getStatus_zh()));
        viewHolder.tvPrice.setText(StringUtil.checkString(item.getPrice_range()));

        if (! TextUtils.isEmpty(item.getImg_url())) {
            BitmapUtil.loadImage(mActivity, item.getImg_url(), viewHolder.ivIcon);
        }

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (BaseApplication.isDoubleClick()) {
                    return;
                }
                Intent intent = new Intent(mActivity, BigvCampaignDetailActivity.class);
                intent.putExtra(BigvCampaignDetailActivity.CAMPAIGN_ID, item.getId());
                LogUtil.LogShitou("查看传送的id","+====>"+item.getId());
                mActivity.startActivity(intent);
                //  overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
        return convertView;
    }

    public void notifyDataSetChanged(List<BigvListModel.ListBean> listModelList) {

        notifyDataSetChanged();
    }

    static class ViewHolder {
        public TextView tvName;
        private TextView tvDec;
        private TextView tvStatus;
        private TextView tvPrice;
        private ImageView ivIcon;
    }
}