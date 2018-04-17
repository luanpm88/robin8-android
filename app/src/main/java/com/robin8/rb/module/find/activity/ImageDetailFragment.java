package com.robin8.rb.module.find.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robin8.rb.R;
import com.robin8.rb.util.BitmapUtil;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 Created by zc on 2018/4/9. */

public class ImageDetailFragment extends Fragment implements PhotoViewAttacher.OnPhotoTapListener {
    private String mImageUrl;
    private Context mContext;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();
        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_find_look_img, container, false);
        PhotoView imageView = (PhotoView) view.findViewById(R.id.pv);
        view.findViewById(R.id.background_view).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((ImagePreviewActivity) mContext).finish();
                ((ImagePreviewActivity) mContext).overridePendingTransition(0, 0);
            }
        });
        imageView.setOnPhotoTapListener(this);
        BitmapUtil.loadFindImage(mContext,mImageUrl,imageView,false);
        return view;
    }

    @Override
    public void onPhotoTap(View view, float x, float y) {
        ((ImagePreviewActivity) mContext).finish();
        ((ImagePreviewActivity) mContext).overridePendingTransition(0, 0);
    }
}