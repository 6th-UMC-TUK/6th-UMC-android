package com.example.flo_clone.Song

// 제목, 가수, 사진, 재생시간, 현재 재생시간, isPlaying(재생되고 있는지)
data class Song(
    val title : String = "",
    val singer : String = "",
    val second : Int = 0,
    val playTime : Int = 0,
    var isPlaying : Boolean = false
)
