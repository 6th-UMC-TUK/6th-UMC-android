package com.example.flo_clone

import android.content.Intent
import android.media.MediaPlayer
import android.media.tv.TvContract.Programs.Genres.MUSIC
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.flo_clone.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var song: Song = Song()
    private lateinit var selectedSong: Song
    private lateinit var songDB: SongDatabase
    private var mediaPlayer: MediaPlayer? = null
    private var newPos = 0
    private var songs = listOf<Song>()
    private val playerMusic = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val musicTitle = result.data?.getStringExtra(MUSIC_TITLE)
        val musicSinger = result.data?.getStringExtra(MUSIC_SINGER)
        if (musicTitle != null && musicSinger != null && result.resultCode == RESULT_OK) {
            binding.tvMainPlayingMusicTitle.text = musicTitle
            binding.tvMainPlayingMusicSinger.text = musicSinger
            mediaPlayer?.seekTo(result.data?.getIntExtra(CURRENT_POSITION, 0)!!)
            Toast.makeText(this, "앨범 제목: $musicTitle", Toast.LENGTH_SHORT).show()
            setPlayersStatus(result.data?.getBooleanExtra(IS_PLAYING, false)!!)
        }
    }

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
        songDB = SongDatabase.getInstance(this)!!
        inputDummySongs()
        inputDummyAlbums()
        initClickListener()
        initSelectedMusic()
        updateMusicPlayer()
        initView()

        binding.activityMainPlayer.setOnClickListener {
            Log.d("Player_song", selectedSong.toString())
            val editor = getSharedPreferences(SONG, MODE_PRIVATE)
            editor.putInt(SONG_ID, selectedSong.id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra(SECOND, mediaPlayer?.currentPosition)
            intent.putExtra(IS_PLAYING, selectedSong.isPlaying)
            playerMusic.launch(intent)
        }
        songDB.songDao().updateTitle("Next Level", 8)

//        binding.mainPlayerCl.setOnClickListener {
//            // 뷰에 대한 함수
////            startActivity(Intent(this, SongActivity::class.java))
//            // Intent를 사용하여 this에서 SongActivity로 이동
////            val intent = Intent(this, SongActivity::class.java)
////            // 재사용하기 쉽도록 선언
////            intent.putExtra("title", song.title)
////            intent.putExtra("singer", song.singer)
////            intent.putExtra("second", song.second)
////            intent.putExtra("isPlaying", song.playTime)
////            intent.putExtra("isPlaying", song.isPlaying)
////            intent.putExtra("music", song.music)
////            startActivity(intent)
//            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
//            editor.putInt("songId", song.id)
//            editor.apply()
//
//            val intent = Intent(this, SongActivity::class.java)
//            startActivity(intent)
//        }
    }

    private fun initView() {
        initNavigator()
    }

    private fun initNavigator() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activity_main_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        binding.activityMainBnv.setupWithNavController(navController)
    }

    private fun updateMusicPlayer() {
        binding.activityMainBtnPlay.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.activityMainBtnPause.setOnClickListener {
            setPlayerStatus(false)
        }
    }

    private fun initPlayList() {
        songs = songDB.songDao().getSongsInAlbum(selectedSong.albumIdx)
        Log.d("songs", songs.toString())
    }

    private fun getPlayingSongPosition(songId: Int): Int {
        for (i in 0 until songs.size) {
            if (songs[i].id == songId)
                return i
        }
        return 0
    }

    private fun moveSong(direct: Int) {
        if (newPos + direct < 0) {
            Toast.makeText(this, "first song", Toast.LENGTH_SHORT).show()
            return
        }
        if (newPos + direct >= songs.size) {
            Toast.makeText(this, "last song", Toast.LENGTH_SHORT).show()
            return
        }

        newPos += direct
        mediaPlayer?.release()
        mediaPlayer = null
        songs[newPos].isPlaying = true
        Log.d("moveSong", songs[newPos].toString())
        selectedSong = songs[newPos]
        setPlayer(songs[newPos])
    }

    private fun setPlayer(song: Song) {
        binding.tvMainPlayingMusicTitle.text = song.title
        binding.tvMainPlayingMusicSinger.text = song.singer

        val musicFile = resources.getIdentifier(song.music, "raw", this.packageName)
        if (mediaPlayer != null) {
            mediaPlayer?.release()
            mediaPlayer = null
        }
        mediaPlayer = MediaPlayer.create(this, musicFile)


        binding.activityMainMusicSeekbar.max = mediaPlayer?.duration!!
        binding.activityMainMusicSeekbar.progress = song.second
        mediaPlayer?.seekTo(song.second)
        setPlayerStatus(song.isPlaying)
    }


    private fun initSelectedMusic() {
        supportFragmentManager.setFragmentResultListener(MUSIC, this) { _, bundle ->
            val albumIdx = bundle.getInt(SONG_ALBUM_INDEX, 0)
            var playList = songDB.songDao().getSongsInAlbum(albumIdx)
            songs = playList
            selectedSong = songs[0]
            selectedSong.isPlaying = true
            setPlayer(selectedSong)
        }
    }

    private fun initClickListener() {
        binding.activityMainBtnNext.setOnClickListener {
            moveSong(+1)
        }
        binding.activityMainBtnPrevious.setOnClickListener {
            moveSong(-1)
        }
    }

    //    private fun setMiniPlayer(song: Song){
