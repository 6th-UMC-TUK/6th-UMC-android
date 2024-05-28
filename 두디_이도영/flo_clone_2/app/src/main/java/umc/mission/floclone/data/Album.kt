package umc.mission.floclone.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlbumTable")
data class Album(
    @PrimaryKey val id: Int,
    val title: String,
    val singer: String,
    val coverImg: Int? = null,
    val info: String
)
