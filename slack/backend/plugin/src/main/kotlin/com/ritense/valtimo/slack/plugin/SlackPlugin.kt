/*
 * Copyright 2015-2022 Ritense BV, the Netherlands.
 *
 * Licensed under EUPL, Version 1.2 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ritense.valtimo.slack.plugin

import com.ritense.plugin.annotation.Plugin
import com.ritense.plugin.annotation.PluginAction
import com.ritense.plugin.annotation.PluginActionProperty
import com.ritense.plugin.annotation.PluginProperty
import com.ritense.plugin.domain.ActivityType
import com.ritense.processlink.domain.ActivityTypeWithEventName
import com.ritense.resource.domain.MetadataType
import com.ritense.resource.service.TemporaryResourceStorageService
import com.ritense.valtimo.slack.client.SlackClient
import com.ritense.valueresolver.ValueResolverService
import java.net.URI
import org.camunda.bpm.engine.delegate.DelegateExecution

@Plugin(
    key = "slack",
    title = "Slack Plugin",
    description = "Post message with the Slack plugin"
)
class SlackPlugin(
    private val slackClient: SlackClient,
    private val storageService: TemporaryResourceStorageService,
    private val valueResolverService: ValueResolverService,
) {

    @PluginProperty(key = "url", secret = false)
    lateinit var url: URI

    @PluginProperty(key = "token", secret = true)
    lateinit var token: String

    @PluginAction(
        key = "post-message",
        title = "Post message",
        description = "Sends a message to a Slack channel",
        activityTypes = [ActivityTypeWithEventName.SERVICE_TASK_START]
    )
    fun postMessage(
        execution: DelegateExecution,
        @PluginActionProperty channel: String,
        @PluginActionProperty message: String
    ) {
        slackClient.baseUri = url
        slackClient.token = token
        slackClient.chatPostMessage(
            channel = channel,
            message = resolveValue(execution, message)!!,
        )
    }

    @PluginAction(
        key = "post-message-with-file",
        title = "Post message with file",
        description = "Sends a message to a channel with a file",
        activityTypes = [ActivityTypeWithEventName.SERVICE_TASK_START]
    )
    fun postMessageWithFile(
        execution: DelegateExecution,
        @PluginActionProperty channels: String,
        @PluginActionProperty message: String?,
        @PluginActionProperty fileName: String?,
    ) {
        val resourceId = execution.getVariable(RESOURCE_ID_PROCESS_VAR) as String?
            ?: throw IllegalStateException("Failed to post slack message. No process variable '$RESOURCE_ID_PROCESS_VAR' found.")
        val contentAsInputStream = storageService.getResourceContentAsInputStream(resourceId)
        val metadata = storageService.getResourceMetadata(resourceId)

        slackClient.baseUri = url
        slackClient.token = token
        slackClient.filesUpload(
            channels = channels,
            message = resolveValue(execution, message),
            fileName = fileName ?: metadata[MetadataType.FILE_NAME.key] as String,
            file = contentAsInputStream
        )
    }

    private fun resolveValue(execution: DelegateExecution, value: String?): String? {
        return if (value == null) {
            null
        } else {
            val resolvedValues = valueResolverService.resolveValues(
                execution.processInstanceId,
                execution,
                listOf(value)
            )
            resolvedValues[value] as String?
        }
    }

    companion object {
        const val RESOURCE_ID_PROCESS_VAR = "resourceId"
    }
}
