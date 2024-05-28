package com.example.flo_clone

import android.media.tv.TvContract.Programs.Genres.MUSIC
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.flo_clone.databinding.ActivityMainBinding
import com.example.flo_clone.databinding.FragmentHomeBinding
import com.google.gson.Gson

class HomeFragment() : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>()

    // 선언한 Album Data class를 불러옴
    private lateinit var songDB: SongDatabase
    private lateinit var songList: MutableList<Song>
    private lateinit var podcastList: MutableList<Song>
    private lateinit var videoCollectionList: MutableList<Song>
    private lateinit var homeViewPager1List: MutableList<Int>
    private lateinit var homeViewPager2List: MutableList<Int>
    private val sliderHandler: Handler = Handler()
    private val sliderRunnable = Runnable {
        if (binding.homeBannerViewpager2.currentItem == 3)
            binding.homeBannerViewpager2.currentItem = 0
        else {
            binding.homeBannerViewpager2.currentItem = binding.homeBannerViewpager2.currentItem + 1
        }
    }

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
//        songDB = SongDatabase.getInstance(requireContext())!!
//        albumDatas.addAll(songDB.albumDao().getAlbums())
//
//        val albumRVAdapter = AlbumRVAdapter(albumDatas)
//        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
//        // 어뎁터 연결
//        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        // 가로로 스크롤 설정
//        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener{
//            override fun onItemClick(album: Album){
//                // album: Album은 클릭 했을 때, 그 정보를 인자값으로 전달하기 위함
//                changeAlbumFragment(album)
//            }
//
//            override fun onRemoveAlbum(position: Int) {
//                albumRVAdapter.removeItem(position)
//            }
//        })
//
//        val bannerAdapter = BannerVPAdapter(this)
//        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
//        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
//        binding.homeBannerVp.adapter = bannerAdapter
//        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
//
//        val panelAdapter = PanelVPAdapter(this)
//        panelAdapter.addFragment(PanelFragment(R.drawable.img_first_album_default))
//        panelAdapter.addFragment(PanelFragment(R.drawable.img_first_album_default))
//        panelAdapter.addFragment(PanelFragment(R.drawable.img_first_album_default))
//        binding.homePannelVp.adapter = panelAdapter
//        binding.homePannelVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
//        binding.indicator.setViewPager(binding.homePannelVp)

        makeDummyData()
        initViewPager()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view, R.id.home_new_music_daily_recyclerview, NEW_MUSIC_DAILY)
        initRecyclerView(view, R.id.home_podcast_recyclerview, NEW_MUSIC_DAILY)
        initRecyclerView(view, R.id.home_video_collection_recyclerview, VIDEO_COLLECTION)
        checkNewMusicDailyCategory()
    }

    private fun makeDummyData() {
        songList = mutableListOf(
            Song(
                "Next Level", "aespa", 0, 222, false, "music_nextlevel",
                R.drawable.img_album_exp3, false, "I'm on the Next Level Yeah\n절대적 룰을 지켜", 1
            ),
            Song(
                "작은 것들을 위한 시", "방탄소년단", 0, 229, false, "music_lilac",
                R.drawable.img_album_exp4, false, "모든 게 궁금해\nHow's your day", 2
            ),
            Song(
                "BAAM", "모모랜드 (MOMOLAND)", 0, 208, false, "music_nextlevel",
                R.drawable.img_album_exp5, false, "Bae Bae Bae BAAM BAAM\nBae Bae Bae BAAM BAAM", 3
            ),
            Song(
                "Weekend", "태연", 0, 234, false, "music_nextlevel",
                R.drawable.img_album_exp6, false, "가장 가까운 바다\n혼자만의 영화관", 4
            )
        )
        podcastList = mutableListOf(
            Song("제목", "가수", R.drawable.img_potcast_exp),
            Song("제목", "가수", R.drawable.img_potcast_exp),
            Song("제목", "가수", R.drawable.img_potcast_exp),
            Song("제목", "가수", R.drawable.img_potcast_exp)
        )
        homeViewPager1List = mutableListOf(
            R.drawable.img_home_viewpager_exp,
            R.drawable.img_home_viewpager_exp,
            R.drawable.img_home_viewpager_exp,
        )
        homeViewPager2List = mutableListOf(
            R.drawable.img_home_viewpager_exp2,
            R.drawable.img_home_viewpager_exp2,
            R.drawable.img_home_viewpager_exp2
        )
    }

    private fun initRecyclerView(view: View, recyclerViewId: Int, viewHolderType: Int) {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        val recyclerView = view.findViewById<RecyclerView>(recyclerViewId)
        recyclerView.layoutManager = layoutManager
        val recyclerViewItemList = when (recyclerViewId) {
            R.id.home_new_music_daily_recyclerview -> songList
            R.id.home_podcast_recyclerview -> podcastList
            R.id.home_video_collection_recyclerview -> videoCollectionList
            else -> emptyList()
        }.toMutableList()
        val newMusicDailyAdapter = NewMusicDailyAdapter(viewHolderType, this)
        recyclerView.adapter = newMusicDailyAdapter
        newMusicDailyAdapter.submitList(recyclerViewItemList)
    }


    //    private fun changeAlbumFragment(album: Album) {
