package com.robin8.rb.util;

import android.text.TextUtils;

import com.robin8.rb.module.create.model.Image;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


/**
 * 使用JSoup解析html数据
 * 
 * @author weichao
 * 
 */
public class HtmlUtils {
	/**
	 * 获取网页标题
	 * 
	 * @param html
	 *            源HTML
	 * @return
	 */

	private static final String ASSERT_DEFAULT_IMG = "file:///android_asset/webview_no_image.png";

	public static String getHtmlTitle(Document document) {
		if (document == null) {
			return null;
		}
		String title = document.title();
		return title;
	}

	/**
	 * 获取内容里面的title
	 * 
	 * @param document
	 * @return
	 */
	public static String getContentTitle(Document document) {
		if (document == null) {
			return null;
		}
		Element titleDiv = document.select("#title").get(0);
		String contentTitle = titleDiv.getElementsByClass("title").text();
		return contentTitle;

	}

	/**
	 * 获取新闻来源
	 * 
	 * @param document
	 * @return
	 */
	public static String getHtmlSrc(Document document) {
		if (document == null) {
			return null;
		}
		Element titleDiv = document.select("#title").get(0);
		String contentSrc = titleDiv.getElementsByClass("src").text();
		return contentSrc;
	}

	/**
	 * 获取网页内容
	 */
	public static String getHtmlContent(Document document) {
		if (document == null) {
			return null;
		}
		Element bodyElement = document.body();
		String content = bodyElement.getElementById("content").html();
		return content;
	}

	/**
	 * 获取网页图片列表,并替换成本地URL
	 * 
	 * @return
	 */
	public static String getHtmlImageListWithReplace(Document document, String path, List<String> imgList,
			boolean needImage) {
		if (document == null) {
			return null;
		}
		Elements imgElements = document.getElementsByTag("img");
		for (Element imgElement : imgElements) {
			String imgSrc = imgElement.attr("src");
			if (needImage) {
				if (!TextUtils.isEmpty(path)) {
					imgElement.attr("src", getImageLocalFileUri(path, imgSrc));
				}
			} else {
				imgElement.attr("src", ASSERT_DEFAULT_IMG);
			}
			imgList.add(imgSrc);
		}
		String html = document.html();
		return html;
	}

	public static List<Image> getHtmlImageList(String html) {
		if (TextUtils.isEmpty(html)) {
			return null;
		}
		Document document = Jsoup.parse(html);
		List<Image> imgList = new ArrayList<Image>();
		Elements imgElements = document.getElementsByTag("img");
		for (Element imgElement : imgElements) {
			String imgSrc = imgElement.attr("data-src");
			String imgName = imgElement.attr("alt");
			Image img = new Image();
			img.setSrc(imgSrc);
			img.setImgname(imgName);
			imgList.add(img);
		}
		return imgList;
	}

	/**
	 * 移除多余的div标签(原本在html加载时用js移除，但是部分手机不支持那段js),这里可以改用正则表达式优化
	 * 
	 * @param contentString
	 *            html源码
	 * @return
	 */
	public static String removeExtraDivr(String contentString) {
		String regular = "<div id=\"news_check\">";
		int index = contentString.indexOf(regular);
		contentString = contentString.substring(0, index);
		contentString = contentString + "</body></html>";
		return contentString;

	}



	/**
	 * 获取图片本地存储地址
	 * 
	 * @param path
	 * @param url
	 * @return
	 */
	public static String getImageLocalFileUri(String path, String url) {
		// 将下载链接转MD5作为文件名
		String[] temp = url.split("\\.");
		String pix = temp[temp.length - 1];
		String fName = AppUtils.to16MD5(url) + "." + pix;
		String cpath = "file://" + path + "/" + fName;
		return cpath;
	}


	private static String createLinkOnUri(String uri) {
		String linkHtml = "<link rel='stylesheet' href=\"" + uri + "\"/>";
		return linkHtml;
	}

}
