package com.robin8.rb.module.mine.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.R;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.module.mine.model.ParticipateCrewListModel;
import com.robin8.rb.util.BitmapUtil;

import java.util.List;


public class ParticipateCrewListAdapter extends BaseRecyclerAdapter {
    private ViewHolder mViewHolder;
    private static final String TAG = ParticipateCrewListAdapter.class.getSimpleName();
    private List<ParticipateCrewListModel.CampaignInvitesBean> mDataList;
    private OnRecyclerViewListener onRecyclerViewListener;

    public static interface OnRecyclerViewListener {
        void onItemClick(int position);
    }

    public void setOnRecyclerViewListener(
            OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    public void setDataList(List<ParticipateCrewListModel.CampaignInvitesBean> list) {
        this.mDataList = list;
    }


    public ParticipateCrewListAdapter(List<ParticipateCrewListModel.CampaignInvitesBean> list) {
        this.mDataList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i, boolean isItem) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.particapate_list_item, null);
        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
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
        if (position < 0 || position >= mDataList.size()) {
            return;
        }

        ParticipateCrewListModel.CampaignInvitesBean entity = mDataList.get(position);
        if(entity == null){
            return;
        }

        BitmapUtil.loadImage(BaseApplication.getContext(),entity.getAvatar_url(),holder.imageView);
        holder.nameTv.setText(entity.getKol_name());
        holder.totalClickTv.setText(String.valueOf(entity.getTotal_click()));
        holder.countClickTv.setText(String.valueOf(entity.getAvail_click()));
        holder.position = position;
    }

    @Override
    public int getAdapterItemCount() {
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public int position;
        public TextView nameTv;
        public TextView totalClickTv;
        public TextView countClickTv;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv);
            nameTv = (TextView) itemView.findViewById(R.id.tv_name);

            View layout_total_click = itemView.findViewById(R.id.layout_total_click);
            TextView tv_item_name_total_click = (TextView) layout_total_click.findViewById(R.id.tv_item_name);
            totalClickTv = (TextView) layout_total_click.findViewById(R.id.tv_item_number);
            tv_item_name_total_click.setText(R.string.total_clicks);

            View layout_count_click = itemView.findViewById(R.id.layout_count_click);
            TextView tv_item_name_count_click = (TextView) layout_count_click.findViewById(R.id.tv_item_name);
            countClickTv = (TextView) layout_count_click.findViewById(R.id.tv_item_number);
            tv_item_name_count_click.setText(R.string.count_click);
        }

        @Override
        public void onClick(View v) {
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}
