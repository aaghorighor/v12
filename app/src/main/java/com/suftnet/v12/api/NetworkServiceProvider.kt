package com.suftnet.v12.api

import com.suftnet.v12.MyApplication
import com.suftnet.v12.Store.Store
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkServiceProvider {

    private fun addQueryParameterInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val request: Request
            val modifiedUrl = originalRequest.url().newBuilder()
                .build()
            request = originalRequest.newBuilder().url(modifiedUrl).build()
            chain.proceed(request)
        }
    }
    fun getRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
    private fun addHeaderInterceptor(): Interceptor {
        var store = Store(MyApplication.instance)
        return Interceptor { chain ->

            val request = if (store.isSignedIn) {
                chain.request().newBuilder().addHeader("Authorization", "Bearer ${store.user!!.token}").build()
            } else {
                chain.request()
            }

            chain.proceed(request)
        }
    }
    private fun connectionSpec() :MutableList<ConnectionSpec> {
        val specs: MutableList<ConnectionSpec> = ArrayList()
        specs.add(ConnectionSpec.COMPATIBLE_TLS)
        specs.add(ConnectionSpec.MODERN_TLS)
        specs.add(ConnectionSpec.CLEARTEXT)

        return specs
    }
    private fun getOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        //body,basic,headers
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        //val cacheFile = File(MyApplication.context.cacheDir, "cache")
        //val cache = Cache(cacheFile, 1024 * 1024 * 50) //50Mb

        return OkHttpClient.Builder()
            .addInterceptor(addQueryParameterInterceptor())  //
            .addInterceptor(addHeaderInterceptor()) // token过滤
//              .addInterceptor(addCacheInterceptor())
            .addInterceptor(httpLoggingInterceptor) //
           // .cache(cache)  //添加缓存
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .writeTimeout(60L, TimeUnit.SECONDS)
                .connectionSpecs(connectionSpec())
            .build()
    }
//    @SuppressLint("NewApi")
//    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
//    private fun addCacheInterceptor(): Interceptor {
//        return Interceptor { chain ->
//            var request = chain.request()
//
//            if (!NetWork.isNetWorkAvailable(MyApplication.context)) {
//                request = request.newBuilder()
//                    .cacheControl(CacheControl.FORCE_CACHE)
//                    .build()
//            }
//            val response = chain.proceed(request)
//            if (NetWork.isNetWorkAvailable(MyApplication.context)) {
//                val maxAge = 0
//                response.newBuilder()
//                    .header("Cache-Control", "public, max-age=" + maxAge)
//                    .removeHeader("Retrofit")//
//                    .build()
//            } else {
//                val maxStale = 60 * 60 * 24 * 28
//                response.newBuilder()
//                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                    .removeHeader("nyn")
//                    .build()
//            }
//            response
//        }
//    }
}