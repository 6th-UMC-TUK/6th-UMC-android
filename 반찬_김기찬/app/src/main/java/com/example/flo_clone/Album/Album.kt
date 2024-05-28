package com.example.flo_clone.Album

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flo_clone.Song.Song

@Entity(tableName = "AlbumTable")
data class Album(
    @PrimaryKey(autoGenerate = false) var id: Int = 0, // album의 pk는 임의로 지정해주기 위해 autogenerate 안씁니다.
    var title: String? = "",
    var singer: String? = "",
//    val inMusics : List<Song> = listOf(), // 앨범 수록곡 추가하려면 이렇게 해야함
    var coverImg: Int? = null
)
