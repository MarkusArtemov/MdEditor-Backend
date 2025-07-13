package de.hsfl.mdeditorbackend.auth.controller

import de.hsfl.mdeditorbackend.common.api.UserPrincipal
import de.hsfl.mdeditorbackend.auth.model.dto.ChangePasswordRequest
import de.hsfl.mdeditorbackend.auth.model.dto.ChangeUsernameRequest
import de.hsfl.mdeditorbackend.auth.service.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "User Account")
// shared errors
@ApiResponses(
  value = [
    ApiResponse(
      responseCode = "400",
      description = "Bad request - wrong old password",
      content = [Content(schema = Schema(ref = "#/components/schemas/ApiError"))]
    ),
    ApiResponse(
      responseCode = "404",
      description = "User not found",
      content = [Content(schema = Schema(ref = "#/components/schemas/ApiError"))]
    ),
    ApiResponse(
      responseCode = "409",
      description = "Conflict - Username already taken",
      content = [Content(schema = Schema(ref = "#/components/schemas/ApiError"))]
    )
  ]
)

@RestController
@RequestMapping("/users/me")
class UserSelfController(
    private val userService: UserService
) {
    @Operation(summary = "Change username")
    @ApiResponse(responseCode = "204", description = "Updated")
    @PatchMapping("/username")
    fun changeUsername(
        @Valid @RequestBody req: ChangeUsernameRequest,
        @AuthenticationPrincipal user: UserPrincipal
    ): ResponseEntity<Void> {
        val me = user.username
        userService.updateOwnUsername(me, req.newUsername)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Change password")
    @ApiResponse(responseCode = "204", description = "Updated")
    @PatchMapping("/password")
    fun changePassword(
        @Valid @RequestBody req: ChangePasswordRequest,
        @AuthenticationPrincipal user: UserPrincipal
    ): ResponseEntity<Void> {
        val me = user.username
        userService.updateOwnPassword(me, req.oldPassword, req.newPassword)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Delete account")
    @ApiResponse(responseCode = "204", description = "Deleted")
    @DeleteMapping
    fun deleteAccount(
        @AuthenticationPrincipal user: UserPrincipal
    ): ResponseEntity<Void> {
        val me = user.username
        userService.deleteOwnAccount(me)
        return ResponseEntity.noContent().build()
    }
}
