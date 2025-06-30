package de.hsfl.mdeditorbackend.auth.controller


import de.hsfl.mdeditorbackend.auth.config.JwtTokenProvider
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import de.hsfl.mdeditorbackend.auth.model.entity.User
import de.hsfl.mdeditorbackend.auth.model.entity.Role
import de.hsfl.mdeditorbackend.auth.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder


@RestController
@RequestMapping("/auth")
class AuthController(
    private val authManager: AuthenticationManager,
    private val jwtProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository
) {

    data class LoginRequest(val username: String, val password: String)
    data class AuthResponse(val token: String)

    @PostMapping("/login")
    fun login(@RequestBody req: LoginRequest): ResponseEntity<AuthResponse> {
        val authToken = UsernamePasswordAuthenticationToken(req.username, req.password)
        val auth = authManager.authenticate(authToken)
        val token = jwtProvider.createToken(
            auth.name,
            auth.authorities.map { it.authority }
        )
        return ResponseEntity.ok(AuthResponse(token))
    }

    @PostMapping("/register")
    fun register(@RequestBody req: LoginRequest): ResponseEntity<Void> {
        if (userRepository.findByUsername(req.username) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
        val user = User(
            username = req.username,
            password = passwordEncoder.encode(req.password),
            role = Role.USER
        )
        userRepository.save(user)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}
