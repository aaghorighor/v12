package com.suftnet.v12.repository.produce

import com.suftnet.v12.api.Produce
import com.suftnet.v12.api.model.request.CreateProduce
import com.suftnet.v12.api.model.request.DeleteProduce
import com.suftnet.v12.api.model.request.EditProduce
import com.suftnet.v12.api.model.response.FetchProduce
import retrofit2.Response

class ProduceRepository(private val netWorkServices : Produce) {
    suspend fun create(createProduce : CreateProduce): Response<com.suftnet.v12.api.model.response.CreateProduce> = netWorkServices.create(createProduce)
    suspend fun edit(editProduce : EditProduce): Response<Boolean> = netWorkServices.edit(editProduce)
    suspend fun delete(deleteProduce : DeleteProduce): Response<Boolean> = netWorkServices.delete(deleteProduce)
    suspend fun fetch(): Response<List<FetchProduce>> = netWorkServices.fetch()
}