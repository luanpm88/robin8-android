package com.robin8.rb.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.igexin.sdk.PushManager;
import com.robin8.rb.R;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.model.ContactBean;
import com.robin8.rb.model.LocationModel;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.task.ContactNativeTask;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.UIUtils;

import java.util.List;


public class SplashActivity extends Activity {

    private static final String LOCATION_URL = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        HelpTools.getKoluuidFromNet();
        PushManager.getInstance().initialize(this.getApplicationContext());
        getContacts();
        getLocation();
        goToNextPage();
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

    private void goToNextPage() {

        UIUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        SplashActivity.this.startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SplashActivity.this.finish();
                            }
                        },500);
                    }
                }, 1500);
            }
        });
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
                if(locationModel!= null && !TextUtils.isEmpty(locationModel.getCity())){
                    CacheUtils.putString(SplashActivity.this, SPConstants.LOCATION_CITY,locationModel.getCity());
                }
            }
        });
    }
}
