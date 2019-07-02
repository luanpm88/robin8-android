package com.robin8.rb.ui.module.mine.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.R;
import com.robin8.rb.ui.module.mine.model.MessageModel;
import com.robin8.rb.util.DateUtil;

import java.util.List;


/**
 * 消息页面适配器
 */
public class MessageAdapter extends BaseRecyclerAdapter {
    private ViewHolder mViewHolder;
    private static final String TAG = MessageAdapter.class.getSimpleName();
    private List<MessageModel.MessagesBean> mDataList;
    private OnRecyclerViewListener onRecyclerViewListener;

    public static interface OnRecyclerViewListener {
        void onItemClick(int position);
    }

    public void setOnRecyclerViewListener(
            OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    public void setDataList(List<MessageModel.MessagesBean> list) {
        this.mDataList = list;
    }


    public MessageAdapter(List<MessageModel.MessagesBean> list) {
        this.mDataList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i, boolean isItem) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_list_item, null);
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
        MessageModel.MessagesBean MessagesBean = mDataList.get(position);
        if (MessagesBean == null) {
            return;
        }
        holder.position = position;
        holder.nameTv.setText(MessagesBean.getName());
        holder.contentTv.setText(MessagesBean.getTitle());
        holder.timeTv.setText(DateUtil.getCountdown(holder.timeTv.getContext(),MessagesBean.getCreated_at()));
        if (MessagesBean.isIs_read()) {
            holder.dotIv.setVisibility(View.INVISIBLE);
        } else {
            holder.dotIv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getAdapterItemCount() {
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public int position;
        public TextView nameTv;
        public TextView timeTv;
        public TextView contentTv;
        public ImageView dotIv;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.tv_name);
            timeTv = (TextView) itemView.findViewById(R.id.tv_time);
            contentTv = (TextView) itemView.findViewById(R.id.tv_content);
            dotIv = (ImageView) itemView.findViewById(R.id.iv_dot);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDataList.get(position).setIs_read(true);
                        notifyDataSetChanged();
                    }
                }, 300);
                onRecyclerViewListener.onItemClick(position);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}
