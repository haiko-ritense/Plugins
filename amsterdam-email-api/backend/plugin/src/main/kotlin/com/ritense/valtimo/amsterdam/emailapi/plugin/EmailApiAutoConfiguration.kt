package com.ritense.valtimo.amsterdam.emailapi.plugin

import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.amsterdam.emailapi.EmailService
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EmailApiAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(EmailApiPluginFactory::class)
    fun createAlfrescoAuthenticationPluginFactory(
        pluginService: PluginService,
        emailService: EmailService
    ): EmailApiPluginFactory {
        return EmailApiPluginFactory(pluginService, emailService)
    }

    @Bean
    @ConditionalOnMissingBean(EmailService::class)
    fun createEmailService(): EmailService {
        return EmailService()
    }

}
