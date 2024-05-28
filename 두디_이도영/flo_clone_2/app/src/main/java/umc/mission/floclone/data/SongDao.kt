package umc.mission.floclone.data

import androidx.room.*

@Dao
interface SongDao {
    @Insert
    fun insert(song: Song)

    @Update
    fun update(song: Song)

    @Delete
    fun delete(song: Song)

    @Query("SELECT * FROM SongTable")
    fun getSongs(): List<Song>

    @Query("SELECT * FROM SongTable WHERE id = :id")
    fun getSong(id: Int): Song

    @Query("UPDATE SongTable SET isLike = :isLike WHERE id = :id")
    fun updateIsLikeById(isLike: Boolean, id: Int)

    @Query("UPDATE SongTable SET title = :title WHERE id = :id")
    fun updateTitle(title: String, id: Int)

    @Query("UPDATE SongTable SET coverImg = :coverImg WHERE id = :id")
    fun updateCoverImgId(coverImg: Int, id: Int)

    @Query("SELECT * FROM SongTable WHERE isLike = :isLike")
    fun getLikedSongs(isLike: Boolean): List<Song>

    @Query("SELECT * FROM SongTable WHERE albumIdx = :albumIdx")
    fun getSongsInAlbum(albumIdx: Int): List<Song>

    @Query("DELETE FROM SongTable WHERE id = :id")
    fun removeSong(id: Int)
}