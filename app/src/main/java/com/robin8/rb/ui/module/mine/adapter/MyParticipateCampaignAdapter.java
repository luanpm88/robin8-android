package com.robin8.rb.ui.module.mine.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.R;
import com.robin8.rb.ui.adapter.ViewPagerAdapter;
import com.robin8.rb.ui.module.mine.model.MyCampaignModel;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.util.UIUtils;

import java.util.List;


public class MyParticipateCampaignAdapter extends BaseRecyclerAdapter {

    private ViewHolder mViewHolder;
    private ViewPagerAdapter.SelectItem data;
    private int type;

    public void setData(ViewPagerAdapter.SelectItem data) {
        this.data = data;
    }

    public interface OnRecyclerViewListener {
        void onItemClick(int position);
        void onContentClick(int position);
        void onReasonClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private static final String TAG = MyParticipateCampaignAdapter.class.getSimpleName();

    public void setDataList(List<MyCampaignModel.MyCampaignsBean> list) {
        this.mDataList = list;
    }

    public void setmBigvDataList(List<MyCampaignModel.ListBean> mBigvDataList) {
        this.mBigvDataList = mBigvDataList;
    }

    private List<MyCampaignModel.MyCampaignsBean> mDataList;
    private List<MyCampaignModel.ListBean> mBigvDataList;

    public MyParticipateCampaignAdapter(List<MyCampaignModel.MyCampaignsBean> list, List<MyCampaignModel.ListBean> listBigV, int type) {
        this.mDataList = list;
        this.mBigvDataList = listBigV;
        this.type = type;
    }

