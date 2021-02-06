package com.suftnet.v12.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.suftnet.v12.MyApplication
import com.suftnet.v12.R
import com.suftnet.v12.Store.Store

@Suppress("DEPRECATION")
class SplashActivity : BaseAppCompatActivity() {
    private lateinit var store: Store

    companion object {
        private val TIME_OUT: Long = 2000
    }
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        fullScreen()
        store = Store(this)
        Handler().postDelayed({ start() }, TIME_OUT)
    }

    private fun start() = if(store.isSignedIn)
    {
        when(store.user!!.userType)
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

            }else -> {  println("default")      }

        }

    }else {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

}