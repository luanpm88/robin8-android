package com.robin8.rb.module.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.model.BaseBean;
import com.robin8.rb.module.first.model.KolDetailModel;
import com.robin8.rb.module.first.model.SocialAccountsBean;
import com.robin8.rb.module.mine.model.MineShowModel;
import com.robin8.rb.module.mine.presenter.BindSocialPresenter;
import com.robin8.rb.module.reword.activity.DetailContentActivity;
import com.robin8.rb.module.reword.activity.ScreenImgActivity;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.view.widget.CustomDialogManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


public class NewUserTaskActivity extends BaseActivity {

    private LinearLayout llFirstStep;
    private RelativeLayout llCampaign;
    private RelativeLayout llImgOne;
    private TextView tvButtonRight;
    private TextView tvButtonLeft;
    private View viewLIne;
    private ImageView imgUpOne;
    private LinearLayout llImgUpText;
    private RelativeLayout llLook;
    private ArrayList<String> imgName;
    private ArrayList<String> imgSimple;
    private LinearLayout llBottom;
    private RelativeLayout llBgTwo;
    private boolean isFirstUp = true;
    private RelativeLayout llUp;
    public final static int NEW_USER_LOOK_IMG = 120;
    public final static int NEW_USER_UP_IMG = 121;
    private RelativeLayout llBg;
    private RelativeLayout llShow;
    private LinearLayout llShareWechat;
    private LinearLayout llToWcMoment;
    private LinearLayout llToWc;

