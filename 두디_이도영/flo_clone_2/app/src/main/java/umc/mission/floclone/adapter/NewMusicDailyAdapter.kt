package umc.mission.floclone.adapter

import android.content.Context
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import umc.mission.floclone.data.Song
import umc.mission.floclone.databinding.ItemBSideTrackBinding
import umc.mission.floclone.databinding.ItemDownloadedMusicBinding
import umc.mission.floclone.databinding.ItemHomeVideoCollectionBinding
import umc.mission.floclone.databinding.ItemNewMusicDailyBinding

class NewMusicDailyAdapter(
    private val viewHolderType: Int,
    private val itemClickListener: ItemClickListener? = null
) : ListAdapter<Song, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<Song>() {
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem == newItem
    }
}) {
    private val switchStatus = SparseBooleanArray()
    class NewMusicDailyViewHolder(private val binding: ItemNewMusicDailyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val playBtn = binding.itemNewMusicDailyPlayBtn
        val musicImg = binding.ivRecyclerviewNewMusicDailyImg
        fun bind(song: Song) {
            binding.apply {
                tvRecyclerviewNewMusicDailyTitle.text = song.title
                tvRecyclerviewNewMusicDailySinger.text = song.singer
                ivRecyclerviewNewMusicDailyImg.setImageResource(song.coverImg ?: 0)
            }
        }
    }

    class VideoCollectionViewHolder(private val binding: ItemHomeVideoCollectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            binding.apply {
                tvRecyclerviewVideoCollectionTitle.text = song.title
                tvRecyclerviewVideoCollectionSinger.text = song.singer
                ivRecyclerviewVideoCollectionImg.setImageResource(song.coverImg ?: 0)
            }
        }
    }

    class BSideTrackViewHolder(private val binding: ItemBSideTrackBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            binding.apply {
                itemTrackMusicTitleTv.text = song.title
                itemTrackSingerTv.text = song.singer
            }
        }
    }
    inner class DownloadedMusicViewHolder(private val binding: ItemDownloadedMusicBinding):
        RecyclerView.ViewHolder(binding.root){
        val moreBtn = binding.itemDownloadedMusicMoreBtn
        fun bind(song: Song){
            binding.apply {
                itemDownloadedMusicImgIv.setImageResource(song.coverImg ?: 0)
                itemDownloadedMusicTitleTv.text = song.title
                itemDownloadedMusicSingerTv.text = song.singer
                Switch.isChecked = switchStatus[adapterPosition]
                Switch.setOnClickListener {
                    if(!Switch.isChecked)
                        switchStatus.put(adapterPosition, false)
                    else
                        switchStatus.put(adapterPosition, true)
                    Log.d("switch", "$switchStatus")
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding: ViewBinding?
        return when (viewHolderType) {
            NEW_MUSIC_DAILY -> {
                binding = ItemNewMusicDailyBinding.inflate(inflater, parent, false)
                NewMusicDailyViewHolder(binding)
            }
            VIDEO_COLLECTION -> {
                binding = ItemHomeVideoCollectionBinding.inflate(inflater, parent, false)
                VideoCollectionViewHolder(binding)
            }
            B_SIDE_TRACK -> {
                binding = ItemBSideTrackBinding.inflate(inflater, parent, false)
                BSideTrackViewHolder(binding)
            }
            else -> {
                binding = ItemDownloadedMusicBinding.inflate(inflater, parent, false)
                DownloadedMusicViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val newMusicDaily = currentList[position]
        when (viewHolderType) {
            NEW_MUSIC_DAILY -> {
                (holder as NewMusicDailyViewHolder).apply {
                    bind(newMusicDaily)
                    playBtn.setOnClickListener {
                        itemClickListener?.onClick(newMusicDaily, PLAY_BTN)
                    }
                    musicImg.setOnClickListener {
                        itemClickListener?.onClick(newMusicDaily, MUSIC_IMG)
                    }
                }
            }
            VIDEO_COLLECTION -> {
                (holder as VideoCollectionViewHolder).bind(newMusicDaily)
            }
            B_SIDE_TRACK -> {
                (holder as BSideTrackViewHolder).bind(newMusicDaily)
            }
            else -> {
                (holder as DownloadedMusicViewHolder).apply {
                    bind(newMusicDaily)
                    moreBtn.setOnClickListener {
                        itemClickListener?.onRemove(adapterPosition)
                        notifyItemRangeChanged(0, currentList.size)
                    }
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    companion object {
        const val NEW_MUSIC_DAILY = 0
        const val VIDEO_COLLECTION = 1
        const val B_SIDE_TRACK = 2
        const val PLAY_BTN = 3
        const val MUSIC_IMG = 4
        const val DOWNLOADED_MUSIC = 6
    }

    interface ItemClickListener {
        fun onClick(song: Song, viewType: Int)
        fun onRemove(position: Int)
    }
}