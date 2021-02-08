package com.suftnet.v12.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.suftnet.v12.R
import com.suftnet.v12.Store.Store
import com.suftnet.v12.api.model.request.CreateUser
import com.suftnet.v12.model.Error
import com.suftnet.v12.util.isValidEmail
import com.suftnet.v12.util.resetErrorOnChange
import com.suftnet.v12.util.trimmedText
import com.suftnet.v12.viewModel.account.AccountViewModel
import kotlinx.android.synthetic.main.signin_activity.signIn
import kotlinx.android.synthetic.main.signin_activity.signUp
import kotlinx.android.synthetic.main.signup_activity.*
import org.jetbrains.anko.alert

class SignUpActivity : BaseAppCompatActivity() {

    private lateinit var viewModel: AccountViewModel
    private lateinit var store: Store
    private var userType :String =""

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_activity)
        init()
    }

    private fun init()
    {
        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        store = Store(this)
        listener()
    }

    private fun listener()
    {
        user_group.setOnCheckedChangeListener { group, checkedId ->
                userType = ""
            if(checkedId != -1)
            {
                val chip: Chip = group.findViewById(checkedId)
                userType = "" + chip.text
            }
        }

        signUp.setOnClickListener {

            if(isValid())
            {
                saveChanges()
            }
        }

        signIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        viewModel.loading.observe(this, Observer {
            progressBar.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.error.observe(
            this,
            Observer {
                onError(it)
            }
        )

        onErrorChange()
    }

    private fun saveChanges()
    {
        var createUser = CreateUser(email.trimmedText,
                firstName.trimmedText, lastName.trimmedText, mobile.trimmedText,
            password.trimmedText,true,"", "")

        when(userType)
        {
            "Farmer" -> {
                viewModel.createSeller(createUser)
            }
            "Buyer" -> {
                viewModel.createBuyer(createUser)
            }
            "Logistic" ->{
                viewModel.createDriver(createUser)
            }else -> {  println("default")      }

        }

        viewModel.user.observe(this, Observer {
            store.user = it
            redirect()
        })
    }

    private fun redirect()
    {
        when(userType)
        {
            "Farmer" -> {
                startActivity(Intent(this, SellerDashboardActivity::class.java))
                finish()
            }
            "Buyer" -> {
                startActivity(Intent(this, BuyerDashboardActivity::class.java))
                finish()
            }
            "Logistic" ->{
                startActivity(Intent(this, DriverDashboardActivity::class.java))
                finish()
            }else -> {  println("default")      }

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

        if (mobile.trimmedText.isEmpty()) {
            mobile.error = "Mobile is required"
            isValid = false
        }

        if (email.trimmedText.isEmpty()) {
            email.error = "Email is required"
            isValid = false
        }else if(!email.trimmedText.isValidEmail())
        {
            email.error = "Email Address is not valid"
            isValid = false
        }

        if (password.trimmedText.isEmpty()) {
            password.error = "Password is required"
            isValid = false
        } else if (password.trimmedText.length < 6) {
            password.error = "At least 6 characters"
            isValid = false
        }

        if (userType.isEmpty()) {

            alert {
                title = "Error"
                message = "Please select UserType"
                isCancelable = false
                positiveButton(getString(R.string.Ok)) { dialog ->
                    dialog.dismiss()
                }
            }.show()

            isValid = false
        }

        return isValid
    }
    private fun onErrorChange()
    {
        email.resetErrorOnChange(email)
        password.resetErrorOnChange(password)
        firstName.resetErrorOnChange(firstName)
        lastName.resetErrorOnChange(lastName)
        mobile.resetErrorOnChange(mobile)
    }

    private fun onError(it :Error)
    {
        alert {
            title = "Error"
            message = it.messages
            isCancelable = false
            positiveButton(getString(R.string.Ok)) { dialog ->
                dialog.dismiss()
            }
        }.show()
    }
}