package de.hsfl.mdeditorbackend.common.error

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ApiExceptionHandler {

  private val logger = LoggerFactory.getLogger(ApiExceptionHandler::class.java)

  @ExceptionHandler(BusinessException::class)
  fun handleBusiness(be: BusinessException): ResponseEntity<ApiError> {
    logger.warn("BusinessException: {}", be.message)
    return error(be.status, be.message)
  }

  @ExceptionHandler(Exception::class)
  fun handleUnexpected(ex: Exception): ResponseEntity<ApiError> {
    logger.error("Unexpected error occurred", ex)
    return error(INTERNAL_SERVER_ERROR, "Unexpected error")
  }

  private fun error(status: org.springframework.http.HttpStatus, msg: String?) =
    ResponseEntity.status(status)
      .body(ApiError(status.value(), status.reasonPhrase, msg))
}
