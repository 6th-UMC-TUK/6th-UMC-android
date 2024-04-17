package com.example.flo_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.flo_clone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // binding 사용중
        // 원래라면 findById로 접근해야함 -> binding으로 간편하게 사용 가능

        val song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString())

        binding.mainPlayerCl.setOnClickListener {
            // 뷰에 대한 함수
//            startActivity(Intent(this, SongActivity::class.java))
            // Intent를 사용하여 this에서 SongActivity로 이동
            val intent = Intent(this, SongActivity::class.java)
            // 재사용하기 쉽도록 선언
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            startActivity(intent)
        }

        initView()
    }
    private fun initView(){

        initNavigator()
    }
    private fun initNavigator(){
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_frm) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)
    }
}