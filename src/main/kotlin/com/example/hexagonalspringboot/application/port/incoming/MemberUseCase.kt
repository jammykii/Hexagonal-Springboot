package com.example.hexagonalspringboot.application.port.incoming

import com.example.hexagonalspringboot.adapter.incoming.presentation.SignIn

interface MemberUseCase {
//    suspend fun login(SignIn: SignIn)

    suspend fun memberList(id:String): List<MemberList>
}