package umc.mission.floclone

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.google.gson.Gson
import kotlinx.coroutines.*
import umc.mission.floclone.data.*
import umc.mission.floclone.databinding.ActivitySongBinding
import umc.mission.floclone.databinding.ToastLikeBinding
import java.text.SimpleDateFormat
import java.util.function.BinaryOperator
import kotlin.math.pow
import kotlin.math.roundToInt

class SongActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySongBinding
    private lateinit var song: Song
    private lateinit var songDB: SongDatabase
    private var mediaPlayer: MediaPlayer? = null
    private var repeatBtnState = false
    private var timeFormat = SimpleDateFormat("mm:ss")
    private val songs = arrayListOf<Song>()
    private var newPos = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        songDB = SongDatabase.getInstance(this)!!
        initSong()
        initClickListener()
        updateView()
    }

    private fun initSong() {
        val sharedPreferences = getSharedPreferences(SONG, MODE_PRIVATE)
        val songId = sharedPreferences.getInt(SONG_ID, 0)
        song = songDB.songDao().getSong(songId)
        Log.d("songSong", song.toString())

        initPlayList()
        newPos = getPlayingSongPosition(songId)
        songs[newPos].second = intent.getIntExtra(SECOND, 0)
        songs[newPos].isPlaying = intent.getBooleanExtra(IS_PLAYING, false)
        setPlayer(songs[newPos])
    }

    private fun initPlayList() {
        songs.addAll(songDB.songDao().getSongsInAlbum(song.albumIdx))
        Log.d("songs", songs.toString())
    }

    private fun getPlayingSongPosition(songId: Int): Int {
        for (i in 0 until songs.size) {
            if (songs[i].id == songId)
                return i
        }
        return 0
    }

    private fun setPlayer(song: Song) {
        binding.activitySongMusicTitleTv.text = song.title
        binding.activitySongSingerTv.text = song.singer
        binding.activitySongMusicLyricsTv.text = song.lyrics
        binding.activitySongAlbumImgIv.setImageResource(song.coverImg ?: 0)
        binding.activitySongLikeBtn.setImageResource(if (song.isLike) R.drawable.ic_my_like_on else R.drawable.ic_my_like_off)
        val musicFile = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, musicFile).apply {
            isLooping = repeatBtnState
        }

        binding.activitySongMusicPlayingTimeEndTv.text =
            timeFormat.format(mediaPlayer?.duration)
        binding.activitySongMusicPlayingTimeStartTv.text =
            timeFormat.format(song.second)

        binding.activitySongMusicSeekbar.max = mediaPlayer?.duration!!
        binding.activitySongMusicSeekbar.progress = song.second
        mediaPlayer?.seekTo(song.second)
        Log.d("duration", "${mediaPlayer?.duration}")
        setPlayerStatus(song.isPlaying)
    }

    private fun updateView() {
        var randomBtnState = false
        binding.activitySongPlayerRepeatBtn.setOnClickListener {
            repeatBtnState = !repeatBtnState
            binding.activitySongPlayerRepeatBtn.setImageResource(
                if (repeatBtnState) R.drawable.selected_repeat else R.drawable.nugu_btn_repeat_inactive
            )
        }
        binding.activitySongPlayerRandomBtn.setOnClickListener {
            randomBtnState = !randomBtnState
            binding.activitySongPlayerRandomBtn.setImageResource(
                if (randomBtnState) R.drawable.selected_random else R.drawable.nugu_btn_random_inactive
            )
        }

        binding.activitySongLikeBtn.setOnClickListener {
            setLike(songs[newPos].isLike)
            val toastLikeBinding = ToastLikeBinding.inflate(layoutInflater)
            toastLikeBinding.run {
                toastLikeBinding.toastTv.text = if (songs[newPos].isLike)
                    "                           좋아요 한 곡이 담겼습니다.                           "
                else "                           좋아요 한 곡이 취소되었습니다.                           "
                val toast = Toast(this@SongActivity)
                toast.view = toastLikeBinding.root
                toast.duration = Toast.LENGTH_SHORT
                toast.setGravity(Gravity.BOTTOM, 0, binding.activitySongPlayerPlayBtn.height.times(2)+100.toPx())//binding.activitySongPlayerPlayBtn.layoutParams.height+50.toPx())
                toast.show()
            }
        }
    }

    private fun Int.toPx(): Int = (this/Resources.getSystem().displayMetrics.density).toInt()

    private fun setLike(isLike: Boolean){
        songs[newPos].isLike = !isLike
        binding.activitySongLikeBtn.setImageResource(if (songs[newPos].isLike) R.drawable.ic_my_like_on else R.drawable.ic_my_like_off)
        songDB.songDao().updateIsLikeById(songs[newPos].isLike, songs[newPos].id)
    }


    private fun initClickListener() {
        binding.activitySongPlayerPlayBtn.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.activitySongPlayerPauseBtn.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.activitySongDownBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra(MUSIC_TITLE, songs[newPos].title)
                putExtra(MUSIC_SINGER, songs[newPos].singer)
                putExtra(CURRENT_POSITION, mediaPlayer?.currentPosition)
                putExtra(IS_PLAYING, mediaPlayer?.isPlaying)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        binding.activitySongPlayerNextBtn.setOnClickListener {
            moveSong(+1)
        }

        binding.activitySongPlayerPreviousBtn.setOnClickListener {
            moveSong(-1)
        }
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
        setPlayer(songs[newPos])
    }

    override fun onPause() {
        super.onPause()
        songs[newPos].second = mediaPlayer?.currentPosition!!
        setPlayerStatus(false)
        val sharedPreferences = getSharedPreferences(SONG, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(SONG_ID, songs[newPos].id).apply()
        Log.d("pasue", songs[newPos].toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun setPlayerStatus(isPlaying: Boolean) {
        songs[newPos].isPlaying = isPlaying
        if (isPlaying) {
            binding.activitySongPlayerPlayBtn.visibility = View.GONE
            binding.activitySongPlayerPauseBtn.visibility = View.VISIBLE
            mediaPlayer?.start()
        } else {
            binding.activitySongPlayerPlayBtn.visibility = View.VISIBLE
            binding.activitySongPlayerPauseBtn.visibility = View.GONE
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        }
        updateSeekBar()
    }

    private fun updateSeekBar() {
        CoroutineScope(Dispatchers.Main).launch {
            while (mediaPlayer?.isPlaying!!) {
                delay(50)
                binding.activitySongMusicSeekbar.progress = mediaPlayer?.currentPosition!!
                binding.activitySongMusicPlayingTimeStartTv.text =
                    timeFormat.format(mediaPlayer?.currentPosition)
            }
        }
    }
}