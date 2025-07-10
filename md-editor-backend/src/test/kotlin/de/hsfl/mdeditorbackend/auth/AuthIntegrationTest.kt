package de.hsfl.mdeditorbackend.auth

import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class AuthIntegrationTest @Autowired constructor(
    private val mockMvc: MockMvc
) {

    @Test
    fun `full auth flow`() {
        // 1) Register
        mockMvc.perform(
            post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"username":"testuser","password":"testpass"}""")
        ).andExpect(status().isCreated)

        // 2) Login
        val loginResult = mockMvc.perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"username":"testuser","password":"testpass"}""")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.token").exists())
            .andReturn()

        val token = JSONObject(loginResult.response.contentAsString).getString("token")

        // 3) Access protected endpoint
//        mockMvc.perform(
//            get("/documents")
//                .header("Authorization", "Bearer $token")
//        )
//            .andExpect(status().isOk)
//            .andExpect(content().json("[]"))
    }
}
