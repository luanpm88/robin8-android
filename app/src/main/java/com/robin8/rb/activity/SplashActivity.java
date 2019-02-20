package com.robin8.rb.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.igexin.sdk.PushManager;
import com.robin8.rb.R;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.model.ContactBean;
import com.robin8.rb.model.LocationModel;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.receiver.DemoIntentService;
import com.robin8.rb.receiver.DemoPushService;
import com.robin8.rb.task.ContactNativeTask;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.DialogUtil;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.RequestCode;
import com.robin8.rb.util.UIUtils;
import com.robin8.rb.util.XPermissionUtils;

import java.util.List;


public class SplashActivity extends Activity {

    private static final String LOCATION_URL = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        XPermissionUtils.requestPermissions(this, RequestCode.MORE,
                new String[] { Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_SMS },
                new XPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        LogUtil.LogShitou("onPermissionGranted", "获取联系人,短信权限成功");
                    }

                    @Override
                    public void onPermissionDenied(String[] deniedPermissions, boolean alwaysDenied) {
                        StringBuilder sBuilder = new StringBuilder();
                        for (String deniedPermission : deniedPermissions) {
                            if (deniedPermission.equals(Manifest.permission.WRITE_CONTACTS)) {
                                sBuilder.append("联系人");
                                sBuilder.append(",");
                            }
                            if (deniedPermission.equals(Manifest.permission.READ_SMS)) {
                                sBuilder.append("短信");
                                sBuilder.append(",");
                            }
                        }
                        if (sBuilder.length() > 0) {
                            sBuilder.deleteCharAt(sBuilder.length() - 1);
                        }
                        CustomToast.showShort(SplashActivity.this, "获取" + sBuilder.toString() + "权限失败");
                        if (alwaysDenied) {
                            DialogUtil.showPermissionManagerDialog(SplashActivity.this, sBuilder.toString());
                        }
                    }
                });


        HelpTools.getKoluuidFromNet();
        if (!TextUtils.isEmpty(HelpTools.getCommonXml(HelpTools.SERVER))){
            CommonConfig.SERVICE = HelpTools.getCommonXml(HelpTools.SERVER);
        }
        PushManager.getInstance().initialize(this.getApplicationContext(), DemoPushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), DemoIntentService.class);

        getContacts();
        getLocation();
        goToNextPage(0);

    }

    /**
     * 获取联系人
     */
    private void getContacts() {
        if (BaseApplication.getInstance().getContactBeans() == null)//获取通讯录
            new ContactNativeTask(this, new ContactNativeTask.IGetContactSuccess() {

                @Override
                public void onGetContactSuccess(List<ContactBean> beans) {
                }
            }, false).execute();
    }

    private void goToNextPage(final int i) {

        UIUtils.runInMainThread(new Runnable() {

            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        SplashActivity.this.startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        SplashActivity.this.finish();

                    }
                }, 500);
            }
        });
    }

    private Intent addBundle(Intent intent) {
        Bundle bundle = new Bundle();
        bundle.putInt("from", SPConstants.PUSH_TO_DETAIL);
        bundle.putString("path", "http://baidu.com");
        bundle.putString("title", "nihao");
        bundle.putString("start_time", "140023020");
        bundle.putString("end_time", "1343455454");
        bundle.putString("introduce", "wa");
        bundle.putString("count_way", "click");
        bundle.putString("every_consume", "麦克布莱德");
        bundle.putString("address", "从 v不，、");
        bundle.putString("total_consume", "女款的地方");
        return intent.putExtras(bundle);
    }


    public void getLocation() {
        BasePresenter presenter = new BasePresenter();
        presenter.getDataFromServer(false, HttpRequest.GET, LOCATION_URL, null, new RequestCallback() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
                LocationModel locationModel = GsonTools.jsonToBean(response, LocationModel.class);
                if (locationModel != null && ! TextUtils.isEmpty(locationModel.getCity())) {
                    CacheUtils.putString(SplashActivity.this, SPConstants.LOCATION_CITY, locationModel.getCity());
                }
            }
        });
    }
}

