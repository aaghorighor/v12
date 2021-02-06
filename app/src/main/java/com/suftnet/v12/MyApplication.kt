package com.suftnet.v12

import android.app.Application
import android.content.Context
import android.util.Log
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

class MyApplication : Application(){

    companion object {
       lateinit var instance: MyApplication
    }

    private val TAG = "MyApplication"

    override fun onCreate() {
        super.onCreate()
        instance = this

        ViewPump.init(
            ViewPump.builder().addInterceptor(
            CalligraphyInterceptor(
            CalligraphyConfig.Builder().setDefaultFontPath(getString(R.string.font_regular)).setFontAttrId(R.attr.fontPath).build())
        ).build())
    }

}