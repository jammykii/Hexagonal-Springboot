package com.example.hexagonalspringboot.application.port.incoming

data class MemberList(
    val id : String,
    val name : String,
    val email : String,
    val phoneNumber : String,
) {
}