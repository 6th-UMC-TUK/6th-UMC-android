package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentLockerSavedsongBinding

class SavedSongFragment : Fragment() {
    lateinit var binding: FragmentLockerSavedsongBinding
    lateinit var savedSong: SavedSong
    private var savedSongDatas = ArrayList<SavedSong>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerSavedsongBinding.inflate(inflater, container, false)


        //저장한곡 리스트 생성 더미 데이터
        savedSongDatas.apply {
            add(SavedSong())
        }

        // 더미데이터랑 Adapter 연결
        val savedSongRVAdapter = SavedSongRVAdapter(savedSongDatas)

        // 리사이클러뷰에 Adapter 연결
        binding.lockerSavedSongRv.adapter = savedSongRVAdapter

        initSavedSong()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    // 레이아웃 매니저 연결
    private fun initRecyclerview() {
        binding.lockerSavedSongRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun initSavedSong(){

    }
}