package com.ritense.valtimo.backend.plugin.service

import com.ritense.form.domain.FormTaskOpenResultProperties
import com.ritense.processlink.exception.ProcessLinkNotFoundException
import com.ritense.processlink.service.ProcessLinkActivityService
import com.ritense.valtimo.backend.plugin.domain.PublicTaskEntity
import com.ritense.valtimo.backend.plugin.domain.PublicTaskData
import com.ritense.valtimo.backend.plugin.htmlrenderer.service.HtmlRenderService
import com.ritense.valtimo.backend.plugin.repository.PublicTaskRepository
import mu.KotlinLogging
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.DelegateTask
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.UUID

class PublicTaskService(
    private val publicTaskRepository: PublicTaskRepository,
    private val runtimeService: RuntimeService,
    private val processLinkActivityService: ProcessLinkActivityService,
    private val htmlRenderService: HtmlRenderService
) {

    @Value("\${valtimo.url}")
    private lateinit var baseUrl: String

    fun startNotifyAssigneeCandidateProcess(task: DelegateTask) {
        runtimeService.createMessageCorrelation(NOTIFY_ASSIGNEE_PROCESS_MESSAGE_NAME)
            .processInstanceId(task.processInstanceId)
            .setVariables(mapOf("userTaskId" to task.id))
            .processInstanceBusinessKey(task.execution.processBusinessKey)
            .correlateAll()
    }

    fun createAndSendPublicTaskUrl(
        execution: DelegateExecution,
        publicTaskData: PublicTaskData
    ) {
        val publicTaskUrl = "$baseUrl/$PUBLIC_TASK_URL?publicTaskId=${publicTaskData.publicTaskId}"

        execution.setVariable("assigneeCandidateContactData", publicTaskData.assigneeCandidateContactData)
        execution.setVariable("url", publicTaskUrl)

        savePublicTaskEntity(publicTaskData)
    }

    fun createPublicTaskHtml(publicTaskId: String): ResponseEntity<String> {

        val userTaskId = publicTaskRepository.getReferenceById(UUID.fromString(publicTaskId)).userTaskId

        val camundaTaskData = try {
            processLinkActivityService.openTask(userTaskId).properties
        } catch (e: Exception) {
            return when (e) {
                is ProcessLinkNotFoundException, is NullPointerException -> {
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body("This is not the task you're looking for")
                }

                else -> {
                    ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Something went wrong, try again (later) or contact your administrator")
                }
            }
        }

        val formIoForm = (camundaTaskData as FormTaskOpenResultProperties).prefilledForm.toPrettyString()
        val formHtml = htmlRenderService.generatePublicTaskHtml(
            fileName = PUBLIC_TASK_FILE_NAME,
            variables = mapOf(
                "form_io_form" to formIoForm,
                "public_task_url" to "$baseUrl$PUBLIC_TASK_URL?publicTaskId=$publicTaskId"
            )
        )

        return ResponseEntity(formHtml, HttpStatus.OK)
    }

    fun completeUserTaskWithPublicTaskSubmission(submission: Map<String, Any>): ResponseEntity<String> {
        // TODO: build it ;)
        return ResponseEntity("Your response has been submitted", HttpStatus.OK)
    }

    private fun savePublicTaskEntity(publicTaskData: PublicTaskData) {
        publicTaskRepository.save(
            PublicTaskEntity(
                publicTaskId = publicTaskData.publicTaskId,
                userTaskId = publicTaskData.userTaskId,
                assigneeCandidateContactData = publicTaskData.assigneeCandidateContactData,
                timeToLive = publicTaskData.timeToLive,
                isCompletedByPublicTask = publicTaskData.isCompletedByPublicTask
            )
        )
    }

    companion object {
        val logger = KotlinLogging.logger {}

        private const val PUBLIC_TASK_URL = "/api/v1/public-task"

        private const val NOTIFY_ASSIGNEE_PROCESS_MESSAGE_NAME = "startNotifyAssigneeMessage"

        private const val PUBLIC_TASK_FILE_NAME = "public_task_html"
    }
}