package com.ritense.valtimo.backend.plugin.audit

import com.ritense.valtimo.contract.audit.*
import java.time.LocalDateTime
import java.util.*

data class PublicTaskCompletedEvent(
    private val id: UUID,
    private val origin: String,
    private val occurredOn: LocalDateTime,
    private val user: String,
    private val assignee: String?,
    private val createdOn: LocalDateTime,
    private val taskId: String,
    private val taskName: String,
    private val processDefinitionId: String,
    private val processInstanceId: String,
    private val variables: Map<String, Any>,
    private val businessKey: String?
) : AuditMetaData(id, origin, occurredOn, user), AuditEvent, TaskIdentity, TaskMetaData, ProcessIdentity, VariableScope {

    init {
        businessKey?.let {
            require(it.isNotEmpty()) { "businessKey cannot be empty" }
        }
    }

    override fun getProcessDefinitionId() = processDefinitionId
    override fun getProcessInstanceId() = processInstanceId
    override fun createdOn(): LocalDateTime = createdOn
    override fun getAssignee() = assignee
    override fun getTaskId(): String = taskId
    override fun getTaskName() = taskName
    override fun getVariables() = variables
    override fun getBusinessKey() = businessKey
    override fun getDocumentId(): UUID? = try {
        UUID.fromString(businessKey)
    } catch (e: IllegalArgumentException) {
        null
    }
}
