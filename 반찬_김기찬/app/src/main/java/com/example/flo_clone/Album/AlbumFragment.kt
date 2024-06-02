package com.example.flo_clone.Album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo_clone.Function.CustomSnackbar
import com.example.flo_clone.Home.HomeFragment
import com.example.flo_clone.MainActivity
import com.example.flo_clone.R
import com.example.flo_clone.Song.Song
import com.example.flo_clone.Song.SongDatabase
import com.example.flo_clone.databinding.FragmentAlbumBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment:Fragment() {
    lateinit var binding : FragmentAlbumBinding
    private val tabList = arrayListOf("수록곡", "상세정보", "영상")
    private var gson: Gson = Gson()
    private var isLiked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        // Home 에서 넘어온 데이터 받아오기
        val albumJson = arguments?.getString("album")
        val album = gson.fromJson(albumJson, Album::class.java)

        // Home 에서 넘어온 데이터를 반영
        isLiked = isLikedAlbum(album.id)
        setInit(album)
        setOnClickListeners(album)

        initViewPager()



        return binding.root
    }

    private fun setInit(album: Album?) {
        binding.albumImgIv.setImageResource(album?.coverImg!!)
        binding.albumSongTitle.text = album.title.toString()
        binding.albumSingerNameTv.text = album.singer.toString()
        if (isLiked) {
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun getJwt(): Int {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt", 0)
    }

    private fun likeAlbum(userId: Int, albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId, albumId)

        songDB.albumDao().likeAlbum(like)
    }

    private fun isLikedAlbum(albumId: Int): Boolean {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        val likedId: Int? = songDB.albumDao().isLikedAlbum(userId, albumId)

        return likedId != null
    }

    private fun disLikedAlbum(albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        songDB.albumDao().disLikedAlbum(userId, albumId)
    }

    private fun setOnClickListeners(album: Album) {
        val userId = getJwt()

        binding.albumLikeIv.setOnClickListener {
            if (isLiked) {
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
                disLikedAlbum(album.id)
                CustomSnackbar.make(binding.root, "좋아요 한 앨범이 취소되었습니다.").show()
            } else {
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId, album.id)
                CustomSnackbar.make(binding.root, "좋아요 한 앨범에 담겼습니다.").show()
            }
        }
        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }
    }

    private fun initViewPager() {
        val albumAdapter = AlbumVPAdapter(this)

        binding.albumViewpagerVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumListsTb, binding.albumViewpagerVp) { tab, position ->
            tab.text = tabList[position]
        }.attach()
    }
}