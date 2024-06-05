package com.example.hexagonalspringboot.application.service

import com.example.hexagonalspringboot.adapter.incoming.presentation.SignIn
import com.example.hexagonalspringboot.application.port.incoming.MemberList
import com.example.hexagonalspringboot.application.port.incoming.MemberUseCase
import com.example.hexagonalspringboot.application.port.out.MemberInformation
import com.example.hexagonalspringboot.application.port.out.MemberPort
import com.example.hexagonalspringboot.domain.Member
import org.springframework.stereotype.Service

@Service
class MemberService(private val memberPort: MemberPort) :MemberUseCase {


//    override suspend fun login(SignIn: SignIn) {
//
//    }

    override suspend fun memberList(id: String): List<MemberList> {

        val memberInformation = memberPort.getMember(id)
        val member = informationToDomain(memberInformation)
        val memberList = ArrayList<MemberList>()
        memberList.add(memberToList(member))
        return memberList
    }

    fun informationToDomain(memberInformation: MemberInformation): Member {
        return Member(memberInformation.no, memberInformation.id, memberInformation.username, memberInformation.email, memberInformation.phoneNumber, memberInformation.createDt,memberInformation.deleted,memberInformation.changeDt, memberInformation.roles)
    }

    fun memberToList(member: Member): MemberList {
        return MemberList(member.memberId, member.memberName, member.email, member.phone )
    }

}