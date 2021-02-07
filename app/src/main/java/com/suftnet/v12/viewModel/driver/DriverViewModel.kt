package com.suftnet.v12.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.suftnet.v12.api.Config
import com.suftnet.v12.api.Http
import com.suftnet.v12.api.model.request.CreateUser
import com.suftnet.v12.model.Error
import com.suftnet.v12.repository.DriverRepository
import com.suftnet.v12.util.NetWork

class DriverViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TAG = "DriverViewModel"
    }

    private val networkService = Http.logistic(Config.Url.HOST)
    private var driverRepository : DriverRepository
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<Error> = MutableLiveData()

    init {
        driverRepository =  DriverRepository(networkService)
    }

    fun create(createUser: CreateUser) = liveData {

        loading.value = true
        var response = driverRepository.create(createUser)
        if (response.isSuccessful) {
            emit(response.body())
        } else {
            error.value = NetWork.errorHandler(response)
        }
        loading.value = false
    }

    fun fetch() =  liveData{

        loading.value = true
        var response = driverRepository.fetch()
        if (response.isSuccessful) {
            emit(response.body())
        } else error.value = NetWork.errorHandler(response)
        loading.value = false
    }
}