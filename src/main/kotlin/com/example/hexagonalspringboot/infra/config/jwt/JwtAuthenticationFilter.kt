package com.example.hexagonalspringboot.infra.config.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import java.util.Map

class JwtAuthenticationFilter(private val jwtTokenProvider: JwtTokenProvider) : GenericFilterBean() {
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        // 1. Request Header에서 JWT 토큰 추출
        val token = resolveToken(request as HttpServletRequest)

        // 2. validateToken으로 토큰 유효성 검사
        if (token != null) {
            try {
                if (jwtTokenProvider.validateToken(token)) {
                    val claims = jwtTokenProvider.getClaims(token)
                    // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
                    val authentication = jwtTokenProvider.getAuthentication(token)
                    SecurityContextHolder.getContext().authentication = authentication
                    log.info(authentication.toString())
                }
            } catch (e: ExpiredJwtException) {
                log.error(e.message)
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Access token has expired")
                return
            }
        }
        chain.doFilter(request, response)
    }

    @Throws(IOException::class)
    private fun sendErrorResponse(response: ServletResponse, status: HttpStatus, message: String) {
        val httpResponse = response as HttpServletResponse
        httpResponse.status = status.value()
        httpResponse.contentType = MediaType.APPLICATION_JSON_VALUE

        val objectMapper = ObjectMapper()
        val responseBody = objectMapper.writeValueAsString(Map.of("error", message))

        httpResponse.writer.write(responseBody)
    }

    // Request Header에서 토큰 정보 추출
    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        val cookies = request.cookies
        val path = request.requestURI
        var accessToken = ""
        val antPathMatcher = AntPathMatcher()
        if (antPathMatcher.match("/goods/**", path)) {
            return null
        }
        if ("/members/reissuanceAccessToken" == path || "/members/sign-up" == path || "/members/sign-in" == path || "/members/id/exists" == path) {
            return null
        }
        if (cookies != null && cookies.size > 0) {
            for (cookie in cookies) {
                if (cookie.name == "accessToken") {
                    accessToken = cookie.value
                }
            }
            if (accessToken == "undefined") {
                return null
            }
            return accessToken
        }
        return null
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)
    }
}
