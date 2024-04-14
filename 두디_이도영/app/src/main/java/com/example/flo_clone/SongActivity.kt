package com.example.flo_clone

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_clone.databinding.ActivityMainBinding
import com.example.flo_clone.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity(){
    // :은 상속을 의미
    // AppCompatActivity()는 Android에서 액티비티의 기능들을 사용할 수 있도록 만들어둔 클래스, ()은 꼭 있어야 함
    lateinit var binding : ActivitySongBinding
    // 선언은 지금 하지만, 초기화는 나중에 한다는 뜻 -> 선언하면서 초기화 하는 게 아님

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

        binding.songDownIb.setOnClickListener {
            finish()
            // finish()을 사용하면, mainActivity위에 실행된 songActivity가 종료됨
            // -> 다시 mainActivity가 보여짐
        }
        binding.songMiniplayerIv.setOnClickListener {
            // invisible과 gone의 차이점은, invisible은 공간을 차지하지만 보이지 않고,
            // gone은 공간을 차지하지 않으면서 보이지 않음
            setPlayoerStatus(false)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayoerStatus(true)
        }
    }

    fun setPlayoerStatus(isPlaying : Boolean){
        // ()안에는 현재 플레이어의 상태를 판단할 수 있도록 Boolean 형식 사용
        if(isPlaying){
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        } else{
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            // 정지버튼이 보이도록 바꿈
        }
    }
}