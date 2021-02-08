package com.suftnet.v12.repository

import com.suftnet.v12.api.Answer
import com.suftnet.v12.api.model.request.CreateAnswer
import retrofit2.Response

class AnswerRepository(private val answerApi : Answer) {
    suspend fun fetch(id :String): Response<List<com.suftnet.v12.api.model.response.Answer>> = answerApi.fetch(id)
    suspend fun create(createAnswer: CreateAnswer): Response<com.suftnet.v12.api.model.response.Answer> = answerApi.create(createAnswer)
}