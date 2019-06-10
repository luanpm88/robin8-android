package com.robin8.rb.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.robin8.rb.R;


/**
 * Dialog工具类
 * @date 2017/10/30
 */
public class DialogUtil {

    public static void showPermissionManagerDialog(final Context context, String str) {
        showPermissionManagerDialogCallBack(context,str,null);
    }


    public static void showPermissionManagerDialogCallBack(final Context context, String str,DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(context).setTitle(context.getString(R.string.robin525,str))
            .setMessage(context.getString(R.string.robin559,context.getString(R.string.app_name),str))
            .setNegativeButton(R.string.cancel, cancelListener)
            .setPositiveButton(R.string.robin527, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                    context.startActivity(intent);
                }
            })
            .show();
    }

}
