package de.hsfl.mdeditorbackend.auth.service

import de.hsfl.mdeditorbackend.common.security.UserPrincipal
import org.springframework.security.core.authority.SimpleGrantedAuthority
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
        val authorities = listOf(SimpleGrantedAuthority("ROLE_${user.role.name}"))

        return UserPrincipal(
          id = user.id,
          username = user.username,
          password = user.password,
          roles = authorities
        )
    }
}
