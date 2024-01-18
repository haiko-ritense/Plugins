package com.ritense.valtimo.alfresco

import com.ritense.valtimo.amsterdam.emailapi.plugin.EmailApiPlugin
import com.ritense.valtimo.amsterdam.emailapi.AlfrescoTokenGeneratorService
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.mockito.kotlin.*
import org.springframework.http.HttpMethod
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFunction
import reactor.core.publisher.Mono
import java.net.URI

internal class AlfrescoAuthenticationPluginTest {

    @Test
    fun shouldAddToken() {
        val requestCaptor = argumentCaptor<ClientRequest>()
        val tokenGeneratorService = mock<AlfrescoTokenGeneratorService>()
        val plugin = EmailApiPlugin   (tokenGeneratorService)
        plugin.clientId = "test"
        plugin.clientSecret = "test"

        whenever(tokenGeneratorService.generateToken("test", "test")).thenReturn("token")

        val request = ClientRequest.create(HttpMethod.GET, URI.create("http://some-url.tld")).build()
        val nextFilter = mock<ExchangeFunction>()
        val response = mock<Mono<ClientResponse>>()
        whenever(nextFilter.exchange(any())).thenReturn(response)

        plugin.filter(request, nextFilter)

        verify(nextFilter).exchange(requestCaptor.capture())
        assertEquals("Bearer token", requestCaptor.firstValue.headers()["Authorization"]?.get(0))
    }
}
