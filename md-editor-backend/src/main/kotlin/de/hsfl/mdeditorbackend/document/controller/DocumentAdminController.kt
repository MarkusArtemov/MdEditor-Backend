package de.hsfl.mdeditorbackend.document.controller

import de.hsfl.mdeditorbackend.document.model.dto.DocumentResponse
import de.hsfl.mdeditorbackend.document.model.dto.DocumentVersionSummary
import de.hsfl.mdeditorbackend.document.service.DocumentAdminService
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/admin/documents")
class DocumentAdminController(
  private val adminService: DocumentAdminService
) {

  @GetMapping
  fun listAll(
    pageable: Pageable,
    @RequestParam(required = false) ownerId: Long?,
    @RequestParam(required = false) @Size(max = 255) title: String?
  ): Page<DocumentResponse> =
    adminService.listAll(pageable, ownerId, title)

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  fun delete(@PathVariable @Positive id: Long) =
    adminService.delete(id)

  @GetMapping("/{id}/history")
  fun getDocumentHistory(@PathVariable @Positive id: Long): List<DocumentVersionSummary> =
    adminService.getDocumentHistory(id)
}

