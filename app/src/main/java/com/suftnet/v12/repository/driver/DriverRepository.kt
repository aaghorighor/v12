package com.suftnet.v12.repository.seller

import com.suftnet.v12.api.Driver
import com.suftnet.v12.api.model.request.CreateUser
import com.suftnet.v12.api.model.response.User
import retrofit2.Response

class DriverRepository(private val netWorkServices : Driver) {
    suspend fun create(createUser : CreateUser): Response<User> = netWorkServices.create(createUser)
}