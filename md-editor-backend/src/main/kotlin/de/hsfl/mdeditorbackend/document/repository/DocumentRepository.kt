package de.hsfl.mdeditorbackend.document.repository

import de.hsfl.mdeditorbackend.document.model.entity.Document
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface DocumentRepository : JpaRepository<Document, Long> {
  fun findAllByOwnerId(ownerId: Long): List<Document>
  fun findAllByOwnerId(ownerId: Long, pageable: Pageable): Page<Document>
  fun deleteAllByOwnerId(ownerId: Long)
  fun findAllByTitleContainingIgnoreCase(title: String, pageable: Pageable): Page<Document>
  fun findAllByOwnerIdAndTitleContainingIgnoreCase(ownerId: Long, title: String, pageable: Pageable): Page<Document>
}
