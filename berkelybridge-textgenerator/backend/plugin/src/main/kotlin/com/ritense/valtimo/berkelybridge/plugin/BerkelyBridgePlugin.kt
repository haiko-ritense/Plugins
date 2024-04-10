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
import com.ritense.documentenapi.client.DocumentStatusType
import com.ritense.resource.domain.MetadataType
import com.ritense.resource.domain.TemporaryResourceUploadedEvent
import com.ritense.resource.service.TemporaryResourceStorageService
import com.ritense.valtimo.berkelybridge.client.BerkelyBridgeClient
import com.ritense.valtimo.berkelybridge.client.logger
import com.ritense.valtimo.contract.utils.SecurityUtils

import com.ritense.valueresolver.ValueResolverService
import mu.KotlinLogging


import org.camunda.bpm.engine.delegate.DelegateExecution
import org.hibernate.validator.constraints.Length
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.ResponseEntity
import java.net.URL

private val logger = KotlinLogging.logger {}

@Plugin(
    key = "bbtextgenerator",
    title = "Berkely Bridge tekst en PDF generator",
    description = "Berkely Bridge genereert text of PDF documenten"
)
class BerkelyBridgePlugin(
    private val bbClient: BerkelyBridgeClient,
    private val valueResolverService: ValueResolverService,
    private val resourceService: TemporaryResourceStorageService,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {

    @PluginProperty(key = "berkelybridgeBaseUrl", secret = false, required = true)
    lateinit var berkelybridgeBaseUrl: String

    @Length(min = 9, max = 9)
    @PluginProperty(key = "bronorganisatie", secret = false)
    lateinit var bronorganisatie: String

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
        @PluginActionProperty parameters: List<TemplateProperty>?,
        @PluginActionProperty format: String,
        @PluginActionProperty naam: String,
        @PluginActionProperty variabeleNaam: String
    ) {
        val text = bbClient.generate(bbUrl = berkelybridgeBaseUrl, modelId = modelId, templateId = templateId,
            parameters = resolveValue(execution, parameters),
            naam = naam,
            format = format)

            execution.setVariable(variabeleNaam, text);
    }

    @PluginAction(
        key = "genereer-file-documenten-api",
        title = "Genereer een file",
        description = "Genereer een file die wordt opgeslagen in de documenten API",
        activityTypes = [ActivityType.SERVICE_TASK_START]
    )
    fun generateFile(
        execution: DelegateExecution,
        @PluginActionProperty modelId: String,
        @PluginActionProperty templateId: String,
        @PluginActionProperty parameters: List<TemplateProperty>?,
        @PluginActionProperty format: String,
        @PluginActionProperty naam: String,
        @PluginActionProperty taal: String,
        @PluginActionProperty beschrijving: String,
        @PluginActionProperty informatieObjectType: String,
        @PluginActionProperty variabeleNaam: String,
        @PluginActionProperty status: DocumentStatusType = DocumentStatusType.DEFINITIEF
    ) {
        val downloadLink = bbClient.generateFile(bbUrl = berkelybridgeBaseUrl, modelId = modelId, templateId = templateId,
            parameters = resolveValue(execution, parameters),
            naam = naam,
            format = format)

        val mutableMetaData = mutableMapOf<String, Any>()
        mutableMetaData.put(MetadataType.FILE_NAME.key, naam)
        mutableMetaData.put(MetadataType.CONTENT_TYPE.key, format)
        SecurityUtils.getCurrentUserLogin()?.let { mutableMetaData.putIfAbsent(MetadataType.USER.key, it) }

        val resourceId = resourceService.store( getFileAsByteArray(downloadLink).inputStream(), mutableMetaData)
        applicationEventPublisher.publishEvent(TemporaryResourceUploadedEvent(resourceId))

        return ResponseEntity.ok(
            ResourceDto(
                resourceId,
                mutableMetaData[MetadataType.FILE_NAME.key] as String?,
                file.size
            )
        )

        execution.setVariable(variabeleNaam, downloadLink);
    }

    private fun resolveValue(execution: DelegateExecution, keyValueList: List<TemplateProperty>?): List<TemplateProperty>? {
        return if (keyValueList == null) {
            null
        } else {
            keyValueList.map {
                var resolvedValues = valueResolverService.resolveValues(
                    execution.processInstanceId,
                    execution,
                    listOf(it.value)
                )
                var resolvedValue = resolvedValues[it.value]
                TemplateProperty(it.key, resolvedValue as String)
            }
        }
    }

    private fun getFileAsByteArray(bbUrl: String, fileUrl: String): ByteArray {
        try {
            logger.debug { "getting file for fileUrl: $fileUrl " }

            val getFileUrl = URL("$bbUrl/$fileUrl")
            val fileData = getFileUrl.readBytes();
            return fileData;
        } catch (e: Exception) {
            logger.error { "error berkely bridge retrieving file  \n" + e.message }
            throw e
        }
    }
}
