package de.hslf.md_editor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MdEditorApplication

fun main(args: Array<String>) {
	runApplication<MdEditorApplication>(*args)
}
