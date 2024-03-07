/*
 * Copyright 2015-2024. Ritense BV, the Netherlands.
 *
 * Licensed under EUPL, Version 1.2 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" basis,
 *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


package com.ritense.valtimo.berkelybridge.plugin

import com.ritense.plugin.annotation.Plugin
import com.ritense.plugin.annotation.PluginAction
import com.ritense.plugin.annotation.PluginActionProperty
import com.ritense.plugin.annotation.PluginProperty
import com.ritense.plugin.domain.ActivityType
import com.ritense.valtimo.berkelybridge.client.BerkelyBridgeClient

import com.ritense.valueresolver.ValueResolverService
import org.camunda.bpm.engine.delegate.DelegateExecution

@Plugin(
    key = "bbtextgenerator",
    title = "Berkely Bridge tekst en PDF generator",
    description = "Berkely Bridge genereert text of PDF documenten"
)
class BerkelyBridgePlugin(
    private val bbClient: BerkelyBridgeClient,
    private val valueResolverService: ValueResolverService
) {

    @PluginProperty(key = "berkelybridgeBaseUrl", secret = false, required = true)
    lateinit var berkelybridgeBaseUrl: String

    @PluginAction(
        key = "genereer-tekst",
        title = "Genereer tekst",
        description = "Genereer tekst met template en parameters. Specifeer met format parameter of de output html of text moet zijn",
        activityTypes = [ActivityType.SERVICE_TASK_START]
    )
    fun generateText(
        execution: DelegateExecution,
        @PluginActionProperty modelId: String,
        @PluginActionProperty templateId: String,
        @PluginActionProperty parameters: String?,
        @PluginActionProperty format: String,
        @PluginActionProperty naam: String,
    ) {
        bbClient.generate(bbUrl = berkelybridgeBaseUrl, modelId = resolveValue(execution, modelId) as String, templateId = resolveValue(execution, templateId) as String,
            parameters = resolveValue(execution, parameters) as List<TemplateProperty>,
            naam = resolveValue(execution, naam) as String,
            format = resolveValue(execution, format) as String)

    }

    private fun resolveValue(execution: DelegateExecution, value: String?): Any? {
        return if (value == null) {
            null
        } else {
            val resolvedValues = valueResolverService.resolveValues(
                execution.processInstanceId,
                execution,
                listOf(value)
            )
            resolvedValues[value]
        }
    }
}