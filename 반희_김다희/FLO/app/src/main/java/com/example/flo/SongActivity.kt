package com.example.flo

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.*
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Timer


class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    lateinit var song: Song
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null
    private var gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClickListener()
        initSong()
        setPlayer(song)
    }

    private fun initClickListener() {
        binding.songDownIb.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
            mediaPlayer?.start()
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
            mediaPlayer?.pause()
        }

        binding.songLikeIv.setOnClickListener {
            finish()
        }

        binding.songRepeatIv.setOnClickListener {
            if (binding.songRepeatIv.colorFilter == null) {
                binding.songRepeatIv.setColorFilter(ContextCompat.getColor(this, R.color.flo))
            } else {
                binding.songRepeatIv.setColorFilter(null)
            }
        }

        binding.songRandomIv.setOnClickListener {
            if (binding.songRandomIv.colorFilter == null) {
                binding.songRandomIv.setColorFilter(ContextCompat.getColor(this, R.color.flo))
            } else {
                binding.songRandomIv.setColorFilter(null)
            }
        }
    }

    private fun setPlayerStatus(isPlaying: Boolean) {
        song.isplaying = isPlaying
        timer.isPlaying = isPlaying

        if (isPlaying) {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        } else {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE

//            if (mediaPlayer?.isPlaying == true) {
//                mediaPlayer?.pause()  }
        }
    }

    private fun initSong() {
        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
            song = Song(
                intent.getIntExtra("coverImg", 0),
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false),
                intent.getStringExtra("music")!!
            )
        }
        startTimer()
    }

    private fun setPlayer(song: Song) {
        binding.songMusicTitleTv.text = intent.getStringExtra("title")!!
        binding.songSingerNameTv.text = intent.getStringExtra("singer")!!
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)

        val timeformat = SimpleDateFormat("mm:ss")
//        binding.songStartTimeTv.text = timeformat.format(mediaPlayer?.currentPosition)
        binding.songEndTimeTv.text = timeformat.format(mediaPlayer?.duration)
        binding.songStartTimeTv.text =
            String.format("%02d:%02d", song.second / 60, song.second % 60)
        setPlayerStatus(song.isplaying)

        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            binding.songStartTimeTv.text = timeformat.format(mediaPlayer?.currentPosition)
        }
    }

    private fun startTimer() {
        timer = Timer(song.playTime, song.isplaying)
        timer.start()
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true) : Thread() {
        private var second: Int = 0
        private var mills: Float = 0f

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
                            binding.songProgressSb.progress = ((mills / playTime) * 100).toInt()
                        }
                        if (mills % 1000 == 0f) {
                            runOnUiThread {
                                binding.songStartTimeTv.text =
                                    String.format("%02d:%02d", second / 60, second % 60)
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

    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        song.second = ((binding.songProgressSb.progress * song.playTime) / 100) / 1000
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val songJson = gson.toJson(song)
        editor.putString("songData", songJson)

        editor.apply()
    }

    //    override fun onStart() {
//        super.onStart()
//        setPlayerStatus(true)
//        song.second = ((binding.songProgressSb.progress * song.playTime) /100) / 1000
//        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
//        val songDataGson = sharedPreferences.getString("songData", "")
//        binding.songStartTimeTv.text = song.
//    }
//    override fun onStart() {
//        super.onStart()
//
//        // SharedPreferences에서 저장된 JSON 문자열을 가져옴
//        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
//        val songJson = sharedPreferences.getString("songData", null)
//
//        if (songJson != null) {
//            // JSON 문자열을 Song 객체로 역직렬화
//            song = gson.fromJson(songJson, Song::class.java)
//
//            // song 객체를 이용하여 UI 또는 필요한 작업을 수행
//            binding.songProgressSb.progress = ((song.second * 1000 * 100) / song.playTime).toInt()
//            // 기타 필요한 초기화 작업
//
//            binding.songStartTimeTv.text = songJson.
//        }
//    }


    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()

        mediaPlayer?.release()
        mediaPlayer = null
    }
}






