package com.suftnet.v12.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.suftnet.v12.api.Config
import com.suftnet.v12.api.Http
import com.suftnet.v12.api.model.request.CreateQuestion
import com.suftnet.v12.model.Error
import com.suftnet.v12.repository.QuestionRepository
import com.suftnet.v12.util.NetWork

class QuestionViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TAG = "QuestionViewModel"
    }

    private val networkService = Http.question(Config.Url.HOST)
    private var questionRepository : QuestionRepository
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val listError: MutableLiveData<Error> = MutableLiveData()

    init {
        questionRepository =  QuestionRepository(networkService)
    }

    fun fetch() =  liveData{

        loading.value = true
        var response = questionRepository.fetch()
        if (response.isSuccessful) {
            emit(response.body())
        } else listError.value = NetWork.errorHandler(response)
        loading.value = false
    }

    fun create(createQuestion: CreateQuestion) =  liveData{

        loading.value = true
        var response = questionRepository.create(createQuestion)
        if (response.isSuccessful) {
            emit(response.body()!!)
        } else listError.value = NetWork.errorHandler(response)
        loading.value = false
    }
}