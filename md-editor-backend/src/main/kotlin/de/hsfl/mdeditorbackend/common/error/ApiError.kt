package de.hsfl.mdeditorbackend.common.error

data class ApiError(
  val status: Int,
  val error: String,
  val message: String?
)
