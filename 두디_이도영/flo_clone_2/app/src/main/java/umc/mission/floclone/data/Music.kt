package umc.mission.floclone.data

data class Music(
    val title: String? = null,
    val singer: String? = null,
    val musicImageResId: Int? = null,
    val lyrics: String? = null,
    val albumInfo: String? = null,
    var second: Int = 0,
    var playTime: Int = 0,
    var isPlaying: Boolean = false,
    var musicFileName: String? = null
)
