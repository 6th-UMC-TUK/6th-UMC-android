package com.example.flo_clone

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.flo_clone.Home.HomeFragment
import com.example.flo_clone.Locker.LockerFragment
import com.example.flo_clone.Look.LookFragment
import com.example.flo_clone.Search.SearchFragment
import com.example.flo_clone.Song.Song
import com.example.flo_clone.Song.SongActivity
import com.example.flo_clone.Song.SongDatabase
import com.example.flo_clone.databinding.ActivityMainBinding
import com.google.firebase.perf.util.Timer
import com.google.gson.Gson
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var gson: Gson = Gson()
    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private val updateSeekBarTask = object : Runnable {
        override fun run() {
            if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                binding.mainSeekbarSb.progress = mediaPlayer!!.currentPosition
                handler.postDelayed(this, 1000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Flo_clone)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputDummySongs()
        initClickListener()
        initPlayList()
        initBottomNavigation()

        binding.mainPlayerCl.setOnClickListener {
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", songs[nowPos].id)
            editor.apply()

            mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
            mediaPlayer = null // 미디어 플레이어 해제
            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        // songId는 SongActivity에서 onPause가 호출되었을 때
        // 재생 중이던 노래의 id(pk)이다.
        val songId = sharedPreferences.getInt("songId", 0)

        // SongDatabase의 인스턴스를 가져온다.
        val songDB = SongDatabase.getInstance(this)!!

        songs[nowPos] = if (songId == 0) { // 재생 중이던 노래가 없었으면
            songDB.songDao().getSong(1)
        } else { // 재생 중이던 노래가 있었으면
            songDB.songDao().getSong(songId)
        }

        Log.d("song ID", songs[nowPos].id.toString())
        setMiniPlayer(songs[nowPos]) // song의 정보로 MiniPlayer를 setting
    }

    private fun initClickListener() {
        binding.mainMiniplayBtn.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.mainPauseBtn.setOnClickListener {
            setPlayerStatus(false)
            Log.d("pausebtn", songs[nowPos].second.toString())
            Log.d("pausebtn2", mediaPlayer?.currentPosition.toString())
        }
        binding.mainMinipreviousIv.setOnClickListener {
            moveSong(-1)
        }
        binding.mainMininextIv.setOnClickListener {
            moveSong(+1)
        }
    }
    private fun setPlayerStatus(isPlaying : Boolean){

        if(isPlaying) {
            binding.mainMiniplayBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
            mediaPlayer?.start()

            // 추가: 현재 노래의 재생 시간을 체크하는 스레드 시작
            handler.post(updateSeekBarTask)
            checkSongEnd() // 추가: 노래가 끝났는지 확인하고 다음 노래로 이동할 수 있도록 함
        } else {
            binding.mainMiniplayBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
            handler.removeCallbacks(updateSeekBarTask)
        }
    }

    // 다음 노래로 자동으로 넘어가게 함
    private fun checkSongEnd() {
        handler.postDelayed({
            if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                val currentProgress = mediaPlayer!!.currentPosition / 1000
                val totalDuration = songs[nowPos].playTime

                if (currentProgress +1 >= totalDuration) {
                    // 현재 노래가 종료되었으므로 다음 노래로 이동
                    moveSong(+1)
                } else {
                    // 다음 체크를 위해 재귀 호출
                    checkSongEnd()
                }
            }
        }, 1000) // 1초마다 체크
    }
    private fun moveSong(direct: Int) {
        if (nowPos + direct < 0) {
            Toast.makeText(this, "first song", Toast.LENGTH_SHORT).show()
            return
        }
        if (nowPos + direct >= songs.size) {
            Toast.makeText(this, "last song", Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direct

//        startTimer()
        mediaPlayer?.release()
        mediaPlayer = null
        songs[nowPos].second = 0

        setMiniPlayer(songs[nowPos])
        setPlayerStatus(true)
    }

    override fun onResume() {
        super.onResume()

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songId = sharedPreferences.getInt("songId", 0)
        songs[nowPos].second = sharedPreferences.getInt("songSecond", 0)

        nowPos = getPlayingSongPosition(songId)
        setMiniPlayer(songs[nowPos])
        setPlayerStatus(true)
    }

    private fun getPlayingSongPosition(songId: Int): Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }
    private fun setMiniPlayer(song: Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainSeekbarSb.max = song.playTime * 1000

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)
        mediaPlayer?.seekTo(songs[nowPos].second * 1000)

        binding.mainSeekbarSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                    songs[nowPos].second = progress / 1000
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // 사용자가 터치하기 시작할 때 호출
                mediaPlayer?.pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // 사용자가 터치를 끝냈을 때 호출
                mediaPlayer?.start()
                checkSongEnd()
            }
        })

        setPlayerStatus(song.isPlaying)
    }

    // 현재 재생 정보를 저장해두는 역할
    override fun onPause() {
        super.onPause()
        Log.d("pause", "pause 발생")
        songs[nowPos].second = binding.mainSeekbarSb.progress / 1000
        Log.d("mainpauseprogress", binding.mainSeekbarSb.progress.toString())

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("songSecond", songs[nowPos].second)
        Log.d("mainpause", songs[nowPos].second.toString())

        editor.apply()
    }


    // 사용자가 포커스를 잃으면 미디어 플레이어 해제
    override fun onStop() {
        super.onStop()
        Log.d("onStop", "onstop발생")

        songs[nowPos].second = binding.mainSeekbarSb.progress / 1000

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("songSecond", songs[nowPos].second)
        editor.putInt("songId", songs[nowPos].id)

        editor.apply()
        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제
    }

    // 더미 데이터 삽입
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
                1
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
                1
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
                2
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
                3
            )
        )


        songDB.songDao().insert(
            Song(
                "Boy with Luv",
                "방탄소년단 (BTS)",
                0,
                253,
                false,
                "music_boy",
                R.drawable.img_album_exp4,
                false,
                4
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
                5
            )
        )

        // DB에 데이터가 잘 들어갔는지 확인
        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())

    }

    fun hideMiniPlayer() { // MiniPlayer 숨기기
        binding.mainPlayerCl.visibility = View.GONE
    }

    fun showMiniPlayer() { // MiniPlayer 보이기
        binding.mainPlayerCl.visibility = View.VISIBLE
    }
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