package com.example.hexagonalspringboot.adapter.out.persistence

import com.example.hexagonalspringboot.application.port.out.MemberInformation
import com.example.hexagonalspringboot.application.port.out.MemberPort
import org.springframework.stereotype.Component

@Component
class MemberAdapter(private val memberRepository: MemberRepository) : MemberPort {

    override suspend fun getMember(id: String):MemberInformation {
        val member: MemberEntity = memberRepository.findById(id)!!
        return MemberInformation(member.no!! ,member.id!!, member.username, member.email!!, member.password, member.phone!!, member.memCreateDt!!, member.isMemDeleted, member.memChangeDt, member.roles!!)
    }


}