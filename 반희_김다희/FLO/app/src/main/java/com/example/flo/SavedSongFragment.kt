package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.SavedSong
import com.example.flo.databinding.FragmentLockerSavedsongBinding
import com.google.gson.Gson

class SavedSongFragment : Fragment() {
    lateinit var binding: FragmentLockerSavedsongBinding
    lateinit var savedSong: SavedSong
    private var savedSongDatas = ArrayList<SavedSong>()
    private val gson: Gson = Gson()

//    private val adapter by lazy {
//        SavedSongRVAdapter(savedSongDatas)
//    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerSavedsongBinding.inflate(inflater, container, false)

        val savedSongJson = arguments?.getString("savedSong")
        val savedSong = gson.fromJson(savedSongJson, SavedSong::class.java)
        setInit(savedSong)

        //저장한곡 리스트 생성 더미 데이터
        savedSongDatas.apply {
            add(SavedSong(R.drawable.img_album_exp1, ""))
        }

        // 더미데이터랑 Adapter 연결
        val savedSongRVAdapter = SavedSongRVAdapter(savedSongDatas)

        // 리사이클러뷰에 Adapter 연결
        binding.lockerSavedSongRv.adapter = savedSongRVAdapter

        // 레이아웃 매니저 연결
        binding.lockerSavedSongRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        setInit(savedSong)
        return binding.root
    }



    private fun setInit(savedSong: SavedSong){


    }
}