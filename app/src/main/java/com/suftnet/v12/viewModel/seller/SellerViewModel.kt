package com.suftnet.v12.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.suftnet.v12.api.Config
import com.suftnet.v12.api.Http
import com.suftnet.v12.api.model.request.CreateUser
import com.suftnet.v12.model.Error
import com.suftnet.v12.repository.seller.SellerRepository
import com.suftnet.v12.util.NetWork

class SellerViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TAG = "SellerViewModel"
    }

    private val networkService = Http.seller(Config.Url.HOST)
    private var sellerRepository : SellerRepository
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<Error> = MutableLiveData()

    init {
        sellerRepository =  SellerRepository(networkService)
    }

    fun create(createUser: CreateUser) = liveData {

        loading.value = true
        var response = sellerRepository.create(createUser)
        if (response.isSuccessful) {
            emit(response.body())
        } else {
            error.value = NetWork.errorHandler(response)
        }
        loading.value = false
    }

    fun pending() =  liveData{

        loading.value = true
        var response = sellerRepository.pending()
        if (response.isSuccessful) {
            emit(response.body())
        } else error.value = NetWork.errorHandler(response)
        loading.value = false
    }

    fun completed() =  liveData{

        loading.value = true
        var response = sellerRepository.completed()
        if (response.isSuccessful) {
            emit(response.body())
        } else error.value = NetWork.errorHandler(response)
        loading.value = false
    }
}