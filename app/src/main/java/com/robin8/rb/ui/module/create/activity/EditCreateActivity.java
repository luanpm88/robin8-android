package com.robin8.rb.ui.module.create.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.robin8.rb.R;
import com.robin8.rb.ui.activity.PaySuccessActivity;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.BaseRecyclerViewActivity;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.helper.IconFontHelper;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.ui.model.BaseBean;
import com.robin8.rb.ui.model.NotifyMsgEntity;
import com.robin8.rb.ui.module.create.model.ProductListModel;
import com.robin8.rb.ui.module.reword.helper.DetailContentHelper;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.util.FileUtils;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.util.UIUtils;
import com.robin8.rb.ui.dialog.CustomDialogManager;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Observable;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 开始创作
 */
public class EditCreateActivity extends BaseActivity {

    private static final int IMAGE_REQUEST_CODE = 101;

    private static final int TYPE_ET = 0;
    private static final int TYPE_PIC = 1;
    private static final int TYPE_PRODUCT = 2;
    private static final int FINISH = 0;
    private static final int REALEASE = 1;
    @Bind(R.id.et_title)
    EditText etTitle;
    @Bind(R.id.line_bottom)
    View lineBottom;
    @Bind(R.id.tv_insert_img)
    TextView tvInsertImg;
    @Bind(R.id.tv_insert_goods)
    TextView tvInsertGoods;
    @Bind(R.id.ll_bottom)
    LinearLayout llBottom;
    @Bind(R.id.tv_delete)
    TextView tvDelete;
    @Bind(R.id.ll_add_content)
    LinearLayout llAddContent;
    private BasePresenter mBasePresenter;
    private WProgressDialog mWProgressDialog;
    private List<Item> mDataList = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private int mCursor;
    private ProductListModel.CpsMaterialsBean mCpsMaterialsBean;
    private StringBuffer sb = new StringBuffer();
    private String mContent;
    private String mCover;
    private String mEtTitleStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void setTitleView() {
        getData();
        mTVCenter.setText(R.string.start_create);
    }

    private void getData() {
        Intent intent = getIntent();
        Serializable bean = intent.getSerializableExtra("bean");
        if (bean instanceof ProductListModel.CpsMaterialsBean) {
            mCpsMaterialsBean = (ProductListModel.CpsMaterialsBean) bean;
        }
    }

    @Override
    public void initView() {
        mTVRight.setVisibility(View.VISIBLE);
        mTVRight.setText(R.string.release);
        mLayoutInflater = LayoutInflater.from(this);
        View view = mLayoutInflater.inflate(R.layout.activity_start_create, mLLContent, true);
        ButterKnife.bind(this);
        IconFontHelper.setTextIconFont(tvDelete, R.string.icons_delete2);
        NotifyManager.getNotifyManager().addObserver(this);
        tvInsertImg.setOnClickListener(this);
        tvInsertGoods.setOnClickListener(this);
        tvDelete.setVisibility(View.INVISIBLE);
        tvDelete.setOnClickListener(this);
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    tvDelete.setVisibility(View.VISIBLE);
                } else {
                    tvDelete.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        start();
    }

    private void start() {
        updateData();
        fillLinearLayout();
    }

    private void fillLinearLayout() {
        llAddContent.removeAllViews();
        for (int i = 0; i < mDataList.size(); i++) {
            Item item = mDataList.get(i);
            final int finalI = i;
            View view = null;

            switch (item.type) {
                case TYPE_ET:
                    view = addETView(i);
                    break;
                case TYPE_PIC:
                    view = addPICView(i);
                    break;
                case TYPE_PRODUCT:
                    view = addProductView(i);
                    break;
            }

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            llAddContent.addView(view, lp);
        }
    }

