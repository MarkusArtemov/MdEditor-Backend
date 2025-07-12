package de.hsfl.mdeditorbackend.document.service

import de.hsfl.mdeditorbackend.document.model.dto.*

interface DocumentService {
  fun create(ownerId: Long, req: DocumentCreateRequest): DocumentResponse
  fun listOwn(ownerId: Long): List<DocumentResponse>
  fun get(ownerId: Long, id: Long): DocumentResponse
  fun update(ownerId: Long, id: Long, req: DocumentUpdateRequest): DocumentResponse
  fun delete(ownerId: Long, id: Long)

  fun listVersions(ownerId: Long, docId: Long): List<DocumentVersionSummary>
  fun getVersion(ownerId: Long, docId: Long, versionId: Long): DocumentResponse
  fun restoreVersion(ownerId: Long, docId: Long, versionId: Long) : DocumentResponse
}
