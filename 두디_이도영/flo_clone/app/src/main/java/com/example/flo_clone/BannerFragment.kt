package com.example.flo_clone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo_clone.databinding.FragmentBannerBinding

class BannerFragment(val imgRes : Int) : Fragment() {
    // viewPager에 여러 개의 이미지를 넣을 것이므로 인자 값을 넣음 (BannerFragment()에 인자를 넣는 이유)
    // 이미지 리소스를 받을 예정

    lateinit var binding: FragmentBannerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBannerBinding.inflate(inflater, container, false)
        // 초기화 작업
        binding.bannerImageIv.setImageResource(imgRes)
        // 인지값으로 받은 이미지로 imageView의 src 값이 변경됨

        return binding.root
    }
}