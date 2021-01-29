package com.suftnet.v12.Store

import com.suftnet.v12.api.model.response.User

interface IStore {
    val isSignedIn: Boolean
    var token: String
    var user: User?
}