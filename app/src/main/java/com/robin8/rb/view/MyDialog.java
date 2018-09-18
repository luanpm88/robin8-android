package com.robin8.rb.view;

import android.app.Dialog;
import android.content.Context;

import com.robin8.rb.R;


/**
 * Created by ninth on 2015/11/7.
 */
public class MyDialog {
    public static Dialog createLoadingDialog(Context context) {
        Dialog mDialog = new Dialog(context, R.style.loading_dialog);
        mDialog.setContentView(R.layout.web_progress);
        return mDialog;

    }
}
