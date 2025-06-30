package de.hsfl.mdeditorbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MdEditorBackendApplication

fun main(args: Array<String>) {
	runApplication<MdEditorBackendApplication>(*args)
}
