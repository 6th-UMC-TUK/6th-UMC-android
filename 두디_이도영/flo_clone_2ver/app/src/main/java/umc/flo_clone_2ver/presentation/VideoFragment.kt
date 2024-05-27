package umc.flo_clone_2ver.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import umc.flo_clone_2ver.R
import umc.flo_clone_2ver.databinding.FragmentVideoBinding

class VideoFragment: Fragment() {
    private lateinit var binding: FragmentVideoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }
}