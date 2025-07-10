package de.hsfl.mdeditorbackend.user.service

import de.hsfl.mdeditorbackend.user.model.dto.UserCreateDto
import de.hsfl.mdeditorbackend.user.model.entity.Role
import de.hsfl.mdeditorbackend.user.model.entity.User

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
    fun deleteOwnAccount(currentUsername: String)
}
