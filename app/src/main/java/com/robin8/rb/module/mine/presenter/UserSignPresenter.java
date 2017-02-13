package com.robin8.rb.module.mine.presenter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.view.widget.CustomDialogManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @author Figo
 */
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

    public UserSignPresenter(Activity activity, IUserSignView userView) {
        mActivity = activity;
        mIUserView = userView;
    }

    public void init() {
        mBottomTv = mIUserView.getBottomTv();
        mGridView = mIUserView.getMonthGv();

        updateView();
        getDataFromNet();
    }

    private void getDataFromNet() {
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
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                parseJson(response);
            }
        });
    }

    private void parseJson(String response) {
        if (TextUtils.isEmpty(response)) {
            return;
        }
        mSignHistoryModel = GsonTools.jsonToBean(response, SignHistoryModel.class);
        if (mSignHistoryModel != null && mSignHistoryModel.getError() == 0) {
            updateView();
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
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);
                if (bean == null) {
                    CustomToast.showShort(mActivity, mActivity.getString(R.string.please_data_wrong));
                    return;
                }
                if (bean.getError() == 0) {
                    List<String> list = null;
                    if (mSignHistoryModel != null) {
                        list = mSignHistoryModel.getCheckin_history();
                    }
                    if (list == null) {
                        list = new ArrayList<String>();
                    }
                    Date date = new Date(System.currentTimeMillis());
                    DateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD);
                    list.add(dateFormat.format(date));
                    mSignHistoryModel.setCheckin_history(list);
                    mSignHistoryModel.setContinuous_checkin_count(mSignHistoryModel.getContinuous_checkin_count() + 1);
                    mSignHistoryModel.setToday_had_check_in(true);
                    updateView();
                    showSuccessDialog();
                } else {
                    CustomToast.showShort(mActivity, bean.getDetail());
                }
            }
        });
    }

    private void showSuccessDialog() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_sign_success, null);
        final CustomDialogManager cdm = new CustomDialogManager(mActivity, view);
        cdm.dg.setCanceledOnTouchOutside(true);
        cdm.dg.getWindow().setGravity(Gravity.CENTER);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        cdm.showDialog();

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                cdm.dismiss();
            }
        },1000);
    }

    private void updateView() {

        initData();

        LoginBean bean = BaseApplication.getInstance().getLoginBean();
        if (bean != null && bean.getKol() != null) {
            mIUserView.setUserNameTv(bean.getKol().getName());
            mIUserView.setCircleImageView(bean.getKol().getAvatar_url());
        }

        if (mSignHistoryModel != null) {
            mIUserView.setHasSignedDaysTv(String.valueOf(mSignHistoryModel.getContinuous_checkin_count()));
        }
        if (mSignHistoryModel != null && mSignHistoryModel.isToday_had_check_in()) {
            mBottomTv.setText(mActivity.getString(R.string.sign_was_done));
            mBottomTv.setBackgroundResource(R.color.sub_gray_custom);
        } else {
            mBottomTv.setText(mActivity.getString(R.string.sign_instantly));
            mBottomTv.setBackgroundResource(R.color.blue_custom);
        }
        mIUserView.setMonthTv(String.valueOf(mMonth));

        if (mMonthList != null && mMonthList.size() > 0) {
            mMonthAdapter = new MonthAdapter(mActivity);
        }
        mGridView.setAdapter(mMonthAdapter);
        mMonthAdapter.setMonthList(mMonthList);
    }

    public void initData() {
        Calendar aCalendar = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        aCalendar.setTime(date);
        int day = aCalendar.getActualMaximum(Calendar.DATE);
        mMonth = aCalendar.get(Calendar.MONTH) + 1;
        int lastSpace = aCalendar.getFirstDayOfWeek();
        if (mMonthList != null) {
            mMonthList.clear();
        } else {
            mMonthList = new ArrayList<SignDay>();
        }
        for (int i = 0; i < lastSpace; i++) {
            mMonthList.add(i, null);
        }
        for (int i = 1; i <= day; i++) {
            SignDay dayInfo = null;

            if (isSign(i)) {
                dayInfo = new SignDay(String.valueOf(i), true);
            } else {
                dayInfo = new SignDay(String.valueOf(i), false);
            }
            mMonthList.add(dayInfo);
        }
    }

    private boolean isSign(int i) {
        boolean sign = false;
        if (mSignHistoryModel != null && mSignHistoryModel.getCheckin_history() != null && mSignHistoryModel.getCheckin_history().size() > 0) {
            for (String dates : mSignHistoryModel.getCheckin_history()) {
                int j = Integer.parseInt(dates.substring(8));
                if (i == j) {
                    return true;
                }
            }
        }
        return sign;
    }

    public class SignDay {
        public String day;
        public boolean isSign;

        public SignDay(String s, boolean b) {
            this.day = s;
            this.isSign = b;
        }
    }
}
