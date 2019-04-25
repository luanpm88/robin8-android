package com.robin8.rb.ui.module.reword.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.ui.model.CampaignInviteBean;
import com.robin8.rb.ui.module.first.activity.KolDetailContentActivity;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.ui.widget.CircleImageView;

import java.io.Serializable;
import java.util.List;


/**
 * Created by IBM on 2016/8/21.
 */
public class PostInviteesActivity extends BaseActivity {
    GridView gvKols;
    private List mDataList;

    @Override
    public void setTitleView() {
        mTVCenter.setText(R.string.invitees_list);
    }

    @Override
    public void initView() {
        mLLContent.setBackgroundResource(R.mipmap.mine_bg);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_invitees, mLLContent);
        gvKols = (GridView) view.findViewById(R.id.gv_kols);
        Intent intent = getIntent();
        Serializable serializable = intent.getSerializableExtra("invitees");
        if (serializable instanceof List) {
            mDataList = (List) serializable;
        }
        KolsAdapter adadpter = new KolsAdapter();
        gvKols.setAdapter(adadpter);
    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.CAMPAIGN_DETAIL_KOLS_LIST;
        super.onResume();
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }

    private class KolsAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDataList == null ? 0 : mDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Object obj = getItem(position);
            CampaignInviteBean.InviteesBean bean = null;
            if(obj instanceof CampaignInviteBean.InviteesBean){
                bean = (CampaignInviteBean.InviteesBean) obj;
            }else {
                return convertView;
            }

            ViewHolder holder;
            if(convertView == null){
                convertView = LayoutInflater.from(PostInviteesActivity.this).inflate(R.layout.kols_list_item, null);
                holder = new ViewHolder();
                holder.civImage = (CircleImageView) convertView.findViewById(R.id.civ_image);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            }else {
                 holder = (ViewHolder) convertView.getTag();
            }
            BitmapUtil.loadImage(PostInviteesActivity.this, bean.getAvatar_url(), holder.civImage);
            holder.tvName.setText(bean.getName());
            //LogUtil.LogShitou("人员列表","===>"+bean.getId());
           // LogUtil.LogShitou("人员列表","===>"+bean.getName())
            convertView.setOnClickListener(new MyOnClickListener(bean.getId()));
            return convertView;
        }
    }

    private class ViewHolder {
        public CircleImageView civImage;
        public TextView tvName;
    }

    private class MyOnClickListener implements View.OnClickListener {
        private int id;

        public MyOnClickListener(int id) {
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PostInviteesActivity.this, KolDetailContentActivity.class);
            intent.putExtra("id", id);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
