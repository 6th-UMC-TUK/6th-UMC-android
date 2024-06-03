package umc.flo_clone_2ver.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import umc.flo_clone_2ver.R
import umc.flo_clone_2ver.adapter.ViewPagerFragmentAdapter
import umc.flo_clone_2ver.adapter.ViewPagerFragmentAdapter.Companion.ALBUM
import umc.flo_clone_2ver.data.Album
import umc.flo_clone_2ver.data.Like
import umc.flo_clone_2ver.data.MUSIC_SINGER
import umc.flo_clone_2ver.data.MUSIC_TITLE
import umc.flo_clone_2ver.data.SONG_ALBUM_INDEX
import umc.flo_clone_2ver.data.SongDatabase
import umc.flo_clone_2ver.databinding.FragmentAlbumBinding

class AlbumFragment: Fragment() {
    private lateinit var binding: FragmentAlbumBinding
    private lateinit var musicTitle: String
    private lateinit var musicSinger: String
    private lateinit var album: Album
    private var isLiked: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        isLiked = isLikedAlbum(album.id)
        setInit()
        initViewPager2()
        binding.albumPreviousBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        setOnClickListeners(album)
    }

    private fun initView(){
        val albumIdx = arguments?.getInt(SONG_ALBUM_INDEX, 0)
        val songDB = SongDatabase.getInstance(requireContext())!!
        album = songDB.AlbumDao().getAlbum(albumIdx!!)
        musicTitle = arguments?.getString(MUSIC_TITLE).toString()
        musicSinger = arguments?.getString(MUSIC_SINGER).toString()
        binding.albumAlbumTitleTv.text = album.title
        binding.albumAlbumSingerTv.text = album.singer
        binding.albumAlbumDateTv.text = album.info
        binding.albumAlbumImgIv.setImageResource(album.coverImg ?: 0)
    }

    private fun initViewPager2(){
        var bundle = Bundle()
        //bundle.putString(MUSIC_TITLE, musicTitle)
        //bundle.putString(MUSIC_SINGER, musicSinger)
        bundle.putInt(SONG_ALBUM_INDEX, album.id)
        val fragmentStateAdapter = ViewPagerFragmentAdapter(this, ALBUM, bundle)
        binding.albumViewpager2.adapter = fragmentStateAdapter
        TabLayoutMediator(binding.albumTablayout, binding.albumViewpager2) { tab, position ->
            binding.albumViewpager2.currentItem = tab.position
            tab.text = when(position){
                0 -> "수록곡"
                1 -> "상세정보"
                else -> "영상"
            }
        }.attach()
    }

    private fun setInit(){
        binding.fragmentAlbumLikeBtn.setImageResource(if(isLiked) R.drawable.ic_my_like_on
        else R.drawable.ic_my_like_off)
    }

    private fun getJwt():Int{
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt", 0)
    }

    private fun likeAlbum(userId: Int, albumId: Int){
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId, albumId)

        songDB.AlbumDao().likeAlbum(like)
    }

    private fun isLikedAlbum(albumId: Int): Boolean{
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        val likeId: Int? = songDB.AlbumDao().isLikedAlbum(userId, albumId)

        return likeId != null
    }

    private fun disLikedAlbum(albumId: Int){
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        songDB.AlbumDao().disLikedAlbum(userId, albumId)
    }

    private fun setOnClickListeners(album: Album){
        val userId = getJwt()
        binding.fragmentAlbumLikeBtn.setOnClickListener {
            if(isLiked){
                binding.fragmentAlbumLikeBtn.setImageResource(R.drawable.ic_my_like_off)
                disLikedAlbum(album.id)
            } else{
                binding.fragmentAlbumLikeBtn.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId, album.id)
            }
        }
    }
}