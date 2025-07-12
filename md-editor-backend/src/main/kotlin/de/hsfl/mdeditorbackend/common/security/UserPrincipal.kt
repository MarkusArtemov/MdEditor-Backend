package de.hsfl.mdeditorbackend.common.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails

// Adds user id to the Spring Security principal
data class UserPrincipal(
  val id: Long,
  private val username: String,
  private val password: String,
  private val roles: Collection<GrantedAuthority>
) : UserDetails {

  override fun getUsername()  = username
  override fun getPassword()  = password
  override fun getAuthorities() = roles
  override fun isAccountNonExpired()     = true
  override fun isAccountNonLocked()      = true
  override fun isCredentialsNonExpired() = true
  override fun isEnabled()               = true
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
