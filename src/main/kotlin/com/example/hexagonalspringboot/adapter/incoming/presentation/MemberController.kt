package com.example.hexagonalspringboot.adapter.incoming.presentation

import com.example.hexagonalspringboot.application.port.incoming.MemberList
import com.example.hexagonalspringboot.application.port.incoming.MemberUseCase
import com.example.hexagonalspringboot.application.port.incoming.ShowMember
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/members")
class MemberController(private val memberUseCase: MemberUseCase) {

    @GetMapping("/list")
    suspend fun getMemberList(@RequestParam id:String): List<MemberList> {
        val lists: List<MemberList> =  memberUseCase.memberList(id)
        return lists
    }

//    @PostMapping("/login")
//    public fun login(@RequestBody signIn: SignIn) : ResponseEntity<ShowMember> {
//        return ResponseEntity.ok(MemberUseCase.login(signIn))
//    }
}