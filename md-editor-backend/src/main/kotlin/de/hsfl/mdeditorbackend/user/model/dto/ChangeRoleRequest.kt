package de.hsfl.mdeditorbackend.user.model.dto

import de.hsfl.mdeditorbackend.user.model.entity.Role
import jakarta.validation.constraints.NotNull

data class ChangeRoleRequest(
    @field:NotNull
    val newRole: Role
)