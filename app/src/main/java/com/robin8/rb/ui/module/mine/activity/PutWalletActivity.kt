package com.robin8.rb.mine.activity

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.robin8.rb.R
import com.robin8.rb.base.BaseActivity
import com.robin8.rb.base.constants.CommonConfig
import com.robin8.rb.mine.bean.PutWalltInfoBean
import com.robin8.rb.okhttp.HttpRequest
import com.robin8.rb.okhttp.RequestCallback
import com.robin8.rb.presenter.BasePresenter
import com.robin8.rb.ui.widget.WProgressDialog
import com.robin8.rb.util.GsonTools
import com.robin8.rb.util.HelpTools
import kotlinx.android.synthetic.main.activity_put_wallet.*
import java.lang.Exception

class PutWalletActivity : BaseActivity() {
    lateinit var persenter:BasePresenter
    lateinit var progressDialog:WProgressDialog
    override fun setTitleView() {
        mTVCenter.text=getString(R.string.put_wallet)
    }

    override fun initView() {
        var view = LayoutInflater.from(this).inflate(R.layout.activity_put_wallet, mLLContent, true) as View
        layout_question.setOnClickListener { startActivity(Intent(this@PutWalletActivity, PutWalletQuestionActivity::class.java))}
        tv_show_wallet_list.setOnClickListener{startActivity(Intent(this@PutWalletActivity, PutWalletListActivity::class.java))}
        persenter = BasePresenter()
        progressDialog = WProgressDialog.createDialog(this)
        progressDialog.show()
        getData()
    }

    fun getData(){
        persenter.getDataFromServer(true,HttpRequest.GET,HelpTools.getUrl(CommonConfig.PUT_WALLET_INFO),null,object :RequestCallback{
            override fun onError(e: Exception?) {
                progressDialog.dismiss()
            }

            override fun onResponse(response: String?) {
                var putwalletInfo = GsonTools.jsonToBean(response,PutWalltInfoBean::class.java)
                if (putwalletInfo != null && putwalletInfo.error == 0){
                    tv_balance.text=putwalletInfo.amount
                }
                progressDialog.dismiss()
            }

        })
    }

    override fun executeOnclickLeftView() {
        finish()
    }

    override fun executeOnclickRightView() {
    }

}