package com.suftnet.v12.repository

import com.suftnet.v12.api.Order
import com.suftnet.v12.api.model.request.CreateOrder
import retrofit2.Response

class OrderRepository(private val orderApi: Order) {
    suspend fun create(createOrder : CreateOrder): Response<Boolean> = orderApi.create(createOrder)
    suspend fun pending(): Response<List<com.suftnet.v12.api.model.response.Order>> = orderApi.pending()
    suspend fun completed(): Response<List<com.suftnet.v12.api.model.response.Order>> = orderApi.completed()
}