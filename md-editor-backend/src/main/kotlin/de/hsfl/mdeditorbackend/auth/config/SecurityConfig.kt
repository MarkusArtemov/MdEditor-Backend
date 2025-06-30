package de.hsfl.mdeditorbackend.auth.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke                    // ← Kotlin-DSL-Import
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val jwtTokenFilter: JwtTokenFilter
) {

    @Bean
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager =
        authConfig.authenticationManager

    @Bean
    @Order(1)
    fun h2ConsoleSecurity(http: HttpSecurity): SecurityFilterChain {
        http {
            securityMatcher("/h2-console/**")

            httpBasic { disable() }
            csrf     { disable() }

            headers {
                frameOptions { disable() }
            }

            authorizeHttpRequests {
                authorize(anyRequest, permitAll)
            }

            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.IF_REQUIRED
            }
        }
        return http.build()
    }

    @Bean
    @Order(2)
    fun apiSecurity(
        http: HttpSecurity,
        authConfig: AuthenticationConfiguration
    ): SecurityFilterChain {
        http {
            securityMatcher("/**")

            httpBasic { disable() }
            csrf     { disable() }

            headers {
                frameOptions { disable() }
            }

            authorizeHttpRequests {
                authorize("/auth/**",       permitAll)
                authorize("/swagger-ui/**",  permitAll)
                authorize("/v3/api-docs/**", permitAll)
                authorize(anyRequest,        authenticated)
            }

            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }

            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtTokenFilter)
        }

        return http.build()
    }
}
