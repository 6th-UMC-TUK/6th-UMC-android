package com.example.flo_clone.Locker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo_clone.Song.Song
import com.example.flo_clone.databinding.ItemSongBinding

class SavedSongRVAdapter(private val songList: ArrayList<Song>):
RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {

    interface DeleteItemClickListener {
        fun onRemoveItem(position: Int)
    }

    private lateinit var delItemClickListener: DeleteItemClickListener
    fun setMyItemClickListener(itemClickListener: DeleteItemClickListener) {
        delItemClickListener = itemClickListener
    }
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): SavedSongRVAdapter.ViewHolder {
        val binding: ItemSongBinding = ItemSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedSongRVAdapter.ViewHolder, position: Int) {
        holder.bind(songList[position])
        holder.binding.itemSongMoreIv.setOnClickListener { delItemClickListener.onRemoveItem(position) }
    }

    override fun getItemCount(): Int = songList.size

    inner class ViewHolder(val binding: ItemSongBinding):
            RecyclerView.ViewHolder(binding.root) {

                fun bind(song: Song) {
                    binding.itemSongTitleTv.text = song.title
                    binding.itemSongSingerTv.text = song.singer
                    binding.itemSongImgIv.setImageResource(song.coverImg!!)
                }
            }

    fun removeItem(position: Int) {
        songList.removeAt(position)
        notifyDataSetChanged()
    }
}