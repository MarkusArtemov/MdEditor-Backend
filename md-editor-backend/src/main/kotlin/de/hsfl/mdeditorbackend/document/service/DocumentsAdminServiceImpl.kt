package de.hsfl.mdeditorbackend.document.service

import de.hsfl.mdeditorbackend.document.exception.DocumentNotFoundException
import de.hsfl.mdeditorbackend.document.mapper.DocumentMapper
import de.hsfl.mdeditorbackend.document.model.dto.DocumentResponse
import de.hsfl.mdeditorbackend.document.model.dto.DocumentVersionSummary
import de.hsfl.mdeditorbackend.document.model.entity.Document
import de.hsfl.mdeditorbackend.document.repository.DocumentRepository
import de.hsfl.mdeditorbackend.document.repository.DocumentVersionRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DocumentAdminServiceImpl(
  private val documentRepository: DocumentRepository,
  private val documentVersionRepository: DocumentVersionRepository,
  private val mapper: DocumentMapper
) : DocumentAdminService {

  @Transactional(readOnly = true)
  override fun listAll(pageable: Pageable, ownerId: Long?, title: String?): Page<DocumentResponse> {
    val page: Page<Document> = when {
      ownerId != null && title != null ->
        documentRepository.findAllByOwnerIdAndTitleContainingIgnoreCase(ownerId, title, pageable)
      ownerId != null ->
        documentRepository.findAllByOwnerId(ownerId, pageable)
      title != null ->
        documentRepository.findAllByTitleContainingIgnoreCase(title, pageable)
      else ->
        documentRepository.findAll(pageable)
    }
    return page.map { doc -> mapper.toResponse(doc) }
  }

  override fun delete(documentId: Long) {
    if (!documentRepository.existsById(documentId)) throw DocumentNotFoundException(documentId)
    documentRepository.deleteById(documentId)
  }

  @Transactional(readOnly = true)
  override fun getDocumentHistory(documentId: Long): List<DocumentVersionSummary> =
    documentVersionRepository
      .findAllByDocumentIdOrderByVersionNumberDesc(documentId)
      .map { version -> mapper.toSummary(version) }
}
