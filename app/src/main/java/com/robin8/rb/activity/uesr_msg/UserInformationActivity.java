package com.robin8.rb.activity.uesr_msg;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Joiner;
import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.model.UserCircleBean;
import com.robin8.rb.model.UserShowBean;
import com.robin8.rb.module.first.model.SocialAccountsBean;
import com.robin8.rb.module.mine.activity.BeKolSecondActivity;
import com.robin8.rb.module.social.view.LinearLayoutForListView;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.NetworkUtil;
import com.robin8.rb.view.widget.CircleImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 用户认证信息 */
public class UserInformationActivity extends BaseActivity {

    @Bind(R.id.civ_image)
    CircleImageView civImage;
    @Bind(R.id.tv_nick_name)
    TextView tvNickName;
    @Bind(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @Bind(R.id.layout_header)
    LinearLayout layoutHeader;
    @Bind(R.id.civ_general_image)
    CircleImageView civGeneralImage;
    @Bind(R.id.tv_public_circle)
    TextView tvPublicCircle;
    @Bind(R.id.layout_public)
    LinearLayout layoutPublic;
    @Bind(R.id.tv_have_not_first)
    TextView tvHaveNotFirst;
    @Bind(R.id.civ_bigv_image)
    CircleImageView civBigvImage;
    @Bind(R.id.tv_bigv_check_result)
    TextView tvBigvCheckResult;
    @Bind(R.id.img_bigv_go)
    ImageView imgBigvGo;
    @Bind(R.id.tv_open_bigv)
    TextView tvOpenBigv;
    @Bind(R.id.layout_bigv)
    LinearLayout layoutBigv;
    @Bind(R.id.tv_have_not_second)
    TextView tvHaveNotSecond;
    @Bind(R.id.civ_creator_image)
    CircleImageView civCreatorImage;
    @Bind(R.id.tv_creator_check_result)
    TextView tvCreatorCheckResult;
    @Bind(R.id.img_creator_go)
    ImageView imgCreatorGo;
    @Bind(R.id.tv_open_creator)
    TextView tvOpenCreator;
    @Bind(R.id.layout_creator)
    LinearLayout layoutCreator;
    @Bind(R.id.tv_wechat_icon)
    TextView tvWechatIcon;
    @Bind(R.id.tv_qq_icon)
    TextView tvQqIcon;
    @Bind(R.id.tv_weibo_icon)
    TextView tvWeiboIcon;
    @Bind(R.id.layout_bind_social)
    LinearLayout layoutBindSocial;
    @Bind(R.id.lv_list)
    LinearLayoutForListView lvList;
    private List<UserIdBean> mDataList;
    private MySocialAdapter mySocialAdapter;
    private int wechatFirendsCount;
    private List<SocialAccountsBean> mSocialAccounts;
    private int kolId;
    public static final String USERINFO = "UserInformationActivity";
    private String intentInfo;
    private UserShowBean.KolBean.PublicWechatAccountBean public_wechat_account;
    private UserShowBean.KolBean.CreatorBean creator;
    private UserShowBean.KolBean.WeiboAccountBean weibo_account;
    private UserShowBean.KolBean kolBean;
    private List<String> circleName;

    private final String bigVName = "大V用户";
    private final String weiBoPass = "微博大V已认证";
    private final String weiBoCheck = "微博大V认证中";
    private final String weiBoUnPass = "微博大V认证未通过";

    private final String wechatPass = "微信公众号大V已认证";
    private final String wechatCheck = "微信公众号大V认证中";
    private final String wechatUnPass = "微信公众号大V认证未通过";

    private final String allPass = "微博／微信公众号大V已认证";
    private final String allCheck = "微博／微信公众号大V认证中";
    private final String allUnPass = "微博／微信公众号大V认证未通过";
    private final String allUnCheck = "微博／微信公众号大V未认证";

    private final String craetorName = "内容创作者";
    private final String publicName = "普通用户";

    @Override
    public void setTitleView() {
        mTVCenter.setText("个人信息");
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_user_information, mLLContent, true);
        ButterKnife.bind(this);
        mySocialAdapter = new MySocialAdapter();
        mDataList = new ArrayList<>();
        circleName = new ArrayList<>();
        mDataList.add(new UserIdBean(R.mipmap.icon_general_yes, "身份信息", getResources().getString(R.string.tv_general_user).toString(), "", true));
        mDataList.add(new UserIdBean(R.mipmap.icon_bigv_no, "以下身份未注册", bigVName, allUnCheck, false));
        mDataList.add(new UserIdBean(R.mipmap.icon_creator_no, "", getResources().getString(R.string.tv_general_user).toString(), "职业创作者认证", false));
        lvList.setAdapter(mySocialAdapter);
        initData();
        //  initViewData();
    }

