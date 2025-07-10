package de.hsfl.mdeditorbackend.user.controller

import de.hsfl.mdeditorbackend.user.model.dto.ChangeRoleRequest
import de.hsfl.mdeditorbackend.user.service.UserService
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
