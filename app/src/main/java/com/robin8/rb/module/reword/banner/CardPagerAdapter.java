package com.robin8.rb.module.reword.banner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.robin8.rb.R;
import com.robin8.rb.model.BannerBean;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<RelativeLayout> mViews;
    public Context mContext;

    public List<BannerBean> mData;
    private float mBaseElevation;
    public CardPagerAdapter(Context context, List<BannerBean> list) {
        mContext = context;
        mViews = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            mViews.add(null);
        }
        this.mData = list;
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public RelativeLayout getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_banner, container, false);
        container.addView(view);
        RelativeLayout cardView = (RelativeLayout) view.findViewById(R.id.cardView);
      //  CircleImageView mIcon=(CircleImageView)view.findViewById(R.id.profile_image);
     //   TextView mName= (TextView) view.findViewById(R.id.name);
//        if (mBaseElevation == 0) {
//            mBaseElevation = cardView.getCardElevation();
//        }
        ImageView imgView = (ImageView) view.findViewById(R.id.img);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener!=null)mOnItemClickListener.onClick(position);
            }
        });
       // cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);

        final BannerBean item=mData.get(position);
      //  mIcon.setImageResource(item.getImg());
       // mName.setText(item.getName());
     //  imgView.setBackgroundResource(mData.get(position%mData.size()).resId);
      //  imgView.setImageResource(R.mipmap.binner);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }
    OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener=mOnItemClickListener;
    }
    public interface OnItemClickListener{
        void onClick(int position);
    }
}