    @Override
    public void setTitleView() {
       mLLTitleBar.setVisibility(View.GONE);
    }
    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.NEWUSERTASK;
        super.onResume();
    }
    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_new_user_task, mLLContent, true);
        llFirstStep = ((LinearLayout) view.findViewById(R.id.ll_first_step));
        llFirstStep.setOnClickListener(this);
        llCampaign = ((RelativeLayout) view.findViewById(R.id.ll_campaign));
        llShow = ((RelativeLayout) view.findViewById(R.id.ll_show));
        llImgOne = ((RelativeLayout) view.findViewById(R.id.ll_img_one));
        llBg = ((RelativeLayout) view.findViewById(R.id.ll_bg));
        llImgOne.setOnClickListener(this);

        llShareWechat = ((LinearLayout) view.findViewById(R.id.ll_bottom_share));
        llToWcMoment = ((LinearLayout) view.findViewById(R.id.ll_wechatmoments));
        llToWc = ((LinearLayout) view.findViewById(R.id.ll_wechat));
        llToWcMoment.setOnClickListener(this);
        llToWc.setOnClickListener(this);
        imgName = new ArrayList<>();
        imgSimple = new ArrayList<>();
        imgName.add("截图示例");
        imgSimple.add("http://7xozqe.com1.z0.glb.clouddn.com/wechat_example.jpg");
        //底部
        tvButtonRight = ((TextView) view.findViewById(R.id.tv_bottom_right));
        tvButtonLeft = ((TextView) view.findViewById(R.id.tv_bottom_left));
        viewLIne = view.findViewById(R.id.view_line);
        tvButtonLeft.setOnClickListener(this);
        tvButtonRight.setOnClickListener(this);
        //召唤上传截图底部弹框
        imgUpOne = ((ImageView) view.findViewById(R.id.img_up_one));
        llImgUpText = ((LinearLayout) view.findViewById(R.id.img_up_text));
        llImgUpText.setOnClickListener(this);
        //查看截图示例
        llBottom = ((LinearLayout) view.findViewById(R.id.ll_bottom_dialog));
        llBgTwo = ((RelativeLayout) view.findViewById(R.id.ll_bg_two));
        llLook = ((RelativeLayout) view.findViewById(R.id.ll_looK));
        llLook.setOnClickListener(this);
        //最后上传截图
        llUp = ((RelativeLayout) view.findViewById(R.id.ll_up));
        llUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_first_step:
                llFirstStep.setVisibility(View.GONE);
                llBg.setVisibility(View.VISIBLE);
                llCampaign.setVisibility(View.VISIBLE);
                llShow.setVisibility(View.VISIBLE);
                llImgOne.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_img_one:
                //分享赚收益
                llImgOne.setVisibility(View.GONE);
                llShareWechat.setVisibility(View.VISIBLE);

                break;
            case R.id.img_up_text:
                //底部导航出现，开始查看截图
                imgUpOne.setVisibility(View.GONE);
                llImgUpText.setVisibility(View.GONE);
                llBgTwo.setVisibility(View.VISIBLE);
                llBottom.setVisibility(View.VISIBLE);
                if (isFirstUp == true) {
                    llLook.setVisibility(View.VISIBLE);
                } else {
                    //打开相册上传截图
                    llUp.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.ll_looK:
                // 去查看截图示例
                Intent intent = new Intent(NewUserTaskActivity.this, ScreenImgActivity.class);
                intent.putStringArrayListExtra(ScreenImgActivity.EXTRA_NAME_LIST, imgName);
                //标题
                intent.putExtra(ScreenImgActivity.EXTRA_TITLE, getString(R.string.screenshot_reference));
                intent.putExtra(ScreenImgActivity.EXTRA_TYPE, "3");
                //参考图片
                intent.putStringArrayListExtra(ScreenImgActivity.EXTRA_SCREEN_LISTS,imgSimple );
                startActivityForResult(intent, NEW_USER_LOOK_IMG);
                //截图参考查看完成返回提示此时count==1

                break;
            case R.id.ll_up:
                //真的上传截图
                Intent intentUp = new Intent(NewUserTaskActivity.this, ScreenImgActivity.class);
                intentUp.putStringArrayListExtra(ScreenImgActivity.EXTRA_NAME_LIST, (ArrayList<String>) imgName);
                intentUp.putExtra(ScreenImgActivity.EXTRA_TITLE, getString(R.string.upload_screenshot));
                intentUp.putExtra(ScreenImgActivity.EXTRA_TYPE, "4");
                intentUp.putStringArrayListExtra(ScreenImgActivity.EXTRA_SCREEN_LISTS, imgSimple);
                startActivityForResult(intentUp, NEW_USER_UP_IMG);
                break;
            case R.id.ll_wechatmoments:
                llShareWechat.setVisibility(View.GONE);
                share(this,0);
                break;
            case R.id.ll_wechat:
                llShareWechat.setVisibility(View.GONE);
                share(this,1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case NEW_USER_LOOK_IMG:
                    llLook.setVisibility(View.GONE);
                    llBottom.setVisibility(View.GONE);
                    llBgTwo.setVisibility(View.GONE);
                    showSuccessDialog(1);
                    break;
                case NEW_USER_UP_IMG:
                    llUp.setVisibility(View.GONE);
                    llBottom.setVisibility(View.GONE);
                    llBgTwo.setVisibility(View.GONE);
                    showSuccessDialog(2);
                    break;
            }
        }
    }

    private List<DetailContentActivity.ShareBean> myShareListCampaign;
    private CustomDialogManager mCustomDialogManager;
    private WProgressDialog mWProgressDialog;

    private BasePresenter mBasePresenter;
    private RequestParams mRequestParams;
    private String TAG = "null";

    private void share(final Activity activity, final int wechatType) {

        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(activity);
        }
        mWProgressDialog.show();
        //绑定微信
        String data = CacheUtils.getString(activity, SPConstants.MINE_DATA, null);
        MineShowModel mineShowModel = GsonTools.jsonToBean(data, MineShowModel.class);
        if (mineShowModel != null) {
            int id = mineShowModel.getKol().getId();
            if (mBasePresenter == null) {
                mBasePresenter = new BasePresenter();
            }

            if (mRequestParams == null) {
                mRequestParams = new RequestParams();
            }
            mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.FIRST_KOL_LIST_URL + "/" + String.valueOf(id) + "/" + "detail"), mRequestParams, new RequestCallback() {

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
                    //  LogUtil.LogShitou("活动分享获取信息！！！", response);
                    KolDetailModel kolDetailModel = GsonTools.jsonToBean(response, KolDetailModel.class);
                    if (kolDetailModel != null && kolDetailModel.getError() == 0) {
                        List<SocialAccountsBean> mSocialAccounts = kolDetailModel.getSocial_accounts();
                        if (mSocialAccounts != null) {
                            if (mSocialAccounts.size() != 0) {
                                for (int i = 0; i < mSocialAccounts.size(); i++) {
                                    if (mSocialAccounts.get(i).getProvider_name().equals(activity.getString(R.string.weixin)) || mSocialAccounts.get(i).getProvider().equals("wechat")) {
                                        TAG = activity.getString(R.string.weixin);
                                    }
                                }
                                if (TAG.equals("null")) {
                                    //没有绑定微信
                                    CustomToast.showShort(activity, "正在前往微信中");
                                    bind(activity, activity.getString(R.string.weixin), wechatType);
                                } else {
                                    //绑定了就去分享
                                    popSharedialog(activity,wechatType);
                                }
                            } else {
                                CustomToast.showShort(activity, "正在前往微信中");
                                bind(activity, activity.getString(R.string.weixin), wechatType);
                            }
                        } else {
                            CustomToast.showShort(activity, "正在前往微信中");
                            bind(activity, activity.getString(R.string.weixin), wechatType);
                        }
                    }
                }
            });
        }

    }

    private void bind(final Activity activity, final String names, final int wechatType) {
        BindSocialPresenter presenter = new BindSocialPresenter(activity.getApplicationContext(), null, names, 0);
        presenter.setOnBindListener(new BindSocialPresenter.OnBindListener() {

            @Override
            public void onResponse(String name) {
                if (null != name) {
                    CustomToast.showShort(activity, "已成功绑定" + names);
                    bindPostData(activity, names, name, wechatType);
                } else {
                    CustomToast.showLong(activity, "绑定失败，请重试");
                }
            }
        });
        if (activity.getString(R.string.weixin).equals(names)) {
            presenter.authorize(new Wechat(activity));
        } else if (activity.getString(R.string.weibo).equals(names)) {
            presenter.authorize(new SinaWeibo(activity));
        }
    }

    private void bindPostData(final Activity activity, String name, String userName, final int wechatType) {

        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(activity);
        }
        mWProgressDialog.show();
        BasePresenter mBasePresenter = new BasePresenter();
        RequestParams params = new RequestParams();
        params.put("provider_name", name);
        params.put("price", "1");
        params.put("followers_count", "1");
        params.put("username", userName);
        mBasePresenter.getDataFromServer(true, HttpRequest.POST, (HelpTools.getUrl(CommonConfig.UPDATE_SOCIAL_URL_OLD)), params, new RequestCallback() {

            @Override
            public void onError(Exception e) {

                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
                //   LogUtil.LogShitou("分享页面提交微信绑定", "OK" + response);
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);
                if (bean == null) {
                    CustomToast.showShort(activity, activity.getString(R.string.please_data_wrong));
                    return;
                }
                if (bean.getError() == 0) {
                    //分享
                    // wechatType 0 是朋友圈，1是群组¬
                    popSharedialog(NewUserTaskActivity.this, wechatType);
                } else {
                    CustomToast.showShort(activity, "绑定失败");
                }
            }
        });
    }

    private static final String IMAGE_URL = "http://7xq4sa.com1.z0.glb.clouddn.com/robin8_icon.png";
    private static final String TITLE_URL = CommonConfig.SERVICE + "/invite?inviter_id=";
    /**
     弹出分享面板
     */

    private void popSharedialog(Activity activity, int wechatType) {
        String platName = "";
        if (wechatType == 0) {
            platName = WechatMoments.NAME;
        } else {
            platName = Wechat.NAME;
        }
        int id = BaseApplication.getInstance().getLoginBean().getKol().getId();
        CustomToast.showShort(activity, "正在前往分享...");
        ShareSDK.initSDK(activity);
        OnekeyShare oks = new OnekeyShare();
        oks.setPlatform(platName);
        MySharedListener mySharedListener = new MySharedListener(activity);
        oks.setCallback(mySharedListener);
        oks.disableSSOWhenAuthorize();
        oks.setText(getString(R.string.share_invite_friends_text));
        oks.setTitle(getString(R.string.share_invite_friends_title));

        oks.setTitleUrl(TITLE_URL + String.valueOf(id));
        oks.setImageUrl(IMAGE_URL);
        if (Wechat.NAME.equals(platName) || WechatMoments.NAME.equals(platName)){
            oks.setUrl(TITLE_URL + String.valueOf(id));
        }
        oks.setSite(getString(R.string.app_name));
        oks.setSiteUrl(CommonConfig.SITE_URL);
        oks.show(this);
    }

    public class MySharedListener implements PlatformActionListener {

        private Activity activity;

        public MySharedListener(Activity activity) {

            this.activity = activity;
        }

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            CustomToast.showShort(activity, "分享成功");
            //分享成功
            //弹出奖励框
            showSuccessDialog(0);
            tvButtonLeft.setVisibility(View.VISIBLE);
            viewLIne.setVisibility(View.VISIBLE);
            tvButtonLeft.setText("上传截图");
            tvButtonRight.setText("再次分享");


        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            CustomToast.showShort(activity, "分享失败，请重新分享");

        }

        @Override
        public void onCancel(Platform platform, int i) {
            CustomToast.showShort(activity, "取消分享");
        }
    }

    private void showSuccessDialog(final int count) {
        View view = LayoutInflater.from(NewUserTaskActivity.this).inflate(R.layout.dialog_sign_success, null);
        TextView tvInfo = (TextView) view.findViewById(R.id.tv_info);
        TextView tvKnow = (TextView) view.findViewById(R.id.tv_know);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        if (count == 0) {
            tvTitle.setText("分享成功");
            tvInfo.setText("分享成功，0.50元奖励已放入您的钱包！\n继续完成任务，可获得更多奖励哦～");
            StringUtil.setTextViewSpan(tvInfo, 35, 5, 9, getResources().getColor(R.color.blue_custom));
        } else if (count == 1) {
            tvTitle.setText("查看截图示例");
            tvInfo.setText("查看截图示例任务完成，0.50元奖励已放入您的钱包！继续完成任务，可获得更多奖励哦～");
            StringUtil.setTextViewSpan(tvInfo, 35, 11, 15, getResources().getColor(R.color.blue_custom));
        } else if (count == 2) {
            tvTitle.setText("任务完成");
            tvInfo.setText("上传截图成功，1.00元奖励已放入您的钱包！新手任务已全部完成～");
            StringUtil.setTextViewSpan(tvInfo, 35, 7, 11, getResources().getColor(R.color.blue_custom));
        }

        final CustomDialogManager cdm = new CustomDialogManager(NewUserTaskActivity.this, view);
        tvKnow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cdm.dismiss();
                imgUpOne.setVisibility(View.VISIBLE);
                llImgUpText.setVisibility(View.VISIBLE);
                if (count == 1) {
                    isFirstUp = false;
                } else if (count == 2) {
                    //结束了请求接口并回去
                    setResult(UserSignActivity.NEW_USER_TASKS_OVER);
                    finish();
                }


            }
        });
        cdm.dg.setCanceledOnTouchOutside(false);
        cdm.dg.getWindow().setGravity(Gravity.CENTER);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        cdm.showDialog();
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }
}
