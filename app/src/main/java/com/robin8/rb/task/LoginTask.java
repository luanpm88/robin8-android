package com.robin8.rb.task;

import android.content.Context;

import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.ui.model.LoginBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.util.AppUtils;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.StringUtil;

import java.util.Arrays;
import java.util.List;


/**
 @author DLJ
 @Description 登录任务
 @date 2016/1/25 11:05 */
public class LoginTask {

//    private final static String TD_CHANNEL_ID = "TD_CHANNEL_ID";
    public final static String TD_CHANNEL_ID = "TD_ANALYTICS_CHANNEL_ID";
    private static Context mContext;

    private LoginTask() {
    }

    public static LoginTask newInstance(Context context) {
        mContext = context;
        return new LoginTask();
    }

    public void start(final RequestCallback callback, String... p) {
        List<String> objects = Arrays.asList(p);

        RequestParams requestParams = new RequestParams();
        requestParams.put("mobile_number", objects.get(0));
        requestParams.put("code", objects.get(1));
        requestParams.put("kol_uuid", objects.get(2));
        requestParams.put("invite_code", objects.get(3));
        requestParams.put("app_platform", SPConstants.ANDROID);
        requestParams.put("app_version", AppUtils.getVersionName(mContext));
        requestParams.put("os_version", AppUtils.getSystemVersion());
        requestParams.put("device_model", android.os.Build.MODEL);
        requestParams.put("city_name", CacheUtils.getString(mContext, SPConstants.LOCATION_CITY, ""));
        requestParams.put("device_token", StringUtil.getToken(mContext));
        requestParams.put("IMEI", AppUtils.getImei(mContext));
        requestParams.put("utm_source", AppUtils.getApplicationMeteData(mContext, TD_CHANNEL_ID));

        LogUtil.LogShitou("参数值", "mobile_number：" + objects.get(0) + "\n" + "code：" + objects.get(1) + "\n" + "kol_uuid：" + objects.get(2) + "invite_code：" + objects.get(3) + "\n" + "app_platform：" + SPConstants.ANDROID + "\n" + "app_version：" + AppUtils.getVersionName(mContext) + "\n" + "os_version：" + AppUtils.getSystemVersion() + "\n" + "device_model：" + android.os.Build.MODEL + "\n" + "city_name：" + CacheUtils.getString(mContext, SPConstants.LOCATION_CITY, "") + "\n" + "device_token：" + StringUtil.getToken(mContext) + "\n" + "IMEI：" + AppUtils.getImei(mContext) + "\n" + "utm_source：" + AppUtils.getApplicationMeteData(mContext, TD_CHANNEL_ID));
        HttpRequest.getInstance().post(true, HelpTools.getUrl(CommonConfig.SIGN_IN_URL), requestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }

            @Override
            public void onResponse(String response) {
                //LogUtil.LogShitou("登陆interface" + "sign_in", response);
                LoginBean loginBean = GsonTools.jsonToBean(response, LoginBean.class);
                if (loginBean != null && loginBean.getError() == 0) {
                    HelpTools.insertLoginInfo(HelpTools.Token, BaseApplication.decodeToken(loginBean.getKol().getIssue_token()));
                    HelpTools.insertLoginInfo(HelpTools.LoginNumber, loginBean.getKol().getMobile_number());
                    BaseApplication.getInstance().setLoginBean(loginBean);
                    if (BaseApplication.getInstance().hasLogined()) {
                        NotifyManager.getNotifyManager().notifyChange(NotifyManager.TYPE_LOGIN);//发送消息
                        // Intent intent1 = new Intent(mContext, MainActivity.class);
                        //                        intent1.putExtra("register_main", "zhu");
                        //
                        //                        LogUtil.LogShitou("这是新用户", "dfsdsdvdsvsvsdvsdv");
                        //                        mContext.startActivity(intent1);
                    }
                }
                //                else {
                //                    if (loginBean.getError()==1){
                //
                //                        try {
                //                            if (!TextUtils.isEmpty(loginBean.getDetail())){
                //                                CustomToast.showShort(mContext,loginBean.getDetail());
                //                            }
                //                        }catch (Exception e){
                //                            e.printStackTrace();
                //                        }
                //                    }
                //                }
                callback.onResponse(response);
            }
        });
    }
}


