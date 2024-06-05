package com.example.hexagonalspringboot.application.service

import com.example.hexagonalspringboot.application.port.incoming.MemberUseCase
import com.example.hexagonalspringboot.application.port.out.MemberPort
import com.example.hexagonalspringboot.domain.Member
import com.example.hexagonalspringboot.dto.MemberInformation
import com.example.hexagonalspringboot.dto.MemberList
import com.example.hexagonalspringboot.dto.SignUp
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

@Service
class MemberService(private val memberPort: MemberPort, private val passwordEncoder: PasswordEncoder) :MemberUseCase {

    override suspend fun register(signUp: SignUp) {
        val roles = ArrayList<String>()
        roles.add("USER")
        val encodedPassword = passwordEncoder.encode(signUp.password)
        val member = signUp(signUp,roles)
        val memberInformation = memberToInformation(member, roles, encodedPassword)
        memberPort.create(memberInformation)
    }


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
        return Member(memberInformation.no, memberInformation.id, memberInformation.username, memberInformation.password ,memberInformation.email, memberInformation.phoneNumber, memberInformation.createDt,memberInformation.deleted,memberInformation.changeDt, memberInformation.roles)
    }

    fun memberToList(member: Member): MemberList {
        return MemberList(member.memberId, member.memberName, member.email, member.phone )
    }

    fun signUp(signUp: SignUp, roles: List<String>): Member {
        return Member(null, signUp.id, signUp.username, signUp.password ,signUp.email, signUp.phone, LocalDateTime.now(), false, null, roles)
    }
    fun memberToInformation(member: Member, roles: List<String>, encodedPassword:String): MemberInformation {
        return MemberInformation(null, member.memberId, member.memberName, encodedPassword, member.email, member.phone, LocalDateTime.now(), false, null, roles)
    }

}