package com.suftnet.v12.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.suftnet.v12.api.Config
import com.suftnet.v12.api.Http
import com.suftnet.v12.api.model.request.CreateAnswer
import com.suftnet.v12.model.Error
import com.suftnet.v12.repository.AnswerRepository
import com.suftnet.v12.util.NetWork

class AnswerViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TAG = "AnswerViewModel"
    }

    private val networkService = Http.answer(Config.Url.HOST)
    private var answerRepository : AnswerRepository
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val listError: MutableLiveData<Error> = MutableLiveData()

    init {
        answerRepository =  AnswerRepository(networkService)
    }

    fun fetch(id:String) =  liveData{

        loading.value = true
        var response = answerRepository.fetch(id)
        if (response.isSuccessful) {
            emit(response.body())
        } else listError.value = NetWork.errorHandler(response)
        loading.value = false
    }

    fun create(createAnswer: CreateAnswer) =  liveData{

        loading.value = true
        var response = answerRepository.create(createAnswer)
        if (response.isSuccessful) {
            emit(response.body()!!)
        } else listError.value = NetWork.errorHandler(response)
        loading.value = false
    }
}