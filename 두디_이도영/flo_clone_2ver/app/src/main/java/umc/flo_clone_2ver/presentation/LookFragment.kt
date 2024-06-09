package umc.flo_clone_2ver.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import umc.flo_clone_2ver.R
import umc.flo_clone_2ver.adapter.HomeViewPagerAdapter
import umc.flo_clone_2ver.adapter.NewMusicDailyAdapter
import umc.flo_clone_2ver.adapter.SongRVAdapter
import umc.flo_clone_2ver.data.Song
import umc.flo_clone_2ver.databinding.FragmentLookBinding
import umc.flo_clone_2ver.retrofit.FloChartResult
import umc.flo_clone_2ver.retrofit.LookView
import umc.flo_clone_2ver.retrofit.SongService

class LookFragment : Fragment(), LookView {
    private var binding: FragmentLookBinding? = null
    private lateinit var floChartAdapter: SongRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLookBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onStart() {
        super.onStart()
        getSongs()
    }

    fun initRecyclerView(result: FloChartResult){
        floChartAdapter = SongRVAdapter(requireContext(), result)

        binding?.lookFloChartRv?.adapter = floChartAdapter
    }

    private fun getSongs(){
        val songService = SongService()
        songService.setLookView(this)

        songService.getSongs()
    }

    override fun onGetSongLoading() {
        binding?.lookLoadingPb?.visibility = View.VISIBLE
    }

    override fun onGetSongSuccess(code: Int, result: FloChartResult) {
        binding?.lookLoadingPb?.visibility = View.GONE
        initRecyclerView(result)
    }

    override fun onGetSongFailure(code: Int, message: String) {
        binding?.lookLoadingPb?.visibility = View.GONE
        Log.d("LOOK-FRAG/SONG-RESPONSE", message)
    }
}