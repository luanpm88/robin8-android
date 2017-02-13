package com.robin8.rb.module.mine.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.util.FileUtils;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.ListUtils;
import com.robin8.rb.view.widget.CustomDialogManager;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 成为KOL
 */
public class BeKolFirstActivity extends BaseActivity {
    private static final int ITEM_HEADER = 0;//头部
    private static final int ITEM_NAME = 1;//昵称
    private static final int ITEM_GENDER = 2;//性别
    private static final int ITEM_AGE = 3;//年龄
    private static final int ITEM_JOB = 4;//职业
    private static final int ITEM_INTEREST = 5;//兴趣
    private static final int ITEM_DESC = 6;//简介
    private static final int ITEM_PIC = 7;//图片

    private static final int TYPE_HEADER = 0;//简介
    private static final int TYPE_NORMAL = 1;//简介
    private static final int TYPE_DESC = 2;//简介
    private static final int TYPE_PIC = 3;//简介
    private static final int UNKNOWN = 0;
    private static final int MALE = 1;
    private static final int FEMALE = 2;


    private final String BACKSLASH = "/";
    @Bind(R.id.lv_list)
    ListView lvList;
    private BasePresenter mBasePresenter;
    private WProgressDialog mWProgressDialog;
    private String url;
    private int id;
    private RequestParams mRequestParams;
    private String[] mStrArr;
    private KolDetailModel.BigVBean mBigVBean;
    private List<FirstBeKolItem> mDataList = new ArrayList<>();
    private MyListAdapter mMyListAdapter;
    private List<SocialAccountsBean> mSocialAccounts;
    private ImageView mImageView;
    private boolean mImageLoadB;
    private Uri mImageUri;
    private String mFinalPicturePath;
    private List<KolDetailModel.KolShowsBean> mKolShows;
    private View mLLContentlv;
    private Bitmap mBitmap;

    @Override
    public void setTitleView() {
        mTVCenter.setText(this.getText(R.string.be_kol));
    }

    @Override
    public void initView() {
        mBottomTv.setVisibility(View.VISIBLE);
        mBottomTv.setText(getString(R.string.next));
        View view = LayoutInflater.from(this).inflate(R.layout.activity_be_kol_first, mLLContent, true);
        ButterKnife.bind(this);
        initData();
        mBottomTv.setOnClickListener(this);
        mMyListAdapter = new MyListAdapter();
        lvList.setAdapter(mMyListAdapter);
    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.KOL_APPLY_INFO;
        super.onResume();
    }

    private void initData() {
        NotifyManager.getNotifyManager().addObserver(this);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        url = HelpTools.getUrl(CommonConfig.FIRST_KOL_LIST_URL + BACKSLASH + String.valueOf(id) + BACKSLASH + "detail");
        mStrArr = getResources().getStringArray(R.array.be_kol_first);
        updateData();
        getDataFromNet();
    }

    /**
     * 获取初始信息
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
        if (kolDetailModel != null && kolDetailModel.getError() == 0) {
            mBigVBean = kolDetailModel.getBig_v();
            mSocialAccounts = kolDetailModel.getSocial_accounts();
            mKolShows = kolDetailModel.getKol_shows();
            updateData();
            mMyListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Adapter数据刷新
     */
    private void updateData() {
        mDataList.clear();
        if (mBigVBean == null) {
            for (int i = 0; i < mStrArr.length; i++) {
                FirstBeKolItem item = new FirstBeKolItem(mStrArr[i]);
                mDataList.add(item);
            }
            return;
        } else {
            for (int i = 0; i < mStrArr.length; i++) {
                FirstBeKolItem item = new FirstBeKolItem(mStrArr[i]);
                switch (i) {
                    case ITEM_NAME:
                        item.content = mBigVBean.getName();
                        break;
                    case ITEM_GENDER:
                        switch (mBigVBean.getGender()) {
                            case UNKNOWN:
                                item.content = getString(R.string.unknown);
                                break;
                            case MALE:
                                item.content = getString(R.string.male);
                                break;
                            case FEMALE:
                                item.content = getString(R.string.female);
                                break;
                        }
                        break;
                    case ITEM_AGE:
                        item.content = String.valueOf(mBigVBean.getAge());
                        break;
                    case ITEM_JOB:
                        item.content = mBigVBean.getJob_info();
                        break;
                    case ITEM_INTEREST:
                        item.content = getTags(mBigVBean.getTags());
                        break;
                    case ITEM_DESC:
                        item.content = mBigVBean.getDesc();
                        break;
                    case ITEM_PIC:
                        item.content = mBigVBean.getAvatar_url();
                        break;
                }
                mDataList.add(item);
            }
        }
    }

