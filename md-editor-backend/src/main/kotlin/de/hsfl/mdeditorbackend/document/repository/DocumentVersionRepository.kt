package de.hsfl.mdeditorbackend.document.repository

import de.hsfl.mdeditorbackend.document.model.entity.DocumentVersion
import org.springframework.data.jpa.repository.JpaRepository

interface DocumentVersionRepository : JpaRepository<DocumentVersion, Long> {
  fun findAllByDocumentIdOrderByVersionNumberDesc(documentId: Long): List<DocumentVersion>
}
