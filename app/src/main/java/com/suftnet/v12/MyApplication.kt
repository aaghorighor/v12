package com.suftnet.v12

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.suftnet.v12.api.Config
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import org.acra.ACRA
import org.acra.ReportField
import org.acra.ReportingInteractionMode
import org.acra.annotation.ReportsCrashes
import org.acra.sender.HttpSender

@ReportsCrashes(
        formUri = Config.Url.CRASH,
        reportType = HttpSender.Type.FORM,
        httpMethod = HttpSender.Method.POST,
        customReportContent = [
            ReportField.ANDROID_VERSION,
            ReportField.APP_VERSION_CODE,
            ReportField.AVAILABLE_MEM_SIZE,
            ReportField.BUILD,
            ReportField.CRASH_CONFIGURATION,
            ReportField.LOGCAT,
            ReportField.PACKAGE_NAME,
            ReportField.STACK_TRACE,
            ReportField.PHONE_MODEL,
            ReportField.REPORT_ID],
        mode = ReportingInteractionMode.SILENT
)
class MyApplication : MultiDexApplication(){

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

        ACRA.init(this);
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this);
    }

}