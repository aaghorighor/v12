package com.suftnet.v12.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.suftnet.v12.R
import com.suftnet.v12.api.model.response.Question
import com.suftnet.v12.util.Constants

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class QuestionAdapter(private val context : Context,
                      private val recyclerView : RecyclerView, var questions : MutableList<Question>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_ITEM = 1
    private val VIEW_PROG = 0
    private var loading = false
    private var onLoadMoreListener: OnLoadMoreListener? = null
    private var onItemClickListener: OnItemClickListener? = null

    init {
        lastItemViewDetector(recyclerView) }

    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener?) {
        this.onLoadMoreListener = onLoadMoreListener
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener?) {
        onItemClickListener = itemClickListener
    }

    class ProgressViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var progressBar: ProgressBar = v.findViewById(R.id.progress_loading)
    }

    class ProduceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val user  = itemView.findViewById<View>(R.id.user)  as TextView
        val date  = itemView.findViewById<View>(R.id.date)  as TextView
        val description  = itemView.findViewById<View>(R.id.description)  as TextView
        val smsLayout  = itemView.findViewById<View>(R.id.sms_layout)  as RelativeLayout
        val phoneLayout  = itemView.findViewById<View>(R.id.phone_layout)  as RelativeLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        if (viewType === VIEW_ITEM) {
            val v: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.question_cell, parent, false)
            vh = ProduceViewHolder(v)
        } else {
            val v: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.loading, parent, false)
            vh = ProgressViewHolder(v)
        }
        return vh
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)  {
        if (holder is ProduceViewHolder) {

            val question = questions[position]
            holder.user.text = "${question.firstName} ${question.lastName}"
            holder.date.text = question.createdDt
            holder.description.text = question.description

            holder.smsLayout.setOnClickListener {
                onItemClickListener?.onSms(question, position)
            }

            holder.phoneLayout.setOnClickListener {
                onItemClickListener?.onPhone(question, position)
            }

        }else {
            (holder as ProgressViewHolder).progressBar.isIndeterminate = true
        }
    }

    fun setLoading() {
        if (itemCount != 0) {
            this.questions.clear()
            notifyItemInserted(itemCount - 1)
            loading = true
        }
    }

    fun reset() {
        this.questions = ArrayList()
        notifyDataSetChanged()
    }
    fun add(items: MutableList<Question>)
    {
        val positionStart = itemCount
        val itemCount = items.size
        questions.addAll(items)
        notifyItemRangeInserted(positionStart, itemCount)
    }

    fun add(question: Question)
    {
        this.questions.add(0,question);
        notifyItemInserted(0);
    }

    fun remove(item: Question, position : Int)
    {
        this.questions.remove(item)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    fun clear() {
        questions = ArrayList()
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val test : Question? = this.questions[position]
        var response = VIEW_ITEM
        if (test == null) response =  VIEW_PROG
        return  response
    }
    private fun lastItemViewDetector(recyclerView: RecyclerView) {
        if (recyclerView.layoutManager is LinearLayoutManager) {
            val layoutManager =
                recyclerView.layoutManager as LinearLayoutManager?
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val scrollOutItems = layoutManager!!.findFirstVisibleItemPosition()
                    val currentItems = layoutManager.childCount
                    val totalItems = layoutManager.itemCount
                    if (!loading && currentItems + scrollOutItems == totalItems) {
                        if (onLoadMoreListener != null) {
                            try {
                                val current_page: Int = itemCount / Constants.LOAD_PER_REQUEST
                                onLoadMoreListener!!.onLoadMore(current_page)
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }
                        }
                        loading = true
                    }
                }
            })
        }
    }
    interface OnItemClickListener {
        fun onPhone(question: Question, position: Int)
        fun onSms(question: Question, position: Int)
    }

    interface OnLoadMoreListener {
        fun onLoadMore(current_page: Int)
    }
}