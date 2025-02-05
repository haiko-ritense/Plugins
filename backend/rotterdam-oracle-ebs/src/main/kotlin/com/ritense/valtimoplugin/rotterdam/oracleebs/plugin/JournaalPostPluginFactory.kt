package com.ritense.valtimoplugins.oracleebs.plugin

import com.ritense.plugin.PluginFactory
import com.ritense.plugin.service.PluginService
import com.ritense.valtimoplugin.rotterdam.oracleebs.plugin.JournaalPostPlugin
import com.ritense.valtimoplugin.rotterdam.oracleebs.service.EsbClient

class JournaalPostPluginFactory(
    pluginService: PluginService,
    private val esbClient: EsbClient
) : PluginFactory<JournaalPostPlugin>(pluginService) {

    override fun create(): JournaalPostPlugin {
        return JournaalPostPlugin(esbClient)
    }
}
