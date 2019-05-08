package com.robin8.rb.ui.activity.uesr_msg;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.ui.model.BaseBean;
import com.robin8.rb.ui.model.UserShowBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.FileUtils;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.ui.widget.CircleImageView;
import com.robin8.rb.ui.dialog.CustomDialogManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 用户个人信息详情
 */
public class UserInfoShowActivity extends BaseActivity {
    @Bind(R.id.civ_image)
    CircleImageView civImage;
    @Bind(R.id.layout_photo)
    LinearLayout layoutPhoto;
    @Bind(R.id.edit_nick_name)
    EditText editNickName;
    @Bind(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @Bind(R.id.tv_email_num)
    TextView tvEmailNum;
    @Bind(R.id.tv_gender)
    TextView tvGender;
    @Bind(R.id.tv_birth)
    TextView tvBirth;
    @Bind(R.id.edit_job)
    EditText editJob;
    private WProgressDialog mWProgressDialog;
    private String url;
    private int TYPE_TWO = 0;
    private Bitmap mBitmap;
    private String avatar_url;

    @Override
    public void setTitleView() {
        mTVCenter.setText(R.string.robin315);
        mTvSave.setVisibility(View.VISIBLE);
        mTvSave.setOnClickListener(this);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_user_info_show, mLLContent, true);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String base = intent.getStringExtra("base");
        initPicker();
        if(!TextUtils.isEmpty(base)){
            initData(base);
        }
        layoutPhoto.setOnClickListener(this);
        tvGender.setOnClickListener(this);
        tvBirth.setOnClickListener(this);
        if (!TextUtils.isEmpty(editNickName.getText().toString())){
            editNickName.setSelection(editNickName.getText().toString().length());
        }
        setSave();
    }

