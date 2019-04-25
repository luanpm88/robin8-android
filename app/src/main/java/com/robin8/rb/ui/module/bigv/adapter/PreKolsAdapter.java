package com.robin8.rb.ui.module.bigv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robin8.rb.R;
import com.robin8.rb.ui.module.bigv.model.BigvDetailModel;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.ui.widget.CircleImageView;

import java.util.List;

/**
 Created by zc on 2019/1/21. */

public class PreKolsAdapter extends RecyclerView.Adapter<PreKolsAdapter.ViewHolder> {
    private Context context;
    private List<BigvDetailModel.CreationBean.SelectedKolsBean> list;

    public PreKolsAdapter(Context context, List<BigvDetailModel.CreationBean.SelectedKolsBean> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public PreKolsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pre_kols, parent, false);
        return (new ViewHolder(view));
    }

    @Override
    public void onBindViewHolder(PreKolsAdapter.ViewHolder holder, int position) {
        BigvDetailModel.CreationBean.SelectedKolsBean kolsBean = list.get(position);
        if (!TextUtils.isEmpty(kolsBean.getAvatar_url())){
            BitmapUtil.loadImage(context, kolsBean.getAvatar_url(), holder.ivPhoto);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivPhoto;

        public ViewHolder(View view) {
            super(view);
            ivPhoto = ((CircleImageView) view.findViewById(R.id.civ_image));
        }
    }
}
