package umc.flo_clone_2ver.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import umc.flo_clone_2ver.R
import umc.flo_clone_2ver.login.LoginActivity
import umc.flo_clone_2ver.retrofit.AuthService
import umc.flo_clone_2ver.retrofit.LoginAutoView

class SplashActivity : AppCompatActivity(), LoginAutoView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val authService = AuthService()
        authService.setLoginAutoView(this)

        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val token = spf.getString("jwt2", "")!!
        Log.d("Token", token)

        authService.loginAuto(token)
    }

    override fun onLoginSuccess() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onLoginFailure() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}