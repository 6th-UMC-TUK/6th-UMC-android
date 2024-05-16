package com.example.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemSongBinding
import com.example.flo.Song


class SavedSongRVAdapter(private val songs: ArrayList<Song>) :
    RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {

    //클릭 인터페이스 정의
    interface MyItemClickListener {
        fun onRemoveSong(position: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }


    // 곡 저장
    fun addSongs(songs: ArrayList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }

    // 저장된 곡 삭제
    private fun removeSong(position: Int) {
        songs.removeAt(position)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): SavedSongRVAdapter.ViewHolder {
        val binding: ItemSongBinding =
            ItemSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedSongRVAdapter.ViewHolder, position: Int) {
        holder.bind(songs[position])
        holder.binding.itemSongMoreIv.setOnClickListener {
            mItemClickListener.onRemoveSong(position)
            removeSong(position)
        }
    }

    // 데이터 세트 크기 함수
    override fun getItemCount(): Int = songs.size

    // 뷰 홀더
    inner class ViewHolder(val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            binding.itemSongImgIv.setImageResource(song.coverImg!!)
            binding.itemSongTitleTv.text = song.title
            binding.itemSongSingerTv.text = song.singer
        }
    }
}