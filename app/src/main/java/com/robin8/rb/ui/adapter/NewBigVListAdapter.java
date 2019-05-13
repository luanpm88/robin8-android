package com.robin8.rb.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.ui.model.BigvListModel;
import com.robin8.rb.ui.module.bigv.activity.BigvCampaignDetailActivity;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.DateUtil;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.StringUtil;

import java.util.List;

/**
 * Created by Figo on 2016/6/30.
 */
public class NewBigVListAdapter extends BaseAdapter {

    private final List<BigvListModel.ListBean> mList;
    private final LayoutInflater mLayoutInflater;
    private final Context mActivity;

    public NewBigVListAdapter(Activity activity, List<BigvListModel.ListBean> list) {
        mList = list;
        mActivity = activity;
        mLayoutInflater = LayoutInflater.from(activity);

    }

    @Override
    public int getCount() {

        return mList.size();
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
            convertView = mLayoutInflater.inflate(R.layout.item_new_bigv_list, null, false);
            viewHolder = new ViewHolder();
            viewHolder.payinfoLinearLayout = (LinearLayout) convertView.findViewById(R.id.ll_payinfo);
            viewHolder.ivImageView = (ImageView) convertView.findViewById(R.id.iv_bg);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_remainder = (TextView) convertView.findViewById(R.id.tv_remainder);
            viewHolder.tv_alias = (TextView) convertView.findViewById(R.id.tv_alias);
            viewHolder.tv_actiontype = (TextView) convertView.findViewById(R.id.tv_actiontype);//类型
            viewHolder.tv_over = (TextView) convertView.findViewById(R.id.tv_over);
            viewHolder.tv_last = (TextView) convertView.findViewById(R.id.tv_last);//最多可赚
            viewHolder.fl_content = convertView.findViewById(R.id.fl_content);
            viewHolder.tv_count_down = (TextView) convertView.findViewById(R.id.tv_count_down);
            viewHolder.ll_show_money = (LinearLayout) convertView.findViewById(R.id.ll_show_money);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final BigvListModel.ListBean item = getItem(position);
        if (item == null) {
            return null;
        }
        if (! TextUtils.isEmpty(item.getImg_url())) {
            BitmapUtil.loadImage(mActivity.getApplicationContext(), item.getImg_url(),  viewHolder.ivImageView, R.color.sub_gray_custom);
        }
        viewHolder.tv_alias.setText(StringUtil.checkString(item.getBrand_info().getName()));
        viewHolder.tv_title.setText(StringUtil.checkString(item.getName()));
        viewHolder.tv_over.setText(StringUtil.checkString(item.getStatus_zh()));
        viewHolder.tv_remainder.setText(StringUtil.checkString(item.getPrice_range()));
        viewHolder.tv_count_down.setText(StringUtil.checkString(DateUtil.formatTime("yyyy-MM-dd", StringUtil.checkString(item.getStart_at()),"yyyy-MM-dd") + " - " + DateUtil.formatTime("yyyy-MM-dd", StringUtil.checkString(item.getEnd_at()),"yyyy-MM-dd")));
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

            }
        });
        return convertView;
    }

    public void notifyDataSetChanged(List<BigvListModel.ListBean> mInviteEntityList) {

        notifyDataSetChanged();
    }

    static class ViewHolder {

        public LinearLayout payinfoLinearLayout;
        public ImageView ivImageView;
        public TextView tv_title;
        public TextView tv_remainder;
        public TextView tv_actiontype;
        public TextView tv_alias;
        public TextView tv_over;
        public TextView tv_last;
        public View fl_content;
        public TextView tv_count_down;//倒计时
        public LinearLayout ll_show_money;
    }

}