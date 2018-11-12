package com.robin8.rb.module.mine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.module.mine.model.MineShowModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 检查用户身份审核进度
 Created by zc on 2018/11/9. */

public class UserCheckAdapter extends BaseAdapter {
    private Context mContext;
    private List<MineShowModel.ReadListBean> list;

    public UserCheckAdapter(Context mContext, List<MineShowModel.ReadListBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.user_check_result_item, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        MineShowModel.ReadListBean checkBean = list.get(position);
        int size = list.size();
        if (size == 1) {
            holder.layoutIcon.setVisibility(View.GONE);
            holder.tvCheckContent.setVisibility(View.GONE);
            holder.tvCheckContentSimple.setVisibility(View.VISIBLE);
            holder.tvCheckContentSimple.setText(checkBean.getDsp());
        } else {
            holder.tvCheckContent.setVisibility(View.VISIBLE);
            holder.tvCheckContentSimple.setVisibility(View.GONE);
            holder.tvCheckContent.setText(checkBean.getDsp());
            holder.layoutIcon.setVisibility(View.VISIBLE);
            if (position == (size - 1)) {
                holder.line.setVisibility(View.GONE);
            } else {
                holder.line.setVisibility(View.VISIBLE);
            }
            if (checkBean.getState() == 1) {
                holder.imgPass.setImageResource(R.mipmap.icon_check_pass);
            } else if (checkBean.getState() == - 1) {
                holder.imgPass.setImageResource(R.mipmap.icon_check_pass_no);
            }

        }

        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.icon_check_result)
        ImageView imgPass;
        @Bind(R.id.tv_check_content)
        TextView tvCheckContent;
        @Bind(R.id.tv_check_content_simple)
        TextView tvCheckContentSimple;
        @Bind(R.id.layout_icon)
        LinearLayout layoutIcon;
        @Bind(R.id.line)
        LinearLayout line;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
