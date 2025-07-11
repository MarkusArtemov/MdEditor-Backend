package de.hsfl.mdeditorbackend.common.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.Components
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
  @Bean
  fun openAPI(): OpenAPI {
    val schemeName = "bearerAuth"
    return OpenAPI()
      .addSecurityItem(SecurityRequirement().addList(schemeName))
      .components(
        Components().addSecuritySchemes(
          schemeName,
          SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
        )
      )
  }
}
