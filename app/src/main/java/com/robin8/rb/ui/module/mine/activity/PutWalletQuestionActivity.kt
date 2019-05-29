package com.robin8.rb.mine.activity

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.robin8.rb.R
import com.robin8.rb.base.BaseActivity
import kotlinx.android.synthetic.main.activity_put_wallet_question.*

class PutWalletQuestionActivity : BaseActivity() {
    lateinit var selecte: SparseBooleanArray
    lateinit var adapter: QuestionAdapter
    lateinit var questionList: Array<String>

    override fun setTitleView() {
        mTVCenter.text = getString(R.string.put_question)
    }

    override fun initView() {
        questionList = resources.getStringArray(R.array.put_wallet_questions)
        selecte = SparseBooleanArray()
        for (index in 0 until questionList.size / 2) {
            selecte.put(index, true)
        }
        LayoutInflater.from(this).inflate(R.layout.activity_put_wallet_question, mLLContent, true) as View
        adapter = QuestionAdapter()
        recycler_view.adapter = adapter
    }

    inner class QuestionAdapter : BaseAdapter() {
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var view: View
            var holder: QuestionViewHolder
            if (p1 == null) {
                view = LayoutInflater.from(this@PutWalletQuestionActivity).inflate(R.layout.item_put_wallet_question, p2, false)
                holder = QuestionViewHolder(view)
                view.tag = holder
            } else {
                view = p1
                holder = view.tag as QuestionViewHolder
            }

            holder.tv_title.text = questionList[p0 * 2]
            holder.tv_content.text = questionList[p0 * 2 + 1]
            holder.tv_title.setOnClickListener {
                holder.tv_content.visibility = (if (selecte.get(p0)) {
                    View.VISIBLE
                } else {
                    View.GONE
                })
                selecte.put(p0, !selecte.get(p0))
                adapter.notifyDataSetChanged()
            }
            holder.iv_right.setImageDrawable(if (selecte.get(p0)) {resources.getDrawable(R.mipmap.arrow_right)} else {resources.getDrawable(R.mipmap.icon_down)})
            return view
        }

        override fun getItem(p0: Int): Any = p0

        override fun getItemId(p0: Int): Long = 0

        override fun getCount(): Int = selecte.size()

    }

    inner class QuestionViewHolder(itemView: View) {
        var tv_title  = itemView.findViewById(R.id.tv_title)as TextView
        var tv_content = itemView.findViewById(R.id.tv_content) as TextView
        var iv_right = itemView.findViewById(R.id.iv_right) as ImageView
    }

    override fun executeOnclickLeftView() {
        finish()
    }

    override fun executeOnclickRightView() {

    }

}