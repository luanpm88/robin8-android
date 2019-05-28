package com.robin8.rb.ui.module.find.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.activity.LoginActivity;
import com.robin8.rb.ui.model.BaseBean;
import com.robin8.rb.ui.module.find.FloatAction.FloatActionController;
import com.robin8.rb.ui.module.find.adapter.NineGridViewClickAdapter;
import com.robin8.rb.ui.module.find.model.ArticleReadTimeModel;
import com.robin8.rb.ui.module.find.model.FindArticleListModel;
import com.robin8.rb.ui.module.find.model.ImageInfo;
import com.robin8.rb.ui.module.find.view.NineGridView;
import com.robin8.rb.ui.module.find.view.snaprecyclerview.MultiSnapRecyclerView;
import com.robin8.rb.ui.widget.CircleImageView;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.DateUtil;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.share.RobinShareDialog;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 Created by zc on 2018/4/18. */

public class FindItemDetailActivity extends BaseActivity {
    public static final String FINDDETAIL = "detail";
    public static final String FINDDETAPOSITION = "find_position";
    public static final String FINDWHERE = "find_where";
    private CircleImageView imgUserPhoto;
    private TextView tvUserName;
    private TextView tvTime;
    public TextView tvCollect;
    private TextView tvContent;
    private NineGridView gridView;
    private TextView tvLookNum;
    private LinearLayout llLike;
    private ImageView imgClick;
    private TextView tvLikeNum;
    private LinearLayout llShare;
    private TextView tvShareNum;
    private MultiSnapRecyclerView mListView;
    private List<FindArticleListModel.ListBean> mDataList;
    private WProgressDialog mWProgressDialog;
    private int readTime;
    private int startTime;
    private boolean isLike = false;
    private boolean isCollect = false;
    private FindArticleListModel.ListBean listBean;
    private int widthPixels;
    private int heightPixels;
    private String title;
    private RobinShareDialog shareDialog;
    public int findPosition;
    public String stringExtra;
    public static final int FIND_DETAIL = 124;

