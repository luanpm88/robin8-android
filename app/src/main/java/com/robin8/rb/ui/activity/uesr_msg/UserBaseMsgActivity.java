package com.robin8.rb.ui.activity.uesr_msg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.robin8.rb.R;
import com.robin8.rb.ui.activity.MainActivity;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.ui.model.BaseBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.IntentUtils;
import com.robin8.rb.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 新用户注册信息填写／身份选择
 */
public class UserBaseMsgActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvGeneralUser;
    private TextView tvBigVUser;
    private TextView tvContentCreator;
    private TextView tvChooseYear;
    private TextView tvJump;
    private ImageView imgWoman;
    private ImageView imgMan;
    private int tag = 0;
    private int chooseSex = 0;
    private boolean[] dataType = new boolean[]{true, false, false, false, false, false};//显示类型，默认显示： 年月日
    private Map<String, String> map;
    public static final String BASEMSG = "base_msg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uesr_base_msg);
        initView();
        initPicker();
    }

    private void initView() {
        tvGeneralUser = (TextView) findViewById(R.id.tv_general_user);
        tvBigVUser = (TextView) findViewById(R.id.tv_bigV_user);
        tvContentCreator = (TextView) findViewById(R.id.tv_content_creator);
        tvChooseYear = (TextView) findViewById(R.id.tv_choose_year);
        tvJump = (TextView) findViewById(R.id.tv_jump);
        imgWoman = (ImageView) findViewById(R.id.img_woman_true);
        imgMan = (ImageView) findViewById(R.id.img_man_true);
        TextView next = (TextView) findViewById(R.id.tv_next_step);
        next.setOnClickListener(this);
        tvBigVUser.setOnClickListener(this);
        tvGeneralUser.setOnClickListener(this);
        tvContentCreator.setOnClickListener(this);
        tvChooseYear.setOnClickListener(this);
        tvJump.setOnClickListener(this);
        imgMan.setOnClickListener(this);
        imgWoman.setOnClickListener(this);
        map = new HashMap<>();
    }

    private TimePickerView pickerView;

    private void initPicker() {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(1990, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2018, 2, 28);
        pickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date, View v) {
                tvChooseYear.setText(getTime(date));
            }
        }).setType(dataType).setDate(startDate).
                setRangDate(startDate, endDate).
                setTitleText("").
                setSubmitText(getString(R.string.confirm)).
                setCancelText(getString(R.string.cancel)).setLabel("", "", "", "", "", "").setSubmitColor(getResources().getColor(R.color.blue_custom)).setCancelColor(getResources().getColor(R.color.gray_second)).setTitleBgColor(getResources().getColor(R.color.white_custom)).setBgColor(getResources().getColor(R.color.white_custom)).setTitleColor(getResources().getColor(R.color.white_custom)).setSubCalSize(15).build();
    }

    private WProgressDialog mWProgressDialog;

    private void initData() {
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();
        final BasePresenter mBasePresenter = new BasePresenter();
        RequestParams mRequestParams = new RequestParams();
        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.UPDATE_BASE_INFO_URL), mRequestParams, new RequestCallback() {

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
                LogUtil.LogShitou("选择基本信息", "==>" + response);
                // parseJson(response);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_general_user:
                tag = 1;
                tvGeneralUser.setActivated(true);
                tvBigVUser.setActivated(false);
                tvContentCreator.setActivated(false);
                break;
            case R.id.tv_bigV_user:
                tag = 2;
                tvBigVUser.setActivated(true);
                tvGeneralUser.setActivated(false);
                tvContentCreator.setActivated(false);
                Intent intent = new Intent(this, UserSelectActivity.class);
                intent.putExtra("register", BASEMSG);
                intent.putExtra("register_data", resultData);
                startActivityForResult(intent, SPConstants.CLOSE_ONE);
                break;
            case R.id.tv_content_creator:
                tag = 3;
                tvGeneralUser.setActivated(false);
                tvBigVUser.setActivated(false);
                tvContentCreator.setActivated(true);
                Intent intentC = new Intent(this, CreatorMsgActivity.class);
                intentC.putExtra("register", BASEMSG);
                intentC.putExtra("register_data", resultDataTwo);
                startActivityForResult(intentC, SPConstants.CLOSE_TWO);
                break;
            case R.id.tv_choose_year:
                pickerView.show();
                break;
            case R.id.img_man_true:
                chooseSex = 1;
                imgMan.setImageResource(R.mipmap.icon_man_chose);
                imgWoman.setImageResource(R.mipmap.icon_woman_no_chose);
                break;
            case R.id.img_woman_true:
                chooseSex = 2;
                imgMan.setImageResource(R.mipmap.icon_man_no_chose);
                imgWoman.setImageResource(R.mipmap.icon_woman_chose);
                break;
            case R.id.tv_jump:
                //                Intent intent1 = new Intent(UserBaseMsgActivity.this, ChooseFavoriteActivity.class);
                //                intent1.putExtra("base", "jump");
                //                startActivity(intent1);
                //                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                Intent intentClose = new Intent(this, MainActivity.class);
                intentClose.putExtra("register_main", "zhu");
                startActivity(intentClose);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.tv_next_step:
                if (chooseSex == 0) {
                    CustomToast.showShort(this, R.string.robin419);
                    return;
                }
                map.put("gender", String.valueOf(chooseSex));
                //  map.put("age", tvChooseYear.getText().toString().trim());
                map.put("kol_role", kolRole(tag));
                IntentUtils intentUtils = new IntentUtils(UserBaseMsgActivity.this, ChooseFavoriteActivity.class);
                intentUtils.setMapPaths(ChooseFavoriteActivity.BASE_INFO, map);
                startActivity(intentUtils);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

        }
    }
    private void saveData() {
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();
        final BasePresenter mBasePresenter = new BasePresenter();
        RequestParams mRequestParams = new RequestParams();
        mRequestParams.put("gender",String.valueOf(chooseSex));
        // mRequestParams.put("age", map.get("age"));
        mRequestParams.put("kol_role",kolRole(tag));

        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.UPDATE_BASE_INFO_URL), mRequestParams, new RequestCallback() {

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
                LogUtil.LogShitou("选择基本信息", "==>" + response);
                BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);
                if (baseBean != null) {
                    if (baseBean.getError() == 0) {
                        Intent intent = new Intent(UserBaseMsgActivity.this, MainActivity.class);
                        intent.putExtra("register_main", "zhu");
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }
    private boolean one = false;
    private boolean two = false;
    private String resultData;
    private String resultDataTwo;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPConstants.CLOSE_ONE) {
            if (resultCode != RESULT_OK) {
                // 大V用户信息;
                one = false;
            } else {
                String data1 = data.getStringExtra("data");
                if (! TextUtils.isEmpty(data1)) {
                    resultData = data1;
                    one = true;
                    LogUtil.LogShitou("有数据吗？",data1);
                }

            }
        } else if (requestCode == SPConstants.CLOSE_TWO) {
            if (resultCode != RESULT_OK) {
                // 内容创作者
                two = false;
            } else {
                String data1 = data.getStringExtra("data");
                if (! TextUtils.isEmpty(data1)) {
                    two = true;
                    resultDataTwo = data1;
                }

            }
        }
    }

    private String kolRole(int tag) {
        String role = "";
        switch (tag) {
            case 0:
                role = "";
                break;
            case 1:
                role = "public";
                break;
            case 2:
                role = "big_v";
                break;
            case 3:
                role = "creator";
                break;
        }
        return role;
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(date);
    }

    private void ChangeBg(int type, int colorId, TextView tv) {
        if (type == 0) {
            tv.setTextColor(getResources().getColor(colorId));
            tv.setBackground(getDrawable(R.drawable.shape_solid_gray));
        }

    }
}