    private View addETView(final int position) {
        View view = mLayoutInflater.inflate(R.layout.edit_create_et_item, null);
        EditText contentEt = (EditText) view.findViewById(R.id.et_content);
        if (contentEt != null) {
            contentEt.setText(mDataList.get(position).content);
            contentEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        mCursor = position;
                        lineBottom.setVisibility(View.VISIBLE);
                        llBottom.setVisibility(View.VISIBLE);
                    } else {
                        lineBottom.setVisibility(View.GONE);
                        llBottom.setVisibility(View.GONE);
                    }
                }
            });
            contentEt.addTextChangedListener(new MyTextWatcher(position));
        }
        return view;
    }

    private View addPICView(final int position) {
        final int screenWidth = DensityUtils.getScreenWidth(EditCreateActivity.this);
        View view = mLayoutInflater.inflate(R.layout.edit_create_pic_item, null);
        ImageView picIv = (ImageView) view.findViewById(R.id.iv_pic);
        TextView boxDeleteIv = (TextView) view.findViewById(R.id.tv_box_delete);
        IconFontHelper.setTextIconFont(boxDeleteIv, R.string.icons_box_delete);
        if (boxDeleteIv != null) {
            boxDeleteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteThisItem(position);
                }
            });
        }

        if (picIv != null) {
            final ImageView finalPicIv = picIv;
            Glide.with(EditCreateActivity.this.getApplicationContext())
                    .load(mDataList.get(position).url)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {

                        @Override
                        public void onResourceReady(final Bitmap bitmap, GlideAnimation glideAnimation) {
                            final int bitmapH = bitmap.getHeight();
                            final int bitmapW = bitmap.getWidth();
                            LogUtil.logXXfigo("bitmapH=" + bitmapH + "  bitmapW=" + bitmapW);
                            finalPicIv.setImageBitmap(bitmap);
                            ViewGroup.LayoutParams lp = finalPicIv.getLayoutParams();
                            lp.width = (int) (screenWidth - 26 * BaseApplication.mPixelDensityF);
                            lp.height = lp.width * bitmapH / bitmapW;
                            finalPicIv.setLayoutParams(lp);
                        }
                    });
        }
        return view;
    }

    private View addProductView(final int position) {
        ProductListModel.CpsMaterialsBean mCpsMaterialsBean = mDataList.get(position).cpsArticleBean;
        View view = mLayoutInflater.inflate(R.layout.edit_create_product_item, null);
        ImageView productIv = (ImageView) view.findViewById(R.id.iv_product);
        TextView productNameTv = (TextView) view.findViewById(R.id.tv_product_name);
        TextView priceTv = (TextView) view.findViewById(R.id.tv_price);
        TextView commissionTv = (TextView) view.findViewById(R.id.tv_commission);
        TextView boxDeleteIv = (TextView) view.findViewById(R.id.tv_box_delete);
        IconFontHelper.setTextIconFont(boxDeleteIv, R.string.icons_box_delete);

        BitmapUtil.loadImage(this.getApplicationContext(), mCpsMaterialsBean.getImg_url(), productIv);
        productNameTv.setText(mCpsMaterialsBean.getGoods_name());
        priceTv.setText(getString(R.string.price) + " " + StringUtil.deleteZero(mCpsMaterialsBean.getUnit_price()) + "₫");
        String str = getString(R.string.commission) + "<font color=#ecb200> " + StringUtil.deleteZero(mCpsMaterialsBean.getKol_commision_wl()) + "₫</font>";
        commissionTv.setText(Html.fromHtml(str));

        boxDeleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteThisItem(position);
            }
        });
        return view;
    }

    private void updateData() {
        Item item0 = new Item(TYPE_ET, "", "", null);
        mDataList.add(item0);
        if (mCpsMaterialsBean != null) {
            Item item1 = new Item(TYPE_PRODUCT, "", "", mCpsMaterialsBean);
            Item item2 = new Item(TYPE_ET, "", "", null);
            mDataList.add(item1);
            mDataList.add(item2);
        }
    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.START_CREATE;
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_delete:
                etTitle.setText(null);
                break;
            case R.id.tv_insert_img:
                insertImage();
                break;
            case R.id.tv_insert_goods:
                insertProduct();
                break;
        }
    }

    @Override
    protected void executeOnclickLeftView() {
        popSharePromptDialog(FINISH);
    }

    @Override
    protected void executeOnclickRightView() {
        popSharePromptDialog(REALEASE);
    }

    private void popSharePromptDialog(final int type) {
        View view = mLayoutInflater.inflate(R.layout.dialog_reject_screenshot, null);
        TextView leftTV = (TextView) view.findViewById(R.id.tv_confirm);
        TextView topTv = (TextView) view.findViewById(R.id.tv_top);
        final TextView infoTv = (TextView) view.findViewById(R.id.tv_info);
        TextView rightTv = (TextView) view.findViewById(R.id.tv_right);
        if(REALEASE == type){
            infoTv.setText(R.string.realese_article_tips);
        }else {
            infoTv.setText(R.string.finish_article_tips);
        }
        topTv.setVisibility(View.GONE);
        rightTv.setText(R.string.cancel);
        final CustomDialogManager cdm = new CustomDialogManager(EditCreateActivity.this, view);
        leftTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cdm.dismiss();
                if(REALEASE == type){
                    releaseCreate();
                }else {
                   finish();
                }
            }
        });
        rightTv.setOnClickListener(new View.OnClickListener() {
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

    /**
     * 上传图片
     */
    private void postImage(File file, final Bitmap bitmap) {
        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();

        LinkedHashMap<String, Object> requestMap = new LinkedHashMap<>();
        requestMap.put("[url]", HelpTools.getUrl(CommonConfig.IMAGES_UPLOAD_URL));
        requestMap.put("[file/image/jpeg]file", file);
//        mBasePresenter.postImage(true, HttpRequest.POST, requestMap, new DefaultHttpCallBack(null) {
//
//            @Override
//            public void onComplate(ResponceBean responceBean) {
//                if (mWProgressDialog != null) {
//                    mWProgressDialog.dismiss();
//                }
//                PostImageBean bean = GsonTools.jsonToBean(responceBean.pair.second, PostImageBean.class);
//                if (bean == null) {
//                    CustomToast.showShort(EditCreateActivity.this, EditCreateActivity.this.getString(R.string.please_data_wrong));
//                    return;
//                }
//                if (bean.getError() == 0) {
//                    String url = bean.getUrl();
//                    mDataList.add(mCursor + 1, new Item(TYPE_PIC, url, "", null));
//                    mDataList.add(mCursor + 2, new Item(TYPE_ET, "", "", null));
//                    fillLinearLayout();
//                } else
//                    CustomToast.showShort(EditCreateActivity.this, bean.getDetail());
//            }
//
//            public void onFailure(ResponceBean responceBean) {
//                if (mWProgressDialog != null) {
//                    mWProgressDialog.dismiss();
//                }
//                try {
//                    BaseBean baseBean = GsonTools.jsonToBean(responceBean.pair.second, BaseBean.class);
//                    if (!TextUtils.isEmpty(baseBean.getDetail()))
//                        CustomToast.showShort(BaseApplication.getContext(), baseBean.getDetail());
//                    else
//                        CustomToast.showShort(BaseApplication.getContext(), responceBean.pair.second);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }


    /**
     * 插入图片
     */
    private void insertImage() {
        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
        startActivityForResult(i, IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPConstants.PRODUCT_LIST && data != null) {
            return;
        }

        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case DetailContentHelper.IMAGE_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    final String finalPicturePath = FileUtils.getAbsoluteImagePath(this, selectedImage);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final String uploadPicturePath = BitmapUtil.getCompressImagePath(finalPicturePath);
                            final Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromResource(uploadPicturePath,
                                    DensityUtils.getScreenWidth(EditCreateActivity.this),
                                    DensityUtils.getScreenHeight(EditCreateActivity.this));
                            UIUtils.runInMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (uploadPicturePath == null)
                                        CustomToast.showShort(EditCreateActivity.this, R.string.img_empty);
                                    else {
                                        postImage(new File(uploadPicturePath), bitmap);
                                    }
                                }
                            });
                        }
                    }).start();
                    break;
            }
        }
    }

    /**
     * 插入商品
     */
    private void insertProduct() {
        Intent intent = new Intent(this, BaseRecyclerViewActivity.class);
        intent.putExtra("destination", SPConstants.PRODUCT_LIST);
        intent.putExtra("from", SPConstants.EDIT_CREATE_ACTIVITY);
        intent.putExtra("url", HelpTools.getUrl(CommonConfig.PRODUCT_LIST_URL));
        intent.putExtra("title", getString(R.string.product));
        startActivityForResult(intent, SPConstants.EDIT_CREATE_ACTIVITY);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof NotifyMsgEntity) {
            NotifyMsgEntity msgEntity = (NotifyMsgEntity) data;
            if (msgEntity.getCode() == NotifyManager.TYPE_INSERT_PRODUCT) {
                ProductListModel.CpsMaterialsBean mCpsMaterialsBean = (ProductListModel.CpsMaterialsBean) msgEntity.getData();
                mDataList.add(mCursor + 1, new Item(TYPE_PRODUCT, "", "", mCpsMaterialsBean));
                mDataList.add(mCursor + 2, new Item(TYPE_ET, "", "", null));
                fillLinearLayout();
            }
        }
    }

    /**
     * 发布
     */
    private void releaseCreate() {

        if (!checkParams()) {
            return;
        }

        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();

        RequestParams params = new RequestParams();
        params.put("title", mEtTitleStr);
        params.put("content", mContent);
        params.put("cover", mCover);

        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.RELEASE_URL), params, new RequestCallback() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
                BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);
                if (bean != null && bean.getError() == 0) {
                    Intent intent = new Intent(EditCreateActivity.this, PaySuccessActivity.class);
                    intent.putExtra("from", SPConstants.EDIT_CREATE_ACTIVITY);
                    startActivity(intent);
                    EditCreateActivity.this.finish();
                }
            }
        });
    }

    private boolean checkParams() {
        mEtTitleStr = etTitle.getText().toString();
        if (TextUtils.isEmpty(mEtTitleStr)) {
            CustomToast.showShort(this, getString(R.string.please_write_title));
            return false;
        }
//        <text>这个图片很不错<img>http://xxxx.com/example.jpg<text>nice<product>id
        sb.delete(0, sb.length());
        boolean addCover = false;
        for (int i = 0; i < mDataList.size(); i++) {
            Item item = mDataList.get(i);
            switch (item.type) {
                case TYPE_ET:
                    if (!TextUtils.isEmpty(item.content)) {
                        sb.append("<text>").append(item.content);
                    }
                    break;
                case TYPE_PIC:
                    if (!TextUtils.isEmpty(item.url)) {
                        sb.append("<img>").append(item.url);
                        if (!addCover) {
                            mCover = item.url;
                            addCover = true;
                        }
                    }
                    break;
                case TYPE_PRODUCT:
                    if (item.cpsArticleBean != null) {
                        sb.append("<product>").append(item.cpsArticleBean.getId());
                    }
                    break;
            }
        }

        mContent = sb.toString();
        if (TextUtils.isEmpty(mContent)) {
            CustomToast.showShort(this, getString(R.string.please_write_article_content));
            return false;
        }

        if (TextUtils.isEmpty(mCover)) {
            CustomToast.showShort(this, getString(R.string.please_add_one_picture));
            return false;
        }
        return true;
    }

    private class Item {
        public ProductListModel.CpsMaterialsBean cpsArticleBean;
        public int type;
        public String url;
        public String content;

        public Item(int type, String url, String content, ProductListModel.CpsMaterialsBean cpsArticleBean) {
            this.type = type;
            this.url = url;
            this.content = content;
            this.cpsArticleBean = cpsArticleBean;
        }
    }

    private void deleteThisItem(int position) {
        mDataList.get(position - 1).content = mDataList.get(position - 1).content + "\n" + mDataList.get(position + 1).content;
        mDataList.remove(position);
        if (mDataList.size() > position) {
            mDataList.remove(position);
        }
        fillLinearLayout();
    }

    private class MyTextWatcher implements TextWatcher {
        private int position;

        public MyTextWatcher(int i) {
            this.position = i;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mDataList.get(position).content = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
