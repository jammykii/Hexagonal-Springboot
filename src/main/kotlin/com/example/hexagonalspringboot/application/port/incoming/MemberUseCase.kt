package com.example.hexagonalspringboot.application.port.incoming

import com.example.hexagonalspringboot.dto.JwtTokenDto
import com.example.hexagonalspringboot.dto.MemberList
import com.example.hexagonalspringboot.dto.SignIn
import com.example.hexagonalspringboot.dto.SignUp

interface MemberUseCase {
    suspend fun login(signIn: SignIn): JwtTokenDto
    suspend fun register (signUp: SignUp)
    suspend fun memberList(id:String): List<MemberList>
}