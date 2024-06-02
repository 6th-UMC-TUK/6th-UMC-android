package com.example.flo.song

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SongTable")
data class Song(
        var coverImg: Int? = null,
        val title : String = "",
        val singer : String = "",
        var second : Int = 0,
        var playTime : Int = 0,
        var isplaying : Boolean = false,
        var music : String = "",
        var isLike : Boolean = false,
        var albumIdx : Int = 0
){
        @PrimaryKey(autoGenerate = true) var id: Int = 0
}