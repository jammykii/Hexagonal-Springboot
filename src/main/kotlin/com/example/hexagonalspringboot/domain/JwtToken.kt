package com.example.hexagonalspringboot.domain

data class JwtToken(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String
) {
}