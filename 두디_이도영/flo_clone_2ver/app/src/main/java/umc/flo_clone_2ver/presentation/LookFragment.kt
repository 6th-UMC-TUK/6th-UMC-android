package umc.flo_clone_2ver.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import umc.flo_clone_2ver.R
import umc.flo_clone_2ver.adapter.HomeViewPagerAdapter
import umc.flo_clone_2ver.adapter.NewMusicDailyAdapter
import umc.flo_clone_2ver.data.Song
import umc.flo_clone_2ver.databinding.FragmentLookBinding

class LookFragment : Fragment() {
    private var binding: FragmentLookBinding? = null
    private lateinit var floChartList: MutableList<Song>
    private lateinit var floChartAdapter: NewMusicDailyAdapter
    private lateinit var floChartViewPagerAdapter: HomeViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLookBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}