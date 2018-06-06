package com.robin8.rb.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.R;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.model.LaunchRewordModel;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.DateUtil;
import com.robin8.rb.util.StringUtil;

import java.util.List;

public class MyCampaignAdapter extends BaseRecyclerAdapter {

    public static final String TYPE_RUNNING = "running";
    public static final String TYPE_COMPLETED = "completed";

    public static final String EVALUA_TING = "evaluating";
    public static final String EVALUAT_ED = "evaluated";
    private ViewHolder mViewHolder;
    private ViewPagerAdapter.SelectItem data;

    public void setData(ViewPagerAdapter.SelectItem data) {
        this.data = data;
    }

    public static interface OnRecyclerViewListener {
        void onItemClick(int position);

        void onItemCancelClick(int position);

        boolean onItemLongClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private static final String TAG = MyCampaignAdapter.class.getSimpleName();

    public void setDataList(List<LaunchRewordModel.Campaign> list) {
        this.mDataList = list;
    }

    private List<LaunchRewordModel.Campaign> mDataList;

    public MyCampaignAdapter(List<LaunchRewordModel.Campaign> list) {
        this.mDataList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i, boolean isItem) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_campaign_list_item, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
        LaunchRewordModel.Campaign campaign = mDataList.get(position);
        holder.dateTv.setText(DateUtil.formatTime("yyyy-MM-dd'T'HH:mm:ssZ", campaign.getStart_time()) + " - " + DateUtil.formatTime("yyyy-MM-dd'T'HH:mm:ssZ", campaign.getDeadline()));
        setPayInstantlyTv(holder.payInstantlyTv, campaign);
        setCancelTv(holder.cancelTv, holder.viewLine);
        setEvaluationState(campaign, holder.cancelTv, holder.viewLine);
        holder.nameTv.setText(campaign.getName());
        holder.tvConsumeTv.setText("活动预算：¥" + StringUtil.deleteZero(String.valueOf(campaign.getBudget())));
        if (campaign.getStatus().equals("unpay") ||TextUtils.isEmpty(String.valueOf(campaign.getUsed_credits()))) {
            holder.tvCreditTv.setVisibility(View.INVISIBLE);
        }else {
            holder.tvCreditTv.setVisibility(View.VISIBLE);
            holder.tvCreditTv.setText("积分："+String.valueOf(campaign.getUsed_credits()));
        }
        BitmapUtil.loadImage(BaseApplication.getContext(), campaign.getImg_url(), holder.imageIv);
    }

    private void setCancelTv(TextView cancelTv, View viewLine) {
        switch (data.campaignType) {
            case TYPE_RUNNING:
                //            case TYPE_COMPLETED:
                cancelTv.setVisibility(View.GONE);
                viewLine.setVisibility(View.GONE);
                break;
            default:
                cancelTv.setVisibility(View.VISIBLE);
                viewLine.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setEvaluationState(LaunchRewordModel.Campaign campaign, TextView cancelTv, View viewLine) {
        if (TYPE_COMPLETED.equals(data.campaignType)) {
            if (EVALUA_TING.equals(campaign.getEvaluation_status())) {
                cancelTv.setText("效果评价");
            } else if (EVALUAT_ED.equals(campaign.getEvaluation_status())) {
                cancelTv.setText("查看评价");
            } else {
                cancelTv.setVisibility(View.GONE);
                viewLine.setVisibility(View.GONE);
            }
        }


    }


    private void setPayInstantlyTv(TextView payInstantlyTv, LaunchRewordModel.Campaign campaign) {
        if (campaign == null || TextUtils.isEmpty(campaign.getStatus())) {
            return;
        }
        switch (campaign.getStatus()) {
            case "unpay"://未付款或部分付款(campaign 的need_pay_amount > 0 的时候 都是 未付款的)	string
                payInstantlyTv.setText(BaseApplication.getContext().getString(R.string.pay_instantly));
                break;
            case "unexecute"://付款后 运营人员审核中	string
                payInstantlyTv.setText(BaseApplication.getContext().getString(R.string.checking));
                break;
            case "rejected"://审核拒绝	string
                payInstantlyTv.setText(BaseApplication.getContext().getString(R.string.checked_rejected));
                break;
            case "agreed"://审核通过， 活动未开始前 的状态(即将执行)	string
                payInstantlyTv.setText(R.string.approved_wait_activity_start);
                break;
            case "executing"://已开始 执行中	string
                payInstantlyTv.setText(R.string.executing);
                break;
            case "executed"://活动结束 未结算	string
                payInstantlyTv.setText(BaseApplication.getContext().getString(R.string.executed));
                break;
            case "settled"://已结算
                payInstantlyTv.setText(BaseApplication.getContext().getString(R.string.settled));
                break;
        }
    }

    @Override
    public int getAdapterItemCount() {
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView dateTv;// 07-20
        public TextView tvConsumeTv;// 100
        public TextView tvCreditTv;// 100
        public TextView nameTv;// 名字
        public TextView payInstantlyTv;// 立即支付
        public TextView cancelTv;// 撤销活动
        public ImageView imageIv;// 图片
        public int position;
        public View rlContent;
        public View viewLine;

        public ViewHolder(View itemView) {
            super(itemView);
            rlContent = itemView.findViewById(R.id.rl_content);
            dateTv = (TextView) itemView.findViewById(R.id.tv_date);
            tvConsumeTv = (TextView) itemView.findViewById(R.id.tv_consume);
            tvCreditTv = (TextView) itemView.findViewById(R.id.tv_credit);
            nameTv = (TextView) itemView.findViewById(R.id.tv_name);
            payInstantlyTv = (TextView) itemView.findViewById(R.id.tv_pay_instantly);
            cancelTv = (TextView) itemView.findViewById(R.id.tv_cancel);
            imageIv = (ImageView) itemView.findViewById(R.id.image);
            viewLine = itemView.findViewById(R.id.view_line);
            rlContent.setOnClickListener(this);
            cancelTv.setOnClickListener(this);
            payInstantlyTv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_cancel:
                    if (null != onRecyclerViewListener) {
                        onRecyclerViewListener.onItemCancelClick(position);
                    }
                    break;
                default:
                    if (null != onRecyclerViewListener) {
                        onRecyclerViewListener.onItemClick(position);
                    }
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (null != onRecyclerViewListener) {
                return onRecyclerViewListener.onItemLongClick(position);
            }
            return false;
        }
    }
}
