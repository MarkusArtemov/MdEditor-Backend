package de.hsfl.mdeditorbackend.document.service

import de.hsfl.mdeditorbackend.document.mapper.DocumentMapper
import de.hsfl.mdeditorbackend.document.model.dto.*
import de.hsfl.mdeditorbackend.document.model.entity.DocumentVersion
import de.hsfl.mdeditorbackend.document.repository.DocumentRepository
import de.hsfl.mdeditorbackend.document.repository.DocumentVersionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class DocumentServiceImpl(
  private val documentRepository: DocumentRepository,
  private val documentVersionRepository: DocumentVersionRepository,
  private val documentMapper: DocumentMapper
) : DocumentService {

  @Transactional
  override fun create(owner: String, req: DocumentCreateRequest): DocumentResponse {
    val document = documentMapper.toEntity(req, owner)
    documentRepository.save(document)

    val version = DocumentVersion(
      document = document,
      versionNumber = 1,
      content = req.content,
      author = owner,
      createdAt = Instant.now()
    )
    documentVersionRepository.save(version)

    document.currentVersion = version
    document.updatedAt = version.createdAt
    documentRepository.save(document)

    return documentMapper.toResponse(document)
  }

  @Transactional(readOnly = true)
  override fun listOwn(owner: String): List<DocumentResponse> =
    documentRepository.findAllByOwner(owner).map { documentMapper.toResponse(it) }

  @Transactional(readOnly = true)
  override fun get(owner: String, id: Long): DocumentResponse {
    val doc = documentRepository.findById(id)
      .orElseThrow { NoSuchElementException("Document $id not found") }
    assertOwner(doc.owner, owner)
    return documentMapper.toResponse(doc)
  }

  @Transactional
  override fun update(owner: String, id: Long, req: DocumentUpdateRequest): DocumentResponse {
    val doc = documentRepository.findById(id)
      .orElseThrow { NoSuchElementException("Document $id not found") }
    assertOwner(doc.owner, owner)

    req.title?.let { doc.title = it }
    req.content?.let {
      val newVersionNo = (doc.currentVersion?.versionNumber ?: 0) + 1
      val version = DocumentVersion(
        document = doc,
        versionNumber = newVersionNo,
        content = it,
        author = owner,
        createdAt = Instant.now()
      )
      documentVersionRepository.save(version)
      doc.currentVersion = version
      doc.updatedAt = version.createdAt
    }
    documentRepository.save(doc)
    return documentMapper.toResponse(doc)
  }

  @Transactional
  override fun delete(owner: String, id: Long) {
    val doc = documentRepository.findById(id)
      .orElseThrow { NoSuchElementException("Document $id not found") }
    assertOwner(doc.owner, owner)
    documentVersionRepository.deleteAll(
      documentVersionRepository.findAllByDocumentIdOrderByVersionNumberDesc(id)
    )
    documentRepository.delete(doc)
  }

  @Transactional(readOnly = true)
  override fun listVersions(owner: String, docId: Long): List<DocumentVersionSummary> =
    throw UnsupportedOperationException()

  @Transactional(readOnly = true)
  override fun getVersion(owner: String, docId: Long, versionId: Long): DocumentResponse =
    throw UnsupportedOperationException()

  @Transactional
  override fun restoreVersion(owner: String, docId: Long, versionId: Long) {
    throw UnsupportedOperationException()
  }

  private fun assertOwner(actualOwner: String, currentUser: String) {
    if (actualOwner != currentUser) throw IllegalAccessException("Not your document")
  }
}
