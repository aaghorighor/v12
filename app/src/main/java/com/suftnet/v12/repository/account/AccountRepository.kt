package com.suftnet.v12.repository.account

import com.suftnet.v12.api.Account
import com.suftnet.v12.api.model.request.Login
import com.suftnet.v12.api.model.response.User
import retrofit2.Response

class AccountRepository(private val netWorkServices : Account) {
    suspend fun login(login : Login): Response<User> = netWorkServices.login(login)
}