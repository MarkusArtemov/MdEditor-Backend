package de.hsfl.mdeditorbackend.document.exception

import de.hsfl.mdeditorbackend.common.error.BusinessException
import org.springframework.http.HttpStatus

class DocumentNotFoundException(id: Long) :
  BusinessException(HttpStatus.NOT_FOUND, "Document $id not found")

class VersionNotFoundException(id: Long) :
  BusinessException(HttpStatus.NOT_FOUND, "Version $id not found")

class NotDocumentOwnerException(docId: Long) :
  BusinessException(HttpStatus.FORBIDDEN, "You are not the owner of document $docId")

class EmptyContentException :
  BusinessException(HttpStatus.BAD_REQUEST, "Content must not be empty")

class InvalidTitleException :
  BusinessException(HttpStatus.BAD_REQUEST, "Title is empty or exceeds 255 characters")

