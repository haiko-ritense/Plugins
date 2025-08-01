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

package com.ritense.valtimoplugins.documents.plugin

import com.ritense.documentenapi.client.DocumentenApiClient
import com.ritense.plugin.PluginFactory
import com.ritense.plugin.service.PluginService
import org.springframework.context.ApplicationEventPublisher

class DocumentsXtraPluginFactory(
    pluginService: PluginService,
    val client: DocumentenApiClient,
    val applicationEventPublisher: ApplicationEventPublisher,
) : PluginFactory<DocumentsXtraPlugin>(pluginService) {

    override fun create(): DocumentsXtraPlugin {
        return DocumentsXtraPlugin(client, applicationEventPublisher)
    }
}
