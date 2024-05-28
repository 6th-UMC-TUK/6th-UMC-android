package umc.mission.floclone.locker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import umc.mission.floclone.adapter.ViewpagerFragmentAdapter
import umc.mission.floclone.adapter.ViewpagerFragmentAdapter.Companion.LOCKER
import umc.mission.floclone.databinding.FragmentLockerBinding

class LockerFragment : Fragment() {
    private lateinit var binding: FragmentLockerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewPager2()
    }
    private fun initViewPager2(){
        val fragmentStateAdapter = ViewpagerFragmentAdapter(this, LOCKER)
        binding.fragmentLockerViewpager2.adapter = fragmentStateAdapter
        TabLayoutMediator(binding.fragmentLockerTablayout, binding.fragmentLockerViewpager2) { tab, position ->
            binding.fragmentLockerViewpager2.currentItem = tab.position
            tab.text = when(position){
                0 -> "저장한 곡"
                else -> "음악파일"
            }
        }.attach()
    }
}