/**
 * 
 */
package com.robin8.rb.ui.module.share.thirdplatfom;

/**
 * 第三方平台授权及获取用户信息URL
 * 
 * @author weichao
 * 
 */
public class Apis {
	// 新浪微博获取用户信息URL
	public static final String SINA_USER_URL = "https://api.weibo.com/2/users/show.json";
	// 新浪微博获取TOKEN URL
	public static final String SINA_REFRESH_TOKEN_URL = "https://api.weibo.com/oauth2/access_token?client_id=YOUR_CLIENT_ID&client_secret=YOUR_CLIENT_SECRET&grant_type=refresh_token&redirect_uri=YOUR_REGISTERED_REDIRECT_URI&refresh_token=";
	// 新浪微博分享图文URL
	public static final String SINA_SHARE_URL = "https://api.weibo.com/2/statuses/upload_url_text.json";
	// modify by weichao ,2016-04-15,增加新浪微博的文字分享。新浪微博分享文字URL
	public static final String SINA_SHARE_TEXT_URL = "https://api.weibo.com/2/statuses/update.json";
	// 新浪微博分享URL
	public static final String WX_ACCESSTOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";// ?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code"
	// 微信获取 TOKEN URL // ;
	public static final String WX_REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
	// 微信获取用户信息URL
	public static final String WX_USER_URL = "https://api.weixin.qq.com/sns/userinfo";// ?access_token=ACCESS_TOKEN&openid=OPENID";
	// QQ授权URL,用于网页授权
	public static final String QQ_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";

}
