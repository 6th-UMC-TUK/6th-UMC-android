package umc.flo_clone_2ver.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import umc.flo_clone_2ver.album.AlbumBSideTrackFragment
import umc.flo_clone_2ver.album.DetailFragment
import umc.flo_clone_2ver.locker.MusicFileFragment
import umc.flo_clone_2ver.locker.SavedAlbumFragment
import umc.flo_clone_2ver.locker.SavedSongFragment
import umc.flo_clone_2ver.presentation.VideoFragment

class ViewpagerFragmentAdapter(fragment: Fragment, private val caller: Int, private val bundle: Bundle? = null): FragmentStateAdapter(fragment) {
    private var fragmentList:MutableList<Fragment> = when(caller){
        ALBUM -> mutableListOf(AlbumBSideTrackFragment(), DetailFragment(), VideoFragment())
        LOCKER -> mutableListOf(SavedSongFragment(), MusicFileFragment(), SavedAlbumFragment())
        else -> mutableListOf()
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        if(caller == ALBUM) {
            fragmentList[0].arguments = bundle
        }
        return fragmentList[position]
    }

    fun addFragment(fragment: Fragment){
        fragmentList.add(fragment)
        notifyItemInserted(fragmentList.size-1)
    }

    companion object{
        const val ALBUM = 0
        const val LOCKER = 1
        const val HOME = 2
    }
}