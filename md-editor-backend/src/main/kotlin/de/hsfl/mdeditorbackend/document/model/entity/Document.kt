package de.hsfl.mdeditorbackend.document.model.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "documents")
data class Document(
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0,

  @Column(nullable = false)
  var title: String,

  @Column(name = "owner_id", nullable = false)
  val ownerId: Long,

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "current_version_id")
  var currentVersion: DocumentVersion? = null,

  @OneToMany(
    mappedBy = "document",
    cascade = [CascadeType.ALL],
    orphanRemoval = true
  )
  val versions: MutableList<DocumentVersion> = mutableListOf(),

  @Column(nullable = false)
  val createdAt: Instant = Instant.now(),

  @Column(nullable = false)
  var updatedAt: Instant = Instant.now()
)
