package com.suftnet.v12.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.suftnet.v12.R
import com.suftnet.v12.Store.Store
import com.suftnet.v12.api.model.request.CreateOrder
import com.suftnet.v12.api.model.response.Produce
import com.suftnet.v12.model.Error
import com.suftnet.v12.util.CurrencyFormatter
import com.suftnet.v12.util.trimmedText
import com.suftnet.v12.viewModel.OrderViewModel
import com.suftnet.v12.viewModel.AnswerViewModel
import com.suftnet.v12.viewModel.MarketViewModel
import kotlinx.android.synthetic.main.checkout.*

import org.jetbrains.anko.alert

class CheckoutActivity : BaseAppCompatActivity()  {
    private lateinit var store: Store
    private lateinit var viewModel: MarketViewModel
    private lateinit var orderViewModel: OrderViewModel

    private var produceId : String = ""
    private var amountPaid : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checkout)

        onInit()
    }

    private fun onInit()
    {
        viewModel = ViewModelProvider(this).get(MarketViewModel::class.java)
        orderViewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        store = Store(this)

        load()
        listener()
    }

    private fun listener()
    {
        checkout.setOnClickListener {
            if(isValid())
            {
                var createOrder = CreateOrder(city.trimmedText,state.trimmedText,country.trimmedText, address.trimmedText, store.user!!.id, produceId, amountPaid)
                orderViewModel.create(createOrder).observe(this, Observer {
                    onSuccess()
                })
            }
        }
        back__action.setOnClickListener {
            onBack()
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

        orderViewModel.loading.observe(this, Observer {
            progress_Bar.visibility = if(it) View.VISIBLE else View.GONE
        })
        orderViewModel.error.observe(
                this,
                Observer {
                    onError(it)
                }
        )
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

        produceId = produce.id
        amountPaid =produce.price
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

    private fun onSuccess()
    {
        alert {
            title = getString(R.string.order_confirmation)
            message = getString(R.string.order_success)
            isCancelable = false
            positiveButton(getString(R.string.Ok)) { _ ->
                var i = Intent(this@CheckoutActivity, MarketActivity::class.java)
                startActivity(i)
            }
        }.show()
    }

    private fun isValid(): Boolean {
        var isValid = true

        if (address.trimmedText.isEmpty()) {
            address.error = "Address is required"
            isValid = false
        }

        if (city.trimmedText.isEmpty()) {
            city.error = "City is required"
            isValid = false
        }

        if (state.trimmedText.isEmpty()) {
            state.error = "State is required"
            isValid = false
        }

        if (country.trimmedText.isEmpty()) {
            country.error = "Country is required"
            isValid = false
        }

        return isValid
    }
    private fun onBack()
    {
        val id: String = intent.getStringExtra("id")!!
        var i = Intent(this@CheckoutActivity, ItemDetailActivity::class.java)
        i.putExtra("id", id)
        startActivity(i)
    }
}