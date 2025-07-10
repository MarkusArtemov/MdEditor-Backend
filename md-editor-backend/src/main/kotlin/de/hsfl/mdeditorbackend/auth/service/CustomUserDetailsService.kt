package de.hsfl.mdeditorbackend.auth.service

import de.hsfl.mdeditorbackend.user.service.UserServiceImpl
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userService: UserServiceImpl
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = try {
            userService.findByUsername(username)
        } catch (e: NoSuchElementException) {
            throw UsernameNotFoundException(e.message, e)
        }
        return User(
            user.username,
            user.password,
            listOf(SimpleGrantedAuthority("ROLE_${user.role.name}"))
        )
    }
}
