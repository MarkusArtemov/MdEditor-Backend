package de.hsfl.mdeditorbackend.render.util

import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

object ExportUtil {
  val MARKDOWN_MEDIA_TYPE = MediaType("text", "markdown")

  fun sanitize(input: String): String =
    input.replace(Regex("[^a-zA-Z0-9._-]"), "_")

  fun buildFileResponse(content: ByteArray, filename: String, mediaType: MediaType): ResponseEntity<ByteArrayResource> =
    ResponseEntity.ok()
      .contentLength(content.size.toLong())
      .contentType(mediaType)
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$filename\"")
      .body(ByteArrayResource(content))
}
