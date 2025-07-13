package de.hsfl.mdeditorbackend.auth.service

import de.hsfl.mdeditorbackend.auth.exception.*
import de.hsfl.mdeditorbackend.auth.model.dto.UserCreateDto
import de.hsfl.mdeditorbackend.auth.model.entity.Role
import de.hsfl.mdeditorbackend.auth.model.entity.User
import de.hsfl.mdeditorbackend.auth.repository.UserRepository
import de.hsfl.mdeditorbackend.common.api.UserDeleteRequested
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserServiceImpl(
  private val userRepository: UserRepository,
  private val passwordEncoder: PasswordEncoder,
  private val eventPublisher: ApplicationEventPublisher
) : UserService {

  override fun create(dto: UserCreateDto): User {
    if (userRepository.existsByUsername(dto.username)) throw UsernameTakenException(dto.username)
    val user = User(
      username = dto.username,
      password = passwordEncoder.encode(dto.rawPassword),
      role = dto.role
    )
    return userRepository.save(user)
  }

  override fun findById(id: Long): User =
    userRepository.findById(id).orElseThrow { UserNotFoundException(id) }

  override fun findByUsername(username: String): User =
    userRepository.findByUsername(username) ?: throw UserNotFoundException(username)

  override fun updateUsername(id: Long, newUsername: String) {
    val user = findById(id)
    if (userRepository.existsByUsername(newUsername)) throw UsernameTakenException(newUsername)
    user.username = newUsername
    userRepository.save(user)
  }

  override fun updatePassword(id: Long, oldPassword: String, newPassword: String) {
    val user = findById(id)
    if (!passwordEncoder.matches(oldPassword, user.password)) throw WrongPasswordException()
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
    if (!userRepository.existsById(id)) throw UserNotFoundException(id)
    eventPublisher.publishEvent(UserDeleteRequested(id))
    userRepository.deleteById(id)
  }

  override fun updateOwnUsername(currentUsername: String, newUsername: String) {
    val user = findByUsername(currentUsername)
    if (userRepository.existsByUsername(newUsername)) throw UsernameTakenException(newUsername)
    user.username = newUsername
    userRepository.save(user)
  }

  override fun updateOwnPassword(currentUsername: String, oldPassword: String, newPassword: String) {
    val user = findByUsername(currentUsername)
    if (!passwordEncoder.matches(oldPassword, user.password)) throw WrongPasswordException()
    user.password = passwordEncoder.encode(newPassword)
    userRepository.save(user)
  }

  @Transactional
  override fun deleteOwnAccount(currentUsername: String) {
    val user = userRepository.findByUsername(currentUsername) ?: throw UserNotFoundException(currentUsername)
    eventPublisher.publishEvent(UserDeleteRequested(user.id))
    userRepository.deleteById(user.id)
  }
}
