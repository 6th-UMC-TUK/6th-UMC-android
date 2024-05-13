package com.example.memo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.memo.databinding.ActivityMainBinding
import com.example.memo.databinding.ActivityMemoBinding

class MemoActivity : AppCompatActivity() {
    lateinit var binding : ActivityMemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.storebutton.setOnClickListener{
            val mIntent = Intent(this, MainActivity::class.java).apply {
                putExtra("data", binding.editText.text.toString())
            }
            setResult(RESULT_OK, mIntent)
            if(!isFinishing) finish()
        }
    }
}