package com.robin8.rb.mine.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.andview.refreshview.XRefreshView
import com.robin8.rb.R
import com.robin8.rb.base.BaseActivity
import com.robin8.rb.base.constants.CommonConfig
import com.robin8.rb.mine.bean.WalletListBean
import com.robin8.rb.mine.bean.WalletListItem
import com.robin8.rb.okhttp.HttpRequest
import com.robin8.rb.okhttp.RequestCallback
import com.robin8.rb.okhttp.RequestParams
import com.robin8.rb.presenter.BasePresenter
import com.robin8.rb.ui.widget.WProgressDialog
import com.robin8.rb.util.GsonTools
import com.robin8.rb.util.HelpTools
import kotlinx.android.synthetic.main.activity_put_wallet_list.*

/**
 * PUT钱包
 */
class PutWalletListActivity : BaseActivity() {
	var wallets = arrayListOf<WalletListItem>()
	lateinit var basePresent: BasePresenter
	lateinit var adapter: ListAdapter
	var progressDialog: WProgressDialog? = null
	val pageSize = 10
	var page = 1
	override fun setTitleView() {
		mTVCenter.text = getString(R.string.put_wallet_list)
	}

	override fun initView() {
		basePresent = BasePresenter()
		progressDialog = WProgressDialog.createDialog(this)
		LayoutInflater.from(this).inflate(R.layout.activity_put_wallet_list, mLLContent, true)

		xrefreshview.setXRefreshViewListener(object : XRefreshView.SimpleXRefreshListener() {

			override fun onRefresh() {
				page = 1
				getData()
			}

			override fun onLoadMore(isSlience: Boolean) {
				page++
				getData()
			}
		})
		adapter = ListAdapter(wallets)
		recyclerview.setAdapter(adapter)
		progressDialog!!.show()
		getData()
	}

	override fun executeOnclickLeftView() {
		finish()
	}

	override fun executeOnclickRightView() {

	}

	fun getData() {
		var params = RequestParams()
		params.put("page", page)
		params.put("per_page", pageSize)
		basePresent.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.PUT_WALLET_LIST), params, object : RequestCallback {
			override fun onError(e: Exception?) {
				xrefreshview.stopRefresh()
				xrefreshview.stopLoadMore(true)
				if (progressDialog != null) {
					progressDialog!!.dismiss()
				}
			}

			override fun onResponse(response: String?) {
				xrefreshview.stopRefresh()
				xrefreshview.stopLoadMore(true)
				if (progressDialog != null) {
					progressDialog!!.dismiss()
				}
				var walletList = GsonTools.jsonToBean(response, WalletListBean::class.java)
				if (walletList != null && walletList.error == 0) {
					if (walletList.list.size < pageSize) {
						xrefreshview.pullRefreshEnable = true
						xrefreshview.pullLoadEnable = false
					} else {
						xrefreshview.pullRefreshEnable = true
						xrefreshview.pullLoadEnable = true
					}
					if (page == 1) {
						wallets.clear()
					}
					wallets.addAll(walletList.list)
					adapter.notifyDataSetChanged()
				}
			}

		})
	}

	inner class ListAdapter(wallets1: ArrayList<WalletListItem>?) : BaseAdapter() {
		var wallets = wallets1
		override fun getItem(p0: Int): WalletListItem {
			return wallets!![p0]
		}

		override fun getItemId(p0: Int): Long = 0

		override fun getView(position: Int, convertView: View?, container: ViewGroup?): View {
			var view: View
			var holder: ViewHolder
			if (convertView == null) {
				view = LayoutInflater.from(this@PutWalletListActivity).inflate(R.layout.item_put_wallet_list, container, false) as View
				holder = ViewHolder(view)
				view.tag = holder
			} else {
				view = convertView
				holder = view.tag as ViewHolder
			}
			holder.tv_content.text = getItem(position).title
			holder.tv_num.text = getItem(position).amount.toString()
			holder.tv_time.text = getItem(position).time
			return view
		}

		override fun getCount(): Int = if (wallets == null) {
			0
		} else {
			wallets!!.size
		}

		inner class ViewHolder(view: View) {
			var tv_content = view.findViewById(R.id.tv_content) as TextView
			var tv_num = view.findViewById(R.id.tv_num) as TextView
			var tv_time = view.findViewById(R.id.tv_time) as TextView
		}

	}

}