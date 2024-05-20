package com.example.flo_clone

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo_clone.databinding.ActivityMainBinding
import com.example.flo_clone.databinding.FragmentHomeBinding
import com.google.gson.Gson

class HomeFragment() : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>()
    // 선언한 Album Data class를 불러옴
    private lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )

//        binding.homeAlbumImgIv1.setOnClickListener {
//            // activity에서 사용한 startActivity와는 조금 다름
//            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, AlbumFragment()).commitAllowingStateLoss()
//            // replace 안에는 어느 곳을 어떤 곳으로 바꿀지 작성
//            // 하나의 패턴처럼 이해
//            // 위의 코드로 프래그먼트를 이동함
//        }
//        val bannerAdapter = BannerVPAdapter(this)
//        // this로 초기화 함
//        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
//        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
//        binding.homeBannerVp.adapter = bannerAdapter
        // ViewPager와 Adapter 연결

//        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
//        Log.d("HomeFragment", "ViewPager2 좌우 스크롤")
        // ViewPager가 좌우로 스크롤될 수 있도록 설정

        // 데이터 리스트 생성 (더미 데이터)
        // RecyclerView에 들어갈 데이터
//        albumDatas.apply{
//            add(Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
//            add(Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
//            add(Album("Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3))
//            add(Album("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4))
//            add(Album("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5))
//            add(Album("Weekend", "태연 (Tae Yeon)", R.drawable.img_album_exp6))
//        }
        songDB = SongDatabase.getInstance(requireContext())!!
        albumDatas.addAll(songDB.albumDao().getAlbums())

        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        // 어뎁터 연결
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        // 가로로 스크롤 설정
        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(album: Album){
                // album: Album은 클릭 했을 때, 그 정보를 인자값으로 전달하기 위함
                changeAlbumFragment(album)
            }

            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeIten(position)
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

    private fun changeAlbumFragment(album: Album){
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply{
                arguments = Bundle().apply{
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
        // RecyclerView 클릭 시, AlbumFragment로 넘어가짐
    }
}