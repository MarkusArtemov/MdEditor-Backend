package de.hsfl.mdeditorbackend.auth.config

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class JwtTokenProviderTest {

    private lateinit var provider: JwtTokenProvider

    // Dummy UserDetailsService, nur um getAuthentication testen zu können
    private val uds = object : UserDetailsService {
        override fun loadUserByUsername(username: String) =
            User.withUsername(username)
                .password("irrelevant")
                .roles("USER")
                .build()
    }

    @BeforeEach
    fun setUp() {
        // Secret muss mindestens 32 Bytes lang sein
        provider = JwtTokenProvider(
            secret = "01234567890123456789012345678901",
            validityInMs = 3600_000,
            userDetailsService = uds
        )
    }

    @Test
    fun `token contains correct username and is valid`() {
        val token = provider.createToken("carol", listOf("ROLE_USER"))
        assertTrue(provider.validateToken(token))
        assertEquals("carol", provider.getUsername(token))
    }

    @Test
    fun `getAuthentication returns Authentication with correct principal`() {
        val token = provider.createToken("dave", listOf("ROLE_USER"))
        val auth = provider.getAuthentication(token)
        assertEquals("dave", auth.name)
        assertTrue(auth.authorities.any { it.authority == "ROLE_USER" })
    }

    @Test
    fun `invalid token is rejected`() {
        assertFalse(provider.validateToken("totally.invalid.token"))
    }
}
