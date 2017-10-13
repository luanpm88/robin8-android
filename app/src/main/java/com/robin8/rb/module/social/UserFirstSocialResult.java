package com.robin8.rb.module.social;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.robin8.rb.R;
import com.robin8.rb.autoviewpager.AutoScrollViewPager;
import com.robin8.rb.autoviewpager.RecyclingPagerAdapter;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.module.social.bean.UserPhotoBean;
import com.robin8.rb.module.social.view.HorizontalListView;
import com.robin8.rb.ui.widget.myprogress.RoundIndicatorView;
import com.robin8.rb.util.BitmapUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 第一次影响力测试结果
 * （弃用）
 */
public class UserFirstSocialResult extends BaseActivity {
private RoundIndicatorView testResultPro;
    private ImageView imgUserPhoto;
    private HorizontalListView mHlistView;
    public AutoScrollViewPager mAutoScrollViewPager;
    public LinearLayout llPointsVp;
    private List<UserPhotoBean> mDataList;
    private List<SocialPagerBean> mPagerDataList;
    private LinearLayout llHaveResult;
    private LinearLayout llNoResult;

    @Override
    public void setTitleView() {
        mTVCenter.setText("测评结果");
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_user_first_social_result, mLLContent);
        llHaveResult = ((LinearLayout) findViewById(R.id.layout_have_result));
        llNoResult = ((LinearLayout) findViewById(R.id.layout_no_result));
        testResultPro = ((RoundIndicatorView) view.findViewById(R.id.view_score_result));
        imgUserPhoto = ((ImageView) view.findViewById(R.id.img_user_photo));
        mHlistView = ((HorizontalListView) findViewById(R.id.list_recommend));
        mAutoScrollViewPager = (AutoScrollViewPager) view.findViewById(R.id.vp_auto);
        llPointsVp = (LinearLayout) view.findViewById(R.id.ll_vp_points);
        initData();
    }

    private void initData() {
        llHaveResult.setVisibility(View.VISIBLE);
        llNoResult.setVisibility(View.GONE);
        mDataList = new ArrayList<>();
        mPagerDataList = new ArrayList<>();
        //--
//        mDataList.add(new UserPhotoBean(R.mipmap.icon_weibo_orange));
//        mDataList.add(new UserPhotoBean(R.mipmap.icon_weibo_orange));
//        mDataList.add(new UserPhotoBean(R.mipmap.icon_weibo_orange));
//        mDataList.add(new UserPhotoBean(R.mipmap.icon_weibo_orange));
//        mDataList.add(new UserPhotoBean(R.mipmap.icon_weibo_orange));
//        mDataList.add(new UserPhotoBean(R.mipmap.icon_weibo_orange));
//        mDataList.add(new UserPhotoBean(R.mipmap.icon_weibo_orange));
//        mDataList.add(new UserPhotoBean(R.mipmap.icon_weibo_orange));
//        mDataList.add(new UserPhotoBean(R.mipmap.icon_weibo_orange));
//        mDataList.add(new UserPhotoBean(R.mipmap.icon_weibo_orange));
//        mDataList.add(new UserPhotoBean(R.mipmap.icon_weibo_orange));
//        mHlistView.setAdapter(new MyHoritalListAdapter(UserFirstSocialResult.this,mDataList));
        mPagerDataList.add(new SocialPagerBean("http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/kol_announcement/cover/2/cd953e6ef7.png"));
        mPagerDataList.add(new SocialPagerBean("http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/kol_announcement/cover/3/88cf6b2ddb.png"));
        mPagerDataList.add(new SocialPagerBean("http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/avatars/d3b3609f2e7b7e8e3cf5e12f784ca79f.jpg"));
        mPagerDataList.add(new SocialPagerBean("http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/kol_announcement/cover/4/067a84dd19.png"));
        mAutoScrollViewPager.setAdapter(new MyPagerAdapter(UserFirstSocialResult.this).setInfiniteLoop(true));
        mAutoScrollViewPager.setOnPageChangeListener(new MyOnPageChangeListener(mPagerDataList.size(), llPointsVp));
        if (mPagerDataList.size() != 1) {
            mAutoScrollViewPager.setInterval(5000);
            mAutoScrollViewPager.startAutoScroll();
        }
        mAutoScrollViewPager.setCurrentItem(10000 * mPagerDataList.size());
        setPoints(0, mPagerDataList.size(), llPointsVp);
    }
    private void setPoints(int position, int size, LinearLayout linearLayout) {
        linearLayout.removeAllViews();// 清空以前的点
        if (size < 2) {
            linearLayout.setVisibility(View.INVISIBLE);
            return;
        }
        for (int i = 0; i < size; i++) {
            View view = new View(UserFirstSocialResult.this);
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
    @Override
    protected void executeOnclickLeftView() {

    }

    @Override
    protected void executeOnclickRightView() {

    }
    class MyPagerAdapter extends RecyclingPagerAdapter {

        private Context context;
        private int size;
        private boolean isInfiniteLoop;

        public MyPagerAdapter(Context context) {
            this.context = context;
            this.size = mPagerDataList.size();
            isInfiniteLoop = false;
        }

        @Override
        public int getCount() {
            return isInfiniteLoop ? Integer.MAX_VALUE : mPagerDataList.size();
        }

        /**
         * get really position
         *
         * @param position
         * @return
         */
        private int getPosition(int position) {
            return isInfiniteLoop ? position % size : position;
        }

        @Override
        public View getView(final int position, View view, final ViewGroup container) {
            ViewHolder holder;
            int p = isInfiniteLoop ? position % size : position;
            if (view == null) {
                holder = new ViewHolder();
                view = holder.imageView = new ImageView(context);
                view.setTag(R.id.ll_load_more, holder);
            } else {
                holder = (ViewHolder) view.getTag(R.id.ll_load_more);
            }
            BitmapUtil.loadImage(context.getApplicationContext(), mPagerDataList.get(p).getUrl(), holder.imageView);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    skipToNext(position % size);
                }
            });
            return view;
        }

        public void skipToNext(int position) {

//            if(kolAnnouncements==null || kolAnnouncements.size()<=0 || kolAnnouncements.size()<=position){
//                return;
//            }
            Intent intent = null;
            SocialPagerBean item = mPagerDataList.get(position);
//            if(item!=null && kolAnnouncementsBean.getCategory()!=null){
//                String category = kolAnnouncementsBean.getCategory();
//                switch (category){
//                    case "link":
//                        intent = new Intent(context, WebViewActivity.class);
//                        intent.putExtra("title",kolAnnouncementsBean.getTitle());
//                        intent.putExtra("url", kolAnnouncementsBean.getContent());
//                        intent.putExtra("from", SPConstants.FIRST_PAGER);
//                        break;
//                    case "kol":
//                        intent = new Intent(context, KolDetailContentActivity.class);
//                        intent.putExtra("id", Integer.parseInt(kolAnnouncementsBean.getContent()));
//                        break;
//                }
//            }
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
        }

        private  class ViewHolder {
            ImageView imageView;
        }

        /**
         * @return the isInfiniteLoop
         */
        public boolean isInfiniteLoop() {
            return isInfiniteLoop;
        }

        /**
         * @param isInfiniteLoop the isInfiniteLoop to set
         */
        public MyPagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
            this.isInfiniteLoop = isInfiniteLoop;
            return this;
        }
    }

    private class SocialPagerBean{
        private String url;

        public SocialPagerBean(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

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
