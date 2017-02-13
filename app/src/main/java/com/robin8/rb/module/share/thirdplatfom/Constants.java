package com.robin8.rb.module.share.thirdplatfom;

/**
 * 
 * 第三方平台常量
 * 
 * @author weichao
 * 
 */
public class Constants {

	// 新浪微博APP KEY
	public static String SINA_APP_KEY = "2851445977";// 622531647
	// 新浪微博回调URL
	public static String SINA_REDIRECT_URL = "http://www.robin8.net";// http://mini.eastday.com/
	// 微信APP ID
	public static String WX_APP_ID = "";// wx6d89bc56971ef8e4

	public static String WX_APP_SECRET = "";// 7616b39b596840edfc3e9460aec08da4
	// QQ APP ID
	public static String QQ_APP_ID = "";// "1104868242" ;
	// QQ回调URL,网页授权使用
	public static final String QQ_REDIRECT_URL = "http://www.robin8.net";
	public static final String SINA_SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";

	public static final int PLATFORM_SINA = 3;

	public static final int ERROR_OAUTH_CANCEL = 1; // 授权取消
	public static final int ERROR_OAUTH_EXCEPTION = 2; // 授权出错
	public static final int ERROR_OAUTH_OK = 3; // 授权成功

	public static final int TYPE_LOGIN = 0;
	public static final int TYPE_BIND = 8;

}
