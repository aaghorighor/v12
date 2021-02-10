package com.suftnet.v12.ui

import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.material.snackbar.Snackbar
import com.suftnet.v12.Adapter.ProduceAdapter
import com.suftnet.v12.R
import com.suftnet.v12.api.model.request.DeleteProduce
import com.suftnet.v12.api.model.response.Produce
import com.suftnet.v12.model.Error
import com.suftnet.v12.viewModel.produce.ProduceViewModel
import kotlinx.android.synthetic.main.produce_placeholder.*
import org.jetbrains.anko.alert

@Suppress("DEPRECATION", "DEPRECATED_IDENTITY_EQUALS")
class ProduceActivity : BaseAppCompatActivity() {
    companion object {
        const val TAG = "ProduceActivity"
    }

    private lateinit var viewModel: ProduceViewModel
    private lateinit var produceAdapter : ProduceAdapter
    private lateinit var recyclerView : RecyclerView
    private val context : Context = this
    private var totalCount = 0.0
    private var snackBar: Snackbar? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.produce_placeholder)

        init()
        handler(1)
    }

    private fun init()
    {
        setToolBar("Produce")
        viewModel = ViewModelProvider(this).get(ProduceViewModel::class.java)

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.layoutManager = LinearLayoutManager(context);
        recyclerView.setHasFixedSize(true);

        produceAdapter = ProduceAdapter(context,recyclerView, ArrayList<Produce>())
        recyclerView.adapter = produceAdapter

        listener()
    }

    private fun setToolBar(title: String) {

        main_title.text =title
    }

    private fun listener()
    {
        back_action.setOnClickListener {
            var i = Intent(this@ProduceActivity, SellerDashboardActivity::class.java)
            i.flags = FLAG_ACTIVITY_NEW_TASK
            i.flags = FLAG_ACTIVITY_CLEAR_TOP
            i.flags = FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }

        swipe_refresh.setOnRefreshListener(OnRefreshListener {
            swipe_refresh.isRefreshing = false
            produceAdapter.reset()
            handler(1)
        })

        produceAdapter.setOnItemClickListener(object:ProduceAdapter.OnItemClickListener {
            override fun onDelete(produce: Produce, position: Int) {
                delete(produce,position)
            }

            override fun onEdit(produce: Produce, position: Int) {

                var i = Intent(this@ProduceActivity, CreateProduceActivity::class.java)
                i.putExtra("title","Edit")
                i.putExtra("from","0")
                i.putExtra("produce", produce)

                startActivity(i)
            }
        })

        viewModel.loading.observe(this, Observer {
            progressBar.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.listError.observe(
            this,
            Observer {
                onError(it)
            }
        )

        action_add.setOnClickListener {
            var i = Intent(this, CreateProduceActivity::class.java)
            i.putExtra("title","Create")
            i.putExtra("from","1")
            startActivity(i)
        }
    }

    private fun loadPage()
    {
        viewModel.fetch().observe(this, Observer {
                        if(!it.isNullOrEmpty())
            {
                add(it.toMutableList())
            }else {
                showEmptyLayout(true)
            }
        })
    }

    private fun add(produces: MutableList<Produce>) {

        recyclerView.visibility = View.VISIBLE
        produceAdapter.add(produces)
        if (produces.size == 0) {
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
            recyclerView.scrollToPosition(produceAdapter.getItemCount() - 1)
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
            produceAdapter.setLoading()
        }
        Handler().postDelayed(Runnable { loadPage() }, 10)
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

    private fun delete(produce :Produce, position : Int)
    {
        alert {
            title = getString(R.string.delete_confirmation)
            message = getString(R.string.delete_produce)
            isCancelable = false
            positiveButton(getString(R.string.Ok)) {
                viewModel.delete(DeleteProduce(produce.id)).observe(this@ProduceActivity,Observer{
                   produceAdapter.remove(produce,position)
                })
            }
            negativeButton(getString(R.string.cancel)) {dialog ->
                dialog.dismiss()
            }
        }.show()
    }
}