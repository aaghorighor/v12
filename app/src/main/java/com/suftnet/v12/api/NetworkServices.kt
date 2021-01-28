package com.suftnet.v12.api

import com.suftnet.v12.api.model.request.*
import com.suftnet.v12.api.model.response.FetchProduce
import com.suftnet.v12.api.model.response.User
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

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
    suspend fun fetch() : Response<List<FetchProduce>>
}

interface Seller {
    @POST(Config.Url.Seller.create)
    suspend fun create(@Body body: CreateUser) : Response<User>
}

interface Buyer {
    @POST(Config.Url.Buyer.create)
    suspend fun create(@Body body: CreateUser) : Response<User>
}

interface Driver {
    @POST(Config.Url.Logistic.create)
    suspend fun create(@Body body: CreateUser) : Response<User>
}