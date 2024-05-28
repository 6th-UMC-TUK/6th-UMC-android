package umc.mission.floclone

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.Runnable
import umc.mission.floclone.adapter.NewMusicDailyAdapter.Companion.NEW_MUSIC_DAILY
import umc.mission.floclone.adapter.NewMusicDailyAdapter.Companion.VIDEO_COLLECTION
import umc.mission.floclone.adapter.HomeViewPagerAdapter
import umc.mission.floclone.adapter.HomeViewPagerAdapter.Companion.ADD
import umc.mission.floclone.adapter.NewMusicDailyAdapter
import umc.mission.floclone.adapter.NewMusicDailyAdapter.Companion.PLAY_BTN
import umc.mission.floclone.adapter.ViewpagerFragmentAdapter
import umc.mission.floclone.adapter.ViewpagerFragmentAdapter.Companion.HOME
import umc.mission.floclone.album.AlbumFragment
import umc.mission.floclone.data.*
import umc.mission.floclone.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), NewMusicDailyAdapter.ItemClickListener {
    private lateinit var homeViewPagerAdapter: HomeViewPagerAdapter
    private lateinit var songList: MutableList<Song>
    private lateinit var podcastList: MutableList<Song>
    private lateinit var videoCollectionList: MutableList<Song>
    private lateinit var homeViewPager1List: MutableList<Int>
    private lateinit var homeViewPager2List: MutableList<Int>
    private lateinit var binding: FragmentHomeBinding
    private val sliderHandler: Handler = Handler()
    private val sliderRunnable = Runnable {
        if (binding.homeBannerViewpager2.currentItem == 3)
            binding.homeBannerViewpager2.currentItem = 0
        else {
            binding.homeBannerViewpager2.currentItem = binding.homeBannerViewpager2.currentItem + 1
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
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
            Song("Next Level", "aespa", 0, 222, false, "music_nextlevel",
                R.drawable.img_album_exp3, false,"I'm on the Next Level Yeah\n절대적 룰을 지켜", 1),
            Song("작은 것들을 위한 시", "방탄소년단", 0, 229, false, "music_lilac",
                R.drawable.img_album_exp4, false, "모든 게 궁금해\nHow's your day", 2),
            Song("BAAM", "모모랜드 (MOMOLAND)", 0, 208, false, "music_nextlevel",
                R.drawable.img_album_exp5, false, "Bae Bae Bae BAAM BAAM\nBae Bae Bae BAAM BAAM", 3),
            Song("Weekend", "태연", 0, 234, false, "music_nextlevel",
                R.drawable.img_album_exp6, false, "가장 가까운 바다\n혼자만의 영화관", 4)
        )
        podcastList = mutableListOf(
            Song("제목", "가수", R.drawable.img_potcast_exp),
            Song("제목", "가수", R.drawable.img_potcast_exp),
            Song("제목", "가수", R.drawable.img_potcast_exp),
            Song("제목", "가수", R.drawable.img_potcast_exp)
        )
        videoCollectionList = mutableListOf(
            Song("제목", "가수", R.drawable.img_video_exp),
            Song("제목", "가수", R.drawable.img_video_exp),
            Song("제목", "가수", R.drawable.img_video_exp),
            Song("제목", "가수", R.drawable.img_video_exp)
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
                /*bundle.apply {
                    putString(MUSIC_TITLE, song.title)
                    putString(MUSIC_SINGER, song.singer)
                    putInt(MUSIC_IMG_RES_ID, song.coverImg ?: 0)
                    putString(LYRICS, song.lyrics)
                    putInt(SECOND, song.second)
                    putInt(PLAY_TIME, song.playTime)
                    putString(MUSIC, song.music)
                    putBoolean(IS_PLAYING, true)
                    putBoolean(IS_LIKE, false)
                }*/
                bundle.putInt(SONG_ALBUM_INDEX, song.albumIdx)
                parentFragmentManager.setFragmentResult(MUSIC, bundle)
            }
            else -> {
                var tempFragment = AlbumFragment()
                //bundle.putString(MUSIC_TITLE, song.title)
                //bundle.putString(MUSIC_SINGER, song.singer)
                //bundle.putInt(MUSIC_IMG_RES_ID, song.coverImg ?: 0)
                //bundle.putString(ALBUM_INFO, song.albumInfo)
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

    override fun onRemove(position: Int) {
        TODO("Not yet implemented")
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
