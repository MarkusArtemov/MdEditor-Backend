package de.hsfl.mdeditorbackend.document.repository

import de.hsfl.mdeditorbackend.document.model.entity.Document
import org.springframework.data.jpa.repository.JpaRepository

interface DocumentRepository : JpaRepository<Document, Long> {
  fun findAllByOwner(owner: String): List<Document>
}
