package com.suftnet.v12.repository

import com.suftnet.v12.api.Driver
import com.suftnet.v12.api.model.request.CreateDelivery
import com.suftnet.v12.api.model.request.CreateUser
import com.suftnet.v12.api.model.response.User
import retrofit2.Response

class DriverRepository(private val driverApi : Driver) {
    suspend fun create(createUser : CreateUser): Response<User> = driverApi.create(createUser)
    suspend fun fetch(): Response<List<com.suftnet.v12.api.model.response.Driver>> = driverApi.fetch()
    suspend fun fetchByOrder(id :String): Response<com.suftnet.v12.api.model.response.Driver> = driverApi.fetchByOrder(id)
    suspend fun createDelivery(createDelivery : CreateDelivery): Response<Boolean> = driverApi.createDelivery(createDelivery)
    suspend fun pendingJobs(): Response<List<com.suftnet.v12.api.model.response.Order>> = driverApi.pendingJobs()
    suspend fun completedJobs(): Response<List<com.suftnet.v12.api.model.response.Order>> = driverApi.completedJobs()
}