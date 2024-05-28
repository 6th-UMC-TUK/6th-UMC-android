package umc.mission.floclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import umc.mission.floclone.databinding.FragmentHomeBannerBinding

class HomeBannerFragment(private val title: String): Fragment() {
    private lateinit var binding: FragmentHomeBannerBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBannerBinding.inflate(inflater, container, false)
        binding.homePannelTitleTv.text = title
        return binding.root
    }
}