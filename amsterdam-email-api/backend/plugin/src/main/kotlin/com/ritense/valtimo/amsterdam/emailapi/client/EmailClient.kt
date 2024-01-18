package com.ritense.valtimo.amsterdam.emailapi.client

import org.springframework.web.client.RestTemplate
import java.net.URI

class EmailClient(
    private val restTemplate: RestTemplate,
    var baseUri: URI?,
    var apiKey: String?,
) {

    fun postMessage() {}
}
