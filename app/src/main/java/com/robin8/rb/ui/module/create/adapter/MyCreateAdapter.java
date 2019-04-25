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
import com.robin8.rb.ui.adapter.ViewPagerAdapter;
import com.robin8.rb.ui.module.create.model.CpsArticlesBean;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.util.UIUtils;

import java.util.List;


public class MyCreateAdapter extends BaseRecyclerAdapter {

    public static final String TYPE_PENDING = "pending";
    public static final String TYPE_PASSED = "passed";
    public static final String TYPE_REJECTED = "rejected";
    public static final String TYPE_SHARES = "shares";
    private ViewHolder mViewHolder;
    private ViewPagerAdapter.SelectItem data;

    public void setData(ViewPagerAdapter.SelectItem data) {
        this.data = data;
    }

    public interface OnRecyclerViewListener {
        void onItemClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(
            OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private static final String TAG = MyCreateAdapter.class.getSimpleName();

    public void setDataList(List<CpsArticlesBean> list) {
        this.mDataList = list;
    }

    private List<CpsArticlesBean> mDataList;

    public MyCreateAdapter(List<CpsArticlesBean> list) {
        this.mDataList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i, boolean isItem) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_create_list_item, null);
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
        CpsArticlesBean bean = mDataList.get(position);
        if (bean == null) {
            return;
        }
        Context context = holder.picIv.getContext();
        holder.titleTv.setText(bean.getTitle());
        BitmapUtil.loadImage(context, bean.getCover(), holder.picIv);

        String earningStr = context.getString(R.string.earning) + "<font color=#ecb200>¥ 0</font>";
        String earnedStr = context.getString(R.string.earning) + "<font color=#ecb200>¥ 0</font>";
        switch (data.campaignType) {
            case TYPE_SHARES:
            case TYPE_PASSED:
                holder.lineBottom.setVisibility(View.GONE);
                holder.bottomTv.setVisibility(View.GONE);
                if (TYPE_PASSED.equals(data.campaignType)) {
                    earningStr = context.getString(R.string.earning) + "<font color=#ecb200>¥ " + StringUtil.deleteZero(bean.getWriting_forecast_commission()) + "</font>";
                    earnedStr = context.getString(R.string.earned) + "<font color=#ecb200>¥ " + StringUtil.deleteZero(bean.getWriting_settled_commission()) + "</font>";
                } else {
                    earningStr = context.getString(R.string.earning) + "<font color=#ecb200>¥ " + StringUtil.deleteZero(bean.getShare_forecast_commission()) + "</font>";
                    earnedStr = context.getString(R.string.earned) + "<font color=#ecb200>¥ " + StringUtil.deleteZero(bean.getShare_settled_commission()) + "</font>";
                }

                break;

            case TYPE_PENDING:
                holder.lineBottom.setVisibility(View.VISIBLE);
                holder.bottomTv.setVisibility(View.VISIBLE);
                holder.bottomTv.setTextColor(UIUtils.getColor(R.color.sub_gray_custom));
                holder.bottomTv.setText(R.string.checking);
                break;

            case TYPE_REJECTED:
                holder.lineBottom.setVisibility(View.VISIBLE);
                holder.bottomTv.setVisibility(View.VISIBLE);
                holder.bottomTv.setTextColor(UIUtils.getColor(R.color.red_custom));
                holder.bottomTv.setText(context.getString(R.string.reject_reason) + bean.getCheck_remark());
                break;
        }
        holder.earningTv.setText(Html.fromHtml(earningStr));
        holder.earnedTv.setText(Html.fromHtml(earnedStr));
    }


    @Override
    public int getAdapterItemCount() {
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView picIv;
        public TextView titleTv;
        public TextView earningTv;
        public TextView earnedTv;
        public TextView bottomTv;
        public View lineBottom;
        public int position;

        public ViewHolder(View itemView) {
            super(itemView);
            picIv = (ImageView) itemView.findViewById(R.id.iv_pic);
            titleTv = (TextView) itemView.findViewById(R.id.tv_title);
            earningTv = (TextView) itemView.findViewById(R.id.tv_earning);
            earnedTv = (TextView) itemView.findViewById(R.id.tv_earned);
            lineBottom = itemView.findViewById(R.id.line_bottom);
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
