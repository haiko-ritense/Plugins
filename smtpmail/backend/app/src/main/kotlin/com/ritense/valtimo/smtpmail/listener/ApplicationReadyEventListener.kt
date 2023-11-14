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

package com.ritense.valtimo.smtpmail.listener

import com.fasterxml.jackson.databind.node.ObjectNode
import com.ritense.document.domain.event.DocumentDefinitionDeployedEvent
import com.ritense.document.service.DocumentDefinitionService
import com.ritense.plugin.domain.PluginConfiguration
import com.ritense.plugin.service.PluginConfigurationSearchParameters
import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.contract.authentication.AuthoritiesConstants
import com.ritense.valtimo.contract.json.Mapper
import org.camunda.bpm.engine.RepositoryService
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component


//You can use this Listener to start up Valtimo with a preconfigured Plugin and plugin action

@Component
class ApplicationReadyEventListener(
    private val pluginService: PluginService,
    private val repositoryService: RepositoryService,
    private val documentDefinitionService: DocumentDefinitionService,
) {

    @EventListener(ApplicationReadyEvent::class)
    fun handleApplicationReady() {
//        val smtpMailConfig = createSmtpMailPluginConfiguration()
//        linkSmtpMailToProcess(smtpMailConfig)
    }

    @EventListener(DocumentDefinitionDeployedEvent::class)
    fun handleDocumentDefinitionDeployed(event: DocumentDefinitionDeployedEvent) {
        setDocumentDefinitionRole(event)
    }

    private fun createSmtpMailPluginConfiguration(): PluginConfiguration {
        val smtpMailPluginConfig = pluginService.getPluginConfigurations(PluginConfigurationSearchParameters())
            .firstOrNull { it.title == "SMTP mail configuration" }

        if (smtpMailPluginConfig != null) {
            return smtpMailPluginConfig
        }

        val smtpMailConfigurationProperties = """
            {
                "host": "",
                "port": "",
            }"""

        return pluginService.createPluginConfiguration(
            "SMTP mail configuration",
            Mapper.INSTANCE.get().readTree(smtpMailConfigurationProperties) as ObjectNode,
            "smtp-mail"
        )
    }

//    private fun linkSmtpMailToProcess(smtpMailConfig: PluginConfiguration) {
//
//        val processDefinition = repositoryService.createProcessDefinitionQuery()
//            .processDefinitionKey("SmtpMailExample")
//            .latestVersion()
//            .singleResult()
//
//        if (pluginService.getProcessLinks(processDefinition.id, "SmtpMailExample").isEmpty()) {
//            pluginService.createProcessLink(
//                PluginProcessLinkCreateDto(
//                    processDefinition.id,
//                    "SendSmtpMail",
//                    smtpMailConfig.id.id,
//                    "send-mail",
//                    Mapper.INSTANCE.get().readTree(actionProperties) as ObjectNode,
//                    "bpmn:ServiceTask:start",
//                )
//            )
//        }
//    }

    private fun setDocumentDefinitionRole(event: DocumentDefinitionDeployedEvent) {
        documentDefinitionService.putDocumentDefinitionRoles(
            event.documentDefinition().id().name(),
            setOf(AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER)
        )
    }
}
