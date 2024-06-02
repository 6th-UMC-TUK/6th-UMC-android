package com.example.flo_clone.Login

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_clone.Function.CustomSnackbar
import com.example.flo_clone.Song.SongDatabase
import com.example.flo_clone.databinding.ActivitySignupBinding
import com.google.android.material.snackbar.Snackbar

class SignUpActivity: AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpSignUpBtn.setOnClickListener {
            if(signUp()) {
                finish()
            }
        }
    }

    private fun getUser(): User {
        var email: String = binding.signUpIdEt.text.toString() + "@" + binding.signUpDirectInputEt.text.toString()
        val pwd: String = binding.signUpPasswordEt.text.toString()

        return User(email, pwd)
    }

    // 회원가입을 진행하는 함수
    private fun signUp(): Boolean {
        // 아이디와 이메일 입력 칸이 비어있다면
        if (binding.signUpIdEt.text.toString().isEmpty() || binding.signUpDirectInputEt.text.toString().isEmpty()) {
            CustomSnackbar.make(binding.root, "이메일 형식이 잘못되었습니다.",).setAnchorView(binding.signUpSignUpBtn).show()
            return false
        }
        // 비밀번호 입력 칸이 비어있다면
        if (binding.signUpPasswordEt.text.toString().isEmpty()) {
            CustomSnackbar.make(binding.root, "비밀번호를 입력해주세요.").setAnchorView(binding.signUpSignUpBtn).show()
            return false
        }
        // 비밀번호와 비밀번호 확인이 일치하지 않다면
        if (binding.signUpPasswordEt.text.toString() != binding.signUpPasswordCheckEt.text.toString()) {
            CustomSnackbar.make(binding.root, "비밀번호가 일치하지 않습니다.").setAnchorView(binding.signUpSignUpBtn).show()
            return false
        }
        val userDB = SongDatabase.getInstance(this)!!
        val user = getUser()

        // 중복된 이메일 체크
        val existingUser = userDB.userDao().getUserByEmail(user.email)
        if (existingUser != null) {
            CustomSnackbar.make(binding.root, "이미 존재하는 이메일입니다.").setAnchorView(binding.signUpSignUpBtn).show()
            return false
        }

        userDB.userDao().insert(getUser())

        val users = userDB.userDao().getUsers() // 회원가입 정보가 잘 들어갔는지
        Log.d("SignUpAct", users.toString())

        return true
    }
}