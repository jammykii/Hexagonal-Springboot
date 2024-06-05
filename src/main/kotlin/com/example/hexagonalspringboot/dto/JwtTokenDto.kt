package com.example.hexagonalspringboot.dto

data class JwtTokenDto(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String
) {
}

data class RefreshTokenDto(
    val id: String,
    val refreshToken: String
)