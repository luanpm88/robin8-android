package com.robin8.rb.ui.module.create.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.R;
import com.robin8.rb.ui.module.create.model.ArticleListsModel;
import com.robin8.rb.ui.widget.AlignTextView;
import com.robin8.rb.util.BitmapUtil;

import java.util.List;


public class SearchArticleAdapter extends BaseRecyclerAdapter {
    private LayoutInflater mLayoutInflater;
    private static final String TAG = ArticleListsAdapter.class.getSimpleName();
    private List<Object> mDataList;
    private OnRecyclerViewListener onRecyclerViewListener;
    private RecyclerView.ViewHolder mViewHolder;

    public interface OnRecyclerViewListener {
        void onItemClick(int position);
    }

    public void setOnRecyclerViewListener(
            OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    public void setDataList(List<Object> list) {
        this.mDataList = list;
    }

    public SearchArticleAdapter(List<Object> list, Context context) {
        this.mDataList = list;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return mViewHolder;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = mLayoutInflater.inflate(R.layout.article_search_item, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        mViewHolder = new ViewHolder(view);
        return mViewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position, boolean isItem) {
        ViewHolder holder;
        if (viewHolder instanceof ViewHolder) {
            holder = (ViewHolder) viewHolder;
            holder.position = position;
        } else {
            return;
        }
        if (position < 0 || position >= mDataList.size()) {
            return;
        }

        Object obj = mDataList.get(position);
        if (obj == null) {
            return;
        }
        if (obj instanceof ArticleListsModel.ArticlesBean) {
            ArticleListsModel.ArticlesBean bean = (ArticleListsModel.ArticlesBean) obj;
            holder.titleTv.setText(bean.getArticle_title());
            holder.srcTv.setText(bean.getArticle_author());
            Context context = holder.picIv.getContext();
            BitmapUtil.loadImage(context, bean.getArticle_avatar_url(), holder.picIv);

        }
    }

    @Override
    public int getAdapterItemCount() {
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public int position;
        public AlignTextView titleTv;
        public ImageView picIv;
        public TextView srcTv;

        public ViewHolder(View itemView) {
            super(itemView);
            picIv = (ImageView) itemView.findViewById(R.id.iv_pic);
            titleTv = (AlignTextView) itemView.findViewById(R.id.tv_title);
            srcTv = (TextView) itemView.findViewById(R.id.tv_src);
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
