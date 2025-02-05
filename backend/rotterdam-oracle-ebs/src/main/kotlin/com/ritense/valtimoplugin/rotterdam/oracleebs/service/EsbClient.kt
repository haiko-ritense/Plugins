package com.ritense.valtimoplugin.rotterdam.oracleebs.service

import com.rotterdam.opvoeren.apis.JournaalpostenApi
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestClient
import java.io.ByteArrayInputStream
import java.security.KeyFactory
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.spec.PKCS8EncodedKeySpec
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory


class EsbClient {

    fun configureRestClient(
        base64PrivateKey: String? = null,
        base64ClientCertificate: String? = null,
        base64ServerCertificate: String? = null
    ): RestClient {
        return when {
            (base64PrivateKey != null && base64ClientCertificate != null && base64ServerCertificate != null) ->
                createSecureHttpClient(base64PrivateKey, base64ClientCertificate, base64ServerCertificate)
            else ->
                createHttpClient()
        }.let { httpClient ->
            RestClient.builder()
                .requestFactory ( HttpComponentsClientHttpRequestFactory(httpClient) )
                .build()
        }
    }

    private fun createHttpClient(): CloseableHttpClient {
        return HttpClients.createDefault()
    }

    private fun createSecureHttpClient(
        base64PrivateKey: String,
        base64ClientCertificate: String,
        base64ServerCertificate: String
    ): CloseableHttpClient {
        // Decode Base64 strings
        val privateKeyBytes = java.util.Base64.getDecoder().decode(base64PrivateKey)
        val clientCertBytes = java.util.Base64.getDecoder().decode(base64ClientCertificate)
        val serverCertBytes = java.util.Base64.getDecoder().decode(base64ServerCertificate)

        // Load client certificate
        val certificateFactory = CertificateFactory.getInstance("X.509")
        val clientCertificate = certificateFactory.generateCertificate(ByteArrayInputStream(clientCertBytes))

        // Load private key
        val keyFactory = KeyFactory.getInstance("RSA") // Or "EC" for elliptic curve keys
        val privateKeySpec = PKCS8EncodedKeySpec(privateKeyBytes)
        val privateKey = keyFactory.generatePrivate(privateKeySpec)

        // Create KeyStore for the client certificate and private key
        val keyStore = KeyStore.getInstance("PKCS12").apply {
            load(null, null) // Initialize empty keystore
            setKeyEntry("client", privateKey, null, arrayOf(clientCertificate))
        }

        // Initialize KeyManagerFactory
        val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()).apply {
            init(keyStore, charArrayOf()) // No password needed for the private key
        }

        // Load server certificate into TrustStore
        val trustStore = KeyStore.getInstance("PKCS12").apply {
            load(null, null) // Initialize empty keystore
            setCertificateEntry("server", certificateFactory.generateCertificate(ByteArrayInputStream(serverCertBytes)))
        }

        // Initialize TrustManagerFactory
        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
            init(trustStore)
        }

        // Create SSLContext with client authentication
        val sslContext = SSLContext.getInstance("TLS").apply {
            init(keyManagerFactory.keyManagers, trustManagerFactory.trustManagers, null)
        }

        TODO("return http client")
    }

    fun journaalPostApi(restClient: RestClient) = JournaalpostenApi(restClient)
}
