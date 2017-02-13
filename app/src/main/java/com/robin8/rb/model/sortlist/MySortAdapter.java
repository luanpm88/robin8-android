package com.robin8.rb.model.sortlist;


import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.common.CommonAdapter;
import com.robin8.rb.common.ViewHolder;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * 自动排序的adapter
 * @param <T>
 */
public abstract class MySortAdapter<T extends SortBean> extends CommonAdapter<T> {
	private CharacterParser characterParser;// 汉字转拼音
	private Comparator pinyinComparator;

	/**
	 * 
	 * @param context
	 * @param mDatas
	 * @param itemLayoutId
	 */
	public MySortAdapter(Context context, List<T> mDatas, int itemLayoutId) {
		super(context, mDatas, itemLayoutId);

		characterParser = CharacterParser.getInstance();
		pinyinComparator = new SortBean.PinyinComparator();

		sortByField();
	}

	@Override
	public void notifyDataSetChanged() {
		sortByField();
		super.notifyDataSetChanged();
	}

	@Override
	public void notifyDataSetInvalidated() {
		sortByField();
		super.notifyDataSetInvalidated();
	}

	protected void sortByField() {
		if (mDatas != null) {
			for (T u : mDatas) {// 汉字转拼音处理
				// 汉字转换成拼音
				String pinyin;
				try {
					pinyin = characterParser.getSelling((String) u.getSortString());
					String sortString = pinyin.substring(0, 1).toUpperCase();
					// 正则表达式，判断首字母是否是英文字母
					if (sortString.matches("[A-Z]")) {
						u.setSortLetters(sortString.toUpperCase());
					} else {
						u.setSortLetters("#");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			// 根据a-z进行排序源数据
			Collections.sort(mDatas, pinyinComparator);
		}
	}

	@Override
	public void convert(ViewHolder helper, T item, int position) {
		LinearLayout LL_item = helper.getView(R.id.LL_item);
//		Drawable drawable = MyApp.getMg().getApplicationContext().getResources()
//				.getDrawable(R.drawable.bg_listview);
//		LL_item.setBackgroundDrawable(drawable);
		TextView catalog = helper.getView(R.id.catalog);
		if (catalog != null) {
			// 根据position获取分类的首字母的Char ascii值
			int section = getSectionForPosition(position);
			// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
			if (position == getPositionForSection(section)) {
				catalog.setVisibility(View.VISIBLE);
				catalog.setText(item.getSortLetters());
			} else {
				catalog.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 根据position获取分组的索引值(首字母) 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return mDatas.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据索引值(首字母) 获取分组的第一个position 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = mDatas.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

}
