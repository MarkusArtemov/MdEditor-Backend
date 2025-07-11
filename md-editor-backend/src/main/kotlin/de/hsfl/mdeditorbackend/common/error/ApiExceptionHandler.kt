package de.hsfl.mdeditorbackend.common.error

import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ApiExceptionHandler : ResponseEntityExceptionHandler() {

  private val log = LoggerFactory.getLogger(ApiExceptionHandler::class.java)

  @ExceptionHandler(BusinessException::class)
  fun handleBusiness(be: BusinessException): ResponseEntity<ApiError> {
    log.warn("BusinessException: {}", be.message)
    return error(be.status, be.message)
  }

  override fun handleExceptionInternal(
    ex: Exception,
    body: Any?,
    headers: HttpHeaders,
    statusCode: HttpStatusCode,
    request: WebRequest
  ): ResponseEntity<Any> {
    log.debug("Spring error: {}", ex.message)
    val httpStatus = HttpStatus.valueOf(statusCode.value())
    return error(httpStatus, ex.message) as ResponseEntity<Any>
  }

  @ExceptionHandler(Exception::class)
  fun handleUnexpected(ex: Exception): ResponseEntity<ApiError> {
    log.error("Unexpected error occurred", ex)
    return error(INTERNAL_SERVER_ERROR, "Unexpected error")
  }

  private fun error(status: HttpStatus, msg: String?) =
    ResponseEntity.status(status).body(ApiError(status.value(), status.reasonPhrase, msg))
}
