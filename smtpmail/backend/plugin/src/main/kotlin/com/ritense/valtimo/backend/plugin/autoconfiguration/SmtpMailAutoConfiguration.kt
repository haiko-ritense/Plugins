package com.ritense.valtimo.backend.plugin.autoconfiguration

import com.ritense.plugin.service.PluginService
import com.ritense.resource.service.TemporaryResourceStorageService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.ritense.valtimo.backend.plugin.client.SmtpMailClient
import com.ritense.valtimo.backend.plugin.service.SmtpMailService
import com.ritense.valtimo.backend.plugin.plugin.SmtpMailPluginFactory
import com.ritense.valueresolver.ValueResolverService

@Configuration
class SmtpMailAutoConfiguration {

    @Bean
    fun smtpMailClient(
        pluginService: PluginService,
        storageService: TemporaryResourceStorageService,
        ): SmtpMailClient = SmtpMailClient(
            pluginService = pluginService,
            storageService = storageService
        )

    @Bean
    fun smtpMailService(
        smtpMailClient: SmtpMailClient,
        storageService: TemporaryResourceStorageService
    ): SmtpMailService = SmtpMailService(
        smtpMailClient = smtpMailClient,
        storageService = storageService
    )

    @Bean
    fun smtpMailPluginFactory(
        pluginService: PluginService,
        smtpMailService: SmtpMailService,
        valueResolverService: ValueResolverService
    ): SmtpMailPluginFactory = SmtpMailPluginFactory(
            pluginService = pluginService,
            smtpMailService = smtpMailService,
            valueResolverService = valueResolverService
        )
}