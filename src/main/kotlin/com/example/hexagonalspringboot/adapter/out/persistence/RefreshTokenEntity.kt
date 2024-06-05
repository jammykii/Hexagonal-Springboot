package com.example.hexagonalspringboot.adapter.out.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed

@RedisHash(value = "refresh", timeToLive = 60 * 60 * 24 * 14) // 2주
class RefreshTokenEntity internal constructor(@field:Id val id: String?, @field:Indexed var refreshToken: String?) {
    fun update(sb: String?) {
        this.refreshToken = sb
    }

    class RefreshTokenBuilder internal constructor() {
        private var id: String? = null
        private var refreshToken: String? = null
        fun id(id: String?): RefreshTokenBuilder {
            this.id = id
            return this
        }

        fun refreshToken(refreshToken: String?): RefreshTokenBuilder {
            this.refreshToken = refreshToken
            return this
        }

        fun build(): RefreshTokenEntity {
            return RefreshTokenEntity(this.id, this.refreshToken)
        }

        override fun toString(): String {
            return "RefreshToken.RefreshTokenBuilder(id=" + this.id + ", refreshToken=" + this.refreshToken + ")"
        }
    }

    companion object {
        fun builder(): RefreshTokenBuilder {
            return RefreshTokenBuilder()
        }

        fun from(id: String?, refreshToken: String?): RefreshTokenEntity {
            return builder()
                .id(id)
                .refreshToken(refreshToken)
                .build()
        }
    }
}