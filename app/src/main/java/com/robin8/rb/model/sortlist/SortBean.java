package com.robin8.rb.model.sortlist;


import com.robin8.rb.model.BaseBean;

import java.util.Comparator;

/**
 * 
 * @Description 需要按拼音排序的bean
 * @author DLJ
 * @date 2015年10月27日 上午11:08:28
 */
public abstract class SortBean extends BaseBean implements ISort {
	private String sortLetters; // 对应的英文字母开头 用于按字母排序

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public static class PinyinComparator implements Comparator<SortBean> {
		@Override
		public int compare(SortBean o1, SortBean o2) {
			if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
				return -1;
			} else if (o1.getSortLetters().equals("#") || o2.getSortLetters().equals("@")) {
				return 1;
			} else {
				return o1.getSortLetters().compareTo(o2.getSortLetters());
			}
		}

	}

}
