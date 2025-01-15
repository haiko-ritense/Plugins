package com.ritense.valtimoplugin.rotterdam.oracleebs.autoconfiguration

import com.ritense.valtimoplugins.oracleebs.plugin.JournaalPostPluginFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean

@AutoConfiguration
class OracleEbsAutoConfiguration {

    @Bean
    fun journaalPostPluginFactory() = JournaalPostPluginFactory()
}
