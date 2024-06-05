package com.example.hexagonalspringboot.adapter.out.persistence

import com.example.hexagonalspringboot.application.port.out.MemberPort
import com.example.hexagonalspringboot.dto.MemberInformation
import org.springframework.stereotype.Component

@Component
class MemberAdapter(private val memberRepository: MemberRepository) : MemberPort {

    override suspend fun getMember(id: String):MemberInformation {
        val member: MemberEntity = memberRepository.findById(id)!!
        return MemberInformation(member.no!! ,member.id!!, member.username,member.password, member.email!!, member.phone!!, member.memCreateDt!!, member.isMemDeleted, member.memChangeDt, member.roles!!)
    }

    override suspend fun create(member: MemberInformation) {
        val memberEntity: MemberEntity = toEntity(member)
        memberRepository.save(memberEntity)
    }

    fun toEntity(member: MemberInformation): MemberEntity {
        return MemberEntity(member.no, member.id, member.username, member.email, member.password, member.phoneNumber, member.createDt, member.deleted, null,member.changeDt, member.roles)
    }

}