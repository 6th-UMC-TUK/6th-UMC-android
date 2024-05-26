package com.example.flo_clone.Song

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_clone.R
import com.example.flo_clone.databinding.ActivitySongBinding
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Timer

class SongActivity : AppCompatActivity(){
    lateinit var binding: ActivitySongBinding
    lateinit var timer : Timer
    private var mediaPlayer: MediaPlayer? = null // 추후에 미디어 플레이어 해제를 위해 nullable로 선언
    private var gson: Gson = Gson()

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 재생 중이던 노래가 있으면 해당 노래의 정보로 화면을 구성하고,
        // 재생 중이던 노래가 없으면 0번 index의 노래의 정보로 화면을 구성한다.
        initSong()

        initPlayList() // SongDB에 있는 모든 노래를 songs에 저장
        initClickListener()

    }

    private fun initClickListener() {
        binding.songDownIb.setOnClickListener {
            finish()
        }
        binding.songPlayIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }
        binding.songNextIv.setOnClickListener {
            moveSong(+1)
        }
        binding.songPreviousIv.setOnClickListener {
            moveSong(-1)
        }
        binding.songLikeOffIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }
    }
    private fun initSong() {
//        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
//            song = Song(
//                intent.getStringExtra("title")!!,
//                intent.getStringExtra("singer")!!,
//                intent.getIntExtra("coverImg", 0),
//                intent.getIntExtra("second", 0),
//                intent.getIntExtra("playTime", 0),
//                intent.getBooleanExtra("isPlaying", false),
//                intent.getStringExtra("music")!!
//            )
//        }
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song ID", songs[nowPos].id.toString())

        startTimer()
        setPlayer(songs[nowPos])
    }

    // songId로 position을 얻는 메서드
    private fun getPlayingSongPosition(songId: Int): Int {
        for (i in 0 until songs.size) {
            if (songs[i].id == songId) {
                return i
            }
        }
        return 0
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

        timer.interrupt()
        startTimer()

        mediaPlayer?.release()
        mediaPlayer = null

        setPlayer(songs[nowPos])
    }

    private fun initPlayList() {
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun setPlayer(song : Song) {
        binding.songMusicTitleTv.text = song.title
        binding.songMusicSingerTv.text = song.singer
        binding.songStartTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songAlbumIv.setImageResource(song.coverImg!!)
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
        if (song.isLike){
            binding.songLikeOffIv.setImageResource(R.drawable.ic_my_like_on)
        } else{
            binding.songLikeOffIv.setImageResource(R.drawable.ic_my_like_off)
        }

        setPlayerStatus(song.isPlaying)
    }

    private fun setLike(isLike: Boolean){
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateIsLikeById(!isLike,songs[nowPos].id)

        if (!isLike){
            binding.songLikeOffIv.setImageResource(R.drawable.ic_my_like_on)
        } else{
            binding.songLikeOffIv.setImageResource(R.drawable.ic_my_like_off)
        }

    }

    private fun setPlayerStatus(isPlaying : Boolean){
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying) { // 재생중
            binding.songPlayIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        } else { // 일시정지
            binding.songPlayIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if (mediaPlayer?.isPlaying == true) { // 재생 중이 아닐 때에 pause를 하면 에러가 나기 때문에 이를 방지
                mediaPlayer?.pause()
            }
        }
    }

    private fun startTimer() {
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
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

                        if (mills % 1000 == 0f) { // 1초
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
        songs[nowPos].second = ((binding.songSeekbarSb.progress * songs[nowPos].playTime) / 100) / 1000
        songs[nowPos].isPlaying = false
        setPlayerStatus(false) // 음악을 중지하기 위해 false 값

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("songId", songs[nowPos].id)

        editor.apply()
    }

//    override fun onStart() {
//        super.onStart()
//        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
//        val songJson = sharedPreferences.getString("songData", null)
//        songs[nowPos] = if (songJson == null) {
//            Song("라일락", "아이유 (IU)", 0, 0,214, false, "music_lilac")
//        } else {
//            gson.fromJson(songJson, Song::class.java)
//        }
////        val currentPosition = mediaPlayer?.currentPosition
////        mediaPlayer?.seekTo(song.playTime)
//        binding.songSeekbarSb.progress = songs[nowPos].playTime
//
//    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제
        Log.d("destroy","디스트로이 발생")
    }

}