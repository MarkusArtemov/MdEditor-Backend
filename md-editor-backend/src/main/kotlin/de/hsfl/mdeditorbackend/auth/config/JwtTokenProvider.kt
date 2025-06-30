package de.hsfl.mdeditorbackend.auth.config

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.security.Key
import java.util.Date

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import org.springframework.security.core.Authentication
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService


@Component
class JwtTokenProvider(
    @Value("\${security.jwt.secret}") private val secret: String,
    @Value("\${security.jwt.expiration}") private val validityInMs: Long,
    private val userDetailsService: UserDetailsService
) {
    private val key: Key = Keys.hmacShaKeyFor(secret.toByteArray())

    fun createToken(username: String, roles: List<String>): String {
        val claims = Jwts.claims().setSubject(username)
        claims["roles"] = roles
        val now = Date()
        val expiry = Date(now.time + validityInMs)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val userDetails = userDetailsService.loadUserByUsername(getUsername(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun getUsername(token: String): String =
        Jwts.parserBuilder().setSigningKey(key).build()
            .parseClaimsJws(token)
            .body.subject

    fun validateToken(token: String): Boolean =
        try {
            val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            !claims.body.expiration.before(Date())
        } catch (_: JwtException) {
            false
        }
}
