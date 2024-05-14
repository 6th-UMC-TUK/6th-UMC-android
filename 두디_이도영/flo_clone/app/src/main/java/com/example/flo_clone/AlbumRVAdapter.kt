package com.example.flo_clone

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo_clone.databinding.ItemAlbumBinding

class AlbumRVAdapter(private val albumList: ArrayList<Album>) :
    RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        // RecyclerView 외부에서 클릭 이벤트 발생 시, 이를 사용하기 위함
        fun onItemClick(album: Album)
        fun onRemoveAlbum(position: Int)
    }

    fun addItem(album: Album) {
        albumList.add(album)
        notifyDataSetChanged()
    }
    fun removeIten(position: Int) {
        albumList.removeAt(position)
        notifyDataSetChanged()
    }
    // honeFragment에서 이 함수를 호출하면 됨

    private lateinit var myItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        myItemClickListener = itemClickListener
    }

    // 외부에서 전달받은 클릭 이벤트를 저장
    // HomeFragment에서 받으면 됨
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): AlbumRVAdapter.ViewHolder {
        val binding: ItemAlbumBinding =
            ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])

        holder.itemView.setOnClickListener {
            // 클릭 이벤트 설정
            myItemClickListener.onItemClick(albumList[position])
            // HomeFragment에서 클릭 이벤트 발생 시 myItemClickListener를 통해 이를 전달 받고, 실행함
        }
        // -> 다른 방식으로 작성
//        holder.binding.itemAlbumTitleTv.setOnClickListener{ myItemClickListener.onRemoveAlbum(position)}
        // -> 2번째 방식
    }

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        // Item View 객체들을 재활용하기 위해 사용
        fun bind(album: Album) {
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)
        }

    }
}