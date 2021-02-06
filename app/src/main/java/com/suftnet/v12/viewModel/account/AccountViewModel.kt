package com.suftnet.v12.viewModel.account

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.suftnet.v12.api.Config
import com.suftnet.v12.api.Http
import com.suftnet.v12.api.model.request.CreateUser

import com.suftnet.v12.api.model.request.Login
import com.suftnet.v12.api.model.response.User
import com.suftnet.v12.model.Error
import com.suftnet.v12.repository.account.AccountRepository
import com.suftnet.v12.repository.BuyerRepository
import com.suftnet.v12.repository.DriverRepository
import com.suftnet.v12.repository.seller.SellerRepository
import com.suftnet.v12.util.NetWork
import kotlinx.coroutines.launch

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TAG = "AccountViewModel"
    }

    private val networkService = Http.account(Config.Url.HOST)
    private var accountRepository : AccountRepository
    private val sellerServices = Http.seller(Config.Url.HOST)
    private var sellerRepository : SellerRepository
    private val buyerServices = Http.buyer(Config.Url.HOST)
    private var buyerRepository : BuyerRepository
    private val driverServices = Http.logistic(Config.Url.HOST)
    private var driverRepository : DriverRepository
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<Error> = MutableLiveData()
    var user = MutableLiveData<User>()

    init {
        accountRepository =  AccountRepository(networkService)
        sellerRepository =  SellerRepository(sellerServices)
        buyerRepository =  BuyerRepository(buyerServices)
        driverRepository =  DriverRepository(driverServices)
    }

    fun login(userName :String, password : String) =liveData<User> {

            loading.value = true
            var response = accountRepository.login(Login(userName, password))
            if (response.isSuccessful) {
                emit(response.body()!!)
            } else {
                error.value = NetWork.errorHandler(response)
            }
            loading.value = false
    }

    fun createSeller(createUser: CreateUser) {

        viewModelScope.launch {
            loading.value = true
            var response = sellerRepository.create(createUser)
            if (response.isSuccessful) {
                user.value = response.body()
            } else {
                error.value = NetWork.errorHandler(response)
            }
            loading.value = false
        }
    }

    fun createBuyer(createUser: CreateUser) {

        viewModelScope.launch {
            loading.value = true
            var response = buyerRepository.create(createUser)
            if (response.isSuccessful) {
                user.value = response.body()
            } else {
                error.value = NetWork.errorHandler(response)
            }
            loading.value = false
        }
    }

    fun createDriver(createUser: CreateUser) {

        viewModelScope.launch {
            loading.value = true
            var response = driverRepository.create(createUser)
            if (response.isSuccessful) {
                user.value = response.body()
            } else {
                error.value = NetWork.errorHandler(response)
            }
            loading.value = false
        }
    }
}