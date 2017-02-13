package com.robin8.rb.ui.widget.citylist;

import java.io.Serializable;

public interface ContactItemInterface extends Serializable {
	// 根据该字段来排序
	public String getItemForIndex();

	// 该字段用来显示出来
	public String getDisplayInfo();

	// 该字段用来判断是否选中
	public boolean isSelected();

    public void setSelected(boolean isSelected);
}
