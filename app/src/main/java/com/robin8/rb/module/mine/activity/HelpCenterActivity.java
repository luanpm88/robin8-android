package com.robin8.rb.module.mine.activity;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.helper.IconFontHelper;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.model.LoginBean;
import com.robin8.rb.module.mine.model.HelpCenterModel;
import com.robin8.rb.module.mine.rongcloud.RongCloudBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.CSCustomServiceInfo;
import io.rong.imlib.model.UserInfo;


/**
 Created by IBM on 2016/8/14. */
public class HelpCenterActivity extends BaseActivity {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NORMAL = 1;
    private ListView mListView;
    private HelpListAdapter mHelpListAdapter;
    private List<HelpCenterModel.NoticesBean> mDataList = new ArrayList<>();
    private int from;
    private boolean isRongCloud=false;

    @Override
    public void setTitleView() {
        getData();
        if (from == SPConstants.CREATE_FIRST_LIST) {
            mTVCenter.setText(getString(R.string.text_create));
        } else {
            mTVCenter.setText(getString(R.string.help_center));
        }
//        mTVCenter.setOnClickListener(new View.OnClickListener() {
//
//            final static int COUNTS = 5;//点击次数
//            final static long DURATION = 3 * 1000;//规定有效时间
//            long[] mHits = new long[COUNTS];
//
//            @Override
//            public void onClick(View view) {
//                /**
//                 * 实现双击方法
//                 * src 拷贝的源数组
//                 * srcPos 从源数组的那个位置开始拷贝.
//                 * dst 目标数组
//                 * dstPos 从目标数组的那个位子开始写数据
//                 * length 拷贝的元素的个数
//                 */
//                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
//                //实现左移，然后最后一个位置更新距离开机的时间，如果最后一个时间和最开始时间小于DURATION，即连续5次点击
//                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
//                if (mHits[0] >= (SystemClock.uptimeMillis() - DURATION)) {
//                    //  String tips = "您已在[" + DURATION + "]ms内连续点击【" + mHits.length + "】次了！！！";
//                    //  Toast.makeText(SettingActivity.this, tips, Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(HelpCenterActivity.this, ChangeHttpActivity.class));
//                }
//            }
//        });
    }

    private void getData() {
        Intent intent = getIntent();
        from = intent.getIntExtra("from", - 1);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_help_center, mLLContent);

        if (from == SPConstants.CREATE_FIRST_LIST) {
            View svContent = view.findViewById(R.id.sv_content);
            svContent.setVisibility(View.VISIBLE);
            return;
        }

