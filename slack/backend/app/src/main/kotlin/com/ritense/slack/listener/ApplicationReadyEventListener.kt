package com.ritense.slack.listener

import com.ritense.document.domain.event.DocumentDefinitionDeployedEvent
import com.ritense.document.service.DocumentDefinitionService
import com.ritense.valtimo.contract.authentication.AuthoritiesConstants
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ApplicationReadyEventListener(
    private val documentDefinitionService: DocumentDefinitionService,
) {

    @EventListener(DocumentDefinitionDeployedEvent::class)
    fun handleDocumentDefinitionDeployed(event: DocumentDefinitionDeployedEvent) {
        setDocumentDefinitionRole(event)
    }

    fun setDocumentDefinitionRole(event: DocumentDefinitionDeployedEvent) {
        documentDefinitionService.putDocumentDefinitionRoles(
            event.documentDefinition().id().name(),
            setOf(AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER)
        )
    }
}
