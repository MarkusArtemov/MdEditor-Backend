package de.hsfl.mdeditorbackend.document.exception

import de.hsfl.mdeditorbackend.common.api.BusinessException
import org.springframework.http.HttpStatus

class DocumentNotFoundException(docId: Long) : BusinessException(
  HttpStatus.NOT_FOUND,
  logMessage = "Doc $docId not found",
  clientMessage = "Document not found",
  errorCode = "DOCUMENT_NOT_FOUND"
)

class VersionNotFoundException(versionId: Long) : BusinessException(
  HttpStatus.NOT_FOUND,
  logMessage = "Version $versionId not found",
  clientMessage = "Version not found",
  errorCode = "VERSION_NOT_FOUND"
)

class MissingCurrentVersionException(docId: Long) : BusinessException(
  HttpStatus.CONFLICT,
  logMessage = "Doc $docId has no current version",
  clientMessage = "Document has no current version",
  errorCode = "MISSING_CURRENT_VERSION"
)

class NotDocumentOwnerException(docId: Long, userId: Long) : BusinessException(
  HttpStatus.NOT_FOUND,
  logMessage = "User $userId tried to access doc $docId",
  clientMessage = "Document not found",
  errorCode = "ACCESS_DENIED_DOCUMENT"
)

class EmptyContentException : BusinessException(
  HttpStatus.BAD_REQUEST,
  logMessage = "Empty content submitted",
  clientMessage = "Content must have at least 1 character",
  errorCode = "EMPTY_CONTENT"
)

class InvalidTitleException : BusinessException(
  HttpStatus.BAD_REQUEST,
  logMessage = "Title is empty or too long",
  clientMessage = "Title must be 1-255 characters",
  errorCode = "INVALID_TITLE"
)
