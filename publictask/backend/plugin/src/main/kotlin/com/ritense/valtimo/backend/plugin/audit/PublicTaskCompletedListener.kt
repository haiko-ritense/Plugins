package com.ritense.valtimo.backend.plugin.audit

import com.ritense.valtimo.backend.plugin.repository.PublicTaskRepository
import com.ritense.valtimo.camunda.TaskCompletedListener
import com.ritense.valtimo.contract.audit.utils.AuditHelper
import com.ritense.valtimo.contract.event.TaskCompletedEvent
import com.ritense.valtimo.contract.utils.RequestHelper
import org.camunda.bpm.engine.delegate.DelegateTask
import org.springframework.context.ApplicationEventPublisher
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

class PublicTaskCompletedListener(
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val publicTaskRepository: PublicTaskRepository
): TaskCompletedListener(applicationEventPublisher) {

    override fun notify(delegateTask: DelegateTask) {
        with(publicTaskRepository.findAll()) {
            if (this.any { it.isCompletedByPublicTask && it.userTaskId.toString() == delegateTask.id }) {
                val assignee = this.first { it.userTaskId.toString() == delegateTask.id }.assigneeCandidateContactData
                applicationEventPublisher.publishEvent(
                    PublicTaskCompletedEvent(
                        id = UUID.randomUUID(),
                        origin = RequestHelper.getOrigin(),
                        occurredOn = LocalDateTime.now(),
                        user = assignee,
                        assignee = delegateTask.assignee,
                        createdOn = LocalDateTime.ofInstant(delegateTask.createTime.toInstant(), ZoneId.systemDefault()),
                        taskId = delegateTask.id,
                        taskName = delegateTask.name,
                        processDefinitionId = delegateTask.processDefinitionId,
                        processInstanceId = delegateTask.processInstanceId,
                        variables = delegateTask.variablesTyped,
                        businessKey = delegateTask.execution.processBusinessKey
                    )
                )
            } else {
                applicationEventPublisher.publishEvent(
                    TaskCompletedEvent(
                        UUID.randomUUID(),
                        RequestHelper.getOrigin(),
                        LocalDateTime.now(),
                        AuditHelper.getActor(),
                        delegateTask.assignee,
                        LocalDateTime.ofInstant(delegateTask.createTime.toInstant(), ZoneId.systemDefault()),
                        delegateTask.id,
                        delegateTask.name,
                        delegateTask.processDefinitionId,
                        delegateTask.processInstanceId,
                        delegateTask.variablesTyped,
                        delegateTask.execution.processBusinessKey
                    )
                )
            }
        }
    }
}
