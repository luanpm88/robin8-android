package com.robin8.rb.ui.module.create.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.R;
import com.robin8.rb.ui.module.create.model.ProductListModel;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.StringUtil;

import java.util.List;


public class ProductListAdapter extends BaseRecyclerAdapter {
    private LayoutInflater mLayoutInflater;
    private ViewHolder mViewHolder;
    private static final String TAG = ProductListAdapter.class.getSimpleName();
    private List<Object> mDataList;
    private OnRecyclerViewListener onRecyclerViewListener;

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


    public ProductListAdapter(List<Object> list, Context context) {
        this.mDataList = list;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i, boolean isItem) {
        View view = mLayoutInflater.inflate(R.layout.product_list_item, null);
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
        Object obj = mDataList.get(position);
        if (obj == null) {
            return;
        }

        if (obj instanceof ProductListModel.CpsMaterialsBean) {
            ProductListModel.CpsMaterialsBean bean = (ProductListModel.CpsMaterialsBean) obj;
            holder.position = position;
            holder.productNameTv.setText(bean.getGoods_name());
            Context ctx = holder.productNameTv.getContext();
            holder.priceTv.setText(ctx.getString(R.string.price) + " ¥ " + StringUtil.deleteZero(bean.getUnit_price()));
            String str = ctx.getString(R.string.commission) + "<font color=#ecb200> ¥ " + StringUtil.deleteZero(bean.getKol_commision_wl()) + "</font>";
            holder.commissionTv.setText(Html.fromHtml(str));

            holder.validityOfGoodsTv.setText(ctx.getString(R.string.validity_of_goods) +" "+ bean.getEnd_date());
            Context context = holder.productNameTv.getContext();
            BitmapUtil.loadImage(context, bean.getImg_url(), holder.productIv);
        }
    }

    @Override
    public int getAdapterItemCount() {
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public int position;
        public TextView productNameTv;
        public ImageView productIv;
        public TextView priceTv;
        public TextView commissionTv;
        public TextView validityOfGoodsTv;

        public ViewHolder(View itemView) {
            super(itemView);
            productIv = (ImageView) itemView.findViewById(R.id.iv_product);
            productNameTv = (TextView) itemView.findViewById(R.id.tv_product_name);
            priceTv = (TextView) itemView.findViewById(R.id.tv_price);
            commissionTv = (TextView) itemView.findViewById(R.id.tv_commission);
            validityOfGoodsTv = (TextView) itemView.findViewById(R.id.tv_validity_of_goods);
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
}
