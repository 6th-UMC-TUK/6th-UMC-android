package com.example.flo.locker

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flo.SavedAlbumFragment

class LockerVPAdapter (fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int  = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> SavedSongFragment()
            1 -> MusicFileFragment()
            else -> SavedAlbumFragment()
        }
    }
}