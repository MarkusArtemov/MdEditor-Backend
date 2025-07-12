package de.hsfl.mdeditorbackend.auth.service

import de.hsfl.mdeditorbackend.auth.model.dto.UserCreateDto
import de.hsfl.mdeditorbackend.auth.model.entity.Role
import de.hsfl.mdeditorbackend.auth.model.entity.User

interface UserService {
    fun create(dto: UserCreateDto): User
    fun findById(id: Long): User
    fun findByUsername(username: String): User

    fun updateUsername(id: Long, newUsername: String)
    fun updatePassword(id: Long, oldPassword: String, newPassword: String)
    fun updateRole(id: Long, newRole: Role)
    fun deleteUser(id: Long)

    fun updateOwnUsername(currentUsername: String, newUsername: String)
    fun updateOwnPassword(currentUsername: String, oldPassword: String, newPassword: String)
    fun deleteOwnAccount(id: Long)
}
