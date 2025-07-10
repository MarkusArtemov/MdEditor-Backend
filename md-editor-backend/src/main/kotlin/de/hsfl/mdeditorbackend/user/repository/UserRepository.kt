package de.hsfl.mdeditorbackend.user.repository

import de.hsfl.mdeditorbackend.user.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun existsByUsername(username: String): Boolean
}