package com.robin8.rb.module.share;

/**
 * 分享相关实体类
 * 
 * @author weichao
 * 
 */
public class ShareParams {

	private String subTitle;// 分享的副标题
	private String title;// 分享的标题
	private String text;// 分享的内容
	private String imageUrl;// 网络图片地址
	private String imagePath;// 本地图片地址
	private String url;// 分享的链接
	private int shareType;// 分享的类型,仅qq空间使用
	private int from;// 用于区分新闻分享和其他分享

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getShareType() {
		return shareType;
	}

	public void setShareType(int shareType) {
		this.shareType = shareType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
