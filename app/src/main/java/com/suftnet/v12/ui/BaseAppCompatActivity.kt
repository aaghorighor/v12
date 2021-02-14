package com.suftnet.v12.ui

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.suftnet.v12.R
import com.suftnet.v12.util.ToolBarFont
import com.suftnet.v12.util.Util.lightStatusBar
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

open class BaseAppCompatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lightStatusBar()
    }

    fun setToolbar(mToolbar: Toolbar) {
        setSupportActionBar(mToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        mToolbar.setNavigationOnClickListener { onBackPressed() }
        mToolbar.ToolBarFont()
    }

    fun fullScreen()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }

    fun toastLong(msg :String)
    {
        Toast.makeText(this@BaseAppCompatActivity, msg, Toast.LENGTH_LONG).show()

    }
    fun toastShort(msg :String)
    {
        Toast.makeText(this@BaseAppCompatActivity, msg, Toast.LENGTH_SHORT).show()
    }

    fun BigDecimal.toFormattedString(): String {

        val df = DecimalFormat("#,###.00")
        return df.format(this)
    }
    fun String.toFormattedString(): String {

        val df = DecimalFormat("#,###.00")
        return df.format(this)
    }
    fun String.getStringDate(initialFormat: String, requiredFormat: String, locale: Locale = Locale.getDefault()): String {
        return this.toDate(initialFormat, locale).toString(requiredFormat, locale)
    }
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun String.toDate(format: String, locale: Locale = Locale.getDefault()): Date = SimpleDateFormat(format, locale).parse(this)
    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

}