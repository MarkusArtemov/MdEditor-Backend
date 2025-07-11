package de.hsfl.mdeditorbackend.document.service

import de.hsfl.mdeditorbackend.document.exception.*
import de.hsfl.mdeditorbackend.document.mapper.DocumentMapper
import de.hsfl.mdeditorbackend.document.model.dto.*
import de.hsfl.mdeditorbackend.document.model.entity.DocumentVersion
import de.hsfl.mdeditorbackend.document.repository.DocumentRepository
import de.hsfl.mdeditorbackend.document.repository.DocumentVersionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
@Transactional
class DocumentServiceImpl(
  private val documentRepository: DocumentRepository,
  private val documentVersionRepository: DocumentVersionRepository,
  private val documentMapper: DocumentMapper
) : DocumentService {

  override fun create(ownerId: Long, req: DocumentCreateRequest): DocumentResponse {
    if (req.content.isBlank()) throw EmptyContentException()
    val document = documentMapper.toEntity(req, ownerId)
    documentRepository.save(document)

    val version = DocumentVersion(
      document = document,
      versionNumber = 1,
      content = req.content,
      authorId = ownerId,
      createdAt = Instant.now()
    )
    documentVersionRepository.save(version)

    document.currentVersion = version
    document.updatedAt = version.createdAt
    documentRepository.save(document)

    return documentMapper.toResponse(document)
  }

  @Transactional(readOnly = true)
  override fun listOwn(ownerId: Long): List<DocumentResponse> =
    documentRepository.findAllByOwnerId(ownerId).map(documentMapper::toResponse)

  @Transactional(readOnly = true)
  override fun get(ownerId: Long, id: Long): DocumentResponse {
    val doc = documentRepository.findById(id)
      .orElseThrow { DocumentNotFoundException(id) }
    assertOwner(doc.ownerId, ownerId)
    return documentMapper.toResponse(doc)
  }

  override fun update(ownerId: Long, id: Long, req: DocumentUpdateRequest): DocumentResponse {
    val doc = documentRepository.findById(id)
      .orElseThrow { DocumentNotFoundException(id) }
    assertOwner(doc.ownerId, ownerId)

    req.title?.let {
      if (it.isBlank() || it.length > 255) throw InvalidTitleException()
      doc.title = it
    }

    req.content?.let {
      if (it.isBlank()) throw EmptyContentException()
      val newVersionNo = (doc.currentVersion?.versionNumber ?: 0) + 1
      val version = DocumentVersion(
        document = doc,
        versionNumber = newVersionNo,
        content = it,
        authorId = ownerId,
        createdAt = Instant.now()
      )
      documentVersionRepository.save(version)
      doc.currentVersion = version
      doc.updatedAt = version.createdAt
    }

    documentRepository.save(doc)
    return documentMapper.toResponse(doc)
  }

  override fun delete(ownerId: Long, id: Long) {
    val doc = documentRepository.findById(id)
      .orElseThrow { DocumentNotFoundException(id) }
    assertOwner(doc.ownerId, ownerId)
    documentVersionRepository.deleteAll(
      documentVersionRepository.findAllByDocumentIdOrderByVersionNumberDesc(id)
    )
    documentRepository.delete(doc)
  }

  @Transactional(readOnly = true)
  override fun listVersions(ownerId: Long, docId: Long): List<DocumentVersionSummary> {
    val doc = documentRepository.findById(docId)
      .orElseThrow { DocumentNotFoundException(docId) }
    assertOwner(doc.ownerId, ownerId)
    return documentVersionRepository
      .findAllByDocumentIdOrderByVersionNumberDesc(docId)
      .map(documentMapper::toSummary)
  }

  @Transactional(readOnly = true)
  override fun getVersion(ownerId: Long, docId: Long, versionId: Long): DocumentResponse {
    val version = documentVersionRepository.findByDocumentIdAndVersionNumber(docId, versionId)
      .orElseThrow { VersionNotFoundException(versionId) }
    assertOwner(version.document.ownerId, ownerId)
    return DocumentResponse(
      id = version.document.id,
      title = version.document.title,
      content = version.content,
      versionNumber = version.versionNumber,
      updatedAt = version.createdAt
    )
  }

  override fun restoreVersion(ownerId: Long, docId: Long, versionId: Long) {
    val version = documentVersionRepository.findByDocumentIdAndVersionNumber(docId,versionId)
      .orElseThrow { VersionNotFoundException(versionId) }
    assertOwner(version.document.ownerId, ownerId)

    val document = version.document

    val newVersionNo = (document.currentVersion?.versionNumber ?: 0) + 1
    val restoredVersion = DocumentVersion(
      document = document,
      versionNumber = newVersionNo,
      content = version.content,
      authorId = ownerId,
      createdAt = Instant.now()
    )
    documentVersionRepository.save(restoredVersion)

    document.currentVersion = restoredVersion
    document.updatedAt = restoredVersion.createdAt
    documentRepository.save(document)
  }

  private fun assertOwner(actualOwnerId: Long, currentUserId: Long) {
    if (actualOwnerId != currentUserId) throw NotDocumentOwnerException(actualOwnerId)
  }
}
