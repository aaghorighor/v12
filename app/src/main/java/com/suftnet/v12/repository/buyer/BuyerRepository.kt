package com.suftnet.v12.repository

import com.suftnet.v12.api.Buyer
import com.suftnet.v12.api.Seller
import com.suftnet.v12.api.model.request.CreateUser
import com.suftnet.v12.api.model.response.User
import retrofit2.Response

class BuyerRepository(private val buyerApi : Buyer) {
    suspend fun create(createUser : CreateUser): Response<User> = buyerApi.create(createUser)
    suspend fun pending(): Response<List<com.suftnet.v12.api.model.response.Order>> = buyerApi.pending()
    suspend fun completed(): Response<List<com.suftnet.v12.api.model.response.Order>> = buyerApi.completed()
}