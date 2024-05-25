package com.example.flo_clone.Song

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_clone.databinding.ActivitySongBinding
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Timer

class SongActivity : AppCompatActivity(){
    lateinit var binding: ActivitySongBinding
    lateinit var song : Song
    lateinit var timer : Timer
    private var mediaPlayer: MediaPlayer? = null
    private var gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.songDownIb.setOnClickListener {
            finish()
        }
        binding.songPlayIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }

        initSong()
        setPlayer(song)

    }

//    override fun onDestroy() {
//        super.onDestroy()
//        timer.interrupt()
//    }

    private fun initSong() {
        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("coverImg", 0),
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false),
                intent.getStringExtra("music")!!
            )
        }
        startTimer()
    }

    private fun setPlayer(song : Song) {
        binding.songMusicTitleTv.text = intent.getStringExtra("title")!!
        binding.songMusicSingerTv.text = intent.getStringExtra("singer")!!
        binding.songStartTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songSeekbarSb.progress = (song.second * 1000 / song.playTime)
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)
        val timeformat = SimpleDateFormat("mm:ss")
        binding.songEndTv.text = timeformat.format(mediaPlayer?.duration)


//        binding.songSeekbarSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                if (fromUser) {
//                    binding.songStartTv.text = String.format(
//                        "%02d:%02d",
//                        mediaPlayer?.currentPosition ?: (0 / 60000),
//                        (mediaPlayer?.currentPosition ?: (0 - 60000 * (mediaPlayer?.currentPosition
//                            ?: (0 / 60000)))) / 1000
//                    )
//
//
//                    mediaPlayer?.seekTo(progress)
//                }
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar?) {
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//            }
//
//        })
        setPlayerStatus(song.isPlaying)
    }

    private fun setPlayerStatus(isPlaying : Boolean){
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying) {
            binding.songPlayIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        } else {
            binding.songPlayIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        }
    }

    private fun startTimer() {
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true) : Thread() {
        private var second : Int = 0
        private var mills : Float = 0f

        override fun run() {
            super.run()
            try {
                while (true) {
                    if (second >= playTime) {
                        break
                    }

                    if (isPlaying) {
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.songSeekbarSb.progress = ((mills / playTime) * 100).toInt()
                        }

                        if (mills % 1000 == 0f) {
                            runOnUiThread {
                                binding.songStartTv.text = String.format("%02d:%02d", second / 60, second % 60)
                            }
                            second++
                        }
                    }
                }
            } catch (e: InterruptedException) {
                Log.d("Song", "쓰레드가 죽었습니다. ${e.message}")
            }
        }

    }
    override fun onPause() { // 사용자가 포커스를 잃었을 때 음악 중지
        super.onPause()
        setPlayerStatus(false) // 음악을 중지하기 위해 false 값
        song.second = ((binding.songSeekbarSb.progress * song.playTime) / 100) / 1000
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val songJson = gson.toJson(song)
        editor.putString("songData", songJson)

        editor.apply()
    }

    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songJson = sharedPreferences.getString("songData", null)
        song = if (songJson == null) {
            Song("라일락", "아이유 (IU)", 0, 0,214, false, "music_lilac")
        } else {
            gson.fromJson(songJson, Song::class.java)
        }
//        val currentPosition = mediaPlayer?.currentPosition
//        mediaPlayer?.seekTo(song.playTime)
        binding.songSeekbarSb.progress = song.playTime

    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제
        Log.d("destroy","디스트로이 발생")
    }

}