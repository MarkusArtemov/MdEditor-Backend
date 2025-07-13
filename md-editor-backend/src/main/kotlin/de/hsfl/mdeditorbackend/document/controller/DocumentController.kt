package de.hsfl.mdeditorbackend.document.controller

import de.hsfl.mdeditorbackend.common.api.CurrentUserId
import de.hsfl.mdeditorbackend.document.model.dto.*
import de.hsfl.mdeditorbackend.document.service.DocumentService
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(
  name = "Documents",
  description = "Endpoints for managing own markdown documents"
)
// shared errors
@ApiResponses(
  value = [
    ApiResponse(
      responseCode = "400",
      description = "Validation failed",
      content = [Content(schema = Schema(ref = "#/components/schemas/ApiError"))]
    ),
    ApiResponse(
      responseCode = "404",
      description = "Document not found or its not owned by caller",
      content = [Content(schema = Schema(ref = "#/components/schemas/ApiError"))]
    ),
    ApiResponse(
      responseCode = "409",
      description = "Document has no current version",
      content = [Content(schema = Schema(ref = "#/components/schemas/ApiError"))]
    )
  ]
)
@Validated
@RestController
@RequestMapping("/documents")
class DocumentController(
  private val documentService: DocumentService
) {

  @Operation(summary = "Create a new document")
  @ApiResponse(responseCode = "201", description = "Document created")
  @PostMapping
  fun create(
    @Valid @RequestBody body: DocumentCreateRequest,
    @CurrentUserId userId: Long
  ): ResponseEntity<DocumentResponse> =
    ResponseEntity.status(HttpStatus.CREATED)
      .body(documentService.create(userId, body))

  @Operation(summary = "List all owned documents")
  @GetMapping
  fun listOwn(@CurrentUserId userId: Long): List<DocumentResponse> =
    documentService.listOwn(userId)

  @Operation(summary = "Get a document by its id")
  @GetMapping("/{id}")
  fun get(
    @PathVariable @Positive id: Long,
    @CurrentUserId userId: Long
  ): DocumentResponse =
    documentService.get(userId, id)

  @Operation(summary = "Update title or content of a document")
  @PutMapping("/{id}")
  fun update(
    @PathVariable @Positive id: Long,
    @Valid @RequestBody body: DocumentUpdateRequest,
    @CurrentUserId userId: Long
  ): DocumentResponse =
    documentService.update(userId, id, body)

  @Operation(summary = "Delete a document")
  @ApiResponse(responseCode = "204", description = "Document deleted")
  @DeleteMapping("/{id}")
  fun delete(
    @PathVariable @Positive id: Long,
    @CurrentUserId userId: Long
  ): ResponseEntity<Void> {
    documentService.delete(userId, id)
    return ResponseEntity.noContent().build()
  }

  @Operation(summary = "List all versions of a document")
  @GetMapping("/{id}/versions")
  fun versions(
    @PathVariable @Positive id: Long,
    @CurrentUserId userId: Long
  ): List<DocumentVersionSummary> =
    documentService.listVersions(userId, id)

  @Operation(summary = "Get a version of a document")
  @GetMapping("/{id}/versions/{vid}")
  fun getVersion(
    @PathVariable @Positive id: Long,
    @PathVariable @Positive vid: Long,
    @CurrentUserId userId: Long
  ): DocumentResponse =
    documentService.getVersion(userId, id, vid)

  @Operation(summary = "Restore an old version, creates a new version out of it")
  @PostMapping("/{id}/restore/{vid}")
  fun restore(
    @PathVariable @Positive id: Long,
    @PathVariable @Positive vid: Long,
    @CurrentUserId userId: Long
  ): DocumentResponse =
    documentService.restoreVersion(userId, id, vid)
}
