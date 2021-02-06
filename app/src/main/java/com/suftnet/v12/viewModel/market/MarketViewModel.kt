package com.suftnet.v12.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.suftnet.v12.api.Config
import com.suftnet.v12.api.Http
import com.suftnet.v12.model.Error
import com.suftnet.v12.repository.MarketRepository
import com.suftnet.v12.util.NetWork

class MarketViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TAG = "MarketViewModel"
    }

    private val networkService = Http.market(Config.Url.HOST)
    private var marketRepository : MarketRepository
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val listError: MutableLiveData<Error> = MutableLiveData()

    init {
        marketRepository =  MarketRepository(networkService)
    }

    fun fetch() =  liveData{

        loading.value = true
        var response = marketRepository.fetch()
        if (response.isSuccessful) {
            emit(response.body())
        } else listError.value = NetWork.errorHandler(response)
        loading.value = false
    }

    fun getBy(id :String) =  liveData{

        loading.value = true
        var response = marketRepository.getBy(id)
        if (response.isSuccessful) {
            emit(response.body()!!)
        } else listError.value = NetWork.errorHandler(response)
        loading.value = false
    }
}