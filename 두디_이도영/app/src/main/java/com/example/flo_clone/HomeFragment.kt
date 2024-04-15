package com.example.flo_clone

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.databinding.ViewDataBinding
import com.example.flo_clone.databinding.ActivityMainBinding
import com.example.flo_clone.databinding.FragmentHomeBinding

class HomeFragment() : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )

        binding.homeAlbumImgIv1.setOnClickListener {
            // activity에서 사용한 startActivity와는 조금 다름
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, AlbumFragment()).commitAllowingStateLoss()
            // replace 안에는 어느 곳을 어떤 곳으로 바꿀지 작성
            // 하나의 패턴처럼 이해
            // 위의 코드로 프래그먼트를 이동함
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}