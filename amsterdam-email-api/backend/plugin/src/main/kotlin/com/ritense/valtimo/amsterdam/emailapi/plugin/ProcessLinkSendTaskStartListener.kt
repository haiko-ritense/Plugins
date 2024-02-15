package com.ritense.valtimo.amsterdam.emailapi.plugin

import com.ritense.plugin.domain.ActivityType
import com.ritense.plugin.repository.PluginProcessLinkRepository
import com.ritense.plugin.service.PluginService
import org.camunda.bpm.engine.ActivityTypes
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.ExecutionListener
import org.camunda.bpm.extension.reactor.bus.CamundaSelector
import org.camunda.bpm.extension.reactor.spring.listener.ReactorExecutionListener
import org.springframework.transaction.annotation.Transactional

@CamundaSelector(type = ActivityTypes.TASK_SEND_TASK, event = ExecutionListener.EVENTNAME_START)
open class ProcessLinkSendTaskStartListener(
    private val pluginProcessLinkRepository: PluginProcessLinkRepository,
    private val pluginService: PluginService,
) : ReactorExecutionListener() {

    @Transactional
    override fun notify(execution: DelegateExecution) {
        val pluginProcessLinks = pluginProcessLinkRepository.findByProcessDefinitionIdAndActivityIdAndActivityType(
            execution.processDefinitionId,
            execution.currentActivityId,
            ActivityType.SEND_TASK
        )

        pluginProcessLinks.forEach { pluginProcessLink ->
            pluginService.invoke(execution, pluginProcessLink)
        }
    }
}
