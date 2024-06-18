package umc.flo_clone_2ver.module

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import umc.flo_clone_2ver.R
import umc.flo_clone_2ver.presentation.MainActivity

val BASE_URL = R.string.base_url

fun getRetrofit(): Retrofit{
    val retrofit = Retrofit.Builder().baseUrl(MainActivity.getString(BASE_URL))
//    (BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()

    return retrofit
}