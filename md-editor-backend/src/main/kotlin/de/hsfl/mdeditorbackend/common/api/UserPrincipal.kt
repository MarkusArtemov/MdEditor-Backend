package de.hsfl.mdeditorbackend.common.api

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import java.security.Principal

// Adds user id to the Spring Security principal
data class UserPrincipal(
  val id: Long,
  private val username: String,
  private val password: String,
  private val roles: Collection<GrantedAuthority>
) : UserDetails, Principal {

  override fun getName(): String = username
  override fun getUsername(): String = username
  override fun getPassword(): String = password
  override fun getAuthorities(): Collection<GrantedAuthority> = roles
  override fun isAccountNonExpired() = true
  override fun isAccountNonLocked() = true
  override fun isCredentialsNonExpired() = true
  override fun isEnabled() = true
}

// Only expose used properties of UserPrincipal
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@AuthenticationPrincipal(expression = "id")
annotation class CurrentUserId

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@AuthenticationPrincipal(expression = "username")
annotation class CurrentUsername
