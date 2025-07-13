package de.hsfl.mdeditorbackend.render.controller

import de.hsfl.mdeditorbackend.common.api.CurrentUserId
import de.hsfl.mdeditorbackend.render.service.MarkdownRenderer
import de.hsfl.mdeditorbackend.render.util.ExportUtil
import jakarta.validation.constraints.Positive
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(name = "MyExport")
@ApiResponses(
  value = [
    ApiResponse(
      responseCode = "404",
      description = "Not found",
      content = [Content(schema = Schema(ref = "#/components/schemas/ApiError"))]
    )
  ]
)
@Validated
@RestController
@RequestMapping("/documents/{id}/export")
class DocumentExportController(
  private val renderer: MarkdownRenderer
) {

  @Operation(summary = "Download md")
  @ApiResponse(responseCode = "200", description = "File")
  @GetMapping("/md", produces = ["text/markdown"])
  fun downloadMarkdown(
    @PathVariable @Positive id: Long,
    @CurrentUserId userId: Long
  ): ResponseEntity<ByteArrayResource> {
    val info = renderer.getMarkdownInfo(userId, id)
    val filename = "${ExportUtil.sanitize(info.title)}_${info.documentId}.md"
    return ExportUtil.buildFileResponse(info.content.toByteArray(), filename, ExportUtil.MARKDOWN_MEDIA_TYPE)
  }

  @Operation(summary = "Download HTML")
  @ApiResponse(responseCode = "200", description = "File")
  @GetMapping("/html", produces = ["text/html"])
  fun downloadHtml(
    @PathVariable @Positive id: Long,
    @CurrentUserId userId: Long
  ): ResponseEntity<ByteArrayResource> {
    val info = renderer.getMarkdownInfo(userId, id)
    val html = renderer.toHtml(userId, id)
    val filename = "${ExportUtil.sanitize(info.title)}_${info.documentId}.html"
    return ExportUtil.buildFileResponse(html.toByteArray(), filename, MediaType.TEXT_HTML)
  }

  @Operation(summary = "Download version md")
  @ApiResponse(responseCode = "200", description = "File")
  @GetMapping("/versions/{vid}/md", produces = ["text/markdown"])
  fun downloadVersionMarkdown(
    @PathVariable @Positive id: Long,
    @PathVariable @Positive vid: Long,
    @CurrentUserId userId: Long
  ): ResponseEntity<ByteArrayResource> {
    val info = renderer.getMarkdownInfo(userId, id, vid)
    val filename = "${ExportUtil.sanitize(info.title)}_${info.documentId}_v${info.versionId}.md"
    return ExportUtil.buildFileResponse(info.content.toByteArray(), filename, ExportUtil.MARKDOWN_MEDIA_TYPE)
  }

  @Operation(summary = "Download version HTML")
  @ApiResponse(responseCode = "200", description = "File")
  @GetMapping("/versions/{vid}/html", produces = ["text/html"])
  fun downloadVersionHtml(
    @PathVariable @Positive id: Long,
    @PathVariable @Positive vid: Long,
    @CurrentUserId userId: Long
  ): ResponseEntity<ByteArrayResource> {
    val info = renderer.getMarkdownInfo(userId, id, vid)
    val html = renderer.toHtml(userId, id, vid)
    val filename = "${ExportUtil.sanitize(info.title)}_${info.documentId}_v${info.versionId}.html"
    return ExportUtil.buildFileResponse(html.toByteArray(), filename, MediaType.TEXT_HTML)
  }
}
