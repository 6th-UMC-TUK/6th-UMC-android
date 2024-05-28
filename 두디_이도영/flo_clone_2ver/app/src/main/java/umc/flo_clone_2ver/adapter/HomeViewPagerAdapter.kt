package umc.flo_clone_2ver.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.flo_clone_2ver.databinding.ItemHomeViewPagerBinding

class HomeViewPagerAdapter(private val list: MutableList<Int>, private val viewHolderType: Int): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class HomeViewPagerViewHolder(private val binding: ItemHomeViewPagerBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(ResId: Int){
            binding.itemViewPagerIv.setImageResource(ResId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val binding = ItemHomeViewPagerBinding.inflate(inflater, parent, false)
        return HomeViewPagerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
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