package de.hsfl.mdeditorbackend.render.service

import de.hsfl.mdeditorbackend.common.markdown.MarkdownProvider
import org.springframework.stereotype.Service
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser

@Service
class MarkdownRenderer(
  private val provider: MarkdownProvider
) {

  private val parser = Parser.builder().build()
  private val renderer = HtmlRenderer.builder().build()

  fun toHtml(ownerId: Long, documentId: Long, versionId: Long? = null): String {
    val markdown = provider.findContent(ownerId, documentId, versionId).content
    return renderer.render(parser.parse(markdown))
  }

  fun toMarkdown(ownerId: Long, documentId: Long, versionId: Long? = null): String =
    provider.findContent(ownerId, documentId, versionId).content
}
