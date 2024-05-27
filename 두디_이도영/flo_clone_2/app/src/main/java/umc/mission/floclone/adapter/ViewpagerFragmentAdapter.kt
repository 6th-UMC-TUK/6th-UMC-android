package umc.mission.floclone.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import umc.mission.floclone.*
import umc.mission.floclone.album.AlbumBSideTrackFragment
import umc.mission.floclone.album.DetailFragment
import umc.mission.floclone.locker.SavedSongFragment
import umc.mission.floclone.locker.MusicFileFragment

class ViewpagerFragmentAdapter(fragment: Fragment, private val caller: Int, private val bundle: Bundle? = null): FragmentStateAdapter(fragment) {
    private var fragmentList:MutableList<Fragment> = when(caller){
        ALBUM -> mutableListOf(AlbumBSideTrackFragment(), DetailFragment(), VideoFragment())
        LOCKER -> mutableListOf(SavedSongFragment(), MusicFileFragment())
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