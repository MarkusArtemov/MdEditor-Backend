package de.hsfl.mdeditorbackend.common.error

import org.springframework.http.HttpStatus

abstract class BusinessException(
  val status: HttpStatus,
  override val message: String
) : RuntimeException(message)
