package com.suftnet.v12.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.suftnet.v12.R
import com.suftnet.v12.Store.Store
import com.suftnet.v12.util.isValidEmail
import com.suftnet.v12.util.resetErrorOnChange
import com.suftnet.v12.util.trimmedText
import com.suftnet.v12.viewModel.account.AccountViewModel
import kotlinx.android.synthetic.main.signin_activity.email
import kotlinx.android.synthetic.main.signin_activity.password
import kotlinx.android.synthetic.main.signin_activity.signIn
import kotlinx.android.synthetic.main.signin_activity.signUp
import kotlinx.android.synthetic.main.signup_activity.*
import org.jetbrains.anko.alert

class LoginActivity : BaseAppCompatActivity() {

    private lateinit var viewModel: AccountViewModel
    private lateinit var store: Store
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin_activity)
        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        listener()
        store = Store(this)
    }

    private fun listener()
    {
        signUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        signIn.setOnClickListener {
            if(isValid())
            {
                saveChanges()
            }
        }

        observerError()
        onErrorChange()
    }

    private fun observerError()
    {
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
    }

    private fun saveChanges()
    {
        viewModel.login(email.trimmedText,password.trimmedText).observe(this, Observer {
            store.user = it
            startActivity(Intent(this, SellerDashboardActivity::class.java))
            finish()
        })
    }
    private fun isValid(): Boolean {
        var isValid = true

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
    }
}