package com.robin8.rb.util;

import android.text.TextUtils;

import com.robin8.rb.module.first.model.SocialAccountsBean;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {


	/**
	 * 获取当前连接的网络类型
	 *
	 */
	public static int getSize(List list) {

		if(list == null){
			return 0;
		}
		return list.size();
	}

	public static int getIndex(String etStr, List<String> list) {
		if(list==null || list.size()<=0|| TextUtils.isEmpty(etStr)){
			return -1;
		}
		for(int i=0;i<list.size();i++){
			if(TextUtils.equals(etStr,list.get(i))){
				return i;
			}
		}
		return -1;
	}

	public static boolean deleteStrFromList(String etStr, List<String> list) {
		boolean deleteB;
		List<String> temp = new ArrayList<>();
		if(list==null || list.size()<=0|| TextUtils.isEmpty(etStr)){
			return false;
		}

		for(int i=0;i<list.size();i++){
			if(!TextUtils.equals(etStr,list.get(i))){
				temp.add(list.get(i));
			}
		}
		deleteB = temp.size()==list.size()? false:true;
		list.clear();
		list.addAll(temp);
		return deleteB;
	}

	public static int getIndex(List<SocialAccountsBean> list, String name) {
		if(list==null || list.size()<=0 || TextUtils.isEmpty(name)){
			return -1;
		}
		for(int i=0;i<list.size();i++){
			if(name.equals(list.get(i).getProvider_name())){
				return i;
			}
		}
		return -1;
	}

	public static String getFromText(List<SocialAccountsBean> list) {
		if(list == null || list.size()==0){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for(int i =0;i<list.size();i++){
			sb.append(list.get(i).getProvider_name()).append("/");
		}

		if(sb.length()<1){
			return null;
		}

		return sb.substring(0,sb.length()-1);
	}
}
