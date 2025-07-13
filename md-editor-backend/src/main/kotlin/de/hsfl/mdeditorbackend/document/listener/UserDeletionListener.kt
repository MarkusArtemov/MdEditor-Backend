package de.hsfl.mdeditorbackend.document.listener

import de.hsfl.mdeditorbackend.common.api.UserDeleteRequested
import de.hsfl.mdeditorbackend.document.repository.DocumentRepository
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserDeletionListener(
  private val documentRepository: DocumentRepository,
) {
  @Transactional
  @EventListener
  fun handle(e: UserDeleteRequested) =
    documentRepository.deleteAllByOwnerId(e.userId)
}
