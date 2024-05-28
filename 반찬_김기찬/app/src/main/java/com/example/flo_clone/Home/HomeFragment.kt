package com.example.flo_clone.Home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo_clone.Album.Album
import com.example.flo_clone.Album.AlbumFragment
import com.example.flo_clone.Album.AlbumRVAdapter
import com.example.flo_clone.MainActivity
import com.example.flo_clone.R
import com.example.flo_clone.Song.Song
import com.example.flo_clone.Song.SongDatabase
import com.example.flo_clone.databinding.FragmentHomeBinding
import com.google.gson.Gson

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>()
    private lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

//        binding.homeTodayAlbumCoverImg01Iv.setOnClickListener {
//            (context as MainActivity).supportFragmentManager.beginTransaction()
//                .replace(R.id.main_frm, AlbumFragment())
//                .commitAllowingStateLoss()
//        }
        inputDummyAlbums()

        songDB = SongDatabase.getInstance(requireContext())!!
        albumDatas.addAll(songDB.albumDao().getAlbums()) // songDB에서 album list를 가져옵니다.
        Log.d("albumlist", albumDatas.toString())

//        albumDatas.apply {
//            add(Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
//            add(Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
//            add(Album("Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3))
//            add(Album("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4))
//            add(Album("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5))
//            add(Album("Weekend", "태연 (Tae Yeon)", R.drawable.img_album_exp6))
//        }

        // 더미데이터랑 Adapter 연결
        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        // 리사이클러뷰에 어댑터를 연결
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        // 레이아웃 매니저 설정
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener {
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }
        })

        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val panelAdapter = PanelVPAdapter(this)
        panelAdapter.addFragment(PanelFragment(R.drawable.img_first_album_default))
        panelAdapter.addFragment(PanelFragment(R.drawable.img_first_album_default))
        panelAdapter.addFragment(PanelFragment(R.drawable.img_first_album_default))
        binding.homePannelVp.adapter = panelAdapter
        binding.homePannelVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.indicator.setViewPager(binding.homePannelVp)

        return binding.root
    }

    private fun inputDummyAlbums(){
        val songDB = SongDatabase.getInstance(requireActivity())!!
        val songs = songDB.albumDao().getAlbums()

        if (songs.isNotEmpty()) return

        songDB.albumDao().insert(
            Album(
                1,
                "IU 5th Album 'LILAC'",
                "아이유 (IU)",
                R.drawable.img_album_exp2
            )
        )

        songDB.albumDao().insert(
            Album(
                2,
                "Butter",
                "방탄소년단 (BTS)",
//                listOf(  // 앨범의 수록곡 추가할 때 사용
//                    Song()
//                ),
                R.drawable.img_album_exp
            )
        )

        songDB.albumDao().insert(
            Album(
                3,
                "iScreaM Vol.10: Next Level Remixes",
                "에스파 (AESPA)",
                R.drawable.img_album_exp3
            )
        )

        songDB.albumDao().insert(
            Album(
                4,
                "Map of the Soul Persona",
                "방탄소년단 (BTS)",
                R.drawable.img_album_exp4,
            )
        )


        songDB.albumDao().insert(
            Album(
                5,
                "Great!",
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

    // 뷰페이저 자동으로 넘어가게 하는 코드
    val handler = Handler(Looper.getMainLooper()){ //handler
        setPage()
        true
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val thread = Thread(PagerRunnable())
        thread.start() //자동 넘어가게 실행
    }

    inner class PagerRunnable:Runnable{ //runable
        override fun run() {
            while(true){
                try {
                    Thread.sleep(3000)
                    handler.sendEmptyMessage(0)
                } catch (e : InterruptedException){
                    Log.d("interupt", "interupt발생")
                }
            }
        }
    }
    private fun setPage(){
        binding.homePannelVp.setCurrentItem(binding.homePannelVp.currentItem, true)
        if(binding.homePannelVp.currentItem == 2){
            binding.homePannelVp.currentItem = 0
        }else{
            binding.homePannelVp.currentItem+=1
        }
        binding.homeBannerVp.setCurrentItem(binding.homeBannerVp.currentItem, true)
        if(binding.homeBannerVp.currentItem == 1){
            binding.homeBannerVp.currentItem = 0
        }else{
            binding.homeBannerVp.currentItem+=1
        }
    }

}