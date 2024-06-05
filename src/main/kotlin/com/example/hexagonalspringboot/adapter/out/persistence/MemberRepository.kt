package com.example.hexagonalspringboot.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository: JpaRepository<MemberEntity, Long> {
    fun findById(id: String): MemberEntity?
}
