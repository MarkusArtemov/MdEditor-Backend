package de.hsfl.mdeditorbackend.document.controller

import de.hsfl.mdeditorbackend.common.security.UserPrincipal
import de.hsfl.mdeditorbackend.document.model.dto.*
import de.hsfl.mdeditorbackend.document.service.DocumentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/documents")
class DocumentController(
  private val documentService: DocumentService
) {

  @PostMapping
  fun create(
    @Valid @RequestBody body: DocumentCreateRequest,
    @AuthenticationPrincipal user: UserPrincipal
  ): ResponseEntity<DocumentResponse> =
    ResponseEntity.status(HttpStatus.CREATED)
      .body(documentService.create(user.id, body))


  @GetMapping
  fun listOwn(@AuthenticationPrincipal user: UserPrincipal): List<DocumentResponse> =
    documentService.listOwn(user.id)

  @GetMapping("/{id}")
  fun get(
    @PathVariable id: Long,
    @AuthenticationPrincipal user: UserPrincipal
  ): DocumentResponse =
    documentService.get(user.id, id)


  @PutMapping("/{id}")
  fun update(
    @PathVariable id: Long,
    @Valid @RequestBody body: DocumentUpdateRequest,
    @AuthenticationPrincipal user: UserPrincipal
  ): DocumentResponse =
    documentService.update(user.id, id, body)

  @DeleteMapping("/{id}")
  fun delete(
    @PathVariable id: Long,
    @AuthenticationPrincipal user: UserPrincipal
  ): ResponseEntity<Void> {
    documentService.delete(user.id, id)
    return ResponseEntity.noContent().build()
  }

  @GetMapping("/{id}/versions")
  fun versions(
    @PathVariable id: Long,
    @AuthenticationPrincipal user: UserPrincipal
  ): List<DocumentVersionSummary> =
    documentService.listVersions(user.id, id)

  @GetMapping("/{id}/versions/{vid}")
  fun getVersion(
    @PathVariable id: Long,
    @PathVariable vid: Long,
    @AuthenticationPrincipal user: UserPrincipal
  ): DocumentResponse =
    documentService.getVersion(user.id, id, vid)

  @PostMapping("/{id}/restore/{vid}")
  fun restore(
    @PathVariable id: Long,
    @PathVariable vid: Long,
    @AuthenticationPrincipal user: UserPrincipal
  ): ResponseEntity<Void> {
    documentService.restoreVersion(user.id, id, vid)
    return ResponseEntity.noContent().build()
  }
}
