package com.ritense.valtimoplugins.rotterdam.oracleebs.service

import com.rotterdam.esb.opvoeren.apis.JournaalpostenApi
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestClient

class EsbClient {

    fun createRestClient(
        baseUrl: String,
        base64PrivateKey: String? = null,
        base64ClientCert: String? = null,
        base64ServerCert: String? = null
    ): RestClient {
        logger.debug { "Creating ESB client" }
        return when {
            (base64PrivateKey != null && base64ClientCert != null && base64ServerCert != null) ->
                HttpClientHelper.createSecureHttpClient(
                    base64PrivateKey = base64PrivateKey,
                    base64ClientCert = base64ClientCert,
                    base64CaCert = base64ServerCert
                ).also {
                    logger.debug { "Using secure HttpClient with Client Certificate authentication" }
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

    fun journaalPostApi(restClient: RestClient) = JournaalpostenApi(restClient)

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
