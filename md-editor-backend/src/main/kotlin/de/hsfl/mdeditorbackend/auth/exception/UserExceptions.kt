package de.hsfl.mdeditorbackend.auth.exception

import de.hsfl.mdeditorbackend.common.api.BusinessException
import org.springframework.http.HttpStatus

class UserNotFoundException : BusinessException {
  constructor(id: Long) : super(
    HttpStatus.NOT_FOUND,
    logMessage = "User $id not found",
    clientMessage = "User not found",
    errorCode = "USER_NOT_FOUND"
  )

  constructor(username: String) : super(
    HttpStatus.NOT_FOUND,
    logMessage = "User $username not found",
    clientMessage = "User not found",
    errorCode = "USER_NOT_FOUND"
  )
}


class UsernameTakenException(username: String) : BusinessException(
  HttpStatus.CONFLICT,
  logMessage = "Username $username already taken",
  clientMessage = "Username already taken",
  errorCode = "USERNAME_TAKEN"
)

class WrongPasswordException : BusinessException(
  HttpStatus.BAD_REQUEST,
  logMessage = "Wrong old password",
  clientMessage = "Old password is wrng",
  errorCode = "WRONG_PASSWORD"
)
