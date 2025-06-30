package de.hsfl.mdeditorbackend.auth.service

import de.hsfl.mdeditorbackend.auth.model.entity.Role
import de.hsfl.mdeditorbackend.auth.model.entity.User
import de.hsfl.mdeditorbackend.auth.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.springframework.security.core.userdetails.UsernameNotFoundException

class CustomUserDetailsServiceTest {

    private val repo = mock<UserRepository>()
    private val service = CustomUserDetailsService(repo)

    @Test
    fun `loadUserByUsername returns UserDetails when user exists`() {
        val user = User(id = 1, username = "alice", password = "pw", role = Role.USER)
        given(repo.findByUsername("alice")).willReturn(user)

        val details = service.loadUserByUsername("alice")
        assertEquals("alice", details.username)
        assertEquals(1, details.authorities.size)
        assertTrue(details.authorities.any { it.authority == "ROLE_USER" })
    }

    @Test
    fun `loadUserByUsername throws when user not found`() {
        given(repo.findByUsername("bob")).willReturn(null)
        assertThrows<UsernameNotFoundException> {
            service.loadUserByUsername("bob")
        }
    }
}
