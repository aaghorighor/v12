package com.suftnet.v12.ui

import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.material.snackbar.Snackbar
import com.suftnet.v12.Adapter.OrderAdapter
import com.suftnet.v12.R
import com.suftnet.v12.api.model.response.Order
import com.suftnet.v12.model.Error
import com.suftnet.v12.viewModel.BuyerViewModel
import kotlinx.android.synthetic.main.order_placeholder.*
import org.jetbrains.anko.alert

class BuyerPendingOrderActivity : BaseAppCompatActivity() {
    companion object {
        const val TAG = "BuyerPendingOrderActivity"
    }

    private lateinit var viewModel: BuyerViewModel
    private lateinit var orderAdapter : OrderAdapter
    private lateinit var recyclerView : RecyclerView
    private val context : Context = this
    private var totalCount = 0.0
    private var snackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_placeholder)

        init()
        handler(1)
    }

    private fun init()
    {
        setToolBar("Orders")
        viewModel = ViewModelProvider(this).get(BuyerViewModel::class.java)

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.layoutManager = LinearLayoutManager(context);
        recyclerView.setHasFixedSize(true);

        orderAdapter = OrderAdapter(context,recyclerView, ArrayList<Order>())
        recyclerView.adapter = orderAdapter

        listener()
    }

    private fun setToolBar(title: String) {

        main_title.text =title
    }

    private fun listener()
    {
        back_action.setOnClickListener {
            var i = Intent(this@BuyerPendingOrderActivity, BuyerDashboardActivity::class.java)
            i.flags = FLAG_ACTIVITY_NEW_TASK
            i.flags = FLAG_ACTIVITY_CLEAR_TOP
            i.flags = FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }

        swipe_refresh.setOnRefreshListener(OnRefreshListener {
            swipe_refresh.isRefreshing = false
            orderAdapter.reset()
            handler(1)
        })

        orderAdapter.setOnItemClickListener(object:OrderAdapter.OnItemClickListener {
            override fun onView(order: Order, position: Int) {
                var i = Intent(this@BuyerPendingOrderActivity, BuyerOrderDetailActivity::class.java)
                i.putExtra("order", order)
                i.putExtra("from", "0")
                startActivity(i)
            }
        })

        viewModel.loading.observe(this, Observer {
            progress_Bar.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.error.observe(
            this,
            Observer {
                onError(it)
            }
        )
    }

    private fun loadPage()
    {
        viewModel.pending().observe(this, Observer {
                        if(!it.isNullOrEmpty())
            {
                add(it.toMutableList())
            }else {
                showEmptyLayout(true)
            }
        })
    }

    private fun add(orders: MutableList<Order>) {

        recyclerView.visibility = View.VISIBLE
        orderAdapter.add(orders)
        if (orders.size == 0) {
            showEmptyLayout(true)
        }
    }

    private fun showEmptyLayout(show: Boolean) {
        val emptyLayout = findViewById<View>(R.id.lyt_no_item)
        if (show) {
            recyclerView.visibility = View.GONE
            emptyLayout.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyLayout.visibility = View.GONE
        }
    }

    private fun showFailedView(message: String) {
        snackBar = Snackbar.make(this.parent_view, message, Snackbar.LENGTH_INDEFINITE)
        snackBar!!.setAction(getString(R.string.PleaseTryAgain), View.OnClickListener {
            handler(1)
            recyclerView.scrollToPosition(orderAdapter.getItemCount() - 1)
        })
        snackBar!!.show()
    }

    private fun hidErrorLayout() {
        if (snackBar != null && snackBar!!.isShownOrQueued()) snackBar!!.dismiss()
    }

    private fun handler(page_no: Int) {
        hidErrorLayout()
        showEmptyLayout(false)
        if (page_no == 1) {
        } else {
            orderAdapter.setLoading()
        }
        Handler(Looper.getMainLooper()).postDelayed({
            loadPage()
        }, 10)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun onError(it :Error)
    {
        alert {
            title = getString(R.string.ErrorMessages)
            message = """${it.messages}${{ it.statusCode }}"""
            isCancelable = false
            positiveButton(getString(R.string.Ok)) { dialog ->
                dialog.dismiss()
            }
        }.show()
    }
}