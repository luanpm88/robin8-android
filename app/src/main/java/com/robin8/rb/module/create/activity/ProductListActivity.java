package com.robin8.rb.module.create.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.nineoldandroids.view.ViewHelper;
import com.robin8.rb.R;
import com.robin8.rb.base.BaseDataActivity;
import com.robin8.rb.base.BaseRecyclerViewPresenter;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.helper.IconFontHelper;
import com.robin8.rb.module.create.model.CategoriesModel;
import com.robin8.rb.module.create.model.ProductPopItem;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.ProductPopupWindow;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.view.IRobinIndianaView;

import java.util.ArrayList;
import java.util.List;


/**
 * 商品库
 */
public class ProductListActivity extends BaseDataActivity implements IRobinIndianaView, View.OnClickListener {

    private static final int ET_EMPTY = 0;
    private static final int ET_UNEMPTY = 1;
    private RecyclerView mRecyclerView;
    private XRefreshView mXRefreshView;
    private BaseRecyclerViewPresenter mPresenter;
    private EditText mSearchEt;
    private TextView mDeleteTv;
    private TextView mSearchTv;
    private View mAllClassifyLL;
    private View mAllSequencingLL;
    private View mClassifyIv;
    private View mSequencingIv;
    private boolean mClassifyOpen = false;
    private boolean mSequencingOpen = false;
    private ProductPopupWindow mAllClassifyWindow;
    private ProductPopupWindow mAllSequencingWindow;
    private BasePresenter mBasePresenter;
    private List<String> mCategories;
    private List<ProductPopItem> mOrderList = new ArrayList<>();
    private List<ProductPopItem> mCategoriesList = new ArrayList<>();
    private boolean mFirstExcuteB = true;
    private String mCurrentCategoryName;
    private String mCurrentOrder;
    private TextView mClassifyTv;
    private TextView mSequencingTv;
    private View noDataLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_product_list);
        initView();
        initData();
    }

    private void initData() {
//        分别有 order_overall【综合排序】，order_price_desc【价格降序】，order_price_asc【价格升序】，order_commission_desc【提成降序】，order_commission_asc【提成升序】
        String[] productOrderArr = getResources().getStringArray(R.array.product_order);
        String[] productOrderENArr = getResources().getStringArray(R.array.product_order_en);

        for (int i = 0; i < productOrderArr.length; i++) {
            if (i == 0) {
                mOrderList.add(new ProductPopItem(productOrderArr[i], productOrderENArr[i], true));
            } else {
                mOrderList.add(new ProductPopItem(productOrderArr[i], productOrderENArr[i], false));
            }
        }

        String categoriesJson = CacheUtils.getString(ProductListActivity.this, SPConstants.CATEGORIES_JSON, null);
        if (!TextUtils.isEmpty(categoriesJson)) {
            CategoriesModel bean = GsonTools.jsonToBean(categoriesJson, CategoriesModel.class);
            mCategories = bean.getCategories();
            mCategories.add(0, "全部分类");
            mFirstExcuteB = false;

            for (int i = 0; i < mCategories.size(); i++) {
                if (i == 0) {
                    mCategoriesList.add(new ProductPopItem(mCategories.get(i), null, true));
                } else {
                    mCategoriesList.add(new ProductPopItem(mCategories.get(i), null, false));
                }
            }

            initPop();
        }

        mBasePresenter = new BasePresenter();
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.CATEGORIES_URL), null, new RequestCallback() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
                CategoriesModel bean = GsonTools.jsonToBean(response, CategoriesModel.class);
                if (bean != null && bean.getError() == 0) {
                    CacheUtils.putString(ProductListActivity.this, SPConstants.CATEGORIES_JSON, response);
                    if (mFirstExcuteB) {
                        mCategories = bean.getCategories();
                        initPop();
                    }
                }
            }
        });
    }

    private void initPop() {
        mAllClassifyWindow = getProductPopupWindow(mAllClassifyLL, mCategoriesList);
        mAllClassifyWindow.setOnItemSelectListener(new ProductPopupWindow.OnItemSelectListener() {
            @Override
            public void onItemSelect(int position) {
                mCurrentCategoryName = mCategoriesList.get(position).name;
                mClassifyTv.setText(mCurrentCategoryName);
                mPresenter.searchProduct(mSearchEt.getText().toString(), mCurrentCategoryName, mCurrentOrder);
                mClassifyOpen = updateState(mAllClassifyWindow, mClassifyOpen, mClassifyIv);
            }
        });

        mAllClassifyWindow.setOnDismissListener(new ProductPopupWindow.DismissListener() {
            @Override
            public void onDialogDismiss() {
                mClassifyOpen = updateState(mAllClassifyWindow, mClassifyOpen, mClassifyIv);
            }
        });


        mAllSequencingWindow = getProductPopupWindow(mAllSequencingLL, mOrderList);
        mAllSequencingWindow.setOnDismissListener(new ProductPopupWindow.DismissListener() {
            @Override
            public void onDialogDismiss() {
                mSequencingOpen = updateState(mAllSequencingWindow, mSequencingOpen, mSequencingIv);
            }
        });
        mAllSequencingWindow.setOnItemSelectListener(new ProductPopupWindow.OnItemSelectListener() {
            @Override
            public void onItemSelect(int position) {
                mCurrentOrder = mOrderList.get(position).label;
                mSequencingTv.setText(mOrderList.get(position).name);
                mPresenter.searchProduct(mSearchEt.getText().toString(), mCurrentCategoryName, mCurrentOrder);
                mSequencingOpen = updateState(mAllSequencingWindow, mSequencingOpen, mSequencingIv);
            }
        });
    }


    public void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_income_detail);
        mRecyclerView.setHasFixedSize(true);
        mXRefreshView = (XRefreshView) findViewById(R.id.xrefreshview);

        mAllClassifyLL = findViewById(R.id.ll_all_classify);
        mAllSequencingLL = findViewById(R.id.ll_all_sequencing);
        mClassifyIv = findViewById(R.id.iv_classify);
        mSequencingIv = findViewById(R.id.iv_sequencing);

        View backIv = findViewById(R.id.iv_back_search);
        mSearchEt = (EditText) findViewById(R.id.et_search_content);
        mDeleteTv = (TextView) findViewById(R.id.tv_delete);
        mSearchTv = (TextView) findViewById(R.id.tv_search);
        mClassifyTv = (TextView) findViewById(R.id.tv_classify);
        mSequencingTv = (TextView) findViewById(R.id.tv_sequencing);
        noDataLl = findViewById(R.id.ll_no_data);

        IconFontHelper.setTextIconFont(mDeleteTv, R.string.icons_delete);
        EditTextWatcher editTextWatcher = new EditTextWatcher();
        mSearchEt.addTextChangedListener(editTextWatcher);
        backIv.setOnClickListener(this);
        mSearchTv.setOnClickListener(this);
        mDeleteTv.setOnClickListener(this);
        mAllClassifyLL.setOnClickListener(this);
        mAllSequencingLL.setOnClickListener(this);
        updateView(ET_EMPTY);
        mPresenter = new BaseRecyclerViewPresenter(this, this);
        mPresenter.start();
    }

    @Override
    public void setTitleView(String text) {
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public XRefreshView getXRefreshView() {
        return mXRefreshView;
    }

    @Override
    public View getLLNoData() {
        return noDataLl;
    }

    @Override
    public TextView getRightTv() {
        return null;
    }

    @Override
    public TextView getBottomTv() {
        return null;
    }

    @Override
    public void setPageName(String name) {
        mPageName = name;
    }

    @Override
    public void finish() {
        if (mSequencingOpen) {
            mSequencingOpen = updateState(mAllSequencingWindow, mSequencingOpen, mSequencingIv);
            return;
        }

        if (mClassifyOpen) {
            mClassifyOpen = updateState(mAllClassifyWindow, mClassifyOpen, mClassifyIv);
            return;
        }

        mPresenter.finish();
        super.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_search:
                finish();
                break;

            case R.id.tv_search:
                mPresenter.searchProduct(mSearchEt.getText().toString(), mCurrentCategoryName, mCurrentOrder);
                break;

            case R.id.tv_delete:
                updateView(ET_EMPTY);
                mPresenter.searchProduct(null, mCurrentCategoryName, mCurrentOrder);
                break;

            case R.id.ll_all_classify:
                mClassifyOpen = updateState(mAllClassifyWindow, mClassifyOpen, mClassifyIv);
                if (mSequencingOpen) {
                    mSequencingOpen = updateState(mAllSequencingWindow, mSequencingOpen, mSequencingIv);
                }
                break;
            case R.id.ll_all_sequencing:
                mSequencingOpen = updateState(mAllSequencingWindow, mSequencingOpen, mSequencingIv);
                if (mClassifyOpen) {
                    mClassifyOpen = updateState(mAllClassifyWindow, mClassifyOpen, mClassifyIv);
                }
                break;
        }
    }

    private ProductPopupWindow getProductPopupWindow(View view, List<ProductPopItem> list) {
        final ProductPopupWindow itemDialog = new ProductPopupWindow(this, view, View.MeasureSpec.AT_MOST, View.MeasureSpec.AT_MOST, list);
        return itemDialog;
    }

    private boolean updateState(ProductPopupWindow itemDialog, boolean state, View view) {
        if (state) {
            itemDialog.dismiss();
            ViewHelper.setRotation(view, 360);
        } else {
            itemDialog.delayedShow(0, new int[]{0, 0});
            ViewHelper.setRotation(view, 180);
        }
        return !state;
    }

    class EditTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s != null) {
                updateView(ET_UNEMPTY);
            } else {
                updateView(ET_EMPTY);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private void updateView(int state) {
        switch (state) {
            case ET_EMPTY:
                mSearchEt.setText(null);
                mSearchTv.setEnabled(false);
                mDeleteTv.setVisibility(View.GONE);
                break;
            case ET_UNEMPTY:
                mDeleteTv.setVisibility(View.VISIBLE);
                mSearchTv.setEnabled(true);
                break;
        }
    }
}
