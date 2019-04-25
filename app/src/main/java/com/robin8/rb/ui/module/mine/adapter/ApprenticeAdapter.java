package com.robin8.rb.ui.module.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.R;
import com.robin8.rb.ui.module.mine.model.CollectMoneyModel;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.ui.widget.CircleImageView;

import java.util.List;

/**
 Created by zc on 2018/3/8. */

public class ApprenticeAdapter extends BaseRecyclerAdapter {

    private ViewHolder mViewHolder;
    private List<CollectMoneyModel.ListBean> mDataList;
    private MessageAdapter.OnRecyclerViewListener onRecyclerViewListener;
    private Context context;
    private int type;
    public ApprenticeAdapter(int type,Context context, List<CollectMoneyModel.ListBean> mList) {
        this.type = type;
        this.context = context;
        this.mDataList = mList;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return mViewHolder;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ranking_list_item, null);
                final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(lp);
        mViewHolder = new ViewHolder(view);
        return mViewHolder;
    }
    public void setDataList(List<CollectMoneyModel.ListBean> list,int type) {
        this.type = type;
        this.mDataList = list;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position, boolean isItem) {
        ViewHolder holder = (ViewHolder) viewHolder;
        if (type==0){
            if (position>0){
                CollectMoneyModel.ListBean listBean = mDataList.get(position-1);
                if (listBean == null) {
                    return;
                }
            }
        }else {
            if (position < 0 || position >= mDataList.size()) {
                return;
            }
            CollectMoneyModel.ListBean listBean = mDataList.get(position);
            if (listBean == null) {
                return;
            }
        }
        if(type==0){
            if (position==0){
                holder.llHeader.setVisibility(View.VISIBLE);
                holder.llLayout.setVisibility(View.GONE);
                holder.line.setVisibility(View.GONE);
            }else {
                holder.llHeader.setVisibility(View.GONE);
                holder.llLayout.setVisibility(View.VISIBLE);
                holder.line.setVisibility(View.VISIBLE);
                if (position<4){
                    holder.imgRankIcon.setVisibility(View.VISIBLE);
                    holder.imgRankNum.setVisibility(View.GONE);
                }else {
                    holder.imgRankIcon.setVisibility(View.INVISIBLE);
                    holder.imgRankNum.setVisibility(View.VISIBLE);
                }
                if (position==1){
                    holder.imgRankIcon.setImageResource(R.mipmap.icon_rank_one);
                }else if (position==2){
                    holder.imgRankIcon.setImageResource(R.mipmap.icon_rank_two);
                }else if (position==3){
                    holder.imgRankIcon.setImageResource(R.mipmap.icon_rank_three);
                }else {
                    holder.imgRankNum.setText(String.valueOf(position));
                }
                BitmapUtil.loadImage(context.getApplicationContext(),mDataList.get(position-1).getAvatar_url(),holder.imgUserPhoto);
                holder.tvUserName.setText(mDataList.get(position-1).getKol_name());
                holder.tvUserCampaignNum.setText("已接活动数："+mDataList.get(position-1).getCampaign_invites_count());
                holder.tvCampaignMoney.setText(mDataList.get(position-1).getAmount());
            }
        }else {
            holder.llHeader.setVisibility(View.GONE);
            holder.llLayout.setVisibility(View.VISIBLE);
            holder.line.setVisibility(View.VISIBLE);
            holder.llIcon.setVisibility(View.GONE);
            BitmapUtil.loadImage(context.getApplicationContext(),mDataList.get(position).getAvatar_url(),holder.imgUserPhoto);
            if (TextUtils.isEmpty(mDataList.get(position).getKol_name())){
                holder.tvUserName.setText("");
            }else {
                holder.tvUserName.setText(mDataList.get(position).getKol_name());
            }
            holder.tvUserCampaignNum.setText("已接活动数："+mDataList.get(position).getCampaign_invites_count());
            holder.tvCampaignMoney.setText(mDataList.get(position).getAmount());
        }


    }

    @Override
    public int getAdapterItemCount() {
        if (this.type==0){
            if (mDataList.size()!=0){
                return mDataList.size()+1;
            }else {
                return mDataList.size();
            }
        }else {
            return mDataList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout llHeader;
        private final TextView imgRankNum;
        private final ImageView imgRankIcon;
        private final RelativeLayout llIcon;
        private final CircleImageView imgUserPhoto;
        private final TextView tvUserName;
        private final TextView tvUserCampaignNum;
        private final TextView tvCampaignMoney;
        private final RelativeLayout llLayout;
        private final View line;

        public ViewHolder(View itemView) {
            super(itemView);
            llHeader = ((LinearLayout) itemView.findViewById(R.id.ll_header));
            imgRankNum = ((TextView) itemView.findViewById(R.id.img_rank_num));
            imgRankIcon = ((ImageView) itemView.findViewById(R.id.img_rank_icon));
            llIcon = ((RelativeLayout) itemView.findViewById(R.id.ll_icon));
            imgUserPhoto = ((CircleImageView) itemView.findViewById(R.id.img_user_photo));
            tvUserName = ((TextView) itemView.findViewById(R.id.tv_user_name));
            tvUserCampaignNum = ((TextView) itemView.findViewById(R.id.tv_user_campaign_num));
            tvCampaignMoney = ((TextView) itemView.findViewById(R.id.tv_campaign_money));
            llLayout = ((RelativeLayout) itemView.findViewById(R.id.ll_layout));
            line = ((View) itemView.findViewById(R.id.line));
        }


    }
}
