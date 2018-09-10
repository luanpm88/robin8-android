package com.robin8.rb.module.mine.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
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
import com.robin8.rb.model.IndentyBean;
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
import com.robin8.rb.view.widget.CustomDialogManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 成为KOL */
public class BeKolSecondActivity extends BaseActivity {

    private static final int TYPE_HEADER = 0;//简介
    private static final int TYPE_GRID = 1;//社交账号信息
    private static final int TYPE_NORMAL = 2;//简介
    private boolean isShow = false;
    private final String BACKSLASH = "/";
    @Bind(R.id.lv_list)
    ListView lvList;
    private int id;
    private List<FirstBeKolItem> mDataList = new ArrayList<>();//ListView 数据
    private List<SecondBeKolGridItem> mGridDataList = new ArrayList<>();//GridView 数据
    private MyListAdapter mMyListAdapter;
    private int[] mResId = {R.drawable.social_weixin, R.drawable.social_qq, R.drawable.social_weibo, R.drawable.social_gongzhonghao, R.drawable.social_meipai, R.drawable.social_miaopai, R.drawable.social_zhihu, R.drawable.social_douyu, R.drawable.social_yingke, R.drawable.social_tieba, R.drawable.social_tianya, R.drawable.social_taobao, R.drawable.social_huajiao, R.drawable.social_nice, R.drawable.social_douban, R.drawable.social_xiaohongshu, R.drawable.social_yizhibo, R.drawable.social_meila, R.drawable.social_others};
    private String[] mStrArr;
    private String[] mStrProviderArr;
    private MyGridBaseAdapter mMyGridBaseAdapter;
    private String mBackName;
    private int mBackId;
    private List<SocialAccountsBean> mSocialAccounts;
    private List<KolDetailModel.KolShowsBean> mKolShows;
    private WProgressDialog mWProgressDialog;
    // private SocialAccountsBean mSocialAccountsBean;
    private String userNames;
    private String socialName;
    private int mKolId;

    private BasePresenter mBasePresenter;
    private RequestParams mRequestParams;

    @Override
    public void setTitleView() {
        mTVCenter.setText(this.getText(R.string.bind_social_account));
        mTvEdit.setVisibility(View.VISIBLE);
        mTvEdit.setOnClickListener(this);
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
        mStrProviderArr = getResources().getStringArray(R.array.second_be_kol_item_provider);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        Serializable obj = intent.getSerializableExtra("social_accounts");
        Serializable obj2 = intent.getSerializableExtra("kol_shows");
        mKolId = intent.getIntExtra("kol_id", 0);
        if (obj instanceof List) {
            mSocialAccounts = (List<SocialAccountsBean>) obj;
        }

        if (obj2 instanceof List) {
            mKolShows = (List<KolDetailModel.KolShowsBean>) obj2;
        }
        updateData();
        getDataFromNet();
        //  updateData();
        //  getKolList();
        //unbind();
    }

    /**
     获取初始信息
     */
    private void getDataFromNet() {

        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }

        if (mRequestParams == null) {
            mRequestParams = new RequestParams();
        }

        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();

