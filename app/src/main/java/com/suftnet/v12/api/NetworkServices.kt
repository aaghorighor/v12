package com.suftnet.v12.api

import com.suftnet.v12.api.model.request.*
import com.suftnet.v12.api.model.response.User
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface Account {
    @POST(Config.Url.Account.Login)
    suspend fun login(@Body body: Login) : Response<User>
}

interface Produce {
    @POST(Config.Url.Produce.create)
    suspend fun create(@Body body: CreateProduce) : Response<com.suftnet.v12.api.model.response.CreateProduce>
    @POST(Config.Url.Produce.edit)
    suspend fun edit(@Body body: EditProduce) : Response<Boolean>
    @POST(Config.Url.Produce.delete)
    suspend fun delete(@Body body: DeleteProduce) : Response<Boolean>
    @GET(Config.Url.Produce.fetch)
    suspend fun fetch() : Response<List<com.suftnet.v12.api.model.response.Produce>>
}

interface Seller {
    @POST(Config.Url.Seller.create)
    suspend fun create(@Body body: CreateUser) : Response<User>
    @GET(Config.Url.Seller.sellerPendingOrders)
    suspend fun pending() : Response<List<com.suftnet.v12.api.model.response.Order>>
    @GET(Config.Url.Seller.sellerCompletedOrders)
    suspend fun completed() : Response<List<com.suftnet.v12.api.model.response.Order>>
}

interface Buyer {
    @POST(Config.Url.Buyer.create)
    suspend fun create(@Body body: CreateUser) : Response<User>
    @GET(Config.Url.Buyer.buyerPendingOrders)
    suspend fun pending() : Response<List<com.suftnet.v12.api.model.response.Order>>
    @GET(Config.Url.Buyer.buyerCompletedOrders)
    suspend fun completed() : Response<List<com.suftnet.v12.api.model.response.Order>>
}

interface Driver {
    @POST(Config.Url.Driver.create)
    suspend fun create(@Body body: CreateUser) : Response<User>
    @GET(Config.Url.Driver.fetch)
    suspend fun fetch() : Response<List<com.suftnet.v12.api.model.response.Driver>>
    @POST(Config.Url.Driver.createDelivery)
    suspend fun createDelivery(@Body body: CreateDelivery) : Response<Boolean>
    @GET(Config.Url.Driver.fetchByOrder)
    suspend fun fetchByOrder(@Query("id") id: String) : Response<com.suftnet.v12.api.model.response.Driver>
    @GET(Config.Url.Driver.jobs)
    suspend fun jobs() : Response<List<com.suftnet.v12.api.model.response.Order>>
}

interface Market {
    @GET(Config.Url.Market.fetch)
    suspend fun fetch() : Response<List<com.suftnet.v12.api.model.response.Produce>>
    @GET(Config.Url.Market.getBy)
    suspend fun getBy(@Query("id") id: String) : Response<com.suftnet.v12.api.model.response.Produce>
}

interface Order {
    @POST(Config.Url.Order.create)
    suspend fun create(@Body body: CreateOrder) : Response<Boolean>
    @POST(Config.Url.Order.updateOrderStatus)
    suspend fun updateOrderStatus(@Body body: UpdateOrderStatus) : Response<Boolean>
}

interface Question {
    @POST(Config.Url.Question.create)
    suspend fun create(@Body body: CreateQuestion) : Response<com.suftnet.v12.api.model.response.Question>
    @GET(Config.Url.Question.fetch)
    suspend fun fetch() : Response<List<com.suftnet.v12.api.model.response.Question>>
}

interface Answer {
    @POST(Config.Url.Answer.create)
    suspend fun create(@Body body: CreateAnswer) : Response<com.suftnet.v12.api.model.response.Answer>
    @GET(Config.Url.Answer.fetch)
    suspend fun fetch(@Query("id") id: String) : Response<List<com.suftnet.v12.api.model.response.Answer>>
}