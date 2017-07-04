package com.robin8.rb.module.mine.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.helper.IconFontHelper;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.model.BaseBean;
import com.robin8.rb.module.first.model.KolDetailModel;
import com.robin8.rb.module.first.model.SocialAccountsBean;
import com.robin8.rb.module.mine.presenter.BindSocialPresenter;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.ListUtils;
import com.robin8.rb.util.LogUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 成为KOL
 */
public class BeKolSecondActivity extends BaseActivity {

    private static final int TYPE_HEADER = 0;//简介
    private static final int TYPE_GRID = 1;//社交账号信息
    private static final int TYPE_NORMAL = 2;//简介

    private final String BACKSLASH = "/";
    @Bind(R.id.lv_list)
    ListView lvList;
    private int id;
    private List<FirstBeKolItem> mDataList = new ArrayList<>();//ListView 数据
    private List<SecondBeKolGridItem> mGridDataList = new ArrayList<>();//GridView 数据
    private MyListAdapter mMyListAdapter;
    private int[] mResId = {R.drawable.social_weixin, R.drawable.social_qq, R.drawable.social_weibo, R.drawable.social_gongzhonghao,
            R.drawable.social_meipai, R.drawable.social_miaopai, R.drawable.social_zhihu, R.drawable.social_douyu,
            R.drawable.social_yingke, R.drawable.social_tieba, R.drawable.social_tianya, R.drawable.social_taobao,
            R.drawable.social_huajiao, R.drawable.social_nice, R.drawable.social_douban, R.drawable.social_xiaohongshu,
            R.drawable.social_yizhibo, R.drawable.social_meila, R.drawable.social_others};
    private String[] mStrArr;
    private MyGridBaseAdapter mMyGridBaseAdapter;
    private String mBackName;
    private int mBackId;
    private List<SocialAccountsBean> mSocialAccounts;
    private List<KolDetailModel.KolShowsBean> mKolShows;
    private WProgressDialog mWProgressDialog;
   // private SocialAccountsBean mSocialAccountsBean;
    private String userNames;
    private String socialName;
    @Override
    public void setTitleView() {
        mTVCenter.setText(this.getText(R.string.bind_social_account));
    }

    @Override
    public void initView() {
        mBottomTv.setVisibility(View.GONE);
        mBottomTv.setText(getString(R.string.submit));
        View view = LayoutInflater.from(this).inflate(R.layout.activity_be_kol_first, mLLContent, true);
        ButterKnife.bind(this);
        initData();
        mBottomTv.setOnClickListener(this);
        mMyListAdapter = new MyListAdapter();
        lvList.setAdapter(mMyListAdapter);
    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.KOL_APPLY_SOCIAL;
        super.onResume();
    }

    private void initData() {
        mStrArr = getResources().getStringArray(R.array.second_be_kol_item_name);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        Serializable obj = intent.getSerializableExtra("social_accounts");
        Serializable obj2 = intent.getSerializableExtra("kol_shows");

        if (obj instanceof List) {
            mSocialAccounts = (List<SocialAccountsBean>) obj;
        }

        if (obj2 instanceof List) {
            mKolShows = (List<KolDetailModel.KolShowsBean>) obj2;
        }
        updateData();
    }

    @Override
    public void finish() {
        NotifyManager.getNotifyManager().notifyChange(NotifyManager.TYPE_REFRESH_PROFILE);
        super.finish();
    }

    /**
     * Adapter数据刷新
     */
    private void updateData() {

        mDataList.clear();
        for (int i = 0; i < 3; i++) {
            FirstBeKolItem item;
            if (i == 2) {
               item = new FirstBeKolItem(getString(R.string.personal_show));
                if (mKolShows == null) {
                    item.content = 0;
                } else {
                    item.content = mKolShows.size();
                }
            } else {
                item = null;
            }
            mDataList.add(item);
        }

        mGridDataList.clear();
        for (int i = 0; i < mStrArr.length; i++) {
            SecondBeKolGridItem item = new SecondBeKolGridItem();
            item.id = i;
            item.name = mStrArr[i];
            item.resId = mResId[i];

            int index = ListUtils.getIndex(mSocialAccounts, item.name);
            if (index != -1) {
                item.isChecked = true;
                item.socialAccountsBean = mSocialAccounts.get(index);
            } else {
                item.isChecked = false;
            }
            mGridDataList.add(item);
        }
    }

