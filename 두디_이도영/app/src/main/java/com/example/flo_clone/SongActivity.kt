package com.example.flo_clone

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_clone.databinding.ActivityMainBinding
import com.example.flo_clone.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {
    // :은 상속을 의미
    // AppCompatActivity()는 Android에서 액티비티의 기능들을 사용할 수 있도록 만들어둔 클래스, ()은 꼭 있어야 함
    lateinit var binding: ActivitySongBinding

    // 선언은 지금 하지만, 초기화는 나중에 한다는 뜻 -> 선언하면서 초기화 하는 게 아님
    lateinit var song: Song
    lateinit var timer: Timer

    private var mediaPlayer: MediaPlayer? = null
    // 비디오 플레이어 생성
    // nullable로 설정
    private var gson: Gson = Gson()
    // Gson 설정
    // song을 보다 쉽게 저장하기 위해 사용

    override fun onCreate(savedInstanceState: Bundle?) {
        // activity가 생성될 때 처음으로 실행되는 함수
        // override는 AppCompatActivity안에 있는 함수를 상속받아서 사용하기 때문에 필요
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        // binding 초기화 작업
        // inflate는 xml에 표기된 레이아웃들을 메모리에 객체화 시킴
        // 위의 형식을 패턴으로 사용한다고 인지하면 됨
        setContentView(binding.root)
        // xml에 있는 것들을 가져와서 마음대로 쓸 수 있도록 함
        // ()안에는 사용할 xml의 id 입력
        // root는 xml안에서 최상단 뷰, 즉 <androidx.constraintlayout.widget.ConstraintLayout>를 가리킴

        initSing()
        setPlayer(song)

        binding.songDownIb.setOnClickListener {
            finish()
            // finish()을 사용하면, mainActivity위에 실행된 songActivity가 종료됨
            // -> 다시 mainActivity가 보여짐
        }
        binding.songMiniplayerIv.setOnClickListener {
            // invisible과 gone의 차이점은, invisible은 공간을 차지하지만 보이지 않고,
            // gone은 공간을 차지하지 않으면서 보이지 않음
            setPlayoerStatus(true)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayoerStatus(false)
        }
    }

    private fun initSing() {
        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 60),
                intent.getBooleanExtra("isPlaying", false),
                intent.getStringExtra("music")!!,
            )
        }
        startTimer()
    }

    private fun setPlayer(song: Song) {
        binding.songMusicTitleTv.text = intent.getStringExtra("title")!!
        binding.songSingerNameTv.text = intent.getStringExtra("singer")!!
        binding.songStartTimeTv.text =
            String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text =
            String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        // 찾고자 하는 리소스 이름, 폴더, 패키지 이름을 넣어줌
        // 리소스를 반환 받음
        mediaPlayer = MediaPlayer.create(this, music)
        // 미디어 플레이어에 음악을 넘겨줌

        setPlayoerStatus(song.isPlaying)
    }

    fun setPlayoerStatus(isPlaying: Boolean) {
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying
        // ()안에는 현재 플레이어의 상태를 판단할 수 있도록 Boolean 형식 사용
        if (isPlaying) {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE

            mediaPlayer?.start()
            // mediaPlayer 재생
        } else {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            // 정지버튼이 보이도록 바꿈

            if(mediaPlayer?.isPlaying == true) {
                // if문은 재생 중이 아닐 때 pause를 누르면 오류가 발생할 수 있기 때문에 조건문 추가
                mediaPlayer?.pause()
            }
        }
    }

    private fun startTimer() {
        timer = Timer(song.playTime, song.isPlaying)
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
                Log.d("Song", "쓰레드 사망 ${e.message}")
            }

        }
    }

    override fun onPause(){
        // 사용자가 포커스를 잃었을 때, 음악을 중지하기 위함
        super.onPause()
        setPlayoerStatus(false)

        song.second = ((binding.songProgressSb.progress * song.playTime)/100)/1000
        // 음악이 몇 초까지 재생되었는지 반영
        // 단위가 밀리세컨드이기 때문에 1000을 나눔
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        // 재생하던 음악이 남아있으려면 어딘가에 저장해줘야함
        // 내부 저장소에 저장함
        val editor = sharedPreferences.edit()
        // gson을 사용하여 song객체를 sharedPreferences에 보다 쉽게 저장 가능
        val songJson = gson.toJson(song)
        // song객체를 Json 포멧으로 변환 시켜줌
        editor.putString("songData", songJson)
        // 변환된 포맷을 저장

        editor.apply()
        // 실제 저장을 하기위해 필요함, git의 push 같은 역할
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()

        mediaPlayer?.release()
        // mediaPlayer가 갖고있던 리소스 해제
        mediaPlayer = null
        // mediaPlayer 해제
    }
}