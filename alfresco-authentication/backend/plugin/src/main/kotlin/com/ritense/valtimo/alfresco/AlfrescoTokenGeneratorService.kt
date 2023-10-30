package com.ritense.valtimo.alfresco

import com.ritense.valtimo.contract.utils.SecurityUtils
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.nio.charset.Charset
import java.util.Date
import mu.KotlinLogging

private const val FALLBACK_USER = "System"
private val logger = KotlinLogging.logger {}

class AlfrescoTokenGeneratorService {

    fun generateToken(secretKey: String, clientId: String): String {
        logger.debug { "generating a token" }

        if (secretKey.length < 32) {
            throw IllegalStateException("SecretKey needs to be at least 32 in length")
        }
        val signingKey = Keys.hmacShaKeyFor(secretKey.toByteArray(Charset.forName("UTF-8")))

        val jwtBuilder = Jwts.builder()
        jwtBuilder
            .setIssuer(clientId)
            .setIssuedAt(Date())
            .claim("client_id", clientId)

        appendUserInfo(jwtBuilder)
        return jwtBuilder
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact()
    }

    private fun appendUserInfo(jwtBuilder: JwtBuilder) {
        val authenticated = SecurityUtils.isAuthenticated()
        val userLogin = if(authenticated) SecurityUtils.getCurrentUserLogin() else FALLBACK_USER
        val userId = userLogin ?: FALLBACK_USER

        jwtBuilder
            .claim("user_id", userId)
            .claim("user_representation", "")

        if (authenticated) {
            val roles = SecurityUtils.getCurrentUserRoles()
            if(!roles.isNullOrEmpty()) {
                jwtBuilder.claim("roles", roles)
            }
        }
    }
}
