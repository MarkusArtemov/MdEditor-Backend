package de.hsfl.mdeditorbackend.document.service

import de.hsfl.mdeditorbackend.document.model.dto.DocumentResponse
import de.hsfl.mdeditorbackend.document.model.dto.DocumentVersionSummary
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface DocumentAdminService {
  fun listAll(pageable: Pageable, ownerId: Long? = null, title: String? = null): Page<DocumentResponse>
  fun delete(documentId: Long)
  fun getDocumentHistory(documentId: Long): List<DocumentVersionSummary>
}
