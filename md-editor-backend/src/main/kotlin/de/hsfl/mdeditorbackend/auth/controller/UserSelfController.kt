package de.hsfl.mdeditorbackend.auth.controller

import de.hsfl.mdeditorbackend.common.security.UserPrincipal
import de.hsfl.mdeditorbackend.auth.model.dto.ChangePasswordRequest
import de.hsfl.mdeditorbackend.auth.model.dto.ChangeUsernameRequest
import de.hsfl.mdeditorbackend.auth.service.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users/me")
class UserSelfController(
    private val userService: UserService
) {

    @PatchMapping("/username")
    fun changeUsername(
        @Valid @RequestBody req: ChangeUsernameRequest,
        @AuthenticationPrincipal user: UserPrincipal
    ): ResponseEntity<Void> {
        val me = user.username
        userService.updateOwnUsername(me, req.newUsername)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/password")
    fun changePassword(
        @Valid @RequestBody req: ChangePasswordRequest,
        @AuthenticationPrincipal user: UserPrincipal
    ): ResponseEntity<Void> {
        val me = user.username
        userService.updateOwnPassword(me, req.oldPassword, req.newPassword)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping
    fun deleteAccount(
        @AuthenticationPrincipal user: UserPrincipal
    ): ResponseEntity<Void> {
        val me = user.username
        userService.deleteOwnAccount(me)
        return ResponseEntity.noContent().build()
    }
}
