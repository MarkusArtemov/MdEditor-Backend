package de.hsfl.mdeditorbackend.auth.controller

import de.hsfl.mdeditorbackend.auth.model.dto.ChangeRoleRequest
import de.hsfl.mdeditorbackend.auth.service.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "AdminUsers")
// shared errors
@ApiResponses(
  value = [
    ApiResponse(
      responseCode = "404",
      description = "User not found",
      content = [Content(schema = Schema(ref = "#/components/schemas/ApiError"))]
    )
  ]
)
@RestController
@RequestMapping("/admin/users")
class AdminUserController(
  private val userService: UserService
) {

  @Operation(summary = "Change role")
  @ApiResponse(responseCode = "204", description = "Updated")
  @PatchMapping("/{id}/role")
  fun changeRole(
    @PathVariable id: Long,
    @Valid @RequestBody req: ChangeRoleRequest
  ): ResponseEntity<Void> {
    userService.updateRole(id, req.newRole)
    return ResponseEntity.noContent().build()
  }

  @Operation(summary = "Delete user")
  @ApiResponse(responseCode = "204", description = "Deleted")
  @DeleteMapping("/{id}")
  fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
    userService.deleteUser(id)
    return ResponseEntity.noContent().build()
  }
}
