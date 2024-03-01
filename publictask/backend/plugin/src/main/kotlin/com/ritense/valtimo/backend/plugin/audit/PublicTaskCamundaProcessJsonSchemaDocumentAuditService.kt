package com.ritense.valtimo.backend.plugin.audit

import com.ritense.audit.domain.AuditRecord
import com.ritense.audit.service.AuditService
import com.ritense.authorization.AuthorizationContext
import com.ritense.authorization.AuthorizationService
import com.ritense.authorization.request.EntityAuthorizationRequest
import com.ritense.document.domain.Document
import com.ritense.document.domain.impl.JsonSchemaDocument
import com.ritense.document.domain.impl.event.JsonSchemaDocumentCreatedEvent
import com.ritense.document.domain.impl.event.JsonSchemaDocumentModifiedEvent
import com.ritense.document.event.DocumentAssigneeChangedEvent
import com.ritense.document.event.DocumentUnassignedEvent
import com.ritense.document.service.JsonSchemaDocumentActionProvider.VIEW
import com.ritense.document.service.impl.JsonSchemaDocumentService
import com.ritense.processdocument.event.BesluitAddedEvent
import com.ritense.processdocument.service.ProcessDocumentAuditService
import com.ritense.valtimo.camunda.processaudit.ProcessEndedEvent
import com.ritense.valtimo.camunda.processaudit.ProcessStartedEvent
import com.ritense.valtimo.contract.document.event.DocumentRelatedFileAddedEvent
import com.ritense.valtimo.contract.document.event.DocumentRelatedFileRemovedEvent
import com.ritense.valtimo.contract.documentgeneration.event.DossierDocumentGeneratedEvent
import com.ritense.valtimo.contract.event.TaskAssignedEvent
import com.ritense.valtimo.contract.event.TaskCompletedEvent
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

class PublicTaskCamundaProcessJsonSchemaDocumentAuditService(
    private val auditService: AuditService,
    private val documentService: JsonSchemaDocumentService,
    private val authorizationService: AuthorizationService
): ProcessDocumentAuditService {

    override fun getAuditLog(
        id: Document.Id,
        pageable: Pageable
    ): Page<AuditRecord> {
        val eventTypes = listOf(
            JsonSchemaDocumentCreatedEvent::class.java,
            JsonSchemaDocumentModifiedEvent::class.java,
            TaskAssignedEvent::class.java,
            TaskCompletedEvent::class.java,
            PublicTaskCompletedEvent::class.java,
            ProcessStartedEvent::class.java,
            ProcessEndedEvent::class.java,
            DossierDocumentGeneratedEvent::class.java,
            DocumentRelatedFileAddedEvent::class.java,
            DocumentRelatedFileRemovedEvent::class.java,
            BesluitAddedEvent::class.java,
            DocumentAssigneeChangedEvent::class.java,
            DocumentUnassignedEvent::class.java
        )

        val document = documentService.getDocumentBy(id)
        authorizationService.requirePermission(
            EntityAuthorizationRequest(
                JsonSchemaDocument::class.java,
                VIEW,
                document
            )
        )

        return AuthorizationContext.runWithoutAuthorization {
            auditService.findByEventAndDocumentId(eventTypes, UUID.fromString(id.toString()), pageable)
        }
    }
}