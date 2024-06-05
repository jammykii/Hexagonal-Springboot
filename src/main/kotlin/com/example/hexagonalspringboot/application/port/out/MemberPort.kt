package com.example.hexagonalspringboot.application.port.out

import com.example.hexagonalspringboot.dto.MemberInformation
import org.springframework.stereotype.Component

interface MemberPort {
    suspend fun getMember(id: String):MemberInformation
//    suspend fun getAllMembers():List<MemberInformation>
    suspend fun create(member: MemberInformation)
}
