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
import com.suftnet.v12.api.model.response.Produce
import com.suftnet.v12.model.Error
import com.suftnet.v12.util.CurrencyFormatter
import com.suftnet.v12.util.Util
import com.suftnet.v12.viewModel.MarketViewModel
import kotlinx.android.synthetic.main.item_detail.*
import org.jetbrains.anko.alert


class ItemDetailActivity : BaseAppCompatActivity()  {
    private lateinit var store: Store
    private lateinit var viewModel: MarketViewModel
    private var phoneNumber : String = ""

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_detail)

        onInit()
    }

    private fun onInit()
    {
        viewModel = ViewModelProvider(this).get(MarketViewModel::class.java)
        store = Store(this)

        load()
        listener()
    }

    private fun listener()
    {
        back_action.setOnClickListener {
            var i = Intent(this@ItemDetailActivity, MarketActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }

        viewModel.loading.observe(this, Observer {
            progress_Bar.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.listError.observe(
                this,
                Observer {
                    onError(it)
                }
        )

        checkout.setOnClickListener {
            val id: String = intent.getStringExtra("id")!!
            if(id.isNotEmpty())
            {
                var i = Intent(this@ItemDetailActivity, CheckoutActivity::class.java)
                i.putExtra("id", id)
                startActivity(i)
            }
        }

        cancel.setOnClickListener {
            var i = Intent(this@ItemDetailActivity, MarketActivity::class.java)
            startActivity(i)
        }

        action_phone.setOnClickListener {
            phone(phoneNumber)
        }

        action_sms.setOnClickListener {
            sms(phoneNumber)
        }
    }

    private fun load()
    {
        val id: String = intent.getStringExtra("id")!!
        if(id.isNotEmpty())
        {
            getBy(id)
        }
    }

    private fun getBy(id :String)
    {
        viewModel.getBy(id).observe(this, Observer {
            onMap(it)
        })
    }

    private fun onMap(produce: Produce)
    {
        name.text = produce.name
        quantity.text = "${produce.quantity.toString()} (${produce.unit})"
        price.text = "₦ ${CurrencyFormatter.format(produce.price, 2)}"
        availableDate.text = produce.availableDate
        description.text = produce.description
        phoneNumber = produce.phoneNumber!!
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