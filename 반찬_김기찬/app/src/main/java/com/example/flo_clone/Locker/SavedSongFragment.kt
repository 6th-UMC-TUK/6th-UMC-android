package com.example.flo_clone.Locker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo_clone.R
import com.example.flo_clone.Song.Song
import com.example.flo_clone.databinding.FragmentLockerSavedsongBinding

class SavedSongFragment : Fragment() {
    lateinit var binding: FragmentLockerSavedsongBinding
    private var songDatas = ArrayList<Song>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerSavedsongBinding.inflate(inflater, container, false)

        songDatas.apply {
            add(Song("Supernova", "aespa", R.drawable.supernova))
            add(Song("She's American", "The 1975", R.drawable.the1975))
            add(Song("Impssible", "RIIZE", R.drawable.impossible))
            add(Song("HOMESWEETHOME", "백예린(Yerin Baek)", R.drawable.tellusboutyourself))
            add(Song("Amnesia", "WOODZ", R.drawable.amnesia))
            add(Song("Super Shy", "New Jeans", R.drawable.getup))
            add(Song("주인공 (Irreplaceable)", "NCT DREAM", R.drawable.hellofutrue))
            add(Song("Chill Kill", "레드벨벳 (Red Velvet)", R.drawable.chillkill))
            add(Song("검정색하트 (feat. Leellamarz, BE'O)", "TOIL", R.drawable.blackheart))
            add(Song("Drama", "aespa", R.drawable.drama))
            add(Song("神のまにまに", "ワンダーランズ×ショウタイム", R.drawable.kamino))
        }

        val savedSongRVAdapter = SavedSongRVAdapter(songDatas)
        binding.lockerSavedsongRv.adapter = savedSongRVAdapter
        binding.lockerSavedsongRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        savedSongRVAdapter.setMyItemClickListener(object : SavedSongRVAdapter.DeleteItemClickListener {
            override fun onRemoveItem(position: Int) {
                savedSongRVAdapter.removeItem(position)
            }
        })

        return binding.root
    }
}