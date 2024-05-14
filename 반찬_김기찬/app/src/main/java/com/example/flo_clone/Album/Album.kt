package com.example.flo_clone.Album

import com.example.flo_clone.Song.Song

data class Album(
    var title: String = "",
    var singer: String = "",
    var coverImg: Int? = null,
    var songs: ArrayList<Song>? = null
)
