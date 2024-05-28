package com.example.flo_clone.Locker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.flo_clone.Function.BottomSheetFragment
import com.example.flo_clone.MainActivity
import com.example.flo_clone.R
import com.example.flo_clone.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator

class LockerFragment : Fragment() {
    lateinit var binding: FragmentLockerBinding
    private val tabList = arrayListOf("저장한 곡", "음악파일")
    private var isSelected = false
    private var bottomSheetFragment: BottomSheetFragment? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        initViewPager()

//        val bottomSheetFragment = BottomSheetFragment()
//        binding.lockerSelectAllLl.setOnClickListener {
//            isSelected = !isSelected
//            changeSelectAllText()
//
//            bottomSheetFragment.show(parentFragmentManager, "BottomSheetDialog")
//            (activity as? MainActivity)?.hideMiniPlayer()
//
//            bottomSheetFragment.dialog?.setOnDismissListener {
//                (activity as? MainActivity)?.showMiniPlayer()
//                isSelected = false
//                changeSelectAllText()
//            }
//        }
        binding.lockerSelectAllLl.setOnClickListener {
            toggleBottomSheet()
        }

        return binding.root
    }

    private fun toggleBottomSheet() {
        if (bottomSheetFragment == null) {
            bottomSheetFragment = BottomSheetFragment().apply {
                setOnDismissListener {
                    (activity as? MainActivity)?.showMiniPlayer()
                    isSelected = false
                    changeSelectAllText()
                }
            }
        }

        if (isSelected) {
            bottomSheetFragment?.dismiss()
        } else {
            bottomSheetFragment?.show(parentFragmentManager, "BottomSheetDialog")
            (activity as? MainActivity)?.hideMiniPlayer()
        }

        isSelected = !isSelected
        changeSelectAllText()
    }
    private fun changeSelectAllText() {
        if (isSelected) {
            binding.lockerSelectAllTv.text = "선택해제"
            binding.lockerSelectAllTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.select_color))
        } else {
            binding.lockerSelectAllTv.text = "전체선택"
            binding.lockerSelectAllTv.setTextColor(ContextCompat.getColor(requireContext(), R.color.default_color))
        }
    }

    private fun initViewPager() {
        val lockerAdapter = LockerVPAdapter(this)

        binding.lockerViewpagerVp.adapter = lockerAdapter
        TabLayoutMediator(binding.lockerSegTb, binding.lockerViewpagerVp) { tab, position ->
            tab.text = tabList[position]
        }.attach()
    }
}