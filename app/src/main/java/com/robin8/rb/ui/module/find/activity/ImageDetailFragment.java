package com.robin8.rb.ui.module.find.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.ui.module.find.ImageDownLoadCallBack;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.DownLoadImageService;
import com.robin8.rb.ui.dialog.CustomDialogManager;

import java.io.File;

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


        imageView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                showDownDialog((Activity) mContext);
                return false;
            }
        });
        BitmapUtil.loadFindImage(mContext, mImageUrl, imageView, false);
        return view;
    }

    private void showDownDialog(final Activity activity) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_down_load_img, null);
        TextView tvDown = (TextView) view.findViewById(R.id.tv_down_load);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        //邀请好友一起赚钱

        final CustomDialogManager mCustomDialogManager = new CustomDialogManager(activity, view);
        tvDown.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mCustomDialogManager.dismiss();
                onDownLoad(mImageUrl);
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mCustomDialogManager.dismiss();
            }
        });
        mCustomDialogManager.dg.setCanceledOnTouchOutside(true);
        mCustomDialogManager.dg.getWindow().setGravity(Gravity.BOTTOM);
        mCustomDialogManager.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        mCustomDialogManager.showDialog();
    }

    private final int MSG_VISIBLE = 0;
    private final int MSG_ERROR = 1;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_VISIBLE:
                    CustomToast.showShort(mContext, "保存成功！");
                    break;
                case MSG_ERROR:
                    CustomToast.showShort(mContext, "保存失败");
                    break;
            }

        }
    };

    /**
     启动图片下载线程
     */
    private void onDownLoad(String url) {
        DownLoadImageService service = new DownLoadImageService(mContext, url, new ImageDownLoadCallBack() {

            @Override
            public void onDownLoadSuccess(File file) {
            }

            @Override
            public void onDownLoadSuccess(Bitmap bitmap) {
                // 在这里执行图片保存方法
                Message message = new Message();
                message.what = MSG_VISIBLE;
                handler.sendMessageDelayed(message, 1000);
            }

            @Override
            public void onDownLoadFailed() {
                // 图片保存失败
                Message message = new Message();
                message.what = MSG_ERROR;
                handler.sendMessageDelayed(message, 1000);
            }
        });
        //启动图片下载线程
        new Thread(service).start();
    }


    @Override
    public void onPhotoTap(View view, float x, float y) {
        ((ImagePreviewActivity) mContext).finish();
        ((ImagePreviewActivity) mContext).overridePendingTransition(0, 0);
    }
}