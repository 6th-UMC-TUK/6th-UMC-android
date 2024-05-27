package com.example.flo.locker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemSongBinding
import com.example.flo.song.Song


class SavedSongRVAdapter() :
    RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {
    private val songList = ArrayList<Song>()

    //클릭 인터페이스 정의
    interface MyItemClickListener {
        fun onRemoveItem(position: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>) {
        this.songList.clear()
        this.songList.addAll(songs)

        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeItem(position: Int){
        songList.removeAt(position)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemSongBinding =
            ItemSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songList[position])
        holder.binding.itemSongMoreIv.setOnClickListener {
            mItemClickListener.onRemoveItem(songList[position].id)
            removeItem(position)
        }
    }

    // 데이터 세트 크기 함수
    override fun getItemCount(): Int = songList.size

    // 뷰 홀더
    inner class ViewHolder(val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            binding.itemSongImgIv.setImageResource(song.coverImg!!)
            binding.itemSongTitleTv.text = song.title
            binding.itemSongSingerTv.text = song.singer
        }
    }
}