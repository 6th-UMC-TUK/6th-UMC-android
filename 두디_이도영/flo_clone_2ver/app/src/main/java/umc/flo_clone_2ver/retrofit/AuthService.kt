package umc.flo_clone_2ver.retrofit

import android.util.Log
import android.view.View
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import umc.flo_clone_2ver.data.User
import umc.flo_clone_2ver.module.getRetrofit

class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView
    private lateinit var loginAutoView: LoginAutoView

    fun setSignUpView(signUpView: SignUpView) {
        this.signUpView = signUpView
    }
    fun setLoginView(loginView: LoginView){
        this.loginView = loginView
    }

    fun setLoginAutoView(loginAutoView: LoginAutoView){
        this.loginAutoView = loginAutoView
    }

    fun signUp(user: User) {
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        authService.signUp(user).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SIGNUP/SUCCESS", response.toString())
                val resp: AuthResponse = response.body()!!
                when (resp.code) {
                    1000 -> signUpView.onSignUpSuccess()
                    else -> signUpView.onSignUpFailure(resp.message)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUP/Failure", t.message.toString())
            }
        })
    }

    fun login(user: User) {
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        authService.login(user).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("LOGIN/SUCCESS", response.toString())
                val resp: AuthResponse = response.body()!!
                when (val code = resp.code) {
                    1000 -> loginView.onLoginSuccess(resp.code, resp.result!!)
                    else -> loginView.onLoginFailure(resp.code)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("LOGIN/Failure", t.message.toString())
            }
        })
    }

    fun loginAuto(token: String){
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        authService.loginAuto(token).enqueue(object: Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("LOGIN-AUTO/SUCCESS", response.toString())
                val resp: AuthResponse = response.body()!!
                Log.d("LOGIN_AUTO", response.body().toString())
                when(resp.code){
                    1000 -> loginAutoView.onLoginSuccess()
                    else -> loginAutoView.onLoginFailure()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("LOGIN-AUTO/FAILURE", t.message.toString())
            }
        })
        Log.d("LoginAuto", "SUCCESS")
    }
}