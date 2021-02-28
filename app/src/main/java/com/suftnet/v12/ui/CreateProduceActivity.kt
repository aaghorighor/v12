package com.suftnet.v12.ui

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.content.Intent.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.suftnet.v12.R
import com.suftnet.v12.api.model.request.CreateProduce
import com.suftnet.v12.api.model.request.EditProduce
import com.suftnet.v12.api.model.response.Produce
import com.suftnet.v12.model.Error
import com.suftnet.v12.util.Util
import com.suftnet.v12.util.resetErrorOnChange
import com.suftnet.v12.util.trimmedText
import com.suftnet.v12.viewModel.produce.ProduceViewModel
import kotlinx.android.synthetic.main.create_produce.*
import org.jetbrains.anko.alert
import java.text.SimpleDateFormat
import java.util.*

class CreateProduceActivity : BaseAppCompatActivity() {
    companion object {
        const val TAG = "CreateProduceActivity"
    }
    private var from :String =""
    private var id :String =""

    private lateinit var viewModel: ProduceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_produce)

        init()
    }

    private fun init()
    {
        viewModel = ViewModelProvider(this).get(ProduceViewModel::class.java)
        extra()
        listener()
    }

    private fun setToolBar(title: String) {
        main__title.text =title
    }

    private fun listener()
    {
        save_changes.setOnClickListener {

            if(isValid())
            {
                when(from)
                {
                    "0" -> {
                        var editProduce = EditProduce(id,name.trimmedText,
                                description.trimmedText,
                                quantity.trimmedText.toInt(),
                                price.trimmedText.toDouble(),
                                publish.isChecked,
                                unit.trimmedText,
                                availableDate.trimmedText,
                                city.trimmedText,
                                state.trimmedText,
                                country.trimmedText,
                                address.trimmedText)
                        viewModel.edit(editProduce).observe(this, Observer {
                            oneEdit()
                        })
                    }
                    "1"-> {

                        var createProduce = CreateProduce(name.trimmedText,
                                description.trimmedText,
                                quantity.trimmedText.toInt(),
                                price.trimmedText.toDouble(),
                                publish.isChecked,
                                unit.trimmedText,
                                availableDate.trimmedText,
                                city.trimmedText,
                                state.trimmedText,
                                country.trimmedText,
                                address.trimmedText)
                        viewModel.create(createProduce).observe(this, Observer {
                            onCreated()
                        })
                    }
                }

            }
        }

        back__action.setOnClickListener {
            var i = Intent(this@CreateProduceActivity, ProduceActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(i)
        }

        cancel.setOnClickListener {
            var i = Intent(this@CreateProduceActivity, ProduceActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(i)
        }
        availableDate.setOnClickListener {
            onShowDateDialogPicker()
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

        onErrorChange()
    }

    private fun isValid(): Boolean {
        var isValid = true

        if (name.trimmedText.isEmpty()) {
            name.error = "Name is required"
            isValid = false
        }

        if (quantity.trimmedText.isEmpty()) {
            quantity.error = "Quantity is required"
            isValid = false
        }

        if (price.trimmedText.isEmpty()) {
            price.error = "Price is required"
            isValid = false
        }

        if (availableDate.trimmedText.isEmpty()) {
            availableDate.error = "Available Date is required"
            isValid = false
        }

        if (unit.trimmedText.isEmpty()) {
            unit.error = "Unit is required"
            isValid = false
        }

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
    private fun onErrorChange()
    {
        name.resetErrorOnChange(name)
        quantity.resetErrorOnChange(quantity)
        price.resetErrorOnChange(price)
        availableDate.resetErrorOnChange(availableDate)
        unit.resetErrorOnChange(unit)
    }
    private fun onShowDateDialogPicker() {
        val calendar =  Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = monthOfYear
                calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                val formatted =
                    SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val date = formatted.format(calendar.time)
                availableDate.setText(date)
                description.requestFocus()
            },
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            datePickerDialog.datePicker.touchables[1].performClick()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            datePickerDialog.datePicker.touchables[0].performClick()
        }
        datePickerDialog.show()
    }
    private fun onCreated()
    {
        alert {
            title = getString(R.string.produce_created_successfully)
            message = getString(R.string.created_another_produce)
            isCancelable = false
            positiveButton(getString(R.string.Ok)) { dialog ->
                dialog.dismiss()
                finish();
                startActivity(getIntent());
            }
            negativeButton(getString(R.string.cancel)) {dialog ->
                dialog.dismiss()
                var i = Intent(this@CreateProduceActivity, ProduceActivity::class.java)
                i.flags = FLAG_ACTIVITY_NEW_TASK
                i.flags = FLAG_ACTIVITY_CLEAR_TOP
                i.flags = FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
            }
        }.show()
    }
    private fun oneEdit()
    {
        alert {
            title = getString(R.string.confirmation)
            message = getString(R.string.produce_edited)
            isCancelable = false
            positiveButton(getString(R.string.Ok)) { dialog ->
                dialog.dismiss()
                var i = Intent(this@CreateProduceActivity, ProduceActivity::class.java)
                i.flags = FLAG_ACTIVITY_NEW_TASK
                i.flags = FLAG_ACTIVITY_CLEAR_TOP
                i.flags = FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
            }
        }.show()
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
    private fun extra()
    {
        from = intent.getStringExtra("from")!!
        intent.getStringExtra("title")?.let { setToolBar(it) }

        when(from)
        {
            "0" -> {
                val produce: Produce = intent.getParcelableExtra("produce")!!
                onMap(produce)
            }
            "1"-> {

            }
        }
    }
    private fun onMap(produce: Produce)
    {
        id = produce.id
        name.setText(produce.name)
        quantity.setText(produce.quantity.toString())
        price.setText(produce.price.toString())
        availableDate.setText(produce.availableDate.trim())
        description.setText(produce.description)

        city.setText(produce.city)
        state.setText(produce.state)
        address.setText(produce.address)
        country.setText(produce.country)
        unit.setText(produce.unit)

        publish.isChecked = produce.active
    }
}