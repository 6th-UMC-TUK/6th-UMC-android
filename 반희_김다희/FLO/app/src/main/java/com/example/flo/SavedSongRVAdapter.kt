package com.example.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemSavedSongBinding

class SavedSongRVAdapter(private val savedSongList: ArrayList<SavedSong>) :
    RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>()
{
    private val songs = ArrayList<Song>()
    lateinit var song: Song
    interface MyItemClickListener{
        fun onRemoveSong(songId: Int)
    }
    private lateinit var mItemClickListener : MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }


    // 곡 저장
    fun addSongs(songs: ArrayList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }

    // 저장된 곡 삭제
    private fun removeSong(position: Int){
        songs.removeAt(position)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SavedSongRVAdapter.ViewHolder {
        val binding: ItemSavedSongBinding = ItemSavedSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedSongRVAdapter.ViewHolder, position: Int) {
        holder.bind(savedSong = SavedSong())
        holder.binding.itemSongMoreIv.setOnClickListener {
            mItemClickListener.onRemoveSong(position)
            removeSong(position)
        }
    }

    // 데이터 세트 크기 함수
    override fun getItemCount(): Int = songs.size

    // 뷰 홀더
    inner class ViewHolder(val binding: ItemSavedSongBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(savedSong: SavedSong){
            binding.itemSongImgIv.setImageResource(savedSong.coverImg!!)
            binding.itemSongTitleTv.text = savedSong.title
            binding.itemSongSingerTv.text = savedSong.singer
        }
    }
}