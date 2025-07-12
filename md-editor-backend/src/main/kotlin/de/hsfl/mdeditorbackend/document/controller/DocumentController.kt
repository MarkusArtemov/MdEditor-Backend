package de.hsfl.mdeditorbackend.document.controller

import de.hsfl.mdeditorbackend.common.security.CurrentUserId
import de.hsfl.mdeditorbackend.document.model.dto.*
import de.hsfl.mdeditorbackend.document.service.DocumentService
import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/documents")
class DocumentController(
  private val documentService: DocumentService
) {

  @PostMapping
  fun create(
    @Valid @RequestBody body: DocumentCreateRequest,
    @CurrentUserId userId: Long
  ): ResponseEntity<DocumentResponse> =
    ResponseEntity.status(HttpStatus.CREATED)
      .body(documentService.create(userId, body))

  @GetMapping
  fun listOwn(@CurrentUserId userId: Long): List<DocumentResponse> =
    documentService.listOwn(userId)

  @GetMapping("/{id}")
  fun get(
    @PathVariable @Positive id: Long,
    @CurrentUserId userId: Long
  ): DocumentResponse =
    documentService.get(userId, id)

  @PutMapping("/{id}")
  fun update(
    @PathVariable @Positive id: Long,
    @Valid @RequestBody body: DocumentUpdateRequest,
    @CurrentUserId userId: Long
  ): DocumentResponse =
    documentService.update(userId, id, body)

  @DeleteMapping("/{id}")
  fun delete(
    @PathVariable @Positive id: Long,
    @CurrentUserId userId: Long
  ): ResponseEntity<Void> {
    documentService.delete(userId, id)
    return ResponseEntity.noContent().build()
  }

  @GetMapping("/{id}/versions")
  fun versions(
    @PathVariable @Positive id: Long,
    @CurrentUserId userId: Long
  ): List<DocumentVersionSummary> =
    documentService.listVersions(userId, id)

  @GetMapping("/{id}/versions/{vid}")
  fun getVersion(
    @PathVariable @Positive id: Long,
    @PathVariable @Positive vid: Long,
    @CurrentUserId userId: Long
  ): DocumentResponse =
    documentService.getVersion(userId, id, vid)

  @PostMapping("/{id}/restore/{vid}")
  fun restore(
    @PathVariable @Positive id: Long,
    @PathVariable @Positive vid: Long,
    @CurrentUserId userId: Long
  ): DocumentResponse =
    documentService.restoreVersion(userId, id, vid)
}
