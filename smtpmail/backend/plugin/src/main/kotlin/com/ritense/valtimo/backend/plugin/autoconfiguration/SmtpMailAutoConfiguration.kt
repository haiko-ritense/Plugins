package com.ritense.valtimo.backend.plugin.autoconfiguration

import com.ritense.plugin.service.PluginService
import com.ritense.resource.service.TemporaryResourceStorageService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.ritense.valtimo.backend.plugin.client.SmtpMailClient
import com.ritense.valtimo.backend.plugin.service.SmtpMailService
import com.ritense.valtimo.backend.plugin.plugin.SmtpMailPluginFactory

@Configuration
class SmtpMailAutoConfiguration {

    @Bean
    fun zivverClient(pluginService: PluginService): SmtpMailClient =
        SmtpMailClient(pluginService = pluginService)

    @Bean
    fun zivverService(
        smtpMailClient: SmtpMailClient,
        storageService: TemporaryResourceStorageService
    ): SmtpMailService = SmtpMailService(
        smtpMailClient = smtpMailClient,
        storageService = storageService
    )

    @Bean
    fun zivverPluginFactory(
        pluginService: PluginService,
        smtpMailService: SmtpMailService
    ): SmtpMailPluginFactory =
        SmtpMailPluginFactory(
            pluginService = pluginService,
            smtpMailService = smtpMailService
        )


}