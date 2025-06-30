package de.hsfl.mdeditorbackend

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class MdEditorBackendApplicationTests {

	@Test
	fun contextLoads() {
	}

}
