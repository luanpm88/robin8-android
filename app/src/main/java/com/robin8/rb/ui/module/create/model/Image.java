package com.robin8.rb.ui.module.create.model;

import java.io.Serializable;

public class Image implements Serializable {

	private static final long serialVersionUID = 1L;

	private int idx;
	private String src;
	private String alt;
	private String describe;
	private String imgname;
	private int imgwidth;
	private int imgheight;

	public void setImgwidth(int imgwidth) {
		this.imgwidth = imgwidth;
	}

	public void setImgheight(int imgheight) {
		this.imgheight = imgheight;
	}


	public Image(int imgwidth, int imgheight, String src) {
		super();
		this.imgwidth = imgwidth;
		this.imgheight = imgheight;
		this.src = src;
	}

	public Image() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getImgname() {
		return imgname;
	}

	public void setImgname(String imgname) {
		this.imgname = imgname;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public int getImgwidth() {
		// TODO Auto-generated method stub
		return imgwidth;
	}

	public int getImgheight() {
		// TODO Auto-generated method stub
		return imgheight;
	}
}