    @Override
    public void setTitleView() {
        mTVCenter.setText(R.string.content);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_find_item_detail, mLLContent, true);
        imgUserPhoto = ((CircleImageView) view.findViewById(R.id.img_user_photo));
        tvUserName = ((TextView) view.findViewById(R.id.tv_user_name));
        tvTime = ((TextView) view.findViewById(R.id.tv_article_time));
        tvCollect = ((TextView) view.findViewById(R.id.tv_collect));
        tvContent = ((TextView) view.findViewById(R.id.tv_content));
        gridView = ((NineGridView) view.findViewById(R.id.img_grid_view));
        tvLookNum = ((TextView) view.findViewById(R.id.tv_look_num));
        llLike = ((LinearLayout) view.findViewById(R.id.ll_like));
        imgClick = ((ImageView) view.findViewById(R.id.img_like_click));
        tvLikeNum = ((TextView) view.findViewById(R.id.tv_like_num));
        llShare = ((LinearLayout) view.findViewById(R.id.ll_share));
        tvShareNum = ((TextView) view.findViewById(R.id.tv_like_share_num));
        mListView = ((MultiSnapRecyclerView) view.findViewById(R.id.relevant_listview));
        LinearLayoutManager firstManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mListView.setLayoutManager(firstManager);
        mDataList = new ArrayList<>();
        tvCollect.setOnClickListener(this);
        llShare.setOnClickListener(this);
        llLike.setOnClickListener(this);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        widthPixels = dm.widthPixels;
        heightPixels = dm.heightPixels;
        //startTime= Integer.valueOf(String.valueOf(DateUtil.getNowTimeMs("yyyy-MM=dd HH-mm-dd")))/1000;
        //LogUtil.LogShitou("当前时2","====》"+startTime);
        //1524135420000    2018-04=19 18-57-19
        //1524135480000    2018-04=19 18-58-19
        startTime = Integer.valueOf(DateUtil.getNowTimeM("yyyy-MM-dd HH-mm-ss"));
        //  LogUtil.LogShitou("当前时2", "====》" + Integer.valueOf(DateUtil.getNowTimeM("yyyy-MM-dd HH-mm-ss")));
        //  LogUtil.LogShitou("当前时3", "====》" + DateUtil.getNowTime("yyyy-MM=dd HH-mm-ss"));
        initData();

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_collect:
                if (! BaseApplication.getInstance().hasLogined()) {
                    login();
                } else {
                    if (isCollect == false) {
                        if (listBean.isIs_collected() == false) {
                            setParams(0, true, listBean);
                        } else {
                            setParams(0, false, listBean);
                        }
                    } else {
                        setParams(0, false, listBean);
                    }
                }
                break;
            case R.id.ll_like:
                if (! BaseApplication.getInstance().hasLogined()) {
                    login();
                } else {
                    if (isLike == false) {
                        if (listBean.isIs_liked() == false) {
                            setParams(1, true, listBean);
                        } else {
                            setParams(1, false, listBean);
                        }
                    } else {
                        setParams(1, false, listBean);
                    }
                }
                break;
            case R.id.ll_share:
                if (! BaseApplication.getInstance().hasLogined()) {
                    login();
                } else {
                    showInviteDialog();
                }
                break;
        }
    }

    private void initData() {
        Intent intent = getIntent();
        listBean = (FindArticleListModel.ListBean) intent.getSerializableExtra(FINDDETAIL);
        findPosition = intent.getIntExtra(FINDDETAPOSITION, 0);
        stringExtra = intent.getStringExtra(FINDWHERE);
        BitmapUtil.loadImage(this, listBean.getAvatar_url(), imgUserPhoto);
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        for (int i = 0; i < listBean.getPics().get(0).size(); i++) {
            ImageInfo info = new ImageInfo();
            info.setBigImageUrl(listBean.getPics().get(1).get(i));
            info.setThumbnailUrl(listBean.getPics().get(0).get(i));
            imageInfo.add(info);
        }
        gridView.setAdapter(new NineGridViewClickAdapter(this, imageInfo));
        initAll(listBean.getUser_name(), tvUserName);
        tvTime.setText(DateUtil.getCountdownMore("yyyy-MM-dd HH:mm:ss", listBean.getPost_date()));
        initAll(listBean.getTitle(), tvContent);
        initAll(String.valueOf(listBean.getLikes_count()), tvLikeNum);
        initAll(String.valueOf(listBean.getForwards_count()), tvShareNum);
        if (listBean.getReads_count() >= 10000) {
            tvLookNum.setText(getString(R.string.robin436,String.valueOf(listBean.getReads_count() / 10000) ));
        } else {
            tvLookNum.setText(getString(R.string.robin437,String.valueOf(listBean.getReads_count()) ));
        }
        if (listBean.isIs_collected()) {
            tvCollect.setBackgroundResource(R.drawable.shape_bg_gray_pane_pane);
            tvCollect.setText(getString(R.string.text_collected));
            tvCollect.setTextColor(getResources().getColor(R.color.gray_first));
        } else {
            tvCollect.setBackgroundResource(R.drawable.shape_bg_yellow_pane_first);
            tvCollect.setText(getString(R.string.text_collect));
            tvCollect.setTextColor(getResources().getColor(R.color.yellow_custom));
        }
        if (listBean.isIs_liked()) {
            imgClick.setImageResource(R.mipmap.icon_like_yes);
        } else {
            imgClick.setImageResource(R.mipmap.icon_like_no);
        }
        getData(listBean.getTag(), listBean.getPost_id());

    }
    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.FINDDETAIL;
        super.onResume();
    }
    private void readTime(int time) {

        BasePresenter mBasePresenter = new BasePresenter();
        RequestParams mRequestParams = new RequestParams();
        mRequestParams.put("post_id", listBean.getPost_id());
        mRequestParams.put("stay_time", time);
        mRequestParams.put("tag",listBean.getTag());
        mRequestParams.put("title",listBean.getTitle());
        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.FIND_READ), mRequestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onResponse(String response) {
                LogUtil.LogShitou("find文章阅读时间+", response);
                ArticleReadTimeModel model = GsonTools.jsonToBean(response, ArticleReadTimeModel.class);
//                HelpTools.insertCommonXml(HelpTools.REDNUM,String.valueOf(11.0));
//                FloatActionController.getInstance().startMonkServer(FindItemDetailActivity.this);
                if (model!=null){
                    if (model.getError()==0){
                        if (model.getRed_money()!=0){
                            if (TextUtils.isEmpty(HelpTools.getCommonXml(HelpTools.REDNUM))&& BaseApplication.getInstance().hasLogined()){
                                HelpTools.insertCommonXml(HelpTools.REDNUM,String.valueOf(model.getRed_money()));
                                FloatActionController.getInstance().startMonkServer(FindItemDetailActivity.this);
                            }
                        }else {
                            HelpTools.insertCommonXml(HelpTools.REDNUM,"");
                        }
                    }
                }
            }
        });
    }

    private void setParams(final int whith, final boolean is, final FindArticleListModel.ListBean listBean) {
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(FindItemDetailActivity.this);
        }
        mWProgressDialog.show();
        BasePresenter mBasePresenter = new BasePresenter();
        RequestParams mRequestParams = new RequestParams();
        switch (whith) {
            case 0:
                mRequestParams.put("_type", "collect");
                break;
            case 1:
                mRequestParams.put("_type", "like");
                break;
            case 2:
                mRequestParams.put("_type", "forward");// like|collect
                break;
        }
        mRequestParams.put("post_id", listBean.getPost_id());
        if (is) {
            mRequestParams.put("_action", "add");//add | cancel
        } else {
            mRequestParams.put("_action", "cancel");//add | cancel
        }
        mRequestParams.put("tag", listBean.getTag());
        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.FIND_SET), mRequestParams, new RequestCallback() {

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
              //  LogUtil.LogShitou("find文章设置+", response);
                BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);
                if (baseBean.getError() == 0) {
                    if (whith == 0) {
                        if (is) {
                            isCollect = true;
                            listBean.setIs_collected(true);
                            tvCollect.setBackgroundResource(R.drawable.shape_bg_gray_pane_pane);
                            tvCollect.setText(getString(R.string.text_collected));
                            tvCollect.setTextColor(getResources().getColor(R.color.gray_first));
                        } else {
                            isCollect = false;
                            listBean.setIs_collected(false);
                            tvCollect.setBackgroundResource(R.drawable.shape_bg_yellow_pane_first);
                            tvCollect.setText(getString(R.string.text_collect));
                            tvCollect.setTextColor(getResources().getColor(R.color.yellow_custom));
                        }
                    } else if (whith == 1) {
                        if (is == true) {
                            isLike = true;
                            listBean.setIs_liked(true);
                            imgClick.setImageResource(R.mipmap.icon_like_yes);
                            tvLikeNum.setText(String.valueOf((Integer.valueOf(listBean.getLikes_count()) + 1)));
                            listBean.setLikes_count(Integer.valueOf(listBean.getLikes_count()) + 1);
                        } else {
                            isLike = false;
                            listBean.setIs_liked(false);
                            imgClick.setImageResource(R.mipmap.icon_like_no);
                            if (Integer.valueOf(tvLikeNum.getText().toString()) - 1 < 0) {
                                tvLikeNum.setText(0);
                            } else {
                                tvLikeNum.setText(String.valueOf((Integer.valueOf(tvLikeNum.getText().toString()) - 1)));
                                listBean.setLikes_count(Integer.valueOf(listBean.getLikes_count()) - 1);

                            }
                        }
                    } else {
                        tvShareNum.setText(String.valueOf((Integer.valueOf(tvShareNum.getText().toString()) + 1)));
                        listBean.setForwards_count(Integer.valueOf(listBean.getLikes_count()) + 1);

                    }
                    sendCast(listBean);
                }
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
       // LogUtil.LogShitou("当前时5", "====》" + Integer.valueOf(DateUtil.getNowTimeM("yyyy-MM-dd HH-mm-ss")));
        if (BaseApplication.getInstance().hasLogined()){
            readTime = Integer.valueOf(DateUtil.getNowTimeM("yyyy-MM-dd HH-mm-ss")) - startTime;
            if (readTime < 1) {
                readTime(1);
            } else {
                readTime(readTime);
            }
        }
    }


    private void getData(String tag, String post_id) {
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();
        BasePresenter mBasePresenter = new BasePresenter();
        RequestParams params = new RequestParams();
        params.put("tag", tag);
        params.put("post_id", post_id);

        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.FIND_ITEM_DETAIL), params, new RequestCallback() {

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
               // LogUtil.LogShitou("find+内容推荐", response);
                FindArticleListModel model = GsonTools.jsonToBean(response, FindArticleListModel.class);
                if (model != null) {
                    if (model.getError() == 0) {
                        mDataList.addAll(model.getList());
                        mListView.setAdapter(new MyRelevantAdapter());
                    }
                }
            }
        });
    }

    class MyRelevantAdapter extends RecyclerView.Adapter<MyRelevantAdapter.ViewHolder> {
        @Override
        public MyRelevantAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(FindItemDetailActivity.this).inflate(R.layout.item_find_relevant, viewGroup, false);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.width = widthPixels * 6 / 7;
            view.setLayoutParams(params);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyRelevantAdapter.ViewHolder holder, int position) {
            final FindArticleListModel.ListBean item = mDataList.get(position);
            if (item != null) {
                if (item.getPics().get(0) != null && item.getPics().get(0).size() != 0) {
                    holder.imgIcon.setVisibility(View.VISIBLE);
                    BitmapUtil.loadFindImage(FindItemDetailActivity.this, item.getPics().get(0).get(0), holder.imgIcon, true);
                } else {
                    holder.imgIcon.setVisibility(View.GONE);
                }
                holder.tvTitle.setText(item.getTitle());
            }
            if (!TextUtils.isEmpty(item.getUser_name())){
                holder.tvForm.setText(getString(R.string.robin466,item.getUser_name()));
            }else {
                holder.tvForm.setText(getString(R.string.robin466,getString(R.string.weibo)));
            }
            holder.llRelevant.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(FindItemDetailActivity.this, FindItemDetailActivity.class);
                    if (item != null) {
                        intent.putExtra(FINDDETAIL, item);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private final ImageView imgIcon;
            private final TextView tvTitle;
            private final TextView tvForm;
            private final LinearLayout llRelevant;

            ViewHolder(final View view) {
                super(view);
                imgIcon = ((ImageView) view.findViewById(R.id.img_icon));
                tvTitle = ((TextView) view.findViewById(R.id.tv_title));
                tvForm = ((TextView) view.findViewById(R.id.tv_form));
                llRelevant = ((LinearLayout) view.findViewById(R.id.ll_relevant));
            }
        }
    }

    private void initAll(String str, TextView tv) {
        if (TextUtils.isEmpty(str)) {
            tv.setText("");
        } else {
            tv.setText(str);
        }
    }

    private void showInviteDialog() {
        shareDialog = new RobinShareDialog(this);
        if (listBean != null) {
            title = "#robin8#" + listBean.getTitle();
        } else {
            title = "#robin8#";
        }
        String url = HelpTools.getUrl(listBean.getForward_url());
        shareDialog.shareFacebook(url,title,getString(R.string.app_name),"http://7xq4sa.com1.z0.glb.clouddn.com/robin8_icon.png");
        shareDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shareDialog.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }

    private void sendCast(FindArticleListModel.ListBean bean) {
        if (! TextUtils.isEmpty(stringExtra)) {
            Intent intent = new Intent();
            if (stringExtra.equals("1")) {
                intent.setAction("com.bean.refresh");
            } else if (stringExtra.equals("2")) {
                intent.setAction("com.bean.refresh.collect");
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", bean);
            LogUtil.LogShitou("看看传过来的position","===>"+findPosition);
            bundle.putInt("position", findPosition);
            intent.putExtra("datas", bundle);
            sendBroadcast(intent);
        }
    }


    private void login() {
        Intent intent = new Intent(FindItemDetailActivity.this, LoginActivity.class);
        intent.putExtra("find_detail", "find_detail");
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tvCollect.setBackgroundResource(0);
        System.gc();
    }

}
