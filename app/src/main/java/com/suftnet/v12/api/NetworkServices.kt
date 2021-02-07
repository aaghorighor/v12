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
    @POST(Config.Url.Logistic.create)
    suspend fun create(@Body body: CreateUser) : Response<User>
    @GET(Config.Url.Logistic.fetch)
    suspend fun fetch() : Response<List<com.suftnet.v12.api.model.response.Driver>>
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
}