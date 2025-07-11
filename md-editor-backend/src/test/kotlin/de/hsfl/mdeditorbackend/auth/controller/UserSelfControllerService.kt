package de.hsfl.mdeditorbackend.auth.controller

import com.fasterxml.jackson.databind.ObjectMapper
import de.hsfl.mdeditorbackend.auth.controller.UserSelfController
import de.hsfl.mdeditorbackend.auth.model.dto.ChangeUsernameRequest
import de.hsfl.mdeditorbackend.auth.model.dto.ChangePasswordRequest
import de.hsfl.mdeditorbackend.auth.service.UserService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.Mockito.verify
import org.springframework.http.MediaType
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(MockitoExtension::class)
class UserSelfControllerTest {

    private lateinit var mockMvc: MockMvc
    private val objectMapper = ObjectMapper()

    @Mock
    private lateinit var userService: UserService

    @InjectMocks
    private lateinit var userSelfController: UserSelfController

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(userSelfController)
            .build()
    }

    @Test
    fun `changeUsername should return no content and call service`() {
        val req = ChangeUsernameRequest("newName")
        val json = objectMapper.writeValueAsString(req)

        mockMvc.perform(
            patch("/users/me/username")
                .principal(TestingAuthenticationToken("testuser", null))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isNoContent)

        verify(userService).updateOwnUsername("testuser", "newName")
    }

    @Test
    fun `changePassword should return no content and call service`() {
        val req = ChangePasswordRequest(oldPassword = "old", newPassword = "new")
        val json = objectMapper.writeValueAsString(req)

        mockMvc.perform(
            patch("/users/me/password")
                .principal(TestingAuthenticationToken("testuser", null))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isNoContent)

        verify(userService).updateOwnPassword("testuser", "old", "new")
    }

    @Test
    fun `deleteAccount should return no content and call service`() {
        mockMvc.perform(
            delete("/users/me")
                .principal(TestingAuthenticationToken("testuser", null))
        )
            .andExpect(status().isNoContent)

        verify(userService).deleteOwnAccount("testuser")
    }
}
