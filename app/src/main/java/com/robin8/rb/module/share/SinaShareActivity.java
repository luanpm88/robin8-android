/**
 * 
 */
package com.robin8.rb.module.share;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseDataActivity;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.module.share.thirdplatfom.AccessTokenKeeper;
import com.robin8.rb.module.share.thirdplatfom.Apis;
import com.robin8.rb.module.share.thirdplatfom.Constants;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CustomToast;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;

import cn.sharesdk.sina.weibo.SinaWeibo;


/**
 * @author weichao
 * 
 */
public class SinaShareActivity extends BaseDataActivity implements OnClickListener {

	private TextView titleTv;
	private TextView rightTv;

	private EditText edit_share;
	TextView text_numofword;

	WProgressDialog progressDialog;

	String share_title;
	String share_url;
	String share_img;

	public static final int TYPE_NEWS = 0;
	public static final int TYPE_INVALIT = 1;
	int from;
	CustomPlatformActionListener platformActionListener;
	private ImageView backIv;

	/** 微博微博分享接口实例 */

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_weiboshare);
		initData();
		initTitleBar();
		initView();
		setAction();
		platformActionListener = new CustomPlatformActionListener(this);
	}


	@Override
	public void onResume() {
		super.onResume();
		mPageName = StatisticsAgency.SINA_SHARE;
	}

	private void initData() {
		share_title = getIntent().getStringExtra("share_title");
		share_img = getIntent().getStringExtra("share_img");
		share_url = getIntent().getStringExtra("share_url");
		from = getIntent().getIntExtra("from", TYPE_NEWS);
		Log.e("tag", "title==>" + share_title);
		Log.e("tag", "img==>" + share_img);
		Log.e("tag", "url==>" + share_url);
	}

	/**
	 * 初始化标题栏
	 */
	private void initTitleBar() {
		backIv =  (ImageView)findViewById(R.id.iv_back);
		titleTv = (TextView) findViewById(R.id.tv_title);
		rightTv = (TextView) findViewById(R.id.tv_right);
        View contentLL =   findViewById(R.id.ll_content);
        contentLL.setBackgroundResource(R.color.white_custom);
		backIv.setImageResource(R.mipmap.icon_btn_back_black);
		titleTv.setTextColor(getResources().getColor(R.color.sub_black_custom));
		rightTv.setTextColor(getResources().getColor(R.color.sub_black_custom));
		titleTv.setText("分享到新浪微博");
		rightTv.setText("发送");
		rightTv.setVisibility(View.VISIBLE);
        rightTv.setOnClickListener(this);
		backIv.setOnClickListener(this);
	}

	/**
	 * 初始化界面控件
	 */
	private void initView() {
		edit_share = (EditText) findViewById(R.id.edit_share);
		text_numofword = (TextView) findViewById(R.id.text_numofword);
		edit_share.setText(share_title);
		int textsize = 140 - share_title.length();
		text_numofword.setText(textsize + "");
	}


	public boolean checkShareTitle() {
		share_title = edit_share.getText().toString().trim();
		if (TextUtils.isEmpty(share_title)) {
			CustomToast.showShort(this, "请输入分享内容" );
			return false;
		}
		return true;
	}

	public void hidenKeyBoard() {
		InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit_share.getWindowToken(), 0);
	}

	/**
	 * 设置控件的事件
	 */
	private void setAction() {

		edit_share.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				int numOfWorld = s.length();
				text_numofword.setText((140 - numOfWorld) + "");
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.iv_back:
				onBackPressed();
				break;
			case R.id.tv_right:
				if (checkShareTitle()) {
					disposeLogic();
				}
				break;
		}

	}


	@Override
	public void onBackPressed() {
		CustomToast.showShort(this, "分享取消" );
		super.onBackPressed();
		hidenKeyBoard();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	private void disposeLogic() {

		Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(getApplicationContext(),
				Constants.PLATFORM_SINA);
		String token = "";
		if (accessToken != null) {
			token = accessToken.getToken();
			boolean isValid = accessToken.isSessionValid();
			Log.e("tag", "isValid==>" + isValid);
			if (isValid) {
				// 分享
				/*AccountManager manager = AccountManager.getInstance(SinaShareActivity.this);
				if (from == TYPE_NEWS && manager.isOnLine()
						&& com.oa.eastfirst.constants.Constants.SHOW_THIRDLOGIN_THIRDBIND_INVITE) {
					LoginInfo sinaInfo = manager.getAccountInfo(SyncStateContract.Constants.PLATFORM_SINA);
					if (sinaInfo == null || !sinaInfo.isBinding()) {
						// 绑定
						dogetThirdPlatformInfo(false);
						return;
					}
				}*/
				doShare(token);
				return;
			}
			// 授权
			dogetThirdPlatformInfo(true);
			return;
		}

	}

	private void dogetThirdPlatformInfo(boolean needOauth) {
//		if (progressDialog == null) {
//			progressDialog = WProgressDialog.createDialog(this);
//		}
//		progressDialog.show();
//		ThirdPlatformLoginHelper helper = new ThirdPlatformLoginHelper();
//		helper.doGetUserInfo(SinaShareActivity.this, Constants.PLATFORM_SINA, needOauth, new GetThirdPlatformUserInfoHttpResponseDispose(SinaShareActivity.this, null));
	}


//	class GetThirdPlatformUserInfoHttpResponseDispose extends SimpleHttpResponseDispose {
//
//		/**
//		 * @param context
//		 * @param dialog
//		 */
//		public GetThirdPlatformUserInfoHttpResponseDispose(Context context, Dialog dialog) {
//			super(context, dialog);
//			// TODO Auto-generated constructor stub
//		}
//
//		@Override
//		public boolean OnSucess(Object object) {
//			// TODO Auto-generated method stub
//			// object为第三方账号信息
//			tempInfo = (LoginInfo) object;
//			// 授权成功后:判断是否登录，已经登录，绑定，没登录，分享
//
//			Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(getApplicationContext(),
//					Constants.PLATFORM_SINA);
//			String token = "";
//			token = accessToken.getToken();
//			if (from == TYPE_INVALIT||!com.oa.eastfirst.constants.Constants.SHOW_THIRDLOGIN_THIRDBIND_INVITE) {
//				doShare(token);
//				return true;
//			}
//
//			doVerifyThirdIsBind(tempInfo);
//			return super.OnSucess(object);
//		}
//
//		@Override
//		public boolean onError(int errorCode) {
//			// TODO Auto-generated method stub
//			if (progressDialog != null) {
//				progressDialog.dismiss();
//			}
//			switch (errorCode) {
//			case ERROR_OAUTH_CANCEL:
//				MToast.showToast(context, "授权取消", Toast.LENGTH_SHORT);
//				break;
//			case ERROR_OAUTH_EXCEPTION:
//				MToast.showToast(context, "授权失败", Toast.LENGTH_SHORT);
//				break;
//			}
//			return super.onError(errorCode);
//		}
//
//		@Override
//		public boolean onError(int errorCode, String msg) {
//			// TODO Auto-generated method stub
//			if (progressDialog != null) {
//				progressDialog.dismiss();
//			}
//			return super.onError(errorCode, msg);
//		}
//
//		@Override
//		public boolean onNotWorkError() {
//			// TODO Auto-generated method stub
//			if (progressDialog != null) {
//				progressDialog.dismiss();
//			}
//			return super.onNotWorkError();
//		}
//
//	}

//	private void doVerifyThirdIsBind(LoginInfo info) {
//		VerifyThirdAccountIsBindedHelper helper = new VerifyThirdAccountIsBindedHelper();
//		int usertype = info.getPlatform();
//		String loginname = info.getThirdLoginName();
//		helper.verifyThirdAccountIsBinded(this, loginname, usertype, new ThirdIsBindDispos(this, null));
//	}

	private void doShare(String token) {
		if (progressDialog == null) {
			progressDialog = WProgressDialog.createDialog(this);
		}
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
		String status = share_title + share_url;
		WeiboParameters wbparams = new WeiboParameters(status);
		wbparams.put("access_token", token);
		wbparams.put("status", status);
		// modify by weichao
		// ,2016-04-15,分享到SINA，在没有图片的时候，使用文字分享，有图片，用图文分享(修改bug:TTAND-1565)。
		String shareUrl = Apis.SINA_SHARE_TEXT_URL;// 默认为文字分享
		if (!TextUtils.isEmpty(share_img)) {
			// 图片地址不为空，转为图文分享
			wbparams.put("url", share_img);
			shareUrl = Apis.SINA_SHARE_URL;
		}
		Log.e("tag", "token==>" + token);
		new AsyncWeiboRunner(this).requestAsync(shareUrl, wbparams, "POST", mListener);
	}

	/*private void doBindThirdAccount() {
		BindThirdAccountHelper helper = new BindThirdAccountHelper();
		int usertype = tempInfo.getPlatform();
		String accid = AccountManager.getInstance(this).getAccid();
		String loginname = tempInfo.getThirdLoginName();
		String nickname = tempInfo.getNickname();
		String headUrl = tempInfo.getFigureurl();
		int sex = tempInfo.getSex();
		helper.doSubmit(this, accid, loginname, usertype, nickname, headUrl, sex, "", "", new ThirdBindDispose(this,
				null), TYPE_BIND);
	}*/

	/*class ThirdBindDispose extends SimpleHttpResponseDispose {

		*//**
		 * @param context
		 * @param dialog
		 *//*
		public ThirdBindDispose(Context context, Dialog dialog) {
			super(context, dialog);
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean OnSucess() {
			// TODO Auto-generated method stub
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			MToast.showToast(context, "绑定成功", Toast.LENGTH_SHORT);
			// final Oauth2AccessToken oToken =
			// AccessTokenKeeper.readAccessToken(
			// SinaShareActivity.this, Constants.PLATFORM_SINA);
			// new Handler().postDelayed(new Runnable() {
			//
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			// doShare(oToken.getToken());
			// }
			//
			// }, 500);

			return super.OnSucess();
		}

		@Override
		public boolean onError(int errorCode) {
			// TODO Auto-generated method stub
			if (progressDialog != null) {
				progressDialog.dismiss();
			}

			// final Oauth2AccessToken oToken =
			// AccessTokenKeeper.readAccessToken(
			// SinaShareActivity.this, Constants.PLATFORM_SINA);
			// new Handler().postDelayed(new Runnable() {
			//
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			// doShare(oToken.getToken());
			// }
			//
			// }, 500);
			return super.onError(errorCode);
		}

		@Override
		public boolean onNotWorkError() {
			// TODO Auto-generated method stub
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			return super.onNotWorkError();
		}

		@Override
		public boolean onError(int errorCode, String msg) {
			// TODO Auto-generated method stub
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			if (!TextUtils.isEmpty(msg)) {
				MToast.showToast(context, msg, Toast.LENGTH_SHORT);
				return true;
			}

			MToast.showToast(context, "绑定失败", Toast.LENGTH_SHORT);
			return super.onError(errorCode);
		}
	}*/

	/*class ThirdIsBindDispos extends SimpleHttpResponseDispose {

		*//**
		 * @param context
		 * @param dialog
		 *//*
		public ThirdIsBindDispos(Context context, Dialog dialog) {
			super(context, dialog);
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean OnSucess(Object object) {
			// TODO Auto-generated method stub
			int ret = (Integer) object;
			Log.e("tag", "res======>" + ret);
			if (ret == 1) {
				// 未绑定
				if (AccountManager.getInstance(SinaShareActivity.this).isOnLine()) {
					doBindThirdAccount();
					return true;
				}
				final Oauth2AccessToken oToken = AccessTokenKeeper.readAccessToken(SinaShareActivity.this,
						Constants.PLATFORM_SINA);
				doShare(oToken.getToken());
			} else if (ret == 0) {
				// 绑定
				final Oauth2AccessToken oToken = AccessTokenKeeper.readAccessToken(SinaShareActivity.this,
						Constants.PLATFORM_SINA);
				doShare(oToken.getToken());
				// MToast.showToast(context, "该账号已经被其他账号绑定，请先解绑",
				// Toast.LENGTH_SHORT);
			}
			return super.OnSucess();
		}

		@Override
		public boolean onError() {
			// TODO Auto-generated method stub
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			return super.onError();
		}

		@Override
		public boolean onError(int errorCode) {
			// TODO Auto-generated method stub
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			return super.onError(errorCode);
		}

		@Override
		public boolean onError(int errorCode, String msg) {
			// TODO Auto-generated method stub
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			return super.onError(errorCode, msg);
		}

		@Override
		public boolean onNotWorkError() {
			// TODO Auto-generated method stub
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			return super.onNotWorkError();
		}

	}*/

	/**
	 * 微博 OpenAPI 回调接口。
	 */
	private RequestListener mListener = new RequestListener() {
		@Override
		public void onComplete(String response) {
			if (!TextUtils.isEmpty(response)) {
				Log.e("tag", "share===>" + response);
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				platformActionListener.onComplete(new SinaWeibo(), 0, null);
				SinaShareActivity.this.finish();
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			e.printStackTrace();
			Log.e("tag", "失败====》" + e);
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			platformActionListener.onError(new SinaWeibo(), 0, null);
		}
	};
}
