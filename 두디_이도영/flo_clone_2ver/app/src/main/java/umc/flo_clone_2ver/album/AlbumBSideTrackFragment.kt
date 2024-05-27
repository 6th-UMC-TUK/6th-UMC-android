package umc.flo_clone_2ver.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import umc.flo_clone_2ver.R
import umc.flo_clone_2ver.adapter.NewMusicDailyAdapter
import umc.flo_clone_2ver.adapter.NewMusicDailyAdapter.Companion.B_SIDE_TRACK
import umc.flo_clone_2ver.data.Album
import umc.flo_clone_2ver.data.SONG_ALBUM_INDEX
import umc.flo_clone_2ver.data.Song
import umc.flo_clone_2ver.data.SongDatabase
import umc.flo_clone_2ver.databinding.FragmentAlbumBSideTrackBinding

class AlbumBSideTrackFragment: Fragment() {
    private lateinit var binding: FragmentAlbumBSideTrackBinding
    private lateinit var album: Album
    private var toggle = false
    private val songs = arrayListOf<Song>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBSideTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val albumIdx = arguments?.getInt(SONG_ALBUM_INDEX, 0)
        val songDB = SongDatabase.getInstance(requireContext())!!
        songs.addAll(songDB.songDao().getSongsInAlbum(albumIdx!!))
        initRecyclerView()
        updateView()
    }

    private fun initRecyclerView(){
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val recyclerView = binding.albumBSideTrackRecyclerview
        recyclerView.layoutManager = layoutManager
        binding.albumBSideTrackRecyclerview.adapter = NewMusicDailyAdapter(B_SIDE_TRACK).apply {
            submitList(songs)
        }

    }

    private fun updateView(){
        binding.albumBSideTrackMixBtn.setOnClickListener {
            toggle = !toggle
            binding.albumBSideTrackMixBtn.setImageResource(
                if (toggle) R.drawable.btn_toggle_on else R.drawable.btn_toggle_off
            )
        }
    }
}