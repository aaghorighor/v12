package com.suftnet.v12.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.suftnet.v12.R
import com.suftnet.v12.Store.Store
import com.suftnet.v12.model.Error
import com.suftnet.v12.util.isValidEmail
import com.suftnet.v12.util.resetErrorOnChange
import com.suftnet.v12.util.trimmedText
import com.suftnet.v12.viewModel.BuyerViewModel
import kotlinx.android.synthetic.main.edit_buyer.*
import org.jetbrains.anko.alert

class EditBuyerActivity : BaseAppCompatActivity()  {
    private lateinit var store: Store
    private lateinit var viewModel: BuyerViewModel
    private var buyer :com.suftnet.v12.api.model.response.Buyer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_buyer)

        onInit()
    }

    private fun onInit()
    {
        viewModel = ViewModelProvider(this).get(BuyerViewModel::class.java)
        store = Store(this)

        load()
        listener()
    }

    private fun listener()
    {
        back__action.setOnClickListener {
            var i = Intent(this@EditBuyerActivity, BuyerDashboardActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
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

        save_changes.setOnClickListener {

            if(isValid())
            {
                if(buyer != null)
                {
                    buyer!!.shelfLife = shelf_life.trimmedText
                    buyer!!.rejection = rejection.trimmedText

                    viewModel.edit(buyer!!).observe(
                            this,
                            Observer {
                                onSuccess()
                            }
                    )
                }

            }
        }

        cancel.setOnClickListener {
            var i = Intent(this@EditBuyerActivity, BuyerDashboardActivity::class.java)
            startActivity(i)
        }

        onErrorChange();
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
        viewModel.fetchByUser(id).observe(this, Observer {
           buyer = it
            onMap()
        })
    }

    private fun onMap()
    {
        if(buyer != null)
        {
            firstName.setText(buyer!!.firstName)
            lastName.setText(buyer!!.lastName)
            phoneNumber.setText(buyer!!.phoneNumber)
            email.setText(buyer!!.email)
            description.setText(buyer!!.description)
            shelf_life.setText(buyer!!.shelfLife)
            rejection.setText(buyer!!.rejection)
        }
    }

    private fun isValid(): Boolean {
        var isValid = true

        if (firstName.trimmedText.isEmpty()) {
            firstName.error = "FirstName is required"
            isValid = false
        }

        if (lastName.trimmedText.isEmpty()) {
            lastName.error = "LastName is required"
            isValid = false
        }

        if (phoneNumber.trimmedText.isEmpty()) {
            phoneNumber.error = "Mobile is required"
            isValid = false
        }

        if (email.trimmedText.isEmpty()) {
            email.error = "Email is required"
            isValid = false
        }

        if(!email.trimmedText.isValidEmail())
        {
            email.error = "Email Address is not valid"
            isValid = false
        }


        return isValid
    }
    private fun onErrorChange()
    {
        email.resetErrorOnChange(email)
        firstName.resetErrorOnChange(firstName)
        lastName.resetErrorOnChange(lastName)
        phoneNumber.resetErrorOnChange(phoneNumber)
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
            title = getString(R.string.save_confirmation)
            message = getString(R.string.save_messages)
            isCancelable = false
            positiveButton(getString(R.string.Ok)) { dialog ->
                dialog.dismiss()

                var i = Intent(this@EditBuyerActivity, BuyerDashboardActivity::class.java)
                startActivity(i)
            }
        }.show()
    }

}