package com.example.flo_clone.Api

import com.example.flo_clone.Login.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRetrofitInterface {
    @POST("/users")
    fun signUp(@Body user: User) : Call<AuthResponse>

    @POST("/users/login")
    fun login(@Body user: User): Call<AuthResponse>

}