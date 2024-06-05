package com.example.hexagonalspringboot.adapter.out.persistence

import com.example.hexagonalspringboot.application.port.out.MemberInformation
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.stream.Collectors

@Entity
@Table(name = "member")
@JsonIgnoreProperties("orders", "deliveryList")
data class MemberEntity (

    @JvmField
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mem_no", nullable = false)
    var no: Long? = null,

    @JvmField
    @Column(name = "mem_id", nullable = false, unique = true)
    var id: String? = null,

    @Column(name = "mem_name", nullable = false)
    private var username: String? = null,

    @JvmField
    @Column(name = "mem_email")
    var email: String? = null,

    @Column(name = "mem_pwd", nullable = false)
    private var password: String? = null,

    @JvmField
    @Column(name = "mem_phone", nullable = false)
    var phone: String? = null,

    @Column(name = "mem_created_dt", nullable = false)
    var memCreateDt: LocalDateTime? = null,

    @Column(name = "mem_deleted", nullable = false)
    var isMemDeleted: Boolean = false,

    @Column(name = "mem_deleted_dt")
    var memDeletedDt: LocalDateTime? = null,

    @Column(name = "mem_change_dt")
    var memChangeDt: LocalDateTime? = null,

    @JvmField
    @ElementCollection(fetch = FetchType.EAGER)
    var roles: List<String>? = null

):UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles!!.stream()
            .map { role: String? -> SimpleGrantedAuthority(role) }
            .collect(Collectors.toList())
    }

    override fun getPassword(): String {
        return password!!
    }

    override fun getUsername(): String {
        return username!!
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

}