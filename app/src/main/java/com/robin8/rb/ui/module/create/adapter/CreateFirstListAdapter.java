package com.robin8.rb.ui.module.create.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.robin8.rb.ui.activity.web.WebViewActivity;
import com.robin8.rb.ui.widget.autoviewpager.AutoScrollViewPager;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.helper.IconFontHelper;
import com.robin8.rb.ui.model.BannerBean;
import com.robin8.rb.ui.module.create.activity.ArticleListsActivity;
import com.robin8.rb.ui.module.create.activity.ProductListActivity;
import com.robin8.rb.ui.module.create.model.CpsArticlesBean;
import com.robin8.rb.ui.module.first.helper.ViewPagerHelper;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class CreateFirstListAdapter extends BaseRecyclerAdapter implements View.OnClickListener {
    private Activity mActivity;
    private ViewHolder mViewHolder;
    private static final String TAG = CreateFirstListAdapter.class.getSimpleName();
    private List<Object> mDataList;
    private ViewPagerHelper mViewPagerHelper;
    private List<BannerBean> mBannerBeanList = new ArrayList<>();
    private long lastTime;

    public enum ITEM_TYPE {
        HEADER,
        NORMAL
    }

    public void setDataList(List<Object> list) {
        this.mDataList = list;
    }


    public CreateFirstListAdapter(List<Object> list, Activity activity) {
        this.mDataList = list;
        this.mActivity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i, boolean isItem) {
        if (i == 0) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.create_first_header, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(lp);
            return new ViewHolder2(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.create_first_list_item, null);
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
        if (obj == null) {
            if (viewHolder instanceof ViewHolder2) {
                ViewHolder2 holder = (ViewHolder2) viewHolder;
                setHeaderItem(holder);
            }
        } else  if (obj instanceof CpsArticlesBean) {
            CpsArticlesBean bean = (CpsArticlesBean) obj;
            if (viewHolder instanceof ViewHolder) {
                ViewHolder holder = (ViewHolder) viewHolder;
                setNormalItem(holder, bean);
            }
        }
    }
    /**
     * 填充头布局
     *
     * @param holder
     */
    private void setHeaderItem(ViewHolder2 holder) {
        BannerBean bean0 = new BannerBean(R.mipmap.pic_create_banner_0, BannerBean.CREATE_EARN_MONEY);
        BannerBean bean1 = new BannerBean(R.mipmap.pic_create_banner_1, BannerBean.HOW_CREATE);
        mBannerBeanList.clear();
        mBannerBeanList.add(bean0);
        mBannerBeanList.add(bean1);

        mViewPagerHelper = new ViewPagerHelper(mActivity);
        mViewPagerHelper.updateAutoVp(holder.autoVp, holder.llVpPoints, mBannerBeanList);
    }
    private void setNormalItem(ViewHolder holder, CpsArticlesBean bean) {
        holder.tvTitle.setText(bean.getTitle());
        if(bean.getMaterial_total_price() != 0){
            String str = mActivity.getString(R.string.expected_cut) + "<font color=#ecb200> ¥ " + StringUtil.deleteZero(bean.getMaterial_total_price()) + "</font>";
            holder.tvExpected.setText(Html.fromHtml(str));
            holder.tvExpected.setVisibility(View.VISIBLE);
        }else {
            holder.tvExpected.setVisibility(View.INVISIBLE);
        }

        IconFontHelper.setTextIconFont(holder.tvShareIcon, R.string.share_sign);
        if (bean.getAuthor()!=null){
            holder.tvUserName.setText(bean.getAuthor().getName());
            BitmapUtil.loadImage(mActivity.getApplicationContext(), bean.getAuthor().getAvatar_url(), holder.civUser);
        }else {
            holder.tvUserName.setText("");
            BitmapUtil.loadImage(mActivity.getApplicationContext(), CommonConfig.APP_ICON, holder.civUser);
        }

        holder.tvShareNum.setText(String.valueOf(bean.getCps_article_share_count()));
        setImageView(holder.ivBg, holder.rlContent, bean.getCover());
      //  BitmapUtil.loadImage(mActivity.getApplicationContext(), bean.getAuthor().getAvatar_url(), holder.civUser);
        holder.ivBg.setOnClickListener(new MyOnClickListener(holder.ivBg.getContext(), bean));
    }

    private void skipToKolDetail(CpsArticlesBean bean, Context context) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("title", R.string.robin451);
        intent.putExtra("url", bean.getShow_url());
        intent.putExtra("id", bean.getId());
        intent.putExtra("from", SPConstants.CREATE_FIRST_LIST);
        intent.putExtra("img_url", bean.getCover());
        intent.putExtra("share_title", bean.getTitle());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void setImageView(final ImageView ivBg, final View view, String url) {
        BitmapUtil.loadImage(ivBg.getContext(), url, ivBg);
        ivBg.post(new Runnable() {
            @Override
            public void run() {
                ivBg.getLayoutParams().height = DensityUtils.getScreenWidth(ivBg.getContext()) * 9 / 16;
                view.getLayoutParams().height = DensityUtils.getScreenWidth(ivBg.getContext()) * 9 / 16;
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
        public ImageView civUser;
        public ImageView ivBg;
        public View rlContent;
        public TextView tvTitle;
        public TextView tvExpected;
        public TextView tvUserName;
        public TextView tvShareNum;
        public TextView tvShareIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            ivBg = (ImageView) itemView.findViewById(R.id.iv_bg);
            rlContent = itemView.findViewById(R.id.rl_content);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvExpected = (TextView) itemView.findViewById(R.id.tv_expected);
            civUser = (ImageView) itemView.findViewById(R.id.civ_user);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            tvShareNum = (TextView) itemView.findViewById(R.id.tv_share_num);
            tvShareIcon = (TextView) itemView.findViewById(R.id.tv_share_icon);
        }
    }

    private class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        public AutoScrollViewPager autoVp;
        public ImageView ivProduct;
        public ImageView ivArticle;
        public LinearLayout llVpPoints;
        public ViewHolder2(View view) {
            super(view);
//            view.findViewById(R.id.iv_teach_me).setOnClickListener(this);
//            view.findViewById(R.id.iv_choose_product).setOnClickListener(this);
            autoVp = (AutoScrollViewPager) view.findViewById(R.id.auto_scroll_view_pager);
            ivProduct = (ImageView) view.findViewById(R.id.iv_product);
            ivArticle = (ImageView) view.findViewById(R.id.iv_article);
            llVpPoints = (LinearLayout) view.findViewById(R.id.ll_vp_points);
            ivProduct.setOnClickListener(this);
            ivArticle.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (!isDoubleClick()) {
                Context context = v.getContext();
                Intent intent;
                switch (v.getId()) {
                    case R.id.iv_product:
                        intent = new Intent(context, ProductListActivity.class);
                        intent.putExtra("destination", SPConstants.PRODUCT_LIST);
                        intent.putExtra("from", SPConstants.CREATE_FIRST_LIST_PRODUCT);
                        intent.putExtra("url", HelpTools.getUrl(CommonConfig.PRODUCT_LIST_URL));
                        intent.putExtra("title", context.getString(R.string.product));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case R.id.iv_article:
                        intent = new Intent(context, ArticleListsActivity.class);
                        intent.putExtra("destination", SPConstants.ARTICLE_LIST);
                        intent.putExtra("url", HelpTools.getUrl(CommonConfig.ARTICLES_LIST_URL));
                        intent.putExtra("title", context.getString(R.string.article));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
//                    case R.id.iv_teach_me:
//                        intent = new Intent(v.getContext(), HelpCenterActivity.class);
//                        intent.putExtra("from",SPConstants.CREATE_FIRST_LIST);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(intent);
//                        break;
//                    case R.id.iv_choose_product:
//                        intent = new Intent(context, ProductListActivity.class);
//                        intent.putExtra("destination", SPConstants.PRODUCT_LIST);
//                        intent.putExtra("from", SPConstants.CREATE_FIRST_LIST_PRODUCT);
//                        intent.putExtra("url", HelpTools.getUrl(CommonConfig.PRODUCT_LIST_URL));
//                        intent.putExtra("title", context.getString(R.string.product));
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(intent);
//                        break;
                }
            }
        }
    }

    public boolean isDoubleClick() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastTime < 800) {
            return true;
        }
        lastTime = currentTimeMillis;
        return false;
    }

    private class MyOnClickListener implements View.OnClickListener {

        private CpsArticlesBean bean;
        private Context context;

        public MyOnClickListener(Context context, CpsArticlesBean bean) {
            this.bean = bean;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            skipToKolDetail(bean, context);
        }
    }

}
