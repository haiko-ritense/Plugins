package com.ritense.valtimo.amsterdam.emailapi.plugin

import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.amsterdam.emailapi.client.EmailClient
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

@Configuration
class EmailApiAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(EmailApiPluginFactory::class)
    fun createEmailApiPluginFactory(
        pluginService: PluginService,
        emailClient: EmailClient
    ): EmailApiPluginFactory {
        return EmailApiPluginFactory(pluginService, emailClient)
    }

    @Bean
    @ConditionalOnMissingBean(EmailClient::class)
    fun createEmailClient(restTemplate: RestTemplate, publisher: ApplicationEventPublisher): EmailClient {
        return EmailClient(restTemplate, publisher)
    }

    @Bean
    @ConditionalOnMissingBean(RestTemplate::class)
    fun createRestTemplate(): RestTemplate {
        val httpClient: CloseableHttpClient =
            HttpClients.custom()
                .setSSLHostnameVerifier(NoopHostnameVerifier())
                .build()

        val requestFactory = HttpComponentsClientHttpRequestFactory()
        requestFactory.httpClient = httpClient

        return RestTemplate(requestFactory)
    }
}
