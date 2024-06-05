package com.example.hexagonalspringboot.dto

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
