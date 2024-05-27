package umc.mission.floclone

import android.content.*
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.coroutines.*
import umc.mission.floclone.data.*
import umc.mission.floclone.databinding.ActivityMainBinding
import umc.mission.floclone.locker.LockerFragment
import java.util.Collections.addAll

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
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
            setPlayerStatus(result.data?.getBooleanExtra(IS_PLAYING, false)!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_FloClone)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        songDB = SongDatabase.getInstance(this)!!

        initBottomNavigation()
        inputDummyAlbums()
        inputDummySongs()
        initClickListener()
        initSelectedMusic()
        updateMusicPlayer()

        binding.activityMainPlayer.setOnClickListener {
            Log.d("player_song", selectedSong.toString())
            val editor = getSharedPreferences(SONG, MODE_PRIVATE).edit()
            editor.putInt(SONG_ID, selectedSong.id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra(SECOND, mediaPlayer?.currentPosition)
            intent.putExtra(IS_PLAYING, selectedSong.isPlaying)
            playerMusic.launch(intent)
        }
        songDB.songDao().updateTitle("Next Level", 8)
    }

    private fun initBottomNavigation() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.activity_main_fragment_container, HomeFragment())
            commitAllowingStateLoss()
        }

        binding.activityMainBnv.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.mnu_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_main_fragment_container, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.mnu_locker -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_main_fragment_container, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.mnu_look -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_main_fragment_container, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.mnu_search -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_main_fragment_container, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
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
        if (::selectedSong.isInitialized) {
            songs = songDB.songDao().getSongsInAlbum(selectedSong.albumIdx)
            Log.d("songs", songs.toString())
        }
    }

    private fun getPlayingSongPosition(songId: Int): Int {
        for (i in 0 until songs.size) {
            if (songs[i].id == songId)
                return i
        }
        return 0
    }

    private fun moveSong(direct: Int){
        if(newPos + direct < 0){
            Toast.makeText(this, "first song", Toast.LENGTH_SHORT).show()
            return
        }
        if(newPos + direct >= songs.size){
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
        if(mediaPlayer != null){
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
            if (playList.isNotEmpty()) { // 플레이리스트가 비어있지 않은지 확인
                songs = playList
                selectedSong = songs[0]
                selectedSong.isPlaying = true

                setPlayer(selectedSong)
            } else {
                Log.e("MainActivity", "Playlist is empty")
            }
        }
    }

    private fun initClickListener(){
        binding.activityMainBtnNext.setOnClickListener {
            moveSong(+1)
        }

        binding.activityMainBtnPrevious.setOnClickListener {
            moveSong(-1)
        }
    }

    private fun setPlayerStatus(isPlaying: Boolean) {
        if (::selectedSong.isInitialized) {
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
    }

    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences(SONG, MODE_PRIVATE)
        val songId = sharedPreferences.getInt(SONG_ID, 0)

        selectedSong = if (songId == 0) {
            songDB.songDao().getSong(8)
        } else {
            songDB.songDao().getSong(songId)
        }
        initPlayList()
        newPos = getPlayingSongPosition(songId)
        if (::selectedSong.isInitialized && songs.isNotEmpty()) { // 초기화 여부 확인
            setPlayer(songs[newPos])
            Log.d("select", selectedSong.toString())
        } else {
            Log.e("MainActivity", "selectedSong is not initialized or songs list is empty")
        }
    }

    private fun updateSeekBar() {
        if (mediaPlayer == null) return
        CoroutineScope(Dispatchers.Main).launch {
            while (mediaPlayer?.isPlaying!!) {
                delay(50)
                binding.activityMainMusicSeekbar.progress = mediaPlayer?.currentPosition!!
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

    private fun inputDummyAlbums() {
        val albumList = listOf(
            Album(1, "Next Level", "aespa", R.drawable.img_album_exp3, "2021.05.17 싱글 댄스팝"),
            Album(2, "MAP OF THE SOUL : PERSONA", "방탄소년단", R.drawable.img_album_exp4, "2019.04.12 미니 알앤비, 힙합"),
            Album(3, "Fun to The World", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5, "2018.06.26"),
            Album(4, "Weekend", "태연", R.drawable.img_album_exp6, "2021.07.06 싱글 댄스팝"),
        )

        val albums = songDB.AlbumDao().getAlbums()
        if (albums.isNotEmpty()) return
        albumList.forEach {
            songDB.AlbumDao().insert(it)
        }
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
}
