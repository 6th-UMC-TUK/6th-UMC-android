package com.example.flo_clone

data class Song(
    val title : String = "",
    val singer : String = "",
    //  = "" 을 통해 초기화
    var second: Int = 0,
    var playTime: Int = 0,
    var isPlaying: Boolean = false,
    var music: String = "",
)
