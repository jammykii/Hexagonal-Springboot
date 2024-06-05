package com.example.hexagonalspringboot.adapter.incoming.presentation

import com.example.hexagonalspringboot.application.port.incoming.MemberUseCase
import com.example.hexagonalspringboot.dto.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/members")
class MemberController(private val memberUseCase: MemberUseCase) {

    @GetMapping("/list")
    suspend fun getMemberList(@RequestParam id:String): List<MemberList> {
        val lists: List<MemberList> =  memberUseCase.memberList(id)
        return lists
    }

    @PostMapping("/register")
    suspend fun signUp(@RequestBody signUp: SignUp) {
        memberUseCase.register(signUp)
    }

    @PostMapping("/login")
    suspend fun logIn(@RequestBody signIn: SignIn): JwtTokenDto {
        return memberUseCase.login(signIn)
    }
}