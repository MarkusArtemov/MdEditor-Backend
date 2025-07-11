package de.hsfl.mdeditorbackend.auth.model.dto

import de.hsfl.mdeditorbackend.auth.model.entity.Role
import jakarta.validation.constraints.NotNull

data class ChangeRoleRequest(
    @field:NotNull
    val newRole: Role
)