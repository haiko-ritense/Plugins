package com.ritense.valtimo.slack.listener

import com.fasterxml.jackson.databind.node.ObjectNode
import com.ritense.document.domain.event.DocumentDefinitionDeployedEvent
import com.ritense.document.service.DocumentDefinitionService
import com.ritense.plugin.domain.PluginConfiguration
import com.ritense.plugin.service.PluginConfigurationSearchParameters
import com.ritense.plugin.service.PluginService
import com.ritense.plugin.web.rest.request.PluginProcessLinkCreateDto
import com.ritense.processlink.domain.ActivityTypeWithEventName.SERVICE_TASK_START
import com.ritense.valtimo.contract.authentication.AuthoritiesConstants
import com.ritense.valtimo.contract.json.Mapper
import org.camunda.bpm.engine.RepositoryService
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ApplicationReadyEventListener(
    private val pluginService: PluginService,
    private val repositoryService: RepositoryService,
    private val documentDefinitionService: DocumentDefinitionService,
) {

    @EventListener(ApplicationReadyEvent::class)
    fun handleApplicationReady() {
        val slackConfig = createSlackPluginConfiguration()
        linkSlackToProcess(slackConfig)
    }

    @EventListener(DocumentDefinitionDeployedEvent::class)
    fun handleDocumentDefinitionDeployed(event: DocumentDefinitionDeployedEvent) {
        setDocumentDefinitionRole(event)
    }

    private fun createSlackPluginConfiguration(): PluginConfiguration {
        val slackPluginConfig = pluginService.getPluginConfigurations(PluginConfigurationSearchParameters())
            .firstOrNull { it.title == "Slack configuration" }

        if (slackPluginConfig != null) {
            return slackPluginConfig
        }

        val slackConfigurationProperties = """
            {
                "url": "https://www.slack.com/",
                "token": "xoxb-fake-token"
            }"""

        return pluginService.createPluginConfiguration(
            "Slack configuration",
            Mapper.INSTANCE.get().readTree(slackConfigurationProperties) as ObjectNode,
            "slack"
        )
    }

    private fun linkSlackToProcess(slackConfig: PluginConfiguration) {
        val actionProperties = """
            {
                "channel": "AAAAA1111",
                "message": "Hello world!"
            }"""

        val processDefinition = repositoryService.createProcessDefinitionQuery()
            .processDefinitionKey("SlackMessageExample")
            .latestVersion()
            .singleResult()

        if (pluginService.getProcessLinks(processDefinition.id, "SendSlackMessage").isEmpty()) {
            pluginService.createProcessLink(
                PluginProcessLinkCreateDto(
                    processDefinition.id,
                    "SendSlackMessage",
                    slackConfig.id.id,
                    "post-message",
                    Mapper.INSTANCE.get().readTree(actionProperties) as ObjectNode,
                    SERVICE_TASK_START,
                )
            )
        }
    }

    private fun setDocumentDefinitionRole(event: DocumentDefinitionDeployedEvent) {
        documentDefinitionService.putDocumentDefinitionRoles(
            event.documentDefinition().id().name(),
            setOf(AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER)
        )
    }
}