        mListView = (ListView) view.findViewById(R.id.lv_list);
        mHelpListAdapter = new HelpListAdapter();
        mListView.setAdapter(mHelpListAdapter);
        String helpCenterData = CacheUtils.getString(HelpCenterActivity.this, SPConstants.HELP_CENTER_DATA, null);
        if (! TextUtils.isEmpty(helpCenterData)) {
            parseJson(helpCenterData);
        }
        initData();
        initGetRongCloud();
    }

    /**
     获取融云的token
     */
    private void initGetRongCloud() {
        final LoginBean loginBean = BaseApplication.getInstance().getLoginBean();
        String id;
        final String name;
        final String imgUrl;
        if (loginBean != null) {
            if (TextUtils.isEmpty(HelpTools.getLoginInfo(HelpTools.LoginNumber))){
                id = CommonConfig.TOURIST_PHONE;
            }else {
                id = HelpTools.getLoginInfo(HelpTools.LoginNumber);
            }
            if (! TextUtils.isEmpty(loginBean.getKol().getName())) {
                name = loginBean.getKol().getName();
            }else {
                name = "游客";
            }
            if (! TextUtils.isEmpty(loginBean.getKol().getAvatar_url())) {
                imgUrl = loginBean.getKol().getAvatar_url();
            }else {
                imgUrl = "http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/kol/avatar/109050/22494f2caf!avatar";
            }
        }else {
            id = CommonConfig.TOURIST_PHONE;
            name = "游客";
            imgUrl = "http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/kol/avatar/109050/22494f2caf!avatar";
        }
        if (TextUtils.isEmpty(HelpTools.getCommonXml(HelpTools.CloudToken))) {
            BasePresenter base = new BasePresenter();
            RequestParams requestParams = new RequestParams();
            requestParams.put("userId", id);
            requestParams.put("name", name);
            requestParams.put("portraitUri", imgUrl);
            base.getDataFromServer(false, HttpRequest.POST, CommonConfig.RONG_CLOUD_URL, requestParams, new RequestCallback() {

                @Override
                public void onError(Exception e) {
                    CustomToast.showShort(HelpCenterActivity.this, getString(R.string.no_net));
                }

                @Override
                public void onResponse(String response) {
                    final RongCloudBean rongCloudBean = GsonTools.jsonToBean(response, RongCloudBean.class);
                    if (rongCloudBean.getCode() == 200) {
                        HelpTools.insertCommonXml(HelpTools.CloudToken, rongCloudBean.getToken());
                        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

                            @Override
                            public UserInfo getUserInfo(String s) {
                                UserInfo userInfo = new UserInfo(HelpTools.getLoginInfo(HelpTools.LoginNumber), name
                                        , Uri.parse(imgUrl));
                                return userInfo;
                            }
                        }, true);
                        connect(rongCloudBean.getToken());
                    } else {
                        HelpTools.insertCommonXml(HelpTools.CloudToken, "");
                    }

                }
            });
        }else {
            RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

                @Override
                public UserInfo getUserInfo(String s) {
                    UserInfo userInfo = new UserInfo(HelpTools.getLoginInfo(HelpTools.LoginNumber),name , Uri.parse(imgUrl));
                    return userInfo;
                }
            }, true);
            connect(HelpTools.getCommonXml(HelpTools.CloudToken));
        }
    }

    private void connect(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {

            @Override
            public void onTokenIncorrect() {
              //  Log.e("", "连fgd—————>");
            }

            @Override
            public void onSuccess(String s) {
               // Log.e("", "连接成功—————>" + s);
               // Toast.makeText(HelpCenterActivity.this, "连接成功" + s, Toast.LENGTH_SHORT).show();
                isRongCloud=true;
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
               // Log.e("", "连接失败—————>" + errorCode);
                CustomToast.showShort(HelpCenterActivity.this,getString(R.string.no_net));
            }
        });
    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.MY_HELP;
        super.onResume();
    }

    private void initData() {
        BasePresenter mBasePresenter = new BasePresenter();
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.HELP_CENTER_URL), null, new RequestCallback() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
                LogUtil.LogShitou("帮助中心", "==>" + response);
                parseJson(response);
            }
        });
    }

    private void parseJson(String response) {
        HelpCenterModel helpCenterModel = GsonTools.jsonToBean(response, HelpCenterModel.class);
        if (helpCenterModel != null && helpCenterModel.getError() == 0) {
            List<HelpCenterModel.NoticesBean> temp = helpCenterModel.getNotices();
            mDataList.clear();
            mDataList.add(null);
            mDataList.addAll(temp);
            mHelpListAdapter.notifyDataSetChanged();
            CacheUtils.putString(HelpCenterActivity.this, SPConstants.HELP_CENTER_DATA, response);
        }
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
    }

    private class HelpListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDataList == null ? 0 : mDataList.size();
        }

        @Override
        public HelpCenterModel.NoticesBean getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_HEADER;
            } else {
                return TYPE_NORMAL;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            HelpCenterModel.NoticesBean item = getItem(position);
            if (item == null) {
                View view = LayoutInflater.from(HelpCenterActivity.this).inflate(R.layout.help_center_header_item, null);
//                RelativeLayout llRongCloud = (RelativeLayout) view.findViewById(R.id.ll_server);
//                if (isRongCloud){
//                    llRongCloud.setVisibility(View.VISIBLE);
//                }else {
//                    llRongCloud.setVisibility(View.GONE);
//                }
                TextView tvName = (TextView) view.findViewById(R.id.tv_name);
                TextView tvNameFeed = (TextView) view.findViewById(R.id.tv_name_feed);
                TextView tvArrow = (TextView) view.findViewById(R.id.tv_arrow);
                TextView tvArrowFeed = (TextView) view.findViewById(R.id.tv_arrow_feed);
                tvName.setText(getString(R.string.help_online)+"(工作日10:00 - 17:00)");
                tvNameFeed.setText(R.string.feed_back);
                IconFontHelper.setTextIconFont(tvArrow, R.string.arrow_right);
                IconFontHelper.setTextIconFont(tvArrowFeed, R.string.arrow_right);
                tvName.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        CSCustomServiceInfo.Builder csBuilder = new CSCustomServiceInfo.Builder();
                        CSCustomServiceInfo csInfo = csBuilder.nickName("Robin8").build();

                        /**
                         * 启动客户服聊天界面。
                         *
                         * @param context           应用上下文。
                         * @param customerServiceId 要与之聊天的客服 Id。
                         * @param title             聊天的标题，开发者可以在聊天界面通过 intent.getData().getQueryParameter("title") 获取该值, 再手动设置为标题。
                         * @param customServiceInfo 当前使用客服者的用户信息。{@link io.rong.imlib.model.CSCustomServiceInfo}
                         * KEFU151140489031686
                         */
                        RongIM.getInstance().startCustomerServiceChat(HelpCenterActivity.this, CommonConfig.RONG_CLOUD_ID, getString(R.string.help_online), csInfo);

                    }
                });
                tvNameFeed.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(HelpCenterActivity.this, FeedBackActivity.class);
                        startActivity(intent);
                    }
                });
                return view;
            }
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(HelpCenterActivity.this).inflate(R.layout.help_center_list_item, null);
                holder.tvQuestion = (TextView) convertView.findViewById(R.id.tv_question);
                holder.tvAnswer = (TextView) convertView.findViewById(R.id.tv_answer);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvQuestion.setText(item.getQuestion());
            holder.tvAnswer.setText(item.getAnswer());
            return convertView;
        }
    }

    private class ViewHolder {
        public TextView tvQuestion;
        public TextView tvAnswer;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RongIM.getInstance().disconnect();//不设置收不到推送
    }
}
