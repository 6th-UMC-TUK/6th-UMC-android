package com.example.flo

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.album.Album
import com.example.flo.databinding.ActivityMainBinding
import com.example.flo.home.HomeFragment
import com.example.flo.locker.LockerFragment
import com.example.flo.look.LookFragment
import com.example.flo.search.SearchFragment
import com.example.flo.song.Song
import com.example.flo.song.SongActivity
import com.example.flo.song.SongDatabase
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var gson: Gson = Gson()
    private var mediaPlayer: MediaPlayer? = null

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_FLO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainPlayerCl.setOnClickListener {
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", songs[nowPos].id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)

        }

        inputDummySongs()
        initClickListener()
        initBottomNavigation()
        updateMiniPlayerCl(album = Album())
    }

    override fun onStart() {
        super.onStart()

        // ID 받아옴
        val spf = getSharedPreferences("song", MODE_PRIVATE)

        val songId = spf.getInt("songId", 0)

        val songDB = SongDatabase.getInstance(this)!!

        songs[nowPos] = if (songId == 0) {
            songDB.songDao().getSong(1)
        } else {
            songDB.songDao().getSong(songId)
        }

        Log.d("song ID", songs[nowPos].id.toString())
        setMiniPlayer(songs[nowPos])
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
    private fun setPlayerStatus(isPlaying : Boolean){

        if(isPlaying) {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
        } else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
        }
    }
    private fun setMiniPlayer(song: Song){
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainProgressSb.progress = (song.second * 100000) / song.playTime

        /*
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)

        val timeformat = SimpleDateFormat("mm:ss")
        binding.songStartTimeTv.text = timeformat.format(mediaPlayer?.currentPosition)
        binding.songEndTimeTv.text = timeformat.format(mediaPlayer?.duration)
        binding.songStartTimeTv.text =  String.format("%02d:%02d", song.second / 60, song.second %60)
        setPlayerStatus(song.isplaying)

        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
//            binding.songStartTimeTv.text = timeformat.format(mediaPlayer?.currentPosition)
        */
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

    fun updateMiniPlayerCl(album: Album) {
        Log.d("MainActivity", "updateMiniPlayerCl called with album: ${album.title}")
        // 안됨

        binding.mainMiniplayerTitleTv.text = album.title
        binding.mainMiniplayerSingerTv.text = album.singer
//        binding.mainMiniplayerProgressSb.progress = 0
    }

    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

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
                "music_butter"
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
                "music_lilac"
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
                "music_next"
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
                "music_boy"
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
                "music_bboom"
            )
        )

        // 데이터가 DB에 잘 들어갔는지 확인
        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())

    }
}
