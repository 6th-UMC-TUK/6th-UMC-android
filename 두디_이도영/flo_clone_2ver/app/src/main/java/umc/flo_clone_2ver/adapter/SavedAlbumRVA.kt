package umc.flo_clone_2ver.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.flo_clone_2ver.data.Album
import umc.flo_clone_2ver.databinding.ItemSavedAlbumBinding

class SavedAlbumRVA: ListAdapter<Album, SavedAlbumRVA.SavedAlbumViewHolder>(object : DiffUtil.ItemCallback<Album>() {
    override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem == newItem
    }
}){
    class SavedAlbumViewHolder(private val binding: ItemSavedAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val playBtn = binding.itemDownloadedMusicPlayBtn
        fun bind(album: Album) {
            binding.apply {
                itemDownloadedMusicTitleTv.text = album.title
                itemDownloadedMusicSingerTv.text = album.singer
                itemDownloadedMusicImgIv.setImageResource(album.coverImg ?: 0)
                itemDownloadedAlbumInfoTv.text = album.info
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedAlbumViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemSavedAlbumBinding.inflate(inflater, parent, false)
        return SavedAlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedAlbumViewHolder, position: Int) {
        val album = currentList[position]
        holder.bind(album)
    }
}