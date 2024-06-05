package com.example.hexagonalspringboot.application.service

import com.example.hexagonalspringboot.application.port.incoming.MemberUseCase
import com.example.hexagonalspringboot.application.port.out.MemberPort
import com.example.hexagonalspringboot.application.port.out.RefreshTokenPort
import com.example.hexagonalspringboot.domain.JwtToken
import com.example.hexagonalspringboot.domain.Member
import com.example.hexagonalspringboot.dto.*
import com.example.hexagonalspringboot.infra.config.jwt.JwtTokenProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList

@Service
class MemberService(
    private val memberPort: MemberPort,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val refreshTokenPort: RefreshTokenPort
    ) :MemberUseCase {

    override suspend fun register(signUp: SignUp) {
        val roles = ArrayList<String>()
        roles.add("USER")
        val encodedPassword = passwordEncoder.encode(signUp.password)
        val member = signUp(signUp,roles)
        val memberInformation = memberToInformation(member, roles, encodedPassword)
        memberPort.create(memberInformation)
    }

    @Transactional
    override suspend fun login(signIn: SignIn):JwtTokenDto {
        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        val authenticationToken = UsernamePasswordAuthenticationToken(signIn.id, signIn.password)

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        val authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken)

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        val memberInformation = memberPort.getMember(signIn.id)
        val member = informationToDomain(memberInformation)
        val accessToken =
            jwtTokenProvider.generateAccessToken(member.accessTokenClaims, authentication, ACCESS_TOKEN_MAXAGE)
        val refreshToken = jwtTokenProvider.generateRefreshToken(member)

        val jwtToken = JwtToken("Bearer", accessToken, refreshToken)
        val jwtTokenDto = JwtTokenDto(jwtToken.grantType, jwtToken.accessToken, jwtToken.refreshToken)
        return jwtTokenDto
//            .builder()
//            .grantType("Bearer")
//            .accessToken(accessToken)
//            .refreshToken(saveRefreshToken(refreshToken, member))
//            .build()
    }

//    private suspend fun saveRefreshToken(refreshTokenString: String, member: Member): String? {
//        try {
//            val refreshToken:RefreshTokenDto = refreshTokenPort.getRefreshToken(member.memberId)
//            refreshTokenPort.update(refreshTokenString)
//            refreshTokenPort.create(refreshToken)
//        } catch (e: java.lang.NullPointerException) {
//            return refreshTokenRedisRepository.save(RefreshToken.from(member.id, refreshTokenString)).refreshToken
//        }
//        return refreshTokenString
//    }
//
//    override fun reissuanceAccessTokenWithRefreshToken(refreshTokenCookie: String): JwtToken {
//        try {
//            val refreshToken = refreshTokenRedisRepository.findByRefreshToken(refreshTokenCookie)
//                ?.orElseThrow { NullPointerException() }
//            val member = memberRepository.findById(refreshToken?.id).orElseThrow { NullPointerException() }
//            val authorities: Collection<GrantedAuthority?> = member.roles!!.stream()
//                .map { role: String? -> SimpleGrantedAuthority(role) }
//                .collect(Collectors.toList())
//
//            val authentication: Authentication = UsernamePasswordAuthenticationToken(
//                member.id,
//                null,
//                authorities
//            )
//
//            val newAccesstoken =
//                jwtTokenProvider.generateAccessToken(member.accessTokenClaims, authentication, ACCESS_TOKEN_MAXAGE)
//            val refreshTokenRotation = jwtTokenProvider.generateRefreshToken(member)
//
//            return JwtToken.builder()
//                .grantType("Bearer")
//                .accessToken(newAccesstoken)
//                .refreshToken(saveRefreshToken(refreshTokenRotation, member))
//                .build()
//        } catch (e: java.lang.NullPointerException) {
//            if (CheckRefreshToken(refreshTokenCookie)) {
//                deleteRefreshToken(getMemberByRefreshToken(refreshTokenCookie).id!!)
//                throw java.lang.NullPointerException("Expired token")
//            }
//            throw java.lang.NullPointerException("Expired or invalid token")
//        }
//    }

    override suspend fun memberList(id: String): List<MemberList> {

        val memberInformation = memberPort.getMember(id)
        val member = informationToDomain(memberInformation)
        val memberList = ArrayList<MemberList>()
        memberList.add(memberToList(member))
        return memberList
    }

    fun informationToDomain(memberInformation: MemberInformation): Member {
        return Member(
            memberInformation.no,
            memberInformation.id,
            memberInformation.username,
            memberInformation.password ,
            memberInformation.email,
            memberInformation.phoneNumber,
            memberInformation.createDt,
            memberInformation.deleted,
            memberInformation.changeDt,
            memberInformation.roles
        )
    }

    fun memberToList(member: Member): MemberList {
        return MemberList(member.memberId, member.memberName, member.email, member.phone )
    }

    fun signUp(signUp: SignUp, roles: List<String>): Member {
        return Member(
            null,
            signUp.id,
            signUp.username,
            signUp.password,
            signUp.email,
            signUp.phone,
            LocalDateTime.now(),
            false,
            null,
            roles
        )
    }
    fun memberToInformation(member: Member, roles: List<String>, encodedPassword:String): MemberInformation {
        return MemberInformation(
            member.memberNo,
            member.memberId,
            member.memberName,
            encodedPassword,
            member.email,
            member.phone,
            member.createDate,
            member.memberDeleted,
            member.updateDate,
            roles
        )
    }

    companion object {
        private const val ACCESS_TOKEN_MAXAGE = 30
    }

}