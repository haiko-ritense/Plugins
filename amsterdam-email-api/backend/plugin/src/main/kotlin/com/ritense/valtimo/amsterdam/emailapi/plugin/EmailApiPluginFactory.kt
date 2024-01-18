package com.ritense.valtimo.amsterdam.emailapi.plugin

import com.ritense.plugin.PluginFactory
import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.amsterdam.emailapi.EmailService

class EmailApiPluginFactory(
    pluginService: PluginService,
    val emailService: EmailService,
) : PluginFactory<EmailApiPlugin>(pluginService) {

    override fun create(): EmailApiPlugin {
        return EmailApiPlugin(emailService)
    }
}
