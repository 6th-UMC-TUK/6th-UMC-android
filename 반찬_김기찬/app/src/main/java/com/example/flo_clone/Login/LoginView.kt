package com.example.flo_clone.Login

import com.example.flo_clone.Api.Result

interface LoginView {
    fun onLoginSuccess(code : Int, result : Result)
    fun onLoginFailure()
}