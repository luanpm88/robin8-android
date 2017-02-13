package com.robin8.rb.module.mine.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.R;
import com.robin8.rb.adapter.ViewPagerAdapter;
import com.robin8.rb.model.CampaignListBean;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.util.UIUtils;

import java.util.List;


public class MyParticipateCampaignAdapter extends BaseRecyclerAdapter {

    private ViewHolder mViewHolder;
    private ViewPagerAdapter.SelectItem data;

    public void setData(ViewPagerAdapter.SelectItem data) {
        this.data = data;
    }

    public  interface OnRecyclerViewListener {
        void onItemClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(
            OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private static final String TAG = MyParticipateCampaignAdapter.class.getSimpleName();

    public void setDataList(List<CampaignListBean.CampaignInviteEntity> list) {
        this.mDataList = list;
    }

    private List<CampaignListBean.CampaignInviteEntity> mDataList;

    public MyParticipateCampaignAdapter(List<CampaignListBean.CampaignInviteEntity> list) {
        this.mDataList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i, boolean isItem) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_particapte_campaign_list_item, null);
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
        CampaignListBean.CampaignInviteEntity campaignInviteEntity = mDataList.get(position);
        if (campaignInviteEntity == null) {
            return;
        }

        holder.nameTv.setText(campaignInviteEntity.getCampaign().getName());
        String invite_status = campaignInviteEntity.getInvite_status();
        if("rejected".equals(invite_status)){
            holder.topTv.setText("审核拒绝");
            holder.topTv.setTextColor(UIUtils.getColor(R.color.red_custom));
        }else if("completed".equals(data.campaignType)) {
            holder.topTv.setText("已赚");
            holder.topTv.setTextColor(UIUtils.getColor(R.color.gray_custom));
        }else {
            holder.topTv.setText("即将赚");
            holder.topTv.setTextColor(UIUtils.getColor(R.color.gray_custom));
        }
        holder.bottomTv.setText("¥ "+ StringUtil.deleteZero(campaignInviteEntity.getEarn_money()));
    }


    @Override
    public int getAdapterItemCount() {
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView topTv;
        public TextView nameTv;
        public TextView bottomTv;
        public int position;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.tv_name);
            topTv = (TextView) itemView.findViewById(R.id.tv_top);
            bottomTv = (TextView) itemView.findViewById(R.id.tv_bottom);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener.onItemClick(position);
            }
        }

    }
}
