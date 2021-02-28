package com.suftnet.v12.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.suftnet.v12.R
import com.suftnet.v12.api.model.response.Produce
import com.suftnet.v12.util.Constants
import com.suftnet.v12.util.CurrencyFormatter
import com.suftnet.v12.util.Util

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class MarketAdapter(private val context : Context,
                    private val recyclerView : RecyclerView, var produces : MutableList<Produce>) :
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

        val name  = itemView.findViewById<View>(R.id.name)  as TextView
        val availableDate  = itemView.findViewById<View>(R.id.availableDate)  as TextView
        val address  = itemView.findViewById<View>(R.id.address)  as TextView
        val price  = itemView.findViewById<View>(R.id.price)  as TextView
        val linearLayout  = itemView.findViewById<View>(R.id.linear_layout)  as LinearLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        if (viewType === VIEW_ITEM) {
            val v: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_cell, parent, false)
            vh = ProduceViewHolder(v)
        } else {
            val v: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.loading, parent, false)
            vh = ProgressViewHolder(v)
        }
        return vh
    }

    override fun getItemCount(): Int {
        return produces.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)  {
        if (holder is ProduceViewHolder) {

            val produce = produces[position]
            holder.name.text = produce.name
            holder.availableDate.text = produce.availableDate

            holder.price.text = "₦" + CurrencyFormatter.format(produce.price, 2)

            var completedAddress = "${produce.address}"
            if(produce.city!!.isNotEmpty()) completedAddress += ", ${produce.city}"
            if(produce.state!!.isNotEmpty()) completedAddress += ", ${produce.state!!}"
            if(produce.country!!.isNotEmpty()) completedAddress += ", ${produce.country}"
            holder.address.text = completedAddress

            holder.linearLayout.setOnClickListener {
                onItemClickListener?.onView(produce, position)
            }

        }else {
            (holder as ProgressViewHolder).progressBar.isIndeterminate = true
        }
    }

    fun setLoading() {
        if (itemCount != 0) {
            this.produces.clear()
            notifyItemInserted(itemCount - 1)
            loading = true
        }
    }

    fun reset() {
        this.produces = ArrayList()
        notifyDataSetChanged()
    }
    fun add(items: MutableList<Produce>)
    {
        val positionStart = itemCount
        val itemCount = items.size
        produces.addAll(items)
        notifyItemRangeInserted(positionStart, itemCount)
    }

    fun remove(item: Produce, position : Int)
    {
        this.produces.remove(item)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    fun clear() {
        produces = ArrayList()
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val test : Produce? = this.produces[position]
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
        fun onView(produce: Produce, position: Int)
    }

    interface OnLoadMoreListener {
        fun onLoadMore(current_page: Int)
    }
}