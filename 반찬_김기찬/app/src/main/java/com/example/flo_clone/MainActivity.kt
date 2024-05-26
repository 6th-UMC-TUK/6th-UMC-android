package com.example.flo_clone

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.flo_clone.Home.HomeFragment
import com.example.flo_clone.Locker.LockerFragment
import com.example.flo_clone.Look.LookFragment
import com.example.flo_clone.Search.SearchFragment
import com.example.flo_clone.Song.Song
import com.example.flo_clone.Song.SongActivity
import com.example.flo_clone.Song.SongDatabase
import com.example.flo_clone.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var song: Song = Song()
    private var gson: Gson = Gson()

    private var mediaPlayer: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Flo_clone)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputDummySongs()
        initClickListener()
        initBottomNavigation()

//        val song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString(), 0, 60, false, "music_lilac")

        binding.mainPlayerCl.setOnClickListener {
//            val intent = Intent(this, SongActivity::class.java)
//            intent.putExtra("title", song.title)
//            intent.putExtra("singer", song.singer)
//            intent.putExtra("coverImg", song.coverImg)
//            intent.putExtra("second", song.second)
//            intent.putExtra("playTime", song.playTime)
//            intent.putExtra("isPlaying", song.isPlaying)
//            intent.putExtra("music", song.music)
//
//            startActivity(intent)
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", song.id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
//        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
//        val songJson = sharedPreferences.getString("songData", null)
//
//        song = if (songJson == null) {
//            Song("라일락", "아이유 (IU)", 0, 0,214, false, "music_lilac")
//        } else {
//            gson.fromJson(songJson, Song::class.java)
//        }

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        // songId는 SongActivity에서 onPause가 호출되었을 때
        // 재생 중이던 노래의 id(pk)이다.
        val songId = sharedPreferences.getInt("songId", 0)

        // SongDatabase의 인스턴스를 가져온다.
        val songDB = SongDatabase.getInstance(this)!!

        song = if (songId == 0) { // 재생 중이던 노래가 없었으면
            songDB.songDao().getSong(1)
        } else { // 재생 중이던 노래가 있었으면
            songDB.songDao().getSong(songId)
        }

        Log.d("song ID", song.id.toString())
        setMiniPlayer(song) // song의 정보로 MiniPlayer를 setting
    }
    private fun initClickListener() {
        binding.mainMiniplayBtn.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.mainPauseBtn.setOnClickListener {
            setPlayerStatus(false)
        }
    }
    private fun setPlayerStatus(isPlaying : Boolean){

        if(isPlaying) {
            binding.mainMiniplayBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
//            mediaPlayer?.start()
        } else {
            binding.mainMiniplayBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
//            if (mediaPlayer?.isPlaying == true) {
//                mediaPlayer?.pause()
//            }
        }
    }
    private fun setMiniPlayer(song: Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainSeekbarSb.progress = (song.second * 100000) / song.playTime
//        while (mediaPlayer?.isPlaying == true) {
//            runOnUiThread{
//                binding.mainSeekbarSb.progress = mediaPlayer!!.currentPosition
//            }
//        }
//        val music = resources.getIdentifier(song.music, "raw", this.packageName)
//        mediaPlayer = MediaPlayer.create(this, music)
//        setPlayerStatus(song.isPlaying)
    }

    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        // songs에 데이터가 이미 존재해 더미 데이터를 삽입할 필요가 없음
        if (songs.isNotEmpty()) return

        // songs에 데이터가 없을 때에는 더미 데이터를 삽입해주어야 함
        songDB.songDao().insert(
            Song(
                "Lilac",
                "아이유 (IU)",
                0,
                214,
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
                190,
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
                182,
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
                237,
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
                253,
                false,
                "music_boy",
                R.drawable.img_album_exp4,
                false,
            )
        )


        songDB.songDao().insert(
            Song(
                "BBoom BBoom",
                "모모랜드 (MOMOLAND)",
                0,
                211,
                false,
                "music_bboom",
                R.drawable.img_album_exp5,
                false,
            )
        )

        // DB에 데이터가 잘 들어갔는지 확인
        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())

    }

//    override fun onDestroy() {
//        super.onDestroy()
//        timer.interrupt()
//        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
//        mediaPlayer = null // 미디어 플레이어 해제
//    }


    private fun initBottomNavigation(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }


    }
}