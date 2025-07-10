package de.hsfl.mdeditorbackend.auth.controller


import de.hsfl.mdeditorbackend.auth.config.JwtTokenProvider
import de.hsfl.mdeditorbackend.auth.model.dto.AuthResponse
import de.hsfl.mdeditorbackend.auth.model.dto.LoginRequest
import de.hsfl.mdeditorbackend.user.model.dto.UserCreateDto
import de.hsfl.mdeditorbackend.user.model.entity.Role
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import de.hsfl.mdeditorbackend.user.service.UserServiceImpl


@RestController
@RequestMapping("/auth")
class AuthController(
    private val authManager: AuthenticationManager,
    private val jwtProvider: JwtTokenProvider,
    private val userService: UserServiceImpl
) {

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
    fun register(@RequestBody dto: LoginRequest): ResponseEntity<Void> {
        userService.create(UserCreateDto(dto.username, dto.password, Role.USER))
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}
