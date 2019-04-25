package com.robin8.rb.ui.indiana.adapter;

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
import com.robin8.rb.ui.indiana.model.RobinIndianaModel;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.util.UIUtils;

import java.text.DecimalFormat;
import java.util.List;


public class RobinIndianaAdapter extends BaseRecyclerAdapter {
    private ViewHolder mViewHolder;
    private static final String TAG = RobinIndianaAdapter.class.getSimpleName();
    private List<RobinIndianaModel.IndianaActivity> mDataList;
    private OnRecyclerViewListener onRecyclerViewListener;

    public enum ITEM_TYPE {
        BIG_PIC,
        NORMAL
    }

    public static interface OnRecyclerViewListener {
        void onItemClick(int position);
    }

    public void setOnRecyclerViewListener(
            OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    public void setDataList(List<RobinIndianaModel.IndianaActivity> list) {
        this.mDataList = list;
    }


    public RobinIndianaAdapter(List<RobinIndianaModel.IndianaActivity> list) {
        this.mDataList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i, boolean isItem) {
        if (i == 0) {
            final ImageView iv = new ImageView(viewGroup.getContext());
            int screenWidth = DensityUtils.getScreenWidth(viewGroup.getContext());
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(screenWidth, screenWidth * 9 / 16);
            viewGroup.addView(iv, lp);
            return new ViewHolder2(iv);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.robin_indiana_list_item, null);
            final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(lp);
            mViewHolder = new ViewHolder(view);
            return mViewHolder;
        }
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return mViewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position, boolean isItem) {

        if (viewHolder instanceof ViewHolder2) {
            ViewHolder2 holder2 = (ViewHolder2) viewHolder;
            holder2.iv.setBackgroundResource(R.mipmap.pic_user_indiana);
        } else {
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.position = position;

            if (position < 0 || position >= mDataList.size()) {
                return;
            }
            RobinIndianaModel.IndianaActivity indianaActivity = mDataList.get(position);
            if (indianaActivity == null) {
                return;
            }
            float totalNumber = indianaActivity.getTotal_number();
            float actualNumber = indianaActivity.getActual_number();
            DecimalFormat df = new DecimalFormat("0.00");
            double d = actualNumber / totalNumber * 100;
            String textProgress = df.format(d);
            Context context = holder.nameTv.getContext();
            holder.nameTv.setText(indianaActivity.getName());
            holder.totalTv.setText(context.getString(R.string.total_need_times) + StringUtil.deleteZero(totalNumber));
            holder.progressTv.setText(context.getString(R.string.participated_schedule) + textProgress + context.getString(R.string.percent_sign));
            holder.compeletePb.setMax(10000);
            holder.compeletePb.setProgress((int) (d * 100));
            switch (indianaActivity.getStatus()) {
                case "executing":
                    holder.indianaTv.setText(context.getString(R.string.instantly_indiana));
                    holder.indianaTv.setTextColor(UIUtils.getColor(R.color.blue_custom));
                    holder.indianaTv.setEnabled(true);
                    break;
                case "finished":
                    holder.indianaTv.setText(context.getString(R.string.has_announced));
                    holder.indianaTv.setTextColor(UIUtils.getColor(R.color.sub_gray_custom));
                    holder.indianaTv.setEnabled(false);
                    break;
            }

            BitmapUtil.loadImage(holder.progressTv.getContext(), indianaActivity.getPoster_url(), holder.posterIv);
        }
    }

    @Override
    public int getAdapterItemCount() {
        return mDataList.size();
    }

    @Override
    public int getAdapterItemViewType(int position) {
        return position == 0 ? ITEM_TYPE.BIG_PIC.ordinal() : ITEM_TYPE.NORMAL.ordinal();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public int position;
        public TextView nameTv;
        public TextView totalTv;
        public TextView progressTv;
        public TextView indianaTv;
        public ImageView posterIv;
        public ProgressBar compeletePb;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.tv_name);
            totalTv = (TextView) itemView.findViewById(R.id.tv_total_times);
            progressTv = (TextView) itemView.findViewById(R.id.tv_progress);
            indianaTv = (TextView) itemView.findViewById(R.id.tv_indiana);
            posterIv = (ImageView) itemView.findViewById(R.id.iv_poster);
            compeletePb = (ProgressBar) itemView.findViewById(R.id.pb_compelete);
            itemView.setOnClickListener(this);
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

    class ViewHolder2 extends RecyclerView.ViewHolder {
        public ImageView iv;

        public ViewHolder2(View itemView) {
            super(itemView);
            if (itemView instanceof ImageView) {
                iv = (ImageView) itemView;
            }
        }
    }
}
