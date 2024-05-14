package com.example.flo_clone

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragmentlist : ArrayList<Fragment> = ArrayList()
    // 마지막 = ArrayList()로 초기화 해 줌
    override fun getItemCount(): Int = fragmentlist.size
        // 리스트의 개수
    override fun createFragment(position: Int): Fragment = fragmentlist[position]
    // 보다 간단하게 작성 가능

    fun addFragment(fragment: Fragment){
        fragmentlist.add(fragment)
        // 인자로 받은 fragment를 fragmentlist에 추가함
        notifyItemInserted(fragmentlist.size-1)
        // list안에 새로운 값이 추가되었을 때, viewPager에게 새로운 값이 추가되었음을 알려줌
        // fragmentlist.size-1 은 새로 추가된 리스트 위치를 나타냄
        // 0부터 시작하고, 개수는 1부터 시작하기 때문에 size-1로 작성
    }
}