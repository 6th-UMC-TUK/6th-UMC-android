package com.example.flo

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityMainBinding
import com.example.flo.home.HomeFragment
import com.example.flo.locker.LockerFragment
import com.example.flo.search.SearchFragment
import com.example.flo.song.Song
import com.example.flo.song.SongActivity
import com.example.flo.song.SongDatabase
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var gson: Gson = Gson()

    var songs = listOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private val updateSeekBarTask = object : Runnable {
        override fun run() {
            if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                binding.mainProgressSb.progress = mediaPlayer!!.currentPosition
                handler.postDelayed(this, 1000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_FLO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainPlayerCl.setOnClickListener {
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", songs[nowPos].id)
            editor.apply()

            mediaPlayer?.release()
            mediaPlayer = null

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)

        }

        initClickListener()
        initBottomNavigation()

        Log.d("MAIN/JWT_TO_SERVER", getJwt().toString())
    }

    private fun getJwt(): String? {
        val spf = this.getSharedPreferences("auth2", AppCompatActivity.MODE_PRIVATE)

        return spf!!.getString("jwt", "")
    }

    override fun onStart() {
        super.onStart()

        inputDummySongs()

        // ID 받아옴
        val spf = getSharedPreferences("song", MODE_PRIVATE)

        val songId = spf.getInt("songId", 0)

        val songDB = SongDatabase.getInstance(this)!!

        if (songs.isNotEmpty()){
//            songs[nowPos] = if (songId == 0) {
//                songDB.songDao().getSong(1)
//            } else {
//                songDB.songDao().getSong(songId)
//            }

            Log.d("song ID", songs[nowPos].id.toString())

            setMiniPlayer(songs[nowPos])
        }
    }

    private fun initClickListener() {
        binding.mainMiniplayerBtn.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.mainPauseBtn.setOnClickListener {
            setPlayerStatus(false)
        }
        binding.mainMiniplayerPrevBtn.setOnClickListener {
            moveSong(-1)
        }
        binding.mainMiniplayerNextBtn.setOnClickListener {
            moveSong(+1)
        }
    }

    private fun setPlayerStatus(isPlaying : Boolean){

        if(isPlaying) {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
            mediaPlayer?.start()
            handler.post(updateSeekBarTask)
            checkSongEnd()
        } else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
            handler.removeCallbacks(updateSeekBarTask)
        }
    }

    // 다음 노래 자동 재생
    private fun checkSongEnd() {
        handler.postDelayed({
            if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                val currentProgress = mediaPlayer!!.currentPosition / 1000
                val totalDuration = songs[nowPos].playTime

                if (currentProgress +1 >= totalDuration) {
                    moveSong(+1)
                } else {
                    checkSongEnd()
                }
            }
        }, 1000)
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

    private fun setMiniPlayer(song: Song){
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainProgressSb.progress = (song.second * 100000) / song.playTime

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)
        mediaPlayer?.seekTo(songs[nowPos].second * 1000)

        binding.mainProgressSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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

        setPlayerStatus(song.isplaying)
    }

    // 현재 재생 정보 저장
    override fun onPause() {
        super.onPause()
        Log.d("pause", "pause 발생")
        songs[nowPos].second = binding.mainProgressSb.progress / 1000
        Log.d("mainpauseprogress", binding.mainProgressSb.progress.toString())

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("songSecond", songs[nowPos].second)
        Log.d("mainpause", songs[nowPos].second.toString())

        editor.apply()
    }

    override fun onStop() {
        super.onStop()

        songs[nowPos].second = binding.mainProgressSb.progress / 1000

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("songSecond", songs[nowPos].second)
        editor.putInt("songId", songs[nowPos].id)

        editor.apply()
        mediaPlayer?.release()
        mediaPlayer = null
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

    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        songs = songDB.songDao().getSongs()

        // 데이터 존재 시 함수 종료
        if (songs.isNotEmpty()) return

        // 비어 있으면 데이터 삽입
        songDB.songDao().insert(
            Song(
                R.drawable.img_album_exp1,
                "Butter",
                "방탄소년단 (BTS)",
                0,
                214,
                false,
                "music_butter",
                false,
                1
            )
        )
        songDB.songDao().insert(
            Song(
                R.drawable.img_album_exp2,
                "Lilac",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_lilac",
                false,
                2
            )
        )
        songDB.songDao().insert(
            Song(
                R.drawable.img_album_exp3,
                "Next Level",
                "에스파 (AESPA)",
                0,
                210,
                false,
                "music_next",
                false,
                3
            )
        )
        songDB.songDao().insert(
            Song(
                R.drawable.img_album_exp4,
                "Boy with Luv",
                "방탄소년단 (BTS)",
                0,
                230,
                false,
                "music_boy",
                false,
                4
            )
        )
        songDB.songDao().insert(
            Song(
                R.drawable.img_album_exp5,
                "BBoom BBoom",
                "모모랜드 (MOMOLAND)",
                0,
                240,
                false,
                "music_bboom",
                false,
                5
            )
        )

        // 데이터가 DB에 잘 들어갔는지 확인
        songs = songDB.songDao().getSongs()
        //Log.d("DB data", _songs.toString())

    }
}
