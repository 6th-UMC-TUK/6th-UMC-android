package umc.flo_clone_2ver.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import umc.flo_clone_2ver.R
import umc.flo_clone_2ver.databinding.FragmentHomeBannerBinding

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