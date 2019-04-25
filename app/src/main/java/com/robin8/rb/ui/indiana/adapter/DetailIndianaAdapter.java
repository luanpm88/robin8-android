package com.robin8.rb.ui.indiana.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.R;
import com.robin8.rb.ui.widget.autoviewpager.AutoScrollViewPager;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.ui.indiana.activity.GoodsDetailActivity;
import com.robin8.rb.ui.indiana.activity.IndianaCountActivity;
import com.robin8.rb.ui.indiana.model.IndianaDetailBean;
import com.robin8.rb.ui.indiana.model.OrderBean;
import com.robin8.rb.ui.model.BaseBean;
import com.robin8.rb.ui.widget.AlignTextView;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.DateUtil;
import com.robin8.rb.ui.widget.CircleImageView;

import java.util.List;


public class DetailIndianaAdapter extends BaseRecyclerAdapter implements View.OnClickListener {
    private ViewHolder mViewHolder;
    private static final String TAG = DetailIndianaAdapter.class.getSimpleName();
    private List<BaseBean> mDataList;
    private OnRecyclerViewListener onRecyclerViewListener;
    private IndianaDetailBean mIndianaDetailBean;
    private StringBuffer mStringBuffer;
    private boolean mExecutingB;

    public enum ITEM_TYPE {
        HEADER,
        NORMAL
    }

    public static interface OnRecyclerViewListener {
        void onItemClick(int position);
    }

    public void setOnRecyclerViewListener(
            OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    public void setDataList(List<BaseBean> list) {
        this.mDataList = list;
    }


    public DetailIndianaAdapter(List<BaseBean> list) {
        this.mDataList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i, boolean isItem) {
        if (i == 0) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.detail_indiana_header_item, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(lp);
            return new ViewHolder2(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.detail_indiana_list_item, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(lp);
            mViewHolder = new ViewHolder(view);
            return mViewHolder;
        }
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return mViewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position, boolean isItem) {

        if (position < 0 || position >= mDataList.size()) {
            return;
        }

        BaseBean baseBean = mDataList.get(position);
        if (baseBean == null) {
            return;
        }

        if (viewHolder instanceof ViewHolder2) {
            ViewHolder2 holder = (ViewHolder2) viewHolder;
            setHeaderView(holder, baseBean);
        } else if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.position = position;
            setItemView(holder, baseBean);
        }
    }

    /**
     * 填充头
     *
     * @param holder
     * @param baseBean
     */
    private void setHeaderView(ViewHolder2 holder, BaseBean baseBean) {
        if (!(baseBean instanceof IndianaDetailBean)) {
            return;
        }
        mIndianaDetailBean = (IndianaDetailBean) baseBean;
        if (mIndianaDetailBean == null) {
            return;
        }

        IndianaDetailBean.IndianaActivity indiana = mIndianaDetailBean.getActivity();
        if (indiana == null) {
            return;
        }

        final Context context = holder.goodsNameTv.getContext();
        holder.goodsNameTv.setText(indiana.getName());
        holder.totalNumberTv.setText(String.valueOf(indiana.getTotal_number()));
        float actual = indiana.getActual_number();
        float total = indiana.getTotal_number();
        double percent = actual / total * 10000;
        holder.compeletePb.setMax(10000);
        holder.compeletePb.setProgress((int) (percent));
        setVpAuto(holder, indiana.getPictures());
        if (mIndianaDetailBean.getToken_number() == 0) {
            holder.tipsTv.setText(context.getString(R.string.you_not_participated));
            holder.tipsTv.setVisibility(View.VISIBLE);
            holder.mineTimesLL.setVisibility(View.GONE);
        } else {
            holder.tipsTv.setVisibility(View.GONE);
            holder.mineTimesLL.setVisibility(View.VISIBLE);
            holder.mineTimesTv.setText(context.getString(R.string.you_participated_times) +
                    mIndianaDetailBean.getToken_number() + context.getResources().getString(R.string.person_times));
            holder.mineNumberTv.setText(getIndianaNumber(mIndianaDetailBean.getTickets()));
        }

        switch (indiana.getStatus()) {
            case "executing":
                mExecutingB = true;
                holder.winnerInfoLL.setVisibility(View.GONE);
                holder.surplusNumberTv.setText(String.valueOf(indiana.getTotal_number() - indiana.getActual_number()));
                break;
            case "finished":
                mExecutingB = false;
                holder.winnerInfoLL.setVisibility(View.VISIBLE);
                holder.surplusNumberTv.setText(String.valueOf(0));
                holder.luckyNumberTv.setText(indiana.getLucky_number());
                holder.winnerNameTv.setText(indiana.getWinner_name());
                holder.participateNumberTv.setText(context.getString(R.string.this_participated)
                        + indiana.getWinner_token_number() + context.getString(R.string.person_times));
                holder.issueTv.setText(context.getString(R.string.this_issue) + indiana.getCode());
                holder.winTimeTv.setText(context.getString(R.string.draw_time) + indiana.getDraw_at());
                BitmapUtil.loadImage(context, indiana.getWinner_avatar_url(), holder.imageCiv);
                break;
        }

        holder.goodsDetailLL.setOnClickListener(this);
        holder.countDetailTv.setOnClickListener(this);
    }