//        binding.mainMiniplayerTitleTv.text = song.title
//        binding.mainMiniplayerSingerTv.text = song.singer
//        binding.mainProgressSb.progress = (song.second*100000)/song.playTime
//        // seekbar를 만들 때 최대 값을 100000으로 지정했기 때문에 100000을 곱함
//    }
    private fun setPlayerStatus(isPlaying: Boolean) {
        selectedSong.isPlaying = isPlaying
        if (isPlaying) {
            binding.activityMainBtnPlay.visibility = View.GONE
            binding.activityMainBtnPause.visibility = View.VISIBLE
            mediaPlayer?.start()
        } else {
            binding.activityMainBtnPlay.visibility = View.VISIBLE
            binding.activityMainBtnPause.visibility = View.GONE
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        }
        updateSeekBar()
    }

    override fun onStart() {
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

        val spf = getSharedPreferences(SONG, MODE_PRIVATE)
        val songId = spf.getInt(SONG_ID, 0)

//        val songDB = SongDatabase.getInstance(this)!!
//        song = if (songId == 0) {
//            songDB.songDao().getSong(1)
//        } else {
//            songDB.songDao().getSong(songId)
//        }
        selectedSong = if(songId == 0) {
            songDB.songDao().getSong(8)
        } else {
            songDB.songDao().getSong(songId)
        }
        initPlayList()
        newPos = getPlayingSongPosition(songId)
        setPlayer(songs[newPos])

        Log.d("song ID", song.id.toString())

//        binding.mainProgressSb.progress = (song.second * 100000) / song.playTime
//        setMiniPlayer(song)
    }

    private fun updateSeekBar(){
        if(mediaPlayer == null) return
        CoroutineScope(Dispatchers.Main).launch {
            while (mediaPlayer?.isPlaying!!){
                delay(50)
                binding.activityMainMusicSeekbar.progress = meidaPlayer?.currentPosition!!
            }
        }
    }
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun inputDummySongs() {
        val songList = listOf(
            Song(
                "Next Leve", "aespa", 0, 222, false, "music_nextlevel",
                R.drawable.img_album_exp3, false, "I'm on the Next Level Yeah\n절대적 룰을 지켜", 1
            ),
            Song(
                "작은 것들을 위한 시", "방탄소년단", 0, 229, false, "music_lilac",
                R.drawable.img_album_exp4, false, "모든 게 궁금해\nHow's your day", 2
            ),
            Song(
                "BAAM", "모모랜드 (MOMOLAND)", 0, 208, false, "music_nextlevel",
                R.drawable.img_album_exp5, false, "Bae Bae Bae BAAM BAAM\nBae Bae Bae BAAM BAAM", 3
            ),
            Song(
                "Weekend", "태연", 0, 234, false, "music_nextlevel",
                R.drawable.img_album_exp6, false, "가장 가까운 바다\n혼자만의 영화관", 4
            ),
            Song(
                "Black Mamba", "aespa", 0, 222, false, "music_nextlevel",
                R.drawable.img_album_exp3, false, "I'm addicted\n끊임없이", 1
            )
        )

        val songs = songDB.songDao().getSongs()
        if(songs.isNotEmpty()) return
        songList.forEach {
            songDB.songDao().insert(it)
        }
    }
