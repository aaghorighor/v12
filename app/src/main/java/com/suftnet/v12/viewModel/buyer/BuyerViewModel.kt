package com.suftnet.v12.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.suftnet.v12.api.Config
import com.suftnet.v12.api.Http
import com.suftnet.v12.api.model.request.CreateUser
import com.suftnet.v12.model.Error
import com.suftnet.v12.repository.BuyerRepository
import com.suftnet.v12.util.NetWork

class BuyerViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TAG = "BuyerViewModel"
    }

    private val networkService = Http.buyer(Config.Url.HOST)
    private var buyerRepository : BuyerRepository
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<Error> = MutableLiveData()

    init {
        buyerRepository =  BuyerRepository(networkService)
    }

    fun create(createUser: CreateUser) = liveData {

        loading.value = true
        var response = buyerRepository.create(createUser)
        if (response.isSuccessful) {
            emit(response.body())
        } else {
            error.value = NetWork.errorHandler(response)
        }
        loading.value = false
    }
}