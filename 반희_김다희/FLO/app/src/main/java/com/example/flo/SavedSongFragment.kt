package com.example.flo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentLockerSavedsongBinding
import com.google.gson.Gson

class SavedSongFragment : Fragment() {
    lateinit var binding: FragmentLockerSavedsongBinding
    private var songDatas = ArrayList<Song>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerSavedsongBinding.inflate(inflater, container, false)


        //저장한곡 리스트 생성 더미 데이터
        songDatas.apply {
            add(Song(R.drawable.img_album_exp1, "Butter", "방탄소년단 (BTS)"))
            add(Song(R.drawable.img_album_exp2, "Lilac", "아이유 (IU)"))
            add(Song(R.drawable.img_album_exp3, "Next Level", "에스파 (AESPA)"))
            add(Song(R.drawable.img_album_exp4, "Boy with Luv", "방탄소년단 (BTS)"))
            add(Song(R.drawable.img_album_exp5, "BBoom BBoom", "모모랜드 (MOMOLAND)"))
            add(Song(R.drawable.img_album_exp6, "Weekend", "태연 (Tae Yeon)"))
            add(Song(R.drawable.img_album_exp7, "SPOT!", "지코 (ZICO)"))
            add(Song(R.drawable.img_album_exp8, "해야 (HEYA)", "아이브 (IVE)"))
            add(Song(R.drawable.img_album_exp9, "Magnetic", "아일릿 (ILLIT)"))
            add(Song(R.drawable.img_album_exp10, "나는 아픈 건 딱 질색이니까", "(여자)아이들"))
            add(Song(R.drawable.img_album_exp11, "첫 만남은 계획대로 되지 않아", "투어스 (TWS)"))
            add(Song(R.drawable.img_album_exp12, "To. X", "태연 (TAEYEON)"))
            add(Song(R.drawable.img_album_exp13, "Hype Boy", "뉴진스 (NewJeans)"))
            add(Song(R.drawable.img_album_exp14, "EASY", "르세라핌 (LE SSERAFIM)"))
        }

        val savedSongRVAdapter = SavedSongRVAdapter(songDatas)
        binding.lockerSavedSongRv.adapter = savedSongRVAdapter
        binding.lockerSavedSongRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        savedSongRVAdapter.setMyItemClickListener(object : SavedSongRVAdapter.MyItemClickListener {
            override fun onRemoveItem(position: Int) {
                savedSongRVAdapter.removeItem(position)
            }
        })

        return binding.root
    }



}