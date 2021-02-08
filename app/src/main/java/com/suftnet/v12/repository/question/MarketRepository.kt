package com.suftnet.v12.repository

import com.suftnet.v12.api.Question
import com.suftnet.v12.api.model.request.CreateQuestion
import retrofit2.Response

class QuestionRepository(private val questionApi : Question) {
    suspend fun fetch(): Response<List<com.suftnet.v12.api.model.response.Question>> = questionApi.fetch()
    suspend fun create(createQuestion: CreateQuestion): Response<com.suftnet.v12.api.model.response.Question> = questionApi.create(createQuestion)
}