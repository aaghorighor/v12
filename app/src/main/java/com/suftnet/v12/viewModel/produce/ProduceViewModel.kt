package com.suftnet.v12.viewModel.produce

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.suftnet.v12.api.Config
import com.suftnet.v12.api.Http
import com.suftnet.v12.api.model.request.CreateProduce
import com.suftnet.v12.api.model.request.DeleteProduce
import com.suftnet.v12.api.model.request.EditProduce
import com.suftnet.v12.api.model.response.Produce
import com.suftnet.v12.model.Error
import com.suftnet.v12.repository.produce.ProduceRepository
import com.suftnet.v12.util.NetWork
import kotlinx.coroutines.launch

class ProduceViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TAG = "ProduceViewModel"
    }

    private val networkService = Http.produce(Config.Url.HOST)
    private var produceRepository : ProduceRepository
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val deleteError: MutableLiveData<Error> = MutableLiveData()
    val listError: MutableLiveData<Error> = MutableLiveData()
    val produces = MutableLiveData<List<Produce>>()

    init {
        produceRepository =  ProduceRepository(networkService)
    }

    fun create(createProduce : CreateProduce)= liveData {

        viewModelScope.launch {
            loading.value = true
            var response = produceRepository.create(createProduce)
            if (response.isSuccessful) {
                emit(response.body())
            } else listError.value = NetWork.errorHandler(response)
            loading.value = false
        }
    }

    fun edit(editProduce: EditProduce)= liveData {

        viewModelScope.launch {
            loading.value = true
            var response = produceRepository.edit(editProduce)
            if (response.isSuccessful) {
                emit(response.body())
            } else listError.value = NetWork.errorHandler(response)
            loading.value = false
        }
    }

    fun delete(deleteProduce: DeleteProduce)= liveData {

        viewModelScope.launch {
            loading.value = true
            var response = produceRepository.delete(deleteProduce)
            if (response.isSuccessful) {
                emit(response.body())
            } else listError.value = NetWork.errorHandler(response)
            loading.value = false
        }
    }

    fun fetch() =  liveData{

        loading.value = true
        var response = produceRepository.fetch()
        if (response.isSuccessful) {
            emit(response.body())
        } else listError.value = NetWork.errorHandler(response)
        loading.value = false
    }
}