//    private fun inputDummySongs() {
//        val songDB = SongDatabase.getInstance(this)!!
//        val songs = songDB.albumDao().getAlbums()
//
//        // songs가 비어있지 않다면, 함수 종료
//        // 비어있다면 더미 데이터를 넣어줌
//        if (songs.isNotEmpty()) return
//
//        songDB.songDao().insert(
//            Song(
//                "Lilac",
//                "아이유 (IU)",
//                0,
//                200,
//                false,
//                "music_lilac",
//                R.drawable.img_album_exp2,
//                false,
//            )
//        )
//
//        songDB.songDao().insert(
//            Song(
//                "Flu",
//                "아이유 (IU)",
//                0,
//                200,
//                false,
//                "music_flu",
//                R.drawable.img_album_exp2,
//                false,
//            )
//        )
//
//        songDB.songDao().insert(
//            Song(
//                "Butter",
//                "방탄소년단 (BTS)",
//                0,
//                190,
//                false,
//                "music_butter",
//                R.drawable.img_album_exp,
//                false,
//            )
//        )
//
//        songDB.songDao().insert(
//            Song(
//                "Next Level",
//                "에스파 (AESPA)",
//                0,
//                210,
//                false,
//                "music_next",
//                R.drawable.img_album_exp3,
//                false,
//            )
//        )
//
//        songDB.songDao().insert(
//            Song(
//                "Boy with Luv",
//                "music_boy",
//                0,
//                230,
//                false,
//                "music_lilac",
//                R.drawable.img_album_exp4,
//                false,
//            )
//        )
//        songDB.songDao().insert(
//            Song(
//                "BBoom BBOom",
//                "모모랜드 (MOMOLAND)",
//                0,
//                240,
//                false,
//                "music_bboom",
//                R.drawable.img_album_exp5,
//                false,
//            )
//        )
//
//        val _songs = songDB.songDao().getSongs()
//        Log.d("DB data", _songs.toString())
//
//    }
//
//    private fun inputDummyAlbums() {
//        val songDB = SongDatabase.getInstance(this)!!
//        val albums = songDB.songDao().getSongs()
//
//        // songs가 비어있지 않다면, 함수 종료
//        // 비어있다면 더미 데이터를 넣어줌
//        if (albums.isNotEmpty()) return
//
//        songDB.albumDao().insert(
//            Album(
//                0,
//                "IU 5th Album 'LILAC",
//                "아이유 (IU)",
//                R.drawable.img_album_exp2,
//            )
//        )
//
//        songDB.albumDao().insert(
//            Album(
//                1,
//                "Butter",
//                "방탄소년단 (BTS)",
//                R.drawable.img_album_exp,
//            )
//        )
//
//        songDB.albumDao().insert(
//            Album(
//                2,
//                "iScreaM Vol.10 : Next Level Remixes",
//                "에스파 (AESPA)",
//                R.drawable.img_album_exp3,
//            )
//        )
//
//        songDB.albumDao().insert(
//            Album(
//                3,
//                "Boy Wilt Luv",
//                "music_boy",
//                R.drawable.img_album_exp4,
//            )
//        )
//        songDB.albumDao().insert(
//            Album(
//                4,
//                "BooM BooM",
//                "모모랜드 (MOMOLAND)",
//                R.drawable.img_album_exp5,
//            )
//        )
//
//        val _songs = songDB.songDao().getSongs()
//        Log.d("DB data", _songs.toString())
//
//    }
}