    private void initData(String extra) {
        UserShowBean userShowBean = GsonTools.jsonToBean(extra, UserShowBean.class);
        if (userShowBean.getError() == 0) {
            UserShowBean.KolBean kol = userShowBean.getKol();
            if (kol != null) {
                avatar_url = kol.getAvatar_url();
                if (! TextUtils.isEmpty(avatar_url)) {
                    BitmapUtil.loadImage(this, avatar_url, civImage);
                }
                if (! TextUtils.isEmpty(kol.getName())) {
                    editNickName.setText(kol.getName());
                }
                if (! TextUtils.isEmpty(kol.getMobile_number())) {
                    tvPhoneNum.setText(kol.getMobile_number());
                }
                if (! TextUtils.isEmpty(kol.getEmail())) {
                    tvEmailNum.setText(String.valueOf(kol.getEmail()));
                } else {
                    tvEmailNum.setText("");
                }
                String time = kol.getBirthday();
                if (! TextUtils.isEmpty(time)) {
                    if (time.contains("T")) {
                        tvBirth.setText(time.substring(0, time.indexOf("T")));
                    } else {
                        tvBirth.setText(time);
                    }
                    tvBirth.setTextColor(getResources().getColor(R.color.black_000000));
                }
                if (kol.getGender() == 1) {
                    tvGender.setText(R.string.male);
                    TYPE_TWO = 1;
                    tvGender.setTextColor(getResources().getColor(R.color.black_000000));

                } else {
                    tvGender.setText(R.string.female);
                    TYPE_TWO = 2;
                    tvGender.setTextColor(getResources().getColor(R.color.black_000000));

                }
                if (! TextUtils.isEmpty(kol.getJob_info())) {
                    editJob.setText(kol.getJob_info());
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        setSave();
        switch (v.getId()) {
            case R.id.tv_save:
                saveData();
                break;
            case R.id.layout_photo:
                //  头像
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SPConstants.IMAGE_REQUEST_CODE);
                break;
            case R.id.tv_gender:
                //性别
                showInviteDialog();
                break;
            case R.id.tv_birth:
                //生日
                try {
                    pickerView.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_back:
                finish();
                break;

        }
    }

    private Uri mImageUri;
    private String mFinalPicturePath;

    private void saveData() {
        if (checkInfoCompelete(true) == false) {
            return;
        }

        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();

        String imageName = null;
        File file = null;
        if (! TextUtils.isEmpty(mFinalPicturePath)) {
            imageName = mFinalPicturePath.substring(mFinalPicturePath.lastIndexOf("/") + 1);
            file = new File(mFinalPicturePath);
        }
        LinkedHashMap<String, Object> mRequestParams = new LinkedHashMap<>();
        mRequestParams.put("name",editNickName.getText().toString().trim());
        mRequestParams.put("birthday",tvBirth.getText().toString());
        mRequestParams.put("gender",String.valueOf(TYPE_TWO));
        mRequestParams.put("job_info",editJob.getText().toString());

        HttpRequest.getInstance().post(true, HelpTools.getUrl(CommonConfig.UPDATE_BASE_INFO_URL), "avatar", imageName, file, mRequestParams, new RequestCallback() {

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
                if (! TextUtils.isEmpty(mFinalPicturePath)) {
                    BitmapUtil.deleteBm(mFinalPicturePath);
                }
                LogUtil.LogShitou("提交基本信息", "==>" + response);
                BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);
                if (baseBean.getError() == 0) {
                    Intent intent = new Intent(UserInfoShowActivity.this, UserInformationActivity.class);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case SPConstants.IMAGE_REQUEST_CODE:
                    mImageUri = data.getData();
                    //剪裁
                    BitmapUtil.cropImageUri(mImageUri, 500, 500, SPConstants.RESULT_CROP_CODE, 1, 1, this);
                    break;
                case SPConstants.RESULT_CROP_CODE:
                    mBitmap = BitmapUtil.decodeUriAsBitmap(mImageUri, this);
                    try {
                        mFinalPicturePath = FileUtils.saveBitmapToSD(mBitmap, "temp" + SystemClock.currentThreadTimeMillis());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    civImage.setImageBitmap(mBitmap);
                    break;
            }
        }

    }

    private TimePickerView pickerView;
    private boolean[] dataType = new boolean[]{true, true, true, false, false, false};//显示类型，默认显示： 年月日

    private void initPicker() {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(1950, 1, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2030, 12, 31);
        pickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date, View v) {
                tvBirth.setText(getTime(date));
                tvBirth.setTextColor(getResources().getColor(R.color.black_000000));
            }
        }).setType(dataType).setDate(selectedDate).setRangDate(startDate, endDate).setTitleText("").setSubmitText(getString(R.string.pickerview_submit)).setCancelText(getString(R.string.pickerview_cancel)).setLabel(getString(R.string.pickerview_year), getString(R.string.pickerview_month), getString(R.string.pickerview_day), "", "", "").setSubmitColor(getResources().getColor(R.color.blue_custom)).setCancelColor(getResources().getColor(R.color.gray_second)).setTitleBgColor(getResources().getColor(R.color.white_custom)).setBgColor(getResources().getColor(R.color.white_custom)).setTitleColor(getResources().getColor(R.color.white_custom)).setSubCalSize(15).build();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private void showInviteDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_user_info_item, null);
        final CustomDialogManager manager = new CustomDialogManager(this, view);
        TextView tvFirst = (TextView) view.findViewById(R.id.tv_first);
        final TextView tvSecond = (TextView) view.findViewById(R.id.tv_second);
        TextView tvThird = (TextView) view.findViewById(R.id.tv_third);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvThird.setVisibility(View.GONE);
        tvFirst.setText(R.string.male);
        tvSecond.setText(R.string.female);
        tvFirst.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                manager.dismiss();
                TYPE_TWO = 1;
                tvGender.setText(R.string.male);
                tvGender.setTextColor(getResources().getColor(R.color.black_000000));
            }
        });
        tvSecond.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                manager.dismiss();
                TYPE_TWO = 2;
                tvGender.setText(R.string.female);
                tvGender.setTextColor(getResources().getColor(R.color.black_000000));
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                manager.dismiss();
            }
        });
        manager.dg.setCanceledOnTouchOutside(true);
        manager.dg.getWindow().setGravity(Gravity.BOTTOM);
        manager.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        manager.showDialog();
    }

    private boolean checkInfoCompelete(boolean show) {

        if (TextUtils.isEmpty(mFinalPicturePath)&&TextUtils.isEmpty(avatar_url)) {
                if (show) {
                    CustomToast.showShort(this, getString(R.string.please_write_pic));
                }
            return false;
        }

        if (TextUtils.isEmpty(editNickName.getText().toString().trim())) {
            if (show) {
                CustomToast.showShort(this, getString(R.string.please_write_nick_name));
            }
            return false;
        }

        if (TYPE_TWO == 0) {
            if (show) {
                CustomToast.showShort(this, getString(R.string.please_write_sex));
            }
            return false;
        }

        if (TextUtils.isEmpty(tvBirth.getText().toString()) || tvBirth.getText().toString().equals(getString(R.string.please_write_age).toString())) {
            if (show) {
                CustomToast.showShort(this, getString(R.string.please_write_age));
            }
            return false;

        }
        if (TextUtils.isEmpty(editJob.getText().toString())) {
            if (show) {
                CustomToast.showShort(this, R.string.robin017);
            }
            return false;

        }

        return true;
    }

    private void setSave() {
        if (checkInfoCompelete(false)) {
            mTvSave.setTextColor(getResources().getColor(R.color.black_000000));
            mTvSave.setEnabled(true);
        } else {
            mTvSave.setTextColor(getResources().getColor(R.color.gray_first));
        }
    }

    @Override
    protected void onDestroy() {
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
        NotifyManager.getNotifyManager().deleteObserver(this);
        super.onDestroy();
    }

    @Override
    protected void executeOnclickLeftView() {

    }

    @Override
    protected void executeOnclickRightView() {

    }

}
