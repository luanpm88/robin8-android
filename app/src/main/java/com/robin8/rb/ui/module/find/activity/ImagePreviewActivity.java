package com.robin8.rb.ui.module.find.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.ui.module.find.model.ImageInfo;
import com.robin8.rb.ui.module.reword.chose_photo.widget.ViewPagerFixed;

import java.util.List;

public class ImagePreviewActivity extends AppCompatActivity {
    public static final String IMAGE_INFO = "IMAGE_INFO";
    public static final String CURRENT_ITEM = "CURRENT_ITEM";
    private static final String STATE_POSITION = "STATE_POSITION";
    private ImagePagerAdapter imagePreviewAdapter;
    private List<ImageInfo> imageInfo;
    private int currentItem;
    private ViewPagerFixed viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_image_preview);
        viewPager = (ViewPagerFixed) findViewById(R.id.viewPager);
        final TextView tv_pager = (TextView) findViewById(R.id.tv_pager);
        Intent intent = getIntent();
        imageInfo = (List<ImageInfo>) intent.getSerializableExtra(IMAGE_INFO);
        currentItem = intent.getIntExtra(CURRENT_ITEM, 0);
        imagePreviewAdapter = new ImagePagerAdapter(getSupportFragmentManager(), imageInfo);
        viewPager.setAdapter(imagePreviewAdapter);
        viewPager.setCurrentItem(currentItem);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                tv_pager.setText(String.format(getString(R.string.image_index), currentItem + 1, imageInfo.size()));
            }
        });
        tv_pager.setText(String.format(getString(R.string.image_index), currentItem + 1, imageInfo.size()));
//        if (savedInstanceState != null) {
//            currentItem = savedInstanceState.getInt(STATE_POSITION);
//        }
    }
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        outState.putInt(STATE_POSITION, viewPager.getCurrentItem());
//    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public List<ImageInfo> fileList;

        public ImagePagerAdapter(FragmentManager fm, List<ImageInfo> fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return ImageDetailFragment.newInstance(fileList.get(position).getThumbnailUrl());
        }

    }
}
