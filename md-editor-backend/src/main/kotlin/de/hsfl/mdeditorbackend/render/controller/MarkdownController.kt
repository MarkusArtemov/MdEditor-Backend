package de.hsfl.mdeditorbackend.render.controller

import de.hsfl.mdeditorbackend.common.security.CurrentUserId
import de.hsfl.mdeditorbackend.render.service.MarkdownRenderer
import jakarta.validation.constraints.Positive
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/documents/{id}/export")
class DocumentExportController(
  private val renderer: MarkdownRenderer
) {

  @GetMapping("/md", produces = ["text/markdown"])
  fun downloadMarkdown(@PathVariable @Positive id: Long, @CurrentUserId userId: Long) =
    buildFileResponse(renderer.toMarkdown(userId, id).toByteArray(), "$id.md", MediaType("text", "markdown"))

  @GetMapping("/html", produces = ["text/html"])
  fun downloadHtml(@PathVariable @Positive id: Long, @CurrentUserId userId: Long) =
    buildFileResponse(renderer.toHtml(userId, id).toByteArray(), "$id.html", MediaType.TEXT_HTML)

  @GetMapping("/versions/{vid}/md", produces = ["text/markdown"])
  fun downloadVersionMarkdown(@PathVariable @Positive id: Long, @PathVariable @Positive vid: Long, @CurrentUserId userId: Long) =
    buildFileResponse(renderer.toMarkdown(userId, id, vid).toByteArray(), "${id}_v$vid.md", MediaType("text", "markdown"))

  @GetMapping("/versions/{vid}/html", produces = ["text/html"])
  fun downloadVersionHtml(@PathVariable @Positive id: Long, @PathVariable @Positive vid: Long, @CurrentUserId userId: Long) =
    buildFileResponse(renderer.toHtml(userId, id, vid).toByteArray(), "${id}_v$vid.html", MediaType.TEXT_HTML)

  private fun buildFileResponse(content: ByteArray, filename: String, mediaType: MediaType) =
    ResponseEntity.ok()
      .contentLength(content.size.toLong())
      .contentType(mediaType)
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$filename\"")
      .body(ByteArrayResource(content))
}


