package de.hsfl.mdeditorbackend.document.model.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "document_versions")
data class DocumentVersion(
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0,

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "document_id", nullable = false)
  val document: Document,

  @Column(nullable = false)
  val versionNumber: Int,

  @Lob
  @Column(nullable = false)
  val content: String,

  @Column(name = "author_id", nullable = false)
  val authorId: Long,

  @Column(nullable = false)
  val createdAt: Instant = Instant.now()
)
