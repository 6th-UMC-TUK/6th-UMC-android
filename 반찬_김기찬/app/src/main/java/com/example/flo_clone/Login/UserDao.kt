package com.example.flo_clone.Login

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM UserTable")
    fun getUsers(): List<User>

    // 이메일과 비밀번호가 모두 같은 유저 정보를 가져옴
    @Query("SELECT * FROM UserTable WHERE email = :email AND password = :password")
    fun getUser(email: String, password: String): User? // User가 없을수도 있으므로 ? 처리

    @Query("SELECT * FROM UserTable WHERE email = :email LIMIT 1")
    fun getUserByEmail(email: String): User?

}