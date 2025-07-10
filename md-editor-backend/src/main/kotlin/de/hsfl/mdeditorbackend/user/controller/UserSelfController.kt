package de.hsfl.mdeditorbackend.user.controller

import de.hsfl.mdeditorbackend.user.model.dto.ChangePasswordRequest
import de.hsfl.mdeditorbackend.user.model.dto.ChangeUsernameRequest
import de.hsfl.mdeditorbackend.user.service.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users/me")
class UserSelfController(
    private val userService: UserService
) {

    @PatchMapping("/username")
    fun changeUsername(
        @Valid @RequestBody req: ChangeUsernameRequest,
        authentication: Authentication
    ): ResponseEntity<Void> {
        val me = authentication.name
        userService.updateOwnUsername(me, req.newUsername)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/password")
    fun changePassword(
        @Valid @RequestBody req: ChangePasswordRequest,
        authentication: Authentication
    ): ResponseEntity<Void> {
        val me = authentication.name
        userService.updateOwnPassword(me, req.oldPassword, req.newPassword)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping
    fun deleteAccount(
        authentication: Authentication
    ): ResponseEntity<Void> {
        val me = authentication.name
        userService.deleteOwnAccount(me)
        return ResponseEntity.noContent().build()
    }
}
