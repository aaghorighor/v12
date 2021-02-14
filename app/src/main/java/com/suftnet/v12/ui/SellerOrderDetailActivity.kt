package com.suftnet.v12.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.suftnet.v12.R
import com.suftnet.v12.Store.Store
import com.suftnet.v12.api.model.response.Order
import com.suftnet.v12.model.Error
import com.suftnet.v12.util.CurrencyFormatter
import com.suftnet.v12.util.OrderStatus
import com.suftnet.v12.util.Util
import com.suftnet.v12.viewModel.DriverViewModel
import kotlinx.android.synthetic.main.driver_placeholder.*
import kotlinx.android.synthetic.main.driver_placeholder.back_action
import kotlinx.android.synthetic.main.driver_placeholder.progress_Bar
import kotlinx.android.synthetic.main.seller_order_detail.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.textColor

class SellerOrderDetailActivity : BaseAppCompatActivity()  {

    companion object {
        private const val ADD_OPERATOR = 100
        const val TAG = "SellerOrderDetailActivity"
    }

    private lateinit var viewModel: DriverViewModel

    private lateinit var store: Store
    private var phoneNumber : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seller_order_detail)

        init()
    }

    private fun init()
    {
        store = Store(this)
        viewModel = ViewModelProvider(this).get(DriverViewModel::class.java)
        extra()
        listener()
    }

    private fun listener()
    {
        add_action.setOnClickListener {
            var i = Intent(this@SellerOrderDetailActivity, DriverActivity::class.java)
            i.putExtra("order",  intent.getSerializableExtra("order"))
            i.putExtra("from",  intent.getStringExtra("from"))
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
        val order = intent.getSerializableExtra("order") as Order
        onMap(order)
    }

    private fun onMap(order: Order)
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
        loadDriver(order.id)
    }
    private fun loadDriver(orderId :String)
    {
        viewModel.fetchByOrder(orderId).observe(
                this,
                Observer {

                    if(it != null) if(it.firstName != null) {
                        names.text = "${it.firstName} ${it.lastName} "
                        mobile.text = it.phoneNumber
                        email.text = it.email

                        add_action.text ="Change"
                    }
                }
        )
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

            if(data != null)
            {
                names.text = data.extras!!["names"] as String
                mobile.text = data.extras!!["mobile"] as String
                email.text = data.extras!!["email"] as String
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