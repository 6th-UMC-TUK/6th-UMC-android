package umc.flo_clone_2ver.locker

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import umc.flo_clone_2ver.R
import umc.flo_clone_2ver.adapter.NewMusicDailyAdapter
import umc.flo_clone_2ver.adapter.NewMusicDailyAdapter.Companion.DOWNLOADED_MUSIC
import umc.flo_clone_2ver.data.Song
import umc.flo_clone_2ver.data.SongDatabase
import umc.flo_clone_2ver.databinding.BottomSheetLayoutBinding
import umc.flo_clone_2ver.databinding.FragmentSavedSongBinding

class SavedSongFragment : Fragment(), NewMusicDailyAdapter.ItemClickListener {
    private lateinit var binding: FragmentSavedSongBinding
    private lateinit var songList: List<Song>
    private lateinit var newMusicDailyAdapter: NewMusicDailyAdapter
    private lateinit var bottomSheetLayoutBinding: BottomSheetLayoutBinding
    private lateinit var songDB: SongDatabase
    private lateinit var bottomSheetDialog: BottomSheetDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedSongBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        songDB = SongDatabase.getInstance(requireContext())!!
        initBottomSheetDialog()
        addSongs()
        initRV()
        binding.fragmentDownloadedMusicSelectAllTv.setOnClickListener {
            selectAll(true)
        }
        deleteIsLikeSongs()
    }

    override fun onClick(song: Song, viewType: Int) {
    }

    override fun onRemove(position: Int) {
        val currentList = newMusicDailyAdapter.currentList.toMutableList()
        currentList.removeAt(position)
        Log.d("position", "$position")
        newMusicDailyAdapter.submitList(currentList)

    }

    private fun addSongs() {
        songList = songDB.songDao().getLikedSongs(isLike = true)
    }

    private fun initRV() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val recyclerView = binding.fragmentDownloadedMusicRV
        recyclerView.layoutManager = layoutManager
        newMusicDailyAdapter = NewMusicDailyAdapter(DOWNLOADED_MUSIC, this)
        recyclerView.adapter = newMusicDailyAdapter
        newMusicDailyAdapter.submitList(songList)
    }

    private fun initBottomSheetDialog() {
        bottomSheetLayoutBinding = BottomSheetLayoutBinding.inflate(layoutInflater)
        bottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.TransparentBottomSheetDialogTheme)
        bottomSheetDialog.setContentView(bottomSheetLayoutBinding.root)
        bottomSheetDialog.setCanceledOnTouchOutside(true)
        bottomSheetDialog.setOnCancelListener {
            selectAll(false)
        }
    }

    private fun selectAll(toggle: Boolean) {
        if (toggle) {
            binding.fragmentDownloadedMusicSelectAllTv.text = "선택해제"
            binding.fragmentDownloadedMusicSelectAllTv.setTextColor(
                resources.getColor(R.color.select_color)
            )
            binding.fragmentDownloadedMusicSelectAllBtn.setImageResource(
                R.drawable.btn_playlist_select_on
            )
            bottomSheetDialog.show()
        } else {
            binding.fragmentDownloadedMusicSelectAllTv.text = "전체선택"
            binding.fragmentDownloadedMusicSelectAllTv.setTextColor(Color.BLACK)
            binding.fragmentDownloadedMusicSelectAllBtn.setImageResource(R.drawable.btn_playlist_select_off)
        }
    }

    private fun deleteIsLikeSongs() {
        bottomSheetLayoutBinding.bottomSheetDeleteBtn.setOnClickListener {
            if (songList.isNotEmpty()) {
                songList.forEach {
                    songDB.songDao().updateIsLikeById(false, it.id)
                }
                addSongs()
                newMusicDailyAdapter.submitList(songList)
            }
            bottomSheetDialog.dismiss()
            selectAll(false)
        }
    }
}