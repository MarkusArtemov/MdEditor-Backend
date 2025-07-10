package de.hsfl.mdeditorbackend.document.controller

import de.hsfl.mdeditorbackend.document.model.dto.*
import de.hsfl.mdeditorbackend.document.service.DocumentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/documents")
class DocumentController(
  private val documentService: DocumentService
) {

  @PostMapping
  fun create(
    @Valid @RequestBody body: DocumentCreateRequest,
    auth: Authentication
  ): ResponseEntity<DocumentResponse> =
    ResponseEntity.status(HttpStatus.CREATED)
      .body(documentService.create(auth.name, body))

  @GetMapping
  fun listOwn(auth: Authentication): List<DocumentResponse> =
    documentService.listOwn(auth.name)

  @GetMapping("/{id}")
  fun get(
    @PathVariable id: Long,
    auth: Authentication
  ): DocumentResponse =
    documentService.get(auth.name, id)

  @PutMapping("/{id}")
  fun update(
    @PathVariable id: Long,
    @Valid @RequestBody body: DocumentUpdateRequest,
    auth: Authentication
  ): DocumentResponse =
    documentService.update(auth.name, id, body)

  @DeleteMapping("/{id}")
  fun delete(
    @PathVariable id: Long,
    auth: Authentication
  ): ResponseEntity<Void> {
    documentService.delete(auth.name, id)
    return ResponseEntity.noContent().build()
  }

  @GetMapping("/{id}/versions")
  fun versions(
    @PathVariable id: Long,
    auth: Authentication
  ): List<DocumentVersionSummary> =
    documentService.listVersions(auth.name, id)

  @GetMapping("/{id}/versions/{vid}")
  fun getVersion(
    @PathVariable id: Long,
    @PathVariable vid: Long,
    auth: Authentication
  ): DocumentResponse =
    documentService.getVersion(auth.name, id, vid)

  @PostMapping("/{id}/restore/{vid}")
  fun restore(
    @PathVariable id: Long,
    @PathVariable vid: Long,
    auth: Authentication
  ): ResponseEntity<Void> {
    documentService.restoreVersion(auth.name, id, vid)
    return ResponseEntity.noContent().build()
  }
}
