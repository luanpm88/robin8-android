package com.robin8.rb.ui.module.social.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.ui.module.social.bean.SocialDataBean;
import com.robin8.rb.ui.widget.myprogress.RoundCornerProgressBar;

import java.util.List;

/**
 Created by zc on 2017/8/16. */

public class SocialDetailDataAdapter extends BaseAdapter{
    private List<SocialDataBean> mList;
    private Context mContext;

    public SocialDetailDataAdapter(List<SocialDataBean> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public SocialDataBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.social_detail_data_item, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        SocialDataBean bean = mList.get(position);
        holder.tvTypeName.setText(bean.getName());
        holder.tvRank.setText(bean.getBadge());
        holder.pro.setProgress(bean.getPro());
        return convertView;
    }
    private class ViewHolder{
        private final TextView tvTypeName;
        private final TextView tvRank;
        private final RoundCornerProgressBar pro;

        ViewHolder(View view){
            tvTypeName = (TextView)view.findViewById(R.id.tv_data_type_one);
            tvRank = ((TextView) view.findViewById(R.id.tv_rank_one));
            pro = ((RoundCornerProgressBar) view.findViewById(R.id.pro_one));

        }
    }
}
