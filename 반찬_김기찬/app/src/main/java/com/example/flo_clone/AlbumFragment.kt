package com.example.flo_clone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo_clone.databinding.FragmentAlbumBinding
import com.example.flo_clone.databinding.FragmentSongBinding
import com.google.android.material.tabs.TabLayoutMediator

class AlbumFragment:Fragment() {
    lateinit var binding : FragmentAlbumBinding
    private val tabList = arrayListOf("수록곡", "상세정보", "영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        initViewPager()

        return binding.root
    }

    private fun initViewPager() {
        val albumAdapter = AlbumVPAdapter(this)

        binding.albumViewpagerVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumListsTb, binding.albumViewpagerVp) { tab, position ->
            tab.text = tabList[position]
        }.attach()
    }
}