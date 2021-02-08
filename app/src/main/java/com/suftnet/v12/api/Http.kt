package com.suftnet.v12.api

import com.suftnet.v12.api.model.request.CreateOrder

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

    fun market(baseUrl: String) : Market {
        return NetworkServiceProvider.getRetrofit(baseUrl).create(Market::class.java)
    }

    fun order(baseUrl: String) : Order {
        return NetworkServiceProvider.getRetrofit(baseUrl).create(Order::class.java)
    }

    fun question(baseUrl: String) : Question {
        return NetworkServiceProvider.getRetrofit(baseUrl).create(Question::class.java)
    }

    fun answer(baseUrl: String) : Answer {
        return NetworkServiceProvider.getRetrofit(baseUrl).create(Answer::class.java)
    }
}