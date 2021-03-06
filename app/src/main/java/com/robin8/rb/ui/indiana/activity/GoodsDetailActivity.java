package com.robin8.rb.ui.indiana.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.ui.indiana.model.DetailImageModel;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;


/**
 * 商品详情
 */
public class GoodsDetailActivity extends BaseActivity {

    private String code;
    private ListView mListView;
    private String[] pictures;
    private String name;
    private LinearLayout mLLPic;

    @Override
    public void setTitleView() {
    }

    @Override
    public void initView() {

        Intent intent = getIntent();
        code = intent.getStringExtra("code");
        name = intent.getStringExtra("name");
        mTVCenter.setText(name);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_goods_detail, mLLContent);
        mLLPic  = (LinearLayout) view.findViewById(R.id.ll_pic);
        BasePresenter presenter = new BasePresenter();
        RequestParams params = new RequestParams();
        params.put("code", code);
        presenter.getDataFromServer(true, HttpRequest.GET,
                HelpTools.getUrl(CommonConfig.LOTTERY_ACTIVITIES_URL + "/" + code + "/desc"), params, new RequestCallback() {

                    @Override
                    public void onError(Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        parseJson(response);
                    }
                });
    }

    private void parseJson(String response) {
        DetailImageModel detailImageModel = GsonTools.jsonToBean(response, DetailImageModel.class);
        String[] pictures = detailImageModel.getPictures();
        for(int i=0;i<pictures.length;i++){

            Glide.with(GoodsDetailActivity.this)
                    .load(pictures[i])
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {

                        @Override
                        public void onResourceReady(final Bitmap bitmap, GlideAnimation glideAnimation) {
                            ImageView iv = new ImageView(GoodsDetailActivity.this);
                            int bitmapH = bitmap.getHeight();
                            int bitmapW = bitmap.getWidth();
                            LogUtil.logXXfigo("bitmapH=" + bitmapH + "  bitmapW=" + bitmapW);
                            int screenWidth = DensityUtils.getScreenWidth(GoodsDetailActivity.this);
                            iv.setImageBitmap(bitmap);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(screenWidth,
                                    bitmapH * screenWidth / bitmapW);
                            mLLPic.addView(iv, lp);
                        }
                    });

        }
     }

//    private void parseJson(String response) {
//        DetailImageModel detailImageModel = GsonTools.jsonToBean(response, DetailImageModel.class);
//        if (detailImageModel != null && detailImageModel.getError() == 0) {
//            pictures = detailImageModel.getPictures();
//
//            mListView = new ListView(this);
//            mListView.setFadingEdgeLength(10);
//            mListView.setDivider(null);
//            mListView.setDividerHeight(0);
//            mListView.setSelector(android.R.color.transparent);
//            mListView.setVerticalScrollBarEnabled(false);
//            mListView.setAdapter(new MyBaseAdapter());
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT);
//            mLLContent.addView(mListView, lp);
//        }
//    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.MY_INDIANA_DETAIL_DES;
        super.onResume();
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
    }

    class MyBaseAdapter extends BaseAdapter {

        private Holder holder;

        @Override
        public int getCount() {
            return pictures.length;
        }

        @Override
        public Object getItem(int position) {
            return pictures[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new Holder();
                convertView = LayoutInflater.from(GoodsDetailActivity.this).inflate(R.layout.activity_goods_detail, null);
                holder.iv = (ImageView) convertView.findViewById(R.id.iv);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            Glide.with(GoodsDetailActivity.this)
                    .load(pictures[position])
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {

                        @Override
                        public void onResourceReady(final Bitmap bitmap, GlideAnimation glideAnimation) {
//                            BitmapUtil.loadImage(GoodsDetailActivity.this, pictures[position], holder.iv);
                            int bitmapH = bitmap.getHeight();
                            int bitmapW = bitmap.getWidth();
                            LogUtil.logXXfigo("bitmapH=" + bitmapH + "  bitmapW=" + bitmapW);
                            int screenWidth = DensityUtils.getScreenWidth(GoodsDetailActivity.this);
                            ViewGroup.LayoutParams layoutParams = holder.iv.getLayoutParams();
                            layoutParams.width = screenWidth;
                            layoutParams.height = bitmapH * screenWidth / bitmapW;
                            holder.iv.setLayoutParams(layoutParams);
                            holder.iv.setImageBitmap(bitmap);
                        }
                    });
            return convertView;
        }
    }

    class Holder {
        public ImageView iv;
    }
}
