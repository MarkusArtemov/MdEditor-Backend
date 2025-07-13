package de.hsfl.mdeditorbackend.document.repository

import de.hsfl.mdeditorbackend.document.model.entity.DocumentVersion
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface DocumentVersionRepository : JpaRepository<DocumentVersion, Long> {
  fun findAllByDocumentIdOrderByVersionNumberDesc(documentId: Long): List<DocumentVersion>
  fun findByDocumentIdAndVersionNumber(docId: Long, versionNumber: Long): Optional<DocumentVersion>
}
