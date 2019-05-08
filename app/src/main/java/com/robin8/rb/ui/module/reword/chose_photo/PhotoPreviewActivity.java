package com.robin8.rb.ui.module.reword.chose_photo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.ui.module.reword.chose_photo.widget.ViewPagerFixed;

import java.util.ArrayList;

public class PhotoPreviewActivity extends BaseActivity implements PhotoPagerAdapter.PhotoViewClickListener {

    public static final String EXTRA_PHOTOS = "extra_photos";
    public static final String EXTRA_PHOTOS_MAP = "extra_photos_map";
    public static final String EXTRA_PHOTO = "extra_photo";
    public static final String EXTRA_PHOTO_TITLE = "extra_photo_title";
    public static final String EXTRA_CURRENT_ITEM = "extra_current_item";

    /**
     选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合
     */
    public static final String EXTRA_RESULT = "preview_result";

    /**
     预览请求状态码
     */
    public static final int REQUEST_PREVIEW = 99;

    private ArrayList<String> paths;
    private ViewPagerFixed mViewPager;
    private PhotoPagerAdapter mPagerAdapter;
    private int currentItem = 0;
    public TextView tvPages;


    @Override
    public void setTitleView() {
        mTVCenter.setText(R.string.preview);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_image_preview, mLLContent, true);
        mViewPager = (ViewPagerFixed) view.findViewById(R.id.vp_photos);
        tvPages = ((TextView) view.findViewById(R.id.tv_pages));
        paths = new ArrayList<>();
        ArrayList<String> pathArr = getIntent().getStringArrayListExtra(EXTRA_PHOTOS);
        final ArrayList<String> titleArr = getIntent().getStringArrayListExtra(EXTRA_PHOTO_TITLE);
        String pathSingle = getIntent().getStringExtra(EXTRA_PHOTO);
        Bundle bundle = getIntent().getExtras();
        final SerializableMap mapPaths = (SerializableMap) bundle.get(EXTRA_PHOTOS_MAP);
        if (pathArr != null) {
            paths.addAll(pathArr);
        } else {
            if (mapPaths != null) {
                if (mapPaths.getMap() != null) {
                    for (int i = 0; i < mapPaths.getMap().size(); i++) {
                        paths.add(mapPaths.getMap().get(i));
                    }
                }
            } else {
                if (pathSingle != null) {
                    paths.add(pathSingle);
                }
            }
        }
        //  paths.remove(pathArr.size()-1);
        currentItem = getIntent().getIntExtra(EXTRA_CURRENT_ITEM, 0);
        mPagerAdapter = new PhotoPagerAdapter(this, paths);
        mPagerAdapter.setPhotoViewClickListener(this);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(currentItem);
        mViewPager.setOffscreenPageLimit(5);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //   updateActionBarTitle();
                if (mapPaths!=null){
                    if (mapPaths.getMap().size()!=0){
                        tvPages.setText(getString(R.string.image_index, mViewPager.getCurrentItem() + 1, mapPaths.getMap().size()));
                    }
                    if (titleArr!=null&& titleArr.size()==mapPaths.getMap().size()){
                        mTVCenter.setText(titleArr.get(position));
                    }else {
                        mTVCenter.setText("----");
                    }
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //  updateActionBarTitle();
    }

    private void initViews() {

    }

    @Override
    public void OnPhotoTapListener(View view, float v, float v1) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT, paths);
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }

}
