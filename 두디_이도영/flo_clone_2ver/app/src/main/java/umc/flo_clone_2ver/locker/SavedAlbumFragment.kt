package umc.flo_clone_2ver.locker

import android.graphics.Color
import android.os.Bundle
import android.text.Selection.selectAll
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import umc.flo_clone_2ver.R
import umc.flo_clone_2ver.adapter.SavedAlbumRVA
import umc.flo_clone_2ver.data.Album
import umc.flo_clone_2ver.data.SongDatabase
import umc.flo_clone_2ver.databinding.BottomSheetLayoutBinding
import umc.flo_clone_2ver.databinding.FragmentSavedAlbumBinding

class SavedAlbumFragment: Fragment() {
    private lateinit var binding: FragmentSavedAlbumBinding
    private lateinit var albumList: List<Album>
    private lateinit var savedAlbumRVA: SavedAlbumRVA
    private lateinit var bottomSheetLayoutBinding: BottomSheetLayoutBinding
    private lateinit var songDB: SongDatabase
    private lateinit var bottomSheetDialog: BottomSheetDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedAlbumBinding.inflate(inflater, container, false)
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
    }

    private fun initRV() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val recyclerView = binding.fragmentDownloadedMusicRV
        recyclerView.layoutManager = layoutManager
        savedAlbumRVA = SavedAlbumRVA()
        recyclerView.adapter = savedAlbumRVA
        savedAlbumRVA.submitList(albumList)
    }

    private fun addSongs() {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val userId = spf!!.getInt("jwt", 0)
        albumList = songDB.AlbumDao().getLikedAlbums(userId)
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
}