package com.example.flo.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.MainActivity
import com.example.flo.R
import com.example.flo.album.Album
import com.example.flo.album.AlbumFragment
import com.example.flo.album.AlbumRVAdapter
import com.example.flo.databinding.FragmentHomeBinding
import com.example.flo.song.SongDatabase
import com.google.gson.Gson

class HomeFragment : Fragment(), AlbumRVAdapter.CommunicationInterface {

    lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>()
    private lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        inputDummyAlbums()

        songDB = SongDatabase.getInstance(requireContext())!!
        albumDatas.addAll(songDB.albumDao().getAlbums())
        Log.d("albumlist", albumDatas.toString())


        // 더미데이터 - Adapter 연결
        val albumRVAdapter = AlbumRVAdapter(albumDatas)

        // 리사이클러뷰 - Adapter 연결
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter

        // 레이아웃 매니저 설정
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener {
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }

            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }

            override fun onPlayAlbum(album: Album) {
                sendData(album)
            }
        })

        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        return binding.root
    }

    private fun inputDummyAlbums(){
        val songDB = SongDatabase.getInstance(requireActivity())!!
        val songs = songDB.albumDao().getAlbums()

        if (songs.isNotEmpty()) return

        songDB.albumDao().insert(
            Album(
                1,
                "Butter",
                "방탄소년단 (BTS)",
                R.drawable.img_album_exp
            )
        )
        songDB.albumDao().insert(
            Album(
                2,
                "LILAC",
                "아이유 (IU)",
                R.drawable.img_album_exp2
            )
        )
        songDB.albumDao().insert(
            Album(
                3,
                "Next Level",
                "에스파 (AESPA)",
                R.drawable.img_album_exp3
            )
        )
        songDB.albumDao().insert(
            Album(
                4,
                "Boy with Luv",
                "방탄소년단 (BTS)",
                R.drawable.img_album_exp4,
            )
        )
        songDB.albumDao().insert(
            Album(
                5,
                "BBOOM BBOOM",
                "모모랜드 (MOMOLAND)",
                R.drawable.img_album_exp5
            )
        )

        val songDBData = songDB.albumDao().getAlbums()
        Log.d("DB data", songDBData.toString())
    }

    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }

    
    override fun sendData(album: Album) {

        Log.d("HomeFragment", "sendData called with album: ${album.title}")
        // 안됨

        (activity as? MainActivity)?.let {
            it.updateMiniPlayerCl(album)
        }
    }


}