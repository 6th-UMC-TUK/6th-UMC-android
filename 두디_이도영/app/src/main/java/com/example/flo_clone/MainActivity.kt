package com.example.flo_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.flo_clone.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var song: Song = Song()
    private var gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Flo_clone)
        // onCreate 되었을 때 Splash 테마에서 원래 테마로 변경
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // binding 사용중
        // 원래라면 findById로 접근해야함 -> binding으로 간편하게 사용 가능

        // val song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString(), 0, 60, false, "music_lilac")
        // sharedPreferences에 저장된 값을 가져올 것이기 때문에 더 이상 필요 없음

        binding.mainPlayerCl.setOnClickListener {
            // 뷰에 대한 함수
//            startActivity(Intent(this, SongActivity::class.java))
            // Intent를 사용하여 this에서 SongActivity로 이동
            val intent = Intent(this, SongActivity::class.java)
            // 재사용하기 쉽도록 선언
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            intent.putExtra("second", song.second)
            intent.putExtra("isPlaying", song.playTime)
            intent.putExtra("isPlaying", song.isPlaying)
            intent.putExtra("music", song.music)
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

    private fun setMiniPlayer(song: Song){
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainProgressSb.progress = (song.second*100000)/song.playTime
        // seekbar를 만들 때 최대 값을 100000으로 지정했기 때문에 100000을 곱함
    }

    override fun onStart(){
        super.onStart()
        // onCreate가 아닌 onStart로 하는 이유는, 다른 작업을 하고 다시 돌아왔을 때는 onStart부터 실행되기 때문
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songJson = sharedPreferences.getString("songData", null)

        song = if(songJson == null) {
            Song("라일락", "아이유(IU)", 0, 60, false, "music_lilac")
        } else {
            gson.fromJson(songJson, Song::class.java)
        }
        // song 데이터 객체를 생성함

        setMiniPlayer(song)

    }
}