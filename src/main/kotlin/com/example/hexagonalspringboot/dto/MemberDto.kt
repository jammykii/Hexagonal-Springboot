package com.example.hexagonalspringboot.dto

import java.time.LocalDateTime

data class SignIn(
    val id:String,
    val password:String,
) {
}

data class ShowMember (
    val id:String,
    val name:String,
){
}
data class SignUp(
    val id: String,
    val username: String,
    val email: String,
    val password: String,
    val phone: String,
    val role: String?,
)
data class MemberList(
    val id : String,
    val name : String,
    val email : String,
    val phoneNumber : String,
) {
}
data class MemberInformation(
    val no: Long?,
    val id: String,
    val username: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val createDt: LocalDateTime,
    val deleted: Boolean,
    val changeDt: LocalDateTime?,
    val roles: List<String>,
){}
