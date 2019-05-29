package com.robin8.rb.util.share

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.share.Sharer
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import com.robin8.rb.R
import com.robin8.rb.ui.dialog.CustomDialogManager
import com.robin8.rb.ui.module.share.thirdplatfom.Constants
import com.robin8.rb.util.AppUtils
import com.robin8.rb.util.CustomToast

class RobinShareDialog {
	private val mContext: Context
	private var mCallbackManager: CallbackManager? = null
	private var linkContent: ShareLinkContent? = null
	/**
	 * The method is used to share campaign to facebook
	 *
	 * @param shareUrl     campaign url
	 * @param title        campaign title
	 * @param thumbnailUrl campaign thumbnail url
	 */
	fun shareFacebook(shareUrl: String, title: String, content: String, imageUrl: String) {

		if (ShareDialog.canShow(ShareLinkContent::class.java)) {
			if (imageUrl.isNotEmpty()) {
				linkContent = ShareLinkContent.Builder()
						.setContentUrl(Uri.parse(shareUrl))
						.setContentTitle(title)
						.setContentDescription(content)
						.setImageUrl(Uri.parse(imageUrl))
						.build()
			} else {
				linkContent = ShareLinkContent.Builder()
						.setContentUrl(Uri.parse(shareUrl))
						.setContentTitle(title)
						.setContentDescription(content)
						.build()
			}
		}

	}

	fun show(){
		mCustomDialogManager?.showDialog()
	}

	fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
		mCallbackManager?.onActivityResult(requestCode, resultCode, data)
	}

	private val shareCallback = object : FacebookCallback<Sharer.Result> {
		override fun onCancel() {}

		override fun onError(error: FacebookException) {}

		override fun onSuccess(result: Sharer.Result) {}
	}

	private var mCustomDialogManager: CustomDialogManager? = null

	constructor(context: Context) {
		mContext = context
		val view = LayoutInflater.from(context).inflate(R.layout.share_dialog, null)

		val cancelTV = view.findViewById(R.id.tv_cancel) as TextView
		val tv_facebook = view.findViewById(R.id.tv_facebook) as TextView
		mCustomDialogManager = CustomDialogManager(context, view)
		tv_facebook.setOnClickListener {
			share()
			mCustomDialogManager?.dg?.dismiss()
		}
		cancelTV.setOnClickListener {

			mCustomDialogManager?.dismiss()
		}
		mCustomDialogManager?.dg?.setCanceledOnTouchOutside(true)
		mCustomDialogManager?.dg?.getWindow()!!.setGravity(Gravity.BOTTOM)
		mCustomDialogManager?.dg?.getWindow()!!.setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade)

	}

	fun share() {
		if (linkContent != null) {
//			CustomToast.showShort(mContext, "前往分享...")
			if (AppUtils.isAppInstalled(mContext.getApplicationContext(), Constants.FACEBOOK_PACKAGE)) {
				mCallbackManager = CallbackManager.Factory.create()
				var mShareDialog = ShareDialog(mContext as Activity)
				mShareDialog.registerCallback(mCallbackManager, shareCallback)
				mShareDialog.show(linkContent)
			} else {
				try {
					mContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + Constants.FACEBOOK_PACKAGE)))
				} catch (anfe: android.content.ActivityNotFoundException) {
					mContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + Constants.FACEBOOK_PACKAGE)))
				}

			}

		}
	}
}