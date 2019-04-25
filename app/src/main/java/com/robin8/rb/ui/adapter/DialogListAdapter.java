package com.robin8.rb.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.ui.model.DialogListBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 Created by zc on 2018/11/1. */

public class DialogListAdapter extends BaseAdapter {
    private List<DialogListBean> listBeen;
    private Context context;

    public DialogListAdapter(List<DialogListBean> listBeen, Context context) {
        this.listBeen = listBeen;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listBeen.size();
    }

    @Override
    public DialogListBean getItem(int position) {
        return listBeen.get(position);
    }


    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dialog_list_view_item, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.tvItem.setText(listBeen.get(position).getText());
        return convertView;
    }


    class ViewHolder {

        @Bind(R.id.tv_item)
        TextView tvItem;
        @Bind(R.id.line)
        View line;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
