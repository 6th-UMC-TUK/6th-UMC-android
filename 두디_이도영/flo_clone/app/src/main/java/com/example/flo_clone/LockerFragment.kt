package com.example.flo_clone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flo_clone.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator
import androidx.fragment.app.Fragment

class LockerFragment : Fragment() {
    lateinit var binding: FragmentLockerBinding
    private val information = arrayListOf("저장한곡", "음악파일")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        initViewPager()

        return binding.root
    }

    private fun initViewPager() {
        val lockerAdapter = LockerVPAdapter(this)

        binding.lockerViewpagerVp.adapter = lockerAdapter
        TabLayoutMediator(binding.lockerSegTb, binding.lockerViewpagerVp) { tab, position ->
            tab.text = information[position]
        }.attach()
    }
}
