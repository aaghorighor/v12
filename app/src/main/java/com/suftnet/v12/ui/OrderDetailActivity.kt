package com.suftnet.v12.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.suftnet.v12.R
import com.suftnet.v12.Store.Store
import com.suftnet.v12.api.model.response.Order
import com.suftnet.v12.util.CurrencyFormatter
import com.suftnet.v12.util.OrderStatus
import com.suftnet.v12.util.Util
import kotlinx.android.synthetic.main.order_detail.*
import org.jetbrains.anko.textColor

class OrderDetailActivity : BaseAppCompatActivity()  {
    private lateinit var store: Store
    private var phoneNumber : String = ""

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_detail)

        onInit()
    }

    private fun onInit()
    {
        store = Store(this)
        extra()
        listener()
    }

    private fun listener()
    {
        back_action.setOnClickListener {

            when(intent.getStringExtra("from"))
            {
                "0" -> {
                    var i = Intent(this@OrderDetailActivity, PendingOrderActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(i)
                }

                "1" -> {
                    var i = Intent(this@OrderDetailActivity, CompletedOrderActivity::class.java)
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
    }

    private fun extra()
    {
        val order = intent.getSerializableExtra("order") as Order
        onMap(order)
    }

    private fun onMap(order: Order)
    {
        name.text = order.itemName
        quantity.text = "${order.quantity.toString()} (${order.unit})"
        price.text = "₦ ${CurrencyFormatter.format(order.amountPaid.toDouble(), 2)}"
        availableDate.text = "Available Date :" + order.availableDate

        order_id.text = "Order Id :" + order.id
        order_date.text = "Order Date :" + order.createdAt
        drop_off.text = order.deliveryAddress
        pick_up.text = order.collectionAddress
        order_status.text = order.status

        phoneNumber = order.phoneNumber!!

        status(order.statusId)
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

}