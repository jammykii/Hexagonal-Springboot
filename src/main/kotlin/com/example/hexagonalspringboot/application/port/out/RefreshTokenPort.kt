package com.example.hexagonalspringboot.application.port.out

import com.example.hexagonalspringboot.dto.RefreshTokenDto

interface RefreshTokenPort {
    suspend fun getRefreshToken(memberId:String): RefreshTokenDto
    suspend fun update(refreshTokenString: String)
    suspend fun create(memberId: String)
}