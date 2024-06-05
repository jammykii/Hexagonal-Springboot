package com.example.hexagonalspringboot.adapter.out.persistence

import com.example.hexagonalspringboot.application.port.out.RefreshTokenPort
import com.example.hexagonalspringboot.dto.RefreshTokenDto
import org.springframework.stereotype.Component

@Component
class RefreshTokenAdapter(
    private val refreshTokenRedisRepository: RefreshTokenRedisRepository,
    ):RefreshTokenPort {

    override suspend fun getRefreshToken(memberId: String): RefreshTokenDto {
        val refreshToken: RefreshTokenEntity = refreshTokenRedisRepository.findById(memberId)?.orElse(throw NullPointerException())!!
        return RefreshTokenDto(id = refreshToken.id!!, refreshToken = refreshToken.refreshToken!!)
    }

    override suspend fun update(refreshTokenString: String) {
//        val refreshTokenEntity = RefreshTokenEntity.update()
    }

    override suspend fun create(memberId: String) {
        TODO("Not yet implemented")
    }
}