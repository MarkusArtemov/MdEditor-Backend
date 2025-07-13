package de.hsfl.mdeditorbackend.auth.controller

import de.hsfl.mdeditorbackend.auth.config.JwtTokenProvider
import de.hsfl.mdeditorbackend.auth.model.dto.AuthResponse
import de.hsfl.mdeditorbackend.auth.model.dto.LoginRequest
import de.hsfl.mdeditorbackend.auth.model.dto.UserCreateDto
import de.hsfl.mdeditorbackend.auth.model.entity.Role
import de.hsfl.mdeditorbackend.auth.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "Auth")
// shared errors
@ApiResponses(
  value = [
    ApiResponse(
      responseCode = "400",
      description = "Bad request",
      content = [Content(schema = Schema(ref = "#/components/schemas/ApiError"))]
    )
  ]
)
@RestController
@RequestMapping("/auth")
class AuthController(
  private val authManager: AuthenticationManager,
  private val jwtProvider: JwtTokenProvider,
  private val userService: UserService
) {

  @Operation(summary = "Login")
  @ApiResponses(
    value = [
      ApiResponse(responseCode = "200", description = "OK"),
      ApiResponse(
        responseCode = "401",
        description = "Unauthorized",
        content = [Content(schema = Schema(ref = "#/components/schemas/ApiError"))]
      )
    ]
  )
  @PostMapping("/login")
  fun login(@RequestBody req: LoginRequest): ResponseEntity<AuthResponse> {
    val auth = authManager.authenticate(
      UsernamePasswordAuthenticationToken(req.username, req.password)
    )
    val token = jwtProvider.createToken(
      auth.name,
      auth.authorities.map { it.authority }
    )
    return ResponseEntity.ok(AuthResponse(token))
  }

  @Operation(summary = "Register")
  @ApiResponses(
    value = [
      ApiResponse(responseCode = "201", description = "Created"),
      ApiResponse(
        responseCode = "409",
        description = "Username taken",
        content = [Content(schema = Schema(ref = "#/components/schemas/ApiError"))]
      )
    ]
  )
  @PostMapping("/register")
  fun register(@RequestBody dto: LoginRequest): ResponseEntity<Void> {
    userService.create(UserCreateDto(dto.username, dto.password, Role.USER))
    return ResponseEntity.status(HttpStatus.CREATED).build()
  }
}
