package com.robin8.rb.ui.module.share;

/**
 * 分享平台实体类,当前所支持的分享平台有:QQ,QQ空间,微信,微信朋友圈,新浪微博
 * 
 * @author weichao
 * 
 */
public class Platform {

	public static final int SHARE_WEBPAGE = 4;

	public static final String QQ_NAME = "QQ";
	public static final String QZONE_NAME = "QZONE";
	public static final String WECHATMOMENTS_NAME = "WechatMoments";
	public static final String WECHAT_NAME = "Wechat";
	public static final String SINA_NAME = "SINA";
	public static final String COPY_NAME = "COPY";

	private String tag;// 平台标签
	private String name;// 平台名称:QQ,QZONE,WechatMoments,Wechat,SINA,COPY
	private int image;// 平台图标
	private int id;

	public Platform() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Platform(int id, String tag, String name, int image) {
		super();
		this.id = id;
		this.tag = tag;
		this.name = name;
		this.image = image;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
