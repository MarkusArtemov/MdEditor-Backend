package de.hsfl.mdeditorbackend.auth.model.dto

data class LoginRequest(
    val username: String,
    val password: String
)