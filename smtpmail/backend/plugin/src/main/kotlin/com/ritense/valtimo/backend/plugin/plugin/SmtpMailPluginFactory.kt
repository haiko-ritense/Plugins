package com.ritense.valtimo.backend.plugin.plugin

import com.ritense.plugin.PluginFactory
import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.backend.plugin.service.SmtpMailService
import com.ritense.valueresolver.ValueResolverService

class SmtpMailPluginFactory(
    pluginService: PluginService,
    private val smtpMailService: SmtpMailService,
    private val valueResolverService: ValueResolverService
): PluginFactory<SmtpMailPlugin>(pluginService) {

    override fun create(): SmtpMailPlugin = SmtpMailPlugin(smtpMailService, valueResolverService)
}