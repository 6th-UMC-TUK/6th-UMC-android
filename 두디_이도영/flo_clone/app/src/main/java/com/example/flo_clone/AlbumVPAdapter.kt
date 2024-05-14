package com.example.flo_clone

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumVPAdapter(fragment:Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3
    // 정해진 프래그먼트만 보여줄 예정이기 때문에 개수를 정해줌

    override fun createFragment(position: Int): Fragment {
        // 정해진 프래그먼트만 보여줄 것이기 때문에 프래그먼트를 만들고 이를 각각 연결해 줄 것임
        Log.d("AlbumVPAdapter", "VPAdapter 실행")
        return when (position) {
            // switch 문과 비슷함
            // when은 조건에 따라 다르게 동작함
            // position에 따라 다른 프래그먼트를 보여줄 것임
            0 -> SongFragment()
            // 수록곡 프래그먼트
            1 -> DetailFragment()
            // 상세 정보 프래그먼트
            else -> VideoFragment()
            // 영상 프래그먼트
        }
    }
}