package com.suftnet.v12.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.suftnet.v12.api.Config
import com.suftnet.v12.api.Http
import com.suftnet.v12.api.model.request.CreateOrder
import com.suftnet.v12.api.model.request.UpdateOrderStatus
import com.suftnet.v12.model.Error
import com.suftnet.v12.repository.OrderRepository
import com.suftnet.v12.util.NetWork

class OrderViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TAG = "OrderViewModel"
    }

    private val networkService = Http.order(Config.Url.HOST)
    private var orderRepository : OrderRepository
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<Error> = MutableLiveData()

    init {
        orderRepository =  OrderRepository(networkService)
    }

    fun create(createOrder: CreateOrder) = liveData {

        loading.value = true
        var response = orderRepository.create(createOrder)
        if (response.isSuccessful) {
            emit(response.body())
        } else {
            error.value = NetWork.errorHandler(response)
        }
        loading.value = false
    }

    fun updateOrderStatus(updateOrderStatus: UpdateOrderStatus) = liveData {

        loading.value = true
        var response = orderRepository.updateOrderStatus(updateOrderStatus)
        if (response.isSuccessful) {
            emit(response.body())
        } else {
            error.value = NetWork.errorHandler(response)
        }
        loading.value = false
    }

}