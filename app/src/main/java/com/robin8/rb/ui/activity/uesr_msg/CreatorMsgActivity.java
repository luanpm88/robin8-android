package com.robin8.rb.ui.activity.uesr_msg;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
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
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.ui.dialog.CustomDialogManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 内容创作者信息 */
public class CreatorMsgActivity extends BaseActivity {
    @Bind(R.id.tv_choose_range)
    TextView tvChooseRange;
    @Bind(R.id.layout_item_first)
    LinearLayout layoutItemFirst;
    @Bind(R.id.tv_item_platform)
    TextView tvItemPlatform;
    @Bind(R.id.layout_item_creator)
    LinearLayout layoutItemCreator;
    @Bind(R.id.edit_first_price)
    EditText editFirstPrice;
    @Bind(R.id.edit_second_price)
    EditText editSecondPrice;
    @Bind(R.id.edit_first_num)
    EditText editFirstNum;
    @Bind(R.id.tv_second_choose)
    TextView tvSecondChoose;
    @Bind(R.id.tv_year_choose)
    TextView tvYearChoose;
    @Bind(R.id.tv_third_choose)
    TextView tvThirdChoose;
    @Bind(R.id.edit_link)
    EditText editLink;

    @Bind(R.id.edit_remark)
    EditText editRemark;

    private WProgressDialog mWProgressDialog;

    private int TYPE_TWO = 0;
    private int TYPE_ONE = 0;
    private String txChooseCircle;
    private String txPlatform;
    private String txChooseYear;
    private String txEditFirstPrice;
    private String txEditSecondPrice;
    private String txEditLink;
    private String txEdotRemark;
    private String txEditFirstNum;
    private String txThirdChoose;
    public static final String CREATOR = "CreatorMsgActivity";
    private List<String> circleNameList;
    private ArrayList<Integer> circleIdList;
    private ArrayList<Integer> platIdList;
    private List<String> platNameList;
    private String[] MyAgeListStr = new String[]{"0-17", "18-24", "25-29", "30-34", "35-39", "40-49", "50-59", "60-"};
    private ArrayList<String> cityNameList;

    private List<String> agesList;
    private List<String> authList;//接口的认证类型
    private List<UserCircleBean.CirclesListBean> circlesList;//接口的圈子
    private List<UserCircleBean.TerracesListBean> platList;
    private List<UserShowBean.CirclesBean> initCirclesList;
    private List<UserShowBean.KolBean.CreatorBean.TerracesBean> initTerracesList;

    private String editFirstBegin;
    private String editSecondBegin;
    private String editThirdBegin;
    private boolean isClose = false;
    private boolean isUpdata = false;

    @Override
    public void setTitleView() {
        editFirstBegin = getString(R.string.robin481);
        editSecondBegin = getString(R.string.robin482);
        editThirdBegin = getString(R.string.robin483);
        mTVCenter.setText(getResources().getString(R.string.tv_content_creator));
        mTvSave.setVisibility(View.VISIBLE);
        mTvSave.setOnClickListener(this);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_creator_msg, mLLContent, true);
        ButterKnife.bind(this);
        layoutItemCreator.setOnClickListener(this);
        layoutItemFirst.setOnClickListener(this);
        tvSecondChoose.setOnClickListener(this);
        tvYearChoose.setOnClickListener(this);
        tvThirdChoose.setOnClickListener(this);
        circleIdList = new ArrayList<>();
        circleNameList = new ArrayList<>();
        platIdList = new ArrayList<>();
        cityNameList = new ArrayList<>();
        platNameList = new ArrayList<>();
        agesList=new ArrayList<>();

        initData();
        Intent intent = getIntent();
        String extra = intent.getStringExtra(CREATOR);
        if (! TextUtils.isEmpty(extra)) {
            isUpdata = true;
            initLastData(extra);
        }
        String base = intent.getStringExtra("register");
        String baseData = intent.getStringExtra("register_data");
        if (! TextUtils.isEmpty(base)) {
            isClose = true;
            if (! TextUtils.isEmpty(baseData)) {
                initLastData(baseData);
            }
        }
        initText();
        layoutItemFirst.setFocusable(true);
        MyTextChange myTextChange = new MyTextChange();
        if (! TextUtils.isEmpty(txEditFirstPrice)) {
            editFirstPrice.setSelection(editFirstPrice.getText().toString().trim().length());
        }
        editFirstPrice.setOnFocusChangeListener(new MyFocusListener(R.id.edit_first_price));
        editSecondPrice.setOnFocusChangeListener(new MyFocusListener(R.id.edit_second_price));
        editFirstNum.setOnFocusChangeListener(new MyFocusListener(R.id.edit_first_num));

