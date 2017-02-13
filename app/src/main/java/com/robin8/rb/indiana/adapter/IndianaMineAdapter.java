package com.robin8.rb.indiana.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.R;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.indiana.model.IndianaDetailBean;
import com.robin8.rb.util.BitmapUtil;

import java.text.DecimalFormat;
import java.util.List;


public class IndianaMineAdapter extends BaseRecyclerAdapter {
    private ViewHolder mViewHolder;

    public static interface OnRecyclerViewListener {
        void onItemClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(
            OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private static final String TAG = IndianaMineAdapter.class.getSimpleName();

    public void setDataList(List<IndianaDetailBean.IndianaActivity> list) {
        this.mDataList = list;
    }

    private List<IndianaDetailBean.IndianaActivity> mDataList;

    public IndianaMineAdapter(List<IndianaDetailBean.IndianaActivity> list) {
        this.mDataList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i, boolean isItem) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_indiana_list_item, null);
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
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.position = position;
        Context context = holder.nameTv.getContext();
        IndianaDetailBean.IndianaActivity indiana = mDataList.get(position);

        if(indiana == null){
            return;
        }
        holder.nameTv.setText(indiana.getName());
        holder.issueTv.setText(context.getString(R.string.this_issue)+indiana.getCode());
        holder.mineTimesTv.setText(context.getString(R.string.my_participated_times)+String.valueOf(indiana.getToken_number())+context.getString(R.string.person_times));
        BitmapUtil.loadImage(BaseApplication.getContext(), indiana.getPoster_url(), holder.posterIv);

        float totalNumber = indiana.getTotal_number();
        float actualNumber = indiana.getActual_number();
        DecimalFormat df = new DecimalFormat("0.00");
        double d = actualNumber / totalNumber * 100;
        String textProgress = df.format(d);
        holder.progressTv.setText(context.getString(R.string.participated_schedule) + textProgress + context.getString(R.string.percent_sign));
        holder.compeletePb.setMax(10000);
        holder.compeletePb.setProgress((int) (d * 100));

        switch (indiana.getStatus()) {
            case "executing":
                holder.executingLL.setVisibility(View.VISIBLE);
                holder.finishedLL.setVisibility(View.GONE);
                break;
            case "finished":
                holder.executingLL.setVisibility(View.GONE);
                holder.finishedLL.setVisibility(View.VISIBLE);
                holder.winnerNameTv.setText(indiana.getWinner_name());
                holder.winnerTimesTv.setText(indiana.getWinner_token_number());
                break;
        }
    }

    @Override
    public int getAdapterItemCount() {
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView posterIv;
        public ProgressBar compeletePb;
        public TextView progressTv;
        public TextView issueTv;
        public TextView mineTimesTv;
        public TextView nameTv;
        public int position;
        public View finishedLL;
        public View executingLL;
        public TextView winnerNameTv;
        public TextView winnerTimesTv;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            nameTv = (TextView) itemView.findViewById(R.id.tv_name);
            posterIv = (ImageView) itemView.findViewById(R.id.iv_poster);
            compeletePb = (ProgressBar) itemView.findViewById(R.id.pb_compelete);
            progressTv = (TextView) itemView.findViewById(R.id.tv_progress);
            issueTv = (TextView) itemView.findViewById(R.id.tv_issue);
            mineTimesTv = (TextView) itemView.findViewById(R.id.tv_mine_times);
            finishedLL =  itemView.findViewById(R.id.ll_finished);
            executingLL =  itemView.findViewById(R.id.ll_executing);
            winnerNameTv = (TextView) itemView.findViewById(R.id.tv_winner_name);
            winnerTimesTv = (TextView) itemView.findViewById(R.id.tv_winner_times);
        }

        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener.onItemClick(position);
            }
        }
    }
}
