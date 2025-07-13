package de.hsfl.mdeditorbackend.render.controller

import de.hsfl.mdeditorbackend.common.api.ApiError
import de.hsfl.mdeditorbackend.render.service.MarkdownRenderer
import de.hsfl.mdeditorbackend.render.util.ExportUtil
import jakarta.validation.constraints.Positive
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "AdminExport")
@ApiResponses(
  value = [
    ApiResponse(
      responseCode = "404",
      description = "Not found",
      content = [ Content(
        mediaType = "application/json",
        schema = Schema(implementation = ApiError::class)
      ) ]
    )
  ]
)
@Validated
@RestController
@RequestMapping("/admin/documents/{id}/export")
@PreAuthorize("hasRole('ADMIN')")
class AdminDocumentExportController(
  private val renderer: MarkdownRenderer
) {

  @Operation(summary = "Download md")
  @ApiResponse(responseCode = "200", description = "File")
  @GetMapping("/md", produces = ["text/markdown"])
  fun downloadMarkdown(@PathVariable @Positive id: Long): ResponseEntity<ByteArrayResource> {
    val info = renderer.getMarkdownInfo(null, id)
    val filename = "${ExportUtil.sanitize(info.title)}_${info.documentId}.md"
    return ExportUtil.buildFileResponse(info.content.toByteArray(), filename, ExportUtil.MARKDOWN_MEDIA_TYPE)
  }

  @Operation(summary = "Download HTML")
  @ApiResponse(responseCode = "200", description = "File")
  @GetMapping("/html", produces = ["text/html"])
  fun downloadHtml(@PathVariable @Positive id: Long): ResponseEntity<ByteArrayResource> {
    val info = renderer.getMarkdownInfo(null, id)
    val html = renderer.toHtml(null, id)
    val filename = "${ExportUtil.sanitize(info.title)}_${info.documentId}.html"
    return ExportUtil.buildFileResponse(html.toByteArray(), filename, MediaType.TEXT_HTML)
  }

  @Operation(summary = "Download version md")
  @ApiResponse(responseCode = "200", description = "File")
  @GetMapping("/versions/{vid}/md", produces = ["text/markdown"])
  fun downloadVersionMarkdown(
    @PathVariable @Positive id: Long,
    @PathVariable @Positive vid: Long
  ): ResponseEntity<ByteArrayResource> {
    val info = renderer.getMarkdownInfo(null, id, vid)
    val filename = "${ExportUtil.sanitize(info.title)}_${info.documentId}_v${info.versionId}.md"
    return ExportUtil.buildFileResponse(info.content.toByteArray(), filename, ExportUtil.MARKDOWN_MEDIA_TYPE)
  }

  @Operation(summary = "Download version HTML")
  @ApiResponse(responseCode = "200", description = "File")
  @GetMapping("/versions/{vid}/html", produces = ["text/html"])
  fun downloadVersionHtml(
    @PathVariable @Positive id: Long,
    @PathVariable @Positive vid: Long
  ): ResponseEntity<ByteArrayResource> {
    val info = renderer.getMarkdownInfo(null, id, vid)
    val html = renderer.toHtml(null, id, vid)
    val filename = "${ExportUtil.sanitize(info.title)}_${info.documentId}_v${info.versionId}.html"
    return ExportUtil.buildFileResponse(html.toByteArray(), filename, MediaType.TEXT_HTML)
  }
}
