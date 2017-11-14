package com.robin8.rb.module.mine.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.model.BaseBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.util.AppUtils;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.LinkedHashMap;


public class FeedBackActivity extends BaseActivity {
    private EditText etname;
    private ImageView mImageView;
    private LinearLayout mImageLL;
    private boolean mHaveBitmap;

    @Override
    public void setTitleView() {
        mTVCenter.setText(R.string.feed_back);
    }

    @Override
    public void initView() {
        mBottomTv.setVisibility(View.VISIBLE);
        mBottomTv.setOnClickListener(this);
        mBottomTv.setText(R.string.submit);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_feed_back, mLLContent);
        etname = (EditText) view.findViewById(R.id.et_name);
        mImageView = (ImageView) view.findViewById(R.id.iv);
        mImageLL = (LinearLayout) view.findViewById(R.id.ll_image);
        initData();
    }

    protected void initData() {

        Bundle extras = getIntent().getExtras();
        if(extras == null){
            return;
        }
        mHaveBitmap = extras.getBoolean("bitmap", false);

        if (!mHaveBitmap)
            mImageLL.setVisibility(View.INVISIBLE);
        else {
            mImageLL.setVisibility(View.VISIBLE);
            mImageView.setImageBitmap(BaseApplication.getInstance().getScreenShot());
        }
        boolean feedbackB = CacheUtils.getBoolean(this, SPConstants.FEEDBACK_TOGGLE, true);
        if (!feedbackB) {
            stopShakeListener();
        }
        mBottomTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_bottom:
                sendFeedBack(etname.getText().toString());
                break;
        }
    }

    private void sendFeedBack(String content) {
        if (TextUtils.isEmpty(etname.getText().toString())) {
            CustomToast.showShort(this, "请输入反馈意见");
            return;
        }

        LinkedHashMap<String, Object> requestMap = new LinkedHashMap<>();
        requestMap.put("app_version", AppUtils.getVersionName(this));
        requestMap.put("app_platform", SPConstants.ANDROID);
        requestMap.put("os_version", AppUtils.getSystemVersion());
        requestMap.put("device_model", android.os.Build.MODEL);
        requestMap.put("content", content);

        File file = null;
        if(mHaveBitmap){
            Bitmap bitmap = compressBitmap(BaseApplication.getInstance().getScreenShot());
            if (bitmap!=null){
                file = new File(BitmapUtil.saveBitmap
                        (BitmapUtil.path + File.separator + "screenShot", bitmap));
            }else {
                CustomToast.showShort(FeedBackActivity.this, "感谢您的反馈");
                FeedBackActivity.this.finish();
            }
        }
        HttpRequest.getInstance().post(true, HelpTools.getUrl(CommonConfig.FEED_BACK_URL), "screenshot", "screenshot", file, requestMap, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                CustomToast.showShort(FeedBackActivity.this, getString(R.string.please_data_wrong));
            }

            @Override
            public void onResponse(String response) {
                BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);
                if (null == baseBean || baseBean.getError() == 0) {
                    CustomToast.showShort(FeedBackActivity.this, "感谢您的反馈");
                    FeedBackActivity.this.finish();
                }else if (baseBean.getError()==1){
                    if (!TextUtils.isEmpty(baseBean.getDetail())){
                        CustomToast.showShort(FeedBackActivity.this,baseBean.getDetail());
                        finish();
                    }else {
                        CustomToast.showShort(FeedBackActivity.this, "感谢您的反馈");
                        finish();
                    }
                }else {
                    finish();
                }
            }
        });
    }

    private Bitmap compressBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (bitmap.isRecycled()){
           return null;
        }else {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        if (baos.toByteArray().length / 1024 > 1000)
            options = 20;
        else if (baos.toByteArray().length / 1024 > 2000)
            options = 10;
        while (baos.toByteArray().length / 1024 > 200) {  //循环判断如果压缩后图片是否大于200kb,大于继续压缩
            if (options <= 1)
                break;
            baos.reset();//重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            //这里压缩options%，把压缩后的数据存放到baos中
            options -= 3;//每次都减少10
        }
        bitmap.recycle();
        byte[] bytes = baos.toByteArray();
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);//很容易 OOM
        }
        //return bitmap;
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (BaseApplication.getInstance().getScreenShot()!=null){
            if (!BaseApplication.getInstance().getScreenShot().isRecycled()){
                BaseApplication.getInstance().getScreenShot().recycle();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
    }
}
