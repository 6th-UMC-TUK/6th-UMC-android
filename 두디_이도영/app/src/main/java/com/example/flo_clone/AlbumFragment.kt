package com.example.flo_clone

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.flo_clone.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator

class AlbumFragment : Fragment() {

    lateinit var binding: FragmentAlbumBinding

    private val tabList = arrayListOf("수록곡", "상세정보", "영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
        // activity에서 작성한 패턴과 같게 생각하면 됨

        binding.albumBackIv.setOnClickListener {
            // (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment()).commitAllowingStateLoss()
            // 옛날 방식
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment()).commitAllowingStateLoss()
            // 최근 사용 방식
        }

//        binding.songLalacLayout.setOnClickListener {
//            // 노래를 눌렀을 때 토스트 메시지가 나오도록 하는 코드
//            Toast.makeText(activity, "LILAC",Toast.LENGTH_SHORT).show()
//            // 토스트 메시지를 어디에 (activity), 무슨 메시지를("LILAC"), 얼마나(Toast.LENGTH_SHORT) 보여줄지 설정
//            // 마지막 .show()를 통해 보여줌
//        }
        initViewPager()

        return binding.root
    }
    private fun initViewPager() {
        val albumAdapter = AlbumVPAdapter(this)

        binding.albumViewpagerVp.adapter = albumAdapter
        // 어뎁터 연결
        TabLayoutMediator(binding.albumListsTb, binding.albumViewpagerVp) { tab, position ->
            tab.text = tabList[position]
        }.attach()
    }
}