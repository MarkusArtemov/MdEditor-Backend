package de.hsfl.mdeditorbackend.auth.service

import de.hsfl.mdeditorbackend.auth.model.dto.UserCreateDto
import de.hsfl.mdeditorbackend.auth.model.entity.User
import de.hsfl.mdeditorbackend.auth.model.entity.Role
import de.hsfl.mdeditorbackend.auth.repository.UserRepository
import de.hsfl.mdeditorbackend.common.event.UserDeleteRequested
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.context.ApplicationEventPublisher
import org.springframework.transaction.annotation.Transactional


@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val eventPublisher: ApplicationEventPublisher
) : UserService {
    override fun create(dto: UserCreateDto): User {
        if (userRepository.existsByUsername(dto.username)) {
            throw IllegalArgumentException("Username $dto.username already taken")
        }
        val user = User(
            username = dto.username,
            password = passwordEncoder.encode(dto.rawPassword),
            role = dto.role
        )
        return userRepository.save(user)
    }

    override fun findById(id: Long): User {
        return userRepository.findById(id)
            .orElseThrow { NoSuchElementException("User $id not found") }
    }

    override fun findByUsername(username: String): User {
        if (!userRepository.existsByUsername(username)) {
            throw NoSuchElementException("User $username not found")
        }
        return userRepository.findByUsername(username)!!
    }

    override fun updateUsername(id: Long, newUsername: String) {
        val user = findById(id)
        if (userRepository.existsByUsername(newUsername)) {
            throw IllegalArgumentException("Username $newUsername bereits vergeben")
        }
        user.username = newUsername
        userRepository.save(user)
    }

    override fun updatePassword(id: Long, oldPassword: String, newPassword: String) {
        val user = findById(id)
        if (!passwordEncoder.matches(oldPassword, user.password)) {
            throw IllegalArgumentException("Altes Passwort falsch")
        }
        user.password = passwordEncoder.encode(newPassword)
        userRepository.save(user)
    }

    override fun updateRole(id: Long, newRole: Role) {
        val user = findById(id)
        user.role = newRole
        userRepository.save(user)
    }

    @Transactional
    override fun deleteUser(id: Long) {
        if (!userRepository.existsById(id)) {
            throw NoSuchElementException("User $id nicht gefunden")
        }
        eventPublisher.publishEvent(UserDeleteRequested(id))
        userRepository.deleteById(id)
    }

    override fun updateOwnUsername(currentUsername: String, newUsername: String) {
        val user = findByUsername(currentUsername)
        if (userRepository.existsByUsername(newUsername)) {
            throw IllegalArgumentException("Username $newUsername bereits vergeben")
        }
        user.username = newUsername
        userRepository.save(user)
    }

    override fun updateOwnPassword(currentUsername: String, oldPassword: String, newPassword: String) {
        val user = findByUsername(currentUsername)
        if (!passwordEncoder.matches(oldPassword, user.password)) {
            throw IllegalArgumentException("Altes Passwort falsch")
        }
        user.password = passwordEncoder.encode(newPassword)
        userRepository.save(user)
    }

    @Transactional
    override fun deleteOwnAccount(id: Long) {
      deleteUser(id)
    }
}
