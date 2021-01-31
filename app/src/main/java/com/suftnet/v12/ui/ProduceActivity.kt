package com.suftnet.v12.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.suftnet.v12.Adapter.ProduceAdapter
import com.suftnet.v12.R
import com.suftnet.v12.api.model.response.Produce
import com.suftnet.v12.viewModel.produce.ProduceViewModel

import kotlinx.android.synthetic.main.produce_placeholder.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.alert

@Suppress("DEPRECATION", "DEPRECATED_IDENTITY_EQUALS")
class ProduceActivity : BaseAppCompatActivity() {
    companion object {
        const val TAG = "MobileLoggerActivity"
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

        viewModel = ViewModelProvider(this).get(ProduceViewModel::class.java)
        setToolBar("Produces")

        init()
        listener()
        handler(1)
    }

    private fun setToolBar(title: String) {
        back_action.setOnClickListener {
            onBackPressed()
        }
        main_title.text =title
    }

    private fun init()
    {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.layoutManager = LinearLayoutManager(context);
        recyclerView.setHasFixedSize(true);

        produceAdapter = ProduceAdapter(context,recyclerView, ArrayList<Produce>())
        recyclerView.adapter = produceAdapter
    }

    private fun listener()
    {
        swipe_refresh.setOnRefreshListener(OnRefreshListener {
            swipe_refresh.isRefreshing = false
            produceAdapter.reset()
            handler(1)
        })

        produceAdapter.setOnItemClickListener(object:ProduceAdapter.OnItemClickListener {
            override fun onDelete(mobileLog: Produce, position: Int) {
                TODO("Not yet implemented")
            }

            override fun onEdit(mobileLog: Produce, position: Int) {
                TODO("Not yet implemented")
            }
        })

        viewModel.loading.observe(this, Observer {
            progressBar.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.listError.observe(
            this,
            Observer {
                showFailedView(it.messages)
            }
        )

        viewModel.deleteError.observe(
            this,
            Observer {
                alert {
                    title = getString(R.string.ErrorMessages)
                    message = """${it.messages}${{ it.statusCode }}"""
                    isCancelable = false
                    positiveButton(getString(R.string.Ok)) { dialog ->
                        dialog.dismiss()
                    }
                }.show()
            }
        )

    }

    private fun loadPage(pageNo :Int)
    {
        viewModel.fetch().observe(this, Observer {
            Log.d("________","size" + it!!.size)
            if(!it!!.isNullOrEmpty())
            {
                add(it.toMutableList())
            }else {
                showEmptyLayout(true)
            }
        })
    }

    private fun add(produces: MutableList<Produce>) {

        Log.d("________","observe" + produces!!.size)

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
        Handler().postDelayed(Runnable { loadPage(page_no) }, 10)
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
}