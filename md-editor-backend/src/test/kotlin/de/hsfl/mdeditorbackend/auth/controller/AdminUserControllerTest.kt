package de.hsfl.mdeditorbackend.auth.controller

import com.fasterxml.jackson.databind.ObjectMapper
import de.hsfl.mdeditorbackend.auth.controller.AdminUserController
import de.hsfl.mdeditorbackend.auth.model.dto.ChangeRoleRequest
import de.hsfl.mdeditorbackend.auth.model.entity.Role
import de.hsfl.mdeditorbackend.auth.service.UserService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(MockitoExtension::class)
class AdminUserControllerTest {

    private lateinit var mockMvc: MockMvc
    private val objectMapper = ObjectMapper()

    @Mock
    private lateinit var userService: UserService

    @InjectMocks
    private lateinit var adminUserController: AdminUserController

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminUserController).build()
    }

    @Test
    fun `changeRole should return no content and call service`() {
        val id = 1L
        val req = ChangeRoleRequest(Role.ADMIN)
        val json = objectMapper.writeValueAsString(req)

        mockMvc.perform(
            patch("/admin/users/$id/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isNoContent)

        verify(userService).updateRole(id, Role.ADMIN)
    }

    @Test
    fun `deleteUser should return no content and call service`() {
        val id = 2L

        mockMvc.perform(
            delete("/admin/users/$id")
        )
            .andExpect(status().isNoContent)

        verify(userService).deleteUser(id)
    }
}