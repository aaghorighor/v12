package com.suftnet.v12.repository

import com.suftnet.v12.api.Market
import retrofit2.Response

class MarketRepository(private val marketApi : Market) {
    suspend fun fetch(): Response<List<com.suftnet.v12.api.model.response.Produce>> = marketApi.fetch()
    suspend fun getBy(id:String): Response<com.suftnet.v12.api.model.response.Produce> = marketApi.getBy(id)
}