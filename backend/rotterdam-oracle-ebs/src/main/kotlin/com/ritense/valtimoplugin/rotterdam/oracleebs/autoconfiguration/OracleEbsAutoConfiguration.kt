package com.ritense.valtimoplugin.rotterdam.oracleebs.autoconfiguration

import com.ritense.plugin.service.PluginService
import com.ritense.valtimoplugin.rotterdam.oracleebs.service.EsbClient
import com.ritense.valtimoplugins.oracleebs.plugin.JournaalPostPluginFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean

@AutoConfiguration
class OracleEbsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(JournaalPostPluginFactory::class)
    fun journaalPostPluginFactory(
        pluginService: PluginService,
        esbClient: EsbClient
    ) = JournaalPostPluginFactory(
        pluginService,
        esbClient
    )

    @Bean
    fun esbClient() = EsbClient()
}
