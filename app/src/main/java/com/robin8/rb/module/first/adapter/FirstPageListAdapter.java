package com.robin8.rb.module.first.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.robin8.rb.R;
import com.robin8.rb.autoviewpager.AutoScrollViewPager;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.module.first.activity.KolDetailContentActivity;
import com.robin8.rb.module.first.helper.ViewPagerHelper;
import com.robin8.rb.module.first.model.BigVsBean;
import com.robin8.rb.module.first.model.KolAnnouncementsBean;
import com.robin8.rb.module.first.model.SocialAccountsBean;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.util.ListUtils;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.UIUtils;

import java.util.List;

public class FirstPageListAdapter extends BaseRecyclerAdapter implements View.OnClickListener {
    private boolean needHeader;
    private ViewHolder mViewHolder;
    private static final String TAG = FirstPageListAdapter.class.getSimpleName();
    private List<Object> mDataList;
    private ViewPagerHelper mViewPagerHelper;
    private List<Object> mNewList;

    public void setNewList(List<Object> newList) {
        this.mNewList = newList;
    }

    public enum ITEM_TYPE {
        HEADER,
        NORMAL
    }

    public void setDataList(List<Object> list) {
        this.mDataList = list;
    }


    public FirstPageListAdapter(List<Object> list, boolean needHeader) {
        this.mDataList = list;
        this.needHeader = needHeader;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i, boolean isItem) {
        if (i == 0 && needHeader) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.first_header, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(lp);
            return new ViewHolder2(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.first_page_list_double_item, null);
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

        if (mDataList == null || mDataList.size() == 0 || position < 0 || position >= mDataList.size()) {
            return;
        }

        Object obj = mDataList.get(position);
        List list = null;
        if (obj instanceof List) {
            list = (List) obj;
        }
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            setNormalItem(holder, list);
        } else if (viewHolder instanceof ViewHolder2) {
            ViewHolder2 holder = (ViewHolder2) viewHolder;
            setHeaderItem(holder, list);
        }
    }

    /**
     * 填充头布局
     * @param holder
     * @param kolAnnouncements
     */
    private void setHeaderItem(ViewHolder2 holder, List<KolAnnouncementsBean> kolAnnouncements) {
        Context context = holder.itemVp.getContext();
        mViewPagerHelper = new ViewPagerHelper(context);
//        mViewPagerHelper.initTagItemVp(holder.itemVp);
        mViewPagerHelper.updateHeaderView(holder.autoVp, holder.llpointsVp, kolAnnouncements);
        mViewPagerHelper.initRecyclerView(holder.recyclerView, mNewList);
    }

    private void setNormalItem(ViewHolder holder, List list) {
        if (list == null || list.size() == 0) {
            return;
        }
        if (list.size() >= 2) {
            setOneItemView(list.get(0), holder.tvNameFir, holder.ivBgFir, holder.tvIntroduceFir, holder.llTextFir, holder.viewFir);
            setOneItemView(list.get(1), holder.tvNameSec, holder.ivBgSec, holder.tvIntroduceSec, holder.llTextSec, holder.viewSec);
            holder.viewSec.setVisibility(View.VISIBLE);
        } else {
            setOneItemView(list.get(0), holder.tvNameFir, holder.ivBgFir, holder.tvIntroduceFir, holder.llTextFir, holder.viewFir);
            holder.viewSec.setVisibility(View.INVISIBLE);
        }
    }

    private void setOneItemView(Object obj, TextView tvName, ImageView ivBg, TextView tvIntroduce, LinearLayout llText, View view) {
        BigVsBean bigV;
        LogUtil.logXXfigo("setOneItemView");
        if (obj instanceof BigVsBean) {
            bigV = (BigVsBean) obj;
            if (bigV == null) {
                return;
            }
            LogUtil.logXXfigo("name = "+bigV.getName());
            tvName.setText(bigV.getName());
            setImageView(ivBg, bigV.getAvatar_url());
            setTags(llText, bigV.getTags());

            List<SocialAccountsBean> socialAccounts = bigV.getSocial_accounts();
            if (socialAccounts != null && socialAccounts.size() != 0 && socialAccounts.get(0) != null) {
                tvIntroduce.setText(ListUtils.getFromText(socialAccounts));
            }
            view.setOnClickListener(new MyOnClickListener(bigV, view.getContext()));
        }
    }

    private void skipToKolDetail(int id, Context context) {
        Intent intent = new Intent(context, KolDetailContentActivity.class);
        intent.putExtra("id", id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void setTags(LinearLayout llText, List<BigVsBean.TagsBean> tags) {
        if (tags == null || tags.size() == 0) {
            llText.setVisibility(View.GONE);
            return;
        } else {
            llText.setVisibility(View.VISIBLE);
        }
        int size = tags.size();
        if (size > 4) {
            size = 4;
        }
        llText.removeAllViews();
        for (int i = 0; i < size; i++) {
            TextView tv = new TextView(llText.getContext());
            tv.setText(tags.get(i).getLabel());
            tv.setTextSize(10);
            tv.setTextColor(UIUtils.getColor(R.color.yellow_custom));
            tv.setBackgroundResource(R.drawable.shape_bg_yellow_pane_first);
            tv.setGravity(Gravity.CENTER);
            float px = BaseApplication.mPixelDensityF;
            tv.setPadding(14, 0, 14, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) px * 14);
            if (i != 0) {
                params.leftMargin = (int) (px * 7);
            }
            llText.addView(tv, params);
        }
    }

    private void setImageView(final ImageView ivBg, String url) {
        /*if (ivBg == null) {
            return;
        }*/
        BitmapUtil.loadImage(ivBg.getContext(), url, ivBg, BitmapUtil.getBg());

        ivBg.post(new Runnable() {
            @Override
            public void run() {
                ivBg.getLayoutParams().width = DensityUtils.getScreenWidth(ivBg.getContext()) / 2;
                ivBg.getLayoutParams().height = DensityUtils.getScreenWidth(ivBg.getContext()) * 3 / 5;
            }
        });
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public int getAdapterItemCount() {
        return mDataList.size();
    }

    @Override
    public int getAdapterItemViewType(int position) {
        return position == 0 ? ITEM_TYPE.HEADER.ordinal() : ITEM_TYPE.NORMAL.ordinal();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public int position;
        public View viewFir;
        public View viewSec;
        public TextView tvNameFir;
        public TextView tvNameSec;

        public ImageView ivBgFir;
        public ImageView ivBgSec;
        public LinearLayout llTextSec;
        public LinearLayout llTextFir;
        public TextView tvIntroduceFir;
        public TextView tvIntroduceSec;


        public ViewHolder(View itemView) {
            super(itemView);
            viewFir = itemView.findViewById(R.id.layout_fir);
            tvNameFir = (TextView) viewFir.findViewById(R.id.tv_name);
            ivBgFir = (ImageView) viewFir.findViewById(R.id.iv_bg);
            llTextFir = (LinearLayout) viewFir.findViewById(R.id.ll_text);
            tvIntroduceFir = (TextView) viewFir.findViewById(R.id.tv_introduce);

            viewSec = itemView.findViewById(R.id.layout_sec);
            tvNameSec = (TextView) viewSec.findViewById(R.id.tv_name);
            ivBgSec = (ImageView) viewSec.findViewById(R.id.iv_bg);
            llTextSec = (LinearLayout) viewSec.findViewById(R.id.ll_text);
            tvIntroduceSec = (TextView) viewSec.findViewById(R.id.tv_introduce);
        }

    }

    private class ViewHolder2 extends RecyclerView.ViewHolder {
        private  RecyclerView recyclerView;
        public AutoScrollViewPager autoVp;
        public ViewPager itemVp;
        public LinearLayout llpointsVp;

        public ViewHolder2(View view) {
            super(view);
            autoVp = (AutoScrollViewPager) view.findViewById(R.id.vp_auto);
            itemVp = (ViewPager) view.findViewById(R.id.viewpager);
            llpointsVp = (LinearLayout) view.findViewById(R.id.ll_vp_points);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        }
    }

    private class MyOnClickListener implements View.OnClickListener {

        private Context context;
        private BigVsBean bigV;

        public MyOnClickListener(BigVsBean bigV, Context context) {
            this.bigV = bigV;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            skipToKolDetail(bigV.getId(), context);
        }
    }
}
