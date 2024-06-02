package com.example.flo.locker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentLockerSavedsongBinding
import com.example.flo.song.Song
import com.example.flo.song.SongDatabase

class SavedSongFragment : Fragment() {
    lateinit var binding: FragmentLockerSavedsongBinding
    lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerSavedsongBinding.inflate(inflater, container, false)

        // songDB 초기화
        songDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }
    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        binding.lockerSavedSongRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val songRVAdapter = SavedSongRVAdapter()

        songRVAdapter.setMyItemClickListener(object : SavedSongRVAdapter.MyItemClickListener{
            override fun onRemoveItem(songId: Int) {
                songDB.songDao().updateIsLikeById(false, songId)
            }
        })

        binding.lockerSavedSongRv.adapter = songRVAdapter

        songRVAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList<Song>)
    }

}