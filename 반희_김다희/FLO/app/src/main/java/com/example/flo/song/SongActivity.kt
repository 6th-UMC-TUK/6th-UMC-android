package com.example.flo.song

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.flo.MainActivity
import com.example.flo.R
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
    lateinit var timer : Timer
    private var mediaPlayer: MediaPlayer? = null
    private var gson: Gson = Gson()

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClickListener()
        initSong()
        setPlayer(songs[nowPos])
        initPlayList()
    }

    private fun initClickListener() {
        binding.songDownIb.setOnClickListener {
            finish()
        }

        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
            mediaPlayer?.start()
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
            mediaPlayer?.pause()
        }

        binding.songNextIv.setOnClickListener {
            moveSong(+1)
        }
        binding.songPreviousIv.setOnClickListener {
            moveSong(-1)
        }
        binding.songLikeIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
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

    private fun setLike(islike: Boolean){
        songs[nowPos].isLike = !islike
        songDB.songDao().updateIsLikeById(!islike,songs[nowPos].id)

        if (!islike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }
    private fun setPlayerStatus(isPlaying: Boolean) {
        songs[nowPos].isplaying = isPlaying
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

        timer.interrupt()   // 원래 실행 중이던 쓰레드 멈춤
        songs[nowPos].second = 0
        startTimer()

        // 미디어 플레이어 해제
        mediaPlayer?.release()
        mediaPlayer = null

        // 데이터 렌더링 다시 해줌
        setPlayerStatus(true)
        setPlayer(songs[nowPos])
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initSong(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())

        if (songs.isEmpty()) {
            Toast.makeText(this, "No songs available", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        // 현재 보여지는 song이 nowPos
        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song ID", songs[nowPos].id.toString())

        startTimer()
        setPlayer(songs[nowPos])
    }

    // songId로 position 얻음
    private fun getPlayingSongPosition(songId: Int): Int {
        for (i in 0 until songs.size) {
            if (songs[i].id == songId) {
                return i
            }
        }
        return 0
    }

    private fun setPlayer(song: Song){
        binding.songMusicTitleTv.text = song.title
        binding.songSingerNameTv.text = song.singer
        binding.songStartTimeTv.text = String.format("%02d:%02d", songs[nowPos].second / 60, songs[nowPos].second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songAlbumIv.setImageResource(song.coverImg!!)
        binding.songProgressSb.progress = (songs[nowPos].second * 1000 / song.playTime)

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)

        val timeformat = SimpleDateFormat("mm:ss")
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second %60)
        binding.songEndTimeTv.text = timeformat.format(mediaPlayer?.duration)
        binding.songProgressSb.max = song.playTime * 1000
        mediaPlayer?.seekTo(songs[nowPos].second * 1000)
        // setPlayerStatus(song.isplaying)


        binding.songProgressSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    timer.interrupt()
                    val newSecond = (progress * song.playTime) / 100
                    mediaPlayer?.seekTo(newSecond * 1000)
                    binding.songStartTimeTv.text = String.format("%02d:%02d", newSecond / 60, newSecond % 60)
                    songs[nowPos].second = newSecond
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                mediaPlayer?.pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (songs[nowPos].isplaying) {
                    mediaPlayer?.start()
                    startTimer()
                }
            }
        })

        if (song.isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

        setPlayerStatus(song.isplaying)

        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            binding.songStartTimeTv.text = timeformat.format(mediaPlayer?.currentPosition)
        }
    }

    private fun startTimer(){
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isplaying)
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
        songs[nowPos].second = ((binding.songProgressSb.progress * songs[nowPos].playTime) / 100) / 1000
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("songId", songs[nowPos].id)
        editor.putInt("songSecond", songs[nowPos].second)
        Log.d("songSecond", songs[nowPos].second.toString())

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






