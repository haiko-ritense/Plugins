package com.ritense.valtimoplugins.rotterdam.oracleebs.service

import com.rotterdam.esb.opvoeren.apis.JournaalpostenApi
import com.rotterdam.esb.opvoeren.apis.VerkoopfacturenApi
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestClient

class EsbClient {

    fun createRestClient(
        baseUrl: String,
        authenticationEnabled: Boolean,
        base64PrivateKey: String? = null,
        base64ClientCert: String? = null,
        base64ServerCert: String? = null
    ): RestClient {
        logger.debug { "Creating ESB client" }
        return when {
            authenticationEnabled -> {
                require(base64PrivateKey != null)
                require(base64ClientCert != null)
                require(base64ServerCert != null)
                HttpClientHelper.createSecureHttpClient(
                    base64PrivateKey = base64PrivateKey,
                    base64ClientCert = base64ClientCert,
                    base64CaCert = base64ServerCert
                ).also {
                    logger.debug { "Using secure HttpClient with Client Certificate authentication" }
                }
            }
            else ->
                HttpClientHelper.createDefaultHttpClient().also {
                    logger.debug { "Using default HttpClient" }
                }
        }.let { httpClient ->
            RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(HttpComponentsClientHttpRequestFactory(httpClient))
                .build().also {
                    logger.debug { "Created ESB client using RestClient" }
                }
        }
    }

    fun journaalPostenApi(restClient: RestClient) = JournaalpostenApi(restClient)

    fun verkoopFacturenApi(restClient: RestClient) = VerkoopfacturenApi(restClient)

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
