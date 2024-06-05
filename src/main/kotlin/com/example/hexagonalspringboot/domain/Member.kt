package com.example.hexagonalspringboot.domain

import java.time.LocalDateTime

data class Member(
    val memberNo: Long?,
    val memberId: String,
    val memberName: String,
    val password: String,
    val email: String,
    val phone: String,
    val createDate: LocalDateTime,
    val memberDeleted: Boolean = false,
    val updateDate: LocalDateTime?,
    val roles: List<String>
) {
}