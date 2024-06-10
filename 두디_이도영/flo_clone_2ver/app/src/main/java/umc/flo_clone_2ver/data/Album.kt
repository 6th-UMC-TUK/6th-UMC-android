package umc.flo_clone_2ver.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlbumTable")
data class Album(
    @PrimaryKey(autoGenerate = false) var id: Int = 0,
    val title: String,
    val singer: String,
    val coverImg: Int? = null,
    val info: String
)
