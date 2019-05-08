package com.robin8.rb.util.share

interface ShareView{
	fun getShareBean():ShareInfoBean

	fun checkApkInstall(platform :String):Boolean

}