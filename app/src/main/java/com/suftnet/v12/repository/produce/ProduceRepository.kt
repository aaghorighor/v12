package com.suftnet.v12.repository.produce

import com.suftnet.v12.api.Produce
import com.suftnet.v12.api.model.request.CreateProduce
import com.suftnet.v12.api.model.request.DeleteProduce
import com.suftnet.v12.api.model.request.EditProduce
import retrofit2.Response

class ProduceRepository(private val produceApi : Produce) {
    suspend fun create(createProduce : CreateProduce): Response<com.suftnet.v12.api.model.response.CreateProduce> = produceApi.create(createProduce)
    suspend fun edit(editProduce : EditProduce): Response<Boolean> = produceApi.edit(editProduce)
    suspend fun delete(deleteProduce : DeleteProduce): Response<Boolean> = produceApi.delete(deleteProduce)
    suspend fun fetch(): Response<List<com.suftnet.v12.api.model.response.Produce>> = produceApi.fetch()
}