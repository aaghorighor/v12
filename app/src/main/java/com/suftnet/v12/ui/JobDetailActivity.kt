package com.suftnet.v12.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.suftnet.v12.R
import com.suftnet.v12.Store.Store
import com.suftnet.v12.api.model.request.UpdateOrderStatus
import com.suftnet.v12.api.model.response.Order
import com.suftnet.v12.model.Error
import com.suftnet.v12.util.CurrencyFormatter
import com.suftnet.v12.util.OrderStatus
import com.suftnet.v12.util.Util
import com.suftnet.v12.viewModel.OrderViewModel
import kotlinx.android.synthetic.main.job_detail.*
import kotlinx.android.synthetic.main.job_detail.back_action
import kotlinx.android.synthetic.main.job_detail.progress_Bar
import org.jetbrains.anko.alert
import org.jetbrains.anko.textColor

class JobDetailActivity : BaseAppCompatActivity()  {
    companion object {
        const val TAG = "JobDetailActivity"
    }

    private lateinit var viewModel: OrderViewModel
    private lateinit var store: Store
    private var phoneNumber : String = ""
    private var order : Order? = null

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_detail)

        init()
    }

    private fun init()
    {
        store = Store(this)
        viewModel = ViewModelProvider(this).get(OrderViewModel::class.java)

        extra()
        listener()
    }

    private fun listener()
    {
        back_action.setOnClickListener {
            when(intent.getStringExtra("from"))
            {
                "0" -> {
                    var i = Intent(this@JobDetailActivity, PendingJobsActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(i)
                }

                "1" -> {
                    var i = Intent(this@JobDetailActivity, CompletedJobsActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(i)
                }
            }
        }

        action_phone.setOnClickListener {
            phone(phoneNumber)
        }

        action_sms.setOnClickListener {
            sms(phoneNumber)
        }

        order_status.setOnClickListener {
            var updateOrderStatus = UpdateOrderStatus(order!!.id,order!!.statusId)
            viewModel.updateOrderStatus(updateOrderStatus).observe(this@JobDetailActivity, Observer {
                updateStatus(order!!.statusId)
            })
        }

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

    private fun extra()
    {
        order = intent.getSerializableExtra("order") as Order
        onMap(order)
    }

    private fun onMap(order: Order?)
    {
        if(order != null)
        {
            name.text = order.itemName
            quantity.text = "${order.quantity.toString()} (${order.unit})"
            price.text = "₦ ${CurrencyFormatter.format(order.amountPaid.toDouble(), 2)}"
            availableDate.text = order.availableDate
            drop_off.text = order.deliveryAddress
            pick_up.text = order.collectionAddress
            order_status.text = order.status

            phoneNumber = order.phoneNumber

            status(order.statusId)
        }
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

    private fun status(statusId : String)
    {
        when(statusId)
        {
            OrderStatus.pending.toLowerCase() ->{
                @Suppress("DEPRECATION")
                order_status.background = resources.getDrawable(R.drawable.btn_rounded_indigo)
            }

            OrderStatus.processing.toLowerCase() ->{
                @Suppress("DEPRECATION")
                order_status.background = resources.getDrawable(R.drawable.btn_rounded_red)
            }

            OrderStatus.delivery.toLowerCase() ->{
                @Suppress("DEPRECATION")
                order_status.background = resources.getDrawable(R.drawable.btn_rounded_green_300)
            }

            OrderStatus.completed.toLowerCase() ->{
                @Suppress("DEPRECATION")
                order_status.background = resources.getDrawable(R.drawable.btn_rounded_tag)
                @Suppress("DEPRECATION")
                order_status.textColor = resources.getColor(R.color.grey_90)
            }
        }
    }

    private fun updateStatus(statusId : String)
    {
        when(statusId)
        {
            OrderStatus.processing.toLowerCase() ->{
                @Suppress("DEPRECATION")
                order_status.background = resources.getDrawable(R.drawable.btn_rounded_green_300)
                order_status.text = "Delivery"
            }

            OrderStatus.delivery.toLowerCase() ->{
                @Suppress("DEPRECATION")
                order_status.background = resources.getDrawable(R.drawable.btn_rounded_tag)
                @Suppress("DEPRECATION")
                order_status.textColor = resources.getColor(R.color.grey_90)
                order_status.text = "Completed"
            }
        }
    }


    private fun onError(it : Error)
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