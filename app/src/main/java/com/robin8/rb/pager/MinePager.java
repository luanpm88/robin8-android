package com.robin8.rb.pager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.activity.ADHostActivity;
import com.robin8.rb.activity.LoginActivity;
import com.robin8.rb.activity.WalletActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.BasePager;
import com.robin8.rb.base.BaseRecyclerViewActivity;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.helper.IconFontHelper;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.model.LoginBean;
import com.robin8.rb.model.NotifyMsgEntity;
import com.robin8.rb.module.create.activity.FragmentsActivity;
import com.robin8.rb.module.first.activity.SearchKolActivity;
import com.robin8.rb.module.mine.activity.BeKolFirstActivity;
import com.robin8.rb.module.mine.activity.HelpCenterActivity;
import com.robin8.rb.module.mine.activity.InviteFriendsActivity;
import com.robin8.rb.module.mine.activity.SettingActivity;
import com.robin8.rb.module.mine.activity.UserSignActivity;
import com.robin8.rb.module.mine.model.MineShowModel;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.util.UIUtils;
import com.robin8.rb.view.widget.CustomDialogManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * 新闻界面 NewsPager
 *
 * @author Figo
 */
public class MinePager extends BasePager implements View.OnClickListener, Observer {

    private static final int MY_WALLET = 1;
    private static final int MY_CAMPAIGN = 2;
    private static final int MY_PRODUCT = 3;
    private static final int MY_CARE = 4;
    private static final int AD_HOST = 5;
    private static final int ROBIN_INDIANA = 6;
    private static final int SIGN = 7;
    private static final int INVITE_FRIENDS = 8;
    private static final int HELP_CENTER = 9;

    private static final String ROLE_BIG_V = "big_v";
    private static final String ROLE_PUBLIC = "public";
    private static final String STATE_PENDING = "pending";//pending,applying,passed,rejected
    private static final String STATE_APPLYING = "applying";//pending,applying,passed,rejected
    private static final String STATE_PASSED = "passed";//pending,applying,passed,rejected
    private static final String STATE_REJECTED = "rejected";//pending,applying,passed,rejected

    private View mPager;
    private ListView mLVMineList;
    private TextView mClickNumberTv;
    private TextView mTotalNumberTv;
    private TextView mMaxNumberTv;
    private TextView mAverageNumberTv;
    private MineListAdapter mMineListAdapter;
    private ArrayList<ItemBean> mItemList;
    private TextView mMineMessageTv;
    private TextView mMineSettingTv;
    private View mHeader;
    private View mTitleBar;
    private int mTop;
    private ImageView mCIVImage;
    private TextView mUserNameTv;
    private TextView mUserTagTv;
    private ImageView mKolCertificationIv;
    private TextView mApplyTv;
    private MineShowModel.KolBean mKolBean;
    private View mKolItemLL;

    public MinePager(FragmentActivity activity) {
        this.mActivity = activity;
        initDataList();
        rootView = initView();
        NotifyManager.getNotifyManager().addObserver(this);
    }

    private void initDataList() {
        String[] arrayTitle = mActivity.getResources().getStringArray(R.array.mine_list_title);
        String[] arrayId = mActivity.getResources().getStringArray(R.array.mine_list_icons);
        if (mItemList == null) {
            mItemList = new ArrayList<ItemBean>();
        } else {
            mItemList.clear();
        }
        ItemBean item = null;
        for (int i = 0; i < arrayTitle.length; i++) {
            item = new ItemBean(i, arrayId[i], arrayTitle[i]);
            mItemList.add(item);
        }
        mItemList.add(0, null);
    }

    @Override
    public void initTitleBar() {
        mTitleBar = mPager.findViewById(R.id.rl_title);
        mMineMessageTv = (TextView) mTitleBar.findViewById(R.id.mine_message_tv);
        mMineSettingTv = (TextView) mTitleBar.findViewById(R.id.mine_setting_tv);
        IconFontHelper.setTextIconFont(mMineSettingTv, R.string.icons_setting);
        IconFontHelper.setTextIconFont(mMineMessageTv, R.string.icons_message);
        mMineMessageTv.setOnClickListener(this);
        mMineSettingTv.setOnClickListener(this);
    }

