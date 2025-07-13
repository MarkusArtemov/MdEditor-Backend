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
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag

@Tag(
  name = "AdminDocuments",
  description = "Admin Controller for managing all Documents",
)
// shared errors
@ApiResponses(
  value = [
    ApiResponse(
      responseCode = "404",
      description = "Document not found",
      content = [Content(schema = Schema(ref = "#/components/schemas/ApiError"))]
    )
  ]
)
@Validated
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/admin/documents")
class DocumentAdminController(
  private val adminService: DocumentAdminService
) {

  @Operation(
    summary = "List documents",
    description = "Paging with filters: ownerId, title"
  )
  @GetMapping
  fun listAll(
    pageable: Pageable,
    @RequestParam(required = false) ownerId: Long?,
    @RequestParam(required = false) @Size(max = 255) title: String?
  ): Page<DocumentResponse> =
    adminService.listAll(pageable, ownerId, title)

  @Operation(
    summary = "Hard delete document",
  )
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  fun delete(@PathVariable @Positive id: Long) =
    adminService.delete(id)

  @Operation(
    summary = "Get version history, new ones first",
  )
  @GetMapping("/{id}/history")
  fun getDocumentHistory(@PathVariable @Positive id: Long): List<DocumentVersionSummary> =
    adminService.getDocumentHistory(id)
}
