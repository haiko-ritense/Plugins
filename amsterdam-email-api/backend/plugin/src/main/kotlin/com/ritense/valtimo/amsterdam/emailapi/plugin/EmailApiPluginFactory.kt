package com.ritense.valtimo.amsterdam.emailapi.plugin

import com.ritense.plugin.PluginFactory
import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.amsterdam.emailapi.client.EmailClient

class EmailApiPluginFactory(
    pluginService: PluginService,
    val emailClient: EmailClient,
) : PluginFactory<EmailApiPlugin>(pluginService) {

    override fun create(): EmailApiPlugin {
        return EmailApiPlugin(emailClient)
    }
}
