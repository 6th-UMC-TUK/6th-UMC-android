package com.example.flo_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        inputDummySongs()
        inputDummyAlbums()

        binding.mainPlayerCl.setOnClickListener {
            // 뷰에 대한 함수
//            startActivity(Intent(this, SongActivity::class.java))
            // Intent를 사용하여 this에서 SongActivity로 이동
//            val intent = Intent(this, SongActivity::class.java)
//            // 재사용하기 쉽도록 선언
//            intent.putExtra("title", song.title)
//            intent.putExtra("singer", song.singer)
//            intent.putExtra("second", song.second)
//            intent.putExtra("isPlaying", song.playTime)
//            intent.putExtra("isPlaying", song.isPlaying)
//            intent.putExtra("music", song.music)
//            startActivity(intent)
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", song.id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
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
//        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
//        val songJson = sharedPreferences.getString("songData", null)
//
//        song = if(songJson == null) {
//            Song("라일락", "아이유(IU)", 0, 60, false, "music_lilac")
//        } else {
//            gson.fromJson(songJson, Song::class.java)
//        }
        // song 데이터 객체를

        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        val songDB = SongDatabase.getInstance(this)!!
        song = if(songId == 0) {
            songDB.songDao().getSong(1)
        } else {
            songDB.songDao().getSong(songId)
        }

        Log.d("song ID", song.id.toString())

        binding.mainProgressSb.progress = (song.second * 100000) / song.playTime

        setMiniPlayer(song)
    }

    private fun inputDummySongs(){
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.albumDao().getAlbums()

        // songs가 비어있지 않다면, 함수 종료
        // 비어있다면 더미 데이터를 넣어줌
        if (songs.isNotEmpty()) return

        songDB.songDao().insert(
            Song(
                "Lilac",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_lilac",
                R.drawable.img_album_exp2,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Flu",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_flu",
                R.drawable.img_album_exp2,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Butter",
                "방탄소년단 (BTS)",
                0,
                190,
                false,
                "music_butter",
                R.drawable.img_album_exp,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Next Level",
                "에스파 (AESPA)",
                0,
                210,
                false,
                "music_next",
                R.drawable.img_album_exp3,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Boy with Luv",
                "music_boy",
                0,
                230,
                false,
                "music_lilac",
                R.drawable.img_album_exp4,
                false,
            )
        )
        songDB.songDao().insert(
            Song(
                "BBoom BBOom",
                "모모랜드 (MOMOLAND)",
                0,
                240,
                false,
                "music_bboom",
                R.drawable.img_album_exp5,
                false,
            )
        )

        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())

    }

    private fun inputDummyAlbums(){
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.songDao().getSongs()

        // songs가 비어있지 않다면, 함수 종료
        // 비어있다면 더미 데이터를 넣어줌
        if (albums.isNotEmpty()) return

        songDB.albumDao().insert(
            Album(
                0,
                "IU 5th Album 'LILAC",
                "아이유 (IU)",
                R.drawable.img_album_exp2,
            )
        )

        songDB.albumDao().insert(
            Album(
                1,
                "Butter",
                "방탄소년단 (BTS)",
                R.drawable.img_album_exp,
            )
        )

        songDB.albumDao().insert(
            Album(
                2,
                "iScreaM Vol.10 : Next Level Remixes",
                "에스파 (AESPA)",
                R.drawable.img_album_exp3,
            )
        )

        songDB.albumDao().insert(
            Album(
                3,
                "Boy Wilt Luv",
                "music_boy",
                R.drawable.img_album_exp4,
            )
        )
        songDB.albumDao().insert(
            Album(
                4,
                "BooM BooM",
                "모모랜드 (MOMOLAND)",
                R.drawable.img_album_exp5,
            )
        )

        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())

    }
}