    public MyParticipateCampaignAdapter(List<MyCampaignModel.MyCampaignsBean> list, int type) {
        this.mDataList = list;
        this.type = type;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i, boolean isItem) {
        View view = null;
        if (type == 0) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_particapte_campaign_list_item, null);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_bigv_campaign_list_item, null);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        mViewHolder = new ViewHolder(view);
        return mViewHolder;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return mViewHolder;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position, boolean isItem) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.position = position;
        MyCampaignModel.MyCampaignsBean campaignInviteEntity = null;
        MyCampaignModel.ListBean campaignBigvEntity = null;
        if (type == 0) {
            campaignInviteEntity = mDataList.get(position);
            if (campaignInviteEntity == null) {
                return;
            }
        } else {
            campaignBigvEntity = mBigvDataList.get(position);
            if (campaignBigvEntity == null) {
                return;
            }
        }

        if (type == 0) {
            holder.nameTv.setText(campaignInviteEntity.getCampaign_name());
            String invite_status = campaignInviteEntity.getImg_status();
            String status = campaignInviteEntity.getStatus();
            if (TextUtils.isEmpty(invite_status) || TextUtils.isEmpty(status)) {
                holder.llReason.setVisibility(View.VISIBLE);
                holder.tvReason.setVisibility(View.GONE);
                holder.topTv.setText(R.string.earning);
                holder.topTv.setTextColor(UIUtils.getColor(R.color.gray_custom));
                holder.bottomTv.setText(StringUtil.deleteZero(campaignInviteEntity.getEarn_money()) + "₫");
                holder.bottomTv.setTextColor(UIUtils.getColor(R.color.gray_custom));
                holder.llReason.setClickable(false);
                holder.nameTv.setClickable(false);
            } else {
                if (status.equals("settled") && invite_status.equals("passed")) {
                    holder.llReason.setVisibility(View.VISIBLE);
                    holder.tvReason.setVisibility(View.GONE);
                    holder.topTv.setText(R.string.earned);
                    holder.topTv.setTextColor(UIUtils.getColor(R.color.gray_custom));
                    holder.bottomTv.setText(StringUtil.deleteZero(campaignInviteEntity.getEarn_money()) + "₫");
                    holder.bottomTv.setTextColor(UIUtils.getColor(R.color.gray_custom));
                    holder.topTv.setTextColor(UIUtils.getColor(R.color.gray_custom));
                    holder.llReason.setClickable(false);
                    holder.nameTv.setClickable(false);
                } else if (status.equals("approved") || status.equals("finished")) {
                    if (invite_status.equals("passed")) {
                        holder.topTv.setText(R.string.ending);
                        holder.llReason.setVisibility(View.VISIBLE);
                        holder.tvReason.setVisibility(View.GONE);
                        holder.topTv.setTextColor(UIUtils.getColor(R.color.gray_custom));
                        holder.bottomTv.setText(StringUtil.deleteZero(campaignInviteEntity.getEarn_money()) + "₫");
                        holder.bottomTv.setTextColor(UIUtils.getColor(R.color.gray_custom));
                    } else if (invite_status.equals("rejected")) {
                        holder.llReason.setVisibility(View.GONE);
                        holder.tvReason.setVisibility(View.VISIBLE);
                        if (TextUtils.isEmpty(campaignInviteEntity.getReject_reason())) {
                            if (TextUtils.isEmpty(campaignInviteEntity.getScreenshot())) {
                                holder.tvReason.setText(R.string.robin317);
                            } else {
                                holder.tvReason.setText(R.string.robin318);
                            }
                        } else {
                            holder.tvReason.setText(holder.bottomTv.getContext().getString(R.string.robin515) + campaignInviteEntity.getReject_reason());
                        }
                    } else {
                        holder.topTv.setText(R.string.earning);
                        holder.llReason.setVisibility(View.VISIBLE);
                        holder.tvReason.setVisibility(View.GONE);
                        holder.topTv.setTextColor(UIUtils.getColor(R.color.gray_custom));
                        holder.bottomTv.setText(StringUtil.deleteZero(campaignInviteEntity.getEarn_money()) + "₫");
                        holder.bottomTv.setTextColor(UIUtils.getColor(R.color.gray_custom));
                    }

                    holder.llReason.setClickable(false);
                    holder.nameTv.setClickable(false);
                } else if (invite_status.equals("rejected")) {
                    holder.llReason.setVisibility(View.GONE);
                    holder.tvReason.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(campaignInviteEntity.getReject_reason())) {
                        if (TextUtils.isEmpty(campaignInviteEntity.getScreenshot())) {
                            holder.tvReason.setText(R.string.robin317);
                        } else {
                            holder.tvReason.setText(R.string.robin318);
                        }
                    } else {
                        holder.tvReason.setText(holder.bottomTv.getContext().getString(R.string.robin515) + campaignInviteEntity.getReject_reason());
                    }
                } else {
                    holder.llReason.setVisibility(View.VISIBLE);
                    holder.tvReason.setVisibility(View.GONE);
                    holder.topTv.setText(R.string.earning);
                    holder.topTv.setTextColor(UIUtils.getColor(R.color.gray_custom));
                    holder.bottomTv.setText(StringUtil.deleteZero(campaignInviteEntity.getEarn_money()) + "₫");
                    holder.bottomTv.setTextColor(UIUtils.getColor(R.color.gray_custom));
                    holder.llReason.setClickable(false);
                    holder.nameTv.setClickable(false);
                }
            }
        } else if (type == 1) {
            //大V
            holder.nameTv.setText(campaignBigvEntity.getCreation_name());
            holder.tvType.setText(campaignBigvEntity.getStatus_zh());
            holder.topTv.setText(campaignBigvEntity.getTender_info());
            holder.bottomTv.setVisibility(View.GONE);

        }

        //        if (TextUtils.isEmpty(campaignInviteEntity.getPer_action_type())){
        //            holder.tvType.setVisibility(View.GONE);
        //        }else {
        //            if (!campaignInviteEntity.getPer_action_type().equals("wecaht")){
        //                holder.tvType.setVisibility(View.VISIBLE);
        //                holder.tvType.setText(" (微博)");
        //            }else {
        //                holder.tvType.setVisibility(View.GONE);
        //            }
        //        }
    }


    @Override
    public int getAdapterItemCount() {
        if (type == 1) {
            return mBigvDataList.size();
        } else {
            return mDataList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvType;
        public TextView tvReason;
        public TextView topTv;
        public TextView nameTv;
        public TextView bottomTv;
        public int position;
        public LinearLayout llReason;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.tv_name);
            topTv = (TextView) itemView.findViewById(R.id.tv_top);
            bottomTv = (TextView) itemView.findViewById(R.id.tv_bottom);
            if (type == 0) {
                llReason = (LinearLayout) itemView.findViewById(R.id.ll_reason);
                tvReason = ((TextView) itemView.findViewById(R.id.tv_reason));
                llReason.setOnClickListener(this);
            }
            tvType = ((TextView) itemView.findViewById(R.id.tv_share_type));
            itemView.setOnClickListener(this);
            nameTv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                if (v.getId() == R.id.ll_reason) {
                    onRecyclerViewListener.onReasonClick(position);
                } else if (v.getId() == R.id.tv_name) {
                    onRecyclerViewListener.onContentClick(position);
                } else {
                    onRecyclerViewListener.onItemClick(position);
                }
            }
        }

    }
}
