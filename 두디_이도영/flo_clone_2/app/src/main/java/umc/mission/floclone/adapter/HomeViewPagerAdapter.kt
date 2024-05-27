package umc.mission.floclone.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import umc.mission.floclone.databinding.FragmentAlbumBinding
import umc.mission.floclone.databinding.ItemBSideTrackBinding
import umc.mission.floclone.databinding.ItemHomeVideoCollectionBinding
import umc.mission.floclone.databinding.ItemHomeViewPagerBinding
import umc.mission.floclone.databinding.ItemLookChartBinding

class HomeViewPagerAdapter(private val list: MutableList<Int>, private val viewHolderType: Int): RecyclerView.Adapter<ViewHolder>() {
    class HomeViewPagerViewHolder(private val binding: ItemHomeViewPagerBinding): ViewHolder(binding.root){
        fun bind(ResId: Int){
            binding.itemViewPagerIv.setImageResource(ResId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val binding = ItemHomeViewPagerBinding.inflate(inflater, parent, false)
            return HomeViewPagerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemViewPager = list[position]
        if(viewHolderType == ADD) {
            (holder as HomeViewPagerViewHolder).bind(itemViewPager)
        }
        else{

        }
    }

    companion object{
        const val ADD = 0
        const val BSideTrack = 1
    }
}