//        (context as MainActivity).supportFragmentManager.beginTransaction()
//            .replace(R.id.main_frm, AlbumFragment().apply {
//                arguments = Bundle().apply {
//                    val gson = Gson()
//                    val albumJson = gson.toJson(album)
//                    putString("album", albumJson)
//                }
//            })
//            .commitAllowingStateLoss()
//        // RecyclerView 클릭 시, AlbumFragment로 넘어가짐
//    }
    private fun initViewPager() {
        homeViewPagerAdapter = HomeViewPagerAdapter(homeViewPager1List, ADD)
        binding.homeAdViewpager1.adapter = homeViewPagerAdapter
        homeViewPagerAdapter = HomeViewPagerAdapter(homeViewPager2List, ADD)
        binding.homeAdViewpager2.adapter = homeViewPagerAdapter
        val bannerViewPagerAdapter = ViewpagerFragmentAdapter(this, HOME)
        bannerViewPagerAdapter.addFragment(HomeBannerFragment("포근하게 덮어주는 꿈의\n목소리"))
        bannerViewPagerAdapter.addFragment(HomeBannerFragment("복잡한 머릿속을\n비워주는 잔잔한 팝"))
        bannerViewPagerAdapter.addFragment(HomeBannerFragment("아무 생각이 나지 않는\n밤엔 인디팝"))
        bannerViewPagerAdapter.addFragment(HomeBannerFragment("나만 알고 싶은 쓸쓸한 맛\nK-발라드"))
        val bannerViewPager = binding.homeBannerViewpager2
        bannerViewPager.adapter = bannerViewPagerAdapter
        binding.homeCircleIndicator.setViewPager(bannerViewPager)
        bannerViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 3000)
            }
        })
    }

    private fun checkNewMusicDailyCategory() {
        binding.homeNewMusicDailyDomesticTv.setOnClickListener {
            binding.homeNewMusicDailyDomesticTv.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.category_selected
                )
            )
            binding.homeNewMusicDailyForeignTv.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.category_unselected
                )
            )
            binding.homeNewMusicDailySynthesisTv.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.category_unselected
                )
            )
        }
        binding.homeNewMusicDailyForeignTv.setOnClickListener {
            binding.homeNewMusicDailyDomesticTv.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.category_unselected
                )
            )
            binding.homeNewMusicDailyForeignTv.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.category_selected
                )
            )
            binding.homeNewMusicDailySynthesisTv.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.category_unselected
                )
            )
        }
        binding.homeNewMusicDailySynthesisTv.setOnClickListener {
            binding.homeNewMusicDailyDomesticTv.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.category_unselected
                )
            )
            binding.homeNewMusicDailyForeignTv.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.category_unselected
                )
            )
            binding.homeNewMusicDailySynthesisTv.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.category_selected
                )
            )
        }
    }

    override fun onClick(song: Song, viewType: Int) {
        val bundle = Bundle()
        when (viewType) {
            PLAY_BTN -> {
                bundle.putInt(SONG_ALBUM_INDEX, song.albumIdx)
                parentFragmentManager.setFragmentResult(MUSIC, bundle)
            }

            else -> {
                var tempFragment = AlbumFragment()
                val songDB = SongDatabase.getInstance(requireContext())!!
                val album = songDB.AlbumDao().getAlbum(song.albumIdx)
                bundle.putInt(SONG_ALBUM_INDEX, album.id)
                tempFragment.arguments = bundle
                parentFragmentManager.beginTransaction()
                    .replace(R.id.activity_main_fragment_container, tempFragment)
                    .addToBackStack(ALBUM_FRAGMENT)
                    .commitAllowingStateLoss()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 3000)
    }
    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

}