package com.robin8.rb.model;

import com.google.gson.annotations.Expose;
import com.robin8.rb.model.sortlist.SortBean;

/**
 * 联系人实体类
 *
 * @author guolin
 */
public class ContactBean extends SortBean {

    /**
     * 联系人姓名
     */
    @Expose
    private String name;

    /**
     * 排序字母
     */
	private String sortKey;
    @Expose
    private String mobile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getSortKey() {
		return sortKey;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}

    public String getPhone() {
        return mobile;
    }

    public void setPhone(String phone) {
        this.mobile = phone;
    }

    @Override
    public String getSortString() {
        return name;
    }

}
