package com.robin8.rb.module.mine.presenter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.model.BaseBean;
import com.robin8.rb.model.LoginBean;
import com.robin8.rb.module.mine.adapter.MonthAdapter;
import com.robin8.rb.module.mine.model.SignHistoryModel;
import com.robin8.rb.module.mine.view.IUserSignView;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.presenter.PresenterI;
import com.robin8.rb.ui.widget.OtherGridView;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.DateUtil;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.view.widget.CustomDialogManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 @author Figo, zc */
public class UserSignPresenter extends BasePresenter implements PresenterI {

    protected static final String YYYY_MM_DD = "yyyy-MM-dd";
    private final IUserSignView mIUserView;
    private Activity mActivity;
    private WProgressDialog mWProgressDialog;
    private TextView mBottomTv;
    private SignHistoryModel mSignHistoryModel;
    private List<SignDay> mMonthList;
    private int mMonth;
    private OtherGridView mGridView;
    private MonthAdapter mMonthAdapter;
    private int year;
    private int month;
    public ImageView imgBack;
    public ImageView imgNext;
    public TextView mEarnAccumulatedTv;
    public TextView mEarnTodayTv;
    private List<String> listDate;
    private Map<String, Integer> mapHistory;

    public UserSignPresenter(Activity activity, IUserSignView userView) {
        mActivity = activity;
        mIUserView = userView;
    }

    public void init() {
        mBottomTv = mIUserView.getBottomTv();
        mGridView = mIUserView.getMonthGv();
        imgBack = mIUserView.backLast();
        imgNext = mIUserView.goNext();
        mEarnAccumulatedTv = mIUserView.mEarnAccumulatedTv();
        mEarnTodayTv = mIUserView.getEarnTodayTv();
        listDate = new ArrayList<>();
        mapHistory = new HashMap<>();
        updateView(0);
        getDataFromNet(0);
    }

    /**
     签到历史
     */
    private void getDataFromNet(final int where) {
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(mActivity);
        }
        mWProgressDialog.show();

        getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.CHECK_IN_HISTORY_URL), null, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
                // response = "{\"error\":0,\"continuous_checkin_count\":4,\"today_had_check_in\":true," + "\"checkin_history\":[" + "\"2017-12-07\"," + "\"2017-12-06\"," + "\"2017-12-05\"," + "\"2017-12-04\"," + "\"2017-12-03\"," + "\"2017-12-02\"," + "\"2017-12-01\"," + "\"2017-11-30\"," + "\"2017-11-29\"," + "\"2017-11-16\"," + "\"2017-11-15\"," + "\"2017-11-07\"," + "\"2017-11-06\"," + "\"2017-11-05\"," + "\"2017-11-04\"," + "\"2017-11-03\"," + "\"2017-11-02\"," + "\"2017-11-01\"," + "\"2017-10-29\"," + "\"2017-10-31\"]}";
                LogUtil.LogShitou("签到历史" + HelpTools.getUrl(CommonConfig.CHECK_IN_HISTORY_URL), response);
              //  response = "{\n" + "    \"error\": 0,\n" + "    \"continuous_checkin_count\": 1,\n" + "    \"today_had_check_in\": true,\n" + "    \"checkin_history\": [\n" + "        {\n" + "            \"created_at\": \"2017-12-08\",\n" + "            \"is_continuous\": 1\n" + "        },\n" + "        {\n" + "            \"created_at\": \"2017-12-07\",\n" + "            \"is_continuous\": 1\n" + "        },\n" + "        {\n" + "            \"created_at\": \"2017-12-06\",\n" + "            \"is_continuous\": 1\n" + "        },\n" + "              {\n" + "            \"created_at\": \"2017-12-05\",\n" + "            \"is_continuous\": 1\n" + "        },\n" + "      \n" + "        {\n" + "            \"created_at\": \"2017-12-03\",\n" + "            \"is_continuous\": 0\n" + "        },\n" + "      \n" + "        {\n" + "            \"created_at\": \"2017-12-01\",\n" + "            \"is_continuous\": 0\n" + "        },\n" + "        {\n" + "            \"created_at\": \"2017-11-30\",\n" + "            \"is_continuous\": 1\n" + "        },\n" + "        {\n" + "            \"created_at\": \"2017-11-29\",\n" + "            \"is_continuous\": 1\n" + "        },\n" + "        {\n" + "            \"created_at\": \"2017-11-28\",\n" + "            \"is_continuous\": 1\n" + "        },\n" + "        {\n" + "            \"created_at\": \"2017-11-20\",\n" + "            \"is_continuous\": 0\n" + "        },\n" + "        {\n" + "            \"created_at\": \"2017-11-17\",\n" + "            \"is_continuous\": 0\n" + "        },\n" + "        {\n" + "            \"created_at\": \"2017-11-14\",\n" + "            \"is_continuous\": 0\n" + "        },\n" + "        {\n" + "            \"created_at\": \"2017-11-12\",\n" + "            \"is_continuous\": 1\n" + "        },\n" + "        {\n" + "            \"created_at\": \"2017-11-11\",\n" + "            \"is_continuous\": 1\n" + "        },\n" + "       \n" + "        {\n" + "            \"created_at\": \"2017-10-31\",\n" + "            \"is_continuous\": 1\n" + "        },\n" + "        {\n" + "            \"created_at\": \"2017-10-30\",\n" + "            \"is_continuous\": 1\n" + "        }\n" + "    ],\n" + "    \"total_check_in_days\": 167,\n" + "    \"total_check_in_amount\": 24.9,\n" + "    \"today_already_amount\": 0,\n" + "    \"today_can_amount\": 0.2,\n" + "    \"tomorrow_can_amount\": 0.25\n" + "}";
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                parseJson(response);
                if (where==1){
                    //签到成功弹窗
                    showSuccessDialog();
                }
            }
        });
    }

    private void parseJson(String response) {
        if (TextUtils.isEmpty(response)) {
            return;
        }
        mSignHistoryModel = GsonTools.jsonToBean(response, SignHistoryModel.class);
        if (mSignHistoryModel != null && mSignHistoryModel.getError() == 0) {
            updateView(0);
        }
    }


    @Override
    public void getDataFromServer(boolean needHeader, int method, String url, RequestParams params, RequestCallback callback) {
        switch (method) {
            case HttpRequest.GET:
                HttpRequest.getInstance().get(needHeader, url, params, callback);
                break;
            case HttpRequest.POST:
                HttpRequest.getInstance().post(needHeader, url, params, callback);
                break;
            case HttpRequest.PUT:
                HttpRequest.getInstance().put(needHeader, url, params, callback);
                break;
        }
    }

    public void onDestroy() {
        if (mWProgressDialog != null) {
            mWProgressDialog = null;
        }
    }

    /**
     签到
     */
    public void sign() {
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(mActivity);
        }
        mWProgressDialog.show();

        getDataFromServer(true, HttpRequest.PUT, HelpTools.getUrl(CommonConfig.CHECK_IN_URL), null, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
               // LogUtil.LogShitou("签到" + HelpTools.getUrl(CommonConfig.CHECK_IN_URL), response);
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);
                if (bean == null) {
                    CustomToast.showShort(mActivity, mActivity.getString(R.string.please_data_wrong));
                    return;
                }
                if (bean.getError() == 0) {
                    //List<String> list = new ArrayList<String>();
                    //                    if (mSignHistoryModel != null) {
                    //                        List<SignHistoryModel.CheckinHistoryBean> checkin_history = mSignHistoryModel.getCheckin_history();
                    //                        if (checkin_history.size()!=0){
                    //                            for (int i = 0; i < checkin_history.size(); i++) {
                    //                                list.add(checkin_history.get(i).getCreated_at());
                    //                            }
                    //                        }
                    //                        //  list = mSignHistoryModel.getCheckin_history();
                    //                    }
                    //                   if (list == null) {
                    //                        list = new ArrayList<String>();
                    //                    }
                    //                    Date date = new Date(System.currentTimeMillis());
                    //                    DateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD);
                    //                    list.add(dateFormat.format(date));
                    //                    mSignHistoryModel.setCheckin_history(list);
                    //                    mSignHistoryModel.setContinuous_checkin_count(mSignHistoryModel.getContinuous_checkin_count() + 1);
                    //                    mSignHistoryModel.setToday_had_check_in(true);
                    //                    updateView(0);
                    getDataFromNet(1);

                } else {
                    if (!TextUtils.isEmpty(bean.getDetail())){
                        CustomToast.showShort(mActivity, bean.getDetail());
                    }
                }
            }
        });
    }

    private void showSuccessDialog() {
        String s = "";
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_sign_success, null);
        TextView tvInfo = (TextView) view.findViewById(R.id.tv_info);
        TextView tvKnow = (TextView) view.findViewById(R.id.tv_know);
        final CustomDialogManager cdm = new CustomDialogManager(mActivity, view);
        if (mSignHistoryModel!=null){
            s = "签到成功，"+StringUtil.addZeroForNum(String.valueOf(mSignHistoryModel.getToday_already_amount()),4)+"元奖励已放入您的钱包！\n您已连续签到"+mSignHistoryModel.getContinuous_checkin_count()+"天，不要间断哦～";

        }else {
            s = "签到成功";
        }
        tvInfo.setText(s);
       if (mSignHistoryModel!=null){
           int length = String.valueOf(mSignHistoryModel.getContinuous_checkin_count()).trim().length();
           StringUtil.setTextViewSpan(tvInfo, 33, 5, 9, mActivity.getResources().getColor(R.color.blue_custom));
           StringUtil.setTextViewSpan(tvInfo, 33, 27, 27+length, mActivity.getResources().getColor(R.color.blue_custom));
       }
        tvKnow.setOnClickListener(new View.OnClickListener() {

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


    public void lastMouth(int where) {
        if (where == 0) {
            //上个月
            imgBack.setVisibility(View.GONE);
            imgNext.setVisibility(View.GONE);
            updateView(- 1);
        } else {
            //下个月
            imgNext.setVisibility(View.GONE);
            imgBack.setVisibility(View.GONE);
            updateView(1);
        }

    }

    private void updateView(int i) {
        if (mSignHistoryModel != null) {
            if (mSignHistoryModel.getCheckin_history().size() != 0) {
                for (int j = 0; j < mSignHistoryModel.getCheckin_history().size(); j++) {
                    listDate.add(mSignHistoryModel.getCheckin_history().get(j).getCreated_at());
                    mapHistory.put(mSignHistoryModel.getCheckin_history().get(j).getCreated_at(), mSignHistoryModel.getCheckin_history().get(j).getIs_continuous());
                }
            }
        } else {
            return;
        }
        initData(i);

        LoginBean bean = BaseApplication.getInstance().getLoginBean();
        //昵称头像
        if (bean != null && bean.getKol() != null) {
            mIUserView.setUserNameTv(bean.getKol().getName());
            mIUserView.setCircleImageView(bean.getKol().getAvatar_url());
        }

        if (mSignHistoryModel != null) {
            //连续签到天数
            mIUserView.setHasSignedDaysTv(String.valueOf(mSignHistoryModel.getContinuous_checkin_count()));
            mEarnAccumulatedTv.setText(String.valueOf(mSignHistoryModel.getTotal_check_in_amount()));
            mEarnTodayTv.setText(String.valueOf(mSignHistoryModel.getToday_already_amount()));

        }
        if (mSignHistoryModel != null && mSignHistoryModel.isToday_had_check_in()) {
            //明日签到可领 0.00 元
            // mBottomTv.setText(mActivity.getString(R.string.sign_was_done));
            mBottomTv.setText("明日签到可领 "+mSignHistoryModel.getTomorrow_can_amount()+" 元");
            mBottomTv.setBackgroundResource(R.color.sub_gray_custom);
        } else {
            //今日签到可领 0.00 元
            //  mBottomTv.setText(mActivity.getString(R.string.sign_instantly));
            mBottomTv.setText("今日签到可领 "+mSignHistoryModel.getToday_can_amount()+" 元");
            mBottomTv.setBackgroundResource(R.color.blue_custom);
        }
        mMonth = month;
        mIUserView.setMonthTv(String.valueOf(mMonth));

        if (mMonthList != null && mMonthList.size() > 0) {
            mMonthAdapter = new MonthAdapter(mActivity);
        }
        mGridView.setAdapter(mMonthAdapter);
        mMonthAdapter.setMonthList(mMonthList);
    }

    public void initData(int where) {
        year = DateUtil.getYear();
        month = DateUtil.getMonth();
        if (where == - 1) {
            if (month == 1) {
                month = 12;
                year--;
            } else {
                month--;
            }
        } else if (where == 1) {
            month = month;
            year = year;
        }
        //LogUtil.LogShitou("查看当前的月和年", month + "   年===》" + year);
        int day = DateUtil.getDaysOfMonth(year, month);
        //        Calendar aCalendar = Calendar.getInstance();
        //        Date date = new Date(System.currentTimeMillis());
        //        aCalendar.setTime(date);
        //        int day = aCalendar.getActualMaximum(Calendar.DATE);
        //        //当前的月份
        //        mMonth = aCalendar.get(Calendar.MONTH) + 1;
        //        //计算当前月的第一天是星期几
        //        Calendar calendar = Calendar.getInstance();
        //        calendar.set(Calendar.DAY_OF_MONTH, 1);
        //        int lastSpace = calendar.get(Calendar.DAY_OF_WEEK) - 1;//预留的空间

        if (mMonthList != null) {
            mMonthList.clear();
        } else {
            mMonthList = new ArrayList<SignDay>();
        }
        List<Integer> lastSpaces = DateUtil.getLastDaysNow(year, month);
        //此处是在本月显示上个月的剩余日期，显示签到就必须-1
        if (lastSpaces != null && lastSpaces.size() != 0) {
            int spMonth = month;
            int spYear = year;
            if (spMonth == 1) {
                spMonth = 12;
                spYear--;
            } else {
                spMonth--;
            }

            for (int i = 0; i < lastSpaces.size(); i++) {
                SignDay dayInfo = null;
                dayInfo = new SignDay(String.valueOf(lastSpaces.get(i)), isSign(spYear, spMonth, lastSpaces.get(i)));
                mMonthList.add(dayInfo);
            }
        }
        for (int i = 1; i <= day; i++) {
            SignDay dayInfo = null;
            dayInfo = new SignDay(String.valueOf(i), isSign(year, month, i));
            mMonthList.add(dayInfo);
        }
    }

    /**
     @param year
     @param month
     @param day
     @return -1:未签到 ；0：间隔签到 ；1 :连续签到
     */
    private int isSign(int year, int month, int day) {
        String s = String.valueOf(year + "-" + month + "-" + day);
//        if (day<10 && month<10) {
//            s = String.valueOf(year + "-0" + month + "-0" + day);
//        }else if (day<10 && month>10){
//            s = String.valueOf(year + "-" + month + "-0" + day);
//        }else if (day>10 && month<10){
//            s = String.valueOf(year + "-0" + month + "-" + day);
//        }
        if (day<10){
            s = String.valueOf(year + "-" + month + "-0" + day);
        }

        if (mSignHistoryModel != null && mSignHistoryModel.getCheckin_history() != null && mSignHistoryModel.getCheckin_history().size() > 0) {
            if (listDate.size() != 0) {
                for (String dates : listDate) {
//                    int j = Integer.parseInt(dates.substring(8));   //  日
//                    int n = Integer.valueOf((dates.substring(5)).substring(0, 2));// 月
//                    int m = Integer.valueOf(dates.substring(0, 4));//  年
//                    if (year == m && month == n && day == j) {
//                        if (mapHistory != null) {
//                            return mapHistory.get(String.valueOf(year + "-" + month + "-" + day));
//                        } else {
//                            return - 1;
//                        }
//                    }
                    if (s.equals(dates)) {
                        //已签到，判读是否连续签到
                        if (mapHistory != null) {
                            return mapHistory.get(s);
                        } else {
                            return - 1;
                        }
                    }
                }
            }

        }
        return - 1;
    }


    public class SignDay {
        public String day;
        public int isSign;

        public SignDay(String s, int b) {
            this.day = s;
            this.isSign = b;
        }
    }
}
