package com.suftnet.v12.repository.seller

import com.suftnet.v12.api.Seller
import com.suftnet.v12.api.model.request.CreateUser
import com.suftnet.v12.api.model.response.User
import retrofit2.Response

class SellerRepository(private val sellerApi : Seller) {
    suspend fun create(createUser : CreateUser): Response<User> = sellerApi.create(createUser)
    suspend fun pending(): Response<List<com.suftnet.v12.api.model.response.Order>> = sellerApi.pending()
    suspend fun completed(): Response<List<com.suftnet.v12.api.model.response.Order>> = sellerApi.completed()
}