package com.robin8.rb.ui.activity.uesr_msg;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.google.common.base.Joiner;
import com.robin8.rb.R;
import com.robin8.rb.ui.activity.uesr_msg.choose_city.CityChooseActivity;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.ui.model.BaseBean;
import com.robin8.rb.ui.model.UserCircleBean;
import com.robin8.rb.ui.model.UserShowBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.IntentUtils;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.ScreenUtil;
import com.robin8.rb.ui.dialog.CustomDialogManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 微博认证
 */
public class WeiBoMsgActivity extends BaseActivity {
    @Bind(R.id.tv_item_first_title)
    TextView tvItemFirstTitle;
    @Bind(R.id.edit_nick_name)
    EditText editNickName;
    @Bind(R.id.layout_item_first)
    LinearLayout layoutItemFirst;
    @Bind(R.id.tv_choose_range)
    TextView tvChooseRange;
    @Bind(R.id.layout_item_second)
    LinearLayout layoutItemSecond;
    @Bind(R.id.tv_choose_platform)
    TextView tvChoosePlatform;
    @Bind(R.id.tv_item_platform)
    TextView tvItemPlatform;
    @Bind(R.id.layout_item_type)
    LinearLayout layoutItemType;
    @Bind(R.id.edit_first_price)
    EditText editFirstPrice;
    @Bind(R.id.edit_second_price)
    EditText editSecondPrice;
    @Bind(R.id.edit_third_price)
    EditText editThirdPrice;
    @Bind(R.id.layout_item_third)
    LinearLayout layoutItemThird;
    @Bind(R.id.tv_price_end_time)
    TextView tvPriceEndTime;
    @Bind(R.id.layout_item_fourth)
    LinearLayout layoutItemFourth;
    @Bind(R.id.edit_first_num)
    EditText editFirstNum;
    @Bind(R.id.tv_second_choose)
    TextView tvSecondChoose;
    @Bind(R.id.tv_third_choose)
    TextView tvThirdChoose;
    @Bind(R.id.layout_item_fifth)
    LinearLayout layoutItemFifth;
    @Bind(R.id.edit_link)
    EditText editLink;
    @Bind(R.id.layout_item_sixth)
    LinearLayout layoutItemSixth;
    @Bind(R.id.edit_remark)
    EditText editRemark;
    @Bind(R.id.layout_item_seventh)
    LinearLayout layoutItemSeventh;

    private int TYPE_ONE = 0;
    private int TYPE_TWO = 0;
    private String txEditNickName;
    private String txChooseCircle;
    private String txEditFirstPrice;
    private String txEditSecondPrice;
    private String txEditThirdPrice;
    private String txPriceEndTime;
    private String txEditLink;
    private String txEditRemark;
    private String txEditFirstNum;
    private String txThirdChoose;

    private List<UserCircleBean.CirclesListBean> circlesList;//数据圈子
    private List<UserShowBean.CirclesBean> initCirclesList;//接口圈子
    private ArrayList<Integer> circleIdList;
    private ArrayList<String> cityNameList;
    private List<String> circleNameList;

    public static final String WEIBOMSG = "WEIBO_MSG";
    private String editFirstBegin;
    private String editSecondBegin;
    private String editThirdBegin;
    private String editFifthBegin;

    @Override
    public void setTitleView() {
        editFirstBegin = getString(R.string.robin499);
        editSecondBegin = getString(R.string.robin500);
        editThirdBegin = getString(R.string.robin501);
        editFifthBegin = getString(R.string.robin483);
        mTVCenter.setText(getResources().getString(R.string.weibo));
        mTvSave.setVisibility(View.VISIBLE);
        mTvSave.setOnClickListener(this);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_wei_bo_msg, mLLContent, true);
        ButterKnife.bind(this);
        layoutItemSecond.setOnClickListener(this);
        tvSecondChoose.setOnClickListener(this);
        tvThirdChoose.setOnClickListener(this);
        layoutItemType.setOnClickListener(this);
        tvPriceEndTime.setOnClickListener(this);
        circleIdList = new ArrayList<>();
        cityNameList = new ArrayList<>();
        circleNameList = new ArrayList<>();

