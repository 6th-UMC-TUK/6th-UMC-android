package com.example.flo_clone.Album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo_clone.Home.HomeFragment
import com.example.flo_clone.MainActivity
import com.example.flo_clone.R
import com.example.flo_clone.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment:Fragment() {
    lateinit var binding : FragmentAlbumBinding
    private val tabList = arrayListOf("수록곡", "상세정보", "영상")
    private var gson: Gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        val albumJson = arguments?.getString("album")
        val album = gson.fromJson(albumJson, Album::class.java)
        setInit(album)

        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        initViewPager()

        return binding.root
    }

    private fun setInit(album: Album?) {
        binding.albumImgIv.setImageResource(album?.coverImg!!)
        binding.albumSongTitle.text = album.title.toString()
        binding.albumSingerNameTv.text = album.singer.toString()


    }

    private fun initViewPager() {
        val albumAdapter = AlbumVPAdapter(this)

        binding.albumViewpagerVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumListsTb, binding.albumViewpagerVp) { tab, position ->
            tab.text = tabList[position]
        }.attach()
    }
}