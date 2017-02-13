package com.robin8.rb.module.first.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxun.tagcloudlib.view.TagCloudView;
import com.robin8.rb.R;
import com.robin8.rb.activity.LoginActivity;
import com.robin8.rb.activity.WebViewActivity;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.helper.IconFontHelper;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.module.first.adapter.RotateYTransformer;
import com.robin8.rb.module.first.adapter.TextTagsAdapter;
import com.robin8.rb.module.first.model.KolDetailModel;
import com.robin8.rb.module.first.model.SocialAccountsBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.ListUtils;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.view.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Kol详情页面
 */
public class KolDetailContentActivity extends BaseActivity {

    @Bind(R.id.iv_bg)
    ImageView ivBg;
    @Bind(R.id.tv_classification)
    TextView tvClassification;
    @Bind(R.id.tv_kol_name)
    TextView tvKolName;
    @Bind(R.id.tv_kol_introduce)
    TextView tvKolIntroduce;
    @Bind(R.id.tv_bottom)
    TextView tvBottom;

    private final String PUBLIC_WECHAT = "public_wechat";//公众号
    private final String WECHAT = "wechat";//微信
    private final String QQ = "qq";//QQ
    private final String WEIBO = "weibo";//微博
    private final String MEIPAI = "meipai";//美拍
    private final String MIAOPAI = "miaopai";//秒拍
    private final String ZHIHU = "zhihu";//知乎
    private final String DOUYU = "douyu";//斗鱼
    private final String YINGKE = "yingke";//映客
    private final String TIEBA = "tieba";//贴吧
    private final String TIANYA = "tianya";//天涯
    private final String TAOBAO = "taobao";//淘宝
    private final String HUAJIAO = "huajiao";//花椒
    private final String NICE = "nice";//NICE
    private final String DOUBAN = "douban";//豆瓣
    private final String XIAOHONGSHU = "xiaohongshu";//小红书
    private final String YIZHIBO = "yizhibo";//一直播
    private final String MEILA = "meila";//美啦
    private final String OTHER = "other";//其它
    private final String FOLLOW = "follow";
    private final String BACKSLASH = "/";
    @Bind(R.id.tv_favorite)
    TextView tvFavorite;
    @Bind(R.id.tv_kol_back)
    TextView tvKolBack;
    @Bind(R.id.iv_cover)
    ImageView ivCover;
    @Bind(R.id.ll_social_content)
    LinearLayout llSocialContent;
    @Bind(R.id.viewpager)
    ViewPager mViewPager;
    @Bind(R.id.tag_cloud)
    TagCloudView mTagCloudView;
    @Bind(R.id.tv_kol_from)
    TextView tvKolFrom;
    @Bind(R.id.fl_viewpager)
    FrameLayout flViewpager;
    private BasePresenter mBasePresenter;
    private RequestParams mRequestParams;
    private String url;
    private WProgressDialog mWProgressDialog;
    private MyPagerAdapter mAdapter;
    private TextTagsAdapter mTextTagsAdapter;
    private List<String> mTextTagsList = new ArrayList<>();
    private List<KolDetailModel.KolShowsBean> mVPList = new ArrayList<>();
    private int size;
    private KolDetailModel mKolDetailModel;
    private int id;

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.KOL_DETAIL;
        super.onResume();
    }

    @Override
    public void setTitleView() {
        mTVCenter.setText("");
    }

    private void initData() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        url = HelpTools.getUrl(CommonConfig.FIRST_KOL_LIST_URL + BACKSLASH + String.valueOf(id) + BACKSLASH + "detail");
    }

    @Override
    public void initView() {
        mLLTitleBar.setVisibility(View.GONE);
        mViewLine.setVisibility(View.GONE);
        View inflate = LayoutInflater.from(this).inflate(R.layout.activity_kol_detail, mLLContent);
        ButterKnife.bind(this);
        ivBg.post(new Runnable() {
            @Override
            public void run() {
                ivBg.getLayoutParams().height = DensityUtils.getScreenWidth(KolDetailContentActivity.this) * 6 / 5;
                ivCover.getLayoutParams().height = DensityUtils.getScreenWidth(KolDetailContentActivity.this) * 6 / 5;
                ivBg.getLayoutParams().width = DensityUtils.getScreenWidth(KolDetailContentActivity.this);
                ivCover.getLayoutParams().width = DensityUtils.getScreenWidth(KolDetailContentActivity.this);
            }
        });

        tvKolBack.setOnClickListener(this);
        tvFavorite.setOnClickListener(this);
        tvBottom.setOnClickListener(this);

        Typeface iconFont = Typeface.createFromAsset(getAssets(), SPConstants.ICON_FONT_TTF);
        tvFavorite.setTypeface(iconFont);
        tvKolBack.setTypeface(iconFont);

        initData();
        initViewPager();
        initTagCloudView();
        getDataFromNet();
    }


    private void initViewPager() {

        size = mVPList.size();
        mViewPager.setPageMargin(20);//设置page间间距，自行根据需求设置
        mViewPager.setOffscreenPageLimit(3);//>=3
        mViewPager.setPageTransformer(true, new RotateYTransformer());
    }

    private void initTagCloudView() {
        mTextTagsAdapter = new TextTagsAdapter(mTextTagsList);
        mTagCloudView.setAdapter(mTextTagsAdapter);
    }

    public void setThirdLayout(final List<SocialAccountsBean> socialAccounts) {

        if (socialAccounts == null || socialAccounts.size() == 0) {
            return;
        }

        final int size = socialAccounts.size();
        for (int i = 0; i < size; i++) {
            SocialAccountsBean socialAccountsBean = socialAccounts.get(i);
            View view = LayoutInflater.from(this).inflate(R.layout.item_social_detail, null);
            CircleImageView civImage = (CircleImageView) view.findViewById(R.id.civ_image);
            TextView tvKolNick = (TextView) view.findViewById(R.id.tv_kol_nick);
            TextView tvKolFans = (TextView) view.findViewById(R.id.tv_kol_fans);
            TextView tvVideoNumber = (TextView) view.findViewById(R.id.tv_video_number);
            TextView tvArrow = (TextView) view.findViewById(R.id.tv_arrow);
            setFromIv(socialAccountsBean.getProvider(), civImage);
            tvKolNick.setText(socialAccountsBean.getUsername());
            tvKolFans.setText(StringUtil.getNumberFormat(socialAccountsBean.getFollowers_count()));
            tvVideoNumber.setText(String.valueOf(socialAccountsBean.getStatuses_count()));
            IconFontHelper.setTextIconFont(tvArrow, R.string.arrow_right);

            if (!TextUtils.isEmpty(socialAccountsBean.getHomepage())) {
                view.setOnClickListener(new MyOnClickListener(socialAccountsBean));
            } else {
                tvArrow.setVisibility(View.GONE);
            }

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) (60 * BaseApplication.mPixelDensityF));
            llSocialContent.addView(view, lp);
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
            View viewLine = new View(KolDetailContentActivity.this);
            viewLine.setBackgroundResource(R.color.undefined_divide_line);
            llSocialContent.addView(viewLine, lp2);
        }

        llSocialContent.post(new Runnable() {
            @Override
            public void run() {
                llSocialContent.getLayoutParams().height = (int) (60 * BaseApplication.mPixelDensityF * size);
            }
        });
    }

    class MyOnClickListener implements View.OnClickListener {

        private SocialAccountsBean socialAccounts;

        public MyOnClickListener(SocialAccountsBean socialAccounts) {
            this.socialAccounts = socialAccounts;
        }

        @Override
        public void onClick(View v) {
            if (socialAccounts == null) {
                return;
            }
            skipToThirdDetail(socialAccounts.getHomepage(),
                    socialAccounts.getUsername());
        }
    }

    /**
     * 加载网络数据
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

        mBasePresenter.getDataFromServer(true, HttpRequest.GET, url, mRequestParams, new RequestCallback() {
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

    private void parseJson(String json) {
        KolDetailModel kolDetailModel = GsonTools.jsonToBean(json, KolDetailModel.class);
        if (kolDetailModel.getError() == 0) {

            if (tvKolName == null) {
                return;
            }
            mKolDetailModel = kolDetailModel;
            KolDetailModel.BigVBean big_v = kolDetailModel.getBig_v();
            BitmapUtil.loadImage(this, big_v.getAvatar_url(), ivBg, BitmapUtil.getBg());
            tvKolName.setText(big_v.getName());
            if (big_v.getTags() != null && big_v.getTags().size() > 0) {
                tvClassification.setText(big_v.getTags().get(0).getLabel());
            }

            if (!TextUtils.isEmpty(big_v.getDesc())) {
                tvKolIntroduce.setText(big_v.getDesc());
            } else {
                tvKolIntroduce.setText(R.string.no_introduce);
            }

            List<KolDetailModel.KolShowsBean> kol_shows = kolDetailModel.getKol_shows();
            mVPList.addAll(kol_shows);
            size = mVPList.size();
            if (size == 0) {
                flViewpager.setVisibility(View.GONE);
            } else {
                flViewpager.setVisibility(View.VISIBLE);
            }
            mAdapter = new MyPagerAdapter();
            mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
            mViewPager.setAdapter(mAdapter);
            mViewPager.setCurrentItem(10000 * size);
            List<KolDetailModel.KolKeywordsBean> kol_keywords = kolDetailModel.getKol_keywords();
            mTextTagsList.clear();
            for (int i = 0; i < kol_keywords.size(); i++) {
                mTextTagsList.add(kol_keywords.get(i).getKeyword());
            }

            if (mTextTagsList == null || mTextTagsList.size() == 0) {
                mTagCloudView.setVisibility(View.GONE);
            } else {
                mTagCloudView.setVisibility(View.VISIBLE);
            }

            if (mTextTagsList.size() < 6) {
                mTextTagsList.addAll(mTextTagsList);
            }
            mTextTagsAdapter.setDataSet(mTextTagsList);

            List<SocialAccountsBean> social_accounts = kolDetailModel.getSocial_accounts();
            setThirdLayout(social_accounts);
            tvKolFrom.setText(ListUtils.getFromText(social_accounts));
            if (mKolDetailModel.getIs_follow() == 1) {
                tvFavorite.setSelected(true);
                tvFavorite.setText(getString(R.string.icons_favorite_selected));
            } else {
                tvFavorite.setSelected(false);
                tvFavorite.setText(getString(R.string.icons_favorite_unselected));
            }
        }
    }

    private void setFromIv(String provider, CircleImageView civImage) {
        switch (provider) {
            case WECHAT://微信
                civImage.setBackgroundResource(R.mipmap.social_weixin_on);
                break;
            case PUBLIC_WECHAT://公众号
                civImage.setBackgroundResource(R.mipmap.social_gongzhonghao_on);
                break;
            case QQ://QQ
                civImage.setBackgroundResource(R.mipmap.social_qq_on);
                break;
            case WEIBO://微博
                civImage.setBackgroundResource(R.mipmap.social_weibo_on);
                break;
            case MEIPAI://美拍
                civImage.setBackgroundResource(R.mipmap.social_meipai_on);
                break;
            case MIAOPAI://秒拍
                civImage.setBackgroundResource(R.mipmap.social_miaopai_on);
                break;
            case ZHIHU://知乎
                civImage.setBackgroundResource(R.mipmap.social_zhihu_on);
                break;
            case DOUYU://斗鱼
                civImage.setBackgroundResource(R.mipmap.social_douyu_on);
                break;
            case YINGKE://映客
                civImage.setBackgroundResource(R.mipmap.social_yingke_on);
                break;
            case TIEBA://贴吧
                civImage.setBackgroundResource(R.mipmap.social_tieba_on);
                break;
            case TIANYA://天涯
                civImage.setBackgroundResource(R.mipmap.social_tianya_on);
                break;
            case TAOBAO://淘宝
                civImage.setBackgroundResource(R.mipmap.social_taobao_on);
                break;
            case HUAJIAO://花椒
                civImage.setBackgroundResource(R.mipmap.social_huajiao_on);
                break;
            case NICE://NICE
                civImage.setBackgroundResource(R.mipmap.social_nice_on);
                break;
            case DOUBAN://豆瓣
                civImage.setBackgroundResource(R.mipmap.social_douban_on);
                break;
            case XIAOHONGSHU://小红书
                civImage.setBackgroundResource(R.mipmap.social_xiaohongshu_on);
                break;
            case YIZHIBO://一直播
                civImage.setBackgroundResource(R.mipmap.social_yizhibo_on);
                break;
            case MEILA://美啦
                civImage.setBackgroundResource(R.mipmap.social_meila_on);
                break;
            case OTHER://其它
                civImage.setBackgroundResource(R.mipmap.social_others_on);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_kol_back:
                finish();
                break;
            case R.id.tv_favorite:
                favorite();
                break;
            case R.id.tv_bottom:
                skipToInviteCampaign();
                break;
        }
        super.onClick(v);
    }

    private boolean isLogined(int from) {
        if (!BaseApplication.getInstance().hasLogined()) {
            Intent intent = new Intent(this, LoginActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("from", from);
            intent.putExtras(bundle);
            startActivity(intent);
            return false;
        }
        return true;
    }

    private void skipToInviteCampaign() {
        if (!isLogined(0)) {
            return;
        }

        String kolName = null;
        int kolId = 0;
        if (mKolDetailModel != null || mKolDetailModel.getBig_v() != null) {
            kolName = mKolDetailModel.getBig_v().getName();
            kolId = mKolDetailModel.getBig_v().getId();
        }
        Intent intent = new Intent(this, BusinessCooperationActivity.class);
        intent.putExtra("kol_name", kolName);
        intent.putExtra("kol_id", kolId);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void skipToThirdDetail(String link, String desc) {
        if (link == null) {
            return;
        }
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("title", desc);
        intent.putExtra("url", link);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * 收藏or取消收藏
     */
    private void favorite() {

        if (!isLogined(0)) {
            return;
        }

        if (tvFavorite.isSelected()) {
            tvFavorite.setSelected(false);
            tvFavorite.setText(getString(R.string.icons_favorite_unselected));
        } else {
            tvFavorite.setSelected(true);
            tvFavorite.setText(getString(R.string.icons_favorite_selected));
        }
        String favoriteUrl = HelpTools.getUrl(CommonConfig.FIRST_KOL_LIST_URL + BACKSLASH + String.valueOf(id) + BACKSLASH + FOLLOW);
        mBasePresenter.getDataFromServer(true, HttpRequest.POST, favoriteUrl, null, null);
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
    }

    @Override
    protected void onDestroy() {
        if (mWProgressDialog != null) {
            mWProgressDialog.dismiss();
            mWProgressDialog = null;
        }

        if (mBasePresenter != null) {
            mBasePresenter = null;
        }
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            ImageView imageView = new ImageView(KolDetailContentActivity.this);
            imageView.setBackgroundResource(R.color.light_gray_custom);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            if (size != 0) {
                String url = mVPList.get(position % size).getCover_url();
                if (!TextUtils.isEmpty(url)) {
                    BitmapUtil.loadImage(KolDetailContentActivity.this, url, imageView);
                }
            }

            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            container.addView(imageView, lp);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (size <= 0) {
                        return;
                    }
                    KolDetailModel.KolShowsBean kolShowsBean = mVPList.get(position % size);
                    if (kolShowsBean != null) {
                        skipToThirdDetail(kolShowsBean.getLink(), kolShowsBean.getDesc());
                    }
                }
            });
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE / 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
