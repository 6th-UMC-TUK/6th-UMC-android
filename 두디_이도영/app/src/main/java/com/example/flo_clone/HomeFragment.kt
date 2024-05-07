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
import androidx.viewpager2.widget.ViewPager2
import com.example.flo_clone.databinding.ActivityMainBinding
import com.example.flo_clone.databinding.FragmentHomeBinding

class HomeFragment() : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )

        binding.homeAlbumImgIv1.setOnClickListener {
            // activity에서 사용한 startActivity와는 조금 다름
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, AlbumFragment()).commitAllowingStateLoss()
            // replace 안에는 어느 곳을 어떤 곳으로 바꿀지 작성
            // 하나의 패턴처럼 이해
            // 위의 코드로 프래그먼트를 이동함
        }
//        val bannerAdapter = BannerVPAdapter(this)
//        // this로 초기화 함
//        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
//        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
//        binding.homeBannerVp.adapter = bannerAdapter
        // ViewPager와 Adapter 연결

//        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
//        Log.d("HomeFragment", "ViewPager2 좌우 스크롤")
        // ViewPager가 좌우로 스크롤될 수 있도록 설정
        val albumAdapter = AlbumVPAdapter(this)

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

    override fun onDestroyView() {
        super.onDestroyView()
    }
}