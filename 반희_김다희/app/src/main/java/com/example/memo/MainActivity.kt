package com.example.memo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var getResultText: ActivityResultLauncher<Intent>
    private lateinit var adapter: DataAdapter
    private var dataList = ArrayList<Data>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getResultText =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val mString = result.data?.getStringExtra("data") ?: ""

                    dataList.apply {
                        add(Data(mString))
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, MemoActivity::class.java)
            getResultText.launch(intent)
        }
        binding.rvData.layoutManager = LinearLayoutManager(this) // 레이아웃 매니저 연결
        adapter = DataAdapter(dataList)
        binding.rvData.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(this,"onPause", Toast.LENGTH_SHORT).show()
    }

    override fun onRestart() {
        super.onRestart()
        showRestartDialog()
        Toast.makeText(this, "onRestart",Toast.LENGTH_SHORT).show()
    }

    private fun showRestartDialog(){
    }
}