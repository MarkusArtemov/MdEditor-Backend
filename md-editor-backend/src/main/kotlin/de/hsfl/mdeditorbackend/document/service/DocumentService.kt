package de.hsfl.mdeditorbackend.document.service

import de.hsfl.mdeditorbackend.document.model.dto.*

interface DocumentService {
  fun create(owner: String, req: DocumentCreateRequest): DocumentResponse
  fun listOwn(owner: String): List<DocumentResponse>
  fun get(owner: String, id: Long): DocumentResponse
  fun update(owner: String, id: Long, req: DocumentUpdateRequest): DocumentResponse
  fun delete(owner: String, id: Long)

  fun listVersions(owner: String, docId: Long): List<DocumentVersionSummary>
  fun getVersion(owner: String, docId: Long, versionId: Long): DocumentResponse
  fun restoreVersion(owner: String, docId: Long, versionId: Long)
}
