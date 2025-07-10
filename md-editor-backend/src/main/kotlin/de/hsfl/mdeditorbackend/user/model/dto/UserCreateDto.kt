package de.hsfl.mdeditorbackend.user.model.dto

import de.hsfl.mdeditorbackend.user.model.entity.Role
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class UserCreateDto(
    @field:NotBlank
    @field:Size(min = 3, max = 50)
    val username: String,

    @field:NotBlank
    @field:Size(min = 6, max = 100)
    val rawPassword: String,

    @field:NotNull
    val role: Role
)
