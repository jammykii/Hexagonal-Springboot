package com.example.hexagonalspringboot.application.port.out

import org.springframework.stereotype.Component

interface MemberPort {
    suspend fun getMember(id: String):MemberInformation
}