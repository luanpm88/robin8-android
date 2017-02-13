package com.robin8.rb.base;

import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.util.ShakeListenerUtils;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public abstract class BaseDataActivity extends SwipeBackActivity {

    protected String mPageName;
    protected ShakeListenerUtils shakeUtils;
    private SensorManager mSensorManager;
    protected long lastTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initShake();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(mPageName)) {
            StatisticsAgency.onPageStart(this, mPageName);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!TextUtils.isEmpty(mPageName)) {
            StatisticsAgency.onPageEnd(this, mPageName);
        }
    }

    @Override
    protected void onDestroy() {
        mSensorManager.unregisterListener(shakeUtils);
        super.onDestroy();
    }

    private void initShake() {
        shakeUtils = new ShakeListenerUtils();
        shakeUtils.setContext(this);
        //获取传感器管理服务
        mSensorManager = (SensorManager) this.getSystemService(Service.SENSOR_SERVICE);
        //加速度传感器
        mSensorManager.registerListener(shakeUtils,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                //还有SENSOR_DELAY_UI、SENSOR_DELAY_FASTEST、SENSOR_DELAY_GAME等，
                //根据不同应用，需要的反应速率不同，具体根据实际情况设定
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopShakeListener() {
        try {
            mSensorManager.unregisterListener(shakeUtils);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isDoubleClick() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastTime < 800) {
            return true;
        }
        lastTime = currentTimeMillis;
        return false;
    }
}
