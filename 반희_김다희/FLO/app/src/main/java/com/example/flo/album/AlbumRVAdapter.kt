package com.example.flo.album

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemAlbumBinding

class AlbumRVAdapter(private val albumList: ArrayList<Album>,
    val onClick : (album: Album) -> Unit
) : RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>(){

    // 클릭 인터페이스 정의
    interface MyItemClickListener{
        fun onItemClick(album: Album)
        fun onRemoveAlbum(position: Int)
        fun onPlayAlbum(album: Album)
    }

    // Fragment와 그 Fragment를 호스팅 중인 Activity 간의 통신을 위한 Interface
    interface CommunicationInterface {
        fun sendData(album: Album)
    }

    // 리스너 객체를 전달받는 함수, 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    // 앨범 추가 함수
    fun addItem(album: Album){
        albumList.add(album)
        notifyDataSetChanged()
    }

    // 앨범 삭제 함수
    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

    // 뷰홀더를 생성해줘야 할 때 호출되는 함수 => 아이템 뷰 객체를 만들어서 뷰홀더에 던져줌
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    // 뷰홀더에 데이터를 바인딩해줘야 할 때마다 호출되는 함수 => 엄청나게 많이 호출
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumList[position])

        // 타이틀 누르면 삭제되는 클릭 이벤트
        // holder.binding.itemAlbumTitleTv.setOnClickListener { mItemClickListener.onRemoveAlbum(position) }

        holder.binding.itemAlbumCoverImgCardView.setOnClickListener {
            mItemClickListener.onItemClick(albumList[position])
        }

        // 앨범의 재생 버튼 클릭 이벤트 리스너 => miniPlayer에 해당 앨범 제목, 가수 표시
        holder.binding.itemAlbumPlayImgIv.setOnClickListener {
            Log.d("AlbumRVAdapter", "Play button clicked for album: ${albumList[position].title}")
            // OK
            onClick(albumList[position])
            mItemClickListener.onItemClick(albumList[position])
        }
    }

    // 데이터 세트 크기를 알려주는 함수 => 리사이클러뷰의 마지막이 언제인지를 알게 됨
    override fun getItemCount(): Int = albumList.size

    // 뷰홀더
    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(album: Album){
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)
        }
    }
}