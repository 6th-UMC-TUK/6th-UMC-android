package com.example.flo_clone

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlbumTable")
data class Album(
    @PrimaryKey(autoGenerate = false) var id: Int = 0,
    // album의 PrimaryKey는 임의로 지정해주기 위해 autoGenerate를 false로 설정
    var title: String? = "",
    var singer: String? = "",
    var coverImg: Int? = null,
//    var song: ArrayList<Song>? = null

)
