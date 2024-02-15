package com.ritense.valtimo.amsterdam.emailapi.plugin

import com.ritense.plugin.PluginFactory
import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.amsterdam.emailapi.client.EmailClient
import com.ritense.valueresolver.ValueResolverService
import org.springframework.web.client.RestTemplate

class EmailApiPluginFactory(
    pluginService: PluginService,
    val emailClient: EmailClient,
    val restTemplate: RestTemplate,
    val valueResolver: ValueResolverService
) : PluginFactory<EmailApiPlugin>(pluginService) {

    override fun create(): EmailApiPlugin {
        return EmailApiPlugin(emailClient, restTemplate, valueResolver)
    }
}