    private void postData() {
//        if (!mGridDataList.get(0).isChecked) {
//            CustomToast.showShort(this,getString(R.string.must_bind_weixin));
//            return;
//        }

        BasePresenter mBasePresenter = new BasePresenter();

        if(mWProgressDialog == null){
            mWProgressDialog =  WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();

        String url = HelpTools.getUrl(CommonConfig.SUBMIT_APPLY_URL);
        mBasePresenter.getDataFromServer(true, HttpRequest.POST, url, null, new RequestCallback() {
            @Override
            public void onError(Exception e) {
                if(mWProgressDialog!=null){
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
                if(mWProgressDialog!=null){
                    mWProgressDialog.dismiss();
                }
                BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);
                if (bean == null) {
                    CustomToast.showShort(BeKolSecondActivity.this, getString(R.string.please_data_wrong));
                    return;
                }

                if (bean.getError() == 0) {
                    skipToNext();
                } else {
                    CustomToast.showShort(BeKolSecondActivity.this, bean.getDetail());
                }
            }
        });
    }

    private void skipToNext() {
        NotifyManager.getNotifyManager().notifyChange(NotifyManager.TYPE_REFRESH_PROFILE);
//        Intent intent = new Intent(this, BeKolThirdActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
        Intent intent = getIntent();
        setResult(SPConstants.BE_KOL_BIND_RESULT, intent);
        finish();
        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * 绑定微信
     * @param name
     * @param id
     */
    private void skipToDetail(String name, int id) {
        bind(name,id);
       // judgeConditions(name);
       // mSocialAccountsBean =  mGridDataList.get(id).socialAccountsBean;
       /* NotifyManager.getNotifyManager().notifyChange(NotifyManager.TYPE_REFRESH_PROFILE);
        Intent intent = new Intent(this, BeKolSecondDetailActivity.class);
        mBackName = name;
        mBackId = id;
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        if (id >= 0 && id < mGridDataList.size()) {
            intent.putExtra("socialAccountsBean", mGridDataList.get(id).socialAccountsBean);
        }
        startActivityForResult(intent, SPConstants.BE_KOL_SECOND);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/

    }

    /**
     * wechat/qq/weibo直接绑定
     * @param names
     */
    private void bind(final String names , final int ids) {
        BindSocialPresenter presenter = new BindSocialPresenter(this.getApplicationContext(), null, names);
        presenter.setOnBindListener(new BindSocialPresenter.OnBindListener() {
            @Override
            public void onResponse(String name) {
                if (null!=name){
                    CustomToast.showShort(BeKolSecondActivity.this,"已成功绑定"+names);
                    userNames = name;
                    socialName = names;
                    mypostData(names,name);
                    mGridDataList.get(ids).isChecked = true;
                    mMyGridBaseAdapter.notifyDataSetChanged();
                }else {
                    CustomToast.showShort(BeKolSecondActivity.this,"绑定失败，请重试");
                }
            }
        });
        if (getString(R.string.weixin).equals(names)) {
            presenter.authorize(new Wechat(this));
        } else if (getString(R.string.qq).equals(names)) {
            presenter.authorize(new QQ(this));
        } else if (getString(R.string.weibo).equals(names)) {
            presenter.authorize(new SinaWeibo(this));
        }else {
            NotifyManager.getNotifyManager().notifyChange(NotifyManager.TYPE_REFRESH_PROFILE);
            Intent intent = new Intent(this, BeKolSecondDetailActivity.class);
            mBackName = names;
            mBackId = ids;
            intent.putExtra("id", ids);
            intent.putExtra("name", names);
            if (ids >= 0 && ids< mGridDataList.size()) {
                intent.putExtra("socialAccountsBean", mGridDataList.get(ids).socialAccountsBean);
            }
            startActivityForResult(intent, SPConstants.BE_KOL_SECOND);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
    private void mypostData(String name,String userName) {
        String url;
        BasePresenter mBasePresenter = new BasePresenter();
        RequestParams params = new RequestParams();
        params.put("provider_name", name);
        params.put("price","1");
        params.put("followers_count","1");
        params.put("username", userName);
        LogUtil.LogShitou("绑定qq的报价之类的",name+"//"+userName);
        url = HelpTools.getUrl(CommonConfig.UPDATE_SOCIAL_URL);

        mBasePresenter.getDataFromServer(true, HttpRequest.POST, url, params, new RequestCallback() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
                LogUtil.LogShitou("提交接口","OK"+response);
                BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);

                if (bean == null) {
                    CustomToast.showShort(BeKolSecondActivity.this, getString(R.string.please_data_wrong));
                    return;
                }

                if (bean.getError() == 0) {
                    LogUtil.LogShitou("走到这里没有","直接绑定qq");
                    postData();
                    // setResult(SPConstants.BE_KOL_SECOND_PERSONAL_SHOW, intent);
                    //finish();
                } else {
                    CustomToast.showShort(BeKolSecondActivity.this, bean.getDetail());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == SPConstants.BE_KOL_SECOND_ITEM_SOCIAL && data != null) {
            mGridDataList.get(mBackId).isChecked = true;
            mMyGridBaseAdapter.notifyDataSetChanged();
        } else if (resultCode == SPConstants.BE_KOL_SECOND_PERSONAL_SHOW && data != null) {
            int counts = data.getIntExtra("counts", 0);
            mDataList.get(2).content = counts;
            mMyListAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
    }

    @Override
    public void onClick(View v) {
        if(isDoubleClick()){
            return;
        }
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_bottom:
              // mypostData(socialName,userNames);
                postData();
                break;
        }
    }

    class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public FirstBeKolItem getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           FirstBeKolItem item = getItem(position);
            switch (getItemViewType(position)) {
                case TYPE_HEADER:
                    convertView = LayoutInflater.from(BeKolSecondActivity.this).inflate(R.layout.item_be_kol_header, null);
                    LinearLayout ll_photo = (LinearLayout) convertView.findViewById(R.id.ll_photo);
                    ll_photo.setVisibility(View.GONE);
//                                       final View viewHeader = convertView.findViewById(R.id.view_header);
//                    viewHeader.setBackgroundResource(R.mipmap.pic_kol_step_1);
//                    viewHeader.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            viewHeader.getLayoutParams().height = DensityUtils.getScreenWidth(viewHeader.getContext()) * 58 / 700;
//                        }
//                    });
                    break;
                case TYPE_NORMAL:
                    if (item != null) {
                        convertView = LayoutInflater.from(BeKolSecondActivity.this).inflate(R.layout.item_be_kol_normal, null);
                        LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.ll);
                        ll.setVisibility(View.GONE);
                        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
                        TextView tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                        TextView tvArrow = (TextView) convertView.findViewById(R.id.tv_arrow);
                        View viewDivider = convertView.findViewById(R.id.view_divider);
                        viewDivider.setVisibility(View.GONE);
                        tvName.setText(item.name);
                        tvContent.setText(String.valueOf(item.content));
                        IconFontHelper.setTextIconFont(tvArrow, R.string.arrow_right);
                    }
                    break;
                case TYPE_GRID:
                    convertView = LayoutInflater.from(BeKolSecondActivity.this).inflate(R.layout.item_be_kol_grid, null);
                    GridView gvSocial = (GridView) convertView.findViewById(R.id.gv_social);
                    mMyGridBaseAdapter = new MyGridBaseAdapter();
                    gvSocial.setAdapter(mMyGridBaseAdapter);
                    break;
            }

            if (position == 2) {
                convertView.setOnClickListener(new MyOnClickListener(item.name, -1));
            }
            return convertView;
        }
    }

    private class FirstBeKolItem {
        public String name;
        public int content = 0;

        public FirstBeKolItem(String name) {
            this.name = name;
        }
    }

    class MyGridBaseAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mGridDataList.size();
        }

        @Override
        public SecondBeKolGridItem getItem(int position) {
            return mGridDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SecondBeKolGridItem item = getItem(position);
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(BeKolSecondActivity.this).inflate(R.layout.item_second_be_kol_grid, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (item == null) {
                return convertView;
            }

            convertView.setOnClickListener(new MyOnClickListener(item.name, item.id));
            holder.tvName.setText(item.name);
            holder.tvName.setSelected(item.isChecked);
            holder.tvName.setCompoundDrawablesWithIntrinsicBounds(null, BeKolSecondActivity.this.getResources().getDrawable(item.resId), null, null);
            return convertView;
        }
    }

    private class SecondBeKolGridItem {
        public int id;
        public String name;
        public int resId;
        public boolean isChecked;
        public SocialAccountsBean socialAccountsBean;
    }

    private class ViewHolder {
        public TextView tvName;
    }

    private class MyOnClickListener implements View.OnClickListener {
        private String name;
        private int id;

        public MyOnClickListener(String name, int id) {
            this.name = name;
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            skipToDetail(name, id);
        }
    }
//    private void bind(final String names) {
//        BindSocialPresenter presenter = new BindSocialPresenter(this.getApplicationContext(),null, names);
//        presenter.setOnBindListener(new BindSocialPresenter.OnBindListener() {
//            @Override
//            public void onResponse(String name) {
//                CustomToast.showLong(BeKolSecondActivity.this,"==bind==>"+name);
//                if (names==name){
//                    BasePresenter mBasePresenter = new BasePresenter();
//                    RequestParams params = new RequestParams();
//                        params.put("provider_name", name);
//                        params.put("price", "1");
//
//                    params.put("followers_count", "10");
//                    params.put("username", name);
//                    mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.UPDATE_SOCIAL_URL), params, new RequestCallback() {
//                        @Override
//                        public void onError(Exception e) {
//
//                        }
//
//                        @Override
//                        public void onResponse(String response) {
//                            BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);
//
//                            if (bean == null) {
//                                CustomToast.showShort(BeKolSecondActivity.this, getString(R.string.please_data_wrong));
//                                return;
//                            }
//
//                            if (bean.getError() == 0) {
//                                mGridDataList.get(mBackId).isChecked = true;
//                                mMyGridBaseAdapter.notifyDataSetChanged();
//                            } else {
//                                CustomToast.showShort(BeKolSecondActivity.this, bean.getDetail());
//                            }
//                        }
//                    });
//                }
//            }
//        });
//        if (getString(R.string.weixin).equals(names)) {
//            presenter.authorize(new Wechat(this));
//        } else if (getString(R.string.qq).equals(names)) {
//            presenter.authorize(new QQ(this));
//        } else if (getString(R.string.weibo).equals(names)) {
//            presenter.authorize(new SinaWeibo(this));
//        }
//
//    }
}
