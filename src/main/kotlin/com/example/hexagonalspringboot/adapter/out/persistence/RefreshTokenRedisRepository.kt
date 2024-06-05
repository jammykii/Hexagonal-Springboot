package com.example.hexagonalspringboot.adapter.out.persistence

import org.springframework.data.repository.CrudRepository
import java.util.*

interface RefreshTokenRedisRepository : CrudRepository<RefreshTokenEntity?, String?> {
    fun findByRefreshToken(refreshToken: String?): RefreshTokenEntity?

    fun deleteByRefreshToken(refreshToken: String?)
}
