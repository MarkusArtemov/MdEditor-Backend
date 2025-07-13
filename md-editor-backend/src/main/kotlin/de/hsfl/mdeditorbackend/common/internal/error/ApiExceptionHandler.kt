package de.hsfl.mdeditorbackend.common.internal.error

import de.hsfl.mdeditorbackend.common.api.ApiError
import de.hsfl.mdeditorbackend.common.api.BusinessException
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
    log.warn(be.logMessage)
    return error(be.status, be.clientMessage, be.errorCode)
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

    val apiError = ApiError(
      status = httpStatus.value(),
      error = httpStatus.reasonPhrase,
      message = ex.message
    )
    return ResponseEntity
      .status(httpStatus)
      .headers(headers)
      .body(apiError)
  }


  @ExceptionHandler(Exception::class)
  fun handleUnexpected(ex: Exception): ResponseEntity<ApiError> {
    log.error("Unexpected error occurred", ex)
    return error(INTERNAL_SERVER_ERROR, "Unexpected error")
  }

  private fun error(status: HttpStatus, msg: String?, code: String? = null) =
    ResponseEntity.status(status).body(ApiError(status.value(), status.reasonPhrase, msg, code))
}
