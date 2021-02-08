package com.suftnet.v12.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.suftnet.v12.R
import com.suftnet.v12.api.model.response.Order
import com.suftnet.v12.api.model.response.Produce
import com.suftnet.v12.util.Constants
import com.suftnet.v12.util.CurrencyFormatter
import com.suftnet.v12.util.OrderStatus
import org.jetbrains.anko.textColor

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class JobAdapter(private val context : Context,
                 private val recyclerView : RecyclerView, var orders : MutableList<Order>) :
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
        val quantity  = itemView.findViewById<View>(R.id.quantity)  as TextView
        val orderStatus  = itemView.findViewById<View>(R.id.order_status)  as AppCompatButton
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        if (viewType === VIEW_ITEM) {
            val v: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.job_cell, parent, false)
            vh = ProduceViewHolder(v)
        } else {
            val v: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.loading, parent, false)
            vh = ProgressViewHolder(v)
        }
        return vh
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)  {
        if (holder is ProduceViewHolder) {

            val order = orders[position]
            holder.name.text = order.itemName
            holder.quantity.text = "Quantity : ${order.quantity} ${order.unit}"
            holder.orderStatus.text = order.status

            when(order.statusId)
            {
                OrderStatus.pending.toLowerCase() ->{
                    @Suppress("DEPRECATION")
                    holder.orderStatus.background = context.resources.getDrawable(R.drawable.btn_rounded_indigo)
                }

                OrderStatus.processing.toLowerCase() ->{
                    @Suppress("DEPRECATION")
                    holder.orderStatus.background = context.resources.getDrawable(R.drawable.btn_rounded_red)
                }

                OrderStatus.delivery.toLowerCase() ->{
                    @Suppress("DEPRECATION")
                    holder.orderStatus.background = context.resources.getDrawable(R.drawable.btn_rounded_green_300)
                }

                OrderStatus.completed.toLowerCase() ->{
                    @Suppress("DEPRECATION")
                    holder.orderStatus.background = context.resources.getDrawable(R.drawable.btn_rounded_tag)
                    @Suppress("DEPRECATION")
                    holder.orderStatus.textColor = context.resources.getColor(R.color.grey_90)
                }
            }

            holder.orderStatus.setOnClickListener {
                onItemClickListener?.onView(order, position)
            }

        }else {
            (holder as ProgressViewHolder).progressBar.isIndeterminate = true
        }
    }

    fun setLoading() {
        if (itemCount != 0) {
            this.orders.clear()
            notifyItemInserted(itemCount - 1)
            loading = true
        }
    }

    fun reset() {
        this.orders = ArrayList()
        notifyDataSetChanged()
    }
    fun add(items: MutableList<Order>)
    {
        val positionStart = itemCount
        val itemCount = items.size
        orders.addAll(items)
        notifyItemRangeInserted(positionStart, itemCount)
    }

    fun remove(item: Order, position : Int)
    {
        this.orders.remove(item)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    fun clear() {
        orders = ArrayList()
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val test : Order? = this.orders[position]
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
        fun onView(order: Order, position: Int)
    }

    interface OnLoadMoreListener {
        fun onLoadMore(current_page: Int)
    }
}