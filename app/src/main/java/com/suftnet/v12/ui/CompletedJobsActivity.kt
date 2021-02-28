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
import com.suftnet.v12.Adapter.JobAdapter
import com.suftnet.v12.R
import com.suftnet.v12.api.model.response.Order
import com.suftnet.v12.model.Error
import com.suftnet.v12.viewModel.DriverViewModel
import kotlinx.android.synthetic.main.job_placeholder.*
import org.jetbrains.anko.alert

class CompletedJobsActivity : BaseAppCompatActivity() {
    companion object {
        const val TAG = "CompletedJobsActivity"
    }

    private lateinit var viewModel: DriverViewModel
    private lateinit var jobAdapter : JobAdapter
    private lateinit var recyclerView : RecyclerView
    private val context : Context = this
    private var totalCount = 0.0
    private var snackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_placeholder)

        init()
        handler(1)
    }

    private fun init()
    {
        setToolBar("Jobs")
        viewModel = ViewModelProvider(this).get(DriverViewModel::class.java)

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.layoutManager = LinearLayoutManager(context);
        recyclerView.setHasFixedSize(true);

        jobAdapter = JobAdapter(context,recyclerView, ArrayList<Order>())
        recyclerView.adapter = jobAdapter

        listener()
    }

    private fun setToolBar(title: String) {

        main_title.text =title
    }

    private fun listener()
    {
        back_action.setOnClickListener {
            var i = Intent(this@CompletedJobsActivity, DriverDashboardActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(i)
        }

        swipe_refresh.setOnRefreshListener(OnRefreshListener {
            swipe_refresh.isRefreshing = false
            jobAdapter.reset()
            handler(1)
        })

        jobAdapter.setOnItemClickListener(object:JobAdapter.OnItemClickListener {
            override fun onView(order: Order, position: Int) {
                var i = Intent(this@CompletedJobsActivity, JobDetailActivity::class.java)
                i.putExtra("order", order)
                i.putExtra("from", "1")
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
        viewModel.completedJobs().observe(this, Observer {
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
        jobAdapter.add(orders)
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
            recyclerView.scrollToPosition(jobAdapter.getItemCount() - 1)
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
            jobAdapter.setLoading()
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