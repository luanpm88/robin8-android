package com.robin8.rb.module.reword.chose_photo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.robin8.rb.R;
import com.robin8.rb.module.reword.activity.ScreenImgActivity;
import com.robin8.rb.ui.widget.SquareLayout;

import java.util.ArrayList;
import java.util.Map;

/**
 Created by zc on 2018/1/14. */

public class GridAdapter extends BaseAdapter {
    private ArrayList<String> mList;
    private ArrayList<String> list;
    private Context context;
    private int mItemSize;
    private LayoutInflater mInflater;
    private int chose_position = - 1;
    private static Map<Integer, String> imgMap;
    private GridView.LayoutParams mItemLayoutParams;
    private OnClearListener mOnClearListener;
    private int type;

    public void setOnClearListener(OnClearListener mOnClearListener) {
        this.mOnClearListener = mOnClearListener;
    }

    public interface OnClearListener {
        void onClick(int position);
    }

    public GridAdapter(Context context, ArrayList<String> mList, int itemSize, Map<Integer, String> imgMap,int type) {
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mList = mList;
        this.mItemSize = itemSize;
        this.imgMap = imgMap;
        this.type = type;
        mItemLayoutParams = new GridView.LayoutParams(mItemSize, mItemSize);

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setNewData(Map<Integer, String> imgMap) {
        this.imgMap = imgMap;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.screen_img_item, parent, false);
            //convertView = LayoutInflater.from(ScreenImgActivity.this).inflate(R.layout.screen_img_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.tvContent.setText(mList.get(position));//当前上传图片描述
        holder.llBottom.setVisibility(View.GONE);
        holder.imgClear.setVisibility(View.GONE);//清除所选图片

        if (imgMap.size() != 0) {
            if (ScreenImgActivity.isHaveImg(imgMap, position)) {
                holder.img.setWillNotDraw(false);
                Glide.with(context).load(imgMap.get(position)).
                        placeholder(R.mipmap.default_error).error(R.mipmap.default_error).centerCrop().crossFade().into(holder.img);
                holder.llBottom.setVisibility(View.VISIBLE);
                if (type==1 || type==2){
                    holder.imgClear.setVisibility(View.VISIBLE);
                }else if (type==0){
                    holder.imgClear.setVisibility(View.GONE);
                }
                holder.tvName.setText(mList.get(position));
            }
        }

        //删除所选择的图片
        holder.imgClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                if (mOnClearListener != null)
//                    mOnClearListener.onClick(position);
                imgMap.remove(position);
                if (position == position){
                    holder.img.setWillNotDraw(true);
                    holder.llBottom.setVisibility(View.GONE);
                    holder.imgClear.setVisibility(View.GONE);
                }
            }
        });

        return convertView;
    }

    /**
     重置每个Column的Size
     @param columnWidth
     */
    public void setItemSize(int columnWidth) {

        if (mItemSize == columnWidth) {
            return;
        }

        mItemSize = columnWidth;

        mItemLayoutParams = new GridView.LayoutParams(mItemSize, mItemSize);

        notifyDataSetChanged();
    }

    class ViewHolder {

        public final ImageView img;
        public final TextView tvContent;
        public final SquareLayout llScreen;
        public final ImageView imgClear;
        public final RelativeLayout llBottom;
        public final TextView tvName;

        private ViewHolder(View v) {
            img = ((ImageView) v.findViewById(R.id.img_chose));
            tvContent = ((TextView) v.findViewById(R.id.tv_content));
            llScreen = ((SquareLayout) v.findViewById(R.id.ll_screen));
            imgClear = ((ImageView) v.findViewById(R.id.img_clear));
            llBottom = ((RelativeLayout) v.findViewById(R.id.ll_bottom));
            tvName = ((TextView) v.findViewById(R.id.tv_name));
        }
    }
}
