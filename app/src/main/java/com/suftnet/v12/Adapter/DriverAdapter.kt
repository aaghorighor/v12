package com.suftnet.v12.Adapter

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
import com.suftnet.v12.api.model.response.Driver
import com.suftnet.v12.util.Constants

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class DriverAdapter(private val context : Context,
                    private val recyclerView : RecyclerView, var drivers : MutableList<Driver>) :
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

        val names  = itemView.findViewById<View>(R.id.names)  as TextView
        val addLayout  = itemView.findViewById<View>(R.id.add_layout)  as RelativeLayout
        val smsLayout  = itemView.findViewById<View>(R.id.sms_layout)  as RelativeLayout
        val phoneLayout  = itemView.findViewById<View>(R.id.phone_layout)  as RelativeLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        if (viewType === VIEW_ITEM) {
            val v: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.driver_cell, parent, false)
            vh = ProduceViewHolder(v)
        } else {
            val v: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.loading, parent, false)
            vh = ProgressViewHolder(v)
        }
        return vh
    }

    override fun getItemCount(): Int {
        return drivers.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)  {
        if (holder is ProduceViewHolder) {

            val driver = drivers[position]
            holder.names.text = "${driver.firstName} ${driver.lastName}"

            holder.addLayout.setOnClickListener {
                onItemClickListener?.onAdd(driver, position)
            }

            holder.smsLayout.setOnClickListener {
                onItemClickListener?.onSms(driver, position)
            }

            holder.phoneLayout.setOnClickListener {
                onItemClickListener?.onPhone(driver, position)
            }

        }else {
            (holder as ProgressViewHolder).progressBar.isIndeterminate = true
        }
    }

    fun setLoading() {
        if (itemCount != 0) {
            this.drivers.clear()
            notifyItemInserted(itemCount - 1)
            loading = true
        }
    }

    fun reset() {
        this.drivers = ArrayList()
        notifyDataSetChanged()
    }
    fun add(items: MutableList<Driver>)
    {
        val positionStart = itemCount
        val itemCount = items.size
        drivers.addAll(items)
        notifyItemRangeInserted(positionStart, itemCount)
    }

    fun remove(item: Driver, position : Int)
    {
        this.drivers.remove(item)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    fun clear() {
        drivers = ArrayList()
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val test : Driver? = this.drivers[position]
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
        fun onAdd(driver: Driver, position: Int)
        fun onSms(driver: Driver, position: Int)
        fun onPhone(driver: Driver, position: Int)
    }

    interface OnLoadMoreListener {
        fun onLoadMore(current_page: Int)
    }
}