package com.robin8.rb.module.first.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.robin8.rb.R;
import com.robin8.rb.autoviewpager.AutoScrollViewPager;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.model.BannerBean;
import com.robin8.rb.module.first.activity.KolDetailContentActivity;
import com.robin8.rb.module.first.activity.SearchKolActivity;
import com.robin8.rb.module.first.adapter.ImagePagerAdapter;
import com.robin8.rb.module.first.adapter.NewBigVRecyclerAdapter;
import com.robin8.rb.module.first.adapter.TagItemPagerAdapter;
import com.robin8.rb.module.first.model.BigVsBean;
import com.robin8.rb.module.first.model.KolAnnouncementsBean;

import java.util.List;

/**
 * Created by IBM on 2016/8/9.
 */
public class ViewPagerHelper {
    private  Activity mActivity;
    private Context mContext;
    private NewBigVRecyclerAdapter mNewBigVRecyclerAdapter;

    public ViewPagerHelper(Context context) {
        this.mContext = context;
    }

    public ViewPagerHelper(Activity activity) {
        this.mActivity = activity;
        this.mContext = activity.getApplicationContext();
    }

    public void initTagItemVp(ViewPager mViewPager) {
        TagItemPagerAdapter tagItemPagerAdapter = new TagItemPagerAdapter(mContext);
        tagItemPagerAdapter.setOnclickListener(new TagItemPagerAdapter.ItemOnclickListener() {
            @Override
            public void onClick(String tagNameCN, String tagNameEN) {
                skipToSearch(tagNameCN, tagNameEN, SPConstants.FIRST_PAGE_ITEM_TAG);
            }
        });
        mViewPager.setAdapter(tagItemPagerAdapter);
        mViewPager.setCurrentItem(1000 * 3);
    }

    public void skipToSearch(String tagNameCN, String tagNameEN, String from) {
        Intent intent = new Intent(mContext, SearchKolActivity.class);
        intent.putExtra("tag_name_cn", tagNameCN);
        intent.putExtra("tag_name", tagNameEN);
        intent.putExtra("url", CommonConfig.FIRST_KOL_LIST_URL);
        intent.putExtra("from", from);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 刷新头部
     *
     * @param kolAnnouncements
     */
    public void updateHeaderView(AutoScrollViewPager mAutoScrollViewPager, LinearLayout mLinearLayout, List<KolAnnouncementsBean> kolAnnouncements) {
        if (mAutoScrollViewPager == null || mLinearLayout == null || kolAnnouncements == null || kolAnnouncements.size() <= 0) {
            return;
        }

        int size = kolAnnouncements.size();
        mAutoScrollViewPager.setAdapter(new ImagePagerAdapter(mContext, kolAnnouncements).setInfiniteLoop(true));
        mAutoScrollViewPager.setOnPageChangeListener(new MyOnPageChangeListener(size, mLinearLayout));
        if (size != 1) {
            mAutoScrollViewPager.setInterval(5000);
            mAutoScrollViewPager.startAutoScroll();
        }
        mAutoScrollViewPager.setCurrentItem(10000 * size);
        setPoints(0, size, mLinearLayout);
    }

    /**
     * 刷新头部
     *
     */
    public void updateAutoVp(AutoScrollViewPager mAutoScrollViewPager, LinearLayout mLinearLayout, List<BannerBean> bannerList) {
        if (mAutoScrollViewPager == null || mLinearLayout == null || bannerList == null || bannerList.size() <= 0) {
            return;
        }

        int size = bannerList.size();
        mAutoScrollViewPager.setAdapter(new com.robin8.rb.adapter.ImagePagerAdapter(mActivity, bannerList).setInfiniteLoop(true));
        mAutoScrollViewPager.setOnPageChangeListener(new MyOnPageChangeListener(size, mLinearLayout));
        if (size != 1) {
            mAutoScrollViewPager.setInterval(5000);
            mAutoScrollViewPager.startAutoScroll();
        }
        mAutoScrollViewPager.setCurrentItem(10000 * size);
        setPoints(0, size, mLinearLayout);
    }

    private void setPoints(int position, int size, LinearLayout linearLayout) {
        linearLayout.removeAllViews();// 清空以前的点
        if (size < 2) {
            linearLayout.setVisibility(View.INVISIBLE);
            return;
        }
        for (int i = 0; i < size; i++) {
            View view = new View(mContext);
            view.setBackgroundResource(R.drawable.tab_dot_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (BaseApplication.mPixelDensityF * 7), (int) (BaseApplication.mPixelDensityF * 7));
            if (i != 0) {
                params.leftMargin = (int) (BaseApplication.mPixelDensityF * 7);
            }
            view.setEnabled(false);// 把所有的点变成灰色
            view.setLayoutParams(params);
            linearLayout.addView(view);
        }
        // 一开始把第一个点置为红色
        linearLayout.getChildAt(position).setEnabled(true);
    }

    public void initRecyclerView(RecyclerView recyclerView, final List<Object> mNewList) {
        mNewBigVRecyclerAdapter = new NewBigVRecyclerAdapter(mNewList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mNewBigVRecyclerAdapter);
        mNewBigVRecyclerAdapter.setOnRecyclerViewListener(new NewBigVRecyclerAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                Object obj = mNewList.get(position);
                if(obj instanceof BigVsBean){
                    BigVsBean bean = (BigVsBean) obj;
                    skipToKolDetail(bean.getId(), BaseApplication.getContext());
                }
            }
        });
    }

    private void skipToKolDetail(int id, Context context) {
        Intent intent = new Intent(context, KolDetailContentActivity.class);
        intent.putExtra("id", id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        private int size;
        private LinearLayout linearLayout;

        public MyOnPageChangeListener(int size, LinearLayout linearLayout) {
            this.size = size;
            this.linearLayout = linearLayout;
        }

        @Override
        public void onPageSelected(int position) {
            setPoints(position % size, size, linearLayout);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}
