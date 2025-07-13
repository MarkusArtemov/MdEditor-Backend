package de.hsfl.mdeditorbackend.common.api

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
  name = "ApiError",
  description = "Error that every endpoint returns on failure"
)

data class ApiError(
  val status: Int,
  val error: String,
  val message: String?,
  val code: String? = null
)
