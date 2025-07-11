package de.hsfl.mdeditorbackend.document.model.dto

import jakarta.validation.constraints.Size

/* Make fields optional for partial updates */

data class DocumentUpdateRequest(
  @field:Size(min = 1, max = 255)
  val title: String? = null,

  val content: String? = null
)
