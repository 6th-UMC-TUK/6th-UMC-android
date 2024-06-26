package umc.flo_clone_2ver.locker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import umc.flo_clone_2ver.R
import umc.flo_clone_2ver.databinding.FragmentMusicFileBinding

class MusicFileFragment: Fragment() {
    private lateinit var binding: FragmentMusicFileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMusicFileBinding.inflate(inflater, container, false)
        return binding.root
    }
}