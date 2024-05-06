package com.example.memo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.memo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var memo: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.checkBtn.setOnClickListener{
            val intent = Intent(this, CheckActivity::class.java)
            intent.putExtra("memo", "${binding.memoEt.text}")
            startActivity(intent)
        }
    }

    override fun onResume(){
        super.onResume()

        if(memo != null) {
            binding.memoEt.setText(memo)
        }
    }

    override fun onPause() {
        super.onPause()
        memo = binding.memoEt.text.toString()
    }

    override fun onRestart() {
        super.onRestart()

        AlertDialog.Builder(this).apply {
            setMessage("다시 작성하시겠어요?")

            setNegativeButton("아니요"){ _, _ ->
                binding.memoEt.text = null
            }

            setPositiveButton("예") {_, _ ->
                memo = null
            }
        }.show()
    }
}