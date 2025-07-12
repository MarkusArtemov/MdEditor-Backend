package de.hsfl.mdeditorbackend.auth.service

import de.hsfl.mdeditorbackend.auth.model.dto.UserCreateDto
import de.hsfl.mdeditorbackend.auth.model.entity.Role
import de.hsfl.mdeditorbackend.auth.model.entity.User
import de.hsfl.mdeditorbackend.auth.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.Mockito.*
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserServiceImplTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @Mock
    private lateinit var eventPublisher: ApplicationEventPublisher

    @InjectMocks
    private lateinit var userService: UserServiceImpl

    private lateinit var dto: UserCreateDto
    private val rawPassword = "plainPass"
    private val encodedPassword = "encodedPass"

    @BeforeEach
    fun setup() {
        dto = UserCreateDto(
            username = "testuser",
            rawPassword = rawPassword,
            role = Role.USER
        )
    }

    @Test
    fun `create should save and return new user if username not taken`() {
        // Given
        `when`(userRepository.existsByUsername(dto.username)).thenReturn(false)
        `when`(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword)
        val savedUser = User(id = 1L, username = dto.username, password = encodedPassword, role = dto.role)
        `when`(userRepository.save(any(User::class.java))).thenReturn(savedUser)

        // When
        val result = userService.create(dto)

        // Then
        assertEquals(savedUser, result)
        verify(userRepository).existsByUsername(dto.username)
        verify(passwordEncoder).encode(rawPassword)
        verify(userRepository).save(org.mockito.kotlin.argThat { user ->
            user.username == dto.username &&
                    user.password == encodedPassword &&
                    user.role == dto.role
        })
    }

    @Test
    fun `create should throw IllegalArgumentException if username exists`() {
        // Given
        `when`(userRepository.existsByUsername(dto.username)).thenReturn(true)

        // When / Then
        val ex = assertThrows(IllegalArgumentException::class.java) {
            userService.create(dto)
        }
        assertTrue(ex.message!!.contains(dto.username))
        verify(userRepository).existsByUsername(dto.username)
        verify(userRepository, never()).save(any())
    }

    @Test
    fun `findById should return user if found`() {
        // Given
        val user = User(id = 2L, username = "foo", password = "bar", role = Role.USER)
        `when`(userRepository.findById(2L)).thenReturn(Optional.of(user))

        // When
        val result = userService.findById(2L)

        // Then
        assertEquals(user, result)
    }

    @Test
    fun `findById should throw NoSuchElementException if not found`() {
        // Given
        `when`(userRepository.findById(3L)).thenReturn(Optional.empty())

        // When / Then
        val ex = assertThrows(NoSuchElementException::class.java) {
            userService.findById(3L)
        }
        assertTrue(ex.message!!.contains("3"))
    }

    @Test
    fun `findByUsername should return user if exists`() {
        // Given
        `when`(userRepository.existsByUsername(dto.username)).thenReturn(true)
        val user = User(id = 4L, username = dto.username, password = "pwd", role = Role.USER)
        `when`(userRepository.findByUsername(dto.username)).thenReturn(user)

        // When
        val result = userService.findByUsername(dto.username)

        // Then
        assertEquals(user, result)
    }

    @Test
    fun `findByUsername should throw NoSuchElementException if not exists`() {
        // Given
        `when`(userRepository.existsByUsername(dto.username)).thenReturn(false)

        // When / Then
        assertThrows(NoSuchElementException::class.java) {
            userService.findByUsername(dto.username)
        }
    }
}
