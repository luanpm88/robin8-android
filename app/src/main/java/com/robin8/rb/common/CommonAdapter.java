package com.robin8.rb.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @param <T>
 * @author DLJ
 * @Description 公共adapter 继承baseadapter 使用方法 继承 该类 重写convert
 * @date 2015年6月17日 下午12:36:38
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mDatas;
    protected final int mItemLayoutId;

    public void setmDatas(List<T> mDatas) {
        this.mDatas.clear();//清空集合
        if (this.mDatas == mDatas)
            this.notifyDataSetChanged();
        else {
            this.mDatas = mDatas;
            this.notifyDataSetInvalidated();
        }
    }

    public List<T> getmDatas() {
        return mDatas;
    }

    public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() {
        if (mDatas == null)
            return 0;
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = getViewHolder(position, convertView, parent);
        convert(viewHolder, getItem(position), position);
        return viewHolder.getConvertView();

    }

    /**
     * 要position 通过helper.getPosition();
     *
     * @param helper 获取view 通过helper.getView(int id)
     * @param item   数据item
     */
    public abstract void convert(ViewHolder helper, T item, int position);

    private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
    }

}
