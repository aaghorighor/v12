package com.suftnet.v12.repository.seller

import com.suftnet.v12.api.Seller
import com.suftnet.v12.api.model.request.CreateUser
import com.suftnet.v12.api.model.response.User
import retrofit2.Response

class SellerRepository(private val netWorkServices : Seller) {
    suspend fun create(createUser : CreateUser): Response<User> = netWorkServices.create(createUser)
}