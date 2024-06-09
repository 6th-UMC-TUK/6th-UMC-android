package umc.flo_clone_2ver.retrofit

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import umc.flo_clone_2ver.module.getRetrofit

class SongService() {
    private lateinit var lookView: LookView

    fun setLookView(lookView: LookView) {
        this.lookView = lookView
    }

    fun getSongs() {
        val songService = getRetrofit().create(SongRetrofitInterface::class.java)

        lookView.onGetSongLoading()

        songService.getSongs().enqueue(object : Callback<SongResponse> {
            override fun onResponse(call: Call<SongResponse>, response: Response<SongResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val songResponse: SongResponse = response.body()!!

                    Log.d("SongResponse", songResponse.toString())

                    when (val code = songResponse.code) {
                        1000 -> {
                            lookView.onGetSongSuccess(code, songResponse.result!!)
                        }

                        else -> {
                            lookView.onGetSongFailure(code, songResponse.message)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<SongResponse>, t: Throwable) {
                lookView.onGetSongFailure(400, "네트워크 오류 발생")
            }
        })
    }
}