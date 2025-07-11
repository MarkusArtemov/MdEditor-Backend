package de.hsfl.mdeditorbackend.document.model.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class DocumentCreateRequest(
  @field:NotBlank
  @field:Size(min = 1, max = 255)
  val title: String,

  @field:NotBlank
  val content: String
)
