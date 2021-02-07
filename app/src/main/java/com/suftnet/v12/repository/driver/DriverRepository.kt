package com.suftnet.v12.repository

import com.suftnet.v12.api.Driver
import com.suftnet.v12.api.model.request.CreateUser
import com.suftnet.v12.api.model.response.User
import retrofit2.Response

class DriverRepository(private val driverApi : Driver) {
    suspend fun create(createUser : CreateUser): Response<User> = driverApi.create(createUser)
    suspend fun fetch(): Response<List<com.suftnet.v12.api.model.response.Driver>> = driverApi.fetch()
}