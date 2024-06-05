package com.example.hexagonalspringboot.application.port.out

import java.time.LocalDateTime


data class MemberInformation(
    val no: Long,
    val id: String,
    val username: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val createDt: LocalDateTime,
    val deleted: Boolean,
    val changeDt: LocalDateTime?,
    val roles: List<String>,
    ){}
