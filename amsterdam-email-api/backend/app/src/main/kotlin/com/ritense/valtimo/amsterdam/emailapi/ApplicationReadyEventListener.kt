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


package com.ritense.valtimo.amsterdam.emailapi

import com.fasterxml.jackson.databind.node.ObjectNode
import com.ritense.document.domain.event.DocumentDefinitionDeployedEvent
import com.ritense.document.service.DocumentDefinitionService
import com.ritense.plugin.domain.PluginConfiguration
import com.ritense.plugin.service.PluginConfigurationSearchParameters
import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.contract.authentication.AuthoritiesConstants
import com.ritense.valtimo.contract.json.Mapper
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ApplicationReadyEventListener(
    private val pluginService: PluginService,
    private val documentDefinitionService: DocumentDefinitionService,
) {

    @EventListener(ApplicationReadyEvent::class)
    fun handleApplicationReady() {
        val config = createPluginConfiguration()
    }

    private fun createPluginConfiguration(): PluginConfiguration {
        val pluginConfig = pluginService.getPluginConfigurations(PluginConfigurationSearchParameters())
            .firstOrNull { it.title == "Email API Amsterdam" }

        if (pluginConfig != null) {
            return pluginConfig
        }

        val configurationProperties = """
            {   
                "emailApiBaseUrl": "https://erfpacht-gwextern-ont.amsterdam.nl/hera/mail/api/v1/mail",
                "clientId": "erfpachtmedewerker-portaal-m2m",
                "clientSecret": "VzvTmec67g4NFs1pGxCZv89R6sdq7rG9",
                "tokenEndpoint": "https://id-erfpacht-ont.amsterdam.nl/auth/realms/hera/protocol/openid-connect/token"
            }"""

        return pluginService.createPluginConfiguration(
            "Email API Amsterdam",
            Mapper.INSTANCE.get().readTree(configurationProperties) as ObjectNode,
            "amsterdamemailapi"
        )
    }
}
