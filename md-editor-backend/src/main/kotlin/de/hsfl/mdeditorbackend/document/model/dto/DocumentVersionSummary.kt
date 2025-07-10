package de.hsfl.mdeditorbackend.document.model.dto

import java.time.Instant

data class DocumentVersionSummary(
  val id: Long,
  val versionNumber: Int,
  val author: String,
  val createdAt: Instant
)
