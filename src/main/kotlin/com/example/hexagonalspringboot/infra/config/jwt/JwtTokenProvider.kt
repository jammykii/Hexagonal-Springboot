package com.example.hexagonalspringboot.infra.config.jwt

import com.example.hexagonalspringboot.adapter.out.persistence.MemberEntity
import com.example.hexagonalspringboot.domain.Member
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.Collectors
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(@Value("\${spring.jwt.secret}") secretKey: String?) {
    private val key: SecretKey

    @Value("\${spring.jwt.secret}")
    private val secretKey: String? = null

    init {
        val keyBytes = Decoders.BASE64URL.decode(secretKey)
        this.key = Keys.hmacShaKeyFor(keyBytes)
    }

    fun generateRefreshToken(member: Member): String {
        val now = Date().time

        return Jwts.builder()
            .claim("id", UUID.randomUUID().toString())
            .claim("name", member.memberName)
            .signWith(key, Jwts.SIG.HS256)
            .compact()
    }

    fun validationRefreshToken(refreshToken: String?): String {
        try {
            val parseRefreshToken: Jwt<JwsHeader, Claims> = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(refreshToken)

            val name = parseRefreshToken.payload.get("name", String::class.java)

            return name
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid refresh token")
        }
    }

    fun generateAccessToken(claims: Map<String?, Any?>?, authentication: Authentication, seconds: Int): String {
        val now = Date().time
        val expiresAt = Date(now + 1000L * seconds)
        val authorities = authentication.authorities.stream()
            .map { obj: GrantedAuthority -> obj.authority }
            .collect(Collectors.joining(","))

        return Jwts.builder()
            .claim("body", jsonToStr(claims))
            .claim("auth", authorities)
            .expiration(expiresAt)
            .signWith(key, Jwts.SIG.HS256)
            .compact()
    }

    // Jwt 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    fun getAuthentication(accessToken: String): Authentication {
        // Jwt 토큰 복호화
        val claims = parseClaims(accessToken)

        if (claims["auth"] == null) {
            throw RuntimeException("권한 정보가 없는 토큰입니다.")
        }

        // 클레임에서 권한 정보 가져오기
        val authorities: Collection<GrantedAuthority> = Arrays.stream(
            claims["auth"].toString().split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        )
            .map { role: String? -> SimpleGrantedAuthority(role) }
            .collect(Collectors.toList())

        // UserDetails 객체를 만들어서 Authentication return
        // UserDetails: interface, User: UserDetails를 구현한 class
        val claimsToMap = getClaims(accessToken)
        val principal: UserDetails = User(claimsToMap?.get("id") as String?, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }

    // accessToken
    private fun parseClaims(accessToken: String): Claims {
        return try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(accessToken)
                .payload
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }

    fun getClaims(token: String?): LinkedHashMap<*, *>? {
        val body = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
            .get("body", String::class.java)

        return jsonToMap(body)
    }

    private fun jsonToMap(jsonStr: String): LinkedHashMap<*, *>? {
        val objectMapper = ObjectMapper().registerModule(JavaTimeModule())
        try {
            return objectMapper.readValue(jsonStr, LinkedHashMap::class.java)
        } catch (e: JsonProcessingException) {
            throw DataIntegrityViolationException("..")
        }
    }

    // 토큰 정보를 검증하는 메서드
    fun validateToken(token: String?): Boolean {
        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
            return true
        } catch (e: SecurityException) {
            log.info("Invalid JWT Token", e)
        } catch (e: MalformedJwtException) {
            log.info("Invalid JWT Token", e)
        } catch (e: UnsupportedJwtException) {
            log.info("Unsupported JWT Token", e)
        } catch (e: IllegalArgumentException) {
            log.info("JWT claims string is empty.", e)
        }
        return false
    }

    fun isTokenExpired(token: String?): Boolean {
        val claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload

        val expirationDate = claims.expiration

        return expirationDate.before(Date())
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(JwtTokenProvider::class.java)
        fun jsonToStr(claims: Map<String?, Any?>?): Any {
            val objectMapper = ObjectMapper()

            try {
                return objectMapper.writeValueAsString(claims)
            } catch (e: JsonProcessingException) {
                log.error("에러가 발생하였습니다 : {}", e.message)
                throw DataIntegrityViolationException("임시 예외 선언")
            }
        }
    }
}