package com.suftnet.v12.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.suftnet.v12.R
import com.suftnet.v12.Store.Store
import com.suftnet.v12.api.model.request.CreateUser
import com.suftnet.v12.util.isValidEmail
import com.suftnet.v12.util.resetErrorOnChange
import com.suftnet.v12.util.trimmedText
import com.suftnet.v12.viewModel.account.SellerViewModel
import kotlinx.android.synthetic.main.signin_activity.signIn
import kotlinx.android.synthetic.main.signin_activity.signUp
import kotlinx.android.synthetic.main.signup_activity.*
import org.jetbrains.anko.alert

class SignUpActivity : BaseAppCompatActivity() {

    private lateinit var viewModel: SellerViewModel
    private lateinit var store: Store

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_activity)
        viewModel = ViewModelProvider(this).get(SellerViewModel::class.java)
        listener()
        store = Store(this)
    }

    private fun listener()
    {
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

                alert {
                    title = "Error"
                    message = it.messages
                    isCancelable = false
                    positiveButton(getString(R.string.Ok)) { dialog ->
                        dialog.dismiss()
                    }
                }.show()
            }
        )

        onErrorChange()
    }

    private fun saveChanges()
    {
        var createUser = CreateUser(email.trimmedText,
            firstName.trimmedText,
            lastName.trimmedText, mobile.trimmedText,
            password.trimmedText,true,"", "")

        viewModel.create(createUser).observe(this, Observer {

            store.user = it

            alert {
                title = "Success"
                message = ""+    store.user!!.token
                isCancelable = false
                positiveButton(getString(R.string.Ok)) { dialog ->
                    dialog.dismiss()
                }
            }.show()
        })
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
}