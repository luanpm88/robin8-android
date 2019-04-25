/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.robin8.rb.ui.module.find.FloatAction.permission;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.ui.module.find.FloatAction.permission.rom.HuaweiUtils;
import com.robin8.rb.ui.module.find.FloatAction.permission.rom.MeizuUtils;
import com.robin8.rb.ui.module.find.FloatAction.permission.rom.MiuiUtils;
import com.robin8.rb.ui.module.find.FloatAction.permission.rom.QikuUtils;
import com.robin8.rb.ui.module.find.FloatAction.permission.rom.RomUtils;
import com.robin8.rb.ui.dialog.CustomDialogManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * Des:悬浮窗权限适配，很全的适配http://blog.csdn.net/self_study/article/details/52859790
 */

public class FloatPermissionManager {
    private static final String TAG = "FloatPermissionManager";

    private static volatile FloatPermissionManager instance;

    public static FloatPermissionManager getInstance() {
        if (instance == null) {
            synchronized (FloatPermissionManager.class) {
                if (instance == null) {
                    instance = new FloatPermissionManager();
                }
            }
        }
        return instance;
    }

    public boolean applyFloatWindow(Context context) {
        if (checkPermission(context)) {
            return true;
        } else {
            applyPermission(context);
            return false;
        }
    }

    private boolean checkPermission(Context context) {
        //6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                return miuiPermissionCheck(context);
            } else if (RomUtils.checkIsMeizuRom()) {
                return meizuPermissionCheck(context);
            } else if (RomUtils.checkIsHuaweiRom()) {
                return huaweiPermissionCheck(context);
            } else if (RomUtils.checkIs360Rom()) {
                return qikuPermissionCheck(context);
            }
        }
        return commonROMPermissionCheck(context);
    }

    private boolean huaweiPermissionCheck(Context context) {
        return HuaweiUtils.checkFloatWindowPermission(context);
    }

    private boolean miuiPermissionCheck(Context context) {
        return MiuiUtils.checkFloatWindowPermission(context);
    }

    private boolean meizuPermissionCheck(Context context) {
        return MeizuUtils.checkFloatWindowPermission(context);
    }

    private boolean qikuPermissionCheck(Context context) {
        return QikuUtils.checkFloatWindowPermission(context);
    }

    private boolean commonROMPermissionCheck(Context context) {
        //最新发现魅族6.0的系统这种方式不好用，天杀的，只有你是奇葩，没办法，单独适配一下
        if (RomUtils.checkIsMeizuRom()) {
            return meizuPermissionCheck(context);
        } else {
            Boolean result = true;
            if (Build.VERSION.SDK_INT >= 23) {
                try {
                    Class clazz = Settings.class;
                    Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
                    result = (Boolean) canDrawOverlays.invoke(null, context);
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
            return result;
        }
    }

    private void applyPermission(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                miuiROMPermissionApply(context);
            } else if (RomUtils.checkIsMeizuRom()) {
                meizuROMPermissionApply(context);
            } else if (RomUtils.checkIsHuaweiRom()) {
                huaweiROMPermissionApply(context);
            } else if (RomUtils.checkIs360Rom()) {
                ROM360PermissionApply(context);
            }
        }
        commonROMPermissionApply(context);
    }

    private void ROM360PermissionApply(final Context context) {
        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    QikuUtils.applyPermission(context);
                } else {
                  //  Log.e(TAG, "ROM:360, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void huaweiROMPermissionApply(final Context context) {
        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    HuaweiUtils.applyPermission(context);
                } else {
                  //  Log.e(TAG, "ROM:huawei, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void meizuROMPermissionApply(final Context context) {
        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    MeizuUtils.applyPermission(context);
                } else {
                 //   Log.e(TAG, "ROM:meizu, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void miuiROMPermissionApply(final Context context) {
        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    MiuiUtils.applyMiuiPermission(context);
                } else {
                  //  Log.e(TAG, "ROM:miui, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    /**
     * 通用 rom 权限申请
     */
    private void commonROMPermissionApply(final Context context) {
        //这里也一样，魅族系统需要单独适配
        if (RomUtils.checkIsMeizuRom()) {
            meizuROMPermissionApply(context);
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                showConfirmDialog(context, new OnConfirmResult() {
                    @Override
                    public void confirmResult(boolean confirm) {
                        if (confirm) {
                            try {
                                Class clazz = Settings.class;
                                Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");

                                Intent intent = new Intent(field.get(null).toString());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setData(Uri.parse("package:" + context.getPackageName()));
                                context.startActivity(intent);
                            } catch (Exception e) {
                                Log.e(TAG, Log.getStackTraceString(e));
                            }
                        } else {
                            Log.d(TAG, "user manually refuse OVERLAY_PERMISSION");
                            //需要做统计效果
                        }
                    }
                });
            }
        }
    }

    private void showConfirmDialog(Context context, OnConfirmResult result) {
        showConfirmDialog(context, "您的手机没有授予悬浮窗权限，请开启后再试", result);
    }

    private void showConfirmDialog(Context context, String message, final OnConfirmResult result) {

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_ad_host, null);
        TextView tvMsg = (TextView) view.findViewById(R.id.tv_info);
        TextView tvOk = (TextView) view.findViewById(R.id.tv_right);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_left);
        LinearLayout cancel = (LinearLayout) view.findViewById(R.id.layout_left);
        LinearLayout ok = (LinearLayout) view.findViewById(R.id.layout_right);
        tvMsg.setText(message);
        tvOk.setText("现在去开启");
        tvCancel.setText("暂不开启");
        final CustomDialogManager cdm = new CustomDialogManager(context, view);

        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                cdm.dismiss();
                result.confirmResult(true);

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                cdm.dismiss();
                result.confirmResult(false);
            }
        });
        cdm.dg.setCanceledOnTouchOutside(true);
        Window window = cdm.dg.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.AnimCenter);
        cdm.dg.show();
    }

    private interface OnConfirmResult {
        void confirmResult(boolean confirm);
    }
}
