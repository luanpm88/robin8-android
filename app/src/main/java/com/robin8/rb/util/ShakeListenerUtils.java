package com.robin8.rb.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.ui.module.mine.activity.FeedBackActivity;

/**
 * 摇一摇管理
 */
public class ShakeListenerUtils implements SensorEventListener {
    public ShakeListenerUtils() {
        super();
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    private Activity context;
    // 速度阈值，当摇晃速度达到这值后产生作用
    private static final int SPEED_SHRESHOLD = 2000;
    // 两次检测的时间间隔
    private static final int UPTATE_INTERVAL_TIME = 70;
    // 传感器管理器
    private SensorManager sensorManager;
    // 传感器
    private Sensor sensor;
    // 上下文
    private Context mContext;
    // 手机上一个位置时重力感应坐标
    private float lastX;
    private float lastY;
    private float lastZ;
    // 上次检测时间
    private long lastUpdateTime;

    // 重力感应器感应获得变化数据
    @Override
    public void onSensorChanged(SensorEvent event) {
        // 现在检测时间
        long currentUpdateTime = System.currentTimeMillis();
        // 两次检测的时间间隔
        long timeInterval = currentUpdateTime - lastUpdateTime;
        // 判断是否达到了检测时间间隔
        if (timeInterval < UPTATE_INTERVAL_TIME)
            return;
        // 现在的时间变成last时间
        lastUpdateTime = currentUpdateTime;
        // 获得x,y,z坐标
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        // 获得x,y,z的变化值
        float deltaX = x - lastX;
        float deltaY = y - lastY;
        float deltaZ = z - lastZ;
        // 将现在的坐标变成last坐标
        lastX = x;
        lastY = y;
        lastZ = z;
        double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ  * deltaZ) / timeInterval * 10000;
        // 达到速度阀值，发出提示
        boolean feedbackB = CacheUtils.getBoolean(context, SPConstants.FEEDBACK_TOGGLE, true);
        if (!allowShake()) {//判断是否为重复晃动
            return;
        }
        int sensorType = event.sensor.getType();
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            if (speed >= SPEED_SHRESHOLD && feedbackB) {
                Intent intent = new Intent(context, FeedBackActivity.class);
                try {
                    Bitmap bitmapFromView = CommonUtil.getBitmapFromView(context.getWindow().getDecorView());
                    intent.putExtra("bitmap", true);
                    BaseApplication.getInstance().setScreenShot(bitmapFromView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                context.startActivity(intent);
            }
        }
    }

    private long lastTime;

    private boolean allowShake() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastTime > 2000) {
            lastTime = currentTimeMillis;
            return true;
        }
        return false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //当传感器精度改变时回调该方法，Do nothing.
    }

}
