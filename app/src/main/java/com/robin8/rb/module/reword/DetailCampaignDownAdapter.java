package com.robin8.rb.module.reword;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.activity.web.WebViewActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.helper.IconFontHelper;
import com.robin8.rb.module.reword.bean.CampaignMaterialsModel;
import com.robin8.rb.util.CustomToast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by IBM on 2016/8/19.
 */
public class DetailCampaignDownAdapter extends BaseAdapter {

    private static final String URL_TYPE_ARTICLE = "article";
    private static final String URL_TYPE_IMAGE = "image";
    private static final String URL_TYPE_VIDEO = "video";
    private static final String URL_TYPE_FILE = "file";
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_NORMAL = 1;
    private List<CampaignMaterialsModel.CampaignMaterialsBean> mDataList = new ArrayList<>();

    @Override
    public int getCount() {
        return mDataList != null ? mDataList.size() : 0;
    }

    @Override
    public CampaignMaterialsModel.CampaignMaterialsBean getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CampaignMaterialsModel.CampaignMaterialsBean item = getItem(position);
        if (getItemViewType(position) == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_list_item_footer, null);
            return view;
        } else {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_list_item_normal, null);
                holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                holder.tv_arrow = (TextView) convertView.findViewById(R.id.tv_arrow);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            setItemView(item, holder);
            convertView.setLongClickable(true);
            convertView.setOnLongClickListener(new MyLongClickListener(item));
            convertView.setOnClickListener(new MyOnClickListener(item, holder));
            return convertView;
        }
    }

    private void setItemView(CampaignMaterialsModel.CampaignMaterialsBean item, ViewHolder holder) {
        if (item == null || holder == null) {
            return;
        }
        String urlType = item.getUrl_type();
        switch (urlType) {
            case URL_TYPE_ARTICLE:
                holder.iv_icon.setBackgroundResource(R.mipmap.icon_task_link);
                break;
            case URL_TYPE_IMAGE:
                holder.iv_icon.setBackgroundResource(R.mipmap.icon_task_pic);
                break;
            case URL_TYPE_VIDEO:
                holder.iv_icon.setBackgroundResource(R.mipmap.icon_task_video);
                break;
            case URL_TYPE_FILE:
                holder.iv_icon.setBackgroundResource(R.mipmap.icon_task_article);
                break;
        }
        holder.tv_content.setText(item.getUrl());
        IconFontHelper.setTextIconFont(holder.tv_arrow, R.string.arrow_right);
    }

    public void setData(List<CampaignMaterialsModel.CampaignMaterialsBean> list) {
        mDataList.clear();
        mDataList.addAll(list);
    }

    private class ViewHolder {
        public ImageView iv_icon;
        public TextView tv_content;
        public TextView tv_arrow;
    }

    private class MyLongClickListener implements View.OnLongClickListener {

        private  CampaignMaterialsModel.CampaignMaterialsBean item;

        public MyLongClickListener(CampaignMaterialsModel.CampaignMaterialsBean item) {
            this.item = item;
        }

        @Override
        public boolean onLongClick(View v) {
            copyToClipboard(item.getUrl());
            return true;
        }
    }

    /**
     * 拷贝到剪贴板
     */
    public void copyToClipboard(String str) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                ClipboardManager clipboardManager = (ClipboardManager) BaseApplication.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("simple text", str);
                clipboardManager.setPrimaryClip(clip);
                CustomToast.showShort(BaseApplication.getContext(), "复制成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyOnClickListener implements View.OnClickListener {

        private CampaignMaterialsModel.CampaignMaterialsBean item;
        private ViewHolder holder;

        public MyOnClickListener(CampaignMaterialsModel.CampaignMaterialsBean item, ViewHolder holder) {
            this.item = item;
            this.holder = holder;
        }

        @Override
        public void onClick(View v) {
            Context context = holder.iv_icon.getContext();

            String url = item.getUrl();
            Intent intent = null;

            intent = new Intent(context, WebViewActivity.class);
            intent.putExtra("title", item.getUrl_type());
            intent.putExtra("url", item.getUrl());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