    private WProgressDialog mWProgressDialog;

    private void initData() {
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();
        final BasePresenter mBasePresenter = new BasePresenter();
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.USER_MY_SHOW_URL), null, new RequestCallback() {

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
                parseJson(response);
            }
        });
    }

    private void parseJson(String response) {
        LogUtil.LogShitou("个人基本信息", "==>" + response);
        intentInfo = response;
        UserShowBean userShowBean = GsonTools.jsonToBean(response, UserShowBean.class);
        if (userShowBean != null) {
            if (userShowBean.getError() == 0) {
                tvWechatIcon.setSelected(false);
                tvWeiboIcon.setSelected(false);
                tvQqIcon.setSelected(false);
                kolBean = userShowBean.getKol();
                public_wechat_account = userShowBean.getKol().getPublic_wechat_account();
                creator = userShowBean.getKol().getCreator();
                weibo_account = userShowBean.getKol().getWeibo_account();
                viewSetText(userShowBean.getKol().getName(), "请填写昵称", tvNickName);
                viewSetText(userShowBean.getKol().getMobile_number(), "请填写昵称", tvPhoneNum);
                if (! TextUtils.isEmpty(userShowBean.getKol().getAvatar_url())) {
                    BitmapUtil.loadImage(UserInformationActivity.this, userShowBean.getKol().getAvatar_url(), civImage);
                } else {
                    civImage.setImageResource(R.mipmap.icon_user_default);
                }
                wechatFirendsCount = userShowBean.getKol().getWechat_friends_count();
                kolId = userShowBean.getKol().getId();
                mDataList.clear();
                if (kolBean.getCircles() != null) {
                    if (kolBean.getCircles().size() != 0) {
                        if (circleName.size() != 0) {
                            circleName.clear();
                        }
                        for (int i = 0; i < kolBean.getCircles().size(); i++) {
                            circleName.add(kolBean.getCircles().get(i).getLabel());
                        }
                    }
                }
                mDataList.add(new UserIdBean(R.mipmap.icon_general_yes, "身份信息", getResources().getString(R.string.tv_general_user).toString(), Joiner.on(" ,").join(circleName), true));

                if (userShowBean.getKol().isIs_big_v() == true && userShowBean.getKol().isIs_creator() == false) {
                    mDataList.add(new UserIdBean(R.mipmap.icon_bigv_yes, "", bigVName, allUnCheck, true));
                    mDataList.add(new UserIdBean(R.mipmap.icon_creator_no, "以下身份未注册", craetorName, "职业内容创作者未认证", false));
                } else if (userShowBean.getKol().isIs_big_v() == false && userShowBean.getKol().isIs_creator() == true) {
                    mDataList.add(new UserIdBean(R.mipmap.icon_creator_yes, "", craetorName, "职业内容创作者已认证", true));
                    mDataList.add(new UserIdBean(R.mipmap.icon_bigv_no, "以下身份未注册", bigVName, allUnCheck, false));
                } else if (userShowBean.getKol().isIs_big_v() == true && userShowBean.getKol().isIs_creator() == true) {
                    mDataList.add(new UserIdBean(R.mipmap.icon_bigv_yes, "", bigVName, allPass, true));
                    mDataList.add(new UserIdBean(R.mipmap.icon_creator_yes, "", craetorName, "职业内容创作者已认证", true));
                } else {
                    mDataList.add(new UserIdBean(R.mipmap.icon_bigv_no, "以下身份未注册", bigVName, allUnCheck, false));
                    mDataList.add(new UserIdBean(R.mipmap.icon_creator_no, "", craetorName, "职业内容创作者未认证", false));
                }
                lvList.setAdapter(mySocialAdapter);
                mySocialAdapter.notifyDataSetChanged();
                mSocialAccounts = userShowBean.getKol().getSocial_accounts();

                if (mSocialAccounts != null) {
                    if (mSocialAccounts.size() != 0) {
                        for (int i = 0; i < mSocialAccounts.size(); i++) {
                            if (getString(R.string.weixin).equals(mSocialAccounts.get(i).getProvider_name())) {
                                tvWechatIcon.setSelected(true);
                            } else if (getString(R.string.qq).equals(mSocialAccounts.get(i).getProvider_name())) {
                                tvQqIcon.setSelected(true);
                            } else if (getString(R.string.weibo).equals(mSocialAccounts.get(i).getProvider_name())) {
                                tvWeiboIcon.setSelected(true);
                            }
                        }
                    }
                }
            }
        }

    }

    class MySocialAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public UserIdBean getItem(int position) {
            return mDataList.get(position);
        }


        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(UserInformationActivity.this).inflate(R.layout.user_id_item, null);
                convertView.setTag(new ViewHolder(convertView));
            }
            final ViewHolder holder = (ViewHolder) convertView.getTag();
            if (mDataList.get(position).isOpen == true) {
                holder.tvOpen.setVisibility(View.GONE);
                holder.imgGo.setVisibility(View.VISIBLE);
            } else {
                holder.tvOpen.setVisibility(View.VISIBLE);
                holder.imgGo.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(mDataList.get(position).getTitleText())) {
                holder.tvTitleText.setVisibility(View.GONE);
            } else {
                holder.tvTitleText.setVisibility(View.VISIBLE);
                holder.tvTitleText.setText(mDataList.get(position).getTitleText());
            }
            if (position == 2) {
                holder.line.setVisibility(View.GONE);
            } else {
                holder.line.setVisibility(View.VISIBLE);
            }
            holder.civImage.setImageResource(mDataList.get(position).getIcon());
            holder.tvUserId.setText(mDataList.get(position).getName());
            holder.tvCheckResult.setText(mDataList.get(position).getResult());
            switch (mDataList.get(position).getName()) {
                case bigVName:
                    if (mDataList.get(position).isOpen == false) {
                        //审核未通过
                        if (public_wechat_account != null && weibo_account != null) {
                            if (public_wechat_account.getStatus() == 0 && weibo_account.getStatus() == 0) {
                                holder.vIconReject.setVisibility(View.GONE);
                                holder.tvCheckResult.setText(allCheck);
                            } else if (public_wechat_account.getStatus() != 0 && weibo_account.getStatus() == 0) {
                                //微博中认证中+微信审核拒绝
                                holder.vIconReject.setVisibility(View.VISIBLE);
                                holder.tvCheckResult.setText(wechatUnPass);
                            } else if (public_wechat_account.getStatus() == 0 && weibo_account.getStatus() != 0) {
                                //微博审核拒绝+微信认证中
                                holder.vIconReject.setVisibility(View.VISIBLE);
                                holder.tvCheckResult.setText(weiBoUnPass);
                            } else {
                                //两个都不等于0,认证未通过
                                holder.vIconReject.setVisibility(View.VISIBLE);
                                holder.tvCheckResult.setText(allUnPass);
                            }
                        } else if (public_wechat_account != null && weibo_account == null) {
                            if (public_wechat_account.getStatus() == 0) {
                                holder.tvCheckResult.setText(wechatCheck);
                                holder.vIconReject.setVisibility(View.GONE);
                            } else {
                                //微博未开通+微信审核拒绝
                                holder.tvCheckResult.setText(wechatUnPass);
                                holder.vIconReject.setVisibility(View.VISIBLE);
                            }
                        } else if (public_wechat_account == null && weibo_account != null) {
                            if (weibo_account.getStatus() == 0) {
                                holder.tvCheckResult.setText(weiBoCheck);
                                holder.vIconReject.setVisibility(View.GONE);
                            } else {
                                //微博审核拒绝+微信未开通
                                holder.tvCheckResult.setText(weiBoUnPass);
                                holder.vIconReject.setVisibility(View.VISIBLE);
                            }
                        } else {
                            //都未开通
                            holder.tvCheckResult.setText(allUnCheck);
                            holder.vIconReject.setVisibility(View.GONE);
                        }

                    } else {
                        //审核通过
                        if (public_wechat_account != null && weibo_account == null) {
                            holder.tvCheckResult.setText(wechatPass);
                            holder.vIconReject.setVisibility(View.GONE);
                        } else if (public_wechat_account == null && weibo_account != null) {
                            holder.tvCheckResult.setText(weiBoPass);
                            holder.vIconReject.setVisibility(View.GONE);
                        } else if (public_wechat_account != null && weibo_account != null) {
                            if (public_wechat_account.getStatus() == 1 && weibo_account.getStatus() == 1) {
                                holder.tvCheckResult.setText(allPass);
                                holder.vIconReject.setVisibility(View.GONE);
                            } else if (public_wechat_account.getStatus() != 1 && weibo_account.getStatus() == 1) {
                                if (public_wechat_account.getStatus() == - 1) {
                                    holder.vIconReject.setVisibility(View.VISIBLE);
                                } else {
                                    holder.vIconReject.setVisibility(View.GONE);
                                }
                                holder.tvCheckResult.setText(weiBoPass);
                            } else if (public_wechat_account.getStatus() == 1 && weibo_account.getStatus() != 1) {
                                if (weibo_account.getStatus() == - 1) {
                                    holder.vIconReject.setVisibility(View.VISIBLE);
                                } else {
                                    holder.vIconReject.setVisibility(View.GONE);
                                }
                                holder.tvCheckResult.setText(wechatPass);
                            } else {
                                //不存在的情况，已开通情况下，必定有一方状态为1已经通过
                                holder.vIconReject.setVisibility(View.GONE);
                                holder.tvCheckResult.setText(allUnCheck);
                            }

                        }
                    }
                    break;
                case craetorName:
                    if (mDataList.get(position).isOpen == false) {
                        if (creator != null) {
                            if (creator.getStatus() == 0) {
                                holder.tvCheckResult.setText("职业内容创作者认证中");
                                holder.vIconReject.setVisibility(View.GONE);
                            } else if (creator.getStatus() == - 1) {
                                holder.tvCheckResult.setText("职业内容创作者认证未通过");
                                holder.vIconReject.setVisibility(View.VISIBLE);
                            } else {
                                holder.vIconReject.setVisibility(View.GONE);
                            }

                        } else {
                            holder.vIconReject.setVisibility(View.GONE);
                        }
                    } else {
                        holder.vIconReject.setVisibility(View.GONE);
                    }
                    break;
                case publicName:
                    holder.vIconReject.setVisibility(View.GONE);
                    break;

            }
            holder.layoutItem.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (! NetworkUtil.isNetworkAvailable(UserInformationActivity.this)) {
                        CustomToast.showShort(UserInformationActivity.this, getResources().getString(R.string.no_net).toString());
                    } else {
                        switch (mDataList.get(position).getName()) {
                            case publicName:
                                //传送圈子和微信好友个数
                                Intent intentP = new Intent(UserInformationActivity.this, PublicUserMsgActivity.class);
                                intentP.putExtra(PublicUserMsgActivity.PUBLIC, intentInfo);
                                startActivityForResult(intentP, SPConstants.USER_NEW_INFO);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                break;
                            case bigVName:
                                Intent intentB = new Intent(UserInformationActivity.this, UserSelectActivity.class);
                                if (mDataList.get(position).isOpen || public_wechat_account != null || weibo_account != null) {
                                    intentB.putExtra(UserSelectActivity.SELECTPLAT, intentInfo);
                                }
                                startActivityForResult(intentB, SPConstants.USER_NEW_INFO);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                break;
                            case craetorName:
                                Intent intentC = new Intent(UserInformationActivity.this, CreatorMsgActivity.class);
                                if (mDataList.get(position).isOpen || creator != null) {
                                    intentC.putExtra(CreatorMsgActivity.CREATOR, intentInfo);
                                }
                                startActivityForResult(intentC, SPConstants.USER_NEW_INFO);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                break;

                        }
                    }
                }
            });

            return convertView;
        }

    }

    class ViewHolder {
        @Bind(R.id.tv_title_text)
        TextView tvTitleText;
        @Bind(R.id.view_reject_icon)
        View vIconReject;
        @Bind(R.id.civ_image)
        CircleImageView civImage;
        @Bind(R.id.tv_user_id)
        TextView tvUserId;
        @Bind(R.id.tv_check_result)
        TextView tvCheckResult;
        @Bind(R.id.img_go)
        ImageView imgGo;
        @Bind(R.id.tv_open)
        TextView tvOpen;
        @Bind(R.id.layout_item)
        LinearLayout layoutItem;
        @Bind(R.id.line)
        View line;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class UserIdBean {
        public int icon;
        public String titleText;
        public String name;
        public String result;
        public boolean isOpen;

        public UserIdBean(int icon, String titleText, String name, String result, boolean isOpen) {
            this.icon = icon;
            this.titleText = titleText;
            this.name = name;
            this.result = result;
            this.isOpen = isOpen;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public String getTitleText() {
            return titleText;
        }

        public void setTitleText(String titleText) {
            this.titleText = titleText;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public boolean isOpen() {
            return isOpen;
        }

        public void setOpen(boolean open) {
            isOpen = open;
        }
    }

    private void viewSetText(String str, String empty, TextView tv) {
        if (TextUtils.isEmpty(str)) {
            tv.setText(empty);
        } else {
            tv.setText(str);
        }
    }

    @Override
    protected void executeOnclickLeftView() {

    }

    @Override
    protected void executeOnclickRightView() {

    }

    @OnClick({R.id.iv_back, R.id.layout_header, R.id.layout_public, R.id.layout_bigv, R.id.layout_creator, R.id.layout_bind_social})
    public void onViewClicked(View view) {
        if (! NetworkUtil.isNetworkAvailable(UserInformationActivity.this)) {
            CustomToast.showShort(UserInformationActivity.this, getResources().getString(R.string.no_net).toString());
        } else {
            switch (view.getId()) {
                case R.id.iv_back:
                    finish();
                    break;
                case R.id.layout_header:
                    Intent intentShow = new Intent(this, UserInfoShowActivity.class);
                    intentShow.putExtra("base", intentInfo);
                    startActivityForResult(intentShow, SPConstants.USER_NEW_INFO);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
                case R.id.layout_public:
                    break;
                case R.id.layout_bigv:
                    break;
                case R.id.layout_creator:
                    break;
                case R.id.layout_bind_social:
                    if (TextUtils.isEmpty(intentInfo)) {
                        CustomToast.showShort(UserInformationActivity.this, "努力请求网络中，请稍后再试！");
                    } else {
                        Intent intent = new Intent(this, BeKolSecondActivity.class);
                        intent.putExtra("social_accounts", (Serializable) mSocialAccounts);
                        // intent.putExtra("kol_shows", (Serializable) mKolShows);
                        intent.putExtra("kol_id", kolId);
                        startActivityForResult(intent, SPConstants.BE_KOL_BIND_RESULT);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    break;
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPConstants.BE_KOL_BIND_RESULT) {
            checkInfo();
        } else if (requestCode == SPConstants.USER_NEW_INFO) {
            //                if (resultCode==RESULT_OK){
            //                    checkInfo();
            //                }
            checkInfo();
        }
    }

    /**
     更新数据
     */
    private void checkInfo() {
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();
        final BasePresenter mBasePresenter = new BasePresenter();
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.USER_MY_SHOW_URL), null, new RequestCallback() {

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
                parseJson(response);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mWProgressDialog.isShowing()) {
                mWProgressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViewData() {
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
                LogUtil.LogShitou("更新数据", "==>" + response);
                UserCircleBean bean = GsonTools.jsonToBean(response, UserCircleBean.class);
                if (bean != null) {
                    if (bean.getError() == 0) {
                        CacheUtils.putString(UserInformationActivity.this, HelpTools.BASEINFO, response);
                    }
                }
            }
        });
    }

    //       if (userShowBean.getKol().isIs_big_v() == true) {
    //        tvHaveNotFirst.setVisibility(View.GONE);
    //        civBigvImage.setImageResource(R.mipmap.icon_bigv_yes);
    //    } else {
    //        tvHaveNotFirst.setVisibility(View.VISIBLE);
    //        civBigvImage.setImageResource(R.mipmap.icon_bigv_no);
    //    }
    //            if (userShowBean.getKol().isIs_creator() == true) {
    //        tvHaveNotSecond.setVisibility(View.GONE);
    //        civCreatorImage.setImageResource(R.mipmap.icon_creator_yes);
    //    } else {
    //        tvHaveNotSecond.setVisibility(View.VISIBLE);
    //        civCreatorImage.setImageResource(R.mipmap.icon_creator_no);
    //    }
    //            if (userShowBean.getKol().isIs_big_v() == false && userShowBean.getKol().isIs_creator() == false) {
    //        tvHaveNotSecond.setVisibility(View.GONE);
    //    }
}
