package com.robin8.rb.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.robin8.rb.R;


public class WProgressDialogWithNoBg extends Dialog {

	private static WProgressDialogWithNoBg customProgressDialog = null;

	public WProgressDialogWithNoBg(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public WProgressDialogWithNoBg(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		// this.context = context;
	}

	public WProgressDialogWithNoBg(Context context) {
		super(context);
		// this.context = context;
	}

	public static WProgressDialogWithNoBg createDialog(Context context) {
		customProgressDialog = new WProgressDialogWithNoBg(context, R.style.WeslyDialogNobg);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.widget_progressdialog, null);
		layout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sharp_toast_day));
		customProgressDialog.setContentView(layout);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		// LayoutParams params =
		// customProgressDialog.getWindow().getAttributes() ;
		// params.gravity = Gravity.BOTTOM ;
		// params.y = UIUtils.dip2px(30) ;
		// params.verticalMargin=UIUtils.dip2px(30) ;
		// customProgressDialog.getWindow().setAttributes(params) ;
		// customProgressDialog.getWindow().setAttributes(params) ;
		// customProgressDialog.setCancelable(false) ;
		return customProgressDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus) {

		if (customProgressDialog == null) {
			return;
		}

//		ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.loadingImageView);
//		AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
//		animationDrawable.start();
	}

	public WProgressDialogWithNoBg setTitile(String strTitle) {
		return customProgressDialog;
	}

	public WProgressDialogWithNoBg setMessage(String strMessage) {
		TextView tvMsg = (TextView) customProgressDialog.findViewById(R.id.id_tv_loadingmsg);

		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}

		return customProgressDialog;
	}

	public WProgressDialogWithNoBg setMessage(int strMessage) {
		TextView tvMsg = (TextView) customProgressDialog.findViewById(R.id.id_tv_loadingmsg);
		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}

		return customProgressDialog;
	}

}
