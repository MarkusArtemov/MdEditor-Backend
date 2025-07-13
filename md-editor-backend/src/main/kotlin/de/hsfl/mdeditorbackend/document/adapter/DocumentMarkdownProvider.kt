package de.hsfl.mdeditorbackend.document.adapter

import de.hsfl.mdeditorbackend.common.api.MarkdownInfo
import de.hsfl.mdeditorbackend.common.api.MarkdownProvider
import de.hsfl.mdeditorbackend.document.exception.*
import de.hsfl.mdeditorbackend.document.repository.DocumentRepository
import de.hsfl.mdeditorbackend.document.repository.DocumentVersionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class DocumentMarkdownProvider(
  private val documentRepository: DocumentRepository,
  private val versionRepository: DocumentVersionRepository
) : MarkdownProvider {

  override fun findContent(ownerId: Long?, docId: Long, versionId: Long?): MarkdownInfo {
    val document = if (ownerId == null) {
      documentRepository.findById(docId).orElseThrow { DocumentNotFoundException(docId) }
    } else {
      documentRepository.findByIdAndOwnerId(docId, ownerId)
        .orElseThrow { NotDocumentOwnerException(docId, ownerId) }
    }

    val version = when (versionId) {
      null -> document.currentVersion ?: throw MissingCurrentVersionException(document.id)
      else -> versionRepository.findByDocumentIdAndVersionNumber(docId, versionId)
        .orElseThrow { VersionNotFoundException(versionId) }
    }

    return MarkdownInfo(
      documentId = document.id,
      title = document.title,
      content = version.content,
      versionId = version.versionNumber.toLong()
    )
  }
}
