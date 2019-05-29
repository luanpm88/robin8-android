package com.robin8.rb.mine.bean

import com.robin8.rb.ui.model.BaseBean


data class WalletListBean(var list:ArrayList<WalletListItem>): BaseBean()

data class WalletListItem(var title:String,var amount:Int,var time:String)