        editFirstPrice.addTextChangedListener(myTextChange);
        editSecondPrice.addTextChangedListener(myTextChange);
        editFirstNum.addTextChangedListener(myTextChange);
        editLink.addTextChangedListener(myTextChange);
        setSave();
    }

    private OptionsPickerView pickerView;

    private void initPicker() {
        pickerView = new OptionsPickerBuilder(CreatorMsgActivity.this, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                TYPE_ONE = options1;
                tvYearChoose.setText(getString(R.string.robin268,agesList.get(options1)));
            }
        }).setContentTextSize(16)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(1, 0)//默认选中项
                .setTitleText("").setSubmitText(getString(R.string.confirm)).setCancelText(getString(R.string.cancel)).isRestoreItem(false)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("", "", "").setBackgroundId(0x00000000) //设置外部遮罩颜色
                .setSubmitColor(getResources().getColor(R.color.blue_custom)).setCancelColor(getResources().getColor(R.color.gray_second)).setTitleBgColor(getResources().getColor(R.color.white_custom)).setBgColor(getResources().getColor(R.color.white_custom)).setTitleColor(getResources().getColor(R.color.white_custom)).build();
        pickerView.setPicker(agesList);
    }

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

        String circleResult = Joiner.on(",").join(circleIdList);
        LogUtil.LogShitou("选择的圈子", circleResult);
        mRequestParams.put("circle_ids", circleResult);

        String platResult = Joiner.on(",").join(platIdList);
        mRequestParams.put("terrace_ids", platResult);


        mRequestParams.put("price", editShow(true, txEditFirstPrice, editFirstBegin));
        mRequestParams.put("video_price", editShow(true, txEditSecondPrice, editSecondBegin));
        mRequestParams.put("fans_count", Integer.valueOf(editShow(true, txEditFirstNum, editThirdBegin)));

        String cityResult = Joiner.on(",").join(listAddText(cityNameList));
        mRequestParams.put("cities", cityResult);

        mRequestParams.put("gender", TYPE_TWO);

        mRequestParams.put("age_range", TYPE_ONE);
        mRequestParams.put("content_show", txEditLink);
        mRequestParams.put("remark", txEdotRemark);
        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.UPDATE_CREATOR_INFO_URL), mRequestParams, new RequestCallback() {

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
                LogUtil.LogShitou("更新内容创作者", "==>" + response);
                BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);
                if (baseBean != null) {
                    if (baseBean.getError() == 0) {
                        if (isClose = true) {
                            Intent intent = new Intent(CreatorMsgActivity.this, UserBaseMsgActivity.class);
                            intent.putExtra("data", response);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                        if (isUpdata = true) {
                            Intent intent = new Intent(CreatorMsgActivity.this, UserInformationActivity.class);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    } else {
                        CustomToast.showShort(CreatorMsgActivity.this, baseBean.getMessage());
                    }
                }
                // parseJson(response);
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        setSave();
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:
                saveData();
                break;
            case R.id.layout_item_first:
                IntentUtils intent = new IntentUtils(this, ChooseFavoriteActivity.class);
                if (circleIdList != null && circleIdList.size() != 0) {
                    intent.putIntegerArrayListExtra(ChooseFavoriteActivity.CHOOSE_LIST, circleIdList);
                }
                intent.putExtra(ChooseFavoriteActivity.CHOOSE_CIRCLE, CREATOR);
                startActivityForResult(intent, SPConstants.CIRCLE_CHOOSE);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.layout_item_creator:
                IntentUtils intentUtils = new IntentUtils(this, PlatChooseActivity.class);
                if (platIdList != null && platIdList.size() != 0) {
                    intentUtils.putIntegerArrayListExtra(PlatChooseActivity.PLAT_CHOOSE, platIdList);
                }
                startActivityForResult(intentUtils, SPConstants.PLAT_CHOOSE);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.tv_second_choose:
                showInviteDialog();
                break;
            case R.id.tv_year_choose:
                //首先收起键盘
                ScreenUtil.hideSoftKeyboard(CreatorMsgActivity.this);
                //   layoutChooseYear.setFocusable(true);
                // layoutChooseYear.setFocusableInTouchMode(true);
                pickerView.show();
                break;
            case R.id.tv_third_choose:
                //选择城市
                IntentUtils intentCity = new IntentUtils(this, CityChooseActivity.class);
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

    private ArrayList<String> cityName(ArrayList<String> list) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < cityNameList.size(); i++) {
            arrayList.add(cityNameList.get(i).replace("市", "").trim());
        }
        return arrayList;
    }

    /**
     回调数据
     @param requestCode
     @param resultCode
     @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPConstants.PLAT_CHOOSE) {
            //平台选择
            if (resultCode == RESULT_OK) {
                ArrayList<Integer> listExtra = data.getIntegerArrayListExtra(PlatChooseActivity.PLAT_CHOOSE);
                if (listExtra.size() != 0) {
                    if (platIdList != null || platIdList.size() != 0) {
                        platIdList.clear();
                    }
                    if (platNameList != null || platNameList.size() != 0) {
                        platNameList.clear();
                    }
                    for (int i = 0; i < listExtra.size(); i++) {
                        platIdList.add(listExtra.get(i));
                    }
                    for (int i = 0; i < platList.size(); i++) {
                        for (int j = 0; j < listExtra.size(); j++) {
                            if (platList.get(i).getId() == listExtra.get(j)) {
                                platNameList.add(platList.get(i).getName());
                            }
                        }
                    }
                    tvItemPlatform.setText(Joiner.on(" ,").join(platNameList));
                }
                setSave();
            }
        } else if (requestCode == SPConstants.CIRCLE_CHOOSE) {
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
                    if (circleIdList.size() != 0) {
                        circleIdList.clear();
                    }
                    if (circleNameList.size() != 0) {
                        circleNameList.clear();
                    }
                    tvChooseRange.setText(R.string.robin074);
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
                    tvThirdChoose.setText(getString(R.string.robin269,(Joiner.on(" ,").join(cityName(StringUtil.removeDuplicate(cityNameList))))));
                }
            }
            setSave();
        }
    }


    private void initText() {
        txChooseCircle = tvChooseRange.getText().toString().trim();//选择圈子
        txPlatform = tvItemPlatform.getText().toString().trim();//选择平台
        txEditFirstPrice = editFirstPrice.getText().toString().trim();
        txEditSecondPrice = editSecondPrice.getText().toString().trim();
        txEditFirstNum = editFirstNum.getText().toString().trim();

        txChooseYear = tvYearChoose.getText().toString().trim();//选择年龄区间
        txThirdChoose = tvThirdChoose.getText().toString().trim();//城市选择

        txEditLink = editLink.getText().toString().trim();
        txEdotRemark = editRemark.getText().toString().trim();//备注
    }

    private void initLastData(String extra) {
        initData();
        UserShowBean userShowBean = GsonTools.jsonToBean(extra, UserShowBean.class);
        if (userShowBean != null) {
            if (userShowBean.getError() == 0) {
                UserShowBean.KolBean.CreatorBean creator = userShowBean.getKol().getCreator();
                if (creator != null) {
                    initCirclesList = creator.getCircles();
                    //取出圈子id／名称
                    if (initCirclesList.size() != 0) {
                        for (int i = 0; i < initCirclesList.size(); i++) {
                            circleIdList.add(initCirclesList.get(i).getId());
                            circleNameList.add(initCirclesList.get(i).getLabel());
                        }
                        tvChooseRange.setText(Joiner.on(" , ").join(circleNameList));
                    }
                    initTerracesList = creator.getTerraces();
                    //取出平台名字／Id
                    if (initTerracesList.size() != 0) {
                        for (int i = 0; i < initTerracesList.size(); i++) {
                            platIdList.add(initTerracesList.get(i).getId());
                            platNameList.add(initTerracesList.get(i).getName());
                        }
                        tvItemPlatform.setText(Joiner.on(" ,").join(platNameList));
                    }
                    //报价
                    editFirstPrice.setText(editShow(false, String.valueOf(creator.getPrice()), editFirstBegin));
                    editSecondPrice.setText(editShow(false, String.valueOf(creator.getVideo_price()), editSecondBegin));
                    editFirstNum.setText(editShow(false, String.valueOf(creator.getFans_count()), editThirdBegin));

                    TYPE_TWO = creator.getGender();
                    TYPE_ONE = creator.getAge_range();
                    if (TYPE_TWO == 1) {
                        tvSecondChoose.setText(R.string.robin355);
                    } else {
                        tvSecondChoose.setText(R.string.robin356);
                    }
                    if (agesList != null) {
                        if (agesList.size() != 0) {
                            tvYearChoose.setText(getString(R.string.robin268,agesList.get(TYPE_ONE)));
                        }
                    }

                    //集中城市
                    List<String> cities = creator.getCities();
                    if (cities != null && cities.size() != 0) {
                        for (int i = 0; i < cities.size(); i++) {
                            cityNameList.add(cities.get(i));
                        }
                        tvThirdChoose.setText(getString(R.string.robin269,Joiner.on(" ,").join(cityName(cityNameList))));
                    }
                    editLink.setText(creator.getContent_show());
                    if (! TextUtils.isEmpty(creator.getRemark())) {
                        editRemark.setText(creator.getRemark());
                    }
                }
            }
        }
    }

    private void showInviteDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_user_info_item, null);
        final CustomDialogManager manager = new CustomDialogManager(this, view);
        TextView tvFirst = (TextView) view.findViewById(R.id.tv_first);
        TextView tvSecond = (TextView) view.findViewById(R.id.tv_second);
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
                tvSecondChoose.setText(R.string.robin355);
            }
        });
        tvSecond.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                manager.dismiss();
                TYPE_TWO = 2;
                tvSecondChoose.setText(R.string.robin356);
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

    /**
     * 初始化数据
     */
    private void initData() {
        String data = CacheUtils.getString(CreatorMsgActivity.this, HelpTools.BASEINFO, null);
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
                    UserCircleBean bean = GsonTools.jsonToBean(response, UserCircleBean.class);
                    if (bean != null) {
                        if (bean.getError() == 0) {
                            CacheUtils.putString(CreatorMsgActivity.this, HelpTools.BASEINFO, response);
                            agesList = bean.getAges_list();
                            circlesList = bean.getCircles_list();
                            platList = bean.getTerraces_list();
                            authList = bean.getWeibo_auth_list();
                        }
                    }
                }
            });
        } else {
            UserCircleBean bean = GsonTools.jsonToBean(data, UserCircleBean.class);
            if (bean != null) {
                if (bean.getError() == 0) {
                    agesList = bean.getAges_list();
                    circlesList = bean.getCircles_list();
                    platList = bean.getTerraces_list();
                    authList = bean.getWeibo_auth_list();
                }
            }
        }
        if (agesList!=null){
            initPicker();
        }
        //        if (circlesList != null && circlesList.size() != 0) {
        //            if (circleIdList != null) {
        //                circleIdList.clear();
        //            }
        //            for (int i = 0; i < circlesList.size(); i++) {
        //                circleIdList.add(circlesList.get(i).getId());
        //            }
        //        }
    }

    /**
     检测参数
     @param show 是否开启toast
     @return
     */
    private Boolean checkParams(boolean show) {
        if (TextUtils.isEmpty(txChooseCircle) || txChooseCircle.equals(getString(R.string.robin074)) || circleIdList.size() == 0) {
            if (show) {
                CustomToast.showShort(this, R.string.robin074);
            }
            return false;
        }
        if (TextUtils.isEmpty(txPlatform) || txChooseCircle.equals(getString(R.string.robin076)) || platIdList.size() == 0) {
            if (show) {
                CustomToast.showShort(this, R.string.robin076);
            }
            return false;
        }
        if (TextUtils.isEmpty(txEditFirstPrice)) {
            if (show) {
                CustomToast.showShort(this, R.string.robin078);
            }
            return false;
        }
        if (TextUtils.isEmpty(txEditSecondPrice)) {
            if (show) {
                CustomToast.showShort(this, R.string.robin079);
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

        if (TextUtils.isEmpty(txChooseYear) || txChooseYear.equals(getString(R.string.robin083))) {

            if (show) {
                CustomToast.showShort(this, R.string.robin083);
            }
            return false;
        }

        if (TextUtils.isEmpty(txThirdChoose) || txThirdChoose.equals(getString(R.string.robin084)) || cityNameList.size() == 0) {

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

    /**
     * 监听Edit的焦点展示hint
     */
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
                case R.id.edit_first_num:
                    editShowText(b, editFirstNum, txEditFirstNum, editThirdBegin);
                    break;
            }
        }
    }

    /**
     * 根据Edit变化改变'保存'按键状态
     */
    class MyTextChange implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // setSave();
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            setSave();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            //  setSave();
        }

    }

    /**
     * 保存按键状态改变
     */
    private void setSave() {
        initText();
        if (checkParams(false) == true) {
            mTvSave.setTextColor(getResources().getColor(R.color.black_000000));
            mTvSave.setClickable(true);
        } else {
            mTvSave.setTextColor(getResources().getColor(R.color.gray_second));
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

    @Override
    protected void executeOnclickLeftView() {

    }

    @Override
    protected void executeOnclickRightView() {

    }

}
