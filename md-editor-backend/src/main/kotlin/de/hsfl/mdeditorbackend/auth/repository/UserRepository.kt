package de.hsfl.mdeditorbackend.auth.repository

import de.hsfl.mdeditorbackend.auth.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}