        initData();
        initPicker();
        String initInfo = getIntent().getStringExtra(WEIBOMSG);
        if (! TextUtils.isEmpty(initInfo)) {
            initLastData(initInfo);
        }
        initText();
        MyTextChange myTextChange = new MyTextChange();
        if (! TextUtils.isEmpty(txEditNickName)) {
            editNickName.setSelection(editNickName.getText().toString().trim().length());
        }
        editFirstPrice.setOnFocusChangeListener(new MyFocusListener(R.id.edit_first_price));
        editSecondPrice.setOnFocusChangeListener(new MyFocusListener(R.id.edit_second_price));
        editThirdPrice.setOnFocusChangeListener(new MyFocusListener(R.id.edit_third_price));
        editFirstNum.setOnFocusChangeListener(new MyFocusListener(R.id.edit_first_num));

        editNickName.addTextChangedListener(myTextChange);
        editFirstPrice.addTextChangedListener(myTextChange);
        editSecondPrice.addTextChangedListener(myTextChange);
        editThirdPrice.addTextChangedListener(myTextChange);
        editFirstNum.addTextChangedListener(myTextChange);
        editLink.addTextChangedListener(myTextChange);
        setSave();
    }

    private WProgressDialog mWProgressDialog;

    private void saveData() {
        initText();
        if (checkParams(true) == false) {
            return;
        }
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();
        final BasePresenter mBasePresenter = new BasePresenter();
        RequestParams mRequestParams = new RequestParams();
        mRequestParams.put("nickname", txEditNickName);

        String circleResult = Joiner.on(",").join(circleIdList);
        LogUtil.LogShitou("选择的圈子", circleResult);
        mRequestParams.put("circle_ids", circleResult);

        mRequestParams.put("auth_type", TYPE_ONE);

        mRequestParams.put("fwd_price", editShow(true, txEditFirstPrice, editFirstBegin));
        mRequestParams.put("price", editShow(true, txEditSecondPrice, editSecondBegin));
        mRequestParams.put("live_price", editShow(true, txEditThirdPrice, editThirdBegin));
        mRequestParams.put("fans_count", Integer.valueOf(editShow(true, txEditFirstNum, editFifthBegin)));

        mRequestParams.put("quote_expired_at", txPriceEndTime);
        mRequestParams.put("gender", TYPE_TWO);

        String cityResult = Joiner.on(",").join(listAddText(cityNameList));
        mRequestParams.put("cities", cityResult);

        mRequestParams.put("content_show", txEditLink);
        mRequestParams.put("remark", txEditRemark);

        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.UPDATE_WEIBO_INFO_URL), mRequestParams, new RequestCallback() {

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
                LogUtil.LogShitou("微博更新数据", "==>" + response);
                BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);
                if (baseBean != null) {
                    if (baseBean.getError() == 0) {
                        Intent intent = new Intent(WeiBoMsgActivity.this, UserSelectActivity.class);
                        intent.putExtra("updata", response);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });
    }

    private ArrayList<String> listAddText(ArrayList<String> list) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            arrayList.add(list.get(i) + "市");
        }
        return arrayList;
    }

    /**
     获取资源数据
     */
    private void initData() {
        String data = CacheUtils.getString(WeiBoMsgActivity.this, HelpTools.BASEINFO, null);
        if (TextUtils.isEmpty(data)) {
            if (mWProgressDialog == null) {
                mWProgressDialog = WProgressDialog.createDialog(this);
            }
            mWProgressDialog.show();
            final BasePresenter mBasePresenter = new BasePresenter();
            mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.UPDATE_BASE_INFOS_URL), null, new RequestCallback() {

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
                    LogUtil.LogShitou("数据", "==>" + response);
                    UserCircleBean bean = GsonTools.jsonToBean(response, UserCircleBean.class);
                    if (bean != null) {
                        if (bean.getError() == 0) {
                            CacheUtils.putString(WeiBoMsgActivity.this, HelpTools.BASEINFO, response);
                            circlesList = bean.getCircles_list();
                        }
                    }
                }
            });
        } else {
            UserCircleBean bean = GsonTools.jsonToBean(data, UserCircleBean.class);
            if (bean != null) {
                if (bean.getError() == 0) {
                    circlesList = bean.getCircles_list();
                }
            }
        }
    }

    private Boolean checkParams(boolean show) {
        if (TextUtils.isEmpty(txEditNickName)) {
            if (show) {
                CustomToast.showShort(this, R.string.please_write_nick_name);
            }
            return false;
        }
        if (TextUtils.isEmpty(txChooseCircle) || txChooseCircle.equals(getString(R.string.robin218))) {
            if (show) {
                CustomToast.showShort(this, R.string.robin218);
            }
            return false;
        }
        if (TYPE_ONE == 0) {
            if (show) {
                CustomToast.showShort(this, R.string.robin226);
            }

            return false;
        }
        if (TextUtils.isEmpty(txEditFirstPrice)) {
            if (show) {
                CustomToast.showShort(this, R.string.robin227);
            }

            return false;
        }
        if (TextUtils.isEmpty(txEditSecondPrice)) {
            if (show) {
                CustomToast.showShort(this, R.string.robin228);
            }

            return false;
        }
        if (TextUtils.isEmpty(txEditThirdPrice)) {
            if (show) {
                CustomToast.showShort(this, R.string.robin229);
            }

            return false;
        }

        if (TextUtils.isEmpty(txPriceEndTime) || txPriceEndTime.equals(getString(R.string.robin224))) {
            if (show) {
                CustomToast.showShort(this, R.string.robin224);
            }

            return false;
        }

        if (TextUtils.isEmpty(txEditFirstNum)) {
            if (show) {
                CustomToast.showShort(this, R.string.robin081);
            }

            return false;
        }

        if (TYPE_TWO == 0) {
            if (show) {
                CustomToast.showShort(this, R.string.robin082);
            }

            return false;
        }

        if (TextUtils.isEmpty(txThirdChoose) || txThirdChoose.equals(getString(R.string.robin084))) {
            if (show) {
                CustomToast.showShort(this, R.string.robin084);
            }

            return false;
        }

        if (TextUtils.isEmpty(txEditLink)) {
            if (show) {
                CustomToast.showShort(this, R.string.robin086);
            }
            return false;
        }
        return true;
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }

    private void showInviteDialog(final int type) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_user_info_item, null);
        final CustomDialogManager manager = new CustomDialogManager(this, view);
        TextView tvFirst = (TextView) view.findViewById(R.id.tv_first);
        TextView tvSecond = (TextView) view.findViewById(R.id.tv_second);
        TextView tvThird = (TextView) view.findViewById(R.id.tv_third);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        if (type == 1) {
//            if (weibo_auth_list!=null){
//                if (weibo_auth_list.size()!=0){
//                    tvFirst.setText(weibo_auth_list.get(0));
//                }
//            }
            tvFirst.setText(R.string.robin352);
            tvSecond.setText(R.string.robin353);
            tvThird.setText(R.string.robin354);
        } else {
            tvThird.setVisibility(View.GONE);
            tvFirst.setText(R.string.male);
            tvSecond.setText(R.string.female);
        }
        tvFirst.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                manager.dismiss();
                if (type == 1) {
                    TYPE_ONE = 1;
                    tvItemPlatform.setText(R.string.robin352);
                } else {
                    TYPE_TWO = 1;
                    tvSecondChoose.setText(R.string.robin355);
                }
            }
        });
        tvSecond.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                manager.dismiss();
                if (type == 1) {
                    TYPE_ONE = 2;
                    tvItemPlatform.setText(R.string.robin353);
                } else {
                    TYPE_TWO = 2;
                    tvSecondChoose.setText(R.string.robin356);
                }
            }
        });
        tvThird.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                manager.dismiss();
                if (type == 1) {
                    TYPE_ONE = 3;
                    tvItemPlatform.setText(R.string.robin354);
                }
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

    private void initText() {
        txEditNickName = editNickName.getText().toString().trim();
        txChooseCircle = tvChooseRange.getText().toString().trim();//选择圈子
        txEditFirstPrice = editFirstPrice.getText().toString().trim();
        txEditSecondPrice = editSecondPrice.getText().toString().trim();
        txEditThirdPrice = editThirdPrice.getText().toString().trim();
        txEditFirstNum = editFirstNum.getText().toString().trim();
        txPriceEndTime = tvPriceEndTime.getText().toString().trim();//报价截止时间
        txEditLink = editLink.getText().toString().trim();
        txEditRemark = editRemark.getText().toString().trim();//备注
        txThirdChoose = tvThirdChoose.getText().toString().trim();//城市选择
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        setSave();
        switch (v.getId()) {
            case R.id.tv_save:
                saveData();
                break;
            case R.id.layout_item_second:
                //选择圈子
                IntentUtils intent = new IntentUtils(this, ChooseFavoriteActivity.class);
                if (circleIdList != null && circleIdList.size() != 0) {
                    intent.putIntegerArrayListExtra(ChooseFavoriteActivity.CHOOSE_LIST, circleIdList);
                }
                intent.putExtra(ChooseFavoriteActivity.CHOOSE_CIRCLE, WEIBOMSG);
                startActivityForResult(intent, SPConstants.CIRCLE_CHOOSE);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.tv_price_end_time:
                //报价有效期
                ScreenUtil.hideSoftKeyboard(WeiBoMsgActivity.this);
                pickerView.show();
                break;
            case R.id.tv_second_choose:
                showInviteDialog(2);
                break;
            case R.id.layout_item_type:
                showInviteDialog(1);
                break;
            case R.id.tv_third_choose:
                //选择城市
                Intent intentCity = new Intent(this, CityChooseActivity.class);
                if (cityNameList.size() != 0) {
                    intentCity.putStringArrayListExtra(CityChooseActivity.CITY_CHOOSE, cityName(cityNameList));
                } else {
                    intentCity.putStringArrayListExtra(CityChooseActivity.CITY_CHOOSE, null);
                }
                startActivityForResult(intentCity, SPConstants.CITY_CHOOSE);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPConstants.CIRCLE_CHOOSE) {
            //圈子选择
            if (resultCode == RESULT_OK) {
                ArrayList<Integer> listExtra = data.getIntegerArrayListExtra(ChooseFavoriteActivity.CHOOSE_CIRCLE);
                if (listExtra.size() != 0) {
                    if (circleIdList.size() != 0) {
                        circleIdList.clear();
                    }
                    if (circleNameList.size() != 0) {
                        circleNameList.clear();
                    }
                    for (int i = 0; i < listExtra.size(); i++) {
                        circleIdList.add(listExtra.get(i));
                        LogUtil.LogShitou("返回的圈子id", "===>" + circleIdList.get(i));
                    }
                    for (int i = 0; i < circlesList.size(); i++) {
                        for (int j = 0; j < listExtra.size(); j++) {
                            if (circlesList.get(i).getId() == listExtra.get(j)) {
                                circleNameList.add(circlesList.get(i).getLabel());
                            }
                        }
                    }
                    tvChooseRange.setText(Joiner.on(" ,").join(circleNameList));
                } else {
                    tvChooseRange.setText(R.string.robin218);
                    setSave();
                }

            }

        } else if (requestCode == SPConstants.CITY_CHOOSE) {
            //城市选择
            if (resultCode == RESULT_OK) {
                ArrayList<String> listExtra = data.getStringArrayListExtra(CityChooseActivity.CITY_CHOOSE);
                if (listExtra.size() != 0) {
                    if (cityNameList != null || cityNameList.size() != 0) {
                        cityNameList.clear();
                    }
                    for (int i = 0; i < listExtra.size(); i++) {
                        cityNameList.add(listExtra.get(i));
                    }
                    tvThirdChoose.setText(getString(R.string.robin269,(Joiner.on(" ,").join(cityName(cityNameList)))));
                }
            }
        }
    }

    private ArrayList<String> cityName(ArrayList<String> list) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            arrayList.add(list.get(i).replace("市", "").trim());
        }
        return arrayList;
    }

    private void initLastData(String extra) {
        UserShowBean userShowBean = GsonTools.jsonToBean(extra, UserShowBean.class);
        if (userShowBean != null) {
            if (userShowBean.getError() == 0) {
                UserShowBean.KolBean.WeiboAccountBean wechatAccountBean = userShowBean.getKol().getWeibo_account();
                if (wechatAccountBean != null) {
                    editNickName.setText(wechatAccountBean.getNickname());
                    initCirclesList = wechatAccountBean.getCircles();
                    //取出圈子名称进行拼接
                    if (initCirclesList.size() != 0) {
                        for (int i = 0; i < initCirclesList.size(); i++) {
                            circleIdList.add(initCirclesList.get(i).getId());
                            circleNameList.add(initCirclesList.get(i).getLabel());
                        }
                        tvChooseRange.setText(Joiner.on(" , ").join(circleNameList));
                    }

                    editFirstPrice.setText(editShow(false, String.valueOf(wechatAccountBean.getFwd_price()), editFirstBegin));
                    editSecondPrice.setText(editShow(false, String.valueOf(wechatAccountBean.getPrice()), editSecondBegin));
                    editThirdPrice.setText(editShow(false, String.valueOf(wechatAccountBean.getLive_price()), editThirdBegin));
                    TYPE_ONE = wechatAccountBean.getAuth_type();
                    if (TYPE_ONE == 1) {
                        tvItemPlatform.setText(R.string.robin352);
                    } else if (TYPE_ONE == 2) {
                        tvItemPlatform.setText(R.string.robin353);
                    } else if (TYPE_ONE == 3) {
                        tvItemPlatform.setText(R.string.robin354);
                    }
                    String time = wechatAccountBean.getQuote_expired_at();
                    if (!TextUtils.isEmpty(time)){
                        tvPriceEndTime.setText(time.substring(0,time.indexOf("T")));
                    }
                    editFirstNum.setText(editShow(false, String.valueOf(wechatAccountBean.getFans_count()), editFifthBegin));
                    TYPE_TWO = wechatAccountBean.getGender();
                    if (TYPE_TWO == 1) {
                        tvSecondChoose.setText(R.string.robin355);
                    } else {
                        tvSecondChoose.setText(R.string.robin356);
                    }
                    //集中城市
                    List<String> cities = wechatAccountBean.getCities();
                    if (cities != null && cities.size() != 0) {
                        for (int i = 0; i < cities.size(); i++) {
                            cityNameList.add(cities.get(i));
                        }
                        tvThirdChoose.setText(getString(R.string.robin269,Joiner.on(" ,").join(cityName(cityNameList))));
                    }
                    editLink.setText(wechatAccountBean.getContent_show());
                    if (! TextUtils.isEmpty(wechatAccountBean.getRemark())) {
                        editRemark.setText(wechatAccountBean.getRemark());
                    }
                }
            }
        }
    }

    class MyFocusListener implements View.OnFocusChangeListener {
        private int viewId;

        public MyFocusListener(int id) {
            this.viewId = id;
        }

        @Override
        public void onFocusChange(View view, boolean b) {
            initText();
            switch (viewId) {
                case R.id.edit_first_price:
                    editShowText(b, editFirstPrice, txEditFirstPrice, editFirstBegin);
                    break;
                case R.id.edit_second_price:
                    editShowText(b, editSecondPrice, txEditSecondPrice, editSecondBegin);
                    break;
                case R.id.edit_third_price:
                    editShowText(b, editThirdPrice, txEditThirdPrice, editThirdBegin);
                    break;
                case R.id.edit_first_num:
                    editShowText(b, editFirstNum, txEditFirstNum, editFifthBegin);
                    break;
            }
        }
    }

    private void editShowText(boolean is, EditText editText, String tx, String repaceStr) {
        if (is == false) {
            if (! TextUtils.isEmpty(tx)) {
                if (tx.startsWith(repaceStr)) {
                    editText.setText(tx);
                } else {
                    editText.setText(repaceStr + editText.getText().toString().trim());
                }
            }
        } else {
            if (! TextUtils.isEmpty(tx)) {
                if (tx.startsWith(repaceStr)) {
                    String replace = tx.replace(repaceStr, "");
                    editText.setText(replace);
                }
            }
        }
    }

    /**
     @param is true=提交；false = 赋值
     @param tx
     @param repaceStr editview要添加的开头文字
     @return
     */
    private String editShow(boolean is, String tx, String repaceStr) {
        if (is == true) {
            if (tx.startsWith(repaceStr)) {
                String replace = tx.replace(repaceStr, "");
                tx = replace;
            }
            return tx;
        } else {
            if (! tx.startsWith(repaceStr)) {
                tx = repaceStr + tx;
            }
            return tx;
        }
    }

    class MyTextChange implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            setSave();
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    private void setSave() {
        initText();
        if (checkParams(false) == true) {
            mTvSave.setTextColor(getResources().getColor(R.color.black_000000));
            mTvSave.setClickable(true);
        } else {
            mTvSave.setTextColor(getResources().getColor(R.color.gray_second));
        }
    }

    private TimePickerView pickerView;
    private boolean[] dataType = new boolean[]{true, true, true, false, false, false};//显示类型，默认显示： 年月日

    private void initPicker() {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2018, 1, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2050, 12, 31);
        pickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date, View v) {
                tvPriceEndTime.setText(getTime(date));
            }
        }).setType(dataType).setDate(selectedDate).setRangDate(startDate, endDate).setTitleText("").setSubmitText(getString(R.string.confirm)).setCancelText(getString(R.string.cancel)).setLabel("", "", "", "", "", "").setSubmitColor(getResources().getColor(R.color.blue_custom)).setCancelColor(getResources().getColor(R.color.gray_second)).setTitleBgColor(getResources().getColor(R.color.white_custom)).setBgColor(getResources().getColor(R.color.white_custom)).setTitleColor(getResources().getColor(R.color.white_custom)).setSubCalSize(15).build();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

}