    private String getTags(List<KolDetailModel.BigVBean.TagsBean> tags) {
        if (tags == null || tags.size() == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < tags.size(); i++) {
            sb.append(tags.get(i).getLabel()).append(",");
        }
        return sb.substring(0, sb.length());
    }

    /**
     * 提交
     */
    private void submit() {

        if (!checkInfoCompelete()) {
            return;
        }

        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();

        LinkedHashMap<String, Object> requestMap = new LinkedHashMap<>();
        requestMap.put("name", mDataList.get(ITEM_NAME).content);
        requestMap.put("app_city", CacheUtils.getString(this, SPConstants.LOCATION_CITY, ""));
        requestMap.put("job_info", mDataList.get(ITEM_JOB).content);
        requestMap.put("tag_names", getTagNames(mDataList.get(ITEM_INTEREST).content));
        requestMap.put("desc", mDataList.get(ITEM_DESC).content);
        requestMap.put("age", mDataList.get(ITEM_AGE).content);


        String gender = "全部";
        if (getString(R.string.male).equals(mDataList.get(ITEM_GENDER).content)){
            gender = "1";
        }else if(getString(R.string.female).equals(mDataList.get(ITEM_GENDER).content)){
            gender = "2";
        }

        requestMap.put("gender", gender);


//        String gender = "0";
//        if (getString(R.string.male).equals(mDataList.get(ITEM_GENDER).content)){
//            gender = "1";
//        }else if(getString(R.string.female).equals(mTVInfoSex.getText())){
//            gender = "2";
//        }
//        requestMap.put("gender", gender);
        String imageName = null;
        File file = null;
        if (!TextUtils.isEmpty(mFinalPicturePath)) {
            imageName = mFinalPicturePath.substring(mFinalPicturePath.lastIndexOf("/") + 1);
            file = new File(mFinalPicturePath);
        }

        HttpRequest.getInstance().post(true, HelpTools.getUrl(CommonConfig.BIG_V_APPLY_FIRST_URL), "avatar",
                imageName, file, requestMap, new RequestCallback() {
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

                        if (!TextUtils.isEmpty(mFinalPicturePath)) {
                            BitmapUtil.deleteBm(mFinalPicturePath);
                        }

                        BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);
                        if (bean != null && bean.getError() == 0) {
                            skipToNext();
                        }
                    }
                });
    }

    private boolean checkInfoCompelete() {
        if (TextUtils.isEmpty(mDataList.get(ITEM_NAME).content)) {
            CustomToast.showShort(this, getString(R.string.please_write_nick_name));
            return false;
        }

        if (TextUtils.isEmpty(mDataList.get(ITEM_AGE).content)) {
            CustomToast.showShort(this, getString(R.string.please_write_age));
            return false;
        }

        if (!getString(R.string.male).equals(mDataList.get(ITEM_GENDER).content) && !getString(R.string.female).equals(mDataList.get(ITEM_GENDER).content)) {
            CustomToast.showShort(this, getString(R.string.please_write_sex));
            return false;
        }

        if (TextUtils.isEmpty(mDataList.get(ITEM_JOB).content)) {
            CustomToast.showShort(this, getString(R.string.please_write_job));
            return false;
        }

        if (TextUtils.isEmpty(mDataList.get(ITEM_INTEREST).content)) {
            CustomToast.showShort(this, getString(R.string.please_write_interest));
            return false;
        }

        if (TextUtils.isEmpty(mDataList.get(ITEM_DESC).content)) {
            CustomToast.showShort(this, getString(R.string.please_write_desc));
            return false;
        }

        if (TextUtils.isEmpty(mDataList.get(ITEM_PIC).content) && TextUtils.isEmpty(mFinalPicturePath)) {
            CustomToast.showShort(this, getString(R.string.please_write_pic));
            return false;
        }
        return true;
    }

    private int getGenderInt(String content) {
        if (getString(R.string.male).equals(content)) {
            return 1;
        } else if (getString(R.string.female).equals(content)) {
            return 2;
        }
        return 0;
    }

    private String getTagNames(String content) {
        String[] interestArr = content.split(",");
        String[] enArray = getResources().getStringArray(R.array.first_page_tag_item_total_en);
        String[] chArray = getResources().getStringArray(R.array.first_page_tag_item_total);
        List<String> chList = Arrays.asList(chArray);

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < interestArr.length; i++) {
            int index = ListUtils.getIndex(interestArr[i], chList);
            if (index != -1 && index < enArray.length) {
                sb.append(enArray[index]).append(",");
            }
        }
        if (sb.length() < 1) {
            return null;
        }
        return sb.substring(0, sb.length() - 1);
    }

    @Override
    protected void onDestroy() {
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
        NotifyManager.getNotifyManager().deleteObserver(this);
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
                submit();
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
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
            switch (position) {
                case ITEM_HEADER:
                    return TYPE_HEADER;
                case ITEM_DESC:
                    return TYPE_DESC;
                case ITEM_PIC:
                    return TYPE_PIC;
                default:
                    return TYPE_NORMAL;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 4;
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
                    convertView = LayoutInflater.from(BeKolFirstActivity.this).inflate(R.layout.item_be_kol_header, null);
                    final View viewHeader = convertView.findViewById(R.id.view_header);
                    viewHeader.setBackgroundResource(R.mipmap.pic_kol_step_0);
                    viewHeader.post(new Runnable() {
                        @Override
                        public void run() {
                            viewHeader.getLayoutParams().height = DensityUtils.getScreenWidth(viewHeader.getContext()) * 58 / 700;
                        }
                    });
                    break;
                case TYPE_NORMAL:
                    convertView = LayoutInflater.from(BeKolFirstActivity.this).inflate(R.layout.item_be_kol_normal, null);
                    TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
                    TextView tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                    TextView tvArrow = (TextView) convertView.findViewById(R.id.tv_arrow);
                    tvName.setText(item.name);
                    tvContent.setText(item.content);
                    IconFontHelper.setTextIconFont(tvArrow, R.string.arrow_right);
                    break;
                case TYPE_DESC:
                    convertView = LayoutInflater.from(BeKolFirstActivity.this).inflate(R.layout.item_be_kol_desc, null);
                    tvName = (TextView) convertView.findViewById(R.id.tv_name);
                    tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                    tvArrow = (TextView) convertView.findViewById(R.id.tv_arrow);
                    tvName.setText(item.name);
                    tvContent.setText(item.content);
                    IconFontHelper.setTextIconFont(tvArrow, R.string.arrow_right);
                    break;
                case TYPE_PIC:
                    convertView = LayoutInflater.from(BeKolFirstActivity.this).inflate(R.layout.item_be_kol_pic, null);
                    mLLContentlv = convertView.findViewById(R.id.ll_content);
                    TextView tvAdd = (TextView) convertView.findViewById(R.id.tv_add);
                    mImageView = (ImageView) convertView.findViewById(R.id.iv_image);
                    IconFontHelper.setTextIconFont(tvAdd, R.string.add_sign);

                    if (!TextUtils.isEmpty(item.content)) {
                        BitmapUtil.loadImage(BeKolFirstActivity.this, item.content, mImageView);
                        mLLContentlv.setVisibility(View.GONE);
                    } else {
                        mLLContentlv.setVisibility(View.VISIBLE);
                    }
                    break;
            }

            if (position != ITEM_HEADER) {
                convertView.setOnClickListener(new MyOnClickListener(item.name, item.content, position));
            }
            return convertView;
        }
    }

    private class FirstBeKolItem {
        public String name;
        public String content;

        public FirstBeKolItem(String name) {
            this.name = name;
        }
    }

    private class MyOnClickListener implements View.OnClickListener {

        private int position;
        private String name;
        private String content;

        public MyOnClickListener(String name, String content, int position) {
            this.name = name;
            this.content = content;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (position) {
                case ITEM_GENDER:
                    showGenderSelector();
                    break;
                case ITEM_PIC:
                    updateImage();
                    break;
                default:
                    skipToDetail(name, content);
                    break;
            }
        }
    }

    private void updateImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SPConstants.IMAGE_REQUEST_CODE);
    }

    /**
     * 性别选择弹窗
     */
    private void showGenderSelector() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_gender_selector, null);
        TextView maleTV = (TextView) view.findViewById(R.id.tv_male);
        TextView femaleTV = (TextView) view.findViewById(R.id.tv_female);
        TextView cancelTV = (TextView) view.findViewById(R.id.tv_cancel);
        final CustomDialogManager cdm = new CustomDialogManager(this, view);

        maleTV.setOnClickListener(new GenderSelectorOnClickListener(MALE, cdm));

        femaleTV.setOnClickListener(new GenderSelectorOnClickListener(FEMALE, cdm));

        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cdm.dismiss();
            }
        });

        cdm.dg.setCanceledOnTouchOutside(true);
        cdm.dg.getWindow().setGravity(Gravity.CENTER);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        cdm.showDialog();
    }

    private void skipToNext() {
        Intent intent = new Intent(this, BeKolSecondActivity.class);
        intent.putExtra("social_accounts", (Serializable) mSocialAccounts);
        intent.putExtra("kol_shows", (Serializable) mKolShows);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void skipToDetail(String name, String content) {
        Intent intent = new Intent(this, BeKolDetailActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("content", content);
        startActivityForResult(intent, SPConstants.BE_KOL_FIRST);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPConstants.BE_KOL_FIRST && data != null) {
            String name = data.getStringExtra("name");
            String content = data.getStringExtra("content");
            if (getString(R.string.nickname).endsWith(name)) {//昵称
                mDataList.get(ITEM_NAME).content = content;
            } else if (getString(R.string.age).endsWith(name)) {//年龄
                mDataList.get(ITEM_AGE).content = content;
            } else if (getString(R.string.professional).endsWith(name)) {//职业
                mDataList.get(ITEM_JOB).content = content;
            } else if (getString(R.string.interest).endsWith(name)) {//兴趣
                mDataList.get(ITEM_INTEREST).content = content;
            } else if (getString(R.string.introduction).endsWith(name)) {//简介
                mDataList.get(ITEM_DESC).content = content;
            }
            mMyListAdapter.notifyDataSetChanged();
            return;
        }

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case SPConstants.IMAGE_REQUEST_CODE:
                    mImageUri = data.getData();
                    BitmapUtil.cropImageUri(mImageUri, 350, 420,  SPConstants.RESULT_CROP_CODE, 5, 6,this);
                    break;
                case SPConstants.RESULT_CROP_CODE:
                    mBitmap = BitmapUtil.decodeUriAsBitmap(mImageUri, this);
                    try {
                        mFinalPicturePath = FileUtils.saveBitmapToSD(mBitmap, "temp" + SystemClock.currentThreadTimeMillis());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mLLContentlv.setVisibility(View.GONE);
                    mImageView.setImageBitmap(mBitmap);
                    mImageLoadB = true;
                    break;
            }
        }
    }

    private class GenderSelectorOnClickListener implements View.OnClickListener {
        private int sex;
        private CustomDialogManager cdm;

        public GenderSelectorOnClickListener(int sex, CustomDialogManager cdm) {
            this.sex = sex;
            this.cdm = cdm;
        }

        @Override
        public void onClick(View v) {
            if (sex == MALE) {
                mDataList.get(ITEM_GENDER).content = getString(R.string.male);
            } else {
                mDataList.get(ITEM_GENDER).content = getString(R.string.female);
            }
            mMyListAdapter.notifyDataSetChanged();
            cdm.dismiss();
        }
    }
}
