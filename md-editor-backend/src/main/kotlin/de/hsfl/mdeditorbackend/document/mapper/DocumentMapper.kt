package de.hsfl.mdeditorbackend.document.mapper

import de.hsfl.mdeditorbackend.document.exception.MissingCurrentVersionException
import de.hsfl.mdeditorbackend.document.model.dto.*
import de.hsfl.mdeditorbackend.document.model.entity.Document
import de.hsfl.mdeditorbackend.document.model.entity.DocumentVersion
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class DocumentMapper {

  fun toResponse(document: Document): DocumentResponse {
    val version = document.currentVersion
      ?: throw MissingCurrentVersionException(document.id)

    return DocumentResponse(
      id = document.id,
      title = document.title,
      content = version.content,
      versionNumber = version.versionNumber,
      updatedAt = document.updatedAt
    )
  }

  fun toSummary(version: DocumentVersion): DocumentVersionSummary =
    DocumentVersionSummary(
      id = version.id,
      versionNumber = version.versionNumber,
      authorId = version.authorId,
      createdAt = version.createdAt
    )

  fun toEntity(request: DocumentCreateRequest, ownerId: Long): Document =
    Document(
      title = request.title,
      ownerId = ownerId,
      currentVersion = null,
      createdAt = Instant.now(),
      updatedAt = Instant.now()
    )
}
