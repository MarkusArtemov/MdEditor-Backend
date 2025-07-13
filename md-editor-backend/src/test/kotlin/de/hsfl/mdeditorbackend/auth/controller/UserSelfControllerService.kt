package de.hsfl.mdeditorbackend.auth.controller

import com.fasterxml.jackson.databind.ObjectMapper
import de.hsfl.mdeditorbackend.auth.model.dto.ChangeUsernameRequest
import de.hsfl.mdeditorbackend.auth.model.dto.ChangePasswordRequest
import de.hsfl.mdeditorbackend.auth.service.UserService
import de.hsfl.mdeditorbackend.common.api.UserPrincipal
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
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

  private lateinit var principal: UserPrincipal

  @BeforeEach
  fun setup() {
    mockMvc = MockMvcBuilders
      .standaloneSetup(userSelfController)
      .build()

    principal = UserPrincipal(
      id = 1L,
      username = "testuser",
      password = "secret",
      roles = emptyList()
    )
  }

  @Test
  fun `changeUsername should return no content and call service`() {
    val req = ChangeUsernameRequest("newName")
    val json = objectMapper.writeValueAsString(req)

    mockMvc.perform(
      patch("/users/me/username")
        .principal(principal)
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
        .principal(principal)
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
        .principal(principal)
    )
      .andExpect(status().isNoContent)

    verify(userService).deleteOwnAccount("testuser")
  }
}
