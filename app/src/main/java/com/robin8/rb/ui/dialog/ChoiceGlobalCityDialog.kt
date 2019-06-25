package com.robin8.rb.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter
import com.contrarywind.view.WheelView
import com.robin8.rb.R

class ChoiceGlobalCityDialog:Dialog{

	 var wheelView: WheelView
	 var btnCancel: Button
	 var btnSubmit: Button
	constructor(context: Context, datas:List<String>, listener:OnCheckCityListener):super(context){
		val view = LayoutInflater.from(context).inflate(R.layout.dialog_choice_global_city,null)
		setContentView(view)
		window.attributes.gravity = Gravity.BOTTOM
		window.setBackgroundDrawable(ColorDrawable(Color.parseColor("#ffffff")))
		window.attributes.width = window.windowManager.defaultDisplay.width
		wheelView = view.findViewById(R.id.wheel_city)
		btnCancel = view.findViewById(R.id.btnCancel)
		btnCancel.setOnClickListener { dismiss() }
		btnSubmit = view.findViewById(R.id.btnSubmit)
		btnSubmit.setOnClickListener {
			listener.onCheck(wheelView.currentItem)
			dismiss()
		}

		wheelView.adapter = ArrayWheelAdapter(datas)// 设置"年"的显示数据
		wheelView.setLabel("")// 添加文字
		wheelView.currentItem = 0// 初始化时显示的数据
		wheelView.setGravity(Gravity.CENTER)
	}

	interface OnCheckCityListener{
		fun onCheck(item:Int)
	}
}