package com.suftnet.v12.viewModel.account

import android.app.Application
import androidx.lifecycle.*
import com.suftnet.v12.api.Config
import com.suftnet.v12.api.Http

import com.suftnet.v12.api.model.request.Login
import com.suftnet.v12.api.model.response.User
import com.suftnet.v12.model.Error
import com.suftnet.v12.repository.account.AccountRepository
import com.suftnet.v12.util.NetWork
import kotlinx.coroutines.launch

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TAG = "AccountViewModel"
    }

    private val networkService = Http.account(Config.Url.HOST)
    private var accountRepository : AccountRepository
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<Error> = MutableLiveData()

    init {
        accountRepository =  AccountRepository(networkService)
    }

    fun login(userName :String, password : String) :LiveData<User> {
        val liveData = MutableLiveData<User>()

        viewModelScope.launch {
            loading.value = true
            var response = accountRepository.login(Login(userName, password))
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