        mBasePresenter.getDataFromServer(true, HttpRequest.GET, (HelpTools.getUrl(CommonConfig.FIRST_KOL_LIST_URL + BACKSLASH + String.valueOf(mKolId) + BACKSLASH + "detail")), mRequestParams, new RequestCallback() {

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
             //   LogUtil.LogShitou("社交账号数据刷新", response);
                parseJson(response);
            }
        });
    }

    private void parseJson(String json) {
        KolDetailModel kolDetailModel = GsonTools.jsonToBean(json, KolDetailModel.class);
        if (kolDetailModel != null && kolDetailModel.getError() == 0) {
            mSocialAccounts = kolDetailModel.getSocial_accounts();
            mKolShows = kolDetailModel.getKol_shows();
            updateData();
            if (mSocialAccounts == null || mSocialAccounts.size() == 0) {
                mTvEdit.setVisibility(View.GONE);
            } else {
                mTvEdit.setVisibility(View.VISIBLE);
            }
        } else {
            LogUtil.LogShitou("失败", "失败");
        }
    }


    private void getKolList() {
        BasePresenter mBasePresenter = new BasePresenter();
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.INFLUENCE_INFO_LIST), null, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
                // LogUtil.LogShitou("获取第三方账号url", HelpTools.getUrl(CommonConfig.INFLUENCE_INFO_LIST));
             //   LogUtil.LogShitou("获取第三方账号Uid", response);
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void finish() {
        NotifyManager.getNotifyManager().notifyChange(NotifyManager.TYPE_REFRESH_PROFILE);
        super.finish();
    }

    /**
     Adapter数据刷新
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
        if (mSocialAccounts == null || mSocialAccounts.size() == 0) {
            mTvEdit.setVisibility(View.GONE);
        } else {
            mTvEdit.setVisibility(View.VISIBLE);
        }
        mGridDataList.clear();
        for (int i = 0; i < mStrArr.length; i++) {
            SecondBeKolGridItem item = new SecondBeKolGridItem();
            item.id = i;
            item.name = mStrArr[i];
            item.mProvider = mStrProviderArr[i];
            item.resId = mResId[i];
            int index = ListUtils.getIndex(mSocialAccounts, item.name);
            if (index != - 1) {
                item.isChecked = true;
                item.socialAccountsBean = mSocialAccounts.get(index);
            } else {
                item.isChecked = false;
            }
            mGridDataList.add(item);
        }
    }

    private void postData() {
        BasePresenter mBasePresenter = new BasePresenter();

        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();

        String url = HelpTools.getUrl(CommonConfig.SUBMIT_APPLY_URL);
        mBasePresenter.getDataFromServer(true, HttpRequest.POST, url, null, new RequestCallback() {

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
        Intent intent = getIntent();
        setResult(SPConstants.BE_KOL_BIND_RESULT, intent);
        finish();
    }

    /**
     绑定微信
     @param name
     @param id
     */
    private void skipToDetail(String name, int id) {
        bind(name, id);
    }

    /**
     wechat/qq/weibo直接绑定
     @param names
     */
    private void bind(final String names, final int ids) {
        CustomToast.showShort(BeKolSecondActivity.this, "正在前往"+names+"中...");
        BindSocialPresenter presenter = new BindSocialPresenter(this.getApplicationContext(), null, names,0);
        presenter.setOnBindListener(new BindSocialPresenter.OnBindListener() {

            @Override
            public void onResponse(String name) {
                if (null != name) {
                    CustomToast.showShort(BeKolSecondActivity.this, "已成功绑定" + names);
                    userNames = name;
                    socialName = names;
                    mypostData(names, name);
                    mGridDataList.get(ids).isChecked = true;
                    mMyGridBaseAdapter.notifyDataSetChanged();
                } else {
                    CustomToast.showShort(BeKolSecondActivity.this, "绑定失败，请重试");
                }
            }
        });
        if (getString(R.string.weixin).equals(names)) {
            presenter.authorize(new Wechat());
        } else if (getString(R.string.qq).equals(names)) {
            presenter.authorize(new QQ());
        } else if (getString(R.string.weibo).equals(names)) {
            presenter.authorize(new SinaWeibo());
        } else {
            NotifyManager.getNotifyManager().notifyChange(NotifyManager.TYPE_REFRESH_PROFILE);
            Intent intent = new Intent(this, BeKolSecondDetailActivity.class);
            mBackName = names;
            mBackId = ids;
            intent.putExtra("id", ids);
            intent.putExtra("name", names);
            if (ids >= 0 && ids < mGridDataList.size()) {
                intent.putExtra("socialAccountsBean", mGridDataList.get(ids).socialAccountsBean);
            }
            startActivityForResult(intent, SPConstants.BE_KOL_SECOND);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    private void mypostData(String name, String userName) {
        String url;
        BasePresenter mBasePresenter = new BasePresenter();
        RequestParams params = new RequestParams();
        params.put("provider_name", name);
        params.put("price", "0.1");
        params.put("followers_count", "0.1");
        params.put("username", userName);
      //  LogUtil.LogShitou("绑定qq的报价之类的",name+"//"+userName);
        url = HelpTools.getUrl(CommonConfig.UPDATE_SOCIAL_URL);

        mBasePresenter.getDataFromServer(true, HttpRequest.POST, url, params, new RequestCallback() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
              //  LogUtil.LogShitou("提交接口",response);
                BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);

                if (bean == null) {
                    CustomToast.showShort(BeKolSecondActivity.this, getString(R.string.please_data_wrong));
                    return;
                }

                if (bean.getError() == 0) {
                    postData();
                    //  skipToNext();
                    // setResult(SPConstants.BE_KOL_SECOND_PERSONAL_SHOW, intent);
                    //finish();
                } else {
                    CustomToast.showShort(BeKolSecondActivity.this, "提交失败");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == SPConstants.BE_KOL_SECOND_ITEM_SOCIAL && data != null) {
            mGridDataList.get(mBackId).isChecked = true;
            getDataFromNet();
            mMyGridBaseAdapter.notifyDataSetChanged();
        } else if (resultCode == SPConstants.BE_KOL_SECOND_PERSONAL_SHOW && data != null) {
            int counts = data.getIntExtra("counts", 0);
            mDataList.get(2).content = counts;
           // mGridDataList.get(mBackId).isChecked = true;
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
        if (isDoubleClick()) {
            return;
        }
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_bottom:
                // mypostData(socialName,userNames);
                postData();
                break;
            case R.id.tv_edit:
                // CustomToast.showShort(BeKolSecondActivity.this, "编辑");
                if (isShow) {
                    mTvEdit.setText("编辑");
                    isShow = false;
                } else {
                    mTvEdit.setText("取消");
                    isShow = true;
                }
                mMyGridBaseAdapter.notifyDataSetChanged();
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
                convertView.setOnClickListener(new MyOnClickListener(item.name, - 1, 0));
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
        //  private AnimatorSet btnSexAnimatorSet;

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

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            SecondBeKolGridItem item = getItem(position);
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(BeKolSecondActivity.this).inflate(R.layout.item_second_be_kol_grid, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.imgDelete = (ImageView) convertView.findViewById(R.id.img_delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (item == null) {
                return convertView;
            }

            holder.tvName.setText(item.name);
            holder.tvName.setSelected(item.isChecked);
            holder.tvName.setCompoundDrawablesWithIntrinsicBounds(null, BeKolSecondActivity.this.getResources().getDrawable(item.resId), null, null);
            if (item.isChecked == true && isShow == false) {
                convertView.setOnClickListener(new MyOnClickListener(item.name, item.id, 1));
            } else if (isShow == true && item.isChecked == true) {
                try {
                    convertView.setOnClickListener(new MyOnClickListener(item.socialAccountsBean.getId(), item.socialAccountsBean.getProvider(), item.name, item.id, 2));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (isShow == false && item.isChecked == false) {
                convertView.setOnClickListener(new MyOnClickListener(item.mProvider, item.name, item.id, 0));
            }

            if (isShow) {
                if (holder.imgDelete.getVisibility() == View.VISIBLE) {
                    holder.imgDelete.setVisibility(View.GONE);
                    if (holder.btnSexAnimatorSet.isStarted()) {
                        //holder.btnSexAnimatorSet.cancel();
                        ObjectAnimator translationXAnim = ObjectAnimator.ofFloat(holder.tvName, "rotation", 0, 0, 0, 0, 0);
                        translationXAnim.setDuration(1000);
                        translationXAnim.setRepeatCount(ValueAnimator.INFINITE);
                        translationXAnim.start();
                        holder.btnSexAnimatorSet.play(translationXAnim);
                        holder.btnSexAnimatorSet.cancel();
                        holder.btnSexAnimatorSet.end();
                        // btnSexAnimatorSet=null;
                    }
                } else if (holder.imgDelete.getVisibility() == View.GONE && (item.isChecked == true)) {
                    holder.imgDelete.setVisibility(View.VISIBLE);
                    List<Animator> animators = new ArrayList<>();
                    ObjectAnimator translationXAnim = ObjectAnimator.ofFloat(holder.tvName, "rotation", 2, 8, 2, - 8, 2);
                    translationXAnim.setDuration(150);
                    translationXAnim.setRepeatCount(ValueAnimator.INFINITE);//无限循环
                    translationXAnim.setRepeatMode(ValueAnimator.INFINITE);
                    translationXAnim.start();
                    animators.add(translationXAnim);
                    ObjectAnimator translationYAnim = ObjectAnimator.ofFloat(holder.tvName, "rotation", - 2, - 8, - 2, 8, - 2);
                    translationYAnim.setDuration(150);

                    translationYAnim.setRepeatCount(ValueAnimator.INFINITE);
                    translationYAnim.setRepeatMode(ValueAnimator.INFINITE);
                    translationYAnim.start();
                    animators.add(translationYAnim);

                    holder.btnSexAnimatorSet = new AnimatorSet();//
                    holder.btnSexAnimatorSet.playTogether(animators);
                    holder.btnSexAnimatorSet.setStartDelay(1);
                    holder.btnSexAnimatorSet.start();
                }
            } else {
                holder.imgDelete.setVisibility(View.GONE);
                if (holder.btnSexAnimatorSet != null) {
                    if (holder.btnSexAnimatorSet.isStarted()) {
                        ObjectAnimator translationXAnim = ObjectAnimator.ofFloat(holder.tvName, "rotation", 0, 0, 0, 0, 0);
                        translationXAnim.setDuration(1000);
                        translationXAnim.setRepeatCount(ValueAnimator.INFINITE);//无限循环
                        translationXAnim.start();
                        holder.btnSexAnimatorSet.play(translationXAnim);
                        holder.btnSexAnimatorSet.cancel();
                        //  holder.btnSexAnimatorSet.end();
                    }
                }
            }
            return convertView;
        }
    }

    private class SecondBeKolGridItem {
        public int id;
        public String name;
        public String mProvider;
        public int resId;
        public boolean isChecked;
        public SocialAccountsBean socialAccountsBean;
    }

    private class ViewHolder {
        public TextView tvName;
        public ImageView imgDelete;
        private AnimatorSet btnSexAnimatorSet;
    }

    private class MyOnClickListener implements View.OnClickListener {
        private String providerName;
        private String name;
        private int id;
        private int uid;
        private int type;

        public MyOnClickListener(int uid, String providerName, String name, int id, int type) {
            this.uid = uid;
            this.providerName = providerName;
            this.name = name;
            this.id = id;
            this.type = type;
        }

        public MyOnClickListener(String providerName, String name, int id, int type) {
            this.providerName = providerName;
            this.name = name;
            this.id = id;
            this.type = type;
        }

        public MyOnClickListener(String name, int id, int type) {

            this.name = name;
            this.id = id;
            this.type = type;
        }

        @Override
        public void onClick(View v) {

            if (type == 0) {
                //未绑定
                isShow = false;
                mMyGridBaseAdapter.notifyDataSetChanged();
                skipToDetail(name, id);
            } else if (type == 1) {
                //已绑定
                CustomToast.showShort(BeKolSecondActivity.this, "已绑定" + name);
            } else if (type == 2) {
                //需要解绑
                //查询解绑次数
                getBindCount(type, providerName, name, uid);
            }

        }


    }

    /**
     是否解绑社交账号
     @param activity
     @param uid type = 0  id；type=2  uid
     @param providerName wechat／type=0 来源本地，type=2来源于接口
     @param title 对话框标题
     @param info 对话框内容
     @param type 当前界面状态是解绑还是绑定
     @param names 中文name ：微信
     */
    private void showMyDialog(final Activity activity, final int uid, final String providerName, String title, String info, final int type, final String names, final boolean is) {
        // String rejectReason = campaignInviteEntity.getReject_reason();
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_reject_screenshot, null);
        TextView confirmTV = (TextView) view.findViewById(R.id.tv_confirm);
        TextView tvTop = (TextView) view.findViewById(R.id.tv_top);
        TextView infoTv = (TextView) view.findViewById(R.id.tv_info);
        TextView rightTv = (TextView) view.findViewById(R.id.tv_right);
        LinearLayout layoutLeft = (LinearLayout) view.findViewById(R.id.layout_left);
        LinearLayout layoutRight = (LinearLayout) view.findViewById(R.id.layout_right);
        tvTop.setText(title);
        if (is) {
            confirmTV.setText("确定");
            rightTv.setText("取消");
            infoTv.setText(info);
        } else {
            layoutRight.setVisibility(View.GONE);
            infoTv.setVisibility(View.GONE);
            confirmTV.setText("确定");
            layoutLeft.setBackgroundResource(R.drawable.shape_corner_bg_bottom);
        }

        final CustomDialogManager cdm = new CustomDialogManager(activity, view);
        layoutLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cdm.dismiss();
                if (is) {
                    if (type == 0) {
                        skipToDetail(names, uid);
                    } else if (type == 2) {
                        unBind(uid, providerName);
                    }
                }


            }
        });
        layoutRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cdm.dismiss();
            }
        });
        cdm.dg.setCanceledOnTouchOutside(false);
        cdm.dg.getWindow().setGravity(Gravity.CENTER);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        cdm.showDialog();
    }

    private void getBindCount(final int type, final String provider, final String name, final int ids) {
        String myBindUrl = "";
        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }

        if (mRequestParams == null) {
            mRequestParams  = new RequestParams();
        }
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();

        mRequestParams.put("kol_id", mKolId);
        mRequestParams.put("provider", provider);
        if (type == 0) {
            myBindUrl = HelpTools.getUrl(CommonConfig.SOCIAL_BIND_COUNT);
        } else if (type == 2) {
            myBindUrl = HelpTools.getUrl(CommonConfig.SOCIAL_UNBIND_COUNT);
        }

        mBasePresenter.getDataFromServer(true, HttpRequest.PUT, myBindUrl, mRequestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
              //  LogUtil.LogShitou("绑次数查询", response);
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);
                if (baseBean.getError() == 0) {
                    if (! TextUtils.isEmpty(baseBean.getDetail())) {
                        if (type == 0) {
                            //未绑定
                            showMyDialog(BeKolSecondActivity.this, ids, provider, "确定绑定当前" + name + "？", baseBean.getDetail(), 0, name, true);
                        } else if (type == 2) {
                            showMyDialog(BeKolSecondActivity.this, ids, provider, "确定解绑" + name + "？", baseBean.getDetail(), 2, name, true);
                        }

                    }
                } else if (baseBean.getError() == 1) {
                    if (! TextUtils.isEmpty(baseBean.getDetail())) {
                        if (type == 0) {
                            //未绑定
                            showMyDialog(BeKolSecondActivity.this, ids, provider, baseBean.getDetail(), null, 0, name, false);
                        } else if (type == 2) {
                            showMyDialog(BeKolSecondActivity.this, ids, provider, baseBean.getDetail(), null, 2, name, false);
                        }

                    }
                }else {
                    try {
                        CustomToast.showShort(BeKolSecondActivity.this, "查询失败");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    private void unBind(final int uid, final String providerName) {
        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }

        if (mRequestParams == null) {
            mRequestParams = new RequestParams();
        }
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();

        if (providerName.equals("wechat") || providerName.equals("weibo") || providerName.equals("qq")) {
            //获取uid
            mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.INFLUENCE_INFO_LIST), null, new RequestCallback() {

                @Override
                public void onError(Exception e) {
                    if (mWProgressDialog != null) {
                        mWProgressDialog.dismiss();
                    }
                }

                @Override
                public void onResponse(String response) {
                    try {
                        if (mWProgressDialog != null) {
                            mWProgressDialog.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final ArrayList<String> listWechat = new ArrayList<>();
                    IndentyBean indentyBean = GsonTools.jsonToBean(response, IndentyBean.class);
                    if (indentyBean != null) {
                        if (indentyBean.getError() == 0) {
                            if (indentyBean.getIdentities() != null) {

                                if (indentyBean.getIdentities().size()==0){
                                    socalUnbind(uid, providerName);
                                }else {
                                    for (int i = 0; i < indentyBean.getIdentities().size(); i++) {
                                        if ((indentyBean.getIdentities().get(i).getProvider()).equals(providerName)) {
                                            listWechat.add(indentyBean.getIdentities().get(i).getUid());
                                        }
                                    }
                                    if (listWechat.size()!=0){
                                        unbindIndenty(listWechat.get(0), uid, providerName);
                                    }else {
                                        socalUnbind(uid, providerName);
                                    }
                                }
                            }else {
                                socalUnbind(uid, providerName);
                            }

                        } else {
                            try {
                                CustomToast.showShort(BeKolSecondActivity.this, indentyBean.getDetail());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }else {
                        socalUnbind(uid, providerName);
                    }

                }
            });
        } else {
            socalUnbind(uid, providerName);
        }
    }

    private void socalUnbind(int uid, String providerName) {
        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }

        if (mRequestParams == null) {
            mRequestParams = new RequestParams();
        }
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();
        mRequestParams.put("kol_id", mKolId);
        mRequestParams.put("provider", providerName);
        mRequestParams.put("id", uid);

        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.SOCIAL_UNBIND), mRequestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
               // LogUtil.LogShitou("解绑结果", response);
                try {
                    if (mWProgressDialog != null) {
                        mWProgressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                KolDetailModel kolDetailModel = GsonTools.jsonToBean(response, KolDetailModel.class);
                if (kolDetailModel != null && kolDetailModel.getError() == 0) {
                    mSocialAccounts = kolDetailModel.getSocial_accounts();
                    mKolShows = kolDetailModel.getKol_shows();
                    updateData();
                    isShow = false;
                    mTvEdit.setText("编辑");
                    mMyGridBaseAdapter.notifyDataSetChanged();
                    CustomToast.showShort(BeKolSecondActivity.this, "解绑成功");
                } else {
                    try {
                        CustomToast.showShort(BeKolSecondActivity.this, kolDetailModel.getDetail());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void unbindIndenty(final String indentityUid, final int socialUid, final String provider) {
        BasePresenter mBasePresenter = new BasePresenter();
        RequestParams params = new RequestParams();
        params.put("uid", indentityUid);
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();

        mBasePresenter.getDataFromServer(true, HttpRequest.PUT, HelpTools.getUrl(CommonConfig.INFLUENCE_UNBIND), params, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {

                try {
                    if (mWProgressDialog != null) {
                        mWProgressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                IndentyBean indentyBean = GsonTools.jsonToBean(response, IndentyBean.class);
                if (indentyBean.getError() != 0) {
                    try {
                        CustomToast.showShort(BeKolSecondActivity.this, indentyBean.getDetail());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    socalUnbind(socialUid, provider);
                }
            }
        });
    }

}
