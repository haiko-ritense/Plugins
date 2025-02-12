package com.ritense.valtimoplugins.rotterdam.oracleebs.plugin

import com.ritense.plugin.PluginFactory
import com.ritense.plugin.service.PluginService
import com.ritense.valtimoplugins.rotterdam.oracleebs.service.EsbClient

class OracleEbsPluginFactory(
    pluginService: PluginService,
    private val esbClient: EsbClient
) : PluginFactory<OracleEbsPlugin>(pluginService) {

    override fun create(): OracleEbsPlugin {
        return OracleEbsPlugin(esbClient)
    }
}
