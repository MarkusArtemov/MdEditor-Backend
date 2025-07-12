package de.hsfl.mdeditorbackend.common.markdown

interface MarkdownProvider {
  fun findContent(ownerId: Long, docId: Long, versionId: Long?): MarkdownInfo
}
data class MarkdownInfo(
  val documentId: Long,
  val title: String,
  val content: String,
  val versionId: Long
)