    @Override
    public View initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity.getApplicationContext());
        mPager = layoutInflater.inflate(R.layout.pager_mine, null);
        initTitleBar();
        initListView();
        return mPager;
    }

    private void initListView() {
        mLVMineList = (ListView) mPager.findViewById(R.id.lv_mine_list);
        mMineListAdapter = new MineListAdapter();
        mLVMineList.setAdapter(mMineListAdapter);
        MineListOnItemClickListener mMineListOnItemClickListener = new MineListOnItemClickListener();
        mLVMineList.setOnItemClickListener(mMineListOnItemClickListener);
        mLVMineList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mHeader != null) {
                    mTop = mHeader.getTop();
                    if (Math.abs(mTop) > BaseApplication.mPixelDensityF * 40) {
                        mTitleBar.setBackgroundResource(R.color.cover_transparent);
                    } else {
                        mTitleBar.setBackgroundResource(android.R.color.transparent);
                    }
                }
            }
        });
    }

    /**
     * 初始化界面
     *
     * @param view
     */
    private void initView(final View view) {
        mHeader = view;
        Context context = view.getContext();
        View layoutClickNumber = view.findViewById(R.id.layout_click_number);
        mClickNumberTv = (TextView) layoutClickNumber.findViewById(R.id.tv_item_number);
        TextView clickNameTv = (TextView) layoutClickNumber.findViewById(R.id.tv_item_name);
        clickNameTv.setText(context.getString(R.string.max_click_number));

        View layoutTotalIncome = view.findViewById(R.id.layout_total_income);
        mTotalNumberTv = (TextView) layoutTotalIncome.findViewById(R.id.tv_item_number);
        TextView totalNameTv = (TextView) layoutTotalIncome.findViewById(R.id.tv_item_name);
        totalNameTv.setText(context.getString(R.string.history_total_income));

        View layoutMaxIncome = view.findViewById(R.id.layout_max_income);
        mMaxNumberTv = (TextView) layoutMaxIncome.findViewById(R.id.tv_item_number);
        TextView maxNameTv = (TextView) layoutMaxIncome.findViewById(R.id.tv_item_name);
        maxNameTv.setText(context.getString(R.string.max_per_income));

        View layoutAverageNumber = view.findViewById(R.id.layout_average_number);
        mAverageNumberTv = (TextView) layoutAverageNumber.findViewById(R.id.tv_item_number);
        TextView averageNameTv = (TextView) layoutAverageNumber.findViewById(R.id.tv_item_name);
        averageNameTv.setText(context.getString(R.string.average_income));

        mCIVImage = (ImageView) view.findViewById(R.id.civ_image);
        mUserNameTv = (TextView) view.findViewById(R.id.tv_user_name);
        mUserTagTv = (TextView) view.findViewById(R.id.tv_user_tag);
        mApplyTv = (TextView) view.findViewById(R.id.tv_apply);
        mKolCertificationIv = (ImageView) view.findViewById(R.id.iv_kol_certification);
        mKolItemLL = view.findViewById(R.id.ll_kol_item);
        mKolItemLL.setOnClickListener(this);
    }

    private void updateView(MineShowModel.KolBean kol) {
        if (kol == null || mClickNumberTv == null) {
            return;
        }

        LoginBean loginBean = BaseApplication.getInstance().getLoginBean();
        LoginBean.KolEntity kolEntity = loginBean.getKol();
        kolEntity.setRole_apply_status(kol.getRole_apply_status());
        kolEntity.setRole_check_remark(kol.getRole_check_remark());
        loginBean.setKol(kolEntity);
        BaseApplication.getInstance().setLoginBean(loginBean);

        mClickNumberTv.setText(String.valueOf(kol.getMax_campaign_click()));
        mTotalNumberTv.setText(StringUtil.deleteZero(kol.getCampaign_total_income()));
        mMaxNumberTv.setText(StringUtil.deleteZero(kol.getMax_campaign_earn_money()));
        mAverageNumberTv.setText(StringUtil.deleteZero(kol.getAvg_campaign_credit()));

        mUserNameTv.setText(kol.getName());
        mUserTagTv.setText(getTags(kol.getTags()));
        BitmapUtil.loadImage(mActivity.getApplicationContext(), kol.getAvatar_url(), mCIVImage);
        setApplyTvState(kol.getRole_apply_status(), kol.getKol_role());
    }

    /**
     * 设置Kol审核以及认证状态
     *
     * @param state
     * @param role
     */
    private void setApplyTvState(String state, String role) {
        if (TextUtils.isEmpty(state) || TextUtils.isEmpty(role)) {
            mKolCertificationIv.setBackgroundResource(R.mipmap.icon_kol_uncertification);
            mApplyTv.setText(R.string.be_kol);
            return;
        }

        switch (state) {
            case STATE_PENDING:
                mKolCertificationIv.setBackgroundResource(R.mipmap.icon_kol_uncertification);
                mApplyTv.setText(R.string.be_kol);
                mApplyTv.setTextColor(UIUtils.getColor(R.color.mine_yellow_custom));
                mApplyTv.setBackgroundResource(R.drawable.shape_bg_yellow_pane);
                break;
            case STATE_APPLYING:
                mKolCertificationIv.setBackgroundResource(R.mipmap.icon_kol_uncertification);
                if (!role.equals(ROLE_BIG_V)) {
                    mApplyTv.setText(R.string.data_reviewing);
                    mApplyTv.setTextColor(UIUtils.getColor(R.color.mine_yellow_custom));
                    mApplyTv.setBackgroundResource(R.drawable.shape_bg_yellow_pane);
                } else {
                    mApplyTv.setText(R.string.data_reviewing_again);
                    mApplyTv.setTextColor(UIUtils.getColor(R.color.mine_yellow_custom));
                    mApplyTv.setBackgroundResource(R.drawable.shape_bg_yellow_pane);
                }
                break;
            case STATE_PASSED:
                if (role.equals(ROLE_BIG_V)) {
                    mKolCertificationIv.setBackgroundResource(R.mipmap.icon_kol_certification);
                    mApplyTv.setText(R.string.edit_kol_data);
                    mApplyTv.setTextColor(UIUtils.getColor(R.color.mine_yellow_custom));
                    mApplyTv.setBackgroundResource(R.drawable.shape_bg_yellow_pane);
                }
                break;
            case STATE_REJECTED:
                mKolCertificationIv.setBackgroundResource(R.mipmap.icon_kol_uncertification);
                mApplyTv.setText(R.string.data_rejected);
                mApplyTv.setTextColor(UIUtils.getColor(R.color.red_custom));
                mApplyTv.setBackgroundResource(R.drawable.shape_bg_red_pane);
                break;
            default:
                mKolCertificationIv.setBackgroundResource(R.mipmap.icon_kol_uncertification);
                mApplyTv.setText(R.string.be_kol);
                mApplyTv.setTextColor(UIUtils.getColor(R.color.mine_yellow_custom));
                mApplyTv.setBackgroundResource(R.drawable.shape_bg_yellow_pane);
                break;
        }
    }

    private String getTags(List<MineShowModel.KolBean.TagsBean> tags) {
        if (tags == null || tags.size() <= 0) {
            return mActivity.getString(R.string.be_kol_get_more_info);
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < tags.size(); i++) {
            sb.append(tags.get(i).getLabel()).append("/");
        }

        if (sb.length() < 1) {
            return mActivity.getString(R.string.be_kol_get_more_info);
        }
        return sb.substring(0, sb.length() - 1);
    }

    @Override
    public void initData() {

        if (!BaseApplication.getInstance().hasLogined()) {
            initViewWithoutLogin();
            return;
        }

        String mMineData = CacheUtils.getString(mActivity, SPConstants.MINE_DATA, null);
        parseJson(mMineData);

        BasePresenter mBasePresenter = new BasePresenter();
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.MY_SHOW_URL), null, new RequestCallback() {
            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onResponse(String response) {
                parseJson(response);
            }
        });
    }

    private void parseJson(String response) {
        MineShowModel mineShowModel = GsonTools.jsonToBean(response, MineShowModel.class);
        if (mineShowModel != null && mineShowModel.getError() == 0) {
            CacheUtils.putString(mActivity, SPConstants.MINE_DATA, response);
            mKolBean = mineShowModel.getKol();
            updateView(mKolBean);
        }
    }

    private void initViewWithoutLogin() {
        if (mCIVImage == null) {
            return;
        }
        mCIVImage.setBackgroundResource(R.mipmap.icon_user_default);
        mUserNameTv.setText(mActivity.getString(R.string.click_login));
        mUserTagTv.setText(mActivity.getString(R.string.login_get_more_campaign));
        mKolCertificationIv.setBackgroundResource(R.mipmap.icon_kol_uncertification);
        mApplyTv.setText(mActivity.getString(R.string.be_kol));
        mClickNumberTv.setText(String.valueOf(0));
        mTotalNumberTv.setText(String.valueOf(0));
        mMaxNumberTv.setText(String.valueOf(0));
        mAverageNumberTv.setText(String.valueOf(0));
    }

    @Override
    public void onClick(View v) {
        if (isDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.ll_kol_item:
                if (mKolBean != null && STATE_REJECTED.equals(mKolBean.getRole_apply_status())) {
                    showRejectedDialog();
                    return;
                }
                skipToBeKol();
                break;
            case R.id.mine_message_tv:
                skipToMessage();
                break;
            case R.id.mine_setting_tv:
                skipToSetting();
                break;
        }
    }

    private void skipToMessage() {
        if (isLogined(SPConstants.SETTING_ACTIVITY)) {
            Intent intent = new Intent(mActivity, BaseRecyclerViewActivity.class);
            intent.putExtra("destination", SPConstants.MESSAGE_ACTIVITY);
            intent.putExtra("url", HelpTools.getUrl(CommonConfig.MESSAGES_URL));
            intent.putExtra("title", mActivity.getString(R.string.message));
            mActivity.startActivityForResult(intent, 0);
        }
    }

    private void skipToSetting() {
        if (isLogined(SPConstants.SETTING_ACTIVITY)) {
            Intent intent = new Intent(mActivity, SettingActivity.class);
            mActivity.startActivity(intent);
        }
    }

    private void skipToBeKol() {
        if (isLogined(SPConstants.BE_KOL_ACTIVITY)) {
            Intent intent = new Intent(mActivity, BeKolFirstActivity.class);
            intent.putExtra("id", mKolBean.getId());
            mActivity.startActivity(intent);
        }
    }

    private void showRejectedDialog() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_reject_kol, null);
        TextView bekolTV = (TextView) view.findViewById(R.id.tv_be_kol);
        TextView reasonTV = (TextView) view.findViewById(R.id.tv_reason);
        TextView cancelTV = (TextView) view.findViewById(R.id.tv_cancel);
        final CustomDialogManager cdm = new CustomDialogManager(mActivity, view);
        reasonTV.setText(mKolBean.getRole_check_remark());
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cdm.dismiss();
            }
        });

        bekolTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipToBeKol();
                cdm.dismiss();
            }
        });

        cdm.dg.setCanceledOnTouchOutside(true);
        cdm.dg.getWindow().setGravity(Gravity.CENTER);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        cdm.showDialog();
    }

    private void skipToAdHost() {
        if (isLogined(SPConstants.AD_HOST_ACTIVITY)) {
            Intent intent = new Intent(mActivity, ADHostActivity.class);
            mActivity.startActivity(intent);
        }
    }

    private boolean isLogined(int from) {
        if (!BaseApplication.getInstance().hasLogined()) {
            Intent intent = new Intent(mActivity, LoginActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("from", from);
            intent.putExtras(bundle);
            mActivity.startActivityForResult(intent, SPConstants.MAIN_TO_LOGIN);
            return false;
        }
        return true;
    }

    private void skipToWallet() {
        if (isLogined(SPConstants.WALLETACTIVIRY)) {
            Intent intent = new Intent(mActivity, WalletActivity.class);
            mActivity.startActivity(intent);
        }
    }

    @Override
    public void update(Observable observable, Object data) {

        if (data instanceof NotifyMsgEntity) {
            NotifyMsgEntity msgEntity = (NotifyMsgEntity) data;
            int type = msgEntity.getCode();
            switch (type) {
                case NotifyManager.TYPE_LOGIN:
                case NotifyManager.TYPE_LOGIN_OUT:
                case NotifyManager.TYPE_REFRESH_PROFILE:
                    initData();
                    break;
            }
        }
    }

    class MineListOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (isDoubleClick()) {
                return;
            }
            switch (position) {
                case MY_WALLET:
                    skipToWallet();
                    break;
                case MY_CAMPAIGN:
                    skipToCampaign();
                    break;
                case MY_PRODUCT:
                    skipToProduct();
                    break;
                case MY_CARE:
                    skipToMyCare();
                    break;
                case INVITE_FRIENDS:
                    skipToInViteFriends();
                    break;
                case AD_HOST:
                    skipToAdHost();
                    break;
                case SIGN:
                    skipToSign();
                    break;
                case ROBIN_INDIANA:
                    skipToRobinIndiana();
                    break;
                case HELP_CENTER:
                    skipToHelpCenter();
                    break;
            }
        }
    }

    private void skipToProduct() {
        Intent intent = new Intent();
        intent.setClass(mActivity, FragmentsActivity.class);
        String nameArr[] = {"我的分享", "我的产品", "待审核", "审核拒绝"};//待审核、审核通过、审核拒绝, 我的分享
        String campaignTypeArr[] = {"shares", "passed", "pending", "rejected"};//'pending' , 'passed','rejected', 'shares'
        Bundle bundle = new Bundle();
        bundle.putStringArray("name", nameArr);
        bundle.putStringArray("type", campaignTypeArr);
        bundle.putString("page_name", StatisticsAgency.MY_CREATE);
        bundle.putString("title_name", mActivity.getString(R.string.my_create));
        bundle.putString("url", HelpTools.getUrl(CommonConfig.MY_CREATE_URL));
        intent.putExtras(bundle);
        intent.setClass(mActivity, FragmentsActivity.class);
        mActivity.startActivity(intent);
    }

    private void skipToHelpCenter() {
        Intent intent = new Intent(mActivity, HelpCenterActivity.class);
        mActivity.startActivity(intent);
    }

    private void skipToInViteFriends() {
        if (isLogined(SPConstants.INVITE_FRIENDS_ACTIVITY)) {
            Intent intent = new Intent(mActivity, InviteFriendsActivity.class);
            intent.putExtra("from", SPConstants.MY_CARE);
            intent.putExtra("url", CommonConfig.MY_CARE_URL);
            intent.putExtra("tag_name_cn", mActivity.getString(R.string.my_concern));
            mActivity.startActivity(intent);
        }
    }

    private void skipToMyCare() {
        if (isLogined(SPConstants.MY_CAMPAIGN_ACTIVITY)) {
            Intent intent = new Intent(mActivity, SearchKolActivity.class);
            intent.putExtra("from", SPConstants.MY_CARE);
            intent.putExtra("url", CommonConfig.MY_CARE_URL);
            intent.putExtra("tag_name_cn", mActivity.getString(R.string.my_concern));
            mActivity.startActivity(intent);
        }
    }

    private void skipToCampaign() {
        if (isLogined(SPConstants.MY_CAMPAIGN_ACTIVITY)) {
            String nameArr[] = {"进行中", "待上传", "审核中", "已完成"};
            String campaignTypeArr[] = {"approved", "waiting_upload", "verifying", "completed"};
            Intent intent = new Intent(mActivity, FragmentsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArray("name",nameArr);
            bundle.putStringArray("type",campaignTypeArr);
            bundle.putString("page_name", StatisticsAgency.MY_TASK);
            bundle.putString("title_name", mActivity.getString(R.string.my_capaign));
            bundle.putString("url",HelpTools.getUrl(CommonConfig.CAMPAIGN_INVITES_URL));
            intent.putExtras(bundle);
            mActivity.startActivity(intent);
        }


    }

    private void skipToSign() {
        if (isLogined(SPConstants.USER_SIGN_ACTIVITY)) {
            Intent intent = new Intent(mActivity, UserSignActivity.class);
            mActivity.startActivity(intent);
        }
    }

    private void skipToRobinIndiana() {
        if (isLogined(SPConstants.ROBININDIANA)) {
            Intent intent = new Intent(mActivity, BaseRecyclerViewActivity.class);
            intent.putExtra("destination", SPConstants.INDIANA_ROBIN);
            intent.putExtra("url", HelpTools.getUrl(CommonConfig.LOTTERY_ACTIVITIES_URL));
            intent.putExtra("title", mActivity.getString(R.string.robin_indiana));
            mActivity.startActivity(intent);
        }
    }

    class MineListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mItemList == null ? 0 : mItemList.size();
        }

        @Override
        public ItemBean getItem(int position) {
            return mItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemBean item = getItem(position);
            if (item != null) {
                Holder holder = null;
                holder = new Holder();
                convertView = View.inflate(mActivity.getApplicationContext(), R.layout.mine_item_width_match, null);
                holder.mTVArrow = (TextView) convertView.findViewById(R.id.tv_arrow);
                holder.mTVItemIcon = (TextView) convertView.findViewById(R.id.tv_item_icon);
                holder.mTVItemTitle = (TextView) convertView.findViewById(R.id.tv_item_title);
                holder.lineTop = convertView.findViewById(R.id.line_top);
                holder.viewUp = convertView.findViewById(R.id.view_up);
                holder.lineUp = convertView.findViewById(R.id.line_up);
                holder.lineDown = convertView.findViewById(R.id.line_down);
                switch (item.id) {
                    case 0:
                    case 5:
                        setLines(holder, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE);
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 6:
                        setLines(holder, View.GONE, View.GONE, View.GONE, View.VISIBLE);
                        break;
                    case 7:
                        setLines(holder, View.GONE, View.GONE, View.GONE, View.GONE);
                        break;
                    case 8:
                        setLines(holder, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE);
                        break;
                }

                IconFontHelper.setTextIconFont(holder.mTVArrow, R.string.arrow_right);
                IconFontHelper.setTextIconFont(holder.mTVItemIcon, item.icons);
                holder.mTVItemTitle.setText(item.name);
            } else {
                convertView = View.inflate(mActivity.getApplicationContext(), R.layout.mine_item_header, null);
                initView(convertView);
            }
            return convertView;
        }

        private void setLines(Holder holder, int visible, int visible1, int visible2, int visible3) {
            holder.lineTop.setVisibility(visible);
            holder.viewUp.setVisibility(visible1);
            holder.lineUp.setVisibility(visible2);
            holder.lineDown.setVisibility(visible3);
        }
    }

    static class Holder {
        public TextView mTVArrow;
        public TextView mTVItemIcon;
        public TextView mTVItemTitle;
        public View viewUp;
        public View lineUp;
        public View lineDown;
        public View lineTop;
    }

    public class ItemBean {
        public int id;
        public String name;
        public String icons;

        public ItemBean(int id, String icons, String name) {
            this.id = id;
            this.icons = icons;
            this.name = name;
        }
    }
}
