package com.ritense.valtimo.backend.plugin.service

import com.fasterxml.jackson.databind.JsonNode
import com.ritense.form.domain.FormTaskOpenResultProperties
import com.ritense.form.service.impl.DefaultFormSubmissionService
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
import java.time.LocalDate
import java.util.UUID

class PublicTaskService(
    private val publicTaskRepository: PublicTaskRepository,
    private val runtimeService: RuntimeService,
    private val processLinkActivityService: ProcessLinkActivityService,
    private val htmlRenderService: HtmlRenderService,
    private val defaultFormSubmissionService: DefaultFormSubmissionService
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
            processLinkActivityService.openTask(userTaskId).properties as FormTaskOpenResultProperties
        } catch (e: Exception) {
            return taskNotAvailableResponse(e)
        }

        val formIoForm = camundaTaskData.prefilledForm.toPrettyString()
        val formHtml = htmlRenderService.generatePublicTaskHtml(
            fileName = PUBLIC_TASK_FILE_NAME,
            variables = mapOf(
                "form_io_form" to formIoForm,
                "public_task_url" to "$baseUrl$PUBLIC_TASK_URL?publicTaskId=$publicTaskId"
            )
        )

        return ResponseEntity(formHtml, HttpStatus.OK)
    }

    fun completeUserTaskWithPublicTaskSubmission(
        publicTaskId: String,
        submission: JsonNode
    ): ResponseEntity<String> {

        val publicTaskEntity = publicTaskRepository.getReferenceById(UUID.fromString(publicTaskId))

        if (LocalDate.parse(publicTaskEntity.taskExpirationDate).isBefore(LocalDate.now())) return TASK_NOT_AVAILABLE_ERROR

        val camundaTask = try {
            processLinkActivityService.openTask(publicTaskEntity.userTaskId)
        } catch (e: Exception) {
            return taskNotAvailableResponse(e)
        }

        val formSubmissionResult = defaultFormSubmissionService.handleSubmission(
            processLinkId = camundaTask.processLinkId,
            formData = submission,
            documentId = "3b7d5a8a-d0dd-4a8b-b31a-27837cebbe6e",
            taskInstanceId = publicTaskEntity.userTaskId.toString()
        )

        if (formSubmissionResult.errors().isNotEmpty()) SERVER_SIDE_ERROR

        publicTaskRepository.save(publicTaskEntity.copy(isCompletedByPublicTask = true))

        return ResponseEntity("Your response has been submitted", HttpStatus.OK)
    }

    private fun savePublicTaskEntity(publicTaskData: PublicTaskData) {
        publicTaskRepository.save(
            PublicTaskEntity(
                publicTaskId = publicTaskData.publicTaskId,
                userTaskId = publicTaskData.userTaskId,
                processBusinessKey = publicTaskData.processBusinessKey,
                assigneeCandidateContactData = publicTaskData.assigneeCandidateContactData,
                taskExpirationDate = publicTaskData.taskExpirationDate,
                isCompletedByPublicTask = publicTaskData.isCompletedByPublicTask
            )
        )
    }

    private fun taskNotAvailableResponse(e: Exception): ResponseEntity<String> = when (e) {
        is ProcessLinkNotFoundException, is NullPointerException -> TASK_NOT_AVAILABLE_ERROR
        else -> SERVER_SIDE_ERROR
    }

    companion object {
        val logger = KotlinLogging.logger {}

        private const val PUBLIC_TASK_URL = "/api/v1/public-task"

        private const val NOTIFY_ASSIGNEE_PROCESS_MESSAGE_NAME = "startNotifyAssigneeMessage"

        private const val PUBLIC_TASK_FILE_NAME = "public_task_html"

        private val SERVER_SIDE_ERROR = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Something went wrong, try again (later) or contact your administrator")

        private val TASK_NOT_AVAILABLE_ERROR = ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("This task does not exist (anymore) or is already completed.")
    }
}