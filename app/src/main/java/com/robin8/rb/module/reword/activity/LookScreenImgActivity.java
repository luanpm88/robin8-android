package com.robin8.rb.module.reword.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private LinearLayout llBottom;
    private LinearLayout llBottomCheck;
    private TextView mTvLook;
    private TextView mTvUp;
    // private WProgressDialog mWProgressDialog;

    @Override
    public void setTitleView() {
        mTVCenter.setText(R.string.screenshot_reference);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_look_screen_img, mLLContent, true);
        //查看自己的截图
        //审核失败
        img = ((ImageView) view.findViewById(R.id.iv_image));
        img_two = ((ImageView) view.findViewById(R.id.iv_image_two));
        mTvUpImg = ((TextView) view.findViewById(R.id.tv_up_img));
        mTvRefreshImg = ((TextView) view.findViewById(R.id.tv_refresh_img));//刷新图片
        llBottom = (LinearLayout) view.findViewById(R.id.ll_bottom);
        //截图示例
        mTvLook = ((TextView) view.findViewById(R.id.tv_look_screenshot));//查看截图示例
        mTvUp = ((TextView) view.findViewById(R.id.tv_up));//上传截图
        llBottomCheck = (LinearLayout) view.findViewById(R.id.ll_bottom_check);
        pro = ((ProgressBar) view.findViewById(R.id.pb_refresh_progress));
        //  mWProgressDialog = WProgressDialog.createDialog(LookScreenImgActivity.this);
        mTvUpImg.setOnClickListener(this);
        mTvUp.setOnClickListener(this);
        mTvRefreshImg.setOnClickListener(this);
        mTvLook.setOnClickListener(this);
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
        String isAlive = intent.getStringExtra("bottom_isShow");
        if (TextUtils.isEmpty(isAlive)) {
            llBottom.setVisibility(View.VISIBLE);
            llBottomCheck.setVisibility(View.GONE);
        } else {
            if (isAlive.equals("0")) {
                mTVCenter.setText(R.string.screenshot_reference);
                llBottom.setVisibility(View.GONE);
                llBottomCheck.setVisibility(View.VISIBLE);
            } else if (isAlive.equals("1")) {
                mTVCenter.setText(R.string.look_the_screenshot);
                llBottom.setVisibility(View.VISIBLE);
                llBottomCheck.setVisibility(View.GONE);
            }
        }
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
            case R.id.tv_up:
            case R.id.tv_up_img:
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
                startActivityForResult(i, IMAGE_REQUEST_CODE);
                break;
            case R.id.tv_refresh_img:
                if (img.getVisibility() == View.VISIBLE) {
                    img.setVisibility(View.GONE);
                    img_two.setVisibility(View.VISIBLE);
                } else {
                    img_two.setVisibility(View.GONE);
                    img.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_look_screenshot:
                showSelectImageDialog(LookScreenImgActivity.this);
                break;
        }
    }

    private void showSelectImageDialog(final Activity activity) {

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_post_screenshot, null);
        TextView openGalleryTv = (TextView) view.findViewById(R.id.tv_open_gallery);
        TextView referenceScreenshotTv = (TextView) view.findViewById(R.id.tv_reference_screenshot);
        TextView helpTv = (TextView) view.findViewById(R.id.tv_help);
        TextView lookScreenShot = (TextView) view.findViewById(R.id.tv_look_screenshot);
        openGalleryTv.setVisibility(View.GONE);
        helpTv.setVisibility(View.VISIBLE);
        lookScreenShot.setText("朋友圈截图示例");
        lookScreenShot.setVisibility(View.VISIBLE);
        referenceScreenshotTv.setText("微信群组截图示例");
        referenceScreenshotTv.setVisibility(View.VISIBLE);
        final CustomDialogManager cdm = new CustomDialogManager(activity, view);
        helpTv.setText(R.string.cancel);
        lookScreenShot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                cdm.dismiss();
                BitmapUtil.loadImageNocrop(getApplicationContext(), img_url, img);
            }
        });
        referenceScreenshotTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cdm.dismiss();
                BitmapUtil.loadImageNocrop(getApplicationContext(), "http://7xuw3n.com1.z0.glb.clouddn.com/20501509603989_.pic.jpg", img);

            }
        });
        helpTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                cdm.dismiss();
            }
        });
        cdm.dg.setCanceledOnTouchOutside(true);
        cdm.dg.getWindow().setGravity(Gravity.BOTTOM);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        cdm.showDialog();
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
                    data.putExtra("look_back_img", finalPicturePath);
                    setResult(DetailContentHelper.IMAGE_REQUEST_LOOK_CODE, data);
                    finish();
                    break;
            }
        }

    }
}