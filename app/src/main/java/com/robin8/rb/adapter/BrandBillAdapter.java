package com.robin8.rb.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.R;
import com.robin8.rb.model.BrandBillCreditModel;
import com.robin8.rb.model.BrandBillModel;

import java.util.List;

public class BrandBillAdapter extends BaseRecyclerAdapter {
    private IncomeDetailViewHolder mIncomeDetailViewHolder;
    private int type;
    private List<BrandBillCreditModel.CreditsBean> listCredit;

    public static interface OnRecyclerViewListener {
        void onItemClick(int position);

        boolean onItemLongClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private static final String TAG = BrandBillAdapter.class.getSimpleName();

    public void setDataList(List<BrandBillModel.BrandBill> list) {
        this.mDataList = list;
    }

    public void setCreditDataList(List<BrandBillCreditModel.CreditsBean> listCredit) {
        this.listCredit = listCredit;
    }

    private List<BrandBillModel.BrandBill> mDataList;

    public BrandBillAdapter(List<BrandBillModel.BrandBill> list, List<BrandBillCreditModel.CreditsBean> listCredit, int type) {
        this.mDataList = list;
        this.listCredit = listCredit;
        this.type = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i, boolean isItem) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.income_detail_list_item, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        mIncomeDetailViewHolder = new IncomeDetailViewHolder(view);
        return mIncomeDetailViewHolder;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return mIncomeDetailViewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position, boolean isItem) {
        IncomeDetailViewHolder holder = (IncomeDetailViewHolder) viewHolder;
        holder.position = position;
        if (type == 0) {
            BrandBillModel.BrandBill brandBill = mDataList.get(position);
            if (brandBill == null) {
                return;
            }
            holder.dateTv.setText(brandBill.getCreated_at());
            holder.everydayIncomeTv.setText(brandBill.getCredits());
            holder.subjectTv.setText(brandBill.getTitle());
            holder.numtypeTv.setText(brandBill.getDirect_text());
        }else {
            BrandBillCreditModel.CreditsBean creditsBean = listCredit.get(position);
            if (creditsBean == null){
                return;
            }
            holder.dateTv.setText(creditsBean.getShow_time());
            holder.subjectTv.setText(creditsBean.getRemark());
            holder.numtypeTv.setText(creditsBean.getDirect());
            holder.everydayIncomeTv.setText(String.valueOf(creditsBean.getScore()));
        }


    }

    @Override
    public int getAdapterItemCount() {
        if (type == 0) {
            return mDataList.size();
        } else {
            return listCredit.size();
        }

    }

    class IncomeDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView dateTv;// 07-20
        public TextView everydayIncomeTv;// +1.4
        public TextView subjectTv;// 签到
        public TextView numtypeTv;// 收入
        public int position;

        public IncomeDetailViewHolder(View itemView) {
            super(itemView);
            dateTv = (TextView) itemView.findViewById(R.id.tv_date);
            everydayIncomeTv = (TextView) itemView.findViewById(R.id.tv_everyday_income);
            subjectTv = (TextView) itemView.findViewById(R.id.tv_subject);
            numtypeTv = (TextView) itemView.findViewById(R.id.tv_numtype);
        }

        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener.onItemClick(position);
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

    private OnBottomListener bottomListener;

    public void setOnBottomListener(OnBottomListener bottomListener) {
        this.bottomListener = bottomListener;
    }

    public interface OnBottomListener {
        void isOnBottom(boolean isBottom);
    }
}
