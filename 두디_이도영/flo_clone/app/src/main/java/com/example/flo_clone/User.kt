package com.example.flo_clone

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserTable")
data class User(
    // 사용자의 이름과 비밀번호 이메일을 저장할 공간
    // 사용자를 구분할 User index 저장
    var email: String,
    var password: String

){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
    // autoGenerate를 통해 유저가 추가될 때마다 키 등록, 자동으로 카운트함
}
