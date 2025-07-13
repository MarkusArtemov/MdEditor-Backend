package de.hsfl.mdeditorbackend.common.api

import org.springframework.http.HttpStatus


abstract class BusinessException(
  val status: HttpStatus,
  val logMessage: String,
  val clientMessage: String,
  val errorCode : String
) : RuntimeException(logMessage)
