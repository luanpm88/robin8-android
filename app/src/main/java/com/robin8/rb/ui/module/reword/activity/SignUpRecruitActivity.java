package com.robin8.rb.ui.module.reword.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.ui.model.CampaignInviteBean;
import com.robin8.rb.ui.model.CampaignListBean;
import com.robin8.rb.ui.model.NotifyMsgEntity;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.util.FileUtils;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 招募活动报名
 */
public class SignUpRecruitActivity extends BaseActivity {

    @Bind(R.id.tv_word)
    TextView tvWord;
    @Bind(R.id.et_word)
    EditText etWord;
    @Bind(R.id.tv_picture)
    TextView tvPicture;
    @Bind(R.id.iv_post)
    ImageView ivPost;
    private WProgressDialog mWProgressDialog;
    private CampaignListBean.CampaignInviteEntity.CampaignEntity mCampaignEntity;
    private Uri mImageUri;
    private Bitmap mBitmap;
    private String mFinalPicturePath;
    private boolean mImageLoadB;
    private int mScreenWidth;
    private int mScreenHeight;

    @Override
    public void setTitleView() {
        mTVCenter.setText(R.string.sign_up_recruit);
    }

    @Override
    public void initView() {
        mScreenWidth = DensityUtils.getScreenWidth(this);
        mScreenHeight = DensityUtils.getScreenHeight(this);
        mBottomTv.setVisibility(View.VISIBLE);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_sign_up_recruit, mLLContent);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Serializable serializable = intent.getSerializableExtra("campaign_invite_entity");
        if (serializable instanceof CampaignListBean.CampaignInviteEntity.CampaignEntity) {
            mCampaignEntity = (CampaignListBean.CampaignInviteEntity.CampaignEntity) serializable;
        }

        if (mCampaignEntity == null) {
            return;
        }

        if (mCampaignEntity.is_applying_note_required()) {
            tvWord.setText(mCampaignEntity.getApplying_note_description());
            tvWord.setVisibility(View.VISIBLE);
            etWord.setVisibility(View.VISIBLE);
        } else {
            tvWord.setVisibility(View.GONE);
            etWord.setVisibility(View.GONE);
        }

        if (mCampaignEntity.is_applying_picture_required()) {
            tvPicture.setText(mCampaignEntity.getApplying_picture_description());
            tvPicture.setVisibility(View.VISIBLE);
            ivPost.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams layoutParams = ivPost.getLayoutParams();
            layoutParams.height = mScreenWidth * 4 / 7;
            ivPost.setLayoutParams(layoutParams);
            ivPost.setOnClickListener(this);
        } else {
            tvPicture.setVisibility(View.GONE);
            ivPost.setVisibility(View.GONE);
        }
        mBottomTv.setText(R.string.submit);
        mBottomTv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_bottom:
                submit();
                break;
            case R.id.iv_post:
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
                startActivityForResult(i, SPConstants.IMAGE_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case SPConstants.IMAGE_REQUEST_CODE:
                    mImageUri = data.getData();
                    BitmapUtil.cropImageUri(mImageUri, mScreenWidth, (int) (mScreenWidth * 4.0f / 7), SPConstants.RESULT_CROP_CODE,7, 4,  this);
                    break;
                case SPConstants.RESULT_CROP_CODE:
                    mBitmap = BitmapUtil.decodeUriAsBitmap(mImageUri, this);
                    try {
                        mFinalPicturePath = FileUtils.saveBitmapToSD(mBitmap, "temp" + SystemClock.currentThreadTimeMillis());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ivPost.setImageBitmap(mBitmap);
                    mImageLoadB = true;
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.SIGN_UP_RECRUIT;
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
    }

    public void submit() {

        LinkedHashMap<String, Object> requestMap = new LinkedHashMap<>();
        requestMap.put("id", String.valueOf(mCampaignEntity.getId()));

        if (mCampaignEntity.is_applying_note_required()) {
            String etWordStr = etWord.getText().toString();
            if (!TextUtils.isEmpty(etWordStr)) {
                requestMap.put("note", etWordStr);
            } else {
                CustomToast.showShort(this, R.string.robin401);
                return;
            }
        }

        String imageName = null;
        File file = null;
        if (mCampaignEntity.is_applying_note_required()) {
            if (!TextUtils.isEmpty(mFinalPicturePath) && mImageLoadB) {
                imageName = mFinalPicturePath.substring(mFinalPicturePath.lastIndexOf("/") + 1);
                file = new File(mFinalPicturePath);
            } else {
                CustomToast.showShort(this, R.string.robin382);
                return;
            }
        }


        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();
        HttpRequest.getInstance().put(true, HelpTools.getUrl(CommonConfig.CAMPAIGNS_APPLY_URL), "picture",
                imageName, file, requestMap, new RequestCallback() {
                    @Override
                    public void onError(Exception e) {
                        if (mWProgressDialog != null) {
                            mWProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onResponse(String response) {
                        if (mWProgressDialog != null) {
                            mWProgressDialog.dismiss();
                        }

                        CampaignInviteBean bean = GsonTools.jsonToBean(response, CampaignInviteBean.class);
                        if (bean != null && bean.getError() == 0 && bean.getCampaign_invite() != null && bean.getCampaign_invite().getCampaign() != null) {
                            if (!TextUtils.isEmpty(mFinalPicturePath)) {
                                BitmapUtil.deleteBm(mFinalPicturePath);
                            }
                            CampaignListBean.CampaignInviteEntity entity = bean.getCampaign_invite();
                            NotifyMsgEntity notifyMsgEntity = new NotifyMsgEntity(NotifyManager.TYPE_SHARE_SUCCESS, entity);
                            NotifyManager.getNotifyManager().notifyChange(notifyMsgEntity);
                            finish();
                        } else if (bean != null) {
                            CustomToast.showShort(SignUpRecruitActivity.this, bean.getDetail());
                        }
                    }
                });
    }

}