    private String getIndianaNumber(String[] tickets) {

        if (tickets == null) {
            return "";
        }

        if (mStringBuffer == null) {
            mStringBuffer = new StringBuffer();
        } else {
            mStringBuffer.delete(0, mStringBuffer.length());
        }

        mStringBuffer.append(BaseApplication.getContext().getString(R.string.indiana_number));
        for (int i = 0; i < tickets.length; i++) {
            mStringBuffer.append(tickets[i] + "  ");
        }
        return mStringBuffer.toString();
    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ll_goods_detail:
                if (mIndianaDetailBean == null || mIndianaDetailBean.getActivity() == null) {
                    return;
                }
                intent = new Intent(context, GoodsDetailActivity.class);
                intent.putExtra("code", mIndianaDetailBean.getActivity().getCode());
                intent.putExtra("name", mIndianaDetailBean.getActivity().getName());
                context.startActivity(intent);
                break;
            case R.id.tv_count_detail:
                intent = new Intent(context, IndianaCountActivity.class);
                intent.putExtra("code", mIndianaDetailBean.getActivity().getCode());
                context.startActivity(intent);
                break;
        }
    }

    /**
     * 设置轮播图
     *
     * @param pictures
     */
    private void setVpAuto(ViewHolder2 holder, String[] pictures) {
        AutoScrollViewPager vpAuto = holder.autoVp;
        if (pictures == null || pictures.length == 0) {
            return;
        }

        if(pictures.length != 1){
            vpAuto.setAdapter(new ImagePagerAdapter(BaseApplication.getContext(), pictures).setInfiniteLoop(true));
            vpAuto.setInterval(5000);
            vpAuto.startAutoScroll();
        }else {
            vpAuto.setAdapter(new ImagePagerAdapter(BaseApplication.getContext(), pictures).setInfiniteLoop(false));
        }

        vpAuto.setOnPageChangeListener(new MyOnPageChangeListener(pictures, holder));
        vpAuto.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % pictures.length);
    }

    private void setPoints(int position, String[] pictures, ViewHolder2 holder) {
        holder.vpPointsLL.removeAllViews();// 清空以前的点
        if (pictures.length < 2) {
            holder.vpPointsLL.setVisibility(View.INVISIBLE);
            return;
        }
        for (int i = 0; i < pictures.length; i++) {
            View view = new View(BaseApplication.getContext());
            view.setBackgroundResource(R.drawable.indiana_tab_dot_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (BaseApplication.mPixelDensityF * 7), (int) (BaseApplication.mPixelDensityF * 7));
            if (i != 0) {
                params.leftMargin = (int) (BaseApplication.mPixelDensityF * 7);
            }
            view.setEnabled(false);// 把所有的点变成灰色
            view.setLayoutParams(params);
            holder.vpPointsLL.addView(view);
        }
        // 一开始把第一个点置为红色
        holder.vpPointsLL.getChildAt(position).setEnabled(true);
    }

    /**
     * 填充普通条目
     *
     * @param holder
     * @param baseBean
     */
    private void setItemView(ViewHolder holder, BaseBean baseBean) {
        if (!(baseBean instanceof OrderBean)) {
            return;
        }
        OrderBean orderBean = (OrderBean) baseBean;
        if (orderBean == null) {
            return;
        }
        OrderBean.KOL kol = orderBean.getKol();
        if (kol == null) {
            return;
        }

        if (mExecutingB && holder.position == getAdapterItemCount()-1) {
            holder.view.setVisibility(View.VISIBLE);
        } else {
            holder.view.setVisibility(View.GONE);
        }

        Context context = holder.imageCiv.getContext();
        holder.nameTv.setText(kol.getName());
        holder.timesTv.setText(context.getString(R.string.participated) + String.valueOf(orderBean.getNumber()) + context.getString(R.string.person_times));

        holder.timeTv.setText(DateUtil.formatTime(orderBean.getCreated_at()));
        BitmapUtil.loadImage(context, kol.getAvatar_url(), holder.imageCiv);
    }

    @Override
    public int getAdapterItemCount() {
        return mDataList.size();
    }

    @Override
    public int getAdapterItemViewType(int position) {
        return position == 0 ? ITEM_TYPE.HEADER.ordinal() : ITEM_TYPE.NORMAL.ordinal();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public int position;
        public CircleImageView imageCiv;
        public TextView nameTv;
        public TextView timesTv;
        public TextView timeTv;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            imageCiv = (CircleImageView) itemView.findViewById(R.id.civ_image);
            nameTv = (TextView) itemView.findViewById(R.id.tv_name);
            timesTv = (TextView) itemView.findViewById(R.id.tv_times);
            timeTv = (TextView) itemView.findViewById(R.id.tv_time);
            view = itemView.findViewById(R.id.view);
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

    class ViewHolder2 extends RecyclerView.ViewHolder {
        private View mineTimesLL;
        private TextView mineTimesTv;
        private AlignTextView mineNumberTv;
        public AutoScrollViewPager autoVp;
        public TextView goodsNameTv;
        public ProgressBar compeletePb;
        public TextView surplusNumberTv;
        public TextView tipsTv;
        public CircleImageView imageCiv;
        public TextView winnerNameTv;
        public TextView participateNumberTv;
        public TextView issueTv;
        public TextView winTimeTv;
        public TextView luckyNumberTv;
        public TextView countDetailTv;
        public View goodsDetailLL;
        public View winnerInfoLL;
        public TextView totalNumberTv;
        public LinearLayout vpPointsLL;

        public ViewHolder2(View itemView) {
            super(itemView);
            autoVp = (AutoScrollViewPager) itemView.findViewById(R.id.vp_auto);
            vpPointsLL = (LinearLayout) itemView.findViewById(R.id.ll_vp_points);
            goodsNameTv = (TextView) itemView.findViewById(R.id.tv_goods_name);
            compeletePb = (ProgressBar) itemView.findViewById(R.id.pb_compelete);
            totalNumberTv = (TextView) itemView.findViewById(R.id.tv_total_number);
            surplusNumberTv = (TextView) itemView.findViewById(R.id.tv_surplus_number);
            tipsTv = (TextView) itemView.findViewById(R.id.tv_tips);
            imageCiv = (CircleImageView) itemView.findViewById(R.id.civ_image);
            winnerNameTv = (TextView) itemView.findViewById(R.id.tv_winner_name);
            participateNumberTv = (TextView) itemView.findViewById(R.id.tv_participate_number);
            issueTv = (TextView) itemView.findViewById(R.id.tv_issue);
            winTimeTv = (TextView) itemView.findViewById(R.id.tv_win_time);
            luckyNumberTv = (TextView) itemView.findViewById(R.id.tv_lucky_number);
            countDetailTv = (TextView) itemView.findViewById(R.id.tv_count_detail);
            goodsDetailLL = itemView.findViewById(R.id.ll_goods_detail);
            winnerInfoLL = itemView.findViewById(R.id.ll_winner_info);
            mineNumberTv = (AlignTextView) itemView.findViewById(R.id.tv_mine_number);
            mineTimesTv = (TextView) itemView.findViewById(R.id.tv_mine_times);
            mineTimesLL = itemView.findViewById(R.id.ll_mine_times);
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        private final ViewHolder2 holder;
        private String[] pictures;

        MyOnPageChangeListener(String[] pictures, ViewHolder2 holder) {
            this.pictures = pictures;
            this.holder = holder;
        }

        @Override
        public void onPageSelected(int position) {
            setPoints(position % pictures.length, pictures, holder);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}
