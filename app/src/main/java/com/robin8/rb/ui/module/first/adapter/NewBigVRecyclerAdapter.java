package com.robin8.rb.ui.module.first.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.R;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.ui.module.first.model.BigVsBean;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.ui.widget.CircleImageView;

import java.util.List;

public class NewBigVRecyclerAdapter extends BaseRecyclerAdapter implements View.OnClickListener {
    private ViewHolder mViewHolder;
    private static final String TAG = NewBigVRecyclerAdapter.class.getSimpleName();
    private List<Object> mDataList;
    private OnRecyclerViewListener onRecyclerViewListener;


    public static interface OnRecyclerViewListener {
        void onItemClick(int position);
    }

    public void setOnRecyclerViewListener(
            OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    public void setDataList(List<Object> list) {
        this.mDataList = list;
    }


    public NewBigVRecyclerAdapter(List<Object> list) {
        this.mDataList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i, boolean isItem) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.new_bigv_list_item, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        mViewHolder = new ViewHolder(view);
        return mViewHolder;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return mViewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position, boolean isItem) {

        if (position < 0 || position >= mDataList.size()) {
            return;
        }

        Object obj = mDataList.get(position);
        if (obj == null) {
            return;
        }

        if (obj instanceof BigVsBean) {
            BigVsBean bean = (BigVsBean) obj;
            if (viewHolder instanceof ViewHolder) {
                ViewHolder holder = (ViewHolder) viewHolder;
                holder.position = position;
                final Context context = holder.imageCiv.getContext();
                holder.imageCiv.post(new MyRunnable(holder.imageCiv, context));
                BitmapUtil.loadImage(context, bean.getAvatar_url(), holder.imageCiv);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }


    @Override
    public int getAdapterItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public int position;
        public CircleImageView imageCiv;

        public ViewHolder(View itemView) {
            super(itemView);
            imageCiv = (CircleImageView) itemView.findViewById(R.id.civ_image);
            imageCiv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener.onItemClick(position);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    private class MyRunnable implements Runnable {

        private Context context;
        private View view;

        MyRunnable(View view, Context context) {
            this.view = view;
            this.context = context;
        }

        @Override
        public void run() {
            view.getLayoutParams().height =
                    (int) ((DensityUtils.getScreenWidth(context) - 6 * 10 * BaseApplication.mPixelDensityF) / 5);
            view.getLayoutParams().width =
                    (int) ((DensityUtils.getScreenWidth(context) - 6 * 10 * BaseApplication.mPixelDensityF) / 5);
        }
    }
}
