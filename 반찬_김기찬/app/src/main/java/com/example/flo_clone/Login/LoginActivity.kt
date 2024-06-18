package com.example.flo_clone.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_clone.Function.CustomSnackbar
import com.example.flo_clone.MainActivity
import com.example.flo_clone.Song.SongDatabase
import com.example.flo_clone.databinding.ActivityLoginBinding

class LoginActivity: AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignUpTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.loginSignInBtn.setOnClickListener {
            login()
        }
    }

    private fun login() {
        if (binding.loginIdEt.text.toString().isEmpty() || binding.loginDirectInputEt.text.toString().isEmpty()) {
            CustomSnackbar.make(binding.root, "이메일을 입력해주세요.").show()
            return
        }
        if (binding.loginPasswordEt.text.toString().isEmpty()){
            CustomSnackbar.make(binding.root, "비밀번호를 입력해주세요.").show()
            return
        }

        val email: String = binding.loginIdEt.text.toString() + "@" + binding.loginDirectInputEt.text.toString()
        val pwd: String = binding.loginPasswordEt.text.toString()

        val by: String = "에베베베베베베베"

        val songDB = SongDatabase.getInstance(this)!!
        val user = songDB.userDao().getUser(email, pwd)

        user?.let {
            Log.d("Login_Act/Get_User", "userId : ${user.id}, $user") // user 의 모든 정보를 보여줌
            saveJwt(user.id)
            startMainActivity()
        }
        CustomSnackbar.make(binding.root, "회원 정보가 존재하지 않습니다.").show()
    }

    private fun saveJwt(jwt: Int) {
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putInt("jwt", jwt)
        editor.apply()
    }

    // 로그인이 완료되면 액티비티 전환
    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        CustomSnackbar.make(binding.root, "환영합니다.").show()
    }
}