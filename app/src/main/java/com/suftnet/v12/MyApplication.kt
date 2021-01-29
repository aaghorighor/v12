package com.suftnet.v12

import android.app.Application
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

class MyApplication : Application(){

    private val TAG = "MyApplication"

    override fun onCreate() {
        super.onCreate()
        appInstance = this

        ViewPump.init(
            ViewPump.builder().addInterceptor(
            CalligraphyInterceptor(
            CalligraphyConfig.Builder().setDefaultFontPath(getString(R.string.font_regular)).setFontAttrId(R.attr.fontPath).build())
        ).build())
    }

    companion object {
        private lateinit var appInstance: MyApplication

        fun getAppInstance(): MyApplication {
            return appInstance
        }
    }

}