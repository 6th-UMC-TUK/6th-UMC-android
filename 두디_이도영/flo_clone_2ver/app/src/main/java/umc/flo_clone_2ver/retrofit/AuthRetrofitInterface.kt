package umc.flo_clone_2ver.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import umc.flo_clone_2ver.data.User

interface AuthRetrofitInterface {
    @POST("/users")
    fun signUp(@Body user: User): Call<AuthResponse>

    @POST("/users/login")
    fun login(@Body user: User): Call<AuthResponse>
}