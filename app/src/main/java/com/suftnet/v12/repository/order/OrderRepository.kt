package com.suftnet.v12.repository

import com.suftnet.v12.api.Order
import com.suftnet.v12.api.model.request.CreateOrder
import retrofit2.Response

class OrderRepository(private val orderApi: Order) {
    suspend fun create(createOrder : CreateOrder): Response<Boolean> = orderApi.create(createOrder)
}