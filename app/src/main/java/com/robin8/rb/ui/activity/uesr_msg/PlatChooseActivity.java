package com.robin8.rb.ui.activity.uesr_msg;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.ui.model.UserCircleBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.OtherGridView;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.ListUtils;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.ui.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 平台选择
 */
public class PlatChooseActivity extends BaseActivity {

    @Bind(R.id.gv_social)
    OtherGridView gvSocial;
    private MyGridBaseAdapter mMyGridBaseAdapter;
    public static final String PLAT_CHOOSE = "PlatChooseActivity";
    private int mKolId;
    private List<UserCircleBean.TerracesListBean> platList;
    private List<MyPlatBean> platGridList;
    private ArrayList<Integer> platIdList;
    private ArrayList<Integer> listChosed;
    private boolean is = false;

    @Override
    public void setTitleView() {
        mTVCenter.setText(R.string.robin075);
        mTvSave.setVisibility(View.VISIBLE);
        mTvSave.setOnClickListener(this);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_plat_choose, mLLContent, true);
        ButterKnife.bind(this);
        listChosed = getIntent().getIntegerArrayListExtra(PLAT_CHOOSE);
        if (listChosed != null) {
            if (listChosed.size() != 0) {
                is = true;
            }
        }

        platIdList = new ArrayList<>();
        platGridList = new ArrayList<>();
        initData();
    }

    private WProgressDialog mWProgressDialog;

    private void initData() {
        String initInfo = CacheUtils.getString(PlatChooseActivity.this, HelpTools.BASEINFO, null);
        if (TextUtils.isEmpty(initInfo)) {
            if (mWProgressDialog == null) {
                mWProgressDialog = WProgressDialog.createDialog(this);
            }
            mWProgressDialog.show();
            final BasePresenter mBasePresenter = new BasePresenter();
            mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.UPDATE_BASE_INFOS_URL), null, new RequestCallback() {


                @Override
                public void onError(Exception e) {
                    if (mWProgressDialog != null) {
                        mWProgressDialog.dismiss();
                    }
                }

                @Override
                public void onResponse(String response) {
                    if (mWProgressDialog != null) {
                        mWProgressDialog.dismiss();
                    }
                    LogUtil.LogShitou("数据", "==>" + response);
                    UserCircleBean bean = GsonTools.jsonToBean(response, UserCircleBean.class);
                    if (bean != null) {
                        if (bean.getError() == 0) {
                            CacheUtils.putString(PlatChooseActivity.this, HelpTools.BASEINFO, response);
                            platList = bean.getTerraces_list();
                        }
                    }
                }
            });
        } else {
            UserCircleBean bean = GsonTools.jsonToBean(initInfo, UserCircleBean.class);
            if (bean != null) {
                if (bean.getError() == 0) {
                    CacheUtils.putString(PlatChooseActivity.this, HelpTools.BASEINFO, initInfo);
                    platList = bean.getTerraces_list();
                }
            }
        }

        if (platList != null) {
            for (int i = 0; i < platList.size(); i++) {
                MyPlatBean myPlatBean = new MyPlatBean();
                myPlatBean.setId(platList.get(i).getId());
                myPlatBean.setAddress(platList.get(i).getAddress());
                myPlatBean.setName(platList.get(i).getName());
                int index = ListUtils.getIndexNew(platList.get(i).getId(), listChosed);
                if (index != - 1) {
                    myPlatBean.setChosed(true);
                } else {
                    myPlatBean.setChosed(false);
                }
                platGridList.add(myPlatBean);
            }
            mMyGridBaseAdapter = new MyGridBaseAdapter();
            gvSocial.setAdapter(mMyGridBaseAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_save:
                //plat选择完成
                for (int i = 0; i <platGridList.size(); i++) {
                    if (platGridList.get(i).isChosed==true){
                        platIdList.add(platGridList.get(i).getId());
                    }
                }
                if (platIdList.size()==0){
                    CustomToast.showShort(this, R.string.robin428);
                }else {
                    Intent intent = new Intent(this, CreatorMsgActivity.class);
                    intent.putIntegerArrayListExtra(PLAT_CHOOSE, platIdList);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    class MyPlatBean {
        private int id;
        private String name;
        private String address;
        private boolean isChosed;

        public boolean isChosed() {
            return isChosed;
        }

        public void setChosed(boolean chosed) {
            isChosed = chosed;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    class MyGridBaseAdapter extends BaseAdapter {



        @Override
        public int getCount() {
            return platGridList.size();
        }

        @Override
        public MyPlatBean getItem(int position) {
            return platGridList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final MyPlatBean item = getItem(position);
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(PlatChooseActivity.this).inflate(R.layout.item_user_choose_plat, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.imgDelete = (ImageView) convertView.findViewById(R.id.img_choose);
                holder.imgChoose = (ImageView) convertView.findViewById(R.id.img_choose_yes);
                holder.civImage = (CircleImageView) convertView.findViewById(R.id.civ_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (item == null) {
                return convertView;
            }
            holder.tvName.setText(item.getName());
            BitmapUtil.loadImage(PlatChooseActivity.this, item.getAddress(), holder.civImage);
            if (item.isChosed == true) {
                holder.imgDelete.setVisibility(View.GONE);
                holder.imgChoose.setVisibility(View.VISIBLE);
            } else {
                holder.imgDelete.setVisibility(View.VISIBLE);
                holder.imgChoose.setVisibility(View.GONE);
            }
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (holder.imgChoose.getVisibility() == View.VISIBLE) {
                        holder.imgDelete.setVisibility(View.VISIBLE);
                        holder.imgChoose.setVisibility(View.GONE);
                        item.setChosed(false);
                    } else {
                        holder.imgDelete.setVisibility(View.GONE);
                        holder.imgChoose.setVisibility(View.VISIBLE);
                        item.setChosed(true);
                    }
                }
            });
            return convertView;
        }
    }


    private class ViewHolder {
        public TextView tvName;
        public ImageView imgDelete;
        public ImageView imgChoose;
        public CircleImageView civImage;
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }
}
