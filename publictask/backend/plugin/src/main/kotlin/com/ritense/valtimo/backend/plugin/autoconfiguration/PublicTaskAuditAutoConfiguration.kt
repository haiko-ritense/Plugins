package com.ritense.valtimo.backend.plugin.autoconfiguration

import com.ritense.audit.service.AuditService
import com.ritense.authorization.AuthorizationService
import com.ritense.document.service.impl.JsonSchemaDocumentService
import com.ritense.valtimo.backend.plugin.audit.PublicTaskCamundaProcessJsonSchemaDocumentAuditService
import com.ritense.valtimo.backend.plugin.audit.PublicTaskCompletedListener
import com.ritense.valtimo.backend.plugin.repository.PublicTaskRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class PublicTaskAuditAutoConfiguration {

    @Bean
    @Primary
    fun taskCompletedListener(
        applicationEventPublisher: ApplicationEventPublisher,
        publicTaskRepository: PublicTaskRepository
    ) = PublicTaskCompletedListener(
        applicationEventPublisher = applicationEventPublisher,
        publicTaskRepository = publicTaskRepository
    )

    @Bean
    @Primary
    fun processDocumentAuditService(
        auditService: AuditService,
        documentService: JsonSchemaDocumentService,
         authorizationService: AuthorizationService
    ) = PublicTaskCamundaProcessJsonSchemaDocumentAuditService(
        auditService = auditService,
        documentService = documentService,
        authorizationService = authorizationService
    )
}