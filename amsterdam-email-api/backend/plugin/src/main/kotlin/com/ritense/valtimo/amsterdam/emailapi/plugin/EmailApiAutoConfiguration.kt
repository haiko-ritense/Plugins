package com.ritense.valtimo.amsterdam.emailapi.plugin

import com.ritense.plugin.repository.PluginProcessLinkRepository
import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.amsterdam.emailapi.client.EmailClient
import com.ritense.valtimo.processlink.ProcessLinkServiceTaskStartListener
import com.ritense.valueresolver.ValueResolverService
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
    @ConditionalOnMissingBean(ProcessLinkSendTaskStartListener::class)
    fun pluginLinkSendTaskStartListener(
        pluginProcessLinkRepository: PluginProcessLinkRepository?,
        pluginService: PluginService?
    ): ProcessLinkSendTaskStartListener {
        return ProcessLinkSendTaskStartListener(
            pluginProcessLinkRepository!!,
            pluginService!!
        )
    }

    @Bean
    @ConditionalOnMissingBean(EmailApiPluginFactory::class)
    fun createEmailApiPluginFactory(
        pluginService: PluginService,
        emailClient: EmailClient,
        restTemplate: RestTemplate,
        valueResolver: ValueResolverService,
    ): EmailApiPluginFactory {
        return EmailApiPluginFactory(pluginService, emailClient, restTemplate, valueResolver)
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
