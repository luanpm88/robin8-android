package com.robin8.rb.ui.module.bigv.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.robin8.rb.R;
import com.robin8.rb.ui.module.bigv.model.BigvDetailModel;
import com.robin8.rb.util.BitmapUtil;

import java.util.HashMap;
import java.util.List;

/**
 Created by zc on 2019/1/8. */

public class AddPriceAdapter extends BaseAdapter {

    private Context context;
    private List<BigvDetailModel.CreationBean.BaseInfoBean.TerraceInfosBean> list;

    public HashMap<Integer, String> contents = new HashMap<>();

    private int type = 0;

    public AddPriceAdapter(Context context, List<BigvDetailModel.CreationBean.BaseInfoBean.TerraceInfosBean> list,int type) {
        this.context = context;
        this.list = list;
        this.type = type;
    }


    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public BigvDetailModel.CreationBean.BaseInfoBean.TerraceInfosBean getItem(int position) {
        return list.get(position);
    }


    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if (convertView == null) {
            if (type==1){
                convertView = LayoutInflater.from(context).inflate(R.layout.item_add_price, parent, false);
            }else {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_upload_link, parent, false);
            }
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
            holder.etPrice.addTextChangedListener(new MyTextWatcher(holder, contents));

        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        holder.etPrice.setTag(position);
       // holder.etPrice.setTextIsSelectable(true);
        BigvDetailModel.CreationBean.BaseInfoBean.TerraceInfosBean bean = list.get(position);
        if (! TextUtils.isEmpty(bean.getAddress())) {
            BitmapUtil.loadImage(context, bean.getAddress(), holder.ivPlat);
        }
        if (type==1){
            holder.etPrice.setHint(context.getString(R.string.robin449,bean.getName()));
        }else {
            holder.etPrice.setHint(context.getString(R.string.robin450,bean.getName()));


        }
        if (! TextUtils.isEmpty(contents.get(position))) {//不为空的时候 赋值给对应的edittext
            holder.etPrice.setText(contents.get(position));
        } else {//置空
            holder.etPrice.getEditableText().clear();
        }
        return convertView;
    }

    class MyTextWatcher implements TextWatcher {

        public MyViewHolder holder;
        public HashMap<Integer, String> contents;

        public MyTextWatcher(MyViewHolder holder, HashMap<Integer, String> contents) {
            this.holder = holder;
            this.contents = contents;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (holder != null && contents != null) {
                int position = (int) holder.etPrice.getTag();
                contents.put(position, editable.toString());
            }
        }
    }

    class MyViewHolder {
        EditText etPrice;
        ImageView ivPlat;

        public MyViewHolder(View itemView) {
            ivPlat = ((ImageView) itemView.findViewById(R.id.iv_plat));
            etPrice = ((EditText) itemView.findViewById(R.id.edit_price));
        }
    }
}
