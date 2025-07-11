package de.hsfl.mdeditorbackend.auth.controller

import de.hsfl.mdeditorbackend.auth.model.dto.ChangeRoleRequest
import de.hsfl.mdeditorbackend.auth.service.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/users")
class AdminUserController(
    private val userService: UserService
) {

    @PatchMapping("/{id}/role")
    fun changeRole(
        @PathVariable id: Long,
        @Valid @RequestBody req: ChangeRoleRequest
    ): ResponseEntity<Void> {
        userService.updateRole(id, req.newRole)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        userService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }
}
