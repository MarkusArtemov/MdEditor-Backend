package de.hsfl.mdeditorbackend.document.mapper

import de.hsfl.mdeditorbackend.document.model.dto.*
import de.hsfl.mdeditorbackend.document.model.entity.*
import org.mapstruct.*
import java.time.Instant

@Mapper(componentModel = "spring", imports = [Instant::class])
interface DocumentMapper {

  @Mapping(source = "currentVersion.content", target = "content")
  @Mapping(source = "currentVersion.versionNumber",target = "versionNumber")
  fun toResponse(document: Document): DocumentResponse

  fun toSummary(version: DocumentVersion): DocumentVersionSummary

  @Mappings(
    Mapping(target = "id", ignore = true),
    Mapping(target = "currentVersion",ignore = true),
    Mapping(target = "createdAt", expression = "java(Instant.now())"),
    Mapping(target = "updatedAt", expression = "java(Instant.now())"),
    Mapping(source = "owner", target = "owner")
  )
  fun toEntity(request: DocumentCreateRequest, @Context owner: String): Document
}
