package de.hsfl.mdeditorbackend.common.security
import org.springframework.security.core.Authentication

fun Authentication.userId(): Long =
  (principal as UserPrincipal).id
