package de.hsfl.mdeditorbackend.user.model.dto

import jakarta.validation.constraints.NotBlank

data class ChangeUsernameRequest(
    @field:NotBlank
    val newUsername: String
)