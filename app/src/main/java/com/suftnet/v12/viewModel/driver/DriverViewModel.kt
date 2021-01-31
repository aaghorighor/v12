package com.suftnet.v12.viewModel.account

import android.app.Application
import androidx.lifecycle.*
import com.suftnet.v12.api.Config
import com.suftnet.v12.api.Http
import com.suftnet.v12.api.model.request.CreateUser
import com.suftnet.v12.api.model.response.User
import com.suftnet.v12.model.Error
import com.suftnet.v12.repository.seller.DriverRepository
import com.suftnet.v12.util.NetWork
import kotlinx.coroutines.launch

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

    fun create(createUser: CreateUser) :LiveData<User> {
        val liveData = MutableLiveData<User>()

        viewModelScope.launch {
            loading.value = true
            var response = driverRepository.create(createUser)
            if (response.isSuccessful) {
                liveData.value =response.body()
            } else {
                error.value = NetWork.errorHandler(response)
            }
            loading.value = false
        }

        return liveData
    }
}