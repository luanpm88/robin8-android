package com.robin8.rb.module.reword.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.module.reword.helper.DetailContentHelper;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.FileUtils;
import com.robin8.rb.view.widget.CustomDialogManager;

public class LookScreenImgActivity extends BaseActivity implements View.OnClickListener {


    private ImageView img;
    private ImageView img_two;
    private TextView mTvUpImg;
    private TextView mTvRefreshImg;
    public static final int IMAGE_REQUEST_CODE = 101;
    private String img_url;
    private ProgressBar pro;
    // private WProgressDialog mWProgressDialog;

    @Override
    public void setTitleView() {
        mTVCenter.setText(R.string.screenshot_reference);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_look_screen_img, mLLContent, true);
        img = ((ImageView) view.findViewById(R.id.iv_image));
        img_two = ((ImageView) view.findViewById(R.id.iv_image_two));
        mTvUpImg = ((TextView) view.findViewById(R.id.tv_up_img));
        mTvRefreshImg = ((TextView) view.findViewById(R.id.tv_refresh_img));
        pro = ((ProgressBar) view.findViewById(R.id.pb_refresh_progress));
        //  mWProgressDialog = WProgressDialog.createDialog(LookScreenImgActivity.this);
        mTvUpImg.setOnClickListener(this);
        mTvRefreshImg.setOnClickListener(this);
        shareCtaAndCti();
        ininData();
    }

    private void shareCtaAndCti() {
        View view = LayoutInflater.from(LookScreenImgActivity.this).inflate(R.layout.dialog_promit_cpa, null);
        TextView confirmTV = (TextView) view.findViewById(R.id.tv_confirm);
        TextView infoTv = (TextView) view.findViewById(R.id.tv_info);
        TextView topTv = (TextView) view.findViewById(R.id.tv_top);
        topTv.setText("截图审核规则");
        confirmTV.setText(R.string.known);
        infoTv.setText(R.string.screenshot_check_ruler);
        infoTv.setGravity(Gravity.LEFT);
        final CustomDialogManager cdm = new CustomDialogManager(LookScreenImgActivity.this, view);
        confirmTV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cdm.dismiss();
            }
        });
        cdm.dg.setCanceledOnTouchOutside(true);
        cdm.dg.getWindow().setGravity(Gravity.CENTER);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        cdm.showDialog();
    }

    private void ininData() {
        Intent intent = getIntent();
        img_url = intent.getStringExtra("img_url");
        BitmapUtil.loadImageNocrop(getApplicationContext(), img_url, img);
        BitmapUtil.loadImageNocrop(getApplicationContext(), img_url, img_two);
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (isDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_up_img:
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
                startActivityForResult(i, IMAGE_REQUEST_CODE);
                break;
            case R.id.tv_refresh_img:
               // mWProgressDialog.show();
               // BitmapUtil.loadImageNocrop(getApplicationContext(), img_url, img);
                //mWProgressDialog.dismiss();
               // pro.setVisibility(View.VISIBLE);
               // pro.setProgress(100);
                if (img.getVisibility()==View.VISIBLE){
                    img.setVisibility(View.GONE);
                    img_two.setVisibility(View.VISIBLE);
                }else {
                    img_two.setVisibility(View.GONE);
                    img.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    final String finalPicturePath = FileUtils.getAbsoluteImagePath(this, selectedImage);
                    data.putExtra("look_back_img",finalPicturePath);
                    setResult(DetailContentHelper.IMAGE_REQUEST_LOOK_CODE,data);
                    finish();
                    break;
            }
        }

    }
//    @Override
//    protected void onDestroy() {
//        if (mWProgressDialog != null) {
//            mWProgressDialog.dismiss();
//            mWProgressDialog = null;
//        }
//        super.onDestroy();
//    }

}