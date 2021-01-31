package com.suftnet.v12.api

object Http {

    fun account(baseUrl: String) : Account {
        return NetworkServiceProvider.getRetrofit(baseUrl).create(Account::class.java)
    }

    fun produce(baseUrl: String) : Produce {
        return NetworkServiceProvider.getRetrofit(baseUrl).create(Produce::class.java)
    }

    fun seller(baseUrl: String) : Seller {
        return NetworkServiceProvider.getRetrofit(baseUrl).create(Seller::class.java)
    }

    fun buyer(baseUrl: String) : Buyer {
        return NetworkServiceProvider.getRetrofit(baseUrl).create(Buyer::class.java)
    }

    fun logistic(baseUrl: String) : Driver {
        return NetworkServiceProvider.getRetrofit(baseUrl).create(Driver::class.java)
    }
}