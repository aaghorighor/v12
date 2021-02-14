package com.suftnet.v12.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.material.snackbar.Snackbar
import com.suftnet.v12.Adapter.DriverAdapter
import com.suftnet.v12.R
import com.suftnet.v12.api.model.request.CreateDelivery
import com.suftnet.v12.api.model.response.Driver
import com.suftnet.v12.api.model.response.Order
import com.suftnet.v12.model.Error
import com.suftnet.v12.util.Util
import com.suftnet.v12.viewModel.DriverViewModel
import kotlinx.android.synthetic.main.driver_placeholder.*
import org.jetbrains.anko.alert

class DriverActivity : BaseAppCompatActivity() {
    companion object {
        const val TAG = "DriverActivity"
    }

    private lateinit var viewModel: DriverViewModel
    private lateinit var driverAdapter : DriverAdapter
    private lateinit var recyclerView : RecyclerView
    private val context : Context = this
    private var totalCount = 0.0
    private var snackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.driver_placeholder)

        init()
        handler(1)
    }

    private fun init()
    {
        setToolBar("Logistic")
        viewModel = ViewModelProvider(this).get(DriverViewModel::class.java)

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.layoutManager = LinearLayoutManager(context);
        recyclerView.setHasFixedSize(true);

        driverAdapter = DriverAdapter(context,recyclerView, ArrayList<Driver>())
        recyclerView.adapter = driverAdapter

        listener()
    }

    private fun setToolBar(title: String) {

        main_title.text =title
    }

    private fun listener()
    {
        back_action.setOnClickListener {

            var i = Intent(this@DriverActivity, SellerOrderDetailActivity::class.java)
            i.putExtra("order", intent.getSerializableExtra("order") )
            i.putExtra("from",  intent.getStringExtra("from"))
            startActivity(i)
        }

        swipe_refresh.setOnRefreshListener(OnRefreshListener {
            swipe_refresh.isRefreshing = false
            driverAdapter.reset()
            handler(1)
        })

        driverAdapter.setOnItemClickListener(object:DriverAdapter.OnItemClickListener {
            override fun onAdd(driver: Driver, position: Int) {
                var order = intent.getSerializableExtra("order") as Order
                var createDelivery = CreateDelivery(driver.id, order.id)
                viewModel.createDelivery(createDelivery).observe(this@DriverActivity, Observer {

                    val intent = Intent()

                    intent.putExtra("names", "${driver.firstName} ${driver.lastName}")
                    intent.putExtra("mobile", driver.phoneNumber)
                    intent.putExtra("email", driver.email)

                    setResult(Activity.RESULT_OK, intent)
                    finish()
                })
            }
            override fun onSms(driver: Driver, position: Int) {
                sms(driver.phoneNumber)
            }
            override fun onPhone(driver: Driver, position: Int) {
                phone(driver.phoneNumber)
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
        viewModel.fetch().observe(this, Observer {
                        if(!it.isNullOrEmpty())
            {
                add(it.toMutableList())
            }else {
                showEmptyLayout(true)
            }
        })
    }

    private fun add(drivers: MutableList<Driver>) {

        recyclerView.visibility = View.VISIBLE
        driverAdapter.add(drivers)
        if (drivers.size == 0) {
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
            recyclerView.scrollToPosition(driverAdapter.getItemCount() - 1)
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
            driverAdapter.setLoading()
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

    private fun phone(phoneNumber: String) {
        if (!Util.isValidPhoneNumber(phoneNumber)) {
            toastShort("Phone Number not Invalid")
            return
        }
        val j = Intent(Intent.ACTION_DIAL)
        j.data = Uri.parse("tel:" + phoneNumber)
        startActivity(j)
    }

    private fun sms(phoneNumber: String) {
        if (!Util.isValidPhoneNumber(phoneNumber)) {
            toastShort("Phone Number not Invalid")
            return
        }
        val j = Intent(Intent.ACTION_SENDTO)
        j.data = Uri.parse("smsto:"  + phoneNumber);
        startActivity(j)
    }
}