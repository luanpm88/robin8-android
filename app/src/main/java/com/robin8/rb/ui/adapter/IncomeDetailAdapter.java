package com.robin8.rb.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.R;
import com.robin8.rb.ui.model.AccountMenuModel;
import com.robin8.rb.util.DateUtil;

import java.util.List;

public class   IncomeDetailAdapter extends BaseRecyclerAdapter {
    private IncomeDetailViewHolder mIncomeDetailViewHolder;

    public static interface OnRecyclerViewListener {
        void onItemClick(int position);

        boolean onItemLongClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(
            OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private static final String TAG = IncomeDetailAdapter.class.getSimpleName();

    public void setDataList(List<AccountMenuModel.TransactionEntity> list) {
        this.mDataList = list;
    }

    private List<AccountMenuModel.TransactionEntity> mDataList;

    public IncomeDetailAdapter(List<AccountMenuModel.TransactionEntity> list) {
        this.mDataList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i,boolean isItem) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.income_detail_list_item, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
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
        AccountMenuModel.TransactionEntity transactionEntity = mDataList.get(position);
        holder.dateTv.setText(DateUtil.formatTime("yyyy-MM-dd", mDataList.get(position).getCreated_at(), "MM-dd"));
        if(TextUtils.equals("收入",transactionEntity.getDirect())){
            holder.everydayIncomeTv.setText("+"+transactionEntity.getCredits());
        }else {
            if (TextUtils.isEmpty(transactionEntity.getRemark())){
                holder.everydayIncomeTv.setText("-"+transactionEntity.getCredits());
            }else {
                holder.everydayIncomeTv.setText("+"+transactionEntity.getCredits());
            }
        }
        if (TextUtils.isEmpty(transactionEntity.getRemark())){
            holder.subjectTv.setText(subSelf(transactionEntity.getSubject()));
        }else {
            holder.subjectTv.setText(holder.subjectTv.getContext().getString(R.string.robin365,transactionEntity.getRemark()));
        }
        holder.numtypeTv.setText(transactionEntity.getDirect());
//        Log.e("xxfigo","position=" + position + ";size=" + mDataList.size());
		if(bottomListener!=null){
			if (position == mDataList.size() - 1) {
				bottomListener.isOnBottom(true);
			} else {
				bottomListener.isOnBottom(false);
			}
		}
    }
    public String subSelf(String name) {
        String all = "";
        if (name.contains("<") || name.contains(">")) {
            int a = name.indexOf("<");
            int b = name.indexOf(">");
            all = name.replaceAll(name.substring(a, b + 1), "");
            return subSelf(all);
        } else {
            return name;
        }
    }
    @Override
    public int getAdapterItemCount() {
        return mDataList.size();
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
