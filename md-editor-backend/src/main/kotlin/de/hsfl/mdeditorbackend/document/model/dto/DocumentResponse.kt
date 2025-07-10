package de.hsfl.mdeditorbackend.document.model.dto

import java.time.Instant

data class DocumentResponse(
    val id: Long,
    val title: String,
    val content: String,
    val versionNumber: Int,
    val updatedAt: Instant
)
