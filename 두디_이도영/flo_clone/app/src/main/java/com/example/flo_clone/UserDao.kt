package com.example.flo_clone

import androidx.room.Insert
import androidx.room.Query

interface UserDao {
    // 유저에 대한 정보를 담을 수 있도록 insert 사용
    @Insert
    fun insert(user: User)

    // 모든 유저 정보를 가져올 Query
    @Query("SELECT * FROM UserTable")
    fun getUsers(): List<User>

    // 한명의 유저 정보만을 가져올 Query
    @Query("SELECT * FROM UserTable WHERE email = :email AND password = :password")
    // 입력받은 email과 password가 같은 유저 정보를 가져옴
    fun getUser(email: String, password: String): User?
}