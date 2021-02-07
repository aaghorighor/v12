package com.suftnet.v12.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.suftnet.v12.R
import com.suftnet.v12.Store.Store
import com.suftnet.v12.api.model.response.Order
import com.suftnet.v12.util.CurrencyFormatter
import com.suftnet.v12.util.OrderStatus
import com.suftnet.v12.util.Util
import kotlinx.android.synthetic.main.order_detail.*
import kotlinx.android.synthetic.main.order_detail.action_phone
import kotlinx.android.synthetic.main.order_detail.action_sms
import kotlinx.android.synthetic.main.order_detail.availableDate
import kotlinx.android.synthetic.main.order_detail.back_action
import kotlinx.android.synthetic.main.order_detail.drop_off
import kotlinx.android.synthetic.main.order_detail.name
import kotlinx.android.synthetic.main.order_detail.order_date
import kotlinx.android.synthetic.main.order_detail.order_id
import kotlinx.android.synthetic.main.order_detail.order_status
import kotlinx.android.synthetic.main.order_detail.pick_up
import kotlinx.android.synthetic.main.order_detail.price
import kotlinx.android.synthetic.main.order_detail.quantity
import kotlinx.android.synthetic.main.seller_order_detail.*
import org.jetbrains.anko.textColor

class SellerOrderDetailActivity : BaseAppCompatActivity()  {

    companion object {
        private const val ADD_OPERATOR = 100
    }

    private lateinit var store: Store
    private var phoneNumber : String = ""

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seller_order_detail)

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
        add_action.setOnClickListener {
            var i = Intent(this@SellerOrderDetailActivity, DriverActivity::class.java)
            i.putExtra("order",  intent.getSerializableExtra("order"))
            i.putExtra("from",  intent.getSerializableExtra("from"))
            startActivityForResult(i,ADD_OPERATOR)
        }

        back_action.setOnClickListener {
            when(intent.getStringExtra("from"))
            {
                "0" -> {
                    var i = Intent(this@SellerOrderDetailActivity, SellerPendingOrderActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(i)
                }

                "1" -> {
                    var i = Intent(this@SellerOrderDetailActivity, SellerCompletedOrderActivity::class.java)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_OPERATOR && resultCode == Activity.RESULT_OK) {
            names.text = data?.extras!!["names"] as String
            mobile.text = data?.extras!!["mobile"] as String
            email.text = data?.extras!!["email"] as String
        }
    }

}