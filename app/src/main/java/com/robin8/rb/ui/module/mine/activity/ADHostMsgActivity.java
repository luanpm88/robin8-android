package com.robin8.rb.ui.module.mine.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.ui.model.BaseBean;
import com.robin8.rb.ui.module.mine.model.ADModel;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.FileUtils;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.ui.widget.CircleImageView;
import com.robin8.rb.ui.dialog.CustomDialogManager;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashMap;

public class ADHostMsgActivity extends BaseActivity {

    private CircleImageView imgUserPhoto;
    private EditText etBrandName;
    private EditText etName;
    private EditText etAddress;
    private EditText etIntroduce;
    private BasePresenter mBasePresenter;
    private WProgressDialog mWProgressDialog;
    private Uri mImageUri;
    private Bitmap mBitmap;
    private String mFinalPicturePath;
    private RelativeLayout llPhoto;
    private ADModel bean;
    private CustomDialogManager cdm;

    @Override
    public void setTitleView() {
        mTVCenter.setText(R.string.mine_ad_host);
        mBottomTv.setVisibility(View.VISIBLE);
        mBottomTv.setText(R.string.save);
        mBottomTv.setOnClickListener(this);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_adhost_msg, mLLContent, true);
        llPhoto = ((RelativeLayout) view.findViewById(R.id.ll_photo));
        imgUserPhoto = ((CircleImageView) view.findViewById(R.id.civ_image));
        etBrandName = ((EditText) view.findViewById(R.id.edit_brand_name));
        etName = ((EditText) view.findViewById(R.id.edit_name));
        etAddress = ((EditText) view.findViewById(R.id.edit_address));
        etIntroduce = ((EditText) view.findViewById(R.id.edit_introduce));
        llPhoto.setOnClickListener(this);
        Intent intent = getIntent();
        Serializable serializable = intent.getSerializableExtra("bean");
        if (! (serializable instanceof ADModel)) {
            return;
        }
        bean = (ADModel) serializable;
        if (bean != null) {
            if (! TextUtils.isEmpty(bean.getAvatar_url())) {
                BitmapUtil.loadImage(this, bean.getAvatar_url(), imgUserPhoto);
            }
            if (! TextUtils.isEmpty(bean.getCampany_name())) {
                etName.setText(bean.getName());
                etName.setSelection(etName.getText().length());
            }
            if (! TextUtils.isEmpty(bean.getName())) {
                etBrandName.setText(bean.getName());
                etBrandName.setSelection(etBrandName.getText().length());
            }
            if (! TextUtils.isEmpty(bean.getUrl())) {
                etAddress.setText(bean.getUrl());
                etAddress.setSelection(etAddress.getText().length());
            }
            if (! TextUtils.isEmpty(bean.getDescription())) {
                etIntroduce.setText(bean.getDescription());
                etIntroduce.setSelection(etIntroduce.getText().length());
            }
        }
        showMyDialog();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_photo:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SPConstants.IMAGE_REQUEST_CODE);
                break;
            case R.id.tv_bottom:
                putData();
                break;
        }
    }

    private void putData() {
        if (StringUtil.isEmpty(etBrandName.getText().toString().trim())) {
            CustomToast.showShort(ADHostMsgActivity.this, R.string.robin467);
            return;
        } else if (StringUtil.isEmpty(etName.getText().toString().trim())) {
            CustomToast.showShort(ADHostMsgActivity.this, R.string.robin468);
            return;
        } else {
            if (mWProgressDialog == null) {
                mWProgressDialog = WProgressDialog.createDialog(this);
            }
            mWProgressDialog.show();
            LinkedHashMap<String, Object> requestMap = new LinkedHashMap<>();
            requestMap.put("name", etBrandName.getText().toString().trim());
            requestMap.put("campany_name", etName.getText().toString().trim());
            requestMap.put("url", etAddress.getText().toString().trim());
            requestMap.put("description", etIntroduce.getText().toString().trim());
            File file = null;
            String imageName = "";
            if (! TextUtils.isEmpty(mFinalPicturePath)) {
                imageName = mFinalPicturePath.substring(mFinalPicturePath.lastIndexOf("/") + 1);
                file = new File(mFinalPicturePath);
            }
            HttpRequest.getInstance().put(true, HelpTools.getUrl(CommonConfig.BRAND_MSG_URL), "avatar", imageName, file, requestMap, new RequestCallback() {

                @Override
                public void onError(Exception e) {
                    if (mWProgressDialog != null) {
                        mWProgressDialog.dismiss();
                    }
                }

                @Override
                public void onResponse(String response) {
                    LogUtil.LogShitou("提交品牌主信息", response);
                    if (mWProgressDialog != null) {
                        mWProgressDialog.dismiss();
                    }
                    BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);
                    if (baseBean != null) {
                        if (baseBean.getError() == 0) {
                            show(3000);
                        }else {
                            if(!TextUtils.isEmpty(baseBean.getDetail())){
                                CustomToast.showShort(ADHostMsgActivity.this,baseBean.getDetail());
                            }
                        }
                    }
                }
            });
        }
    }

    public void show(int duration) {
        TimeCount timeCount = new TimeCount(duration, 1000);
        timeCount.start();
    }
    private void showMyDialog() {
        View view = LayoutInflater.from(ADHostMsgActivity.this).inflate(R.layout.toast_layout, null);
        TextView infoTv = (TextView) view.findViewById(R.id.toast_msg);
        infoTv.setText(R.string.robin371);
        infoTv.setGravity(Gravity.CENTER);
        cdm = new CustomDialogManager(ADHostMsgActivity.this, view);
        cdm.dg.setCanceledOnTouchOutside(true);
        cdm.dg.getWindow().setGravity(Gravity.CENTER);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
       // cdm.showDialog();
    }

    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval); //millisInFuture总计时长，countDownInterval时间间隔(一般为1000ms)
        }

        @Override
        public void onTick(long millisUntilFinished) {
           cdm.showDialog();
        }

        @Override
        public void onFinish() {
            cdm.dismiss();
            setResult(SPConstants.UPDATA_BRAND_MSG);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SPConstants.IMAGE_REQUEST_CODE:
                    mImageUri = data.getData();
                    BitmapUtil.cropImageUri(mImageUri, 500, 500, SPConstants.RESULT_CROP_CODE, 1, 1, this);
                    break;
                case SPConstants.RESULT_CROP_CODE:
                    mBitmap = BitmapUtil.decodeUriAsBitmap(mImageUri, this);
                    try {
                        mFinalPicturePath = FileUtils.saveBitmapToSD(mBitmap, "temp" + SystemClock.currentThreadTimeMillis());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imgUserPhoto.setImageBitmap(mBitmap);
                    break;
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
    protected void onDestroy() {
        if (! TextUtils.isEmpty(mFinalPicturePath)) {
            BitmapUtil.deleteBm(mFinalPicturePath);
        }
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
        NotifyManager.getNotifyManager().deleteObserver(this);
        super.onDestroy();
    }
}
